---
title: Eclipse Setup
permalink: mydoc_eclipse_setup.html
keywords: eclipse
summary: "Covers setting up a new Eclipse for Runway developers."
sidebar: mydoc_sidebar
folder: mydoc
---

## Configure code formatting in Eclipse preferences.
In preferences, search for "formatting"

1. Import the 'TerraCube' formatting settings from 'git/runwaysdk-root/doc/TerraFrameJavaFormat.xml'
    * Java -> Code Style -> Formatter
    * Import and select active profile: Terracube
2. Manually set the Javascript code formatting settings
    * Javascript -> Code Style -> Formatter. Edit the active profile.
    * Tab policy: Spaces only
    * Indentation size: 2
3. Manually configure the Eclipse default text editor
    * General -> Editors -> Text Editors
    * Displayed tab width: 2
    * Insert spaces for tabs: yes
4. XML -> XML Files -> Editor
    * Indent using spaces: yes
    * Indentation size: 2
5. Manually configure the Eclipse 'ant editor'
    * Ant -> Editor -> Formatter
    * Tab Size: 2
    * Use tab character instead of spaces: no

## Disable Eclipse validators

This is especially important for projects (like AMI or DDMS) which include third-party javascript source which prevent the project from building because they take so long to validate.

1. Disable unnecessary validation at the workspace level. If there is particular validation that you don't care about (like JSPS or the like) you may do that here. This will speed up your Eclipse builds.
    * Eclipse Worksapce Preferences -> Validation
2. If disabling Javascript validation at the workspace level does not work, you may need to disable it by excluding the source.
    * Right click your project -> Properties. Javascript -> Include Path. Click "Source" tab.
    * You may either manually exclude the source with a wildcard, or you can remove the source entirely from the "Include path"

## Add XML Catalog for working with Runway metadata schema files

[Follow the instructions here](/Runway-SDK/mydoc_metadata_authoring.html#classpathNotation)

{% include links.html %}
