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
 * Created on Jul 11, 2004
 */
package com.runwaysdk.dataaccess.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.IndexAttributeInfo;
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.EnumerationAttributeItem;
import com.runwaysdk.dataaccess.GraphDAO;
import com.runwaysdk.dataaccess.IndexAttributeDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdGraphDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTableClassIF;
import com.runwaysdk.dataaccess.MdTermRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTreeDAOIF;
import com.runwaysdk.dataaccess.MetadataRelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipRecursionException;
import com.runwaysdk.dataaccess.TermRelationshipDAO;
import com.runwaysdk.dataaccess.TreeDAO;
import com.runwaysdk.dataaccess.UnexpectedTypeException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFactory;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.cache.RelationshipDAOCollection;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.query.AbstractRelationshipQuery;
import com.runwaysdk.query.ColumnInfo;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OrderBy;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;
import com.runwaysdk.util.IdParser;

/**
 * Most SQL operations to the database concerning Relationships are performed
 * here.
 * 
 * @author nathan
 * 
 * @version $Revision: 1.2 $
 * @since 1.4
 */
public class RelationshipDAOFactory
{
  private static Set<String> metaDataRelationshipSet = loadFactory();

  private static Set<String> loadFactory()
  {
    HashSet<String> metaDataRelationshipSet = new HashSet<String>();

    metaDataRelationshipSet.add(RelationshipTypes.ASSIGNMENTS.getType());
    metaDataRelationshipSet.add(RelationshipTypes.BUSINESS_INHERITANCE.getType());
    metaDataRelationshipSet.add(RelationshipTypes.CLASS_ATTRIBUTE.getType());
    metaDataRelationshipSet.add(RelationshipTypes.CLASS_INHERITANCE.getType());
    metaDataRelationshipSet.add(RelationshipTypes.ENTITY_INDEX.getType());

    metaDataRelationshipSet.add(RelationshipTypes.EXCEPTION_INHERITANCE.getType());
    metaDataRelationshipSet.add(RelationshipTypes.INCLUDE_ATTRIBUTES.getType());
    metaDataRelationshipSet.add(RelationshipTypes.INFORMATION_INHERITANCE.getType());
    metaDataRelationshipSet.add(RelationshipTypes.MD_METHOD_METHOD_ACTOR.getType());
    metaDataRelationshipSet.add(RelationshipTypes.METADATA_RELATIONSHIP.getType());
    metaDataRelationshipSet.add(RelationshipTypes.PROBLEM_INHERITANCE.getType());
    metaDataRelationshipSet.add(RelationshipTypes.RELATIONSHIP_INHERITANCE.getType());
    metaDataRelationshipSet.add(RelationshipTypes.TYPE_PERMISSION.getType());
    metaDataRelationshipSet.add(RelationshipTypes.UTIL_INHERITANCE.getType());
    metaDataRelationshipSet.add(RelationshipTypes.VIEW_INHERITANCE.getType());
    metaDataRelationshipSet.add(RelationshipTypes.VIRTUALIZE_ATTRIBUTE.getType());
    metaDataRelationshipSet.add(RelationshipTypes.WARNING_INHERITANCE.getType());

    // Not a subclass of MetaDataRelationship
    metaDataRelationshipSet.add(RoleDAOIF.ROLE_INHERITANCE);

    metaDataRelationshipSet.add(RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType());
    metaDataRelationshipSet.add(RelationshipTypes.CLASS_ATTRIBUTE_VIRTUAL.getType());
    metaDataRelationshipSet.add(RelationshipTypes.CONTROLLER_ACTION.getType());
    metaDataRelationshipSet.add(RelationshipTypes.ENUMERATION_ATTRIBUTE.getType());
    metaDataRelationshipSet.add(RelationshipTypes.MD_TYPE_MD_METHOD.getType());
    metaDataRelationshipSet.add(RelationshipTypes.METADATA_PARAMETER.getType());

    return metaDataRelationshipSet;
  }

