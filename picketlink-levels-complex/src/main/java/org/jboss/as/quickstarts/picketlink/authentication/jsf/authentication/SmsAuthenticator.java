package org.jboss.as.quickstarts.picketlink.authentication.jsf.authentication;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.model.basic.User;

/**
 * <p>Simulation of the authorization by smsCode. Code is hardcoded into this example.</p>
 *
 * @author Michal Trnka
 */
public class SmsAuthenticator extends BaseAuthenticator {

    @Inject
    private DefaultLoginCredentials credentials;

    @Inject
    private FacesContext facesContext;

    @Override
    public void authenticate() {
        SmsCode code = (SmsCode) credentials.getCredential();
        if ("123qwert".equals(code.getCode())) {
            setStatus(AuthenticationStatus.SUCCESS);
            setAccount(new User(credentials.getUserId()));

            facesContext.addMessage(null, new FacesMessage(
                    "Congratulations! You have raised your security level!"));
            NavigationHandler nav = facesContext.getApplication().getNavigationHandler();
            nav.handleNavigation(facesContext, null,"/home");
        } else {
            setStatus(AuthenticationStatus.FAILURE);
            facesContext.addMessage(null, new FacesMessage("Authentication Failure - The sms you provided is wrong"));
        }
    }
}
