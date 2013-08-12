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

package org.jboss.as.quickstarts.picketlink.idm.ldap;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.sample.Role;
import org.picketlink.idm.model.sample.SampleModel;
import org.picketlink.idm.model.sample.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import static org.jboss.as.quickstarts.picketlink.idm.ldap.ApplicationRole.*;
import static org.picketlink.idm.model.sample.SampleModel.*;

@Startup
@Singleton
public class IDMInitializer {

    @Inject
    private PartitionManager partitionManager;

    /**
     * <p>Initializes the identity store with some default users and roles.</p>
     */
    @PostConstruct
    public void createDefaultUsers() {
        createUser("admin", ADMINISTRATOR);
        createUser("john", PROJECT_MANAGER);
        createUser("kate", DEVELOPER);
    }

    private void createUser(String loginName, ApplicationRole roleName) {
        IdentityManager identityManager = this.partitionManager.createIdentityManager();

        User user = SampleModel.getUser(identityManager, loginName);

        if (user == null) {
            user = new User(loginName);

            identityManager.add(user);

            Password password = new Password(loginName + "123");

            identityManager.updateCredential(user, password);
        }

        Role role = getRole(identityManager, roleName.name());

        if (role == null) {
            role = new Role(roleName.name());

            identityManager.add(role);
        }

        RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();

        if (!hasRole(relationshipManager, user, role)) {
            grantRole(relationshipManager, user, role);
        }
    }

}
