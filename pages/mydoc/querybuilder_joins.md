---
title: Querybuilder Joins
keywords: terraframe, runway, querybuilder, query, join, leftjoin
last_updated: December 21, 2017
tags: [metadata]
summary: "Covers joining tables when using the QueryBuilder"
sidebar: mydoc_sidebar
permalink: querybuilder_joins.html
folder: mydoc
---

## Overview

Runway's Querybuilder technology is capable of joining across many different tables, using many different join types.

## Left Joins

The following is an example of Left Join chaining using Runway's Querybuilder.

```
    SynonymRelationshipQuery relationshipQuery = new SynonymRelationshipQuery(vQuery);
    SynonymQuery synonymQuery = new SynonymQuery(vQuery);
    SynonymDisplayLabelQuery labelQuery = new SynonymDisplayLabelQuery(vQuery);

    vQuery.WHERE(new LeftJoinEq(geQuery.getOid(), relationshipQuery.parentOid()));
    vQuery.AND(new LeftJoinEq(relationshipQuery.childOid(), synonymQuery.getOid()));
    vQuery.AND(new LeftJoinEq(synonymQuery.getDisplayLabel(), labelQuery.getOid()));
```

This particular example was pulled from SynonymRestriction.java in the GeoRegistry server project. This example joins a GeoEntity table with it's associated Synonym, and then joins in the Synonym's Display label.

When using left joins,  ordering is extremely important. Here is an example of correct joining ordering:

```
    A join B
    B join C
    C join D
```



{% include links.html %}
