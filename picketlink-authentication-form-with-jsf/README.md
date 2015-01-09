picketlink-authentication-form-with-jsf: PicketLink HTTP FORM Authentication
===============================
Author: Anil Saldhana
Level: Beginner
Technologies: CDI, PicketLink, JSF
Summary: Basic example that demonstrates simple username/password authentication using the FORM Authentication with a JSF view layer
Source: <https://github.com/jboss-developer/jboss-picketlink-quickstarts/>


What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *PicketLink* in *JBoss Enterprise Application Platform 6* or *WildFly*.

You'll learn from this quickstart how to use PicketLink to authenticate users using a JSF-based form to collect and submit credentials.
When using JSF you must change your *h:form* as follows:

* Set *prependId="false"*
* Change the id of username and password fields to *j_username* and *j_password*, respectively.

Here is how the form looks like:

        <h:form method="POST" prependId="false">
            <h:inputText id="j_username" />
            <h:inputSecret id="j_password"/>
            <h:commandButton id="login" value="Login" action="#{identity.login()}"/>
        </h:form>

Another important aspect when using a JSF-based form is how the form is submited. By default PicketLink expects that you
submit the form to */j_security_check*, this is the default authentication URI. But when using JSF, the form is usually submited to the page itself if you have a *h:commandButton*
like that:

        <h:commandButton id="login" value="Login" action="#{identity.login()}"/>

In this case, you also need to provide a configuration as follow:

        builder
            .http()
                .allPaths()
                    .authenticateWith()
                        .form()
                            .authenticationUri("/login.jsf")
                            .loginPage("/login.jsf")
                            .errorPage("/error.jsf")
                            .restoreOriginalRequest()
                .forPath("/logout")
                    .logout()
                    .redirectTo("/index.html");

The configuration above tells PicketLink that the authentication URI should be:

        .authenticationUri("/login.jsf")

Instead of */j_security_check*, which is the default.

The application is configured to provide public access for some resources(eg.: /index.html) and to protected others for
authenticated users only(eg.: /protected/*).

Identity data such as users, roles and groups, are managed using PicketLink IDM backed by a file-based identity store.
This store is used by default when no specific configuration is provided.

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

4. This will deploy `target/picketlink-authentication-form-with-jsf.war` to the running instance of the server.


Access the application
---------------------

The application will be running at the following URL: <http://localhost:8080/picketlink-authentication-form-with-jsf>.


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

