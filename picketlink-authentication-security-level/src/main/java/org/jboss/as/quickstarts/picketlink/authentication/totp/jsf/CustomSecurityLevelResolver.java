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
package org.jboss.as.quickstarts.picketlink.authentication.totp.jsf;

import org.picketlink.authentication.levels.Level;
import org.picketlink.authentication.levels.SecurityLevelResolver;
import org.picketlink.authentication.levels.internal.DefaultLevel;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.credential.TOTPCredentials;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * <p>This custom {@link org.picketlink.authentication.levels.SecurityLevelResolver} resolve a security level based on
 * the credentials associated within the current request.</p>
 *
 * <p>You can perform any logic at this moment in order to resolve levels based on any contextual information.</p>
 *
 * @author Pedro Igor
 */
@RequestScoped
public class CustomSecurityLevelResolver implements SecurityLevelResolver {

    @Inject
    private DefaultLoginCredentials defaultLoginCredentials;

    @Override
    public Level resolve() {
        Object currentCredential = this.defaultLoginCredentials.getCredential();

        if (TOTPCredentials.class.isInstance(currentCredential)) {
            return new DefaultLevel(2);
        }

        return null;
    }
}