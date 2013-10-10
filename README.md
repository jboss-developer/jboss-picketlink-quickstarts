# PicketLink Quickstarts

## Introduction

These quickstarts run on both JBoss Enterprise Application Platform 6 and JBoss AS 7.
If you want to run the quickstarts on JBoss Enterprise Application Platform 6, we recommend using the JBoss Enterprise Application Platform 6 ZIP file.
This version uses the correct dependencies and ensures you test and compile against your runtime environment.

## System Requirements

To run these quickstarts with the provided build scripts, you need the following:

1. Java 1.6, to run JBoss AS and Maven. You can choose from the following:
    * OpenJDK
    * Oracle Java SE
    * Oracle JRockit

2. Maven 3.0.0 or newer, to build and deploy the examples
    * If you have not yet installed Maven, see the [Maven Getting Started Guide](http://maven.apache.org/guides/getting-started/index.html) for details.
    * If you have installed Maven, you can check the version by typing the following in a command line:

            mvn --version

3. The JBoss Enterprise Application Platform 6 distribution ZIP or the JBoss AS 7 distribution ZIP.
    * For information on how to install and run JBoss, refer to the product documentation.

## Check Out the Source

1. To clone this Git repository, use the following command:

        git clone git@github.com:picketlink/picketlink-quickstarts.git

2. If you want the quickstarts for a particular version (eg.: 2.5.2.Final) execute the following command:

        cd picketlink-quickstarts
        git checkout v2.5.2.Final

## Run the Quickstarts

The root folder of each individual quickstart contains a README file with specific details on how to build and run the example. In most cases you do the following:

* [Start the JBoss server](#start-the-jboss-server)
* [Build and deploy the quickstarts](#build-and-deploy-the-quickstarts)


### Start the JBoss Server

Before you deploy a quickstart, in most cases you need a running JBoss Enterprise Application Platform 6 or JBoss AS 7server. A few of the Arquillian tests do not require a running server. This will be noted in the README for that quickstart.

The JBoss server can be started a few different ways.

* [Start the JBoss Server With the _web_ profile](#start-the-jboss-server-with-the-web-profile): This is the default configuration. It defines minimal subsystems and services.
* [Start the JBoss Server with the _full_ profile](#start-the-jboss-server-with-the-full-profile): This profile configures many of the commonly used subsystems and services.
* [Start the JBoss Server with a custom configuration](#start-the-jboss-server-with-custom-configuration-options): Custom configuration parameters can be specified on the command line when starting the server.

The README for each quickstart will specify which configuration is required to run the example.

#### Start the JBoss Server with the Web Profile

To start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile:

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the JBoss server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

#### Start the JBoss Server with the Full Profile

To start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Full Profile:

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the JBoss server with the full profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full.xml

#### Start the JBoss Server with Custom Configuration Options

To start JBoss Enterprise Application Platform 6 or JBoss AS 7 with custom configuration options:

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

            mvn clean package

#### Build and Deploy the Quickstart Archive

1. Make sure you [start the JBoss server](#start-the-jboss-server) as described in the README.
2. Open a command line and navigate to the root directory of the quickstart you want to run.
3. Use this command to build and deploy the archive:

            mvn clean package jboss-as:deploy

#### Undeploy an Archive

The command to undeploy the quickstart is simply:

        mvn jboss-as:undeploy

PicketLink Documentation
------------

The documentation is available from the following [link](http://docs.jboss.org/picketlink/2/latest/).