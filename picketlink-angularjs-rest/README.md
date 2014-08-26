picketlink-angularjs-rest: PicketLink AngularJS and REST Security
===============================
Author: Maximos Sapranidis
Level: Intermediate  
Technologies: CDI, PicketLink, REST, HTML5, AngularJS, JSON, JWT, JWS  
Summary: Demonstrates how to use PicketLink and Angular.js to secure a RESTful (JAX-RS) JEE application.  
Source: <https://github.com/jboss-developer/jboss-picketlink-quickstarts/>


What is it?
-----------

This example demonstrates the use of *PicketLink* in HTML5 + AngularJS + RESTful applications in *JBoss Enterprise Application Platform* or *WildFly*.

The application provides an authentication and registration page. Once the user is registered, he must activate his account by
accessing the following URL:

    https://localhost:8443/Project/#/activate/[ACTIVATION_CODE]

The activation code is sent to the e-mail provided during the registration process. You can also activate an account by log in as adminstrator.
Only administrators are allowed to enable/disable accounts:

    Username: admin@picketlink.org
    Password: admin

Once the account is activated, the user is able to *Sign In* using his email and password.

This application demonstrates how you can use PicketLink to:

* Secure HTML5-based applications using AngularJS at the frontend
* Secure RESTful endpoints based on RBAC and Security Annotations
* Token-based Authentication using JSON Web Token and JSON Web Signature
* Provide a custom credential type and handler
* Provide your own types to represent your users

Before you run this example, you must create certificates and configure the server to use SSL. You must also
provide a Mail configuration in your server in order to send the activation code to new users.

The latest PicketLink documentation is available [here](http://docs.jboss.org/picketlink/2/latest/).

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or WildFly.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/#configure_maven) before testing the quickstarts.


Create the Server Certicate
------------------------

1. Open a command line and navigate to the JBoss server `configuration` directory:

        For Linux:   JBOSS_HOME/standalone/configuration
        For Windows: JBOSS_HOME\standalone\configuration
2. Create a certificate for your server using the following command:

        keytool -genkey -alias server -keyalg RSA -keystore server.keystore -storepass change_it -validity 365

   You'll be prompted for some additional information, such as your name, organizational unit, and location. Enter any values you prefer.
3. The certificates and keystores are now properly configured.


## Configure the Server to Use SSL

Now that the certificates and keystores are properly configured, you must enable SSL in the server configuration. 

### Configure the HTTPS Connector in the Web Subsystem by Running the JBoss CLI Script (JBoss Enterprise Application Platform)

1. Start the WildFly Server by typing the following:

        For Linux:  JBOSS_HOME/bin/standalone.sh 
        For Windows:  JBOSS_HOME\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-https.cli

This script adds and configures the `https` connector to the `web` subsystem in the server configuration. You should see the following result when you run the script:

        {"outcome" => "success"}
        {"outcome" => "success"}
        {"outcome" => "success"}

This command reloads the server configuration before completion. You don`t need to manually stop/start the server to the configuration take effect.

### Configure the HTTPS Connector in the Web Subsystem by Running the JBoss CLI Script (WildFly)

In order to configure SSL support on Wildfly please follow this instructions:

1. Edit standalone.xml, search for **"urn:jboss:domain:undertow:1.0"** and add following listener: **https-listener name="default-https" socket-binding="https" security-realm="ssl-realm"**
Final undertow subsystem should look like this one:

	<subsystem xmlns="urn:jboss:domain:undertow:1.0">
		<buffer-caches>
			<buffer-cache name="default" buffer-size="1024" buffers-per-region="1024" max-regions="10"/>
        </buffer-caches>
        <server name="default-server">
			<http-listener name="default" socket-binding="http"/>
            <https-listener name="default-https" socket-binding="https" security-realm="ssl-realm"/>
            <host name="default-host" alias="localhost">
				<location name="/" handler="welcome-content"/>
            </host>
        </server>
        <servlet-container name="default" default-buffer-cache="default" stack-trace-on-error="local-only">
			<jsp-config/>
            <persistent-sessions/>
        </servlet-container>
        <handlers>
			<file name="welcome-content" path="${jboss.home.dir}/welcome-content" directory-listing="true"/>
        </handlers>
    </subsystem>

2. Edit standalone.xml, search for **security-realms**

Add following security-realm:

	<security-realm name="ssl-realm">
		<server-identities>
			<ssl>
			<keystore path="server.keystore" relative-to="jboss.server.config.dir"
			keystore-password="SUPER_SECRET_PASS" alias="server" key-password="SUPER_SECRET_PASS"
			/>
			</ssl>
		</server-identities>
		<authentication>
			<local default-user="$local"/>
			<properties path="mgmt-users.properties" relative-to="jboss.server.config.dir"/>
		</authentication>
	</security-realm>

Final security realms should look like:

	<security-realms>
		<security-realm name="ManagementRealm">
			<authentication>
				<local default-user="$local"/>
				<properties path="mgmt-users.properties" relative-to="jboss.server.config.dir"/>
			</authentication>
			<authorization map-groups-to-roles="false">
				<properties path="mgmt-groups.properties" relative-to="jboss.server.config.dir"/>
			</authorization>
		</security-realm>
		<security-realm name="ssl-realm">
			<server-identities>
				<ssl>
					<keystore path="server.keystore" relative-to="jboss.server.config.dir" keystore-password="SUPER_SECRET_PASS" alias="server" key-password="SUPER_SECRET_PASS" />
				</ssl>
			</server-identities>
			<authentication>
				<local default-user="$local"/>
				<properties path="mgmt-users.properties" relative-to="jboss.server.config.dir"/>
			</authentication>
		</security-realm>
		<security-realm name="ApplicationRealm">
			<authentication>
				<local default-user="$local" allowed-users="*"/>
				<properties path="application-users.properties" relative-to="jboss.server.config.dir"/>
			</authentication>
			<authorization>
				<properties path="application-roles.properties" relative-to="jboss.server.config.dir"/>
			</authorization>
		</security-realm>
	</security-realms>

Test the Server SSL Configuration
---------------------------------

To test the SSL configuration, access: <https://localhost:8443>

If it is configured correctly, you should be asked to trust the server certificate.

Configuring the Mail Service in the Mail Subsystem by Running the JBoss CLI Script (JBoss Enterprise Application Platform)
----------------------------

Before configuring the mail service, please generate a application specific password as described in this article:

    https://support.google.com/accounts/answer/185833?hl=en

You must also change the *configure-mail.cli* script to provide your own GMail username and the application specific password.

1. Start the WildFly Server by typing the following:

        For Linux:  JBOSS_HOME/bin/standalone.sh
        For Windows:  JBOSS_HOME\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-mail.cli

This script adds and configures the `https` connector to the `web` subsystem in the server configuration. You should see the following result when you run the script:

        {"outcome" => "success"}
        {"outcome" => "success"}
        {"outcome" => "success"}

This command reloads the server configuration before completion. You don`t need to manually stop/start the server to the configuration take effect.

Configuring the Mail Service in the Mail Subsystem by Running the JBoss CLI Script (WildFly)
----------------------------

In order to configure the email JNDI resource please follow this instructions:

1) Edit standalone.xml, search for **subsystem xmlns="urn:jboss:domain:mail:2.0"** and add following mail-session in the **socket-binding-group**:

        <mail-session name="App" jndi-name="java:/mail/gmail">
            <smtp-server outbound-socket-binding-ref="mail-smtp-gmail" ssl="true" username="YOUR_GMAIL_EMAIL" password="YOUR_GMAIL_PASSWORD"/>
        </mail-session>

