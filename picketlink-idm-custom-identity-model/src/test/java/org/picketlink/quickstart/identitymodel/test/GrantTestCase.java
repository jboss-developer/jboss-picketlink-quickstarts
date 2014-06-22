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
import org.picketlink.quickstart.identitymodel.Grant;
import org.picketlink.quickstart.identitymodel.Realm;
import org.picketlink.quickstart.identitymodel.Role;
import org.picketlink.quickstart.identitymodel.User;

import static org.junit.Assert.assertEquals;

/**
 * @author Pedro Igor
 */
public class GrantTestCase extends AbstractIdentityManagementTestCase {

    @Test
    public void testGrantRole() throws Exception {
        // we need an identity manager instance for acme realm. so we can store the role
        PartitionManager partitionManager = getPartitionManager();
        Realm acmeRealm = getAcmeRealm();
        IdentityManager acmeIdentityManager = partitionManager.createIdentityManager(acmeRealm);
        Role globalRole = new Role("Global Role");

        // stores the global role
        acmeIdentityManager.add(globalRole);

        // we need an identity manager instance for acme realm. so we can store the role
        ApplicationRealm applicationPartition = getSalesApplicationPartition();
        IdentityManager applicationIdentityManager = partitionManager.createIdentityManager(applicationPartition);
        Role applicationRole = new Role("Application Role");

        // stores a application specific role
        applicationIdentityManager.add(applicationRole);

        User user = new User("mary");

        // stores the user in the acme partition
        acmeIdentityManager.add(user);

        RelationshipManager relationshipManager = partitionManager.createRelationshipManager();

        // assign global role to user
        relationshipManager.add(new Grant(user, globalRole));

        // assign application specific role to user
        relationshipManager.add(new Grant(user, applicationRole));

        RelationshipQuery<Grant> query = relationshipManager.createRelationshipQuery(Grant.class);

        query.setParameter(Grant.ASSIGNEE, user);

        // user is assigned with two roles
        assertEquals(2, query.getResultCount());
    }

}
