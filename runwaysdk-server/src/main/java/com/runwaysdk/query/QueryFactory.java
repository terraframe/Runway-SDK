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
package com.runwaysdk.query;

import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.RelationshipQuery;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.StructQuery;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.system.metadata.MdTableQuery;

public class QueryFactory
{
  private   int                          aliasCounter;

  /**
   * Key: attribute name+"-"+reference table, Value: reference table alias
   */
  protected Map<String, String>          tableAliasMap;

  /**
   * Key: namespace plus column name, Value: column alias.
   */
  protected Map<String, String>          columnAliasMap;

  public QueryFactory()
  {
    super();
    this.aliasCounter          = 0;
    this.tableAliasMap         = new HashMap<String, String>();

    this.columnAliasMap        = new HashMap<String, String>();
  }
  
  /**
   * Returns a {@link TableQuery} object for querying BusinessDAOs.
   * @param type type of the table.
   * @return  {@link TableQuery}  object for querying data objects.
   */
  public TableQuery tableQuery(String type)
  {
    return new TableQuery(this, type);
  }

  /**
   * Returns a {@link BusinessDAOQuery} object for querying BusinessDAOs.
   * @param type type of the {@link BusinessDAO}.
   * @return  {@link BusinessDAOQuery}  object for querying data objects.
   */
  public BusinessDAOQuery businessDAOQuery(String type)
  {
    return new BusinessDAOQuery(this, type);
  }

  /**
   * Returns a StructDAOQuery object for querying StructDAOs.
   * @param type type of the StructDAOs.
   * @return StructDAOQuery object for querying struct objects.
   */
  public StructDAOQuery structDAOQuery(String type)
  {
    return new StructDAOQuery(this, type);
  }

  /**
   * Returns a RelationshipDAOQuery object for querying Relationships.
   * @param type type of the Relationship.
   * @return RelationshipDAOQuery object for querying Relationships.
   */
  public RelationshipDAOQuery relationshipDAOQuery(String type)
  {
    return new RelationshipDAOQuery(this, type);
  }

  /**
   * Returns a BusinessQuery object for querying Businesses.
   * @param type type of the Business.
   * @return BusinessQuery object for querying Businesses.
   */
  public BusinessQuery businessQuery(String type)
  {
    return new BusinessQuery(this, type);
  }

  /**
   * Returns a ValueQuery object for querying ValueQuery.
   *
   * @return ValueQuery object for querying ValueQuery.
   */
  public ValueQuery valueQuery()
  {
    return new ValueQuery(this);
  }

  /**
   * Returns a StructQuery object for querying Structs.
   * @param type type of the Struct.
   * @return StructQuery object for querying Structs.
   */
  public StructQuery structQuery(String type)
  {
    return new StructQuery(this, type);
  }

  /**
   * Returns a RelationshipQuery object for querying Relationships.
   * @param type type of the Relationship.
   * @return RelationshipQuery object for querying Relationships.
   */
  public RelationshipQuery relationshipQuery(String type)
  {
    return new RelationshipQuery(this, type);
  }

  /**
   * Returns a EntityQuery object that queries for objects of the same type (both Java and runway type).
   * @param componentIF
   * @return EntityQuery object that queries for objects of the same type (both Java and runway type).
   */
  public EntityQuery entityQuery(ComponentIF componentIF)
  {
    if (componentIF instanceof BusinessDAO)
    {
      return businessDAOQuery(componentIF.getType());
    }
    else if (componentIF instanceof StructDAO)
    {
      return structDAOQuery(componentIF.getType());
    }
    else if (componentIF instanceof RelationshipDAO)
    {
      return relationshipDAOQuery(componentIF.getType());
    }
    else if (componentIF instanceof Business)
    {
      return businessQuery(componentIF.getType());
    }
    else if (componentIF instanceof Struct)
    {
      return structQuery(componentIF.getType());
    }
    else if (componentIF instanceof Relationship)
    {
      return relationshipQuery(componentIF.getType());
    }
    else
    {
      String error = "[" + componentIF.getClass().getName() + "] is an invalid type for query";
      throw new QueryException(error);
    }
  }

  /**
   * Returns a <code>EntityQueryDAO</code> object that queries for data access objects defined by the given <code>MdEntityDAOIF</code>.
   * @param mdEntityIF
   * @return <code>EntityQueryDAO</code> object that queries for data access objects defined by the given <code>MdEntityDAOIF</code>.
   */
  public EntityQuery entityQueryDAO(MdEntityDAOIF mdEntityIF)
  {
    if (mdEntityIF instanceof MdBusinessDAOIF)
    {
      return businessDAOQuery(mdEntityIF.definesType());
    }
    else if (mdEntityIF instanceof MdStructDAOIF)
    {
      return structDAOQuery(mdEntityIF.definesType());
    }
    else if (mdEntityIF instanceof MdRelationshipDAOIF)
    {
      return relationshipDAOQuery(mdEntityIF.definesType());
    }
    else
    {
      String error = "[" + mdEntityIF.definesType() + "] is an invalid type for query";
      throw new QueryException(error);
    }
  }

