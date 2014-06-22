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
import org.picketlink.idm.model.annotation.InheritsPrivileges;
import org.picketlink.idm.model.annotation.RelationshipStereotype;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.query.RelationshipQueryParameter;

import static org.picketlink.idm.model.annotation.RelationshipStereotype.Stereotype.GRANT;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.RELATIONSHIP_GRANT_ASSIGNEE;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.RELATIONSHIP_GRANT_ROLE;

/**
 * <p>A {@link org.picketlink.idm.model.Relationship} that associate a {@link org.picketlink.quickstart.identitymodel.Role}
 * with any {@link org.picketlink.idm.model.IdentityType}.</p>
 *
 * @author Pedro Igor
 */
@RelationshipStereotype(GRANT)
public class Grant extends AbstractAttributedType implements Relationship {

    public static final RelationshipQueryParameter ASSIGNEE = RELATIONSHIP_QUERY_ATTRIBUTE.byName("assignee");
    public static final RelationshipQueryParameter ROLE = RELATIONSHIP_QUERY_ATTRIBUTE.byName("role");

    @InheritsPrivileges("role")
    @StereotypeProperty(RELATIONSHIP_GRANT_ASSIGNEE)
    private IdentityType assignee;


    @StereotypeProperty(RELATIONSHIP_GRANT_ROLE)
    private Role role;

    private Grant() {
        this(null, null);
    }

    public Grant(IdentityType assignee, Role role) {
        this.assignee = assignee;
        this.role = role;
    }

    public IdentityType getAssignee() {
        return this.assignee;
    }

    public void setAssignee(IdentityType assignee) {
        this.assignee = assignee;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}