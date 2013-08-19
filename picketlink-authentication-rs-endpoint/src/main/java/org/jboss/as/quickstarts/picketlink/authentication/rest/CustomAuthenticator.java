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
package org.jboss.as.quickstarts.picketlink.authentication.rest;

import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * <p>A simple authenticator that supports two credential types: username/password or a simple token.</p>
 */
@RequestScoped
@PicketLink
public class CustomAuthenticator extends BaseAuthenticator {

    @Inject
    private DefaultLoginCredentials credentials;

    @Override
    public void authenticate() {
        if (this.credentials.getCredential() == null) {
            return;
        }

        if (isUsernamePasswordCredential()) {
            String userId = this.credentials.getUserId();
            Password password = (Password) this.credentials.getCredential();

            if (userId.equals("jane") && String.valueOf(password.getValue()).equals("abcd1234")) {
                successfulAuthentication();
            }
        } else if (isCustomCredential()) {
            SimpleTokenCredential customCredential = (SimpleTokenCredential) this.credentials.getCredential();

            if (customCredential.getToken() != null && customCredential.getToken().equals("valid_token")) {
                successfulAuthentication();
            }
        }
    }

    private boolean isUsernamePasswordCredential() {
        return Password.class.equals(credentials.getCredential().getClass()) && credentials.getUserId() != null;
    }

    private boolean isCustomCredential() {
        return SimpleTokenCredential.class.equals(credentials.getCredential().getClass());
    }

    private User getDefaultUser() {
        return new User("jane");
    }

    private void successfulAuthentication() {
        setStatus(AuthenticationStatus.SUCCESS);
        setAccount(getDefaultUser());
    }

}