The final one should be like this one:

	<subsystem xmlns="urn:jboss:domain:mail:2.0">
		<mail-session name="default" jndi-name="java:jboss/mail/Default">
			<smtp-server outbound-socket-binding-ref="mail-smtp"/>
		</mail-session>
		<mail-session name="App" jndi-name="java:/mail/gmail">
			<smtp-server outbound-socket-binding-ref="mail-smtp-gmail" ssl="true" username="YOUR_GMAIL_EMAIL" password="YOUR_GMAIL_PASSWORD"/>
		</mail-session>
	</subsystem>

2) Search for **outbound-socket-binding name="mail-smtp"** and add the following outbound-socket-binding in the **socket-binding-group**:

        <outbound-socket-binding name="mail-smtp-gmail">
            <remote-destination host="smtp.gmail.com" port="465"/>
        </outbound-socket-binding>

The final one should be like this one :

	<socket-binding-group name="standard-sockets" default-interface="public" port-offset="${jboss.socket.binding.port-offset:0}">
		<socket-binding name="management-native" interface="management" port="${jboss.management.native.port:9999}"/>
		<socket-binding name="management-http" interface="management" port="${jboss.management.http.port:9990}"/>
		<socket-binding name="management-https" interface="management" port="${jboss.management.https.port:9993}"/>
		<socket-binding name="ajp" port="${jboss.ajp.port:8009}"/>
		<socket-binding name="http" port="${jboss.http.port:8080}"/>
		<socket-binding name="https" port="${jboss.https.port:8443}"/>
		<socket-binding name="txn-recovery-environment" port="4712"/>
		<socket-binding name="txn-status-manager" port="4713"/>
		<outbound-socket-binding name="mail-smtp">
			<remote-destination host="localhost" port="25"/>
		</outbound-socket-binding>
		<outbound-socket-binding name="mail-smtp-gmail">
			<remote-destination host="smtp.gmail.com" port="465"/>
		</outbound-socket-binding>
	</socket-binding-group>


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

4. This will deploy `target/picketlink-angularjs-rest.war` to the running instance of the server.


## Access the application

The application will be running at the following URL: <https://localhost:8443/picketlink-angularjs-rest>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        For EAP 6:     mvn jboss-as:undeploy
        For WildFly:   mvn -Pwildfly wildfly:undeploy


Remove the SSL Configuration
----------------------------

You can remove the security domain configuration by running the  `remove-https.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

### Remove the SSL Configuration by Running the JBoss CLI Script

1. Start the JBoss Enterprise Application Platform 6 or WildFly Server by typing the following:

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=remove-https.cli 
This script removes the `https` connector from the `web` subsystem in the server configuration. You should see the following result when you run the script:

        {"outcome" => "success"}
        {"outcome" => "success"}


### Remove the SSL Configuration Manually
1. If it is running, stop the JBoss Enterprise Application Platform 6 or WildFly Server.
2. Replace the `JBOSS_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.


Remove the Mail Configuration by Running the JBoss CLI Script
----------------------------

1. Start the JBoss Enterprise Application Platform 6 or WildFly Server by typing the following:

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=remove-mail.cli
You should see the following result when you run the script:

        {"outcome" => "success"}
        {"outcome" => "success"}


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc