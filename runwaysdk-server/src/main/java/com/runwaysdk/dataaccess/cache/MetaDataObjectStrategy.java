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
package com.runwaysdk.dataaccess.cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.EntityDAOFactory;

public abstract class MetaDataObjectStrategy extends CacheAllBusinessDAOstrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = 8365650321769045564L;

  /**
   *
   *
   *
   * @param type
   *
   * @param bucketSize
   */
  public MetaDataObjectStrategy(String type)
  {
    super(type);
  }

  /**
   *Reloads the cache of this collection.  The cache is cleared.  All EntityDAOs of this
   * type stored in this collection are instantiated an put in the cache.  This method
   * uses the database instead of the metadata cache.
   *
   * <br/><b>Precondition:</b>   true
   *
   * <br/><b>Postcondition:</b>  Cache contains all EntityDAOs of this type that are to be stored in this
   *        collection
   *
   */
  public synchronized void reload()
  {
    // Key: type, Value: tablename
    Map<String, String> allTypesTableMap = new HashMap<String, String>();

    // Get a list of classes that the given class inherits from
    LinkedHashMap<String, String> rootInheritanceListMap = EntityDAOFactory.getSuperEntityTypes(this.getEntityType());

    allTypesTableMap.putAll(rootInheritanceListMap);

    // Root type is the last key in the ordered map
    String rootType = "";
    for (String someType : rootInheritanceListMap.keySet())
    {
      rootType = someType;
    }

    // Key is the subtype.  Value is a map with the key
    // is an inherited type and and the value is the database table.

    // IMPORTANT! Start with the subclasses first
    List<String> attributeClassList = this.getSubTypesFromDatabase();
    for (int i=attributeClassList.size()-1; i >=0; i--)
    {
      String subType = attributeClassList.get(i);

      // Get a list of classes that the given class inherits from
      LinkedHashMap<String, String> subTypeInheritanceListMap = EntityDAOFactory.getSuperEntityTypes(subType);

      allTypesTableMap.putAll(subTypeInheritanceListMap);
    }

    List<String> idList = new LinkedList<String>();
    
    String entityTableName = allTypesTableMap.get(this.getEntityType());

    // get attributes from the root table
    String rootTableName = allTypesTableMap.get(rootType);
    HardCodedMetadataIterator i =
      Database.getAttributesForHardcodedMetadataType(entityTableName, rootType, rootTableName, null, true);
    
    // Build incomplete objects with attributes only defined from the root type.
    Map<String, Map<String, Attribute>> tupleMap = i.next();
    while (tupleMap != null)
    {      
      Entry<String, Map<String, Attribute>> entry = tupleMap.entrySet().iterator().next();

      String oid = entry.getKey();
      idList.add(oid);
      
      Map<String, Attribute> attributeMap = entry.getValue();
      String type = attributeMap.get(ComponentInfo.TYPE).getValue();
      BusinessDAO businessDAO = BusinessDAOFactory.factoryMethod(attributeMap, type, false);
      ObjectCache.putEntityDAOIFintoCache(businessDAO);
      
      tupleMap = i.next();
    }
    
    for (Entry<String, String> entry : allTypesTableMap.entrySet())
    {
      String currentType = entry.getKey();
      
      if (rootType.equals(currentType))
      {
        continue;
      }
          
      // get attributes from the table
      String tableName = entry.getValue();
      i = Database.getAttributesForHardcodedMetadataType(entityTableName, currentType, tableName, null, false);
      
      tupleMap = i.next();
      while (tupleMap != null)
      {       
        String oid = tupleMap.keySet().iterator().next();
        idList.add(oid);
        
        Map<String, Attribute> attributeMap = tupleMap.get(oid);

        BusinessDAO businessDAO = (BusinessDAO)ObjectCache.getEntityDAOIFfromCache(oid);       
        businessDAO.addAdditionalAttributes(attributeMap);
        ObjectCache.putEntityDAOIFintoCache(businessDAO);
        
        tupleMap = i.next();
      }
    }
    
    // Now that the objects are complete, we need to update all of the other data structures this collection maintains.
    for (String oid : idList)
    {
      BusinessDAO businessDAO = (BusinessDAO)ObjectCache.getEntityDAOIFfromCache(oid);
      this.updateCache(businessDAO);
    }    
  }
    
  /**
   * Returns a list of sub types(including this type).
   * @return a list of sub types(including this type).
   */
  protected abstract List<String> getSubTypesFromDatabase();

  /**
   *
   * @param oid
   *
   * @return
   */
  protected BusinessDAOIF getFromFactory(String oid)
  {
    String type = Database.getTypeFromInstanceId(oid);

    // Get a list of classes that the given class inherits from
    LinkedHashMap<String, String> inheritanceListMap = EntityDAOFactory.getSuperEntityTypes(type);

    // Root type is the last key in the ordered map
    String rootType = "";
    for (String someType : inheritanceListMap.keySet())
    {
      rootType = someType;
    }

    return this.getFromFactory(oid, type, rootType, inheritanceListMap);
  }

  /**
   *
   * @param oid
   * @param type
   * @param rootType
   * @param inheritanceListMap
   *
   * @return
   */
  protected BusinessDAOIF getFromFactory(String oid, String type, String rootType, Map<String, String> inheritanceListMap)
  {
    // get attribute from each inherited class

    Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();

    for (Entry<String, String> entry : inheritanceListMap.entrySet())
    {
      String thisType = entry.getKey();
      String tableName = entry.getValue();
      
      boolean rootOfHierarchy = false;

      if (rootType.equals(thisType))
      {
        rootOfHierarchy = true;
      }

      attributeMap.putAll(EntityDAOFactory.getAttributesForHardcodedMetadata(oid, thisType, tableName, null,
      rootOfHierarchy));
    }

    BusinessDAO businessDAO = BusinessDAOFactory.factoryMethod(attributeMap, type, false);

    return businessDAO;
  }
}
