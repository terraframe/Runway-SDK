/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.RunwayConfigurationException;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.RunwayProperties;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.resource.ClasspathResource;
import com.runwaysdk.util.ServerInitializerFacade;

/**
 * Installs all available metadata, preferably from the filesystem (at classpath com/runwaysdk/resources/metadata), into Runway.
 */
public class InstallerCP
{
  private static final Logger logger = LoggerFactory.getLogger(InstallerCP.class);
  
  public static void main(String args[]) throws IOException
  {
    if (args.length != 4 && args.length != 5)
    {
      String errMsg = "Four arguments are required for Installation:\n" + "  1) Root Database User\n" + "  2) Root Database Password\n" + "  3) Root Database Name\n" + "  4) metadata XSD resource path\n";
      throw new CoreException(errMsg);
    }
    
    String[] metadataFiles = null;
    if (args.length == 5)
    {
      metadataFiles = StringUtils.split(args[4], ",");
    }

    install(args[0], args[1], args[2], args[3], metadataFiles);
  }

  public static void install(String rootUser, String rootPass, String rootDb, String xsd, String[] metadataFiles) throws IOException
  {
    Database.initialSetup(rootUser, rootPass, rootDb);
    
    InputStream xsdIS = InstallerCP.class.getClassLoader().getResourceAsStream(xsd);
    if (xsdIS == null)
    {
      throw new RunwayConfigurationException("Unable to find the xsd '" + xsd + "' on the classpath, the specified resource does not exist.");
    }

    InputStream[] xmlFilesIS;
    if (metadataFiles != null)
    {
      xmlFilesIS = buildMetadataInputStreamList(metadataFiles);
    }
    else
    {
      xmlFilesIS = buildMetadataInputStreamList();
    }

    XMLImporter x = new XMLImporter(xsdIS, xmlFilesIS);
    x.toDatabase();

    ServerInitializerFacade.rebuild();
  }
  
  public static InputStream[] buildMetadataInputStreamList() throws IOException
  {
    return buildMetadataInputStreamList(new String[] {"metadata.xml", "gismetadata.xml"});
  }
  
  public static InputStream[] buildMetadataInputStreamList(String[] metadataFiles) throws IOException
  {
//    List<ClasspathResource> xmlFileDependencies = orderMetadataFiles(ClasspathResource.getResourcesInPackage(ConfigGroup.METADATA.getPath()));
    
    logger.info("Importing runway metadata.xml files [" + StringUtils.join(metadataFiles, ",") + "] into database.");
    List<ClasspathResource> xmlFileDependencies = getMetadataFiles(metadataFiles);
    
    ClasspathResource[] xmlFiles = (ClasspathResource[]) xmlFileDependencies.toArray(new ClasspathResource[xmlFileDependencies.size()]);
    
    InputStream[] xmlFilesIS = new InputStream[xmlFiles.length];
    for (int i = 0; i < xmlFiles.length; ++i)
    {
      ClasspathResource cpr = xmlFiles[i];
      
      if (cpr.getNameExtension().equals("xml"))
      {
        String s = cpr.getName();
        
        if (LocalProperties.isRunwayEnvironment()) {
          // Always read the metadata directly from src/main/resources if it exists, instead of reading it from a jar.
          try {
            String targetFilePath = RunwayProperties.getRunwayServerResources() + "/" + ConfigGroup.METADATA.getPath() + s;
            File targetMetadata = new File(targetFilePath);
            xmlFilesIS[i] = new FileInputStream(targetMetadata);
          }
          catch (Exception e) {
            xmlFilesIS[i] = cpr.openNewStream();
          }
        }
        else {
          xmlFilesIS[i] = cpr.openNewStream();
        }
      }
    }
    
    return xmlFilesIS;
  }
  
  public static List<ClasspathResource> getMetadataFiles(String[] sMetadataFiles)
  {
    List<ClasspathResource> lcpr = new ArrayList<ClasspathResource>();
    
    for (String cpLoc : sMetadataFiles)
    {
      lcpr.add(new ClasspathResource(ConfigGroup.METADATA.getPath() + cpLoc));
    }
    
    return lcpr;
  }
  
//  public static List<ClasspathResource> orderMetadataFiles(List<ClasspathResource> resources)
//  {
//    // Yes I know this is a gigantic hack but I'm in a time crunch. We can fix this later.
//    ClasspathResource metadataXml = null;
//    ClasspathResource gisMetadataXml = null;
//    
//    List<ClasspathResource> ordered = new ArrayList<ClasspathResource>();
//    
//    for (int i = 0; i < resources.size(); ++i)
//    {
//      ClasspathResource res = resources.get(i);
//      
//      if (res.getName().equals("metadata.xml"))
//      {
//        metadataXml = res;
//      }
//      else if (res.getName().equals("gismetadata.xml"))
//      {
//        gisMetadataXml = res;
//      }
//    }
//    
//    if (metadataXml != null) { ordered.add(metadataXml); }
//    if (gisMetadataXml != null) { ordered.add(gisMetadataXml); }
//    
//    return ordered;
//  }
}