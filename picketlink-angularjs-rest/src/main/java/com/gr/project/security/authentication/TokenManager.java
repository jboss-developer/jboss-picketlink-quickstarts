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
package com.gr.project.security.authentication;

import com.gr.project.security.authentication.credential.Token;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.json.jose.JWSBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

/**
 * @author Pedro Igor
 */
@ApplicationScoped
public class TokenManager {

    @Inject
    private PartitionManager partitionManager;

    public Token issue(Account account) {
        JWSBuilder builder = new JWSBuilder();

        builder
            .id(UUID.randomUUID().toString())
            .rsa256(getPrivateKey(account))
            .issuer("picketlink.org")
            .issuedAt(getCurrentTime())
            .subject(account.getId())
            .expiration(getCurrentTime() + (5 * 60))
            .notBefore(getCurrentTime());

        return new Token(builder.build().encode());
    }

    private byte[] getPrivateKey(Account account) {
        return getPartition(account).<byte[]>getAttribute("PrivateKey").getValue();
    }

    private int getCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    private Realm getPartition(Account account) {
        return partitionManager.lookupById(Realm.class, account.getPartition().getId());
    }
}
