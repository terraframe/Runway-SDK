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
package com.runwaysdk.dataaccess.database;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.attributes.value.Attribute;
import com.runwaysdk.dataaccess.attributes.value.AttributeBlob;
import com.runwaysdk.dataaccess.attributes.value.AttributeEnumeration;
import com.runwaysdk.dataaccess.attributes.value.AttributeFactory;
import com.runwaysdk.dataaccess.attributes.value.AttributeStruct;
import com.runwaysdk.query.ColumnInfo;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.sql.MdAttributeConcrete_SQL;

public class ValueObjectFactory
{

  /**
   * Builds a ValueObject from a row from the given resultset.
   * @param columnInfoMap         contains information about attributes used in the query
   * @param definedByMdEntityMap  sort of a hack.  It is a map where the key is the id of an MdAttribute and the value is the
   *                              MdEntity that defines the attribute.  This is used to improve performance.
   * @param MdAttributeIFList     contains MdAttribute objects for the attributes used in this query
   * @param resultSet             ResultSet object from a query.
   * @return ValueObject from a row from the given resultset
   */
  public static ValueObject buildObjectFromQuery( Map<String, ColumnInfo> columnInfoMap, Map<String, MdEntityDAOIF> definedByMdEntityMap,
      List<Selectable> selectableList, ResultSet results)
  {
    // Get the attributes
    Map<String, Attribute> attributeMap = ValueObjectFactory.getAttributesFromQuery(columnInfoMap, definedByMdEntityMap, selectableList, results);
    ValueObject valueObject = new ValueObject(attributeMap, ValueObject.class.getName(), ServerIDGenerator.nextID());
    return valueObject;
  }

