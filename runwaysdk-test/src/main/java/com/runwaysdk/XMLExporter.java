/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.runwaysdk;

import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.io.FacadeIO;

public class XMLExporter
{
  public static void main(String[] args)
  {
    try
    {
      if (args.length != 2)
      {
        String errMsg = "Two arguments are required for XMLExporter:\n" + "  1) metadata XSD file path\n" + "  2) metadata XML file path";
        throw new CoreException(errMsg);
      }

      String schemaFile = args[0];
      String exportFile = args[1];

      FacadeIO.exportAll(schemaFile, exportFile);
    }
    catch (Throwable t)
    {
      t.printStackTrace(System.out);
    }
    finally
    {
      CacheShutdown.shutdown();
    }
  }
}
