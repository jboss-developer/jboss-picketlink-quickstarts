picketlink-idm-custom-identity-model: PicketLink Custom Identity Model
===============================
Author: Pedro Igor  
Level: Intermediate  
Technologies: PicketLink IDM, JPA, Hibernate  
Summary: An example about how to extends PicketLink IDM to provide your own Identity Model  
Source: <https://github.com/jboss-developer/jboss-picketlink-quickstarts/>


What is it?
-----------

This quickstart will show you how to use the PicketLink Identity Management API to design and implement your own identity model accordingly with your requirements.

PicketLink IDM provides a very extensible Identity Model from which you can build your own representation of security-related entities such as users, roles, groups, devices, applications, partitions, relationships between them and so forth.

It also provides a default implementation to support some very common security concepts, the Basic Model. 
The Basic Model can be used for most applications, covering some basic security requirements and representations for users, roles, groups, grant roles to users or turn them as members of different groups.

However, some applications may have a more complex set of requirements and require different types in order to better represent their own security-related concepts. This guide will show you how to satisfy those specific requirements and how to implement them using the PicketLink IDM API. Once you read this guide you should be able to:

* Understand what is an Identity Model
* Understand the limitations of the Basic Model
* Understand when you need to provide your own Identity Model
* Extend PicketLink IDM and provide your own Identity Model

Before enabling security into your application you should ask yourself about what it needs in order to represent all entities involved with your security requirements.

Let's say that some of these requirements are password-based authentication and RBAC (Role-based Access Control), pretty common in most applications. You would probably need something to represent users, roles and password-based credentials. And this is exactly what an Identity Model is, a representation of entities required by your application in order to support its security requirements.

In this quickstart we're going to consider the following security requirements to design a identity model:

* Support multiple security domains or realms, where each realm defines a set of security policies such as a key pair, HTTP/SSL enforcement, maximum number of failed login attempts.
* A security domain may have one or multiple applications. They inherit all policies defined by the security domain they belong.
* A security domain may have one or multiple users. Where each user is allowed to access a set of applications from a specific security domain.
* A security domain may have one or more roles. They are visible by all applications for a specific realm, also called global roles.
* A security domain may have one or more groups. They are visible by all applications for a specific realm, also called global groups.
* An application may have one or more roles. They are not shared by other applications.
* An application may have one or more groups. They are not shared by other applications.
* Users and groups are granted with roles. When granted to a group, all its members inherit the roles granted to the group.
* Applications are accessible only from authorized users. If a group is authorized, all its members are allowed to access an application.
* Users must be authenticated using an username/password credential

The latest PicketLink documentation is available [here](http://docs.jboss.org/picketlink/2/latest/).

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/#configure_maven) before testing the quickstarts.


Build and Run
-------------------------

This quickstart provides a set of JUnit test cases from where you can see the custom identity model in action. Just run them
using your favorite IDE.

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
