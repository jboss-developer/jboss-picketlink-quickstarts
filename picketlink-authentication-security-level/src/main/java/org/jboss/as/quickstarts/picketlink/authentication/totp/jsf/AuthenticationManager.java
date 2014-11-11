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
package org.jboss.as.quickstarts.picketlink.authentication.totp.jsf;

import org.picketlink.Identity;
import org.picketlink.common.util.Base32;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.TOTPCredentials;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.User;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import static org.jboss.as.quickstarts.picketlink.authentication.totp.jsf.SecurityInitializer.TOTP_SECRET_USER_ATTRIBUTE;
import static org.picketlink.Identity.AuthenticationResult;

/**
 * @author Pedro Igor
 */
@Named
@RequestScoped
public class AuthenticationManager {

    @Inject
    private Identity identity;

    @Inject
    private DefaultLoginCredentials loginCredentials;

    @Inject
    private FacesContext facesContext;

    private String token;

    public String performTwoFactorAuthentication() {
        if (this.token == null || this.token.isEmpty()) {
            this.facesContext.addMessage(null, new FacesMessage(
                "You have configured two-factor authentication. Please, provide your token."));
        } else {
            TOTPCredentials credential = new TOTPCredentials();
            User user = (User) this.identity.getAccount();

            credential.setUsername(user.getLoginName());
            credential.setPassword(new Password(this.loginCredentials.getPassword()));
            credential.setToken(this.token);

            // we override the credential to set a totp credential
            this.loginCredentials.setCredential(credential);

            AuthenticationResult status = identity.login();

            if (AuthenticationResult.SUCCESS.equals(status)) {
                return "/home.xhtml";
            }
        }

        this.facesContext.addMessage(null, new FacesMessage(
            "Authentication failed. Please, try again"));

        return null;
    }

    public String getTotpSecret() {
        Account account = this.identity.getAccount();
        Attribute<String> totpSecretAttribute = account.getAttribute(TOTP_SECRET_USER_ATTRIBUTE);
        String totpSecret = totpSecretAttribute.getValue();

        return Base32.encode(totpSecret.getBytes());
    }

    public String logout() {
        this.identity.logout();
        return "/login.xhtml";
    }


    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}