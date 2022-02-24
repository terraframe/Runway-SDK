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
package com.runwaysdk.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import com.runwaysdk.business.generation.GenerationFacade;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

public class UpdateDatabaseSourceAndClasses
{

  /**
   * 
   * @param args
   */
  @Request
  public static void main(String[] args)
  {
    try
    {
      if (args.length > 0 && args[0].trim().equals("-compile"))
      {
        GenerationFacade.compileAll();
      }

      storeSourceAndClassesInDatabase();
    }
    finally
    {
      CacheShutdown.shutdown();
    }
  }

  /**
   * Stores the Java source and Class files on the file system in the database.
   * 
   */
  @Transaction
  public static void storeSourceAndClassesInDatabase()
  {
    Collection<MdTypeDAOIF> mdTypeIFGenerateClasses = new LinkedList<MdTypeDAOIF>();

    QueryFactory qFactory = new QueryFactory();
    BusinessDAOQuery mdTypeQ = qFactory.businessDAOQuery(MdTypeInfo.CLASS);
    OIterator<BusinessDAOIF> mdTypeIterator = mdTypeQ.getIterator();
    try {
      while (mdTypeIterator.hasNext())
      {
        MdTypeDAOIF mdTypeIF = (MdTypeDAOIF) mdTypeIterator.next();
        mdTypeIFGenerateClasses.add(mdTypeIF);
      }
    }
    finally {
      mdTypeIterator.close();
    }

    Connection conn = Database.getConnection();

    try
    {
      for (MdTypeDAOIF mdTypeIF : mdTypeIFGenerateClasses)
      {
        if (GenerationUtil.isSkipCompileAndCodeGeneration(mdTypeIF))
        {
          continue;
        }
        else if (mdTypeIF.getPackage().contains("com.runwaysdk.facade"))
        {
          continue;
        }

        // This cast is OK, as we are not modifying the object itself.
        ( (MdTypeDAO) mdTypeIF ).writeFileArtifactsToDatabaseAndObjects(conn);
      }
    }
    finally
    {
      try
      {
        Database.closeConnection(conn);
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
  }
}
