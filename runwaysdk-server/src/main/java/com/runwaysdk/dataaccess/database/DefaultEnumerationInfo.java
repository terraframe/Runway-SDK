/**
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
 */
package com.runwaysdk.dataaccess.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;


public class DefaultEnumerationInfo
{
  /**
   * key = packageName.classType.AttributeName
   * value = tableName
   */
  private static volatile Map<String, String> enumerationTableMap;
  
  public static void refresh()
  {
    buildEnumerationInfoMap();
  }
  
  /**
   * 
   *
   */
  private static void buildEnumerationInfoMap()
  {
    enumerationTableMap = new Hashtable<String, String>();

    String query = 
      " SELECT  MdEnumeration."+MdEnumerationDAOIF.TABLE_NAME_COLUMN+", \n "+
      "         MdEntity."+MdTypeDAOIF.TYPE_NAME_COLUMN+", \n "+
      "         MdEntity."+MdTypeDAOIF.PACKAGE_NAME_COLUMN+", \n "+
      "         "+MdAttributeConcreteDAOIF.TABLE+"."+MdAttributeConcreteDAOIF.NAME_COLUMN+" \n "+
      "    FROM "+MdTypeDAOIF.TABLE+" MdEntity, \n"+
      "         "+MdEnumerationDAOIF.TABLE+" MdEnumeration, \n"+
      "         "+MdAttributeConcreteDAOIF.TABLE+", \n"+
      "         "+MdAttributeEnumerationDAOIF.TABLE+" \n"+
      "   WHERE MdEnumeration."+EntityDAOIF.ID_COLUMN+" = "+MdAttributeEnumerationDAOIF.TABLE+"."+MdAttributeEnumerationDAOIF.MD_ENUMERATION_COLUMN+" \n"+
      "     AND "+MdAttributeConcreteDAOIF.TABLE+"."+EntityDAOIF.ID_COLUMN+" = "+MdAttributeEnumerationDAOIF.TABLE+"."+EntityDAOIF.ID_COLUMN+" \n"+
      "     AND "+MdAttributeConcreteDAOIF.TABLE+"."+MdAttributeConcreteDAOIF.DEFINING_MD_CLASS_COLUMN+" = "+"MdEntity."+EntityDAOIF.ID_COLUMN;
      
    ResultSet resultSet = Database.query(query);
    
    try
    {
      while (resultSet.next())
      {
        String tableName = resultSet.getString(MdEnumerationDAOIF.TABLE_NAME_COLUMN);
        String packageName = resultSet.getString(MdTypeDAOIF.PACKAGE_NAME_COLUMN);
        String className = resultSet.getString(MdTypeDAOIF.TYPE_NAME_COLUMN);
        String attributeName = resultSet.getString(MdAttributeConcreteDAOIF.NAME_COLUMN);  

        enumerationTableMap.put(packageName+"."+className+"."+attributeName, tableName);      
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
