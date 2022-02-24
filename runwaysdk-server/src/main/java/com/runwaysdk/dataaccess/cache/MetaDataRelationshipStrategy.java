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

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.EntityDAOFactory;
import com.runwaysdk.dataaccess.database.RelationshipDAOFactory;


public class MetaDataRelationshipStrategy extends CacheAllRelationshipDAOStrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = -261152595636613913L;

  /**
   *
   * @param relationshipType
   */
  public MetaDataRelationshipStrategy(String relationshipType)
  {
    super(relationshipType);
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

    List<String> idList = new LinkedList<String>();
    
    String entityTableName = allTypesTableMap.get(this.getEntityType());

    // get parent_oid and child_oid
    Map<String, Map<String, String>> relationshipAttributesHackMap = new HashMap<String, Map<String, String>>();

    // get attributes from the root table
    String rootTableName = allTypesTableMap.get(rootType);
    HardCodedMetadataIterator i =
      Database.getAttributesForHardcodedMetadataType(entityTableName, rootType, rootTableName, relationshipAttributesHackMap, true);
    
    // Build incomplete objects with attributes only defined from the root type.
    Map<String, Map<String, Attribute>> tupleMap = i.next();
    while (tupleMap != null)
    {      
      String oid = tupleMap.keySet().iterator().next();
      idList.add(oid);
      Map<String, Attribute> attributeMap = tupleMap.get(oid);
      String type = attributeMap.get(ComponentInfo.TYPE).getValue();
      
      String parentOid = relationshipAttributesHackMap.get(oid).get(RelationshipInfo.PARENT_OID);
      String childOid = relationshipAttributesHackMap.get(oid).get(RelationshipInfo.CHILD_OID);
      
      RelationshipDAO relationshipDAO = RelationshipDAOFactory.get(oid, parentOid, childOid, attributeMap, type);
      this.updateCache(relationshipDAO);
      
      tupleMap = i.next();
    }
    
    for (String currentType : allTypesTableMap.keySet())
    {
      if (rootType.equals(currentType))
      {
        continue;
      }
          
      // get attributes from the table
      String tableName = allTypesTableMap.get(currentType);
      i = Database.getAttributesForHardcodedMetadataType(entityTableName, currentType, tableName, null, false);
      
      tupleMap = i.next();
      while (tupleMap != null)
      {       
        String oid = tupleMap.keySet().iterator().next();
        idList.add(oid);
        
        Map<String, Attribute> attributeMap = tupleMap.get(oid);

        RelationshipDAO relationshipDAO = (RelationshipDAO)ObjectCache.getEntityDAOIFfromCache(oid);       
        relationshipDAO.addAdditionalAttributes(attributeMap);
        ObjectCache.putEntityDAOIFintoCache(relationshipDAO);
        
        tupleMap = i.next();
      }
    }
    
    // No that the objects are complete, we need to update all of the other data structures this collection maintains.
    for (String oid : idList)
    {
      RelationshipDAO relationshipDAO = (RelationshipDAO)ObjectCache.getEntityDAOIFfromCache(oid);
      this.updateCache(relationshipDAO);
    }

  }

  /**
   *
   * @param oid
   *
   * @return
   */
  protected RelationshipDAOIF getFromFactory(String oid)
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
  protected RelationshipDAOIF getFromFactory(String oid, String type, String rootType, Map<String, String> inheritanceListMap)
  {
    // get parent_oid and child_oid
    Map<String, String> relationshipAttributesHackMap = new HashMap<String, String>(2);
    Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();

    // get attribute from each inherited class
    for (String thisType : inheritanceListMap.keySet())
    {
      boolean rootOfHierarchy = false;

      if (rootType.equals(thisType))
      {
        rootOfHierarchy = true;
      }

      String tableName = inheritanceListMap.get(thisType);

      attributeMap.putAll(EntityDAOFactory.getAttributesForHardcodedMetadata(oid, thisType, tableName, relationshipAttributesHackMap,
      rootOfHierarchy));
    }

    String parentOid = relationshipAttributesHackMap.get(RelationshipInfo.PARENT_OID);
    String childOid = relationshipAttributesHackMap.get(RelationshipInfo.CHILD_OID);

    RelationshipDAO relationshipDAO = RelationshipDAOFactory.get(oid, parentOid, childOid, attributeMap, type);
    return relationshipDAO;
  }

}
