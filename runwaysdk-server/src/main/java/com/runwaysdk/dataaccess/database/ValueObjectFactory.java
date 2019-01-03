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
package com.runwaysdk.dataaccess.database;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTableClassIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.attributes.value.Attribute;
import com.runwaysdk.dataaccess.attributes.value.AttributeBlob;
import com.runwaysdk.dataaccess.attributes.value.AttributeEnumeration;
import com.runwaysdk.dataaccess.attributes.value.AttributeFactory;
import com.runwaysdk.dataaccess.attributes.value.AttributeMultiReference;
import com.runwaysdk.dataaccess.attributes.value.AttributeStruct;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableBlob;
import com.runwaysdk.query.SelectableEnumeration;
import com.runwaysdk.query.SelectableMultiReference;
import com.runwaysdk.query.SelectableStruct;
import com.runwaysdk.query.sql.MdAttributeConcrete_SQL;

public class ValueObjectFactory
{

  /**
   * Builds a ValueObject from a row from the given resultset.
   * 
   * @param definedByTableClassMap
   *          sort of a hack. It is a map where the key is the oid of an
   *          {@link MdAttributeDAOIF} and the value is the MdEntity that defines the
   *          attribute. This is used to improve performance.
   * @param MdAttributeIFList
   *          contains {@link MdAttributeDAOIF} objects for the attributes used in this query
   * @param resultSet
   *          ResultSet object from a query.
   * @return ValueObject from a row from the given resultset
   */
  public static ValueObject buildObjectFromQuery(Map<String, MdTableClassIF> definedByTableClassMap, List<Selectable> selectableList, ResultSet results)
  {
    // Get the attributes
    Map<String, Attribute> attributeMap = ValueObjectFactory.getAttributesFromQuery(definedByTableClassMap, selectableList, results);
    ValueObject valueObject = new ValueObject(attributeMap, ValueObject.class.getName(), ServerIDGenerator.nextID());
    return valueObject;
  }

  /**
   * Returns the attribute objects from a single query row necessary to
   * instantiate an ValueObject object.
   * 
   * @param definedByTableClassMap
   *          sort of a hack. It is a map where the key is the oid of an
   *          MdAttribute and the value is the MdEntity that defines the
   *          attribute. This is used to improve performance.
   * @param MdAttributeIFList
   *          contains MdAttribute objects for the attributes used in this query
   * @param selectableList
   *          list of {@link Selectable} objects used in the query
   * @param resultSet
   *          ResultSet object from a query.
   */
  protected static Map<String, Attribute> getAttributesFromQuery(Map<String, MdTableClassIF> definedByTableClassMap, List<Selectable> selectableList, ResultSet resultSet)
  {
    Map<String, Attribute> attributeMap = new LinkedHashMap<String, Attribute>();

    for (Selectable selectable : selectableList)
    {
      String definingType = null;
      MdAttributeConcreteDAOIF mdAttributeIF = selectable.getMdAttributeIF();

      if (! ( mdAttributeIF instanceof MdAttributeConcrete_SQL ))
      {
        MdTableClassIF mdTableClassIF = definedByTableClassMap.get(mdAttributeIF.getOid());

        if (mdTableClassIF == null)
        {
          mdTableClassIF = (MdTableClassIF) mdAttributeIF.definedByClass();

          // SQL pass through attributes do not have type metadata
          if (mdTableClassIF != null)
          {
            definedByTableClassMap.put(mdAttributeIF.getOid(), mdTableClassIF);
            definingType = mdTableClassIF.definesType();
          }
        }
      }

      String columnAlias = selectable.getColumnAlias();      
      Object columnValue = com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.getColumnValueFromRow(resultSet, columnAlias, mdAttributeIF.getType(), true);
      Attribute attribute = AttributeFactory.createAttribute(mdAttributeIF, mdAttributeIF.definesAttribute(), definingType, columnValue, selectable.getAllEntityMdAttributes());
      attributeMap.put(mdAttributeIF.definesAttribute(), attribute);

      if (selectable instanceof SelectableEnumeration)
      {
        SelectableEnumeration selectableEnumeration = (SelectableEnumeration)selectable;       
        String cacheColumnAlias = selectableEnumeration.getCacheColumnAlias();
        String cachedEnumerationMappings = (String) com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.getColumnValueFromRow(resultSet, cacheColumnAlias, MdAttributeCharacterInfo.CLASS, true);

        AttributeEnumeration attributeEnumeration = (AttributeEnumeration) attribute;
        attributeEnumeration.initEnumMappingCache(cachedEnumerationMappings);
      }
      else if (selectable instanceof SelectableMultiReference)
      {
        AttributeMultiReference attributeMultiReference = (AttributeMultiReference) attribute;

        String tableName = ( (MdAttributeMultiReferenceDAOIF) mdAttributeIF ).getTableName();
        Set<String> itemIds = Database.getEnumItemIds(tableName, attribute.getValue());

        attributeMultiReference.initMappingCache(itemIds);
      }
      else if (selectable instanceof SelectableBlob)
      {
        AttributeBlob attributeBlob = (AttributeBlob) attribute;
        attributeBlob.setBlobAsBytes(EntityDAOFactory.getBlobColumnValueFromRow(resultSet, columnAlias));
      }
      else if (selectable instanceof SelectableStruct)
      {
        SelectableStruct selectableStruct = (SelectableStruct)selectable;
        
        MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeIF;
        MdStructDAOIF mdStructIF = mdAttributeStructIF.getMdStructDAOIF();

        Map<String, com.runwaysdk.dataaccess.attributes.entity.Attribute> structAttributeMap = new HashMap<String, com.runwaysdk.dataaccess.attributes.entity.Attribute>();
        for (com.runwaysdk.query.Attribute queryAttributeStruct : selectableStruct.getStructAttributes())
        {
          MdAttributeDAOIF structMdAttributeDAOIF = queryAttributeStruct.getMdAttributeIF();
          
          String structColumnAlias = queryAttributeStruct.getColumnAlias();
          String structColumnValue = (String) com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.getColumnValueFromRow(resultSet, structColumnAlias, structMdAttributeDAOIF.getType(), true);

          com.runwaysdk.dataaccess.attributes.entity.Attribute entityStructAttribute = com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.createAttribute(structMdAttributeDAOIF.getKey(), structMdAttributeDAOIF.getType(), structMdAttributeDAOIF.definesAttribute(), mdStructIF.definesType(), structColumnValue);

          if (queryAttributeStruct instanceof SelectableEnumeration)
          {
            SelectableEnumeration selectableEnumeration = (SelectableEnumeration)queryAttributeStruct;

            com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration structAttributeEnumeration = (com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration) entityStructAttribute;

            String cacheColumnAlias = selectableEnumeration.getCacheColumnAlias();

            String cachedEnumerationMappings = (String) com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.getColumnValueFromRow(resultSet, cacheColumnAlias, MdAttributeCharacterInfo.CLASS, true);
            structAttributeEnumeration.initEnumMappingCache(cachedEnumerationMappings);
          }

          structAttributeMap.put(structMdAttributeDAOIF.definesAttribute(), entityStructAttribute);
        }

        StructDAO structDAO = null;
        com.runwaysdk.dataaccess.attributes.entity.Attribute idAttribute = structAttributeMap.get(EntityInfo.OID);
        if (!idAttribute.getValue().trim().equals(""))
        {
          structDAO = (StructDAO) StructDAOFactory.factoryMethod(structAttributeMap, mdStructIF.definesType());
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
