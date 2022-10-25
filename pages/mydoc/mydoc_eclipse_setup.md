---
title: Eclipse Setup
permalink: mydoc_eclipse_setup.html
keywords: eclipse
summary: "Covers setting up a new Eclipse for Runway developers."
sidebar: mydoc_sidebar
folder: mydoc
---

## Downloading the Right Eclipse

The first thing you want to do is to make sure that the latest verison of Eclipse actually has AspectJ support, which you can check in the "Eclipse Plugins" section down below. Once you find a version of Eclipse which has AspectJ support, download it from the Eclipse foundation. You will need to make sure that you get the "Eclipse IDE for Enterprise Java Developers" packaged version of Eclipse, because this will include out of the box many useful tools such as WTP.

Personally I have found that it makes sense to unzip Eclipse on Linux to `~/program/eclipse/<year>-<month>`

## Downloading the Right Java

The Java version you use to run Eclipse will likely be different than that which you use to run Runway applications. For this reason we recommend using [SDKMan!](https://sdkman.io/) to install and manage the various Java versions which you will need to install on your machine.

Once SDKMan is installed, you can use `sdk java list` to list all the available Java verisons. We recommend installing:
1. Java 8 Open for Runway apps (from Java.net provider)
2. Java 11 Oracle for Eclipse [(The Eclipse foundation recommends only usng Sun or IBM based Java verisons, especially on Linux)](https://wiki.eclipse.org/IRC_FAQ#I_just_installed_Eclipse_on_Linux.2C_but_it_does_not_start._What_is_the_problem.3F)
3. Maven 3. For use in command-line applications.

When SDKMan asks if you want to set your newly Java as default, make sure to set the Java 11 Oracle as default. This will be used later when we configure Eclipse.

## Configuring Eclipse.ini

Once your Eclispe is downloaded and unzipped, you need to open the eclipse.ini file at the root of the unzipped directory. You will want to configure:

1. Memory settings
   * By default, Eclipse comes with 2GB memory configured. Given that Runway applications tend to be rather memory intensive, we recommend increasing this value if you can, which will depend upon how much memory your development machine has available. To do this, change the `-xmx` setting in your eclipse.ini. As an example, mine is configured as:
      
      -xmx8000m
2. JVM
   * Since your Java was installed with SDKMan, you will want to tell Eclipse where your Java is located. To do this, add a new line after the -vmargs, and write in the path to your SDKMan current java candidiate, which you can find by running `which java`. This path should look something like `/home/<user>/.sdkman/candidates/java/current/bin/java`

## Installing Java 8 Into Eclipse

Once Eclipse is up and running, you will need to tell it which version of Java to use for compiling and running Runway applications.

1. Install the SDK in Eclipse
   * In Eclipse, go to Window -> Preferences -> Java -> Installed JREs
   * Click add on the right
   * Click "Standard VM"
   * When asked for the JRE home, put the path to the SDKMan candidate, not current. This path will look something like:
      `/home/<user>/.sdkman/candidates/java/8.0.282.hs-adpt`
2. Specify the newly installed Java 8 as the default Java 8 execution environment
   * In Eclipse preferences, navigate to: Java -> Instaled JREs -> Execution Environments
   * Under JavaSE-1.8, left click the box on the right which lists the new Java 8 which you just installed.
3. Specify your default Java compiler
   * In Eclipse preferences, navigate to Java -> Compiler
   * Under 'JDK Compliance', specify the 'Compiler compliance level' to 1.8.

## Eclipse Plugins

1. Install AspectJ Development Tools.
    * Runway SDK utilizes source code weaving via AspectJ. In order to properly build our projects, we must install an Eclipse plugin called AspectJ Development Tools.
    * When you import one of our projects, Eclipe may attempt to automatically install this for you. Resist the urge to have Eclipse automatically do this for you, or to use the Eclipse Marketplace to install AspectJ. There are times when these processes have installed the wrong version for me and it has been tiresome to debug. Instead, [find the latest Eclipse update site via this link](https://github.com/kriegaex/org.aspectj/blob/master/docs/developer/IDE.md#eclipse). This wiki will also tell you which verisons of Eclipse are supported. Once you find the right update site, copy and paste the URL as follows:
    * * In Eclipse, go to ‘help -> Install New Software’
    * * Enter the URL in the "Work with" input field
    * * When it finds "AspectJ Development Tools (Required)", check the box and click next to be guided through the rest of the installation.
2. Install Maven Integration for AspectJ
    * Eclipse does not out of the box know how to weave AspectJ projects if they are derived from a maven pom. This plugin facilitates building AspectJ projects when using Maven.
    * In Eclipse go to ‘help -> Install New Software’
    * Enter this URL in the input field ‘http://dist.springsource.org/release/AJDT/configurator/’
    * When it finds "AJDT M2E configurator, check the box and click next to be guided through the rest of the installation.
3. Install M2E connector, buildhelper
    * Our projects typically contain source code which has been generated via Runway SDK. This source code is separated in a different src directory than is standard and expected via Maven. To facilitate this, we must install a Maven plugin called "buildhelper".
    * In Eclipse go to ‘window -> Preferences -> Maven -> Discovery -> Open Catalog’.
    * Search for buildhelper (by Takari or Sonatype) and install.
    * If it asks you to "trust unsigned content" click "select all" and continue.

## Configure code formatting in Eclipse preferences.

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
