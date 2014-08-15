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
package org.picketlink.quickstarts.federation.saml;

import org.picketlink.common.exceptions.ProcessingException;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2HandlerRequest;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2HandlerResponse;
import org.picketlink.identity.federation.web.core.HTTPContext;
import org.picketlink.identity.federation.web.handlers.saml2.SAML2AuthenticationHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A custom {@link org.picketlink.identity.federation.web.handlers.saml2.SAML2AuthenticationHandler} that dynamically discovers
 * the IdP URL based on the information from the incoming {@link javax.servlet.http.HttpServletRequest}.</p>
 *
 * <p>In this case, we're just using a request parameter to choose the appropriate IdP.</p>
 *
 */
public class DynamicIdPSAML2AuthenticationHandler extends SAML2AuthenticationHandler {

    public static final String DYNAMIC_IDP_URL_SESSION_ATTRIBUTE_NAME = "org.picketlink.federation.dynamic.idp.url";

    private final Map<String, String> idpRegistry = new HashMap<String, String>();

    public DynamicIdPSAML2AuthenticationHandler() {
        this.idpRegistry.put("one", "http://localhost:8080/idp-one/");
        this.idpRegistry.put("two", "http://localhost:8080/idp-two/");
    }

    @Override
    public void handleRequestType(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
        response.setDestination(resolveIdentityProviderUrl(request, response));
        super.handleRequestType(request, response);
    }

    @Override
    public void handleStatusResponseType(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
        response.setDestination(resolveIdentityProviderUrl(request, response));
        super.handleStatusResponseType(request, response);
    }

    @Override
    public void generateSAMLRequest(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
        response.setDestination(resolveIdentityProviderUrl(request, response));
        super.generateSAMLRequest(request, response);
    }

    /**
     * <p>Resolves the IdP dynamically using the information the request.</p>
     *
     * @param request
     * @param response
     * @return
     */
    private String resolveIdentityProviderUrl(SAML2HandlerRequest request, SAML2HandlerResponse response) {
        HTTPContext httpContext = (HTTPContext) request.getContext();
        HttpServletRequest httpRequest = httpContext.getRequest();
        HttpSession session = httpRequest.getSession(false);
        String idpUrl = null;

        if (session != null) {
            idpUrl = (String) session.getAttribute(DYNAMIC_IDP_URL_SESSION_ATTRIBUTE_NAME);

            // check if the idp was previously selected. if not, we try to use the parameter to select an idp.
            if (idpUrl == null) {
                idpUrl = this.idpRegistry.get(httpRequest.getParameter("IDP"));
                session.setAttribute(DYNAMIC_IDP_URL_SESSION_ATTRIBUTE_NAME, idpUrl);
            }
        }

        // defaults to the idp defined in WEB-INF/picketlink.xml
        if (idpUrl == null) {
            idpUrl = response.getDestination();
        }

        return idpUrl;
    }
}
