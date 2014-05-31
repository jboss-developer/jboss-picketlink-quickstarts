package org.jboss.as.quickstarts.picketlink.angularjs.security.service;

import org.jboss.as.quickstarts.picketlink.angularjs.model.Person;
import org.jboss.as.quickstarts.picketlink.angularjs.security.authorization.AllowedRole;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.ApplicationRole;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.IdentityModelManager;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.MyUser;
import org.jboss.as.quickstarts.picketlink.angularjs.util.EntityValidator;
import org.jboss.as.quickstarts.picketlink.angularjs.util.MessageBuilder;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@javax.ejb.Stateless
@Path("/private/account")
@AllowedRole(ApplicationRole.ADMINISTRATOR)
public class AccountService {

    @Inject
    private IdentityModelManager identityModelManager;
    
    @Inject
    private EntityValidator validator;

    @POST
    @Path("enableAccount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enable(@NotNull Person passedUser) {
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
    public Response disable(@NotNull Person passedUser) {
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
