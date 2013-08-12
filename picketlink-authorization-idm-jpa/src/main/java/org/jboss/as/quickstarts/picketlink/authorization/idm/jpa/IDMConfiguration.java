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
package org.jboss.as.quickstarts.picketlink.authorization.idm.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.jpa.model.sample.simple.AccountTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.AttributeTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.AttributedTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.GroupTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.IdentityTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PartitionTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PasswordCredentialTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RelationshipIdentityTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RelationshipTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RoleTypeEntity;
import org.picketlink.idm.model.Relationship;
import org.picketlink.internal.EEJPAContextInitializer;

/**
 * This bean produces the configuration for PicketLink IDM
 * 
 * 
 * @author Shane Bryzak
 *
 */
@ApplicationScoped
public class IDMConfiguration {

    @Inject
    private EEJPAContextInitializer contextInitializer;

    private IdentityConfiguration identityConfig = null;

    @Produces IdentityConfiguration createConfig() {
        if (identityConfig == null) {
            initConfig();
        }
        return identityConfig;
    }

    /**
     * This method uses the IdentityConfigurationBuilder to create an IdentityConfiguration, which 
     * defines how PicketLink stores identity-related data.  In this particular example, a 
     * JPAIdentityStore is configured to allow the identity data to be stored in a relational database
     * using JPA.
     */
    private void initConfig() {
        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();

        builder
            .named("default")
                .stores()
                    .jpa()
                        .mappedEntity(
                                AccountTypeEntity.class,
                                RoleTypeEntity.class,
                                GroupTypeEntity.class,
                                IdentityTypeEntity.class,
                                RelationshipTypeEntity.class,
                                RelationshipIdentityTypeEntity.class,
                                PartitionTypeEntity.class,
                                PasswordCredentialTypeEntity.class,
                                AttributeTypeEntity.class)
                        .supportGlobalRelationship(Relationship.class)
                        .addContextInitializer(this.contextInitializer)
                        // Specify that this identity store configuration supports all features
                        .supportAllFeatures();

        identityConfig = builder.build();
    }
}
