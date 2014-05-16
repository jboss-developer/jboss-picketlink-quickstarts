/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.gr.project.security.model;

import com.gr.project.model.Person;
import com.gr.project.security.authentication.TokenManager;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.Token;
import org.picketlink.idm.credential.storage.TokenCredentialStorage;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.query.IdentityQuery;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static com.gr.project.security.model.ApplicationRole.ADMINISTRATOR;
import static com.gr.project.security.model.IdentityModelUtils.getRole;
import static org.picketlink.idm.model.basic.BasicModel.hasRole;

/**
 * <p>This class provides an abstraction point to the Identity Management operations required by the application./p>
 *
 * <p>The main objective of this class is avoid the spread use of the <code>IdentityManager</code> by different components of
 * the application and code duplication, providing a centralized point of access for the most common operations like create/update/query users and so forth.</p>
 *
 * <p>Also it is very useful to understand how PicketLink Identity Management is being used and what is being used by the application from a IDM perspective.</p>
 *
 * <p>Please note that PicketLink IDM provides a very flexible and poweful identity model and API, from which you can extend and fulfill your own requirements.</p>
 *
 * @author Pedro Igor
 */
@Stateless
public class IdentityModelManager {

    @Inject
    private IdentityManager identityManager;

    @Inject
    private RelationshipManager relationshipManager;

    @Inject
    private TokenManager tokenManager;

    public MyUser createAccount(Registration request) {
        if (!request.isValid()) {
            throw new IllegalArgumentException("Insuficient information.");
        }

        Person person = new Person();

        person.setEmail(request.getEmail());
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());

        MyUser newUser = new MyUser(request.getEmail());

        newUser.setPerson(person);

        String activationCode = UUID.randomUUID().toString();

        newUser.setActivationCode(activationCode); // we set an activation code for future use.

        this.identityManager.add(newUser);

        updatePassword(newUser, request.getPassword());

        disableAccount(newUser);

        return newUser;
    }

    public void updatePassword(Account account, String password) {
        this.identityManager.updateCredential(account, new Password(password));
    }

    public Token issueToken(Account account) {
        Token token = this.tokenManager.issue(account);

        this.identityManager.updateCredential(account, token);

        return token;
    }

    public void grantRole(MyUser account, ApplicationRole role) {
        Role storedRole = BasicModel.getRole(this.identityManager, role.name());
        BasicModel.grantRole(this.relationshipManager, account, storedRole);
    }

    public Token activateAccount(String activationCode) {
        MyUser user = findUserByActivationCode(activationCode);

        if (user == null) {
            throw new IllegalArgumentException("Invalid activation code.");
        }

        user.setEnabled(true);
        user.invalidateActivationCode();

        this.identityManager.update(user);

        return issueToken(user);
    }

    public void activateAccount(MyUser user) {
        activateAccount(user.getActivationCode());
    }

    public MyUser findByLoginName(String loginName) {
        return IdentityModelUtils.findByLoginName(loginName, this.identityManager);
    }

    public MyUser findUserByActivationCode(String activationCode) {
        if (activationCode == null) {
            throw new IllegalArgumentException("Invalid activation code.");
        }

        IdentityQuery<MyUser> query = identityManager.createIdentityQuery(MyUser.class);
        List<MyUser> result = query
            .setParameter(MyUser.ACTIVATION_CODE, activationCode.replaceAll("\"", ""))
            .getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    }

    public String getToken(Account account) {
        TokenCredentialStorage storage = this.identityManager.retrieveCurrentCredential(account, TokenCredentialStorage.class);

        if (storage == null) {
            return null;
        }

        return storage.getToken();
    }

    public void disableAccount(MyUser user) {
        if (hasRole(this.relationshipManager, user, getRole(ADMINISTRATOR, this.identityManager))) {
            throw new IllegalArgumentException("Administrators can not be disabled.");
        }

        user.setEnabled(false);

        if (user.getId() != null) {
            issueToken(user); // we invalidate the current token and create a new one. so any token stored by clients will be no longer valid.
            this.identityManager.update(user);
        }
    }

    public void enableAccount(MyUser user) {
        if (hasRole(this.relationshipManager, user, getRole(ADMINISTRATOR, this.identityManager))) {
            throw new IllegalArgumentException("Administrators can not be enabled.");
        }

        user.setEnabled(true);
        user.invalidateActivationCode();

        if (user.getId() != null) {
            this.identityManager.update(user);
        }


    }
}
