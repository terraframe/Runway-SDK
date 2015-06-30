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
package com.runwaysdk.session;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Used to iterate over sessions in a session cache.
 * 
 * @author Richard Rowlands
 */
public interface SessionIterator
{
  /**
   * Returns the next Session on the iterator. If there are no more Sessions a
   * {@link NoSuchElementException} is thrown.
   * 
   * @return next Session on the iterator.
   */
  public SessionIF next();
  
  /**
   * Removes the Session from the cache.
   */
  public void remove();
  
  /**
   * @return true if there are Sessions remaining on this iterator, false otherwise.
   */
  public boolean hasNext();
  
  /**
   * Closes any resources that may or may not be used by the iterator.  This method must
   * be called if the iterator has not iterated over the complete set
   * of results, otherwise, valuable resources may not be closed and reused.
   */
  public void close();

  /**
   * Fetches all objects in this iterator and returns them in a List.  Caution:
   * if the iterator represents a large result set, then the entire set is copied into
   * memory, which will may blow the memory stack.  Use with caution.
   * @return all objects in this iterator
   */
  public Collection<SessionIF> getAll();
}
