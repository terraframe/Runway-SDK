/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.metadata;

import java.io.Serializable;


public abstract class MdAttributeConcreteStrategy implements Serializable
{  
  /**
   * 
   */
  private static final long serialVersionUID = 5618692588284062510L;

  protected MdAttributeConcreteDAO mdAttribute;
    
  protected boolean appliedToDB;
  
  /**
   * @param mdAttribute
   */
  public MdAttributeConcreteStrategy(MdAttributeConcreteDAO mdAttribute)
  {
    super();
    this.mdAttribute = mdAttribute;
    this.appliedToDB = false;
  } 
  
  public void setAppliedToDB(boolean createColumn)
  {
    this.appliedToDB = createColumn;
  }

  /**
   * Returns the MdAttribute.
   * 
   * @return the MdAttribute
   */
  protected MdAttributeConcreteDAO getMdAttribute()
  {
    return this.mdAttribute;
  }
  
  /**
   * No special validation logic.
   */
  protected void preSaveValidate() {}
  
  /**
   * No special validation logic.
   */
  protected void validate() {}
  
  /**
   * No special commit logic.
   * 
   */
  public void setCommitState() {}
    
  /**
   * No special save logic
   */
  public void save() {}
  
  /**
   * No special delete logic.
   */
  public void delete() {}
  
  /**
   * No special post delete logic.
   */
  public void postDelete() {}
}
