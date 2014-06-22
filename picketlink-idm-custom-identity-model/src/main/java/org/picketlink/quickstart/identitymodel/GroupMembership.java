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
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Relationship;
import org.picketlink.idm.model.annotation.InheritsPrivileges;
import org.picketlink.idm.model.annotation.RelationshipStereotype;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.query.RelationshipQueryParameter;

import static org.picketlink.idm.model.annotation.RelationshipStereotype.Stereotype.GROUP_MEMBERSHIP;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.RELATIONSHIP_GROUP_MEMBERSHIP_GROUP;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.RELATIONSHIP_GROUP_MEMBERSHIP_MEMBER;

/**
 * <p>A {@link org.picketlink.idm.model.Relationship} that associate a {@link org.picketlink.quickstart.identitymodel.User}
 * as a member of a {@link org.picketlink.quickstart.identitymodel.Group}.
 *
 * @author Pedro Igor
 */
@RelationshipStereotype(GROUP_MEMBERSHIP)
public class GroupMembership extends AbstractAttributedType implements Relationship {

    public static final RelationshipQueryParameter MEMBER = RELATIONSHIP_QUERY_ATTRIBUTE.byName("member");
    public static final RelationshipQueryParameter GROUP = RELATIONSHIP_QUERY_ATTRIBUTE.byName("group");

    private Account member;
    private Group group;

    private GroupMembership() {
        this(null, null);
    }

    public GroupMembership(Account member, Group group) {
        this.member = member;
        this.group = group;
    }

    @InheritsPrivileges("group")
    @StereotypeProperty(RELATIONSHIP_GROUP_MEMBERSHIP_MEMBER)
    public Account getMember() {
        return member;
    }

    public void setMember(Account member) {
        this.member = member;
    }

    @StereotypeProperty(RELATIONSHIP_GROUP_MEMBERSHIP_GROUP)
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