  /**
   * Returns a <code>EntityQuery</code> object that queries for business objects defined by the given <code>MdEntityDAOIF</code>.
   * @param mdEntityIF
   * @return <code>EntityQuery</code> object that queries for business objects defined by the given <code>MdEntityDAOIF</code>.
   */
  public EntityQuery entityQuery(MdEntityDAOIF mdEntityIF)
  {
    if (mdEntityIF instanceof MdBusinessDAOIF)
    {
      return businessQuery(mdEntityIF.definesType());
    }
    else if (mdEntityIF instanceof MdStructDAOIF)
    {
      return structQuery(mdEntityIF.definesType());
    }
    else if (mdEntityIF instanceof MdRelationshipDAOIF)
    {
      return relationshipQuery(mdEntityIF.definesType());
    }
    else
    {
      String error = "[" + mdEntityIF.definesType() + "] is an invalid type for query";
      throw new QueryException(error);
    }
  }

  /**
   * Returns a ComponentQuery object that queries for business objects defined by the given MdEntityIF.
   * @param mdEntityIF
   * @return ComponentQuery object that queries for business objects defined by the given MdEntityIF.
   */
  public ComponentQuery componentQueryEntity(MdEntityDAOIF mdEntityIF)
  {
    if (mdEntityIF instanceof MdBusinessDAOIF)
    {
      return businessQuery(mdEntityIF.definesType());
    }
    else if (mdEntityIF instanceof MdStructDAOIF)
    {
      return structQuery(mdEntityIF.definesType());
    }
    else if (mdEntityIF instanceof MdRelationshipDAOIF)
    {
      return relationshipQuery(mdEntityIF.definesType());
    }
    else
    {
      String error = "[" + mdEntityIF.definesType() + "] is an invalid type for query";
      throw new QueryException(error);
    }
  }


  /**
   * Returns the alias used in the query for the given table.
   * @param namespace used to determine where the table is used in the query.  If
   * the table is defined by the type queried by this ComponentQuery object, then
   * the namespace is an empty String.  If the table is used by a reference,
   * field, struct, or
   * enumeration, then the namespace involves the name of the attribute.
   * @param tableName
   * @return alias used in the query for the given table.
   */
  public String getTableAlias(String namespace, String tableName)
  {
    String qualifiedString = namespace+"-"+tableName;

    String tableAlias = this.tableAliasMap.get(qualifiedString);
    if ( tableAlias == null)
    {
      tableAlias = this.getUniqueAlias(tableName);
      this.tableAliasMap.put(qualifiedString, tableAlias);
    }
    return tableAlias;
  }


  /**
   * Returns the alias used in the query for the given column.
   * @param namespace used to determine where the column is used in the query.
   * The namespace is normally the type that defines the attribute.
   * @param attributeName.
   * @return alias used in the query for the given table.
   */
  public String getColumnAlias(String namespace, String columnName)
  {
    String qualifiedString = namespace+"-"+columnName;

    String columnNameAlias = this.columnAliasMap.get(qualifiedString);
    if ( columnNameAlias == null)
    {
      columnNameAlias = this.getUniqueAlias(columnName);
      this.columnAliasMap.put(qualifiedString, columnNameAlias);
    }
    return columnNameAlias;
  }

  /**
   * Returns a database friendly alias derived from the given string.
   * @param aliasString string from which to derive a database alias.
   * @return database friendly alias derived from the given string.
   */
  protected String getUniqueAlias(String aliasString)
  {
    String _aliasString = aliasString;

    if (_aliasString.length() > Database.MAX_ATTRIBUTE_NAME_SIZE - 5)
    {
      _aliasString = _aliasString.substring(0, Database.MAX_ATTRIBUTE_NAME_SIZE - 5);
    }
    return _aliasString+"_"+incrementAlias();
  }

  /**
   * Increments the alias counter.  Counter resets to 0 after 1000.
   * @return incremented alias counter.
   */
  protected int incrementAlias()
  {
    this.aliasCounter += 1;
    if (this.aliasCounter % 10000 == 0)
    {
      this.aliasCounter = 1;
    }
    return this.aliasCounter;
  }
}
