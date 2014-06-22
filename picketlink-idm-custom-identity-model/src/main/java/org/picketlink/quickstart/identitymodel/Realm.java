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
import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.annotation.IdentityPartition;

/**
 * <p>A {@link org.picketlink.quickstart.identitymodel.Realm} represents a security domain where users, roles and applications are
 * stored.</p>
 *
 * <p>Roles stored within a realm are visible to all its applications. Also known as global roles. The same applies to groups.</p>
 *
 * @author Pedro Igor
 */
@IdentityPartition(supportedTypes = {Application.class, User.class, Role.class, Group.class})
public class Realm extends AbstractPartition {

    @AttributeProperty
    private boolean enforceSSL;

    @AttributeProperty
    private int numberFailedLoginAttempts;

    @AttributeProperty
    private byte[] publickKey;

    @AttributeProperty
    private byte[] privateKey;

    // PicketLink requires a default constructor to create and populate instances using reflection
    private Realm() {
        this(null);
    }

    public Realm(String name) {
        super(name);
    }

    public boolean isEnforceSSL() {
        return this.enforceSSL;
    }

    public void setEnforceSSL(boolean enforceSSL) {
        this.enforceSSL = enforceSSL;
    }

    public int getNumberFailedLoginAttempts() {
        return this.numberFailedLoginAttempts;
    }

    public void setNumberFailedLoginAttempts(int numberFailedLoginAttempts) {
        this.numberFailedLoginAttempts = numberFailedLoginAttempts;
    }

    public byte[] getPublickKey() {
        return this.publickKey;
    }

    public void setPublickKey(byte[] publickKey) {
        this.publickKey = publickKey;
    }

    public byte[] getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }
}
