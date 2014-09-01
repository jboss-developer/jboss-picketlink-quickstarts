picketlink-levels-complex: PicketLink Authentication with JSF
===============================
Author: Michal Trnka  
Level: Intermediate  
Technologies: CDI, PicketLink, JSF  
Summary: Example that demonstrates complex usage of security-levels in PicketLink  
Source: <https://github.com/jboss-developer/jboss-picketlink-quickstarts/>

What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *PicketLink* in *JBoss Enterprise Application Platform 6* or *WildFly*.

You'll learn from this quickstart how to use PicketLink to assign security levels to your users based on the authenticator they used and on the context they have.

The application provides a base page representing e-shop. You can always add books to basket, but in order to pay, you must be logged in. User can log in using password/username. Logging in gives the user security level 2.

If a user tries to access payment page manually and tries to pay he will cause exception, because in order to pay he needs to have level 2. This is here just as example, because in real application @loggedIn rule would work better to assure that.

Once the user is logged in, he can see his settings within e-shop. The settings contain the by address for delivering his orders and the trusted IP.

In order to change those settings user must have security level 3. It can be obtained by two ways. The first is to log in from trusted IP and the second is to verify with sms code.

The change of the user settings simulates manipulation of the confidential or critical data, where the user needs to be more trusted then for normal operations. Also it show the way how the security level resolvers can check the user's context.

The latest PicketLink documentation is available [here](http://docs.jboss.org/picketlink/2/latest/).

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or WildFly.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/#configure_maven) before testing the quickstarts.


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

4. This will deploy `target/picketlink-authentication-jsf.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/picketlink-authentication-jsf>.


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
