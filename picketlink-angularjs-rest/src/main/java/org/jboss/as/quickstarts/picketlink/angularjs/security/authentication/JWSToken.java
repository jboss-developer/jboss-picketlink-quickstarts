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
package org.jboss.as.quickstarts.picketlink.angularjs.security.authentication;

import org.picketlink.idm.credential.AbstractToken;
import org.picketlink.json.jose.JWS;
import org.picketlink.json.jose.JWSBuilder;

/**
 * <p>This is a simple example about how to represent your own {@link org.picketlink.idm.credential.Token} type.
 * In this case, we're using PicketLink JSON API to support a JSON Web Signature (JWS) format to our token.</p>
 *
 * <p>Tokens are managed by their respective {@link org.picketlink.idm.credential.Token.Provider}. In this case, we're using
 * {@link org.jboss.as.quickstarts.picketlink.angularjs.security.authentication.JWSTokenProvider}.</p>
 *
 * @author Pedro Igor
 *
 * @see org.jboss.as.quickstarts.picketlink.angularjs.security.authentication.JWSTokenProvider
 */
public class JWSToken extends AbstractToken {

    private final JWS jws;

    public JWSToken(String encodedToken) {
        super(encodedToken);
        this.jws = new JWSBuilder().build(encodedToken);
    }

    @Override
    public String getSubject() {
        return this.jws.getSubject();
    }
}
