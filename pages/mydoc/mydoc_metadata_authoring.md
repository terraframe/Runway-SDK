---
title: Manipulating Metadata in XML
keywords: terraframe, runway, metadata
last_updated: December 21, 2017
tags: [metadata]
summary: "Covers the basics of manipulating metadata in Runway via XML"
sidebar: mydoc_sidebar
permalink: mydoc_metadata_authoring.html
folder: mydoc
---

## Overview

The most common way to create metadata is through Runway's custom XML schema.  This XML allows for developers to intuitively manipulate metadata, which includes creating new metadata definitions, updating and deleting existing definitions, and defining permissions for metadata definitions.  Along these lines the XML is broken up into five major sections: Referencing existing definitions, deleting definitions, creating definitions, updating definitions, and defining permissions. 

It is important to understand that the XML is not the final definition of the model, but rather an input mechanism with syntax for managing the lifecyle of the domain model. Unlike raw DML SQL, the XML covers transitions between model versions that migrate both generated source code and database schemas (if applicable) transactionally. 

The XML syntax for creating a new definition and updating an existing definition are (almost) identical.  The `create` tag is used to create a new definition while the `update` tag is used to update existing definitions.  There are a few key differences between creates and updates, however we will go into this later.

## Creating a persisted Class

The following XML defines a new type of User called a GeoprismUser. An MdBusiness is a sublass of MdClass to represent classes that are persisted to a relational database with a business-focused workflow. Because users in Runway are defined via an MdBusiness, they can be extended to create custom user types that more accurately reflect the needs of the application.

```
<version xsi:noNamespaceSchemaLocation="classpath:com/runwaysdk/resources/xsd/version.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <doIt>
    <create>
      <mdBusiness name="net.geoprism.GeoprismUser" label="User" extends="com.runwaysdk.system.Users">
        <attributes>
          <char name="firstName" label="First name" size="255" required="true" />
          <char name="lastName" label="Last name" size="255" required="true" />
          <char name="phoneNumber" label="Phone number" size="255" required="false" />
          <char name="email" label="Email" size="255" required="true" />
        </attributes>
      </mdBusiness>
    </create>
  </doIt>
  <undoIt>
    <delete>
      <object key="net.geoprism.GeoprismUser" type="com.runwaysdk.system.metadata.MdBusiness" />
    </delete>
  </undoIt>
</version>
```

Lets break down this XML line by line.

## The version tag

All metadata files start with this line:

```
<version xsi:noNamespaceSchemaLocation="classpath:com/runwaysdk/resources/xsd/version.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
```

The `version` tag lets us know that we are defining verisoned metadata and is associated with the particular Runway importer we will be using ([com.runwaysdk.dataaccess.io.Versioning](https://github.com/terraframe/Runway-SDK/blob/master/runwaysdk-server/src/main/java/com/runwaysdk/dataaccess/io/Versioning.java)).


## The classpath notation {#classpathNotation}

At the top of every schema file you will see this:

```
xsi:noNamespaceSchemaLocation="classpath:com/runwaysdk/resources/xsd/version.xsd"
```

The `xsi:noNamespaceSchemaLocation` is a standard way in XML to define an XSD schema definition, which we set to our `version.xsd` file. However, `classpath:` is non-standard, and is a concept we created. This allows us to read the XSD schema from the classpath. Our XSD schema file is included within the runway server jar. You can view this file on the web here:

<https://raw.githubusercontent.com/terraframe/Runway-SDK/master/runwaysdk-server/src/main/resources/com/runwaysdk/resources/xsd/version.xsd>

This custom classpath modifier has many advantages when used at runtime, however IDE's like Eclipse cannot read the XSD file to provide auto-complete. You can place this URL into the `xsi:noNamespaceSchemaLocation` attribute like so:

```
xsi:noNamespaceSchemaLocation="https://raw.githubusercontent.com/terraframe/Runway-SDK/master/runwaysdk-server/src/main/resources/com/runwaysdk/resources/xsd/version.xsd"
```

As a workaround for development, but when the file is committed you should use the classpath notation.


## doIt and undoIt

```
<version xsi:noNamespaceSchemaLocation="classpath:com/runwaysdk/resources/xsd/version.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <doIt>
    ...
  </doIt>
  <undoIt>
    ...
  </undoIt>
```

An XML schema can be either imported or unimported. The `doIt` tag defines what happens when the schema is imported. The `undoIt` tag defines what happens when the schema is unimported. Thus, the `doIt` section defines the domain for whatever feature you are implementing, and the `undoIt` section removes the feature. This is useful in a development context because your feature is in a state of flux. If the metadata already exists in the database from the last time you imported this schema, you must first delete all the metadata before you can import the schema again to redefine your model.

As you progress in experience writing schema files, you may be tempted to ignore the `undoIt` section and simply comment out metadata that already exists, allowing you to skip unimporting the schema and running the import again (with only that which has changed). Resist the urge to do this! When the time comes to finally commit your metadata file, you will have never run the schema file in its entirety, which means you are committing untested code. In addition, you will start to lose track of the actual state of your database. All of these things will cost you time. Get in the good habit now of writing your `undoIt` section along-side your `doIt` code.


## Import action tags

```
<version xsi:noNamespaceSchemaLocation="classpath:com/runwaysdk/resources/xsd/version.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <doIt>
    <create>
     ...
    </create>
  </doIt>
  <undoIt>
    <delete>
     ...
    </delete>
  </undoIt>
```

The import action tag is a required tag that comes immediately after the `doIt` or `undoIt` tag and it specifies what action to perform with the metadata that we're defining. In our GeoprismUser example, we are using the `create` import action tag because we simply want to define new metadata from scratch. In the `undoIt` section, you see an example of the `delete` action, and it functions much the same as the `create` action. Simply place your metadata within the action tag and your action will be performed on the specified metadata.

The `update` tag is however a little different.

## mdBusiness tag

```
      <mdBusiness name="net.geoprism.GeoprismUser" label="User" extends="com.runwaysdk.system.Users">
        <attributes>
          ...
        </attributes>
      </mdBusiness>
```

The `mdBusiness` tag tells the system that we are referring to an MdBusiness. When inside a `create` action, the system will create a new mdBusiness. During an update, it will be used as context to fetch an MdBusiness to update. Any additional attributes will be updated to their provided values in the XML. The following attributes are available on an `mdBusiness` tag:

An MdBusiness may also include a list of MdAttributes, which is specified via an inner `attributes` tag.

## File conventions

Metadata is typically stored at the path 'src/main/domain' in the server project. In this directory you will see a list of files with an unusual naming convention. As an example:

```
(0001466352372715)CategoryIconImage.xml
```

The number enclosed within the parenthesis is a timestamp, generated by Runway. We like to put it first because when you view the files alphabetically (as is default in eclipse or a filesystem navigator) the files are ordered by their creation date. The second part, 'CategoryIconImage' is a name that you the developer create to describe the feature or grouping of features that this metadata schema contains. When the schemas are processed by Runway, the timestamp must be isolated from the filename. Runway uses a regex expression to do this. This regex expression can be found in TimestampFile.java, and although your timestamp must be numbers, your description can be alpanumeric characters as well as dashes, underscores, periods and whitespaces.

{% include links.html %}
