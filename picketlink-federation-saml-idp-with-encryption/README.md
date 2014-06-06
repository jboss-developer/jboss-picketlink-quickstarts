picketlink-federation-saml-idp-with-encryption: PicketLink Identity Provider With Encryption
===============================
Author: Pedro Igor
Level: Intermediate
Technologies: PicketLink Federation, SAML v2.0
Summary: Basic example that demonstrates how to setup an application as a SAML v2.0 Identity Provider With Encryption.
Source: <https://github.com/picketlink/picketlink-quickstarts/>


What is it?
-----------

This example demonstrates the use of *PicketLink Federation* SAML v2.0 support to setup an application as an Identity Provider in *JBoss Enterprise Application Platform 6* or *WildFly*.

It provides a minimal configuration to enable your application as an Identity Provider, accordingly with the SAML v2.0 specification. An Identity Provider or IdP, is responsible to centralize authentication and issue SAML Assertions to their relying parties, also known as Service Providers.
PicketLink supports both SAML v1.1 and v2.0 versions.

Although this example provides you a good start to understand how an Identity Provider works, it does not provide some other important security-related configurations such as signature and encryption.
We strongly recommend you to read our documentation about how to make your deployment safe and secure from a SAML perspective.

Before you run this example, you must have a security-domain configuration in your server to authenticate users and provide role mappings.

You can checkout the SAML v2.0 specification from [here](http://saml.xml.org/saml-specifications). We strongly recommend you to spend some time understanding at least the basic core concepts from it.

The latest PicketLink documentation is available [here](http://docs.jboss.org/picketlink/2/latest/).

*Note: An Identity Provider alone is not very useful without some Service Providers. Once you get this application deployed, please take a look at one of the Service Provider example applications to get the full SSO flow working.*

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or WildFly.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/#configure_maven) before testing the quickstarts.

Create the Security Domain
---------------

These steps assume you are running the server in standalone mode and using the default standalone.xml supplied with the distribution.

You configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-security-domain.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the JBoss server.
    * Backup the file: `JBOSS_HOME/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the JBoss server by typing the following:

        For Linux:  JBOSS_HOME/bin/standalone.sh
        For Windows:  JBOSS_HOME\bin\standalone.bat
3. Review the `configure-security-domain.cli` file in the root of this quickstart directory. This script adds the `idp` domain to the `security` subsystem in the server configuration and configures authentication access. Comments in the script describe the purpose of each block of commands.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-security-domain.cli
You should see the following result when you run the script:

        The batch executed successfully
        {
            "outcome" => "success",
        }



Review the Modified Server Configuration
-----------------------------------

If you want to review and understand newly added XML configuration, stop the JBoss server and open the  `JBOSS_HOME/standalone/configuration/standalone.xml` file.

The following `idp` security-domain was added to the `security` subsystem.

        <security-domain name="idp" cache-type="default">
            <authentication>
                    <login-module code="UsersRoles" flag="required">
                    <module-option name="usersProperties" value="users.properties"/>
                    <module-option name="rolesProperties" value="roles.properties"/>
                </login-module>
            </authentication>
        </security-domain>

The configuration above defines a security-domain which will be used by the IdP to authenticate users. This is a very simple configuration,
using a JAAS LoginModule that reads users and their corresponding roles from properties files. Both properties files, *users.properties*
and *roles.properties* are located at *src/main/resources* directory.

In a real world scenario your users and roles will not be located in properties files, but in LDAP, databases or whatever, depending
where your identity data is located.

SAML v1.1 and v2.0 IdP-Initiated Single Sign-On
-----------------------------------

Both versions of the SAML specification define a specific SSO mode called *IdP-Initiated SSO*. For more details, please take a look at the documentation below:

1. [SAML v1.1 IdP-Initiated SSO](https://docs.jboss.org/author/display/PLINK/SAML+v1.1)
2. [SAML v2.0 Unsolicited Responses](https://docs.jboss.org/author/display/PLINK/Unsolicted+Responses)

_NOTE: In order to test how this SSO mode works you must have at least one Service Provider deployed. We provide a plenty of SP example applications
along with our quickstarts, please read their instructions about how to build and deploy them.

SAML SP-Initiated Single Sign-On
-----------------------------------

The SAML v2.0 specification defines a specific SSO mode called *SP-Initiated SSO*. In this mode, the SSO flow starts at the Service Provider side.
Please, take a look at the following documentation for more details:

1. [SAML v2.0 SP-Initiated SSO](https://docs.jboss.org/author/display/PLINK/SP-Initiated+SSO)

_NOTE: In order to test how this SSO mode works you must have at least one Service Provider deployed. We provide a plenty of SP example applications
along with our quickstarts, please read their instructions about how to build and deploy them.

Start JBoss Enterprise Application Platform 6 or WildFly with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        For EAP 6:     mvn clean package jboss-as:deploy
        For WildFly:   mvn -Pwildfly clean package wildfly:deploy

4. This will deploy `target/picketlink-federation-saml-idp-with-encryption.war` to the running instance of the server.


Access the application
---------------------

The application will be running at the following URL: <http://localhost:8080/idp-enc>.

The IdP is pre-configured with a default user, whose credentials are:

    Username: tomcat
    Password: tomcat

*Note: An Identity Provider alone is not very useful without some Service Providers. Once you get this application deployed, please take a look at [About the PicketLink Federation Quickstarts](../README.md#about-the-picketlink-federation-quickstarts).*


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        For EAP 6:     mvn jboss-as:undeploy
        For WildFly:   mvn -Pwildfly wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
