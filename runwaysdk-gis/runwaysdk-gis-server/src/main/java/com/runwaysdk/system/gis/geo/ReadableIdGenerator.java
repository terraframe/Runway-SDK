/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.gis.geo;

import com.runwaysdk.business.generation.Base30;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class ReadableIdGenerator extends ReadableIdGeneratorBase
{
  private static final long serialVersionUID = 709841638;
  
  public ReadableIdGenerator()
  {
    super();
  }
 
  @Transaction
  public static String getNextId()
  {
    ReadableIdGenerator inst = ReadableIdGenerator.getInstance();
    inst.appLock();
    
    Long counter = inst.getCounter();
    inst.setCounter(counter+1);
    inst.apply();
    
    return Base30.toBase30String(counter, 6);
  }
  
  public static ReadableIdGenerator getInstance()
  {
    QueryFactory f = new QueryFactory();
    ReadableIdGeneratorQuery q = new ReadableIdGeneratorQuery(f);

    OIterator<? extends ReadableIdGenerator> iter = q.getIterator();
    try
    {
      while(iter.hasNext())
      {
        return iter.next(); // There will always be one.
      }

      return null;
    }
    finally
    {
      iter.close();
    }
  }
  
  @Override
  public void apply()
  {
    if(isNew())
    {
      String error = "ReadableIdGenerator is a singleton.";
      throw new RuntimeException(error);
    }

    super.apply();
  }
  
  @Override
  public void delete()
  {
    String error = "ReadableIdGenerator is a singleton.";
    throw new RuntimeException(error);
  }
}
