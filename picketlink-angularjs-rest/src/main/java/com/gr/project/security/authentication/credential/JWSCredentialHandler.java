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
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.TokenCredential;
import org.picketlink.idm.credential.handler.TokenCredentialHandler;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Partition;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.spi.IdentityContext;
import org.picketlink.json.JsonException;
import org.picketlink.json.jose.JWS;
import org.picketlink.json.jose.JWSBuilder;

import java.util.List;

import static org.picketlink.idm.IDMMessages.MESSAGES;

/**
 * @author Pedro Igor
 */
public class JWSCredentialHandler extends TokenCredentialHandler {

    @Override
    protected Account getAccount(IdentityContext context, TokenCredential credentials) {
        JWS jws = unmarshall(context, credentials);

        if (jws != null) {
            IdentityManager identityManager = getIdentityManager(context);

            IdentityQuery<MyUser> query = identityManager.createIdentityQuery(MyUser.class);

            query.setParameter(MyUser.ID, jws.getSubject());

            List<MyUser> result = query.getResultList();

            if (!result.isEmpty()) {
                return result.get(0);
            }
        }

        return null;
    }

    private JWS unmarshall(IdentityContext context, TokenCredential credentials) {
        try {
            return new JWSBuilder().build(credentials.getToken(), getPublicKey(context)); // build and validate the token.
        } catch (JsonException je) {
            throw MESSAGES.credentialValidationFailed(credentials, je);
        }
    }

    private byte[] getPublicKey(IdentityContext context) {
        Partition partition = context.getPartition();
        return partition.<byte[]>getAttribute("PublicKey").getValue();
    }
}
