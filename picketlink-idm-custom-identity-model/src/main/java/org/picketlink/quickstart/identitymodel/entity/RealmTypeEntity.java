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
package org.picketlink.quickstart.identitymodel.entity;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.quickstart.identitymodel.Realm;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * <p>Entity with the mapping for {@link org.picketlink.quickstart.identitymodel.Realm}.</p>
 *
 * @author Pedro Igor
 */

@IdentityManaged(Realm.class)
@Entity
public class RealmTypeEntity extends PartitionTypeEntity {

    @AttributeValue
    private boolean enforceSSL;

    @AttributeValue
    private int numberFailedLoginAttempts;

    @AttributeValue
    @Column(columnDefinition = "TEXT")
    private byte[] publickKey;

    @AttributeValue
    @Column(columnDefinition = "TEXT")
    private byte[] privateKey;

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

    public boolean isEnforceSSL() {
        return this.enforceSSL;
    }

    public void setEnforceSSL(boolean enforceSSL) {
        this.enforceSSL = enforceSSL;
    }
}
