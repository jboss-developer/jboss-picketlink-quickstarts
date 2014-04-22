package com.gr.project.security.service;

import com.gr.project.model.Person;
import com.gr.project.rest.MessageBuilder;
import com.gr.project.security.authorization.AllowedRole;
import com.gr.project.security.model.ApplicationRole;
import com.gr.project.security.model.IdentityModelManager;
import com.gr.project.security.model.MyUser;
import com.gr.project.util.EntityValidator;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@javax.ejb.Stateless
@Path("/admin")
@AllowedRole(ApplicationRole.ADMINISTRATOR)
public class AdminService {

    @Inject
    private IdentityModelManager identityModelManager;
    
    @Inject
    private EntityValidator validator;

    @POST
    @Path("enableAccount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enableAccount(@NotNull Person passedUser) {
        MessageBuilder message;

        try {
        	// validate input
        	validator.validateEntity(passedUser);
        	
            MyUser user = this.identityModelManager.findByLoginName(passedUser.getEmail());

            if (user == null) {
                return MessageBuilder.badRequest().message("Invalid account.").build();
            }
            
            if(user.isEnabled()) {
                return MessageBuilder.badRequest().message("Account is already enabled.").build();
            }

            this.identityModelManager.enableAccount(user);

            message = MessageBuilder.ok().message("Account is now enabled.");
        } catch (Exception e) {
            message = MessageBuilder.badRequest().message(e.getMessage());
        }

        return message.build();
    }

    @POST
    @Path("disableAccount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response disableAccount(@NotNull Person passedUser) {
        MessageBuilder message;

        try {
            // validate input
            validator.validateEntity(passedUser);

            MyUser user = this.identityModelManager.findByLoginName(passedUser.getEmail());

            if (user == null) {
                return MessageBuilder.badRequest().message("Invalid account.").build();
            }

            if(!user.isEnabled()) {
                return MessageBuilder.badRequest().message("Accound is already disabled.").build();
            }

            this.identityModelManager.disableAccount(user);

            message = MessageBuilder.ok().message("Account is now disabled.");
        } catch (Exception e) {
            message = MessageBuilder.badRequest().message(e.getMessage());
        }

        return message.build();
    }
}
