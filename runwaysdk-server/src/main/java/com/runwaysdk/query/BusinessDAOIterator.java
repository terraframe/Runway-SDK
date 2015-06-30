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
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;


@SuppressWarnings("unchecked")
public class BusinessDAOIterator<T> extends ComponentIterator<T>
{ 
  protected MdEntityDAOIF  rootMdEntityIF;
  
  /**
   * Initializes the iterator.
   * @param columnInfoMap
   * @param resultSet
   */
  @SuppressWarnings("unchecked")
  protected BusinessDAOIterator(MdEntityDAOIF mdEntityIF, Map<String, ColumnInfo> columnInfoMap, ResultSet resultSet)
  {
    super(columnInfoMap, resultSet);

    this.rootMdEntityIF = mdEntityIF.getRootMdClassDAO();
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
    BusinessDAOIF businessDAO = null;
    
    if (this.hasNext)
    {
      try
      {
        String typeColumnAlias = ((ColumnInfo)this.columnInfoMap.get(this.rootMdEntityIF.definesType()+"."+EntityInfo.TYPE)).getColumnAlias();
        String businessDAOType = this.resultSet.getString(typeColumnAlias);
        
        List<? extends MdAttributeConcreteDAOIF> mdAttributeIFList = (List<? extends MdAttributeConcreteDAOIF>)this.mdEntityMdAttributeMap.get(businessDAOType);
        if (mdAttributeIFList == null)
        {
          mdAttributeIFList = MdElementDAO.getMdElementDAO(businessDAOType).getAllDefinedMdAttributes();
          this.mdEntityMdAttributeMap.put(businessDAOType, mdAttributeIFList);
        }
        
        businessDAO = BusinessDAOFactory.buildObjectFromQuery(businessDAOType, this.columnInfoMap, this.definedByMdEntityMap, mdAttributeIFList, this.resultSet);
        
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

      return (T)businessDAO;
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
