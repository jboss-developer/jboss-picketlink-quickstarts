/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.picketlink.idm.ldap;

import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Role;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

import static org.picketlink.idm.model.basic.BasicModel.*;

/**
 * <p>This bean class centralizes all authorization services for this application.</p>
 * 
 * @author pedroigor
 * 
 */
@ApplicationScoped
@Named
public class AuthorizationManager {

    @Inject
    private Instance<Identity> identityInstance;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private RelationshipManager relationshipManager;

    public boolean isRisksManagementAllowed() {
        return isAdministrator() || (isProjectManager());
    }

    public boolean isTimesheetAllowed() {
        return isAdministrator() || (isProjectManager() || isDeveloper());
    }

    public boolean isSystemAdministrationAllowed() {
        return isAdministrator();
    }

    public boolean isProjectManager() {
        return hasRole(ApplicationRole.PROJECT_MANAGER);
    }

    public boolean isAdministrator() {
        return hasRole(ApplicationRole.ADMINISTRATOR);
    }

    public boolean isDeveloper() {
        return hasRole(ApplicationRole.DEVELOPER);
    }

    private Identity getIdentity() {
        return this.identityInstance.get();
    }

    /**
     * <p>Checks if the current user is granted with the given role.</p>
     *
     * @param applicationRole
     * @return
     */
    private boolean hasRole(ApplicationRole applicationRole) {
        Account agent = getIdentity().getAccount();
        Role role = getRole(this.identityManager, applicationRole.name());

        return BasicModel.hasRole(this.relationshipManager, agent, role);
    }
}
