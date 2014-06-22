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
import org.picketlink.idm.PartitionManager;
import org.picketlink.quickstart.identitymodel.Realm;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Pedro Igor
 */
public class RealmTestCase extends AbstractIdentityManagementTestCase {

    @Test
    public void testCreate() throws Exception {
        Realm acme = new Realm("JBoss");

        acme.setEnforceSSL(true);

        // let's generate a keypair for the realm
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();

        acme.setPrivateKey(keyPair.getPrivate().getEncoded());
        acme.setPublickKey(keyPair.getPublic().getEncoded());

        acme.setNumberFailedLoginAttempts(3);

        PartitionManager partitionManager = getPartitionManager();

        // stores the realm
        partitionManager.add(acme);

        assertNotNull("Realm identifier not generated.", acme.getId());

        // retrieves the realm and check state
        Realm storedRealm = partitionManager.getPartition(Realm.class, acme.getName());

        assertNotNull("Realm not stored.", storedRealm);
        assertEquals(acme.isEnforceSSL(), storedRealm.isEnforceSSL());
        assertEquals(acme.getNumberFailedLoginAttempts(), storedRealm.getNumberFailedLoginAttempts());
        assertEquals(acme.getPrivateKey(), storedRealm.getPrivateKey());
        assertEquals(acme.getPublickKey(), storedRealm.getPublickKey());
    }

}
