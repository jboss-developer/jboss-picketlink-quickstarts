picketlink-angularjs-rest: PicketLink AngularJS and REST Security
===============================
Author: Pedro Igor
Level: Intermediate
Technologies: CDI, PicketLink, REST, HTML5, AngularJS, JSON, JWT, JWS
Summary: Demonstrates how to use PicketLink and Angular.js to secure a RESTful (JAX-RS) JEE application.
Source: <https://github.com/picketlink/picketlink-quickstarts/>


What is it?
-----------

Before you run this example, you must create certificates and configure the server to use SSL and validate client certificates.

The latest PicketLink documentation is available [here](http://docs.jboss.org/picketlink/2/latest/).

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or WildFly.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/#configure_maven) before testing the quickstarts.


Create the Client Certicates
------------------------

1.  Open a command line and navigate to the JBoss server `configuration` directory:

        For Linux:   JBOSS_HOME/standalone/configuration
        For Windows: JBOSS_HOME\standalone\configuration
2. Create a certificate for your server using the following command:

        keytool -genkey -alias server -keyalg RSA -keystore server.keystore -storepass change_it -validity 365

   You'll be prompted for some additional information, such as your name, organizational unit, and location. Enter any values you prefer.
3. Create the client certificate, which is used to authenticate against the server when accessing a resource through SSL.

         keytool -genkey -alias client -keystore client.keystore -storepass change_it -validity 365 -keyalg RSA -keysize 2048 -storetype pkcs12 -dname "CN=client, OU=Org. Unit, O=Company, ST=NC, C=US"
4. Export the client certificate and create a truststore by importing this certificate:

        keytool -exportcert -keystore client.keystore  -storetype pkcs12 -storepass change_it -alias client -keypass change_it -file client.cer
        keytool -import -file client.cer -alias client -keystore client.truststore
5. The certificates and keystores are now properly configured.


Configure the Server to Use SSL 
--------------------------------

Now that the certificates and keystores are properly configured, you must enable SSL in the server configuration. 

### Configure the HTTPS Connector in the Web Subsystem by Running the JBoss CLI Script

1. Start the JBoss Enterprise Application Platform 6 or WildFly Server by typing the following:

        For Linux:  JBOSS_HOME/bin/standalone.sh 
        For Windows:  JBOSS_HOME\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-https.cli

This script adds and configures the `https` connector to the `web` subsystem in the server configuration. You should see the following result when you run the script:

        {"outcome" => "success"}
        {"outcome" => "success"}
        {"outcome" => "success"}

This command reloads the server configuration before completion. You don`t need to manually stop/start the server to the configuration take effect.

### Configure the HTTPS Connector in the Web Subsystem by Manually Editing the Server Configuration File

1.  If it is running, stop the JBoss Enterprise Application Platform 6 or WildFly Server.
2.  Backup the file: `JBOSS_HOME/standalone/configuration/standalone.xml`
3.  Open the `JBOSS_HOME/standalone/configuration/standalone.xml` file in an editor and locate the subsystem `urn:jboss:domain:web`.
4.  Add the following XML to the `web` subsystem:

        <connector name="https" protocol="HTTP/1.1" scheme="https" socket-binding="https" enable-lookups="false" secure="true">
            <ssl name="localhost-ssl" key-alias="server" password="change_it"
                certificate-key-file="${jboss.server.config.dir}/server.keystore"
                protocol="TLSv1"
                verify-client="want"
                ca-certificate-file="${jboss.server.config.dir}/client.truststore"/>
        </connector>


Test the Server SSL Configuration
---------------------------------

To test the SSL configuration, access: <https://localhost:8443>

If it is configured correctly, you should be asked to trust the server certificate.

Configuring the Mail Service
----------------------------

In order to configure the email JNDI resource please follow this instructions:

* Edit standalone.xml, search for the mail subsystem:

add following mail-session as follows:

	<mail-session name="App" jndi-name="java:/mail/gmail">
		<smtp-server outbound-socket-binding-ref="mail-smtp-gmail" ssl="true" username="YOUR_GMAIL_EMAIL" password="YOUR_GMAIL_PASSWORD"/>
	</mail-session>

* search for **outbound-socket-binding name="mail-smtp"**

Add the following outbound-socket-binding:

	<outbound-socket-binding name="mail-smtp-gmail">
		<remote-destination host="smtp.gmail.com" port="465"/>
	</outbound-socket-binding>

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


Access the application 
---------------------

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

### Remove the Security Domain Configuration by Running the JBoss CLI Script

1. Start the JBoss Enterprise Application Platform 6 or WildFly Server by typing the following:

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=remove-https.cli 
This script removes the `https` connector from the `web` subsystem in the server configuration. You should see the following result when you run the script:

        {"outcome" => "success"}
        {"outcome" => "success"}


### Remove the Security Domain Configuration Manually
1. If it is running, stop the JBoss Enterprise Application Platform 6 or WildFly Server.
2. Replace the `JBOSS_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc