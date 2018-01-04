---
title: Introduction to Runway permissions
keywords: terraframe, runway, permissions
last_updated: December 21, 2017
tags: [aspects]
summary: "Covers the basics of using permissions within Runway"
sidebar: mydoc_sidebar
permalink: mydoc_permissions_introduction.html
folder: mydoc
---

## Overview

Runway provides role based access control for metadata-driven components. The permissions model is restrictive, meaning that unless explicitly allowed, the action is restricted. The permissions are configurable all the way down to the attribute. The permissions for domain types are configured by the developers for a particular role, and then the users are assigned to a role. This allows developers to define roles that are specific to the application's needs, and then the admins of the app can assign users to these roles. This allows for a very high level of configurability and reusability. 

When defining a permission on a component, the operation is also specified. This provides a distinction between reading and writing, allowing for the creation of a read-only role.

Just like metadata, permissions can also be defined in an XML schema. 

{% include links.html %}