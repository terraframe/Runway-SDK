---
title: Introduction to Metadata Classes (MdClass)
keywords: terraframe, runway, mdtype
last_updated: December 21, 2017
tags: [mdbusiness]
summary: "A class definition that transcends Java."
sidebar: mydoc_sidebar
permalink: mydoc_metadata_mdclass_intro.html
folder: mydoc
---

## Overview

An MdClass is anything that can be represented in traditional object-oriented terms. That is to say, there is a definition of what behavior, data and also context this class contains, and instances of this class behave as such. A MdClass transcends Java because its representation exists on the server, the client, potentially the database (depending on the MdClass subtype), and yes, depending on the front-end logic, its representation may even exist in the web browser.

MdClasses do not necessarily have a database representation (see MdTransient). MdClass is an abstract class and cannot be created directly.

## Generated Base and stub classes

Because MdClasses generate Java source code, they bring with them a distinction between Base and Stub source. When you are defining your business logic for your MdClass, you will define it in the Stub source because the stub source is only ever generated once. At the time of its generation it only contains the class definition, a zero argument constructor (which supers), and an extension of your generated base class.

Your generated base class is where a lot of the Runway magic happens. 


{% include links.html %}
