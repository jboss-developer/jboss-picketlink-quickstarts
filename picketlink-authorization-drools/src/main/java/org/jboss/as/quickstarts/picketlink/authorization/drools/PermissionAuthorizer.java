package org.jboss.as.quickstarts.picketlink.authorization.drools;

import javax.enterprise.context.ApplicationScoped;

import org.apache.deltaspike.security.api.authorization.Secures;
import org.picketlink.Identity;

/**
 * Performs the business logic required for the declared security binding annotations
 *
 * @author Shane Bryzak
 *
 */
@ApplicationScoped
public class PermissionAuthorizer {
    @Secures
    @TimeRestricted
    public boolean testTimeRestricted(Identity identity) {
        return identity.hasPermission("TestAction", "invoke");
    }
}
