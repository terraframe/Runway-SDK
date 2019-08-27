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

Runway provides role based access control for metadata-driven components. The permissions model is pessimistic, meaning that unless explicitly allowed, the action is restricted. The permissions are configurable all the way down to the attribute. The permissions for domain types are configured by the developers for a particular role, and then the users are assigned to a role. This allows developers to define roles that are specific to the application's needs, and then the admins of the app can assign users to these roles. This allows for a very high level of configurability and reusability. 

When defining a permission on a component, the operation is also specified. This provides a distinction between reading and writing, allowing for the creation of a read-only role.

Just like metadata, permissions can also be defined in an XML schema. 

## @Authenticate

Authenticate is an annotation that can be placed above a server MdMethod to modify the way permissions are validated. 

    @Authenticate
    public void executeAuthenticated()
    {
      ...
    }

When code is executed within an authenticate annotation, the permissions are only checked for the execution of the method, and not for each individual piece of metadata contained within the method. This can be used to simplify permissions, especially when dealing with very large metadata hierarchies.

## MdMethods

Due to the pessimistic nature of Runway's permissions model, by default no users have permissions to execute your MdMethod. The following example gives the Geoprism "Administrator" role permissions to execute the "executeAuthenticated" method on the "DataUploaderImportJob" MdBusiness.

    <permissions
      <role roleName="geoprism.admin.Administrator">
        <grant>
          <mdBusinessPermission type="net.geoprism.data.etl.excel.DataUploaderImportJob">
            <mdMethodPermission methodName="executeAuthenticated">
              <operation name="EXECUTE"/>
            </mdMethodPermission>
          </mdBusinessPermission>
        </grant>
      </role>
    </permissions>

Because MdMethods are also Actors in the system, you can directly assign permissions to an MdMethod, just like it were a role or user. You can also assign roles to an MdMethod. When the method is executng, it will execute with the permissions of the specified role. The following example assigns the "executeAuthenticated" method to the Administrator role.

    <permissions>    
      <method methodName="executeAuthenticated" type="net.geoprism.data.etl.excel.DataUploaderImportJob">
        <assignedRole roleName="geoprism.admin.Administrator"/>
      </method>
    </permissions>

By default, MdMethods do not have permissions to do anything, which is why its important you either give them a role or directly assign them permissions.

Putting it all together, we arrive at the most common permissions usecase for MdMethods:
1. Add an @Authenticate annotation to the top of your method
2. Grant the relevant roles the ability to execute your MdMethod
3. Assign your MdMethod to the relevant role.

## Role inheritance

Roles can inherit from other roles.

## The Hierarchy

[![UML of Runway Permissions Hierarchy](./images/RBAC.svg "UML of Runway Attribute Hierarchy")](./images/RBAC.svg)

{% include links.html %}
