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
import org.picketlink.quickstart.identitymodel.Application;
import org.picketlink.quickstart.identitymodel.ApplicationRealm;
import org.picketlink.quickstart.identitymodel.Realm;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Pedro Igor
 */
public class ApplicationTestCase extends AbstractIdentityManagementTestCase {

    @Test
    public void testCreateApplication() throws Exception {
        // get the realm where the application should be stored
        PartitionManager partitionManager = getPartitionManager();

        // we need an identity manager instance for acme realm. so we can store the application
        Realm acmeRealm = getAcmeRealm();
        IdentityManager identityManager = partitionManager.createIdentityManager(acmeRealm);
        Application timeSheetApplication = new Application("TimeSheet Application");

        // stores the application in the acme partition
        identityManager.add(timeSheetApplication);

        IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();
        IdentityQuery<Application> query = queryBuilder.createIdentityQuery(Application.class);

        // let's check if the application is stored by querying by the identifier
        query.where(queryBuilder.equal(Application.ID, timeSheetApplication.getId()));

        List<Application> applications = query.getResultList();

        assertEquals(1, applications.size());

        Application storedApplication = applications.get(0);

        assertEquals(timeSheetApplication.getName(), storedApplication.getName());

        ApplicationRealm salesApplicationPartition = new ApplicationRealm(timeSheetApplication.getName());

        // now, we also need to create a partition for this application
        partitionManager.add(salesApplicationPartition);

        // applications have two distinct representations: identity type and partition. They mean different things.
        assertFalse(storedApplication.getId().equals(salesApplicationPartition.getId()));
    }

}
