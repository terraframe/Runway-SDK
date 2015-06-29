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
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.dataaccess.UnexpectedTypeException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.CacheAllStructDAOStrategy;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.query.ColumnInfo;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.StructDAOQuery;
import com.runwaysdk.util.IdParser;

public class StructDAOFactory
{

  /**
   *
   * @param attributeMap
   * @param type
   * @return
   */
  public static StructDAO factoryMethod(Map<String, Attribute> attributeMap, String type)
  {
    return new StructDAO(attributeMap, type);
  }


  /**
   *
   * @param id
   * @return
   */
  public static StructDAOIF get(String id)
  {
    StructDAOQuery structDAOQuery = StructDAOQuery.getObjectInstance(id);
    List<StructDAO> structDAOList = queryStructDAOs(structDAOQuery);

    // If the BusinessDAO list is null, then a BusinessDAO with the given ID does not exist.
    if (structDAOList.size() == 0)
    {
      return null;
    }
    else
    {
      return structDAOList.get(0);
    }
  }

  /**
   * Returns a StructDAO of the given type with the given key in the database. This
   * method does the same thing as get(String id), but is faster. If you
   * know the type of the id, use this method. Otherwise use the
   * get(String id) method.
   *
   * <br/><b>Precondition:</b> key != null
   * <br/><b>Precondition:</b> !key.trim().equals("")
   * <br/><b>Precondition:</b> type != null
   * <br/><b>Precondition:</b> !type.trim().equals("")
   * <br/><b>Postcondition:</b> BusinessDAO representing
   * the item in the database of the given key and type is returned
   *
   * @param type fully qualified type of an item in the database
   * @param key key of an item in the database
   *
   * @return StructDAO instance of the given type and key
   */
  public static StructDAO get(String type, String key)
  {
    StructDAOIF structDAO = null;
    QueryFactory qFactory = new QueryFactory();
    StructDAOQuery query = qFactory.structDAOQuery(type);
    query.WHERE(query.aCharacter(ComponentInfo.KEY).EQ(key));

    OIterator<StructDAOIF> iterator = query.getIterator();

    if (iterator.hasNext())
    {
      structDAO = iterator.next();
    }
    iterator.close();

    if(structDAO == null)
    {
      MdStructDAOIF mdStruct = MdStructDAO.getMdStructDAO(type);
      String msg = "An item of type [" + type + "] with the key [" + key + "] does not exist.";

      throw new DataNotFoundException(msg, mdStruct);
    }

    return structDAO.getStructDAO();
  }

  /**
   * Returns a List of StructDAO objects that match the criteria specified with the given StructDAOQuery.
   * @param objectQuery specifies criteria.
   * @return List of StructDAO objects that match the criteria specified with the given StructDAOQuery.
   */
  public static List<StructDAO> queryStructDAOs(StructDAOQuery structDAOQuery)
  {
    return queryStructDAOs(structDAOQuery, null);
  }

  /**
   * Returns a List of StructDAO objects that match the criteria specified with the given StructDAOQuery.
   * @param objectQuery specifies criteria.
   * @param cacheStrategy cacheStrategy that is updated for each result.
   * @return List of StructDAO objects that match the criteria specified with the given StructDAOQuery.
   */
  public static List<StructDAO> queryStructDAOs(StructDAOQuery structDAOQuery, CacheAllStructDAOStrategy cacheStrategy)
  {
    List<StructDAO> structDAOList = new LinkedList<StructDAO>();

    String sqlStmt = structDAOQuery.getSQL();
    Map<String, ColumnInfo> columnInfoMap = structDAOQuery.getColumnInfoMap();
    ResultSet results = Database.query(sqlStmt);

    // ThreadRefactor: get rid of this map.
    // Key: ID of an MdAttribute  Value: MdEntity that defines the attribute;
    Map<String, MdEntityDAOIF> definedByMdEntityMap = new HashMap<String, MdEntityDAOIF>();
    // This is map improves performance.
    // Key: type Values: List of MdAttributeIF objects that an instance of the type has.
    Map<String, List<? extends MdAttributeConcreteDAOIF>> mdEntityMdAttributeMap = new HashMap<String, List<? extends MdAttributeConcreteDAOIF>>();

    String structDAOType = structDAOQuery.getMdEntityIF().definesType();

    Statement statement = null;
    try
    {
      statement = results.getStatement();
      while(results.next())
      {
        List<? extends MdAttributeConcreteDAOIF> MdAttributeIFList = mdEntityMdAttributeMap.get(structDAOType);
        if (MdAttributeIFList == null)
        {
          MdAttributeIFList = MdStructDAO.getMdStructDAO(structDAOType).getAllDefinedMdAttributes();
          mdEntityMdAttributeMap.put(structDAOType, MdAttributeIFList);
        }

        StructDAO structDAO = buildObjectFromQuery(structDAOType, columnInfoMap, definedByMdEntityMap, MdAttributeIFList, results);

        if (cacheStrategy != null)
        {
          cacheStrategy.updateCache(structDAO);
        }
        structDAOList.add(structDAO);
      }
    }
    catch(SQLException sqlEx)
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
      catch(SQLException sqlEx)
      {
        Database.throwDatabaseException(sqlEx);
      }
    }

