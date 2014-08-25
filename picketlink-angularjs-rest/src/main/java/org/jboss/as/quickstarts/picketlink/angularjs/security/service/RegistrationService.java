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
package org.jboss.as.quickstarts.picketlink.angularjs.security.service;

import org.jboss.as.quickstarts.picketlink.angularjs.model.Email;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.IdentityModelManager;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.MyUser;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.UserRegistration;
import org.jboss.as.quickstarts.picketlink.angularjs.util.MessageBuilder;
import org.picketlink.idm.credential.Token;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.jboss.as.quickstarts.picketlink.angularjs.security.model.ApplicationRole.USER;

/**
 * <p>RESTFul endpoint responsible for:</p>
 *
 * <ul>
 *     <li>Create a new user account and send a notification with the activation code.</li>
 *     <li>Activate a previously created account based on a activation code..</li>
 * </ul>
 *
 * <p>
 *  After a successful registration, an account is always disabled. In order to enable the account and be able to log in,
 *  the activation code must be used to invoke the <code>enableAccount</code> resource.
 * </p>
 *
 * @author Pedro Igor
 */
@Stateless
@Path("/register")
public class RegistrationService {

    @Inject
    private IdentityModelManager identityModelManager;

    @Inject
    @Any
    private Event<Email> event;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMember(UserRegistration request) {
        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            return MessageBuilder.badRequest().message("Password mismatch.").build();
        }

        MessageBuilder message;

        try {
            // if there is no user with the provided e-mail, perform registration
            if (this.identityModelManager.findByLoginName(request.getEmail()) == null) {
                MyUser newUser = this.identityModelManager.createAccount(request);
                
                this.identityModelManager.grantRole(newUser, USER);
                
                String activationCode = newUser.getActivationCode();

                sendNotification(request, activationCode);

                message = MessageBuilder.ok().activationCode(activationCode);
            } else {
                message = MessageBuilder.badRequest().message("This username is already in use. Try another one.");
            }
        } catch (Exception e) {
            message = MessageBuilder.badRequest().message(e.getMessage());
        }

        return message.build();
    }

    @POST
    @Path("/activation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateAccount(String activationCode) {
        MessageBuilder message;

        try {
            Token token = this.identityModelManager.activateAccount(activationCode);
            message = MessageBuilder.ok().token(token.getToken());
        } catch (Exception e) {
            message = MessageBuilder.badRequest().message(e.getMessage());
        }

        return message.build();
    }


    private void sendNotification(UserRegistration request, String activationCode) {
        Email email = new Email("Please complete the signup", "http://localhost:8080/picketlink-angularjs-rest/#/activate/" + activationCode, request.getEmail());

        event.fire(email);
    }
}
