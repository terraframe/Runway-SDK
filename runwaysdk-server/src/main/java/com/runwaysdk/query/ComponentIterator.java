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
package com.runwaysdk.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdTableClassIF;
import com.runwaysdk.dataaccess.database.Database;

public abstract class ComponentIterator<T> implements OIterator<T> 
{
  protected Map<String, ColumnInfo> columnInfoMap;
  protected ResultSet resultSet;
  protected boolean   hasNext;
  
  // ThreadRefactor: get rid of this map.
  // Key: OID of an {@link MdAttriuteDAOIF}  Value: MdEntity that defines the attribute;
  protected Map<String, MdTableClassIF> definedByTableClassTableMap;
  // This is map improves performance.  
  // Key: type Values: List of MdAttributeIF objects that an instance of the type has.
  protected Map<String, List<? extends MdAttributeConcreteDAOIF>> mdTableClassMdAttributeMap;

  /**
   * Initializes the iterator.
   * @param columnInfoMap
   * @param resultSet
   */
  protected ComponentIterator(Map<String, ColumnInfo> columnInfoMap, ResultSet resultSet)
  {
    super();

    this.columnInfoMap = columnInfoMap;
    this.resultSet     = resultSet;
    
    this.definedByTableClassTableMap = new HashMap<String, MdTableClassIF>();
    this.mdTableClassMdAttributeMap = new HashMap<String, List<? extends MdAttributeConcreteDAOIF>>();
    
    try
    {
      this.hasNext = this.resultSet.next();
    }
    catch(SQLException sqlEx)
    {
      Database.throwDatabaseException(sqlEx);
    }
    
    if (!this.hasNext)
    {
      this.close();
    }
  }

  /**
   * Returns true if there are Components remaining on this iterator, false otherwise.
   * @return true if there are Components remaining on this iterator, false otherwise.
   */
  public boolean hasNext()
  {
    return this.hasNext;
  }

  /**
   * Closes JDBC resources used by the iterator.  This method must
   * be called if the iterator has not iterated over the complete set
   * of results, otherwise, valuable JDBC resources will not be closed 
   * and reused.
   */
  public void close()
  {
    if ( this.resultSet == null)
    {
      return;
    }

    try
    {
      java.sql.Statement statement = this.resultSet.getStatement();
      this.resultSet.close();
      statement.close();
      this.hasNext = false;
      this.resultSet = null;
    }
    catch(SQLException sqlEx)
    {
      Database.throwDatabaseException(sqlEx);
    }
  }
  
  /**
   * Needed to implement the Iterable interface.
   */
  public abstract Iterator<T> iterator();
  
  /**
   * Fetches all objects in this iterator and returns them in a List.  Caution:
   * if the iterator represents a large result set, then the entire set is copied into
   * memory, which will may blow the memory stack.  Use with caution.
   * @return all objects in this iterator
   */
  public List<T> getAll()
  {
    List<T> returnList = new LinkedList<T>();

    for (T t : this)
    {
      returnList.add(t);
    }

    return returnList;
  }
  
  /**
   * Close database resources.
   */
  protected void finalize() throws Throwable 
  {
    if (resultSet != null)
    {
      this.close();
    }
    super.finalize();
  }
}
