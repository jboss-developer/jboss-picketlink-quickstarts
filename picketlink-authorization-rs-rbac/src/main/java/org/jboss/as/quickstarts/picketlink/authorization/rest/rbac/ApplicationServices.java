/**
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
package org.jboss.as.quickstarts.picketlink.authorization.rest.rbac;

import org.picketlink.authorization.annotations.RolesAllowed;
import org.picketlink.credential.DefaultLoginCredentials;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.jboss.as.quickstarts.picketlink.authorization.rest.rbac.ApplicationRole.ADMINISTRATOR;
import static org.jboss.as.quickstarts.picketlink.authorization.rest.rbac.ApplicationRole.DEVELOPER;
import static org.jboss.as.quickstarts.picketlink.authorization.rest.rbac.ApplicationRole.PROJECT_MANAGER;

@Path("/")
public class ApplicationServices {

    @GET
    @Path("/risksManagement")
    @RolesAllowed({PROJECT_MANAGER, ADMINISTRATOR})
    public Response risksManagement(DefaultLoginCredentials credential) {
        return Response.ok().entity("You're allowed to manage risks.").type(MediaType.TEXT_PLAIN).build();
    }

    @GET
    @Path("/timesheet")
    @RolesAllowed({PROJECT_MANAGER, DEVELOPER, ADMINISTRATOR})
    public Response timesheet(DefaultLoginCredentials credential) {
        return Response.ok().entity("You're allowed to work with your timesheet.").type(MediaType.TEXT_PLAIN).build();
    }

    @GET
    @Path("/systemAdministration")
    @RolesAllowed({ADMINISTRATOR})
    public Response systemAdministration(DefaultLoginCredentials credential) {
        return Response.ok().entity("You're allowed to perform system administration tasks.").type(MediaType.TEXT_PLAIN).build();
    }

}
