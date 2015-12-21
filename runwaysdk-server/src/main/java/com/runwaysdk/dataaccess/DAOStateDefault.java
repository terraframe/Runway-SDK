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
package com.runwaysdk.dataaccess;

import java.io.Serializable;
import java.util.Map;

import com.runwaysdk.dataaccess.attributes.entity.Attribute;

/**
 * The subclass {@link DAOStateDefault} is the default state when an {@link EntityDAO} object is 
 * instantiated. 
 * 
 * @author nathan
 *
 */
public class DAOStateDefault extends DAOState implements Serializable
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1251949152615268643L;

  protected DAOStateDefault(Map<String, Attribute> _attributeMap)
  {
    super(_attributeMap);
  }

  @Override
  public Map<String, Attribute> getAttributeMap()
  {
    return this.attributeMap;
  }
  
  /**
   * @return savepoint it
   */
  public Integer getSavepointId()
  {
    return this.savepointId;
  }
  
  public void clearSavepoint()
  {
    this.savepointId = null;
  }
  
  public void setSavepointId(Integer _savepointId)
  {   
    this.savepointId = _savepointId;
  }

  public String getProblemNotificationId()
  {
    return problemNotificationId;
  }

  public void setProblemNotificationId(String _problemNotificationId)
  {
    this.problemNotificationId = _problemNotificationId;
  }
  
  public boolean isNew()
  {
    return this.isNew;
  }
  
  /**
   * Do not call this method unless you know what you are doing.  Sets the new state of the object.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   */
  public void setIsNew(boolean isNew)
  {
    this.isNew = isNew;
  }
    
  public boolean isAppliedToDB()
  {
    return appliedToDB;
  }

  public void setAppliedToDB(boolean appliedToDB)
  {
    this.appliedToDB = appliedToDB;
  }

  public String getOldSequenceNumber()
  {
    return oldSequenceNumber;
  }

  public void setOldSequenceNumber(String _oldSequenceNumber)
  {
    this.oldSequenceNumber = _oldSequenceNumber;
  }
}
