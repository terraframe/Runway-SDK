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
package com.runwaysdk.query;

import java.sql.ResultSet;
import java.util.Map;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.util.IdParser;


public class RelationshipDAOQuery extends AbstractRelationshipQuery
{

  protected RelationshipDAOQuery(QueryFactory queryFactory, String type)
  {
    super(queryFactory, type);
  }

  /**
   *
   * @param valueQuery
   * @param type
   */
  protected RelationshipDAOQuery(ValueQuery valueQuery, String type)
  {
    super(valueQuery, type);
  }

  /**
   * Creates a query object with the criteria set to fetch an object with the given id.
   * @param id ID of the Relationship
   * @return query object with the criteria set to fetch an object with the given id.
   */
  public static RelationshipDAOQuery getRelationshipInstance(String id)
  {
    MdClassDAOIF mdClassIF = MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(id));
    RelationshipDAOQuery relationshipQuery = new QueryFactory().relationshipDAOQuery(mdClassIF.definesType());
    relationshipQuery.WHERE(relationshipQuery.aCharacter(EntityInfo.ID).EQ(id));
    relationshipQuery.instanceQuery = true;

    return relationshipQuery;
  }


  /**
   * Creates a query object with the criteria set to fetch an object with the given id.
   * @param parentId ID of the object in the Relationship
   * @param childId ID of the object in the Relationship
   * @param relationshipType Type of the Relationship
   * @return query object with the criteria set to fetch an object with the given id.
   */
  public static RelationshipDAOQuery getRelationshipInstance(String parentId, String childId, String relationshipType)
  {
    RelationshipDAOQuery relationshipQuery = new QueryFactory().relationshipDAOQuery(relationshipType);

    relationshipQuery.
      WHERE(relationshipQuery.parentId().EQ(parentId).
      AND(relationshipQuery.childId().EQ(childId))
    );

    relationshipQuery.instanceQuery = true;

    return relationshipQuery;
  }

  /**
   * Returns an iterator of Relationships that match the query criteria specified
   * on this query object.
   * @return iterator of Relationships that match the query criteria specified
   * on this query object.
   */
  public OIterator<RelationshipDAOIF> getIterator()
  {
    this.checkNotUsedInValueQuery();

    String sqlStmt = this.getSQL();
    Map<String, ColumnInfo> columnInfoMap = this.columnInfoMap;

    ResultSet results = Database.query(sqlStmt);
    return new RelationshipDAOIterator<RelationshipDAOIF>(this.mdEntityIF, this, columnInfoMap, results);
  }


  /**
   * Returns an iterator of Relationships that match the query criteria specified
   * on this query object.
   * @param pageSize   number of results per page
   * @param pageNumber page number
   * @return iterator of Relationships that match the query criteria specified
   * on this query object.
   */
  public OIterator<RelationshipDAOIF> getIterator(int pageSize, int pageNumber)
  {
    this.checkNotUsedInValueQuery();

    String sqlStmt = this.getSQL();
    Map<String, ColumnInfo> columnInfoMap = this.columnInfoMap;

    ResultSet results = Database.query(sqlStmt);
    return new RelationshipDAOIterator<RelationshipDAOIF>(this.mdEntityIF, this, columnInfoMap, results);
  }
}
