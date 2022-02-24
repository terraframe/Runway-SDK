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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.ValueObject;

public class ViewIterator<T> implements OIterator<T>
{
  private ValueIterator<ValueObject> valueIterator;
  private MdViewDAOIF mdViewIF;
  
  public ViewIterator(MdViewDAOIF mdViewIF, ValueIterator<ValueObject> valueIterator)
  {
    super();
    this.valueIterator = valueIterator;
    this.mdViewIF = mdViewIF;
  }

  protected ValueIterator<ValueObject> getValueIterator()
  {
    return this.valueIterator;
  }

  protected MdViewDAOIF getMdViewIF()
  {
    return this.mdViewIF;
  }
  
  /**
   * Returns the next component on the iterator, or null
   * if there are no more Components left on the iterator.
   * @return next component on the iterator.
   */
  @SuppressWarnings("unchecked")
  public T next()
  {
    if (this.getValueIterator().hasNext())
    {
      ValueObject valueObject = this.getValueIterator().next();
      return (T)BusinessFacade.convertValueObjectToView(this.getMdViewIF().definesType(), valueObject);
    }
    else
    {
      throw new NoSuchElementException();
    } 
  }

  /**
   * Returns true if there are Components remaining on this iterator, false otherwise.
   * @return true if there are Components remaining on this iterator, false otherwise.
   */
  public boolean hasNext()
  {
    return this.getValueIterator().hasNext();
  }

  /**
   * Does nothing, as iterators should not allow one to remove an object from the underlying collection.
   */
  public void remove() {}

  /**
   * Closes JDBC resources used by the iterator.  This method must
   * be called if the iterator has not iterated over the complete set
   * of results, otherwise, valuable JDBC resources will not be closed 
   * and reused.
   */
  public void close()
  {
    this.getValueIterator().close();
  }

  /**
   * Needed to implement the Iterable interface.
   */
  public Iterator<T> iterator()
  {
    return this;
  }

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
    this.getValueIterator().finalize();
  }
}
