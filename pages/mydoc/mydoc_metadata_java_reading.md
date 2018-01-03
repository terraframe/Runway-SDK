---
title: Reading metadata at runtime
keywords: terraframe, runway, metadata
last_updated: December 21, 2017
tags: [metadata]
summary: "Some examples of how to read metadata at runtime using Java"
sidebar: mydoc_sidebar
permalink: mydoc_metadata_java_reading.html
folder: mydoc
---

## Overview

One of the biggest strengths of a formalized domain model is that it can be read at runtime. The defining MdBusiness of a domain class can be fetched in a single line using the fully qualified typename:

```
MdBusiness mdBiz = MdBusiness.getMdBusiness("net.geoprism.GeoprismUser");
```


{% include links.html %}