  /**
   * Returns an array of Relationship objects that represent child relationships
   * (of the given relationship type) of the BusinessDAO with the given oid. If
   * <code>relationshipType</code> is an empty String, then Relationship objects
   * of all types will be returned.
   * 
   * <br/>
   * <b>Precondition:</b> businessDAOid != null <br/>
   * <b>Precondition:</b> !businessDAOid.trim().equals("") <br/>
   * <b>Precondition:</b> mdRelationshipId != null <br/>
   * <b>Postcondition:</b> Returns a LinkedList of Relationship objects that
   * represent child relationships (of the given relationship type) of the
   * BusinessDAO with the given oid.
   * 
   * @param businessDAOid
   *          oid of the BusinessDAO
   * @param relationshipType
   *          Type of the relationship
   * @return An array of Relationship objects that represent child relationships
   *         (of the given relationship type) of the BusinessDAO with the given
   *         oid.
   */
  public static List<RelationshipDAOIF> getChildren(String businessDAOid, String relationshipType)
  {
    RelationshipDAOQuery relationshipQuery = new QueryFactory().relationshipDAOQuery(relationshipType);
    relationshipQuery.WHERE(relationshipQuery.parentOid().EQ(businessDAOid));

    MdAttributeConcreteDAOIF sortMdAttribute = MdRelationshipDAO.getMdRelationshipDAO(relationshipType).getSortMdAttribute();

    if (sortMdAttribute != null)
    {
      relationshipQuery.ORDER_BY(relationshipQuery.getPrimitive(sortMdAttribute.definesAttribute()), OrderBy.SortOrder.ASC);
    }

    return queryRelationshipObjects(relationshipQuery);
  }

  /**
   * Returns an array of Relationship objects that represent parent
   * relationships (of the given relationship type) of the BusinessDAO with the
   * given oid. If <code>reationshipNameId</code> is an empty String, then
   * Relationship objects of all types will be returned.
   * 
   * <br/>
   * <b>Precondition:</b> businessDAOid != null <br/>
   * <b>Precondition:</b> !businessDAOid.trim().equals("") <br/>
   * <b>Precondition:</b> relationshipType != null <br/>
   * <b>Postcondition:</b> Returns a LinkedList of Relationship objects that
   * represent parent relationships (of the given relationship type) of the
   * BusinessDAO with the given oid.
   * 
   * @param businessDAOid
   *          OID of the BusinessDAO
   * @param relationshipType
   *          Type of the relationship
   * @return an array of Relationship objects that represent parent
   *         relationships (of the given relationship type) with the BusinessDAO
   *         with the given oid
   */
  public static List<RelationshipDAOIF> getParents(String businessDAOid, String relationshipType)
  {
    RelationshipDAOQuery relationshipQuery = new QueryFactory().relationshipDAOQuery(relationshipType);
    relationshipQuery.WHERE(relationshipQuery.childOid().EQ(businessDAOid));

    MdAttributeConcreteDAOIF sortMdAttribute = MdRelationshipDAO.getMdRelationshipDAO(relationshipType).getSortMdAttribute();

    if (sortMdAttribute != null)
    {
      relationshipQuery.ORDER_BY(relationshipQuery.getPrimitive(sortMdAttribute.definesAttribute()), OrderBy.SortOrder.ASC);
    }

    return queryRelationshipObjects(relationshipQuery);
  }

