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
package com.gr.project.security.authentication.credential;

import com.gr.project.security.model.MyUser;
import org.picketlink.idm.IdentityManagementException;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.handler.CredentialHandler;
import org.picketlink.idm.credential.handler.annotations.SupportsCredentials;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Partition;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.spi.CredentialStore;
import org.picketlink.idm.spi.IdentityContext;
import org.picketlink.json.JsonException;
import org.picketlink.json.jose.JWS;
import org.picketlink.json.jose.JWSBuilder;

import java.util.Date;
import java.util.List;

/**
 * @author Pedro Igor
 */
@SupportsCredentials(
    credentialClass = { TokenCredential.class, Token.class },
    credentialStorage = TokenCredentialStorage.class
)
public class TokenCredentialHandler<S extends CredentialStore<?>, V extends TokenCredential, U extends Token> implements CredentialHandler<S, V, U> {

    @Override
    public void setup(S identityStore) {
    }

    @Override
    public void validate(IdentityContext context, V credentials, S store) {
        credentials.setStatus(Credentials.Status.INVALID);

        Account account = getAccount(context, credentials);

        if (account != null) {
            if (account.isEnabled()) {
                TokenCredentialStorage storage = store.retrieveCurrentCredential(context, account, TokenCredentialStorage.class);

                if (storage.getToken().equals(credentials.getToken())) {
                    credentials.setStatus(Credentials.Status.VALID);
                    credentials.setValidatedAccount(account);
                }
            } else {
                credentials.setStatus(Credentials.Status.ACCOUNT_DISABLED);
            }
        }
    }

    @Override
    public void update(IdentityContext context, Account account, Token credential, @SuppressWarnings("rawtypes") CredentialStore store, Date effectiveDate, Date expiryDate) {
        TokenCredentialStorage tokenStorage = new TokenCredentialStorage();

        tokenStorage.setToken(credential.getToken());

        if (effectiveDate != null) {
            tokenStorage.setEffectiveDate(effectiveDate);
        }

        tokenStorage.setExpiryDate(expiryDate);

        store.storeCredential(context, account, tokenStorage);
    }

    private Account getAccount(IdentityContext context, TokenCredential credentials) {
        IdentityManager identityManager = getIdentityManager(context);
        IdentityQuery<MyUser> query = identityManager.createIdentityQuery(MyUser.class);
        JWS token = validateToken(context, credentials);

        if (token != null) {
            query.setParameter(MyUser.ID, token.getSubject());

            List<MyUser> result = query.getResultList();

            if (!result.isEmpty()) {
                return result.get(0);
            }
        }

        return null;
    }

    private JWS validateToken(IdentityContext context, TokenCredential credentials) {
        JWS token = null;

        try {
            token = new JWSBuilder().build(credentials.getToken(), getPublicKey(context)); // build and validate the token.
        } catch (JsonException ignore) {
        }

        return token;
    }

    private IdentityManager getIdentityManager(IdentityContext context) {
        IdentityManager identityManager = context.getParameter(IdentityManager.IDENTITY_MANAGER_CTX_PARAMETER);

        if (identityManager == null) {
            throw new IdentityManagementException("IdentityManager not set into context.");
        }

        return identityManager;
    }

    private byte[] getPublicKey(IdentityContext context) {
        Partition partition = context.getPartition();
        return partition.<byte[]>getAttribute("PublicKey").getValue();
    }
}
