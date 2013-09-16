package org.jboss.as.quickstarts.picketlink.authentication.idm.jsf;

import org.picketlink.idm.model.basic.User;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 * @author Pedro Igor
 */
@SessionScoped
@Named
public class AuthenticatedUser implements Serializable {

    private User user;

    @Inject
    private AuthenticationManager authenticationManager;

    public void login(final String userName, final String password) {
        this.user = this.authenticationManager.authenticate(userName, password);
    }

    public boolean isAuthenticated() {
        return this.user != null;
    }

    public void logout() {
        this.user = null;
    }

    public User getUser() {
        return this.user;
    }
}