  /**
   * Returns a List of all relationships of the given type. If a default sort
   * order has been defined for the relationship type, the relationship objects
   * will be returned in that order.
   * 
   * <br/>
   * <b>Precondition:</b> mdRelationshipIF != null
   * 
   * @param RelationshipDAOCollection
   *          puts the relationship objects into this collection.
   * @return List of all relationships of the given type.
   */
  public static List<RelationshipDAOIF> getRelationshipTypeInstances(MdRelationshipDAOIF mdRelationshipIF, RelationshipDAOCollection relationshipCollection)
  {
    RelationshipDAOQuery relationshipQuery = new QueryFactory().relationshipDAOQuery(mdRelationshipIF.definesType());

    MdAttributeConcreteDAOIF sortMdAttribute = MdRelationshipDAO.getMdRelationshipDAO(mdRelationshipIF.definesType()).getSortMdAttribute();

    if (sortMdAttribute != null)
    {
      relationshipQuery.ORDER_BY(relationshipQuery.getPrimitive(sortMdAttribute.definesAttribute()), OrderBy.SortOrder.ASC);
    }

    return queryRelationshipObjects(relationshipQuery, relationshipCollection);
  }

  /**
   * Throws an exception if adding the given child to the given parent will
   * create a recursive relationship.
   * 
   * <br/>
   * <b>Precondition:</b> parentOid != null <br/>
   * <b>Precondition:</b> !parentOid.trim().equals("") <br/>
   * <b>Precondition:</b> parentOid represents a valid BusinessDAO in the
   * database
   * 
   * <br/>
   * <b>Precondition:</b> childOid != null <br/>
   * <b>Precondition:</b> !childOid().equals("") <br/>
   * <b>Precondition:</b> childOid represents a valid BusinessDAO in the database
   * 
   * @param parentOid
   *          oid of the parent BusinessDAO
   * @param childOid
   *          oid of the child BusinessDAO
   * @param relationshipType
   *          oid of the metadata BusinessDAO that defines this relationship type
   * @throws DataAccessException
   *           if adding the given child to the given parent will create a
   *           recursive relationship.
   */
  public static void recursiveLinkCheck(String parentOid, String childOid, String relationshipType)
  {
    Stack<String> idStack = new Stack<String>();
    idStack.push(childOid);

    while (!idStack.isEmpty())
    {
      String childOidOnStack = idStack.pop();

      for (RelationshipDAOIF relationship : ObjectCache.getChildren(childOidOnStack, relationshipType))
      {
        if (relationship.getChildOid().equals(parentOid))
        {
          MdRelationshipDAOIF mdRelationship = (MdRelationshipDAOIF) MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

          String error = "Relationship [" + mdRelationship.definesType() + "] cannot be created.  The child object [" + relationship.getChildOid() + "] already has the parent [" + parentOid + "] as a child.  " + "This would cause an infinite recursive relationship.";
          throw new RelationshipRecursionException(error, mdRelationship, parentOid, childOid);
        }
        else
        {
          idStack.push(relationship.getChildOid());
        }
      }
    }
  }

  /**
   * Returns a new Relationship instance object.
   * 
   * @param parentOid
   * @param childOid
   * @param relationshipType
   * @return new Relationship instance object.
   */
  public static RelationshipDAO newInstance(String parentOid, String childOid, String relationshipType)
  {
    // get the meta data for the given class
    MdEntityDAOIF mdEntityIF = MdEntityDAO.getMdEntityDAO(relationshipType);

    if (! ( mdEntityIF instanceof MdRelationshipDAOIF ))
    {
      throw new UnexpectedTypeException("Type [" + relationshipType + "] is not a Relationship");
    }

    MdRelationshipDAOIF mdRelationship = (MdRelationshipDAOIF) mdEntityIF;

    if (mdRelationship.isAbstract())
    {
      String error = "Relationship [" + mdRelationship.definesType() + "] is abstract and cannot be instantiated";
      throw new AbstractInstantiationException(error, mdRelationship);
    }

    Hashtable<String, Attribute> attributeMap = new Hashtable<String, Attribute>();

    // get list of all classes in inheritance relationship
    List<MdRelationshipDAOIF> superMdRelationshipList = mdRelationship.getSuperClasses();

    for (MdRelationshipDAOIF superMdRelationshipIF : superMdRelationshipList)
    {
      attributeMap.putAll(EntityDAOFactory.createRecordsForEntity(superMdRelationshipIF));
    }

    RelationshipDAO newRelationshipObject = factoryMethod(parentOid, childOid, attributeMap, mdRelationship.definesType(), true);

    newRelationshipObject.setIsNew(true);
    newRelationshipObject.setAppliedToDB(false);

    newRelationshipObject.setTypeName(mdRelationship.definesType());

    // This used to be in EntityDAO.save(), but has been moved here to help with
    // distributed issues
    String newId = IdParser.buildId(ServerIDGenerator.nextID(), mdEntityIF.getRootId());
    newRelationshipObject.getAttribute(EntityInfo.OID).setValue(newId);

    return newRelationshipObject;
  }

