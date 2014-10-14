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
package org.picketlink.quickstart.identitymodel.test;

import org.junit.Test;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.picketlink.quickstart.identitymodel.Realm;
import org.picketlink.quickstart.identitymodel.User;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.picketlink.idm.credential.Credentials.Status.VALID;

/**
 * @author Pedro Igor
 */
public class UserTestCase extends AbstractIdentityManagementTestCase {

    @Test
    public void testCreateUser() throws Exception {
        // get the realm where the user should be stored
        Realm acmeRealm = getAcmeRealm();
        Realm acme = acmeRealm;

        // we need an identity manager instance for acme realm. so we can store the user
        PartitionManager partitionManager = getPartitionManager();
        IdentityManager identityManager = partitionManager.createIdentityManager(acme);
        User user = new User("mary");

        // stores the user in the acme partition
        identityManager.add(user);

        IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();
        IdentityQuery<User> query = queryBuilder.createIdentityQuery(User.class);

        // let's check if the user is stored by querying by name
        query.where(queryBuilder.equal(User.USER_NAME, user.getUserName()));

        List<User> users = query.getResultList();

        assertEquals(1, users.size());

        User storedUser = users.get(0);

        assertEquals(user.getUserName(), storedUser.getUserName());
    }

    @Test
    public void testAuthentication() {
        // we need an identity manager instance for acme realm. so we can store the user
        PartitionManager partitionManager = getPartitionManager();
        Realm acme = getAcmeRealm();
        IdentityManager identityManager = partitionManager.createIdentityManager(acme);
        User user = new User("mary");

        // stores the user in the acme partition
        identityManager.add(user);

        Password password = new Password("secret");

        identityManager.updateCredential(user, password);

        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user.getUserName(), password);

        identityManager.validateCredentials(credentials);

        assertEquals(VALID, credentials.getStatus());
    }

}