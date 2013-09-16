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
package org.jboss.as.quickstarts.picketlink.authentication.idm.jsf;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.idm.model.basic.User;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

/**
 * This startup bean creates a default user account when the application is started. Since we are not
 * providing an IDM configuration in this example, PicketLink will default to using a file-based identity
 * store to persist user and other identity state.
 * 
 * 
 * @author Shane Bryzak
 */
@Singleton
@Startup
@ApplicationScoped
public class IDMInitializer {

    @Resource(mappedName = "java:/picketlink/PartitionManager")
    private PartitionManager partitionManager;

    @PostConstruct
    public void create() {
        Realm realm = this.partitionManager.getPartition(Realm.class, Realm.DEFAULT_REALM);

        if (realm == null) {
            this.partitionManager.add(new Realm(Realm.DEFAULT_REALM));
        }

        User user = new User("jane");

        user.setEmail("jane@doe.com");
        user.setFirstName("Jane");
        user.setLastName("Doe");

        IdentityManager identityManager = this.partitionManager.createIdentityManager();

        if (BasicModel.getUser(identityManager, user.getLoginName()) == null) {
            identityManager.add(user);
            identityManager.updateCredential(user, new Password("abcd1234"));
        }
    }

    @Produces
    @RequestScoped
    public IdentityManager produceIdentityManager() {
        return this.partitionManager.createIdentityManager();
    }

}
