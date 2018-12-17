---
title: Introduction to Runway SDK
keywords: homepage
tags: [introduction]
sidebar: mydoc_sidebar
permalink: index.html
summary: Runway SDK is an enterprise, data intelligence web application framework
---

## Overview

Runway SDK® is a model-driven engineering (MDE) software development toolkit for building robust and scalable enterprise applications faster, better, and cheaper. Runway SDK was designed from day one to manage the complexity of enterprise applications, such as Enterprise Resource Planning (ERP), with large business domain models, complex integrity rules, and stringent data governance requirements.

Runway provides the foundation for your application. When your application's domain model is defined using the concepts provided by Runway, you are leveraging decades of industry expertise and gently guided into doing things in scalable, efficient, reusable ways. Applications built using Runway SDK:

* Play nice with others. Your data is stored using predictable, globally unique ids. This makes sharing data between different applications a breeze.
* Support a variety of different front-ends. Want to provide access to your app through both mobile and web interfaces? No problem.
* Define the entire lifecycle of a transaction with a single annotation.
* Have robust security models via role-based access control.
* Leverage advanced relationship modeling and ontological features.
* Make GIS easy with industry proven, cutting-edge techniques.
* Have access to Runway's built-in object relational mapping (ORM) which makes advanced queries simple and intuitive.
* Can read and modify their domain model at runtime.
* and more ...

## The Power of Metadata

In Runway SDK, an entire domain model can be created, updated, and deleted all within a transaction at runtime, providing a capability that makes it ideal for dynamic applications where the domain model needs to be formally managed but cannot be defined programmatically or up-front. In addition, an executable implementation of the domain model at all levels of the software stack is generated with intelligent defaults and automatically kept in-sync with the model definition at runtime. By generating and maintaining an API for interacting with the domain model, developers have an abstraction across the entire software stack, including database tables, object oriented classes, and JavaScript classes with which they can implement business domain rules and enforce business integrity constraints. This increases developer productivity and helps ensure data integrity.

Applications built using Runway SDK have been deployed in multiple countries across four continents in industries such as economic development, international development, disease intervention, media analytics, and data management for oil and gas. The Runway SDK application stack, including the metamodel, is fully localized and has enabled applications to be deployed in multiple languages.

## What is Model-Driven Engineering (MDE)?

All applications utilize domain models, regardless of whether they are formally managed or not. In the object oriented modeling methodology, classes are used to abstract the data and behavior of the business domain. In statically typed languages, these classes are formally defined in source code artifacts before any application code is written that references them. In dynamically typed languages, no formal definition of a class is required and properties of these classes can be defined within the application code itself. However, even in dynamically typed languages, the definition of the model exists regardless of whether it is formally defined or not. This puts a greater burden on the application code that references the model implementation to ensure that the integrity constraints of the model are enforced throughout the entire codebase. This is often manageable for many types of applications but does not scale to maintain the complexity of a large enterprise application where the model needs to be formally managed regardless of which type of programming language is used. Statically typed class definitions by themselves do not contain enough metadata, such as access control rules, needed in an enterprise application. MDE utilizes the metamodel abstraction to manage the lifecycle of the domain model, making it an ideal methodology for enterprise applications.

## MDE and Runway SDK

Runway SDK was built from the ground up to use the MDE paradigm, which treats classes, class attributes, and associations in a domain model as first-order objects through an expressive metamodel API to dynamically manage models and to formally define data integrity and access constraints. This is ideal for applications that require a formal abstraction to manage a dynamic domain model where the datasets modeled in the application cannot be known and programmed up-front and need to continually change. For example, data analysis and integration applications work with datasets that are introduced at runtime, such as spreadsheets and database tables, and those applications needs a formal method, such as the metamodel, for modeling their metadata dynamically. Users also have the ability to define new dataset types on the fly, to specify data integrity constraints on those types, and to specify what user roles are allowed to perform operations on them. The users themselves at runtime are defining and modifying dataset definitions according to the needs of the application. An executable implementation of the model is generated to both decrease software development time and also ensure correctness.

![MetaModel Diagram](https://github.com/terraframe/Runway-SDK/blob/v2/doc/design/Metadata.png)


## Model Transactions and Versioning

In Runway SDK, model definitions can be created, updated, and deleted within a single transaction, including all generated code artifacts. This means that, when importing a new model definition into the application consisting of several class, attribute, and association definitions, the entire transaction will rollback should an error occur and not leave the application in a corrupted state. Source code and even changes to database tables are rolled back. This capability is provided out of the box to provide version update and patch management of the domain model for enterprise applications.

Enterprise applications tend to have very complex domain models. Changing the model involves modifying metadata records and also migrating database tables. Runway SDK has a formal abstraction for managing different versions of the domain model. It automatically generates code that implements the transition between different versions of the model to modify the metadata and database tables accordingly. Included are all updated class, attribute, and association definitions while removing metadata that may have been deprecated between versions. This involves computing the delta of the set of changes to the model and then automatically generating code to configure the domain model metadata. Additionally, it generates SQL DML code to modify the database table structure. It does this all within a transaction! But don’t SQL databases automatically commit when executing a DML command? Yes they do, but Runway SDK utilizes the command design pattern to automatically generate SQL DML code that will restore the database schema to its previous state should an error occur. This mechanism is a critical component for managing the lifecycle of any enterprise application, including patching and upgrading the model, that is provided by Runway SDK out of the box.


{% include links.html %}
