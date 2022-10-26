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
/*
 * Created on Jun 8, 2005
 *
 */
package com.runwaysdk.dataaccess.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;


/**
 *Provides a quick lookup for the DataAccess datatypes of attributes.  This class is used during
 * the DataAccess startup process before the metadata is populated, and should not be used after
 * the metadata is populated.
 *
 * @author nathan
 *
 * @version $Revision: 1.15 $
 * @since 1.4
 */
public class DefaultMdEntityInfo
{
  /**
   * The key is the name of a class, the object value is another map
   * with values for each attribute defined by the class.  The key in
   * the attribute map is the database column name of the attribute, which
   * is the attribute name in lower case.  The final map contains properties
   * of the attribute.
   * <br/><b>invariant</b> classMap != null
   */
  // type, column name
  private static volatile Map<String, Map<String, Map<String, String>>> entityAttributeMap;

  /**
   *Refreshes the quick lookup cache.
   *
   * <br/><b>Precondition:</b>   true
   * <br/><b>Postcondition:</b>  true
   *
   */
  public static void refresh()
  {
    entityAttributeMap = new HashMap<String, Map<String, Map<String, String>>>();
    populateEntityMap();
  }

  /**
   * Returns a map information on attributes defined by the given type.
   * @param type
   * @return information on attributes defined by the given type.
   */
  public static Map<String, Map<String, String>> getAttributeMapForType(String type)
  {
    Map<String, Map<String, String>> returnMap = entityAttributeMap.get(type);
    
    if (returnMap == null)
    {
      returnMap = new HashMap<String, Map<String, String>>();
    }
    
    return returnMap;
  }

  /**
   * Returns the name of the attribute that defines the given database column name defined by the given BusinessDAO
   *  class.  This method should not be called after the metadata cache has been populated.
   *
   * <br/><b>Precondition:</b>  type != null
   * <br/><b>Precondition:</b>  !type.trim().equals("")
   * <br/><b>Precondition:</b>  attributeName != null
   * <br/><b>Precondition:</b>  !attributeName.trim().equals("")
   * <br/><b>Postcondition:</b> return value may not be null
   *
   * @param type type of the class that defines the given attribute
   * @param attributeColumnName attribute name
   * @return datatype of the given attribute defined by the given BusinessDAO.  This method
   *  should not be called after the metadata cache has been populated
   */
  public static String getAttributeName(String type, String attributeColumnName)
  {
    Map<String, Map<String, String>> attributeMap = entityAttributeMap.get(type);

    Map<String, String> attributePropertyMap = attributeMap.get(attributeColumnName);
    String returnString = attributePropertyMap.get(MdAttributeConcreteInfo.NAME);

    return returnString;
  }

  /**
   * Returns the Runway type of the attribute that defines the given database column name defined by the given EntityDAO
   *  class.  This method should not be called after the metadata cache has been populated.
   *
   * <br/><b>Precondition:</b>  type != null
   * <br/><b>Precondition:</b>  !type.trim().equals("")
   * <br/><b>Precondition:</b>  attributeName != null
   * <br/><b>Precondition:</b>  !attributeName.trim().equals("")
   * <br/><b>Postcondition:</b> return value may not be null
   *
   * @param type type of the class that defines the given attribute
   * @param attributeColumnName attribute name
   * @return Runway type of the given attribute defined by the given EntityDAO.  This method
   *  should not be called after the metadata cache has been populated
   */
  public static String getAttributeType(String type, String attributeColumnName)
  {
    Map<String, Map<String, String>> attributeMap = entityAttributeMap.get(type);

    Map<String, String> attributePropertyMap = attributeMap.get(attributeColumnName);
    String returnString = attributePropertyMap.get(EntityInfo.TYPE);

    return returnString;
  }

