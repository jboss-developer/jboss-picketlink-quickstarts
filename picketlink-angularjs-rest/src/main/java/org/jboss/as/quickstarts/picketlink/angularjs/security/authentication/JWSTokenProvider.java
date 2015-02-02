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
package org.jboss.as.quickstarts.picketlink.angularjs.security.authentication;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Token;
import org.picketlink.idm.credential.storage.TokenCredentialStorage;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.json.jose.JWSBuilder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.UUID;

/**
 * <p>This is a simple {@link org.picketlink.idm.credential.Token.Provider} that manages a specific token type. In this case the
 * type is {@link org.jboss.as.quickstarts.picketlink.angularjs.security.authentication.JWSToken}.</p>
 *
 * @author Pedro Igor
 *
 * @see org.jboss.as.quickstarts.picketlink.angularjs.security.authentication.JWSToken
 */
@Stateless
public class JWSTokenProvider implements Token.Provider<JWSToken> {

    @Inject
    private PartitionManager partitionManager;

    @Override
    public JWSToken issue(Account account) {
        TokenCredentialStorage tokenCredentialStorage = getIdentityManager().retrieveCurrentCredential(account, TokenCredentialStorage.class);
        JWSToken token;

        if (tokenCredentialStorage == null) {
            JWSBuilder builder = new JWSBuilder();

            byte[] privateKey = getPrivateKey();

            // here we construct a JWS signed with the private key provided by the partition.
            builder
                    .id(UUID.randomUUID().toString())
                    .rsa256(privateKey)
                    .issuer(account.getPartition().getName())
                    .issuedAt(getCurrentTime())
                    .subject(account.getId())
                    .expiration(getCurrentTime() + (5 * 60))
                    .notBefore(getCurrentTime());

            token = new JWSToken(builder.build().encode());

            // now we update the account with the token previously issued by this provider.
            getIdentityManager().updateCredential(account, token);
        } else {
            token = new JWSToken(tokenCredentialStorage.getToken());

        }

        return token;
    }

    @Override
    public JWSToken renew(Account account, JWSToken renewToken) {
        return issue(account);
    }

    @Override
    public void invalidate(Account account) {
        getIdentityManager().removeCredential(account, TokenCredentialStorage.class);
    }

    @Override
    public Class<JWSToken> getTokenType() {
        return JWSToken.class;
    }

    private byte[] getPrivateKey() {
        return getPartition().<byte[]>getAttribute("PrivateKey").getValue();
    }

    private int getCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    private Realm getPartition() {
        return partitionManager.getPartition(Realm.class, Realm.DEFAULT_REALM);
    }

    private IdentityManager getIdentityManager() {
        return this.partitionManager.createIdentityManager(getPartition());
    }
}
