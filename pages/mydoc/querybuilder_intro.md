---
title: Introduction to Runway's Querybuilder
keywords: terraframe, runway, querybuilder, query
last_updated: December 21, 2017
tags: [metadata]
summary: "Covers the basics of using Runway's Querybuilder"
sidebar: mydoc_sidebar
permalink: querybuilder_intro.html
folder: mydoc
---

## Overview

Runway provides an advanced, robust metadata driven ORM query builder toolset.

All MdClasses include auto-generated Query classes, which are named the same as the name of your MdBusiness, with 'Query' appended to the end. Here is a very basic example:

```
    GeoEntityQuery query = new GeoEntityQuery(new QueryFactory());
    query.WHERE(query.getDisplayLabel().localize().LIKEi("%central%"));
    
    OIterator<? extends GeoEntity> it = query.getIterator();
    
    try
    {
      while (it.hasNext())
      {
        GeoEntity central = it.next();
        
        // Do something with the GeoEntity
      }
    }
    finally
    {
      it.close();
    }
```

This particular example queries for all GeoEntities that have a display label who's value includes "central" (case insensitive) somewhere in the label.

{% include links.html %}
