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
package org.jboss.as.quickstarts.picketlink.angularjs.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pedro Igor
 */
public class MessageBuilder {

    public static final String MESSAGE_PARAMETER = "message";
    public static final String ACTIVATION_CODE_PARAMETER = "activationCode";
    public static final String TOKEN_PARAMETER = "token";

    private final ResponseBuilder response;
    private final Map<String, Object> messageData = new HashMap<String, Object>();

    public MessageBuilder(ResponseBuilder response) {
        this.response = response;
    }

    public static MessageBuilder badRequest() {
        return new MessageBuilder(Response.status(Response.Status.BAD_REQUEST));
    }

    public static MessageBuilder ok() {
        return new MessageBuilder(Response.ok());
    }

    public static MessageBuilder authenticationRequired() {
        return new MessageBuilder(Response.status(Response.Status.UNAUTHORIZED));
    }

    public static MessageBuilder accessDenied() {
        return new MessageBuilder(Response.status(Response.Status.FORBIDDEN));
    }

    @SuppressWarnings("unchecked")
    public MessageBuilder message(String... message) {
		List<String> actualMessages = (List<String>) this.messageData.get(MESSAGE_PARAMETER);

        if (actualMessages == null) {
            actualMessages = new ArrayList<String>();
            this.messageData.put(MESSAGE_PARAMETER, actualMessages);
        }

        actualMessages.addAll(Arrays.asList(message));

        return this;
    }

    public MessageBuilder activationCode(String activationCode) {
        this.messageData.put(ACTIVATION_CODE_PARAMETER, activationCode);
        return this;
    }

    public MessageBuilder token(String token) {
        this.messageData.put(TOKEN_PARAMETER, token);
        return this;
    }

    public Response build() {
        return this.response.entity(this.messageData).build();
    }
}
