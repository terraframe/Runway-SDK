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
/**
 * 
 */
package com.runwaysdk.dataaccess.cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.runwaysdk.constants.MdAttributeBooleanUtil;
import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;

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
public class MdAttributeDimensionDAOStrategy extends CacheAllBusinessDAOstrategy
{

  /**
   * 
   */
  private static final long serialVersionUID = 2652069202447565104L;

  /**
   * @param classType
   */
  public MdAttributeDimensionDAOStrategy(String classType)
  {
    super(classType);
  }
  
  /**
   * Returns a list of sub types(including this type).
   * @return a list of sub types(including this type).
   */
  protected List<String> getSubTypesFromDatabase()
  {
    return Database.getConcreteSubClasses(MdAttributeDimensionInfo.ID_VALUE);
  }

  /**
   * 
   */
  public synchronized void reload()
  {
    super.reload();
    
    ResultSet resultSet = Database.getMdAttributeDimensionFields(null);
    
    try
    {
      while (resultSet.next())
      {
        String mdAttrDimensionId = resultSet.getString(MdAttributeDimensionInfo.ID);
        String stringBooleanValue = resultSet.getString(MdAttributeDimensionInfo.REQUIRED);
        String defaultValue = resultSet.getString(MdAttributeDimensionDAOIF.DEFAULT_VALUE);
        boolean isRequired = MdAttributeBooleanUtil.getBooleanValue(stringBooleanValue);
        String definingMdAttrId = resultSet.getString(MdAttributeDimensionDAOIF.DEFINING_MD_ATTRIBUTE);
        String mdDefiningDimensionId = resultSet.getString(MdAttributeDimensionDAOIF.DEFINING_MD_DIMENSION);
        
        MdAttributeDAO mdAttributeDAO = (MdAttributeDAO)ObjectCache.getEntityDAOIFfromCache(definingMdAttrId);  
        
        if(mdAttributeDAO != null)
        {
          mdAttributeDAO.addAttributeDimension(mdAttrDimensionId, isRequired, defaultValue, mdDefiningDimensionId);
          ObjectCache.putEntityDAOIFintoCache(mdAttributeDAO);
        }
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }
  }
  

}
