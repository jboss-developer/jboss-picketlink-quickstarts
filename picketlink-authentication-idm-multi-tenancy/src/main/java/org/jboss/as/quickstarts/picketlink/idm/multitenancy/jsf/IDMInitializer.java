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
package org.jboss.as.quickstarts.picketlink.idm.multitenancy.jsf;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.idm.model.basic.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class IDMInitializer {

    @Inject
    private PartitionManager partitionManager;

    /**
     * <p>Creates some default users for each realm/company.</p>
     */
    @PostConstruct
    public void createDefaultUsers() {
        createUserForRealm(Resources.REALM.acme.name(), "bugs");
        createUserForRealm(Resources.REALM.umbrella.name(), "jill");
        createUserForRealm(Resources.REALM.wayne.name(), "bruce");
    }

    private void createUserForRealm(String realmName, String loginName) {
        Realm partition = this.partitionManager.getPartition(Realm.class, realmName);

        if (partition == null) {
            partition = new Realm(realmName);
            this.partitionManager.add(partition);
        }

        IdentityManager identityManager = this.partitionManager.createIdentityManager(partition);

        User user = new User(loginName);
        Password password = new Password(user.getLoginName() + "123");

        identityManager.add(user);
        identityManager.updateCredential(user, password);
    }

}
