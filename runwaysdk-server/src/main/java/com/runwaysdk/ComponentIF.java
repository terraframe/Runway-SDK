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
/*
 * Created on May 14, 2005
 *
 */
package com.runwaysdk;

import java.util.List;

import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;


/**
 *Root interface for all objects in the dataaccess layer that represent
 * units of data with a collection of attributes, where each attribute
 * is a name->value pair.
 * 
 *  @author nathan
 *
 * @version $Revision: 1.22 $
 * @since 1.4
 */
public interface ComponentIF
{  
  /**
   * Indicates if this is a new instance. If it is new, then the records that
   * represent this Component have not been created.
   * 
   * An object is new from the time it is constructed until it is applied. 
   */
  public boolean    isNew();
  
  /**
   * Returns the ID of the component.
   * 
   * <br/><b>Precondition:</b>   true
   * <br/><b>Postcondition:</b>  The state of the Component does not change
   * <br/><b>Postcondition:</b>  return value != null
   * 
   * @return  The ID of the component
   */
  public String     getId();
  
  /**
   * Returns the name of the class of this component.
   * 
   * <br/><b>Precondition:</b>   true
   * <br/><b>Postcondition:</b>  The state of the Component does not change
   * <br/><b>Postcondition:</b>  return value != null
   * 
   * @return name of the class of this component.nent
   */
  public String getType();
    
  /**
   * Returns the Key of the component.
   * 
   * <br/><b>Precondition:</b>   true
   * <br/><b>Postcondition:</b>  The state of the Component does not change
   * <br/><b>Postcondition:</b>  return value != null
   * 
   * @return  The Key of the component
   */
  public String getKey();  
  
  
  /**
   *Returns the value of the given attribute of this Component.
   *
   * <br/><b>Precondition:</b>   name != null
   * <br/><b>Precondition:</b>   !name.trim().equals("")
   * <br/><b>Precondition:</b>   An attribute of the given name exists for instances of this Component:
   * <br/><b>Postcondition:</b>  Returns the value of the given attribute
   *        value != null
   *
   * @param   name  name of the attribute
   * @return  the value of the given attribute
   * @throws  DataAccessException if no attribute with the given name exists for
   *          instances of this Component
   */  
  public String getValue(String name);
  
  /**
   * Some attributes store objects instead of strings.
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue(String name);
  
  /**
   *Returns a LinkedList of MdAttributeIFs that define each attribute
   * defined for this object's class.
   * 
   * <br/><b>Precondition:</b>   true
   *
   * @return LinkedList of MdAttributeIFs that define each attribute
   * defined for this object's class.
   */  
  public List<? extends MdAttributeDAOIF> getMdAttributeDAOs();
  
  /**
   * Returns a MdAttributeIF that defines the attribute with
   *  the given name.
   * 
   * <br/><b>Precondition:</b>   name != null
   * <br/><b>Precondition:</b>   !name.trim().equals("")
   * <br/><b>Precondition:</b>   name is a valid attribute with respect the this Component's class.
   *
   * @param name of the attribute. 
   * @return MdAttributeIF that defines the attribute with
   *  the given name.
   */
  public MdAttributeDAOIF getMdAttributeDAO(String name);
  
  /**
   * Writes to standard out all attribute names and their values of this
   * Component instance.  All values that are keys are dereferenced and
   * the values referenced by those keys are returned.  
   * 
   * <br/><b>Precondition:</b>   true
   *
   */    
  public void printAttributes();
  
}
