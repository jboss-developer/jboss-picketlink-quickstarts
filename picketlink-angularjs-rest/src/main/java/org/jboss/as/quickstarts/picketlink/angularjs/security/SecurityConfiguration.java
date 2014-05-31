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
package org.jboss.as.quickstarts.picketlink.angularjs.security;

import org.jboss.as.quickstarts.picketlink.angularjs.model.Person;
import org.jboss.as.quickstarts.picketlink.angularjs.security.authentication.JWSTokenProvider;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.ApplicationRole;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.MyUser;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.entity.MyUserTypeEntity;
import org.picketlink.IdentityConfigurationEvent;
import org.picketlink.PartitionManagerCreateEvent;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.config.SecurityConfigurationException;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.handler.TokenCredentialHandler;
import org.picketlink.idm.jpa.model.sample.simple.AttributeTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.GroupTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.IdentityTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PartitionTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PasswordCredentialTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RelationshipIdentityTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RelationshipTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RoleTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.TokenCredentialTypeEntity;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.internal.EEJPAContextInitializer;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import static org.jboss.as.quickstarts.picketlink.angularjs.security.model.IdentityModelUtils.findByLoginName;
import static org.picketlink.idm.model.basic.BasicModel.getRole;
import static org.picketlink.idm.model.basic.BasicModel.grantRole;

/**
 * @author Pedro Igor
 */
@Stateless
public class SecurityConfiguration {

    public static final String KEYSTORE_FILE_PATH = "/keystore.jks";

    private KeyStore keyStore;

    @Inject
    private JWSTokenProvider tokenProvider;

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
                        .addContextInitializer(this.contextInitializer)
                        .setCredentialHandlerProperty(TokenCredentialHandler.TOKEN_PROVIDER, this.tokenProvider)
                        .supportType(MyUser.class)
                        .supportAllFeatures();
    }

    public void configureDefaultPartition(@Observes PartitionManagerCreateEvent event) {
        PartitionManager partitionManager = event.getPartitionManager();

        createDefaultPartition(partitionManager);
        createDefaultRoles(partitionManager);
        createAdminAccount(partitionManager);
    }

    private void createDefaultRoles(PartitionManager partitionManager) {
        IdentityManager identityManager = partitionManager.createIdentityManager();

        createRole(ApplicationRole.ADMINISTRATOR, identityManager);
        createRole(ApplicationRole.USER, identityManager);
    }

    private void createDefaultPartition(PartitionManager partitionManager) {
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

    public static Role createRole(ApplicationRole applicationRole, IdentityManager identityManager) {
        String roleName = applicationRole.name();
        Role role = getRole(identityManager, roleName);

        if (role == null) {
            role = new Role(roleName);
            identityManager.add(role);
        }

        return role;
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

    public void createAdminAccount(PartitionManager partitionManager) {
        IdentityManager identityManager = partitionManager.createIdentityManager();
        String email = "admin@picketlink.org";

        // if admin exists dont create again
        if(findByLoginName(email, identityManager) != null) {
            return;
        }

        Person person = new Person();

        person.setFirstName("Almight");
        person.setLastName("Administrator");
        person.setEmail(email);

        MyUser admin = new MyUser(person.getEmail());

        admin.setPerson(person);

        identityManager.add(admin);

        identityManager.updateCredential(admin, new Password("admin"));

        Role adminRole = getRole(identityManager, ApplicationRole.ADMINISTRATOR.name());

        grantRole(partitionManager.createRelationshipManager(), admin, adminRole);
    }
}
