package org.jboss.as.quickstarts.picketlink.authentication.idm.jsf;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.User;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Pedro Igor
 */
@Named
public class AuthenticationManager implements Serializable {

    @Inject
    private IdentityManager identityManager;

    public User authenticate(String userName, String password) {
        if (userName != null && password != null) {
            User user = BasicModel.getUser(this.identityManager, userName);

            if (user != null) {
                UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();

                credentials.setUsername(user.getLoginName());
                credentials.setPassword(new Password(password));

                this.identityManager.validateCredentials(credentials);

                if (Credentials.Status.VALID.equals(credentials.getStatus())) {
                    return user;
                }
            }
        }

        return null;
    }
}
