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
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.picketlink.quickstart.identitymodel.ApplicationRealm;
import org.picketlink.quickstart.identitymodel.Realm;
import org.picketlink.quickstart.identitymodel.Role;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Pedro Igor
 */
public class RoleTestCase extends AbstractIdentityManagementTestCase {

    @Test
    public void testCreateRole() throws Exception {
        PartitionManager partitionManager = getPartitionManager();
        // we need an identity manager instance for acme realm. so we can store the role
        Realm acmeRealm = getAcmeRealm();
        IdentityManager acmeIdentityManager = partitionManager.createIdentityManager(acmeRealm);
        Role globalRole = new Role("Global Role");

        // stores the global role
        acmeIdentityManager.add(globalRole);

        IdentityQueryBuilder acmeQueryBuilder = acmeIdentityManager.getQueryBuilder();
        IdentityQuery<Role> query = acmeQueryBuilder.createIdentityQuery(Role.class);

        // let's check if the role is stored by querying using a name
        query.where(acmeQueryBuilder.equal(Role.NAME, globalRole.getName()));

        List<Role> roles = query.getResultList();

        assertEquals(1, roles.size());

        Role storedGlobalRole = roles.get(0);

        assertEquals(globalRole.getName(), storedGlobalRole.getName());

        // we need an identity manager instance for the application partition. so we can store the role
        ApplicationRealm salesApplicationPartition = getSalesApplicationPartition();
        IdentityManager applicationIdentityManager = partitionManager.createIdentityManager(salesApplicationPartition);
        Role applicationRole = new Role("Application Role");

        // stores a application specific role
        applicationIdentityManager.add(applicationRole);

        IdentityQueryBuilder applicationQueryBuilder = applicationIdentityManager.getQueryBuilder();
        query = applicationQueryBuilder.createIdentityQuery(Role.class);

        // let's check if the role is stored by querying using a name
        query.where(applicationQueryBuilder.equal(Role.NAME, applicationRole.getName()));

        roles = query.getResultList();

        assertEquals(1, roles.size());

        Role storedApplicationRole = roles.get(0);

        assertEquals(applicationRole.getName(), storedApplicationRole.getName());

        // let's check if is possible to get the application role from the acme partition
        query = acmeQueryBuilder.createIdentityQuery(Role.class);

        query.where(acmeQueryBuilder.equal(Role.NAME, applicationRole.getName()));

        // partitions don't share identity types
        assertTrue(query.getResultList().isEmpty());
    }

}