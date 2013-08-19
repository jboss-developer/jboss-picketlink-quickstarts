/**
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
package org.jboss.as.quickstarts.picketlink.authorization.rest.rbac;

import org.picketlink.Identity;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.Grant;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.query.RelationshipQuery;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  <p>
 *      Simple JAX-RS Authentication Service that supports username/password credential.
 *  </p>
 */
@Path("/")
public class AuthenticationService {

    @Inject
    private Identity identity;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private RelationshipManager relationshipManager;

    @Inject
    private DefaultLoginCredentials credentials;

    @POST
    @Path("/authenticate")
    public Response authenticate(DefaultLoginCredentials credential) {
        if (!this.identity.isLoggedIn()) {
            this.credentials.setUserId(credential.getUserId());
            this.credentials.setPassword(credential.getPassword());
            this.identity.login();
        }

        Account account = this.identity.getAccount();
        List<Role> roles = getUserRoles(account);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(account, roles);

        return Response.ok().entity(authenticationResponse).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    private List<Role> getUserRoles(Account account) {
        RelationshipQuery<Grant> query = this.relationshipManager.createRelationshipQuery(Grant.class);

        query.setParameter(Grant.ASSIGNEE, account);

        List<Role> roles = new ArrayList<Role>();

        for (Grant grant: query.getResultList()) {
            roles.add(grant.getRole());
        }

        return roles;
    }

    private class AuthenticationResponse implements Serializable {

        private static final long serialVersionUID = 1297387771821869087L;

        private Account account;
        private List<Role> roles;

        public AuthenticationResponse(Account account, List<Role> roles) {
            this.account = account;
            this.roles = roles;
        }

        public Account getAccount() {
            return this.account;
        }

        public List<Role> getRoles() {
            return this.roles;
        }
    }

}
