/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.picketlink.idm.ldap;

import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.ldap.internal.LDAPConstants;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.basic.Grant;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.GroupMembership;
import org.picketlink.idm.model.basic.Role;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import static org.picketlink.idm.ldap.internal.LDAPConstants.CN;
import static org.picketlink.idm.ldap.internal.LDAPConstants.CREATE_TIMESTAMP;
import static org.picketlink.idm.ldap.internal.LDAPConstants.EMAIL;
import static org.picketlink.idm.ldap.internal.LDAPConstants.GROUP_OF_NAMES;
import static org.picketlink.idm.ldap.internal.LDAPConstants.SN;

@ApplicationScoped
public class IDMConfiguration {

    private static final String BASE_DN = "o=picketlink,DC=jboss,DC=test1";
    private static final String LDAP_URL = "ldaps://dev101.mw.lab.eng.bos.redhat.com:636";
    private static final String ROLES_DN_SUFFIX = "ou=Roles,o=picketlink,DC=jboss,DC=test1";
    private static final String GROUP_DN_SUFFIX = "ou=Groups,o=picketlink,DC=jboss,DC=test1";
    private static final String USER_DN_SUFFIX = "ou=People,o=picketlink,DC=jboss,DC=test1";
    private static final String AGENT_DN_SUFFIX = "ou=Agent,o=picketlink,DC=jboss,DC=test1";

    /**
     * <p>
     *     We use this method to produce a {@link IdentityConfiguration} configured with a LDAP store.
     * </p>
     *
     * @return
     */
    @Produces
    public IdentityConfiguration configure() {
        System.setProperty("javax.net.ssl.keyStore",  "/pedroigor/java/workspace_idea/jboss/picketlink/2.5/picketlink-quickstarts/picketlink-authorization-idm-ldap/keystore.jks");
        System.setProperty("javax.net.ssl.trustStore", "/pedroigor/java/workspace_idea/jboss/picketlink/2.5/picketlink-quickstarts/picketlink-authorization-idm-ldap/keystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "change_it");
        System.setProperty("javax.net.ssl.keyStorePassword", "change_it");

        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();

        builder
            .named("default")
                .stores()
                    .ldap()
                        .baseDN(BASE_DN)
                        .bindDN("JBOSS1\\jbossqa")
                        .bindCredential("jboss42")
                        .url(LDAP_URL)
                        .activeDirectory(true)
                        .supportCredentials(true)
                        .supportType(IdentityType.class)
                        .supportGlobalRelationship(Grant.class, GroupMembership.class)
                        .addCredentialHandler(MyUserPasswordCredentialHandler.class)
                        .mapping(MyUser.class)
                            .baseDN(USER_DN_SUFFIX)
                            .objectClasses("user")
                            .attribute("fullName", "CN", true)
                            .attribute("loginName", "samAccountName")
                            .attribute("firstName", LDAPConstants.GIVENNAME)
                            .attribute("lastName", SN)
                            .attribute("email", EMAIL)
                            .readOnlyAttribute("createdDate", CREATE_TIMESTAMP)
                        .mapping(Role.class)
                            .baseDN(ROLES_DN_SUFFIX)
                            .objectClasses(GROUP_OF_NAMES)
                            .attribute("name", CN, true)
                            .readOnlyAttribute("createdDate", CREATE_TIMESTAMP)
                        .mapping(Group.class)
                            .baseDN(GROUP_DN_SUFFIX)
                            .objectClasses(GROUP_OF_NAMES)
                            .attribute("name", CN, true)
                            .readOnlyAttribute("createdDate", CREATE_TIMESTAMP)
                            .parentMembershipAttributeName("member")
                            .parentMapping("QA Group", "ou=QA,dc=jboss,dc=org")
                        .mapping(Grant.class)
                            .forMapping(Role.class)
                            .attribute("assignee", "member")
                        .mapping(GroupMembership.class)
                            .forMapping(Group.class)
                            .attribute("member", "member");

        return builder.build();
    }

}
