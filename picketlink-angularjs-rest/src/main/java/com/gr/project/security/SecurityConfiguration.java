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
package com.gr.project.security;

import com.gr.project.security.authentication.credential.TokenCredentialHandler;
import com.gr.project.security.model.MyUser;
import com.gr.project.security.model.entity.MyUserTypeEntity;
import com.gr.project.security.model.entity.TokenCredentialTypeEntity;
import org.picketlink.IdentityConfigurationEvent;
import org.picketlink.PartitionManagerCreateEvent;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.config.SecurityConfigurationException;
import org.picketlink.idm.credential.handler.PasswordCredentialHandler;
import org.picketlink.idm.jpa.model.sample.simple.AttributeTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.GroupTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.IdentityTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PartitionTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PasswordCredentialTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RelationshipIdentityTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RelationshipTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RoleTypeEntity;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.internal.EEJPAContextInitializer;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * @author Pedro Igor
 */
@Stateless
public class SecurityConfiguration {

    public static final String KEYSTORE_FILE_PATH = "/keystore.jks";

    private KeyStore keyStore;

    @Inject
    private EEJPAContextInitializer contextInitializer;

    public void configureIdentityManagement(@Observes IdentityConfigurationEvent event) {
        IdentityConfigurationBuilder builder = event.getConfig();

        builder
            .named("default.config")
                .stores()
                    .jpa()
                        .mappedEntity(
                            PartitionTypeEntity.class,
                            RoleTypeEntity.class,
                            GroupTypeEntity.class,
                            IdentityTypeEntity.class,
                            RelationshipTypeEntity.class,
                            RelationshipIdentityTypeEntity.class,
                            PasswordCredentialTypeEntity.class,
                            TokenCredentialTypeEntity.class,
                            AttributeTypeEntity.class,
                            MyUserTypeEntity.class)
                        .addCredentialHandler(TokenCredentialHandler.class)
                        .addContextInitializer(this.contextInitializer)
                        .setCredentialHandlerProperty(PasswordCredentialHandler.SUPPORTED_ACCOUNT_TYPES_PROPERTY, MyUser.class)
                        .supportAllFeatures();
    }

    public void configureDefaultPartition(@Observes PartitionManagerCreateEvent event) {
        PartitionManager partitionManager = event.getPartitionManager();
        Realm partition = partitionManager.getPartition(Realm.class, Realm.DEFAULT_REALM);

        if (partition == null) {
            try {
                partition = new Realm(Realm.DEFAULT_REALM);

                partition.setAttribute(new Attribute<byte[]>("PublicKey", getPublicKey()));
                partition.setAttribute(new Attribute<byte[]>("PrivateKey", getPrivateKey()));

                partitionManager.add(partition);
            } catch (Exception e) {
                throw new SecurityConfigurationException("Could not create default partition.", e);
            }
        }
    }

    private byte[] getPrivateKey() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        return getKeyStore().getKey("servercert", "test123".toCharArray()).getEncoded();
    }

    private byte[] getPublicKey() throws KeyStoreException {
        return getKeyStore().getCertificate("servercert").getPublicKey().getEncoded();
    }

    private KeyStore getKeyStore() {
        if (this.keyStore == null) {
            try {
                this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                getKeyStore().load(getClass().getResourceAsStream(KEYSTORE_FILE_PATH), "store123".toCharArray());
            } catch (Exception e) {
                throw new SecurityException("Could not load key store.", e);
            }
        }

        return this.keyStore;
    }

}
