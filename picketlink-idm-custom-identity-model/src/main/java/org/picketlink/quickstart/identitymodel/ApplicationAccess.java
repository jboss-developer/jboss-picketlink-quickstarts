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

import org.picketlink.idm.model.AbstractAttributedType;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.Relationship;
import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.annotation.InheritsPrivileges;
import org.picketlink.idm.query.RelationshipQueryParameter;

import java.util.Date;

/**
 * <p>A {@link org.picketlink.idm.model.Relationship} that authorizes an {@link org.picketlink.quickstart.identitymodel.User}
 * to access a specific {@link org.picketlink.quickstart.identitymodel.Application}.
 *
 * @author Pedro Igor
 */
public class ApplicationAccess extends AbstractAttributedType implements Relationship {

    public static final RelationshipQueryParameter ASSIGNEE = RELATIONSHIP_QUERY_ATTRIBUTE.byName("assignee");
    public static final RelationshipQueryParameter APPLICATION = RELATIONSHIP_QUERY_ATTRIBUTE.byName("application");

    @InheritsPrivileges("application")
    private IdentityType assignee;

    private Application application;

    @AttributeProperty
    private Date lastSuccessfulLogin;

    @AttributeProperty
    private Date lastFailedLogin;

    @AttributeProperty
    private int failedLoginAttempts;

    private ApplicationAccess() {
    }

    public ApplicationAccess(IdentityType assignee, Application application) {
        setAssignee(assignee);
        setApplication(application);
    }

    public IdentityType getAssignee() {
        return this.assignee;
    }

    public void setAssignee(IdentityType assignee) {
        // only users and groups can be assigned
        if (assignee != null && !User.class.isAssignableFrom(assignee.getClass()) && !Group.class.isAssignableFrom(assignee.getClass())) {
            throw new IllegalArgumentException("Assignee can be only an User or a Group.");
        }

        this.assignee = assignee;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

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
