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
package org.jboss.as.quickstarts.picketlink.authorization.idm.jpa;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.sample.Group;
import org.picketlink.idm.model.sample.Role;
import org.picketlink.idm.model.sample.User;
import static org.picketlink.idm.model.sample.SampleModel.addToGroup;
import static org.picketlink.idm.model.sample.SampleModel.grantGroupRole;
import static org.picketlink.idm.model.sample.SampleModel.grantRole;

/**
 * This startup bean creates a number of default users, groups and roles when the application is started.
 * 
 * @author Shane Bryzak
 */
@Singleton
@Startup
public class IDMInitializer {

    @Inject
    private IdentityManager identityManager;

    @Inject
    private RelationshipManager relationshipManager;

    @PostConstruct
    public void create() {

        // Create user john
        User john = new User("john");
        john.setEmail("john@acme.com");
        john.setFirstName("John");
        john.setLastName("Smith");
        identityManager.add(john);
        identityManager.updateCredential(john, new Password("demo"));

        // Create user mary
        User mary = new User("mary");
        mary.setEmail("mary@acme.com");
        mary.setFirstName("Mary");
        mary.setLastName("Jones");
        identityManager.add(mary);
        identityManager.updateCredential(mary, new Password("demo"));

        // Create user jane
        User jane = new User("jane");
        jane.setEmail("jane@acme.com");
        jane.setFirstName("Jane");
        jane.setLastName("Doe");
        identityManager.add(jane);
        identityManager.updateCredential(jane, new Password("demo"));

        // Create role "manager"
        Role manager = new Role("manager");
        identityManager.add(manager);

        // Create application role "superuser"
        Role superuser = new Role("superuser");
        identityManager.add(superuser);

        // Create group "sales"
        Group sales = new Group("sales");
        identityManager.add(sales);

        // Make john a member of the "sales" group
        addToGroup(this.relationshipManager, john, sales);

        // Make mary a manager of the "sales" group
        grantGroupRole(this.relationshipManager, mary, manager, sales);

        // Grant the "superuser" application role to jane
        grantRole(this.relationshipManager, jane, superuser);
    }
}