  /**
   * Returns the attribute objects from a single query row necessary to
   * instantiate an ValueObject object.
   *
   * @param columnInfoMap
   *          contains information about attributes used in the query
   * @param definedByMdEntityMap
   *          sort of a hack. It is a map where the key is the id of an
   *          MdAttribute and the value is the MdEntity that defines the
   *          attribute. This is used to improve performance.
   * @param MdAttributeIFList
   *          contains MdAttribute objects for the attributes used in this query
   * @param resultSet
   *          ResultSet object from a query.
   */
  protected static Map<String, Attribute> getAttributesFromQuery(Map<String, ColumnInfo> columnInfoMap,
      Map<String, MdEntityDAOIF> definedByMdEntityMap, List<Selectable> selectableList,
      ResultSet resultSet)
  {
    Map<String, Attribute> attributeMap = new LinkedHashMap<String, Attribute>();

    for (Selectable selectable : selectableList)
    {
      String definingType = null;
      MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

      if (!(mdAttributeIF instanceof MdAttributeConcrete_SQL))
      {
         MdEntityDAOIF mdEntityIF = definedByMdEntityMap.get(mdAttributeIF.getId());

        if (mdEntityIF == null)
        {
          mdEntityIF = (MdEntityDAOIF)mdAttributeIF.definedByClass();

          // SQL pass through attributes do not have type metadata
          if (mdEntityIF != null)
          {
            definedByMdEntityMap.put(mdAttributeIF.getId(), mdEntityIF);
           definingType = mdEntityIF.definesType();
          }
        }
      }

      String attributeName = selectable._getAttributeName();
      String attributeNameSpace = selectable.getAttributeNameSpace();
      String attributeQualifiedName = attributeNameSpace + "." + attributeName;

      ColumnInfo columnInfo = columnInfoMap.get(attributeQualifiedName);

      String columnAlias = columnInfo.getColumnAlias();

      Object columnValue =
        com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.getColumnValueFromRow(resultSet, columnAlias, mdAttributeIF.getType(), true);

      Attribute attribute = AttributeFactory.createAttribute(mdAttributeIF, mdAttributeIF.definesAttribute(),
          definingType, columnValue, selectable.getAllEntityMdAttributes());

      attributeMap.put(mdAttributeIF.definesAttribute(), attribute);

      if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
      {
        AttributeEnumeration attributeEnumeration = (AttributeEnumeration) attribute;

        String cacheColumnName = ( (MdAttributeEnumerationDAOIF) mdAttributeIF ).getCacheColumnName();
        String cacheAttributeQualifiedName = attributeNameSpace + "." + cacheColumnName;
        ColumnInfo cacheColumnInfo = columnInfoMap.get(cacheAttributeQualifiedName);

        String cachedEnumerationMappings =
          (String)com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.getColumnValueFromRow(resultSet, cacheColumnInfo.getColumnAlias(), MdAttributeCharacterInfo.CLASS, true);
        attributeEnumeration.initEnumMappingCache(cachedEnumerationMappings);
      }
      else if (mdAttributeIF instanceof MdAttributeBlobDAOIF)
      {
         AttributeBlob attributeBlob = (AttributeBlob)attribute;
         attributeBlob.setBlobAsBytes(EntityDAOFactory.getBlobColumnValueFromRow(resultSet, columnAlias));
      }
      else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
      {
        MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeIF;
        MdStructDAOIF mdStructIF = mdAttributeStructIF.getMdStructDAOIF();
        List<? extends MdAttributeConcreteDAOIF> structMdAttributeList = mdStructIF.definesAttributes();

        Map<String, com.runwaysdk.dataaccess.attributes.entity.Attribute> structAttributeMap = new HashMap<String, com.runwaysdk.dataaccess.attributes.entity.Attribute>();
        for (MdAttributeConcreteDAOIF structMdAttributeIF : structMdAttributeList)
        {
          String structQualifiedAttributeName = attributeQualifiedName + "."
              + structMdAttributeIF.definesAttribute();
          ColumnInfo structColumnInfo = columnInfoMap.get(structQualifiedAttributeName);
          String structColumnAlias = structColumnInfo.getColumnAlias();
          String structColumnValue =
            (String)com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.getColumnValueFromRow(resultSet, structColumnAlias, structMdAttributeIF.getType(), true);

          com.runwaysdk.dataaccess.attributes.entity.Attribute structAttribute =
             com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.createAttribute(structMdAttributeIF.getKey(), structMdAttributeIF.getType(),
              structMdAttributeIF.definesAttribute(), mdStructIF.definesType(), structColumnValue);

          if (structMdAttributeIF instanceof MdAttributeEnumerationDAOIF)
          {
            MdAttributeEnumerationDAOIF structMdAttributeEnumIF = (MdAttributeEnumerationDAOIF)structMdAttributeIF;

            com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration structAttributeEnumeration = (com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration) structAttribute;
            String cacheColumnName = structMdAttributeEnumIF.getCacheColumnName();
            String cacheAttributeQualifiedName = attributeQualifiedName + "." + cacheColumnName;

            ColumnInfo cacheColumnInfo = columnInfoMap.get(cacheAttributeQualifiedName);

            String cachedEnumerationMappings =
              (String)com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.getColumnValueFromRow(resultSet, cacheColumnInfo.getColumnAlias(), MdAttributeCharacterInfo.CLASS, true);
            structAttributeEnumeration.initEnumMappingCache(cachedEnumerationMappings);
          }

          structAttributeMap.put(structMdAttributeIF.definesAttribute(), structAttribute);
        }

        StructDAO structDAO = null;
        com.runwaysdk.dataaccess.attributes.entity.Attribute idAttribute = structAttributeMap.get(EntityInfo.ID);
        if (!idAttribute.getValue().trim().equals(""))
        {
          structDAO = (StructDAO) StructDAOFactory.factoryMethod(structAttributeMap,
              mdStructIF.definesType());
        }
        else
        {
          structDAO = (StructDAO) StructDAO.newInstance(mdStructIF.definesType());
          structDAO.setIsNew(true);
          structDAO.setAppliedToDB(false);
        }

        ( (AttributeStruct) attribute ).setStructDAO(structDAO);
      }
    }
    return attributeMap;
  }

}
