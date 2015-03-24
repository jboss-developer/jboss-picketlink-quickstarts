picketlink-events: PicketLink HTTP FORM Authentication
===============================
Author: Pedro Igor
Level: Beginner
Technologies: CDI, PicketLink, JSF
Summary: Basic example that demonstrates how to listen for PL specific CDI events
Source: <https://github.com/jboss-developer/jboss-picketlink-quickstarts/>


What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *PicketLink* in *JBoss Enterprise Application Platform 6* or *WildFly*.

You'll learn from this quickstart how to listen for security events fired during authentication, logout and when performing
identity management operations.

When running this example, you should see some messages in the server logs similar as these ones:

    INFO [org.jboss.as.quickstarts.picketlink.authentication.events.ExampleObserver] User login
    INFO  [org.jboss.as.quickstarts.picketlink.authentication.events.ExampleObserver] IdentityType created
    INFO  [org.jboss.as.quickstarts.picketlink.authentication.events.ExampleObserver] User logout

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

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        For EAP 6:     mvn clean package jboss-as:deploy
        For WildFly:   mvn -Pwildfly clean package wildfly:deploy

4. This will deploy `target/picketlink-events.war` to the running instance of the server.


Access the application
---------------------

The application will be running at the following URL: <http://localhost:8080/picketlink-events>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        For EAP 6:     mvn jboss-as:undeploy
        For WildFly:   mvn -Pwildfly wildfly:undeploy

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/#useeclipse)


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

