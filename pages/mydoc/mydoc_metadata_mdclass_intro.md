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

## Generated Source

MdClasses, by default, generate lots of source code for you. Pairs of base + stub java files are generated in server, client (referred to as DTOs), controllers, and also JSPs. You can disable the client code generation (DTOs + controllers + JSPs) by setting 'publish' to false before applying the MdClass.

The stub class is where you write your business logic, you will never modify the base class. This is because if you decide to modify your MdClass definition, your base class will be regenerated and updated to reflect your metadata. Your stub class is only ever generated once. At the time of its generation it only contains the class definition, a zero argument constructor (which supers), and an extension of your generated base class.


{% include links.html %}
