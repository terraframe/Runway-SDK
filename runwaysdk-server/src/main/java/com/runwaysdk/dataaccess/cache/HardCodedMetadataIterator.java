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
package com.runwaysdk.dataaccess.cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFactory;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DefaultMdEntityInfo;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;


public class HardCodedMetadataIterator
{
  private ResultSet resultSet;
  
  private String type;
  
  private Map<String, Map<String, String>> relationshipAttributesHackMap;
  
  private boolean rootClass;
  
  public HardCodedMetadataIterator(ResultSet _resultSet, String _type,
      Map<String, Map<String, String>> _relationshipAttributesHackMap, boolean _rootClass)
  {
    this.resultSet = _resultSet;
    this.type = _type;
    this.relationshipAttributesHackMap = _relationshipAttributesHackMap;
    this.rootClass = _rootClass;
  }
  
  public Map<String, Map<String, Attribute>> next()
  {
    Map<String, Map<String, Attribute>> tupleMap = new HashMap<String, Map<String, Attribute>>();
    try
    {
      boolean hasResult = this.resultSet.next();
      if (!hasResult)
      {
        this.closeDBresources();
        return null;
      }
    
      Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();

      String id = this.resultSet.getString(EntityDAOIF.ID_COLUMN).toString();

      if (this.relationshipAttributesHackMap != null)
      {
        Map<String, String> relHackMap = new HashMap<String, String>(2);
        relHackMap.put(RelationshipInfo.PARENT_ID, this.resultSet.getString(RelationshipDAOIF.PARENT_ID_COLUMN).toString());
        relHackMap.put(RelationshipInfo.CHILD_ID, this.resultSet.getString(RelationshipDAOIF.CHILD_ID_COLUMN).toString());

        this.relationshipAttributesHackMap.put(id, relHackMap);
      }

      Map<String, Map<String, String>> mdAttributeInfoMap = DefaultMdEntityInfo.getAttributeMapForType(this.type);

      // Iterate over the fields
      for (String columnName : mdAttributeInfoMap.keySet())
      {
        if ( ( !columnName.equals(EntityDAOIF.ID_COLUMN) || type.equals(ElementInfo.CLASS) || this.rootClass ))
        {
          String attributeName = DefaultMdEntityInfo.getAttributeName(type, columnName);

          String attributeType = DefaultMdEntityInfo.getAttributeType(type, columnName);

          Object value = AttributeFactory.getColumnValueFromRow(this.resultSet, columnName, attributeType, false);

          Map<String, String> attributePropertyMap = mdAttributeInfoMap.get(columnName);
          String attributeTypeName = attributePropertyMap.get(EntityInfo.TYPE);
          String mdAttributeKey = attributePropertyMap.get(EntityInfo.KEY);
          Attribute attribute = AttributeFactory.createAttribute(mdAttributeKey, attributeTypeName,
              attributeName, this.type, value);

          if (attribute instanceof AttributeEnumeration)
          {
            AttributeEnumeration attributeEnumeration = (AttributeEnumeration) attribute;
            String cacheColumnName = MdAttributeEnumerationDAO.getCacheDbColumnName(columnName);
            String cachedEnumerationMappings = "";

            cachedEnumerationMappings = resultSet.getString(cacheColumnName);

            attributeEnumeration.initEnumMappingCache(cachedEnumerationMappings);
          }
          else if (attribute instanceof AttributeStruct)
          {
            AttributeStruct attributeStruct = ((AttributeStruct)attribute);
            attributeStruct.setStructDAO((StructDAO)ObjectCache.getEntityDAOIFfromCache(attributeStruct.getValue()));
            ObjectCache.removeEntityDAOIFfromCache(attributeStruct.getValue(), false);
          }

          attributeMap.put(attributeName, attribute);
        }
      }

      tupleMap.put(id, attributeMap);
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    
    return tupleMap;
  }
  
  private void closeDBresources()
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
