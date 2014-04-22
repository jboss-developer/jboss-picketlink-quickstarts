/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.gr.project.security.authorization;

import static org.picketlink.idm.model.basic.BasicModel.getRole;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.deltaspike.security.api.authorization.AccessDeniedException;
import org.apache.deltaspike.security.api.authorization.Secures;
import org.picketlink.Identity;
import org.picketlink.Identity.Stateless;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Role;

import com.gr.project.security.authorization.annotation.UserLoggedIn;
import com.gr.project.security.model.ApplicationRole;

/**
 * <p>
 *  Provides authorization services for the application.
 * </p>
 * 
 * @author Pedro Silva
 * 
 */
@RequestScoped
public class AuthorizationManager {

    private Map<String, String[]> roleProtectedResources = new HashMap<String, String[]>();
    
    @Inject
    @Named("resource.wildcard")
    private String ANY_RESOURCE_PATTERN;

    @Inject
    @Stateless
    private Identity identity;
    

    @Inject
    private Instance<IdentityManager> identityManager;

    @Inject
    private Instance<RelationshipManager> relationshipManager;

    @PostConstruct
    public void init() {
        // let's configure which URIs should be protected
        this.roleProtectedResources.put("/admin/*", new String[] { "Administrator" });
    }

    /**
     * <p>
     * Check if a method or type annotated with the {@link com.gr.project.security.authorization.annotation.UserLoggedIn} is being access by an authenticated user. This method
     * is called before the annotated method is called.
     * </p>
     * 
     * @return
     */
    @Secures
    @UserLoggedIn
    public boolean isUserLoggedIn() {
        return identity.isLoggedIn();
    }

    /**
     * <p>
     *  This authorization method provides the validation logic for resources annotated with the security annotation {@link AllowedRole}.
     * </p>
     * <p>
     *  Note that this method is also annotated with {@link Secures}, which is an annotation from Apache DeltaSpike.
     *  This annotation tells the @{link SecurityInterceptor} that this method must be called before the execution of
     *  methods annotated with {@checkDeclaredRoles} in order to perform authorization checks.
     * </p>
     * 
     * @param invocationContext
     * @param manager
     * @return true if the user can execute the method or class
     * @throws Exception
     */
    @Secures
    @AllowedRole
    public boolean checkDeclaredRoles(InvocationContext invocationContext, BeanManager manager) throws Exception {
        // administrators can access everything
        if (hasRole(ApplicationRole.ADMINISTRATOR.name())) {
            return true;
        }

        Object targetBean = invocationContext.getTarget();

        AllowedRole declareRoles = targetBean.getClass().getAnnotation(AllowedRole.class);

        if (declareRoles == null) {
            declareRoles = invocationContext.getMethod().getAnnotation(AllowedRole.class);
        }

        ApplicationRole[] requiredRoles = declareRoles.value();

        if (requiredRoles.length == 0) {
            throw new IllegalArgumentException("@DeclaredRoles does not define any role.");
        }

        for (ApplicationRole requiredRole: requiredRoles) {
            if (hasRole(requiredRole.name())) {
                return true;
            }
        }

        return false;
    }
    
    public boolean isAdmin() {
        if (isUserLoggedIn()) {
            IdentityManager identityManager = getIdentityManager();
            RelationshipManager relationshipManager = getRelationshipManager();

            return BasicModel.hasRole(relationshipManager, identity.getAccount(), BasicModel.getRole(identityManager, "Administrator"));
        }

        return false;
    }

    /**
     * <p>
     * Check if the current user is allowed to access the requested resource.
     * </p>
     * 
     * @param httpRequest
     * @throws AccessDeniedException If the request is not allowed considering the resource permissions.
     */
    public boolean isAllowed(HttpServletRequest httpRequest) throws AccessDeniedException {
        final String requestURI = httpRequest.getRequestURI();

        Set<Entry<String, String[]>> entrySet = this.roleProtectedResources.entrySet();

        for (Entry<String, String[]> entry : entrySet) {
            if (matches(entry.getKey(), requestURI)) {
                Identity identity = getIdentity();

                if (!identity.isLoggedIn()) {	
                    return false;
                } else {
                	
                    String[] roles = entry.getValue();

                    for (String roleName : roles) {
                        IdentityManager identityManager = getIdentityManager();

                        Role role = BasicModel.getRole(identityManager, roleName.trim());

                        if (role == null) {
                            throw new IllegalStateException("The specified role does not exists [" + role
                                    + "]. Check your configuration.");
                        }

                        if (!BasicModel.hasRole(getRelationshipManager(), identity.getAccount(), role)) {
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }

    private RelationshipManager getRelationshipManager() {
        return this.relationshipManager.get();
    }

    /**
     * <p>
     * Checks if the provided URI matches the specified pattern.
     * </p>
     * 
     * @param uri
     * @param pattern
     * @return
     */
    private boolean matches(String pattern, String uri) {
        if (pattern.equals(ANY_RESOURCE_PATTERN)) {
            return true;
        }

        if (pattern.equals(uri)) {
            return true;
        }

        if (pattern.endsWith(ANY_RESOURCE_PATTERN)) {
            String formattedPattern = pattern.replaceAll("/[*]", "/");

            if (uri.contains(formattedPattern)) {
                return true;
            }
        }

        if (pattern.equals("*")) {
            return true;
        } else {
            return (pattern.startsWith(ANY_RESOURCE_PATTERN) && uri.endsWith(pattern.substring(
                    ANY_RESOURCE_PATTERN.length() + 1, pattern.length())));
        }
    }

    public boolean hasRole(String roleName) {
        Account account = getIdentity().getAccount();
        Role role = getRole(this.identityManager.get(), roleName);
        
        if(role == null)
        	return false;

        return BasicModel.hasRole(this.relationshipManager.get(), account, role);
    }

    private IdentityManager getIdentityManager() {
        return this.identityManager.get();
    }

    private Identity getIdentity() {
        return this.identity;
    }

}