picketlink-federation-saml-sp-post-with-ssl: PicketLink Service Provider With a Basic Configuration using SAML HTTP POST Binding With Signature Support
===============================
Author: Pedro Igor
Level: Intermediate
Technologies: PicketLink Federation, SAML v2.0
Summary: Basic example that demonstrates how to setup an application as a SAML v2.0 Service Provider using SAML HTTP POST Binding with Signature Support.
Source: <https://github.com/picketlink/picketlink-quickstarts/>


What is it?
-----------

This example demonstrates the use of *PicketLink Federation* SAML v2.0 support to setup an application as a Service Provider in *JBoss Enterprise Application Platform 6* or *WildFly*.

It provides a minimal configuration to enable your application as a Service Provider, accordingly with the SAML v2.0 specification. A Service Provider is an application that participates in a Single Sign-On as a relying party.
It relies on the Identity Provider to authenticate users and issue SAML Assertions, which will be used by the Service Provider to authenticate the user locally.

This example relies on *picketlink-federation-saml-idp-with-ssl* in order to perform the authentication based on a client certificate using SSL. 

Although this example provides you a good start to understand how a Service Provider works, it does not provide some other important security-related configurations such as signature and encryption.
We strongly recommend you to read our documentation about how to make your deployment safe and secure from a SAML perspective.

Before you run this example, you must have a security-domain configuration in your server to authenticate users based on the SAML Assertion issued by the Identity Provider.

You can checkout the SAML v2.0 specification from [here](http://saml.xml.org/saml-specifications). We strongly recommend you to spend some time understanding at least the basic core concepts from it.

The latest PicketLink documentation is available [here](http://docs.jboss.org/picketlink/2/latest/).

*Note: A Service Provider alone is not very useful without an Identity Provider to authenticate users and issue SAML Assertions. Once you get this application deployed, please take a look at [About the PicketLink Federation Quickstarts](../README.md#about-the-picketlink-federation-quickstarts).*

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or WildFly.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/#configure_maven) before testing the quickstarts.

Create the Security Domain for JBoss EAP
---------------

These steps assume you are running the server in standalone mode and using the default standalone.xml supplied with the distribution.

You configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-security-domain-eap.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the JBoss server.
    * Backup the file: `JBOSS_HOME/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the JBoss server by typing the following:

        For Linux:  JBOSS_HOME/bin/standalone.sh
        For Windows:  JBOSS_HOME\bin\standalone.bat
3. Review the `configure-security-domain-eap.cli` file in the root of this quickstart directory. This script adds the `sp` domain to the `security` subsystem in the server configuration and configures authentication access. Comments in the script describe the purpose of each block of commands.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-security-domain-eap.cli

You should see the following result when you run the script:

        The batch executed successfully
        {
            "outcome" => "success",
        }


Create the Security Domain for WildFly
---------------

These steps assume you are running the server in standalone mode and using the default standalone.xml supplied with the distribution.

You configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-security-domain-wildfly.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the JBoss server.
    * Backup the file: `JBOSS_HOME/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the JBoss server by typing the following:

        For Linux:  JBOSS_HOME/bin/standalone.sh
        For Windows:  JBOSS_HOME\bin\standalone.bat
3. Review the `configure-security-domain-wildfly.cli` file in the root of this quickstart directory. This script adds the `sp` domain to the `security` subsystem in the server configuration and configures authentication access. Comments in the script describe the purpose of each block of commands.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-security-domain-wildfly.cli

You should see the following result when you run the script:

        The batch executed successfully
        {
            "outcome" => "success",
        }



Review the Modified Server Configuration for EAP
-----------------------------------

If you want to review and understand newly added XML configuration, stop the JBoss server and open the  `JBOSS_HOME/standalone/configuration/standalone.xml` file.

The following `sp` security-domain was added to the `security` subsystem.

        <security-domain name="sp" cache-type="default">
            <authentication>
                <login-module code="org.picketlink.identity.federation.bindings.jboss.auth.SAML2LoginModule" flag="required"/>
            </authentication>
        </security-domain>

The configuration above defines a security-domain which will be used by the SP to authenticate users based on a SAML Assertion previously issued by a Identity Provider.

Review the Modified Server Configuration for WildFly
-----------------------------------

If you are using Wildfly, the security-domain should have the following configuration:

        <security-domain name="sp" cache-type="default">
            <authentication>
                <login-module code="org.picketlink.identity.federation.bindings.wildfly.SAML2LoginModule" flag="required"/>
            </authentication>
        </security-domain>


SAML SP-Initiated Single Sign-On
-----------------------------------

The SAML v2.0 specification defines a specific SSO mode called *SP-Initiated SSO*. In this mode, the SSO flow starts at the Service Provider side.
Please, take a look at the following documentation for more details:

1. [SAML v2.0 SP-Initiated SSO](https://docs.jboss.org/author/display/PLINK/SP-Initiated+SSO)


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

4. This will deploy `target/picketlink-federation-saml-sp-post-with-ssl.war` to the running instance of the server.


Access the application
---------------------

The application will be running at the following URL: <http://localhost:8080/sales-post-sig>.

*Note: A Service Provider alone is not very useful without an Identity Provider to authenticate users and issue SAML Assertions. Once you get this application deployed, please take a look at [About the PicketLink Federation Quickstarts](../README.md#about-the-picketlink-federation-quickstarts).*

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