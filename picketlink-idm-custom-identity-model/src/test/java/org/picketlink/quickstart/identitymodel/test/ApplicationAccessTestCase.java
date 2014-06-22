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
import org.picketlink.quickstart.identitymodel.Application;
import org.picketlink.quickstart.identitymodel.ApplicationAccess;
import org.picketlink.quickstart.identitymodel.Group;
import org.picketlink.quickstart.identitymodel.GroupMembership;
import org.picketlink.quickstart.identitymodel.Realm;
import org.picketlink.quickstart.identitymodel.User;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Pedro Igor
 */
public class ApplicationAccessTestCase extends AbstractIdentityManagementTestCase {

    @Test
    public void testGrantApplicationAccessToUser() throws Exception {
        // we need an identity manager instance for acme realm. so we can store the user
        PartitionManager partitionManager = getPartitionManager();
        Realm acmeRealm = getAcmeRealm();
        IdentityManager acmeIdentityManager = partitionManager.createIdentityManager(acmeRealm);

        User user = new User("mary");

        // stores the user in the acme partition
        acmeIdentityManager.add(user);

        RelationshipManager relationshipManager = partitionManager.createRelationshipManager();

        // grant access to application
        ApplicationAccess applicationAccess = new ApplicationAccess(user, getSalesApplication());

        applicationAccess.setLastSuccessfulLogin(new Date());
        applicationAccess.setLastFailedLogin(new Date());
        applicationAccess.setFailedLoginAttempts(3);

        relationshipManager.add(applicationAccess);

        RelationshipQuery<ApplicationAccess> query = relationshipManager.createRelationshipQuery(ApplicationAccess.class);

        query.setParameter(ApplicationAccess.ASSIGNEE, user);

        // user is assigned with two roles
        List<ApplicationAccess> result = query.getResultList();

        assertEquals(1, result.size());

        ApplicationAccess storedAccess = result.get(0);

        assertEquals(applicationAccess.getFailedLoginAttempts(), storedAccess.getFailedLoginAttempts());
        assertEquals(applicationAccess.getLastSuccessfulLogin(), storedAccess.getLastSuccessfulLogin());
        assertEquals(applicationAccess.getLastFailedLogin(), storedAccess.getLastFailedLogin());
    }

    @Test
    public void testGrantApplicationAccessToGroup() throws Exception {
        // we need an identity manager instance for acme realm. so we can store the group
        PartitionManager partitionManager = getPartitionManager();
        Realm acmeRealm = getAcmeRealm();
        IdentityManager acmeIdentityManager = partitionManager.createIdentityManager(acmeRealm);

        Group group = new Group("Acme Administrators");

        // stores the group in the acme partition
        acmeIdentityManager.add(group);

        RelationshipManager relationshipManager = partitionManager.createRelationshipManager();

        // grant access to application
        Application salesApplication = getSalesApplication();
        relationshipManager.add(new ApplicationAccess(group, salesApplication));

        RelationshipQuery<ApplicationAccess> query = relationshipManager.createRelationshipQuery(ApplicationAccess.class);

        query.setParameter(ApplicationAccess.ASSIGNEE, group);

        // group is allowed to access the application
        assertEquals(1, query.getResultCount());

        User user = new User("mary");

        acmeIdentityManager.add(user);

        GroupMembership groupMembership = new GroupMembership(user, group);

        relationshipManager.add(groupMembership);

        // the user inherits the group privileges
        assertTrue(relationshipManager.inheritsPrivileges(user, salesApplication));

        relationshipManager.remove(groupMembership);

        // user no longer is assigned to group, thus is not allowed to access the applicaion
        assertFalse(relationshipManager.inheritsPrivileges(user, salesApplication));
    }

    @Test
    public void testQueryUsersWithAccessToApplication() throws Exception {
        // we need an identity manager instance for acme realm. so we can store the user
        PartitionManager partitionManager = getPartitionManager();
        Realm acmeRealm = getAcmeRealm();
        IdentityManager acmeIdentityManager = partitionManager.createIdentityManager(acmeRealm);

        User user = new User("mary");

        // stores the user in the acme partition
        acmeIdentityManager.add(user);

        RelationshipManager relationshipManager = partitionManager.createRelationshipManager();

        // grant access to application
        Application salesApplication = getSalesApplication();
        relationshipManager.add(new ApplicationAccess(user, salesApplication));

        RelationshipQuery<ApplicationAccess> query = relationshipManager.createRelationshipQuery(ApplicationAccess.class);

        query.setParameter(ApplicationAccess.APPLICATION, salesApplication);

        // user is assigned with two roles
        assertEquals(1, query.getResultCount());
    }

}
