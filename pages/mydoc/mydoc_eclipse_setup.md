---
title: Eclipse Setup
permalink: mydoc_eclipse_setup.html
keywords: eclipse
summary: "Covers setting up a new Eclipse for Runway developers."
sidebar: mydoc_sidebar
folder: mydoc
---

## Downloading the Right Eclipse

We typically recommend downloading the latest version of Eclipse, directly from the Eclipse foundation. You will however need to make sure that you get the "Eclipse IDE for Enterprise Java Developers" packaged version of Eclipse, because this will include out of the box many useful tools such as WTP.

## Eclipse Plugins

1. Install AspectJ Development Tools.
    * Runway SDK utilizes source code weaving via AspectJ. In order to properly build our projects, we must install an Eclipse plugin called AspectJ Development Tools.
    * When you import one of our projects, Eclipe should automatically install this for you. If Eclipse does not automatically guide you through this process, open the Eclipse Marketplace and search for "AspectJ Development Tools" and click "install". You will be guided through the rest of the installation.
2. Install Maven Integration for AspectJ
    * Eclipse does not out of the box know how to weave AspectJ projects if they are derived from a maven pom. This plugin facilitates building AspectJ projects when using Maven.
    * In Eclipse go to ‘help -> Install New Software’
    * Enter this URL in the input field ‘http://dist.springsource.org/release/AJDT/configurator/’
    * When it finds "AJDT M2E configurator, check the box and click next to be guided through the rest of the installation.
3. Install M2E connector, buildhelper
    * Our projects typically contain source code which has been generated via Runway SDK. This source code is separated in a different src directory than is standard and expected via Maven. To facilitate this, we must install a Maven plugin called "buildhelper".
    * In Eclipse go to ‘window -> Preferences -> Maven -> Discovery -> Open Catalog’.
    * Search for buildhelper (by Sonatype) and install.

## Configure code formatting in Eclipse preferences.
In preferences, search for "format"

1. Import the 'TerraCube' formatting settings from 'git/runwaysdk-root/doc/TerraFrameJavaFormat.xml'
    * Java -> Code Style -> Formatter
    * Import and select active profile: Terracube
2. Manually set the Javascript code formatting settings
    * (Newer Eclipse) Web -> Client-side JavaScript -> Formatter
    * (Older Eclipse) Javascript -> Code Style -> Formatter.
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
2. In my experience, disabling Javascript validation at the workspace level does not work. You should also disable it by excluding the source using the Javascript Include Path configuration.
    * Right click your project -> Properties. Javascript -> Include Path. Click "Source" tab.
    * You may either manually exclude the source with a wildcard, or you can remove the source entirely from the "Include path"
    * If you choose to remove the source entirely from the include path then you are done. If you choose to exclude via a wildcard, then be sure to include the target directory as well as any lib or ng2 module directories.
    * Make sure to do this for ALL web projects, since this configuration is project specific. This may also include test projects (i.e. georegistry-test) if they are web projects.

## Add XML Catalog for working with Runway metadata schema files

[Follow the instructions here](/Runway-SDK/mydoc_metadata_authoring.html#classpathNotation)

{% include links.html %}
