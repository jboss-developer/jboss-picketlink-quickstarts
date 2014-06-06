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

import org.jboss.as.quickstarts.picketlink.angularjs.security.authentication.JWSTokenProvider;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.MyUser;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.entity.MyUserTypeEntity;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.web.TokenAuthenticationScheme;
import org.picketlink.config.SecurityConfigurationBuilder;
import org.picketlink.event.SecurityConfigurationEvent;
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
import org.picketlink.internal.EEJPAContextInitializer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * <p>This class is responsible to enable the {@link org.picketlink.authentication.web.TokenAuthenticationScheme}.</p>
 *
 * @author Pedro Igor
 */
@ApplicationScoped
public class SecurityConfiguration {

    @Inject
    private JWSTokenProvider tokenProvider;

    @Inject
    private EEJPAContextInitializer contextInitializer;

    @Inject
    private TokenAuthenticationScheme tokenAuthenticationScheme;

    @Produces
    @PicketLink
    public TokenAuthenticationScheme configureTokenAuthenticationScheme() {
        return this.tokenAuthenticationScheme;
    }

    public void configureIdentityManagement(@Observes SecurityConfigurationEvent event) {
        SecurityConfigurationBuilder builder = event.getBuilder();

        builder
            .identity()
            .stateless()
            .idmConfig()
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
}
