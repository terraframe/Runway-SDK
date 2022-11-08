---
title: Runway Metadata XML
keywords: runway dev, metadata
last_updated: November 8, 2022
summary: "Documentation on Runway core XML metadata"
sidebar: mydoc_sidebar
permalink: runwaydev_metadataxml.html
folder: mydoc
---

## Rebuilding Core Metadata

1. mvn clean install on runwaysdk-root to make sure your source is up-to-date
2. launch "drop and reimport runway metadata (no gis metadata)"
3. launch "import-schema-diff" to import your new metadata schema into the database. A sql diff will be generated in src/main/domain
4. launch "export-all-metadata" to regenerate the metadata.xml file
5. Move sql diff patch to runwaysdk-server/src/main/resources/domain/patch . You will need to manually edit the sql file to remove the first line in it (INSERT INTO dynamic_properties ...)
6. Make sure your xml schema are archived in src/main/domain/archive

