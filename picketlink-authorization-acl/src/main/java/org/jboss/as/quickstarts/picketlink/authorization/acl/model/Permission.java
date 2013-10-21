/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.picketlink.authorization.acl.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.picketlink.idm.jpa.annotations.PermissionAssignee;
import org.picketlink.idm.jpa.annotations.PermissionOperation;
import org.picketlink.idm.jpa.annotations.PermissionResourceClass;
import org.picketlink.idm.jpa.annotations.PermissionResourceIdentifier;
import org.picketlink.idm.jpa.annotations.entity.PermissionManaged;

/**
 * This entity stores object permissions, as configured by the @PermissionManaged annotation.  As
 * the annotation does not specify a value for its resourceClasses member, it may be used to store
 * permissions for resources of all types.
 *
 * @author Shane Bryzak
 */
@Entity
@PermissionManaged
public class Permission implements Serializable {

    private static final long serialVersionUID = -7409821749592191950L;

    @Id @GeneratedValue
    private Long id;

    @PermissionAssignee
    private String assignee;

    @PermissionResourceClass
    private String resourceClass;

    @PermissionResourceIdentifier
    private String resourceIdentifier;

    @PermissionOperation
    private String operation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getResourceClass() {
        return resourceClass;
    }

    public void setResourceClass(String resourceClass) {
        this.resourceClass = resourceClass;
    }

    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    public void setResourceIdentifier(String resourceIdentifier) {
        this.resourceIdentifier = resourceIdentifier;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
