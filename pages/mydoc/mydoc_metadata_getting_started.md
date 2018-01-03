---
title: Introduction to Runway metadata
keywords: terraframe, runway, mdbusiness
last_updated: December 21, 2017
tags: [mdbusiness]
summary: "Covers topics of model driven engineering and the theory of metadata"
sidebar: mydoc_sidebar
permalink: mydoc_metadata_getting_started.html
folder: mydoc
---

## Overview

When your domain is written down and formalized using Runway SDK's revolutionary xml syntax, it can be better managed and maintained and it serves as a single point of truth across your application. A single xml tag can tell Runway to automatically create for you:

* A table in the database with the appropriate columns.
* Generated Java stub and base classes across both client and server including RMI communication infrastructure which separates presentation and business tiers
* Generated Java query classes to make retrieving your data easy

And because your generated domain classes automatically extend Runway core classes, your domain classes expose a wealth of out-of-the-box functionality like:

* The 'apply' method. Simply call apply on an instance of your domain class and it will save the instance as a row in your generated table.
* 'lock' or 'applock' which is required when updating existing data to ensure data integrity
* Attribute getters/setters which allow you to read/modify the columns of your database record.
* 'delete' which removes the row from the database. Runway performs lots of validation for you to ensure data integrity.

{% include links.html %}
