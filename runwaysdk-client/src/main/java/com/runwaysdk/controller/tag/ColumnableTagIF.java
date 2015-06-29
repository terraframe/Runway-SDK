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
package com.runwaysdk.controller.tag;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.controller.table.ColumnableIF;
import com.runwaysdk.transport.attributes.AttributeDTO;



/**
 * Interface indicating that the object is able to add Columns to it
 * and is able to iterate over a collection of MutableDTOs
 * 
 * @author jsmethie
 */
public interface ColumnableTagIF
{
  /**
   * @param name
   * @return The query AttributeDTO corresponding to the attributeName
   */
  public AttributeDTO getAttributeDTO(String name);

  /**
   * @return The generated Columnar
   */
  public ColumnableIF getColumnar();

  /**
   * Resets the current MutableDTO to the first MutableDTO in the collection.
   * Additionally it updates the scope variables (The 'value' variable
   * and the var variable) to the appropriate objects as determined by the first
   * MutableDTO.
   * 
   * @param attributeName
   */
  public void reset(String attributeName);
  
  /**
   * Increments the current MutableDTO to the next MutableDTO in the collection.
   * Additionally it updates the scope variables (The 'value' variable
   * and the var variable) to the appropriate objects as determined by the next
   * MutableDTO.
   * 
   * @param attributeName
   */
  public void next(String attributeName);
  
  /** 
   * @return The current MutableDTO
   */
  public MutableDTO getCurrent();
  
  /**
   * @param attributeName
   * 
   * @return Value of the given attribute on the current MutableDTO
   */
  public Object getValue(String attributeName);
  
  /**
   * @return Flag indicating if the collection contains more MutableDTOs
   */
  public boolean hasNext();
}
