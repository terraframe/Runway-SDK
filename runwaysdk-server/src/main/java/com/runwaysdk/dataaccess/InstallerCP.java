/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.RunwayConfigurationException;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.RunwayProperties;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.util.ServerInitializerFacade;

/**
 * Installs all available metadata, preferably from the filesystem (at classpath com/runwaysdk/resources/metadata), into Runway.
 */
public class InstallerCP
{
  public static void main(String args[]) throws IOException
  {
    if (args.length != 4)
    {
      String errMsg = "Four arguments are required for Installation:\n" + "  1) Root Database User\n" + "  2) Root Database Password\n" + "  3) Root Database Name\n" + "  4) metadata XSD resource path\n";
      throw new CoreException(errMsg);
    }

    install(args[0], args[1], args[2], args[3]);
  }

  public static void install(String rootUser, String rootPass, String rootDb, String xsd) throws IOException
  {
    Database.initialSetup(rootUser, rootPass, rootDb);

    InputStream xsdIS = InstallerCP.class.getClassLoader().getResourceAsStream(xsd);
    if (xsdIS == null)
    {
      throw new RunwayConfigurationException("Unable to find the xsd '" + xsd + "' on the classpath, the specified resource does not exist.");
    }

    InputStream[] xmlFilesIS = buildMetadataInputStreamList();

    XMLImporter x = new XMLImporter(xsdIS, xmlFilesIS);
    x.toDatabase();

    ServerInitializerFacade.rebuild();
  }
  
  public static InputStream[] buildMetadataInputStreamList() throws IOException {
    ArrayList<String> xmlFileDependencies = getClassNamesFromPackage(ConfigGroup.METADATA.getPath());
    String[] xmlFiles = (String[]) xmlFileDependencies.toArray(new String[xmlFileDependencies.size()]);
    
    InputStream[] xmlFilesIS = new InputStream[xmlFiles.length];
    for (int i = 0; i < xmlFiles.length; ++i)
    {
      String s = xmlFiles[i] + ".xml";
      
      if (LocalProperties.isRunwayEnvironment()) {
        // Always read the metadata directly from src/main/resources if it exists, instead of reading it from a jar.
        try {
          String targetFilePath = RunwayProperties.getRunwayServerResources() + "/" + ConfigGroup.METADATA.getPath() + s;
          File targetMetadata = new File(targetFilePath);
          xmlFilesIS[i] = new FileInputStream(targetMetadata);
        }
        catch (Exception e) {
          InputStream is = ConfigurationManager.getResourceAsStream(ConfigGroup.METADATA, s);
          xmlFilesIS[i] = is;
        }
      }
      else {
        InputStream is = ConfigurationManager.getResourceAsStream(ConfigGroup.METADATA, s);
        xmlFilesIS[i] = is;
      }
    }
    
    return xmlFilesIS;
  }

  /**
   * Returns a list of names of files on the classpath with the given
   * packageName or inside the given directory.
   * 
   * @param packageName
   *          A classname or directory on the classpath
   * @return A list of fully qualified file names found inside the
   *         package/directory.
   * @throws IOException
   */
  public static ArrayList<String> getClassNamesFromPackage(String packageName) throws IOException
  {
    ClassLoader classLoader = InstallerCP.class.getClassLoader();
    Enumeration<URL> packageURLs;
    ArrayList<String> names = new ArrayList<String>();

    packageName = packageName.replace(".", "/");
    packageURLs = classLoader.getResources(packageName);

    while (packageURLs.hasMoreElements())
    {
      URL packageURL = packageURLs.nextElement();

      if (packageURL.getProtocol().equals("jar"))
      {
        String jarFileName;
        JarFile jf;
        Enumeration<JarEntry> jarEntries;
        String entryName;

        // build jar file name, then loop through zipped entries
        jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
        jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
        jf = new JarFile(jarFileName);
        try
        {
          jarEntries = jf.entries();
          while (jarEntries.hasMoreElements())
          {
            entryName = jarEntries.nextElement().getName();
            if (entryName.startsWith(packageName) && entryName.length() > packageName.length() + 5)
            {
              entryName = entryName.substring(entryName.lastIndexOf('/') + 1, entryName.lastIndexOf('.'));
              names.add(entryName);
            }
          }
        }
        finally
        {
          jf.close();
        }
      }
      else
      {
        File folder = new File(packageURL.getFile());
        File[] contenuti = folder.listFiles();
        String entryName;
        for (File actual : contenuti)
        {
          entryName = actual.getName();
          entryName = entryName.substring(0, entryName.lastIndexOf('.'));
          names.add(entryName);
        }
      }
    }

    return names;
  }
}