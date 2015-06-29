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

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.runwaysdk.query.OIterator;

public class CashedComponentIterator<T> implements OIterator<T>
{
  private List<T> componentList;
  private int listIndex;
  
  protected CashedComponentIterator(List<T> componentList)
  {
    this.componentList = componentList;
    this.listIndex = 0;
  }
  
  /**
   * Returns the next component on the iterator, or null
   * if there are no more Components left on the iterator.
   * @return next component on the iterator.
   */
  public T next()
  {
    if (this.hasNext())
    {
      T t = this.componentList.get(this.listIndex);
      this.listIndex++;
      return t;
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
   * Returns true if there are Components remaining on this iterator, false otherwise.
   * @return true if there are Components remaining on this iterator, false otherwise.
   */
  public boolean hasNext()
  {
    if (this.listIndex < this.componentList.size())
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  /**
   * Closes the iterator.  The method <code>hasNext</code> will return false.
   */
  public void close()
  {
    this.listIndex = this.componentList.size();
  }

  /**
   * Fetches all objects in this iterator and returns them in a List.  Caution:
   * if the iterator represents a large result set, then the entire set is copied into
   * memory, which will may blow the memory stack.  Use with caution.
   * @return all objects in this iterator
   */
  public List<T> getAll()
  {
    return this.componentList;
  }
  
  /**
   * Needed to implement the Iterable interface.
   */
  public Iterator<T> iterator()
  {
    return this;
  }
}
