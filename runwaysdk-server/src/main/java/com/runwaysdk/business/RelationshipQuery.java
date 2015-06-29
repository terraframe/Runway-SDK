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
package com.runwaysdk.business;

import java.sql.ResultSet;
import java.util.Map;

import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.AbstractRelationshipQuery;
import com.runwaysdk.query.ColumnInfo;
import com.runwaysdk.query.ComponentQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;


public class RelationshipQuery extends AbstractRelationshipQuery
{

  public RelationshipQuery(QueryFactory queryFactory, String type)
  {
    super(queryFactory, type);
  }

  /**
   *
   * @param valueQuery
   * @param type
   */
  public RelationshipQuery(ValueQuery valueQuery, String type)
  {
    super(valueQuery, type);
  }

  /**
   * Returns an iterator of Relationships that match the query criteria specified
   * on this query object.
   * @param pageSize   number of results per page
   * @param pageNumber page number
   * @return iterator of Relationships that match the query criteria specified
   * on this query object.
   */
  public OIterator<Relationship> getIterator()
  {
    this.checkNotUsedInValueQuery();

    String sqlStmt = this.getSQL();
    Map<String, ColumnInfo> columnInfoMap = this.columnInfoMap;

    ResultSet results = Database.query(sqlStmt);
    return new RelationshipIterator<Relationship>(this.mdEntityIF, this, columnInfoMap, results);
  }

  /**
   * Returns an iterator of Relationships that match the query criteria specified
   * on this query object.
   * @return iterator of Relationships that match the query criteria specified
   * on this query object.
   */
  public OIterator<Relationship> getIterator(int pageSize, int pageNumber)
  {
    this.checkNotUsedInValueQuery();

    int limit = ComponentQuery.rowLimit(pageSize);
    int skip = ComponentQuery.rowSkip(pageSize, pageNumber);

    String sqlStmt = this.getSQL(limit, skip);
    Map<String, ColumnInfo> columnInfoMap = this.columnInfoMap;

    ResultSet results = Database.query(sqlStmt);
    return new RelationshipIterator<Relationship>(this.mdEntityIF, this, columnInfoMap, results);
  }
}
