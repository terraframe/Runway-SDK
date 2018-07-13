
/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK GIS(tm). If not, see <http://www.gnu.org/licenses/>.
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;

import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.Versioning;
import com.runwaysdk.session.Request;

public class SchemaUpdater
{
  private static Log     log     = LogFactory.getLog(Sandbox.class);

  private static boolean logging = true;

  @Request
  public static void main(String[] args)
  {
    try
    {
      init();
      Versioning.main(args);

      System.out.println("---- SUCCESS!!! ------");
    }
    catch (Throwable t)
    {
      System.out.println("---- ERROR!!! ------");

      t.printStackTrace(System.out);
    }
  }

  private static void init()
  {
    if (logging)
    {
      Database.enableLoggingDMLAndDDLstatements(true);
    }

    // Use the console logger for quick debugging.
    BasicConfigurator.configure();
  }
}
