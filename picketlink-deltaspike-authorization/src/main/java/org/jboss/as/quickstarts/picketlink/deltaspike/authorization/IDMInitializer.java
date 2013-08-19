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
package org.jboss.as.quickstarts.picketlink.deltaspike.authorization;

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

import static org.picketlink.idm.model.basic.BasicModel.*;

/**
 * This startup bean creates the default users, groups and roles when the application is started.
 * 
 * @author Shane Bryzak
 */
@Singleton
@Startup
public class IDMInitializer {

    @Inject
    private PartitionManager partitionManager;

    @PostConstruct
    public void create() {

        // Create user john
        User john = new User("john");
        john.setEmail("john@acme.com");
        john.setFirstName("John");
        john.setLastName("Smith");

        IdentityManager identityManager = this.partitionManager.createIdentityManager();

        identityManager.add(john);
        identityManager.updateCredential(john, new Password("demo"));

        // Create user mary
        User mary = new User("mary");
        mary.setEmail("mary@acme.com");
        mary.setFirstName("Mary");
        mary.setLastName("Jones");
        identityManager.add(mary);
        identityManager.updateCredential(mary, new Password("demo"));

        // Create role "employee"
        Role employee = new Role("employee");
        identityManager.add(employee);

        // Create application role "superuser"
        Role admin = new Role("admin");
        identityManager.add(admin);

        RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();

        // Grant the "employee" application role to john
        grantRole(relationshipManager, john, employee);

        // Grant the "admin" application role to jane
        grantRole(relationshipManager, mary, admin);
    }
}
