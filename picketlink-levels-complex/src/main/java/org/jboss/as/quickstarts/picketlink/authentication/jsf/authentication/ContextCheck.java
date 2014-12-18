package org.jboss.as.quickstarts.picketlink.authentication.jsf.authentication;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.as.quickstarts.picketlink.authentication.model.UserSettings;
import org.picketlink.Identity;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.levels.Level;
import org.picketlink.authentication.levels.SecurityLevelResolver;
import org.picketlink.authentication.levels.internal.DefaultLevel;

/**
 * <p>This custom {@link org.picketlink.authentication.levels.SecurityLevelResolver} resolve a security level based on
 * the request IP.</p>
 *
 * <p>You can perform any logic at this moment in order to resolve levels based on any contextual information.</p>
 *
 * @author Michal Trnka
 */
@PicketLink
public class ContextCheck implements SecurityLevelResolver {

    @Inject
    Identity identity;

    @Inject
    UserSettings settings;

    @Inject
    FacesContext fc;

    @Override
    public Level resolve() {
        if(identity.isLoggedIn()){
            HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();

            //very simple check if the IP of the request is same as the one saved in settings
            if(request.getRemoteAddr().equals(settings.getIp())){
                //because we did not define our own Level implementation we use default
                return new DefaultLevel(3);
            }
        }

        return null;
    }

}
