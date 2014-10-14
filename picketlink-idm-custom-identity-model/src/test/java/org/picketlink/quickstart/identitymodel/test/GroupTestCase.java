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
import org.picketlink.quickstart.identitymodel.Group;
import org.picketlink.quickstart.identitymodel.Realm;
import org.picketlink.quickstart.identitymodel.Role;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Pedro Igor
 */
public class GroupTestCase extends AbstractIdentityManagementTestCase {

    @Test
    public void testCreateGroup() throws Exception {
        PartitionManager partitionManager = getPartitionManager();
        // get the realm where global groups are stored
        Realm acme = partitionManager.getPartition(Realm.class, REALM_ACME_NAME);

        assertNotNull(acme);

        // we need an identity manager instance for acme realm. so we can store the group
        IdentityManager acmeIdentityManager = partitionManager.createIdentityManager(acme);
        String globalGroupName = "Acme Administrators";
        Group globalGroup = new Group(globalGroupName);

        // stores the group
        acmeIdentityManager.add(globalGroup);

        IdentityQueryBuilder acmeQueryBuilder = acmeIdentityManager.getQueryBuilder();
        IdentityQuery<Group> query = acmeQueryBuilder.createIdentityQuery(Group.class);

        // let's check if the role is stored by querying using a name
        query.where(acmeQueryBuilder.equal(Group.NAME, globalGroupName));

        List<Group> groups = query.getResultList();

        assertEquals(1, groups.size());

        Group storedGlobalGroup = groups.get(0);

        assertEquals(globalGroup.getName(), storedGlobalGroup.getName());

        ApplicationRealm applicationPartition = getSalesApplicationPartition();

        IdentityManager applicationIdentityManager = partitionManager.createIdentityManager(applicationPartition);
        String applicationGroupName = "Sales Group";
        Group applicationGroup = new Group(applicationGroupName);

        applicationIdentityManager.add(applicationGroup);

        IdentityQueryBuilder applicationQueryBuilder = applicationIdentityManager.getQueryBuilder();
        query = applicationQueryBuilder.createIdentityQuery(Group.class);

        // let's check if the group is stored by querying using a name
        query.where(applicationQueryBuilder.equal(Role.NAME, applicationGroupName));

        groups = query.getResultList();

        assertEquals(1, groups.size());

        Group storedApplicationGroup = groups.get(0);

        assertEquals(applicationGroup.getName(), storedApplicationGroup.getName());

        // application group is not visible from realm partition. Neither from other applications.
        query = acmeQueryBuilder.createIdentityQuery(Group.class);

        query.where(acmeQueryBuilder.equal(Group.NAME, applicationGroup.getName()));

        assertTrue(query.getResultList().isEmpty());
    }

    @Test
    public void testGroupHierarchy() {
        PartitionManager partitionManager = getPartitionManager();
        // get the realm where global groups are stored
        ApplicationRealm applicationPartition = getSalesApplicationPartition();

        // we need an identity manager instance for applicationPartition realm. so we can store the group
        IdentityManager identityManager = partitionManager.createIdentityManager(applicationPartition);
        Group salesUnit = new Group("Sales Unit");

        // stores the sales unit
        identityManager.add(salesUnit);

        // we create the managers group as a child of sales unit
        Group salesManagers = new Group("Sales Managers", salesUnit);

        // stores the managers group
        identityManager.add(salesManagers);

        IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();
        IdentityQuery<Group> query = queryBuilder.createIdentityQuery(Group.class);

        // query all childs of sales unit
        query.where(queryBuilder.equal(Group.PARENT, salesUnit));

        List<Group> salesUnitChilds = query.getResultList();

        assertEquals(1, salesUnitChilds.size());

        Group salesVendors = new Group("Vendors", salesUnit);

        identityManager.add(salesVendors);

        query = queryBuilder.createIdentityQuery(Group.class);

        // query all childs of sales unit again
        query.where(queryBuilder.equal(Group.PARENT, salesUnit));

        salesUnitChilds = query.getResultList();

        assertEquals(2, salesUnitChilds.size());
    }
}
