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
package org.jboss.as.quickstarts.picketlink.authentication.totp.jsf;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.TOTPCredential;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.UUID;

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
public class SecurityInitializer {

    public static final String TOTP_SECRET_USER_ATTRIBUTE = "TOTP_SECRET_USER_ATTRIBUTE";

    @Inject
    private PartitionManager partitionManager;

    @PostConstruct
    public void create() {
        User user = new User("jane");

        user.setEmail("jane@doe.com");
        user.setFirstName("Jane");
        user.setLastName("Doe");

        IdentityManager identityManager = this.partitionManager.createIdentityManager();

        identityManager.add(user);

        updateCredentials(user, identityManager);
    }

    private void updateCredentials(Account account, IdentityManager identityManager) {
        String password = "abcd1234";

        identityManager.updateCredential(account, new Password(password));

        String totpSecret = UUID.randomUUID().toString();

        // we're only doing this to show you how you can retrieve the secret for your users.
        account.setAttribute(new Attribute<String>(TOTP_SECRET_USER_ATTRIBUTE, totpSecret));

        identityManager.update(account);

        TOTPCredential credential = new TOTPCredential(password, totpSecret);

        // now the account has a two-factor credential configured using TOTP tokens
        identityManager.updateCredential(account, credential);
    }

}
