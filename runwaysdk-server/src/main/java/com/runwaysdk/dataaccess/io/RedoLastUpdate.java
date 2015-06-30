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
package com.runwaysdk.dataaccess.io;

import java.text.ParseException;

import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.session.Request;

public class RedoLastUpdate
{

  /**
   * @param args
   */
  @Request
  public static void main(String[] args)
  {
    if (args.length < 2)
    {
      String errMsg = "Two arguments are required for RedoLastUpdate:\n"
          + "  1) Location of the folder containing the schema(version date).xml files\n"
          + "  2) xsd file to use\n"
          + "  3) Number to Redo (optional)\n";
      throw new CoreException(errMsg);
    }

    try
    {
      String location = args[0];
      String xsd = args[1];
      Integer numToRollBack = 1;
      if (args.length == 3)
      {
        numToRollBack = Integer.parseInt(args[2]);
      }
      DatabaseVersioning versioner =  new DatabaseVersioning(location, xsd );
      versioner.rollBackNversions(numToRollBack);
      versioner.doAll();
    }
    catch (ParseException e)
    {
      throw new CoreException(e);
    }

    CacheShutdown.shutdown();
  }

}
