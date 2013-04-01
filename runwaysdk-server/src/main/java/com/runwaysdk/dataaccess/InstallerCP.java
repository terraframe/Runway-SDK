/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess;

import java.io.InputStream;

import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.XMLImporter;

/**
 * This class is the same as com.runwaysdk.dataaccess.Installer with one key difference:
 *   The metadata.xml file and the xsd are read from the server jar (using the classpath, hence the CP).
 */
public class InstallerCP
{
  public static void main (String args[])
  {
    int argOffSet = 4;

    if (args.length <= argOffSet)
    {
      String errMsg = "Five arguments are required for Installation:\n" +
        "  1) Root Database User\n" +
        "  2) Root Database Password\n" +
        "  3) Root Database Name\n"+
        "  4) metadata XSD resource path\n" +
        "  5) list of metadata resources separated by a space";
      throw new CoreException(errMsg);
    }
    
    String[] xmlFileDependencies = new String[args.length - argOffSet];
    
    for (int i=0; i<xmlFileDependencies.length; i++)
    {
      xmlFileDependencies[i] = args[i+argOffSet];
    }
    
    install(args[0], args[1], args[2], args[3],  xmlFileDependencies);
  }
  
  public static void install(String rootUser, String rootPass, String rootDb, String xsd, String[] xmlFiles)
  {
    Database.initialSetup(rootUser, rootPass, rootDb);
    
    InputStream xsdIS = InstallerCP.class.getClassLoader().getResourceAsStream(xsd);
    if (xsdIS == null) {
      throw new CoreException("Unable to find the xsd '" + xsd + "' on the classpath, the specified resource does not exist.");
    }
    
    InputStream[] xmlFilesIS = new InputStream[xmlFiles.length];
    for (int i = 0; i < xmlFiles.length; ++i) {
      String s = xmlFiles[i];
      
      InputStream is = InstallerCP.class.getClassLoader().getResourceAsStream(s);
      
      if (is == null) {
        throw new CoreException("Unable to find the metadata schema '" + s + "' on the classpath, the specified resource does not exist.");
      }
      
      xmlFilesIS[i] = is;
    }
    
    XMLImporter x = new XMLImporter(xsdIS, xmlFilesIS);
    x.toDatabase();
  }
}