  /**
   * Populates the entityMap with maps for each entity. Each map where the key
   * is the name of an attribute and the value is the datatype of that
   * attribute.
   * 
   */
  private static void populateEntityMap()
  {
    String query = "SELECT "+MdAttributeConcreteDAOIF.TABLE+"."+MdAttributeConcreteDAOIF.NAME_COLUMN+
                           ", "+MdAttributeConcreteDAOIF.TABLE+"."+MdAttributeConcreteDAOIF.COLUMN_NAME_COLUMN+
                           ", "+MdAttributeConcreteDAOIF.TABLE+"."+MdAttributeConcreteDAOIF.INDEX_TYPE_COLUMN+
                           ", "+MetadataDAOIF.TABLE+"."+EntityDAOIF.TYPE_COLUMN+" \n"+
                           ", "+MetadataDAOIF.TABLE+"."+EntityDAOIF.KEY_COLUMN+" \n"+
                   "FROM "+MetadataDAOIF.TABLE+", "+MdAttributeConcreteDAOIF.TABLE+" \n"+
                   "WHERE "+MetadataDAOIF.TABLE+"."+EntityDAOIF.ID_COLUMN+" = "+MdAttributeConcreteDAOIF.TABLE+"."+EntityDAOIF.ID_COLUMN+" \n"+
                   "AND "+MdAttributeConcreteDAOIF.TABLE+"."+EntityDAOIF.ID_COLUMN+" IN \n"+
                   "  (SELECT "+RelationshipDAOIF.CHILD_OID_COLUMN+" FROM "+RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getTableName()+" \n"+
                   "   WHERE "+RelationshipDAOIF.PARENT_OID_COLUMN+" IN \n" +
                   "     (SELECT "+MdTypeDAOIF.TABLE+"."+EntityDAOIF.ID_COLUMN+" \n " +
                   "      FROM "+MdTypeDAOIF.TABLE+", "+MetadataDAOIF.TABLE+" \n"+
                   "      WHERE "+MetadataDAOIF.TABLE+"."+EntityDAOIF.ID_COLUMN+" = "+MdTypeDAOIF.TABLE+"."+EntityDAOIF.ID_COLUMN+" \n"+
                   "     )\n" +
                   "  )";

    ResultSet resultSet = Database.query(query);
    try
    {
      while (resultSet.next())
      {
        // Original code had fully qualified names here. DynaBeans only
        // recognize fully qualified names if there is a column name conflict,
        // which forces
        // qualification. This should never happen. If it does, add if
        // conditions here.
        String attributeName = resultSet.getString(MdAttributeConcreteDAOIF.NAME_COLUMN);
        String columnName = resultSet.getString(MdAttributeConcreteDAOIF.COLUMN_NAME_COLUMN);
        String attributeType = resultSet.getString(EntityDAOIF.TYPE_COLUMN);
        String mdAttributeKey = resultSet.getString(EntityDAOIF.KEY_COLUMN);
        String indexType = resultSet.getString(MdAttributeConcreteDAOIF.INDEX_TYPE_COLUMN);

        // Parse the defining type from the attribute key
        String[] typeTokens = mdAttributeKey.split("\\.");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < typeTokens.length - 1; i++)
        {
          if (i != 0)
          {
            builder.append(".");
          }
          builder.append(typeTokens[i]);
        }
        String type = builder.toString().trim();

        Map<String, Map<String, String>> attributeMap = entityAttributeMap.get(type);
        if (attributeMap == null)
        {
          attributeMap = new HashMap<String, Map<String, String>>();
          entityAttributeMap.put(type, attributeMap);
        }

        Map<String, String> attributePropertyMap = new HashMap<String, String>();

        attributePropertyMap.put(MdAttributeConcreteInfo.NAME, attributeName);
        attributePropertyMap.put(EntityInfo.TYPE, attributeType);
        attributePropertyMap.put(EntityInfo.KEY, mdAttributeKey);
        attributePropertyMap.put(MdAttributeConcreteInfo.INDEX_TYPE, indexType);

        attributeMap.put(columnName, attributePropertyMap);
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


