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
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.query.RelationshipQuery;
import org.picketlink.quickstart.identitymodel.ApplicationRealm;
import org.picketlink.quickstart.identitymodel.Group;
import org.picketlink.quickstart.identitymodel.GroupMembership;
import org.picketlink.quickstart.identitymodel.Realm;
import org.picketlink.quickstart.identitymodel.User;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Pedro Igor
 */
public class GroupMembershipTestCase extends AbstractIdentityManagementTestCase {

    @Test
    public void testAddUserToGroup() throws Exception {
        // we need an identity manager instance for acme realm. so we can store the group
        PartitionManager partitionManager = getPartitionManager();
        Realm acmeRealm = getAcmeRealm();
        IdentityManager acmeIdentityManager = partitionManager.createIdentityManager(acmeRealm);
        Group globalGroup = new Group("Global Group");

        // stores the global group
        acmeIdentityManager.add(globalGroup);

        // we need an identity manager instance for acme realm. so we can store the group
        ApplicationRealm applicationPartition = getSalesApplicationPartition();
        IdentityManager applicationIdentityManager = partitionManager.createIdentityManager(applicationPartition);
        Group applicationGroup = new Group("Application Group");

        // stores a application specific group
        applicationIdentityManager.add(applicationGroup);

        User user = new User("mary");

        // stores the user in the acme partition
        acmeIdentityManager.add(user);

        RelationshipManager relationshipManager = partitionManager.createRelationshipManager();

        // user us now a member of global group
        relationshipManager.add(new GroupMembership(user, globalGroup));

        // user us now a member of application specific group
        relationshipManager.add(new GroupMembership(user, applicationGroup));

        RelationshipQuery<GroupMembership> query = relationshipManager.createRelationshipQuery(GroupMembership.class);

        query.setParameter(GroupMembership.MEMBER, user);

        // user is assigned with two groups
        List<GroupMembership> resultList = query.getResultList();
        assertEquals(2, resultList.size());
    }

}
