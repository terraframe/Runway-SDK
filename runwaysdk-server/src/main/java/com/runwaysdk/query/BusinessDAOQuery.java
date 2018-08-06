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

import java.sql.ResultSet;
import java.util.Map;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.util.IdParser;


public class BusinessDAOQuery extends AbstractObjectQuery
{
  protected BusinessDAOQuery(QueryFactory queryFactory, String type)
  {
    super(queryFactory, type);
  }

  /**
   *
   * @param valueQuery
   * @param type
   */
  protected BusinessDAOQuery(ValueQuery valueQuery, String type)
  {
    super(valueQuery, type);
  }

  /**
   * Creates a query object with the criteria set to fetch an object with the given oid.
   * @param oid ID of the BusinessDAO
   * @return query object with the criteria set to fetch an object with the given oid.
   */
  public static BusinessDAOQuery getObjectInstance(String oid)
  {
    MdClassDAOIF mdClassIF = MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(oid));
    BusinessDAOQuery businessDAOquery = new QueryFactory().businessDAOQuery(mdClassIF.definesType());
    businessDAOquery.WHERE(businessDAOquery.aCharacter(EntityInfo.ID).EQ(oid));
    businessDAOquery.instanceQuery = true;

    return businessDAOquery;
  }

  /**
   * Returns an iterator of BusinessDAOs that match the query criteria specified
   * on this query object.
   * @return iterator of BusinessDAOs that match the query criteria specified
   * on this query object.
   */
  public OIterator<BusinessDAOIF> getIterator()
  {
    this.checkNotUsedInValueQuery();

    String sqlStmt = this.getSQL();
    Map<String, ColumnInfo> columnInfoMap = this.getColumnInfoMap();

    ResultSet results = Database.query(sqlStmt);
    BusinessDAOIterator<BusinessDAOIF> businessDAOiterator = new BusinessDAOIterator<BusinessDAOIF>(this.getMdEntityIF(), columnInfoMap, results);

    return businessDAOiterator;
  }

  /**
   * Returns an iterator of BusinessDAOs that match the query criteria specified
   * on this query object.
   * @param pageSize   number of results per page
   * @param pageNumber page number
   * @return iterator of BusinessDAOs that match the query criteria specified
   * on this query object.
   */
  public OIterator<BusinessDAOIF> getIterator(int pageSize, int pageNumber)
  {
    this.checkNotUsedInValueQuery();

    int limit = ComponentQuery.rowLimit(pageSize);
    int skip = ComponentQuery.rowSkip(pageSize, pageNumber);

    String sqlStmt = this.getSQL(limit, skip);
    Map<String, ColumnInfo> columnInfoMap = this.getColumnInfoMap();
    ResultSet results = Database.query(sqlStmt);
    BusinessDAOIterator<BusinessDAOIF> businessDAOiterator = new BusinessDAOIterator<BusinessDAOIF>(this.getMdEntityIF(), columnInfoMap, results);

    return businessDAOiterator;
  }
}
