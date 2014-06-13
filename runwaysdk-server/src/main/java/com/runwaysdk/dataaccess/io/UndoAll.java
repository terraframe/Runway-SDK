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
package com.runwaysdk.dataaccess.io;

import java.text.ParseException;

import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.session.Request;

public class UndoAll
{
  @Request
  public static void main(String[] args)
  {
    try
    {
      boolean record = true;

      if (args.length < 1)
      {
        String errMsg = "Invalid argument(s) for Versioning:\n" + "  1) Location of the folder containing the schema(version date).xml files\n" + "  2) (OPTIONAL) location of xsd file to use 3) (OPTIONAL) Record boolean";
        throw new CoreException(errMsg);
      }
      
      String xsd = null;
      
      if (args.length > 1 && args[1] != null && !args[1].equals("") && !args[1].equals("null")) {
        xsd = args[1];
      }

      if (args.length > 2)
      {
        record = Boolean.parseBoolean(args[2]);
      }

      try
      {
        if (record)
        {
          new DatabaseVersioning(args[0], xsd).undoAll();
        }
        else
        {
          new Versioning(args[0], xsd).undoAll();
        }
      }
      catch (ParseException e)
      {
        throw new CoreException(e);
      }
    }
    finally
    {
      CacheShutdown.shutdown();
    }
  }
}
