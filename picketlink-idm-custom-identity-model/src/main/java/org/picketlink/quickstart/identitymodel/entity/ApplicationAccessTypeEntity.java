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
import org.picketlink.quickstart.identitymodel.ApplicationAccess;

import javax.persistence.Entity;
import java.util.Date;

/**
 * <p>Entity with the mapping for {@link org.picketlink.quickstart.identitymodel.ApplicationAccess}.</p>
 *
 * @author Pedro Igor
 */
@IdentityManaged(ApplicationAccess.class)
@Entity
public class ApplicationAccessTypeEntity extends RelationshipTypeEntity {

    @AttributeValue
    private Date lastSuccessfulLogin;

    @AttributeValue
    private Date lastFailedLogin;

    @AttributeValue
    private int failedLoginAttempts;

    public Date getLastSuccessfulLogin() {
        return this.lastSuccessfulLogin;
    }

    public void setLastSuccessfulLogin(Date lastSuccessfulLogin) {
        this.lastSuccessfulLogin = lastSuccessfulLogin;
    }

    public Date getLastFailedLogin() {
        return this.lastFailedLogin;
    }

    public void setLastFailedLogin(Date lastFailedLogin) {
        this.lastFailedLogin = lastFailedLogin;
    }

    public int getFailedLoginAttempts() {
        return this.failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }
}