    return structDAOList;
  }

  /**
   * Builds a StructDAO from a row from the given resultset.
   * @param type
   * @param columnInfoMap         contains information about attributes used in the query
   * @param definedByMdEntityMap  sort of a hack.  It is a map where the key is the id of an MdAttribute and the value is the
   *                              MdEntity that defines the attribute.  This is used to improve performance.
   * @param MdAttributeIFList     contains MdAttribute objects for the attributes used in this query
   * @param resultSet             ResultSet object from a query.
   * @return StructDAO from a row from the given resultset
   */

  public static StructDAO buildObjectFromQuery(String type, Map<String, ColumnInfo> columnInfoMap, Map<String, MdEntityDAOIF> definedByMdEntityMap,
      List<? extends MdAttributeConcreteDAOIF> MdAttributeIFList, ResultSet results)
  {
    // Get the attributes
    Map<String, Attribute> attributeMap = EntityDAOFactory.getAttributesFromQuery(columnInfoMap, definedByMdEntityMap, MdAttributeIFList, results);
    StructDAO structDAO = factoryMethod(attributeMap, type);
    return structDAO;
  }


  /**
   * Returns a List of all structDAOs of the given CONCRETE type.
   *
   * <br/><b>Precondition:</b>  structType is a concrete class
   *
   * @param structType type of the struct.
   * @param cacheStrategy cacheStrategy that is updated for each result.
   */
  public static void getStructDAOTypeInstances(String structType, CacheAllStructDAOStrategy cacheStrategy)
  {
    StructDAOQuery structDAOQuery = new QueryFactory().structDAOQuery(structType);
    queryStructDAOs(structDAOQuery, cacheStrategy);
  }

  /**
   * Returns a new StructDAO instance of the given struct type. Default values are
   * assigned attributes if specified by the metadata.
   *
   * <br/><b>Precondition:</b> structType != null
   * <br/><b>Precondition:</b> !structType.trim().equals("")
   *
   * @param structType Class name of the new StructDAO to instantiate
   * @return new StructDAO of the given StructDAO
   * @throws DataAccessException
   *           if metadata is not defined for the given struct type.
   */
  public static StructDAO newInstance(String structType)
  {
    // get the meta data for the given class
    MdEntityDAOIF mdEntityIF = MdEntityDAO.getMdEntityDAO(structType);

    if (!(mdEntityIF instanceof MdStructDAOIF))
    {
      throw new UnexpectedTypeException("Type [" + structType + "] is not a StructDAO");
    }

    MdStructDAOIF mdStructIF = (MdStructDAOIF)mdEntityIF;

    Hashtable<String, Attribute> attributeMap = new Hashtable<String, Attribute>();

    attributeMap.putAll(EntityDAOFactory.createRecordsForEntity(mdStructIF));

    // Create the structDAO
    StructDAO newStructDAO = factoryMethod(attributeMap, mdStructIF.definesType());

    newStructDAO.setIsNew(true);
    newStructDAO.setAppliedToDB(false);

    newStructDAO.setTypeName(mdStructIF.definesType());

    // This used to be in EntityDAO.save(), but has been moved here to help with distributed issues
    String newId = IdParser.buildId(ServerIDGenerator.nextID(),mdEntityIF.getId());
    newStructDAO.getAttribute(EntityInfo.ID).setValue(newId);

    return newStructDAO;
  }

}
