# PicketLink Quickstarts

## Introduction

These quickstarts run JBoss Enterprise Application Platform 6 and WindFly.

We recommend using the ZIP distribution file for both JBoss Enterprise Application Platform 6 and WildFly.

You can also run PicketLink in Apache TomEE or Glassfish. In this case, you may need some additional configuration in order to get them
up and running. For PicketLink JEE Security examples, you must ship JBoss Logging jars in your deployments. 

## System Requirements

To run these quickstarts with the provided build scripts, you need the following:

1. Java 1.6 or Java 1.7, depending if you're using JBoss EAP or WildFly to run the quickstarts. You can choose from the following:
    * OpenJDK
    * Oracle Java SE
    * Oracle JRockit

2. Maven 3.0.0 or newer, to build and deploy the examples
    * If you have not yet installed Maven, see the [Maven Getting Started Guide](http://maven.apache.org/guides/getting-started/index.html) for details.
    * If you have installed Maven, you can check the version by typing the following in a command line:

            mvn --version

3. The JBoss Enterprise Application Platform 6 distribution ZIP or the WildFly distribution ZIP.
    * For information on how to install and run those servers, refer to the their documentation.

## Check Out the Source

1. To clone this Git repository, use the following command:

        git clone git@github.com:jboss-developer/jboss-picketlink-quickstarts.git

2. If you want the quickstarts for a particular version (eg.: 2.5.2.Final) execute the following command:

        cd jboss-picketlink-quickstarts
        git checkout v2.5.2.Final

The command above will checkout a TAG corresponding to the version you want to use. For each release of PicketLink we also release and TAG
a version for the quickstarts. Each TAG uses a specific PicketLink version. Make sure you're using the TAG for the version you're looking for.

We recommend to always consider the latest version of the quickstarts, so you can check the latest changes and updates to PicketLink.

## Run the Quickstarts

The root folder of each individual quickstart contains a README file with specific details on how to build and run the example. In most cases you do the following:

* [Start the JBoss server](#start-the-jboss-server)
* [Build and deploy the quickstarts](#build-and-deploy-the-quickstarts)

## About the PicketLink Federation Quickstarts

The *PicketLink Federation Quickstarts* provide a lot of examples about how to use *PicketLink Federation SAML Support* to enable SSO for your applications.
Before running them you need to understand how they are related with each other. Basically, each Identity Provider is meant to be used by a group of Service Providers.
In order to get the whole Single Sing-On functionality demonstrated, you need to deploy them together.

| SAML Configuration                        | Identity Provider                                 | Service Provider(s)                                                                                      |
| -------------                             |:-------------------------------------------------:| --------------------------------------------------------------------------------------------------------:|
| Basic                                     | picketlink-federation-saml-idp-basic              | picketlink-federation-saml-sp-post-basic, picketlink-federation-saml-sp-redirect-basic                   |
| Encryption                                | picketlink-federation-saml-idp-with-encryption    | picketlink-federation-saml-sp-with-encryption                                                            |
| Metadata                                  | picketlink-federation-saml-idp-with-metadata      | picketlink-federation-saml-sp-with-metadata                                                              |
| Signatures                                | picketlink-federation-saml-idp-with-signature     | picketlink-federation-saml-sp-post-with-signature, picketlink-federation-saml-sp-redirect-with-signature |
| HTTP CLIENT_CERT and FORM Authentication  | picketlink-federation-saml-idp-ssl                | picketlink-federation-saml-sp-post-basic, picketlink-federation-saml-sp-redirect-basic                   |
| IDP Servlet Filter                        | picketlink-federation-saml-idp-servlet-filter     | picketlink-federation-saml-sp-post-with-signature, picketlink-federation-saml-sp-redirect-with-signature |

The table above describes what are the Identity Provider and Service Providers required to test a specific configuration. It is important that you respect these dependencies to get the
functionality properly working.

### Using SAML Tracer Firefox Add-On to Debug the SAML SSO Flow

If you want to understand even better how IdPs and SPs communicate with each other, you may want to configure the [SAML Tracer Add-On](https://addons.mozilla.org/en-US/firefox/addon/saml-tracer/) to your Mozilla Firefox.
This is a nice way to debug and view SAML Messages, so you can take a look on how the IdP and SP exchange messages when establishing a SSO session.

### Start the JBoss Server

Before you deploy a quickstart, in most cases you need a running JBoss Enterprise Application Platform 6 or WildFlyserver. A few of the Arquillian tests do not require a running server. This will be noted in the README for that quickstart.

The JBoss server can be started a few different ways.

* [Start the JBoss Server With the _web_ profile](#start-the-jboss-server-with-the-web-profile): This is the default configuration. It defines minimal subsystems and services.
* [Start the JBoss Server with the _full_ profile](#start-the-jboss-server-with-the-full-profile): This profile configures many of the commonly used subsystems and services.
* [Start the JBoss Server with a custom configuration](#start-the-jboss-server-with-custom-configuration-options): Custom configuration parameters can be specified on the command line when starting the server.

The README for each quickstart will specify which configuration is required to run the example.

#### Start the JBoss Server with the Web Profile

To start JBoss Enterprise Application Platform 6 or WildFly with the Web Profile:

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the JBoss server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

#### Start the JBoss Server with the Full Profile

To start JBoss Enterprise Application Platform 6 or WildFly with the Full Profile:

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the JBoss server with the full profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full.xml

#### Start the JBoss Server with Custom Configuration Options

To start JBoss Enterprise Application Platform 6 or WildFly with custom configuration options:

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the JBoss server. Replace the CUSTOM_OPTIONS with the custom optional parameters specified in the quickstart.

        For Linux:   JBOSS_HOME/bin/standalone.sh CUSTOM_OPTIONS
        For Windows: JBOSS_HOME\bin\standalone.bat CUSTOM_OPTIONS

### Build and Deploy the Quickstarts

See the README file in each individual quickstart folder for specific details and information on how to run and access the example.

#### Build the Quickstart Archive

In some cases, you may want to build the application to test for compile errors or view the contents of the archive.

1. Open a command line and navigate to the root directory of the quickstart you want to build.
2. Use this command if you only want to build the archive, but not deploy it:

        For EAP 6:     mvn clean package
        For WildFly:   mvn -Pwildfly clean package

#### Build and Deploy the Quickstart Archive

1. Make sure you [start the JBoss server](#start-the-jboss-server) as described in the README.
2. Open a command line and navigate to the root directory of the quickstart you want to run.
3. Use this command to build and deploy the archive:

        For EAP 6:     mvn clean package jboss-as:deploy
        For WildFly:   mvn -Pwildfly clean package wildfly:deploy

#### Undeploy an Archive

The command to undeploy the quickstart is simply:

        For EAP 6:     mvn jboss-as:undeploy
        For WildFly:   mvn -Pwildfly wildfly:undeploy

PicketLink Documentation
------------

The documentation is available from the following [link](http://docs.jboss.org/picketlink/2/latest/).