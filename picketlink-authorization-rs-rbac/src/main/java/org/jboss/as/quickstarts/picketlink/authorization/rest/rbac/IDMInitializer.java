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

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import static org.jboss.as.quickstarts.picketlink.authorization.rest.rbac.ApplicationRole.*;
import static org.picketlink.idm.model.basic.BasicModel.*;

@Singleton
@Startup
public class IDMInitializer {

    @Inject
    private PartitionManager partitionManager;

    @PostConstruct
    public void createUsers() {
        createUser("admin", ADMINISTRATOR);
        createUser("john", PROJECT_MANAGER);
        createUser("kate", DEVELOPER);
    }

    private void createUser(String loginName, ApplicationRole roleName) {
        User user = new User(loginName);

        IdentityManager identityManager = this.partitionManager.createIdentityManager();

        identityManager.add(user);

        Password password = new Password(loginName + "123");

        identityManager.updateCredential(user, password);

        Role role = new Role(roleName.name());

        identityManager.add(role);

        RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();

        grantRole(relationshipManager, user, role);
    }
}
