---
title: Manipulating Metadata in Java
keywords: terraframe, runway, metadata
last_updated: December 21, 2017
tags: [metadata]
summary: "Some examples of how to manipulate metadata using Java"
sidebar: mydoc_sidebar
permalink: mydoc_metadata_java_reading.html
folder: mydoc
---

## Overview

One of the biggest strengths of Runway is that the domain model can be manipulated at runtime. One the applications we developed uses this functionality extensively through a robust feature known as the form generator. The DDMS form generator allows users to create MdBusinesses (classes with a business contect) and MdAttributes at runtime for the purpose of exposing new datatypes for collection and reporting. This functionality exists in some of the industry's most popular applications such as SalesForce®.

## Creating Metadata

Suppose you wanted to write your own form generator. To keep things simple, lets assume for now that all they can do is create new datatypes with a name that they are allowed to specify. You may have a method somewhere on the server that looks like this:

```
@Transaction
public void createMetadata(String name)
{
  MdBusiness customForm = new MdBusiness();
  customForm.setPackageName("com.test.forms");
  customForm.setTypeName(this.convertToTypename(name));
  customForm.getDisplayLabel().setDefaultValue(name);
  customForm.apply();
}
```

The `@Transaction` annotation tells Runway's transaction aspects to weave into this method. Click here to learn more about Runway transactions.

`setPackageName` is a method that configures the package of your new MdBusiness. Because applying a new MdBusiness generates Java code, all MdBusinesses must have a unique keyname. The keyname of a MdBusiness is the concatenated package name and type name of the format: `packageName` + "." + `typeName`.

`setTypeName` configures the MdBusiness's type name. The type name is the class name of the generated Java type and it also is used when creating your database table. Because the user is specifying the name, I also included `this.convertToTypename`, which is another method we would need to implement to sanitize and convert the user input into something suitable for a typename that does not, for example, contain any spaces.

`getDisplayLabel` and `setDefaultValue` sets the default locale of the display label of the MdBusiness. [Click here](./mydoc_localization_introduction.html) to learn more about localization.

`apply` tells Runway that our MdBusiness is fully configured and the changes are ready to be applied. Because we are in a transaction, the MdBusiness is added to Runway's transaction cache. At the end of the transaction (which happens at the end of our `createMetadata` method), all changes will be applied. This includes all database changes as well as Java code changes.

## Reading and Querying Metadata

Now that the user has the ability to create new data types, we need a way to display to the user which data types exist already in the system.

If the MdBusiness is hardcoded and known ahead of time, we can fetch it in a single line:

```
MdBusiness mdBiz = MdBusiness.getMdBusiness("net.geoprism.GeoprismUser");
```

Unfortunately for us, things are not so simple because these objects are created arbitrarily at runtime. Luckily Runway's query API makes this very simple.

```
QueryFactory qf = new QueryFactory();

MdBusinessQuery mbq = new MdBusinessQuery(qf);
mbq.WHERE(mbq.getPackageName().EQ("com.test.forms"));
OIterator<? extends MdBusiness> mbIt = mbq.getIterator();

try
{
  while (mbIt.hasNext())
  {
    MdBusiness mdBiz = mbIt.next();
    
    // Serialize the MdBusiness (to JSON) for display
  }
}
finally
{
  mbIt.close();
}
```

To learn how Runway's query API works, click here.

{% include links.html %}
