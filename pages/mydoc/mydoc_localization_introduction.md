---
title: Introduction to localization
keywords: terraframe, runway, localization
last_updated: December 21, 2017
tags: [metadata]
summary: "Covers the basics of using localization in Runway"
sidebar: mydoc_sidebar
permalink: mydoc_localization_introduction.html
folder: mydoc
---

## Overview

Runway metadata provides a built-in, enterprise ready localization solution. Defining a default value is as simple as setting the `label` attribute on a metadata component:

```
<mdBusiness name="net.geoprism.GeoprismUser" label="User" ...
```

This label can then be read from the metadata:

```
MdBusiness userMdBiz = MdBusiness.getMdBusiness("net.geoprism.GeoprismUser");

userMdBiz.getDisplayLabel().getDefaultValue();
```

Display labels can be thought of as a sort of hash map where the key is the Locale and the value is the localized label for that locale. The example above writes and reads from the default locale. Other locales can be written to and read from like so:

```
MdBusiness userMdBiz = MdBusiness.getMdBusiness("net.geoprism.GeoprismUser");

userMdBiz.getDisplayLabel().setValue(Locale.CANADA, "User");

userMdBiz.getDisplayLabel().getValue(Locale.CANADA);
```

{% include links.html %}
