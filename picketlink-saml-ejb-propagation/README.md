picketlink-saml-ejb-propagation: Propagation of SAML tokens between web and EJB layers
====================================================================
Author: Pedro Igor
Level: Advanced
Technologies: EJB, EAR, PicketLink, SAML, WS-Trust
Summary: Propagates a SAML token between the web and EJB layers
Target Product: EAP
Source: <https://github.com/picketlink/picketlink-quickstarts/>

What is it?
-----------

This example demonstrates the deployment of an EAR artifact. The EAR contains: Simple WAR and an *EJB 3.1* JAR.

The example is composed of three maven projects, each with a shared parent. The projects are as follows:

1. `ejb`: This project contains the EJB code and can be built independently to produce the JAR archive.

2. `sts`: This project contains the PicketLink Security Token Service, responsible to issue and validate SAML tokens using WS-Trust.

3. `idp`: This project contains the PicketLink Identity Provider, responsible for the SAML Web Browser SSO.

4. `web`: This project contains a Service Provider with a Servlet that propagates the SAML assertion when invoking an EJB.

5. `ear`: This project builds the EAR artifact and pulls in the ejb and web artifacts.

The root `pom.xml` builds each of the subprojects in the above order and deploys the EAR archive to the server.

This example demonstrates how you can use PicketLink Federation and its SAML support to authenticate your users
and use their SAML assertions to propagate the security context when invoking the EJB layer.

The steps below summarize what you'll see from this example:

1. The user tries to to access the web application and is redirect to the IDP.

2. The user provide his credentials and is authenticated. After that the user is redirect back to the web application.

3. Once authenticated, the user can access the URL: http://localhost:8080/jboss-as-picketlink-saml-ejb-propagation/test. This URL is mapped to a Servlet that
knows how to invoke an EJB propagating the user's security context using his SAML assertion.

4. The EJB has two protected methods. The *echo* method is allowed only for users with the *STSClient* role. The *echoManager* method is
allowed only for users with the "Manager" role. By default, users have only the *STSClient* role granted, what means that the invocation
of the "echoManager* method will not be allowed.

Configuring EJB3 Remoting Authentication
-------------------

Before deploying the application you must change the remoting security configuration as follows. All configurations must be done
in your JBOSS_HOME/standalone/configuration/standalone.xml.

Add a security-realm:

        <security-realm name="SAMLRealm">
            <authentication>
                <jaas name="ejb-remoting-sts"/>
            </authentication>
        </security-realm>

Change the remoting subsystem as follows:

        <subsystem xmlns="urn:jboss:domain:remoting:1.1">
            <connector name="remoting-connector" socket-binding="remoting" security-realm="SAMLRealm"/>
        </subsystem>

Configuring the Security Domains
-------------------

Before deploying the application you must configure some security domains in your JBOSS_HOME/standalone/configuration/standalone.xml.

Add the security domain for the web application (Service Provider):

        <security-domain name="sp" cache-type="default">
            <authentication>
                <login-module code="org.picketlink.identity.federation.bindings.jboss.auth.SAML2LoginModule" flag="required"/>
            </authentication>
        </security-domain>

Add the security domain for the PicketLink STS:

        <security-domain name="picketlink-sts" cache-type="default">
            <authentication>
                <login-module code="UsersRoles" flag="required">
                    <module-option name="usersProperties" value="${jboss.server.config.dir}/users.properties"/>
                    <module-option name="rolesProperties" value="${jboss.server.config.dir}/roles.properties"/>
                </login-module>
            </authentication>
        </security-domain>

Add the security domain for the PicketLink Identity Provider:

        <security-domain name="idp" cache-type="default">
            <authentication>
                <login-module code="UsersRoles" flag="required">
                    <module-option name="usersProperties" value="${jboss.server.config.dir}/users.properties"/>
                    <module-option name="rolesProperties" value="${jboss.server.config.dir}/roles.properties"/>
                </login-module>
            </authentication>
        </security-domain>

Add the security domain for the EJB:

        <security-domain name="picketlink-saml-ejb-propagation-ejb" cache-type="default">
            <authentication>
                <login-module code="org.picketlink.identity.federation.bindings.jboss.auth.SAML2STSLoginModule" flag="required" module="org.picketlink">
                    <module-option name="configFile" value="${jboss.server.config.dir}/sts-config.properties"/>
                    <module-option name="roleKey" value="Role"/>
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
            </authentication>
        </security-domain>

Add the security domain for EJB Remoting:

        <security-domain name="ejb-remoting-sts" cache-type="default">
            <authentication>
                <login-module code="org.picketlink.identity.federation.bindings.jboss.auth.SAML2STSLoginModule" flag="required" module="org.picketlink">
                    <module-option name="configFile" value="${jboss.server.config.dir}/sts-config.properties"/>
                    <module-option name="roleKey" value="Role"/>
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
            </authentication>
        </security-domain>

Copy the users/roles and sts configuration properties files
-------------------

Please copy all files from PROJECT_BASE_DIR/config to JBOSS_HOME/standalone/configuration.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.


Configure Maven
-------------

If you have not yet done so, you must [Configure Maven](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/#configure_maven) before testing the quickstarts.


Start JBoss EAP 6.1
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-picketlink-saml-ejb-propagation.ear` to the running instance of the server.



Access the application
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-as-picketlink-saml-ejb-propagation>.

Enter a name in the input field and click the _Greet_ button to see the response.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts)


Debug the Application
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc