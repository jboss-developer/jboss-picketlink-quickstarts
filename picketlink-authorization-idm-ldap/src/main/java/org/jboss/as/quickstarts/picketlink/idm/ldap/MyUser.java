package org.jboss.as.quickstarts.picketlink.idm.ldap;

import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.basic.User;

/**
 * @author Pedro Igor
 */
public class MyUser extends User {

    private String fullName;

    public MyUser() {
        this(null);
    }

    public MyUser(final String loginName) {
        super(loginName);
    }

    @AttributeProperty
    public String getFullName() {
        if (this.fullName == null) {
            this.fullName = getFirstName() + " " + getLastName();
        }

        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }
}
