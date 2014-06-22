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
package org.picketlink.quickstart.identitymodel;

import org.picketlink.idm.model.AbstractPartition;
import org.picketlink.idm.model.annotation.IdentityPartition;

/**
 * <p>{@link org.picketlink.quickstart.identitymodel.ApplicationRealm} represents the partition for a particular application.</p>
 *
 * <p>Only roles and groups can be stored within the application partition.</p>
 *
 * @author Pedro Igor
 */
@IdentityPartition(supportedTypes = {Role.class, Group.class})
public class ApplicationRealm extends AbstractPartition {

    private ApplicationRealm() {
        this(null);
        // PicketLink requires a default constructor to create and populate instances using reflection
    }

    public ApplicationRealm(String name) {
        super(name);
    }

}
