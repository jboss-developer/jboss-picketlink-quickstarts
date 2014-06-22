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

import org.picketlink.idm.credential.storage.EncodedPasswordStorage;
import org.picketlink.idm.jpa.annotations.CredentialClass;
import org.picketlink.idm.jpa.annotations.CredentialProperty;
import org.picketlink.idm.jpa.annotations.EffectiveDate;
import org.picketlink.idm.jpa.annotations.ExpiryDate;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.ManagedCredential;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * <p>This entity is mapped to support Password credential types using a {@link org.picketlink.idm.credential.storage.EncodedPasswordStorage}.</p>
 *
 * @author pedroigor
 */
@ManagedCredential(EncodedPasswordStorage.class)
@Entity
public class PasswordCredentialTypeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OwnerReference
    @ManyToOne
    private UserTypeEntity owner;

    @CredentialClass
    private String typeName;

    @Temporal(TemporalType.TIMESTAMP)
    @EffectiveDate
    private Date effectiveDate;

    @Temporal(TemporalType.TIMESTAMP)
    @ExpiryDate
    private Date expiryDate;

    @CredentialProperty(name = "encodedHash")
    private String passwordEncodedHash;

    @CredentialProperty(name = "salt")
    private String passwordSalt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserTypeEntity getOwner() {
        return owner;
    }

    public void setOwner(UserTypeEntity owner) {
        this.owner = owner;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPasswordEncodedHash() {
        return passwordEncodedHash;
    }

    public void setPasswordEncodedHash(String passwordEncodedHash) {
        this.passwordEncodedHash = passwordEncodedHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

}
