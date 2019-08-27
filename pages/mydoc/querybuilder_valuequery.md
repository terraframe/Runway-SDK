---
title: ValueQueries
keywords: terraframe, runway, querybuilder, query, join, leftjoin
last_updated: December 21, 2017
tags: [metadata]
summary: "Covers using QueryBuilder ValueQueries"
sidebar: mydoc_sidebar
permalink: querybuilder_valuequery.html
folder: mydoc
---

## Overview

ValueQueries can be used when it is not appropriate or necessary to query for instances of a particular business object. This is particularly useful when querying for data across many different tables (alternatively, see MdViews).

## Quirks

When instantiating query objects it is important to pass in the ValueQuery (instead of the typical QueryFactory) because it may influence SQL generation logic.

```  new SynonymRelationshipQuery(vQuery) ```


{% include links.html %}