  /**
   * Returns the relationship object with the given oid.
   * 
   * @param oid
   *          of the relationship oid.
   * @return relationship object with the given oid.
   */
  public static RelationshipDAOIF get(String oid)
  {
    RelationshipDAOQuery relationshipQuery = RelationshipDAOQuery.getRelationshipInstance(oid);
    List<RelationshipDAOIF> relationshipList = queryRelationshipObjects(relationshipQuery);

    // If the Relationship list is null, then a Relationship with the given OID
    // does not exist.
    if (relationshipList.size() == 0)
    {
      return null;
    }
    else
    {
      return relationshipList.get(0);
    }
  }

  /**
   * Return the Relationship instance of the given type with the given key.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> key != null <br/>
   * <b>Precondition:</b> !key.trim().equals("")
   * 
   * @param type
   *          Fully qualified type of a Relationship
   * @param key
   *          The key of a Relationship
   * 
   * @return RelationshipDAO of the given type with the given key
   */
  public static RelationshipDAO get(String type, String key)
  {
    RelationshipDAOIF relationshipDAO = null;
    QueryFactory qFactory = new QueryFactory();
    RelationshipDAOQuery query = qFactory.relationshipDAOQuery(type);
    query.WHERE(query.aCharacter(ComponentInfo.KEY).EQ(key));

    OIterator<RelationshipDAOIF> iterator = query.getIterator();

    if (iterator.hasNext())
    {
      relationshipDAO = iterator.next();
    }
    iterator.close();

    if (relationshipDAO == null)
    {
      MdRelationshipDAOIF mdRelationship = MdRelationshipDAO.getMdRelationshipDAO(type);
      String msg = "An item of type [" + type + "] with the key [" + key + "] does not exist.";

      throw new DataNotFoundException(msg, mdRelationship);
    }

    return relationshipDAO.getRelationshipDAO();
  }

  /**
   * Returns a List of Relationship objects that match the criteria specified
   * with the given RelationshipDAOQuery.
   * 
   * @param relationshipQuery
   *          specifies criteria.
   * @return List of Relationship objects that match the criteria specified with
   *         the given RelationshipDAOQuery.
   */
  public static List<RelationshipDAOIF> queryRelationshipObjects(RelationshipDAOQuery relationshipQuery)
  {
    return queryRelationshipObjects(relationshipQuery, null);
  }

