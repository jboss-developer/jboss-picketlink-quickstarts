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
package com.gr.project.security.authentication;

import com.gr.project.security.authentication.credential.TokenCredential;
import org.picketlink.Identity;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.web.HTTPAuthenticationScheme;
import org.picketlink.credential.DefaultLoginCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>A custom {@link org.picketlink.authentication.web.HTTPAuthenticationScheme} that knows how to extract a header from
 * the request containing a token to be used to authenticate/re-authenticate an user.</p>
 *
 * <p>The authentication is stateless, which means that security state is discarded once the request finishes. The token must be always
 * provided in order to create a the security context for a request and provide access to protected resources.</p>
 *
 * <p>This scheme is used by the {@link org.picketlink.authentication.web.AuthenticationFilter}, which is configured in the web application
 * deployment descriptor(web.xml).</p>
 *
 * @author Pedro Igor
 */
@ApplicationScoped
@PicketLink
public class JWSAuthenticationScheme implements HTTPAuthenticationScheme {

    public static final String AUTHORIZATION_TOKEN_HEADER_NAME = "x-session-token";

    @Inject
    private Instance<Identity> identityInstance;

    @Override
    public void initialize(FilterConfig config) {

    }

    @Override
    public void extractCredential(HttpServletRequest request, DefaultLoginCredentials creds) {
        String header = getTokenHader(request);

        if (header != null) {
            creds.setCredential(new TokenCredential(header));
        }
    }

    /**
     * <p>We use a 401 http status code to sinalize to clients that authentication is required.</p>
     *
     * <p>We only challenge clients if the authentication failed. In other words, if there is a token in the request bu it is invalid.</p>
     *
     * @param request
     * @param response
     * @throws java.io.IOException
     */
    @Override
    public void challengeClient(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public boolean postAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return true;
    }

    /**
     * <p>We only initiate the authentication process if there the <code>AUTHORIZATION_TOKEN_HEADER_NAME</code> header
     * is present.</p>
     *
     * <p>Otherwise, the request will continue and reach its destination. It is very important that any protected resource such as
     * RESTFul endpoints, is annotated with one of the provided security annotations. Eg.: {@link com.gr.project.security.authorization.annotation.UserLoggedIn}.</p>
     *
     * <p>Each resource defines its requirements about how access protection should be performed.</p>
     *
     * @param request
     * @return
     */
//    @Override
    public boolean isProtected(HttpServletRequest request) {
        return getTokenHader(request) != null;
    }
    
    

    private String getTokenHader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_TOKEN_HEADER_NAME);
    }
}
