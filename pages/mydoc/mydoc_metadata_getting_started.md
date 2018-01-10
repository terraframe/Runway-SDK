---
title: Introduction to Runway metadata
keywords: terraframe, runway, mdbusiness
last_updated: December 21, 2017
tags: [mdbusiness]
summary: "Covers the basics of Runway metadata"
sidebar: mydoc_sidebar
permalink: mydoc_metadata_getting_started.html
folder: mydoc
---

## Overview

Metadata is a term that simply means 'data about data'. In the context of Runway, metadata refers to data that describe the structure of your data. These metadata are stored in the database, primarily in the form of MdClass and MdAttributes, so that metadata can be persisted and manipulated even at runtime through an abstraction with the same level of flexibility as regular data. These two types can be thought of as the building blocks of your domain, which in turn is the building block of your entire application. A good way to think about it is to think of an MdClass as an object oriented class and MdAttributes are the fields on that class. Your actual data exists as instances of this class.

MdAttribute is subclassed by metadata types which mirror all of your standard computer science data types. For example, there exists MdAttributeBoolean for defining a boolean type, MdAttributeInteger for defining an integer, etc. When an MdAttribute is applied to an MdBusiness, that piece of data becomes available throughout the entire stack. Getters and setters are generated at the Java level and the appropriate column(s) are automatically created in your database. In addition, type safety is enforced. If for some reason an integer is attempted to be set to a boolean, Runway will throw a localizable error. There is also some additional constraints you can enforce. For example, you can specify that your integers must always be positive. All of this logic is handled by Runway in a clean, structured way so that you don't have to repeat these rules time and time again. This is the benefit of RunwaySDK metadata.

These two hierarchies comprise the majority of Runway's metadata domain. There are other metadata components which exist as siblings to MdClass and MdAttribute, however you will discover these the more you use Runway.

## The Hierarchy

[![UML of Runway Metadata Hierarchy](./images/Metadata.svg "UML of Runway Metadata Hierarchy")](./images/Metadata.svg)

{% include links.html %}
