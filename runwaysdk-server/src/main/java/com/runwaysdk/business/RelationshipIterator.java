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
package com.runwaysdk.business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.RelationshipDAOFactory;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.query.ColumnInfo;
import com.runwaysdk.query.ComponentIterator;


public class RelationshipIterator<T> extends ComponentIterator<T>
{
  private RelationshipQuery relationshipQuery;

  protected MdEntityDAOIF  rootMdEntityIF;
  
  public RelationshipIterator(MdEntityDAOIF mdEntityIF, RelationshipQuery relationshipQuery, Map<String, ColumnInfo> columnInfoMap, ResultSet resultSet)
  {
    super(columnInfoMap, resultSet);

    this.rootMdEntityIF = mdEntityIF.getRootMdClassDAO();
    this.relationshipQuery = relationshipQuery;
  }

  /**
   * Returns the next component on the iterator, or null
   * if there are no more Components left on the iterator.
   * 
   * @return next component on the iterator.
   */
  @SuppressWarnings("unchecked")
  public T next()
  {
    Relationship relationship = null;
    
    if (this.hasNext)
    {
      try
      {     
        String typeColumnAlias = columnInfoMap.get(this.rootMdEntityIF.definesType()+"."+EntityInfo.TYPE).getColumnAlias();
        String relationshipType = this.resultSet.getString(typeColumnAlias);
        
        List<? extends MdAttributeConcreteDAOIF> mdAttributeIFList = (List<? extends MdAttributeConcreteDAOIF>)this.mdTableClassMdAttributeMap.get(relationshipType);
        if (mdAttributeIFList == null)
        {
          mdAttributeIFList = MdElementDAO.getMdElementDAO(relationshipType).getAllDefinedMdAttributes();
          this.mdTableClassMdAttributeMap.put(relationshipType, mdAttributeIFList);
        }
               
        RelationshipDAO relationshipDAO = RelationshipDAOFactory.buildRelationshipFromQuery(relationshipType, this.relationshipQuery, this.columnInfoMap, 
            this.definedByTableClassTableMap, mdAttributeIFList, this.resultSet);
        
        if(BusinessFacade.isReservedType(relationshipDAO.getType()))
        {
           relationship = new Relationship(relationshipDAO);
        }
        else
        {
          relationship = Relationship.instantiate(relationshipDAO);
        }
        
        this.hasNext = this.resultSet.next();
        if (!this.hasNext)
        {
          this.close();
        }
      }
      catch(SQLException sqlEx)
      {
        // close the connection
        this.close();
        Database.throwDatabaseException(sqlEx);
      }

      return (T)relationship;
    }
    else
    {
      throw new NoSuchElementException();
    }
  }

  /**
   * @throws UnsupportedOperationException iterators should not allow one to remove an object from the underlying collection.
   */
  public void remove()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Needed to implement the Iterable interface.
   */
  public Iterator<T> iterator()
  {
    return this;
  }
}
