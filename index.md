---
title: Introduction to Runway SDK
keywords: homepage
tags: [introduction]
sidebar: mydoc_sidebar
permalink: index.html
summary: Runway SDK is an enterprise, data intelligence web application framework
---


## Overview

Runway provides the foundation that the rest of your application is built upon. When your application's domain model is defined using the concepts provided by Runway, you are leveraging decades of industry expertise and gently guided into doing things in scalable, efficient, reusable ways. Applications built using Runway SDK:

* Play nice with others. Your data is stored using predictable, globally unique ids. This makes sharing data between different applications a breeze.
* Support a variety of different front-ends. Want to provide access to your app through both mobile and web interfaces? No problem.
* Can define the entire lifecycle of a transaction with a single annotation
* Have robust security models via role-based access control.
* Are able to leverage advanced relationship modeling and ontological features.
* Make GIS easy with industry proven, cutting-edge techniques.
* Have access to Runway's built-in ORM which makes advanced queries simple and intuitive.
* Can read and modify their domain model at runtime.
* .. and more ..

## Who is using it

Runway SDK provides the backend for DDMS (Disease Data Management System), Geoprism, and a few other 'top-secret' projects in the works. Although Runway is shared between many diverse applications, much of the effort is reusable and open-source, creating an IP-sharing ecosystem that reduces the cost of any single organisation.

## Metadata at work

When your domain is written down and formalized using Runway SDK's revolutionary xml syntax, it can be better managed and maintained and it serves as a single point of truth across your application. A single xml tag can tell Runway to automatically create for you:

* A table in the database with the appropriate columns.
* Generated Java stub and base classes across both client and server including RMI communication infrastructure which separates presentation and business tiers
* Generated Java query classes to make retrieving your data easy

And because your generated domain classes automatically extend Runway core classes, your domain classes expose a wealth of out-of-the-box functionality like:

* The 'apply' method. Simply call apply on an instance of your domain class and it will save the instance as a row in your generated table.
* 'lock' or 'applock' which is required when updating existing data to ensure data integrity
* Attribute getters/setters which allow you to read/modify the columns of your database record.
* .. and so much more ..

## Relationships and Ontologies

Relationships can also be defined using a single XML keyword. Relationships are used to associate two (or more) domain entities. Accessor methods (getParents, getChildren, etc) as well as mutator methods (addChild, addParent, etc) are automatically available on your generated base classes, which makes defining a new relationship as simple as, `parent.addChild(newChild)`.


{% include links.html %}
