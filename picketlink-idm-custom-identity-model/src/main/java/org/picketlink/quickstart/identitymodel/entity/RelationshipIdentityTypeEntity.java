/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.picketlink.quickstart.identitymodel.entity;

import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.RelationshipDescriptor;
import org.picketlink.idm.jpa.annotations.RelationshipMember;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.model.Relationship;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * <p>Entity with the mapping for the {@link org.picketlink.idm.model.IdentityType} participating in a relationship.</p>
 *
 * @author Pedro Igor
 */

@IdentityManaged({Relationship.class})
@Entity
public class RelationshipIdentityTypeEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long identifier;

    @RelationshipDescriptor
    private String descriptor;

    @RelationshipMember
    private String identityType;

    @OwnerReference
    @ManyToOne
    private RelationshipTypeEntity owner;

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public RelationshipTypeEntity getOwner() {
        return owner;
    }

    public void setOwner(RelationshipTypeEntity owner) {
        this.owner = owner;
    }

}