  /**
   * Returns a List of Relationship objects that match the criteria specified
   * with the given RelationshipDAOQuery.
   * 
   * @param relationshipQuery
   *          specifies criteria.
   * @param RelationshipDAOCollection
   *          puts the relationship objects into this collection.
   * @return List of Relationship objects that match the criteria specified with
   *         the given RelationshipDAOQuery.
   */
  public static List<RelationshipDAOIF> queryRelationshipObjects(RelationshipDAOQuery relationshipQuery, RelationshipDAOCollection relationshipCollection)
  {
    List<RelationshipDAOIF> relationshipList = new LinkedList<RelationshipDAOIF>();

    String sqlStmt = relationshipQuery.getSQL();

    Map<String, ColumnInfo> columnInfoMap = relationshipQuery.getColumnInfoMap();

    ResultSet results = Database.query(sqlStmt);

    // ThreadRefactor: get rid of this map.
    // Key: OID of an MdAttribute Value: MdEntity that defines the attribute;
    Map<String, MdTableClassIF> definedByMdTableClassMap = new HashMap<String, MdTableClassIF>();
    // This is map improves performance.
    // Key: type Values: List of MdAttributeIF objects that an instance of the
    // type has.
    Map<String, List<? extends MdAttributeConcreteDAOIF>> mdEntityMdAttributeMap = new HashMap<String, List<? extends MdAttributeConcreteDAOIF>>();

    MdEntityDAOIF rootMdEntityIF = relationshipQuery.getMdEntityIF().getRootMdClassDAO();
    String typeColumnAlias = columnInfoMap.get(rootMdEntityIF.definesType() + "." + EntityDAOIF.TYPE_COLUMN).getColumnAlias();

    Statement statement = null;
    try
    {
      while (results.next())
      {
        statement = results.getStatement();

        String relationshipType = results.getString(typeColumnAlias);

        List<? extends MdAttributeConcreteDAOIF> MdAttributeIFList = mdEntityMdAttributeMap.get(relationshipType);
        if (MdAttributeIFList == null)
        {
          MdAttributeIFList = MdElementDAO.getMdElementDAO(relationshipType).getAllDefinedMdAttributes();
          mdEntityMdAttributeMap.put(relationshipType, MdAttributeIFList);
        }

        RelationshipDAO relationshipDAO = buildRelationshipFromQuery(relationshipType, relationshipQuery, columnInfoMap, definedByMdTableClassMap, MdAttributeIFList, results);
        if (relationshipCollection != null)
        {
          relationshipCollection.updateCache(relationshipDAO);
        }
        relationshipList.add(relationshipDAO);
      }
    }
    catch (SQLException sqlEx)
    {
      Database.throwDatabaseException(sqlEx);
    }
    finally
    {
      try
      {
        results.close();
        if (statement != null)
        {
          statement.close();
        }
      }
      catch (SQLException sqlEx)
      {
        Database.throwDatabaseException(sqlEx);
      }
    }

    return relationshipList;
  }

  /**
   * Builds a Relationship from a row from the given resultset.
   * 
   * @param type
   * @param relationshipQuery
   *          specifies criteria.
   * @param columnInfoMap
   *          contains information about attributes used in the query
   * @param definedByMdTableClassMap
   *          sort of a hack. It is a map where the key is the oid of an
   *          MdAttribute and the value is the MdEntity that defines the
   *          attribute. This is used to improve performance.
   * @param MdAttributeIFList
   *          contains MdAttribute objects for the attributes used in this query
   * @param resultSet
   *          ResultSet object from a query.
   * @return
   */
  public static RelationshipDAO buildRelationshipFromQuery(String type, AbstractRelationshipQuery relationshipQuery, Map<String, ColumnInfo> columnInfoMap, Map<String, MdTableClassIF> definedByMdTableClassMap, List<? extends MdAttributeConcreteDAOIF> MdAttributeIFList, ResultSet results)
  {
    // Get the attributes
    Map<String, Attribute> attributeMap = EntityDAOFactory.getAttributesFromQuery(columnInfoMap, definedByMdTableClassMap, MdAttributeIFList, results);

    String parentAttributeQualifiedName = relationshipQuery.getMdEntityIF().definesType() + "." + RelationshipInfo.PARENT_OID;
    String childAttributeQualifiedName = relationshipQuery.getMdEntityIF().definesType() + "." + RelationshipInfo.CHILD_OID;

    ColumnInfo parentOidColumnInfo = columnInfoMap.get(parentAttributeQualifiedName);
    ColumnInfo childOidColumnInfo = columnInfoMap.get(childAttributeQualifiedName);

    String parentColumnAlias = parentOidColumnInfo.getColumnAlias();
    String parentOidValue = (String) AttributeFactory.getColumnValueFromRow(results, parentColumnAlias, MdAttributeUUIDInfo.CLASS, false);

    String childColumnAlias = childOidColumnInfo.getColumnAlias();
    String childOidValue = (String) AttributeFactory.getColumnValueFromRow(results, childColumnAlias, MdAttributeUUIDInfo.CLASS, false);

    RelationshipDAO relationshipDAO = factoryMethod(parentOidValue, childOidValue, attributeMap, type, true);
    return relationshipDAO;
  }

