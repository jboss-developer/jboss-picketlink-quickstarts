package org.jboss.as.quickstarts.picketlink.authentication.jsf;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.picketlink.authentication.jsf.authentication.SimpleAuthenticator;
import org.jboss.as.quickstarts.picketlink.authentication.jsf.authentication.SmsAuthenticator;
import org.jboss.as.quickstarts.picketlink.authentication.jsf.authentication.SmsCode;
import org.picketlink.Identity;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.Authenticator;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.model.basic.User;

/**
 * @author Michal Trnka
 */
@Named
@RequestScoped
public class SmsLogInController {
    private String smsCode;

    @Inject
    DefaultLoginCredentials credentials;

    @Inject
    @Any
    Instance<SimpleAuthenticator> simpleAuthenticator;

    @Inject
    Instance<SmsAuthenticator> smsAuthenticator;

    @Inject
    Identity identity;

    @Inject
    private FacesContext facesContext;

    public void logIn() {
        if (identity.isLoggedIn()) {
            User u = (User)identity.getAccount();
            credentials.setUserId(u.getLoginName());
            credentials.setCredential(new SmsCode(smsCode));
            identity.login();
        } else {
            //without logging in before method identity.getAccount would return null as there would be no account
            facesContext.addMessage(null, new FacesMessage("You got to log in first to use SMS authentication."));
        }
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    /**
     * <p>Produces the right authenticator to be used depending on the method the user is trying to log in.</p>
     *
     * <p>Note - this producer method works for any injection of authenticator, not only for injection from this class.</p>
     * @return Correct authenticator
     */
    @Produces
    @PicketLink
    public Authenticator selectAuthenticator() {
        if (smsCode != null) {
            return smsAuthenticator.get();
        } else {
            return simpleAuthenticator.get();
        }
    }
}