  /**
   * Returns a list of relationship objects of the given type with the given
   * parent and child ids. Throws an exception if the relationship does not
   * exist.
   * 
   * <b>Precondition:</b>Assumes that the given relationship type is concrete.
   * 
   * @param parentOid
   * @param childOid
   * @param relationshipType
   * @return list of relationship objects of the given type with the given
   *         parent and child ids. Throws an exception if the relationship does
   *         not exist.
   */
  public static List<RelationshipDAOIF> get(String parentOid, String childOid, String relationshipType)
  {
    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

    if (mdRelationshipIF.isAbstract())
    {
      String error = "Relationship [" + mdRelationshipIF.definesType() + "] is abstract and cannot be instantiated";
      throw new AbstractInstantiationException(error, mdRelationshipIF);
    }

    RelationshipDAOQuery relationshipQuery = RelationshipDAOQuery.getRelationshipInstance(parentOid, childOid, relationshipType);

    return queryRelationshipObjects(relationshipQuery);
  }

  /**
   * This is called when the core boots up.
   * 
   * @param oid
   * @param parentOid
   * @param childOid
   * @param attributeMap
   * @param relationshipType
   * @return
   */
  public static RelationshipDAO get(String oid, String parentOid, String childOid, Map<String, Attribute> attributeMap, String relationshipType)
  {
    return factoryMethod(parentOid, childOid, attributeMap, relationshipType, false);
  }

  /**
   * Instantiates a relationship object using the given attributes.
   * 
   * @param parentOid
   * @param childOid
   * @param attributeMap
   * @param relationshipType
   * @param useCache
   *          true if the cache has been instantiated, false otherwise.
   * @return
   */
  public static RelationshipDAO factoryMethod(String parentOid, String childOid, Map<String, Attribute> attributeMap, String relationshipType, boolean useCache)
  {
    if (relationshipType.equals(RelationshipTypes.ENUMERATION_ATTRIBUTE_ITEM.getType()))
    {
      return new EnumerationAttributeItem(parentOid, childOid, attributeMap, relationshipType);
    }
    else if (relationshipType.equals(IndexAttributeInfo.CLASS))
    {
      return new IndexAttributeDAO(parentOid, childOid, attributeMap, relationshipType);
    }

    if (metaDataRelationshipSet.contains(relationshipType))
    {
      return new MetadataRelationshipDAO(parentOid, childOid, attributeMap, relationshipType);
    }

    // Metadata always uses tree relationships.
    // Consequently, only tree relationships are instantiated when the cache
    // initializes.
    else if (useCache)
    {
      MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(relationshipType);

      if (mdRelationshipIF instanceof MdTermRelationshipDAOIF)
      {
        return new TermRelationshipDAO(parentOid, childOid, attributeMap, relationshipType);
      }
      else if (mdRelationshipIF instanceof MdTreeDAOIF)
      {
        return new TreeDAO(parentOid, childOid, attributeMap, relationshipType);
      }
      else if (mdRelationshipIF instanceof MdGraphDAOIF)
      {
// Heads up: test 
//        MdRelationshipDAOIF superMdRelationshipIF = mdRelationshipIF.getSuperClass();

        return new GraphDAO(parentOid, childOid, attributeMap, relationshipType);
      }
      else
      {
        return new RelationshipDAO(parentOid, childOid, attributeMap, relationshipType);
      }
    }
    else
    {
      return new TreeDAO(parentOid, childOid, attributeMap, relationshipType);
    }
  }
}
