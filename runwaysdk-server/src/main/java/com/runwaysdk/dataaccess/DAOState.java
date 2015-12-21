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
 * Stores the state of an {@link EntityDAO} object. This strategy pattern allows for the state of
 * a reference to an object that was created before a transaction to have its state properly updated 
 * after a transaction. The transaction cache updates the state of the object at the end of the 
 * transaction, but a different object reference is updated. Therefore, an object reference made prior 
 * to a transaction to an object that is updated by a transaction may not have its state properly updated 
 * immediately after the transaction.
 * 
 * The subclass {@link DAOStateDefault} is the default state when an {@link EntityDAO} object is 
 * instantiated. 
 * 
 * The subclass {@link DAOStatePostTransaction} is set on an {@link EntityDAO} object just before it
 * is placed into the transaction cache, where it is then serialized into another reference. The original
 * reference will then have its state updated as soon as the next read to its state is made. After the state
 * is updated, the strategy should switch to {@link DAOStateDefault}.
 * 
 * @author nathan
 *
 */
public abstract class DAOState implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -5336013306500162749L;

  /**
   * Map of Attribute objects the component has. They are of a name-value pair
   * relation. <br/>
   * <b>invariant</b> attributeMap != null
   */
  protected Map<String, Attribute> attributeMap;
  
  /**
   * Id of the database savepoint (if any) used to create the object.
   */
  protected Integer savepointId          = null;
  
  /**
   * Id used for AttributeProblems (not messages). New instances that fail will
   * have a different ID on the client.
   */
  protected String problemNotificationId = "";

  /**
   * Indicates if this is a new instance. If it is new, then the records that
   * represent this component have not been created.
   */
  protected boolean isNew                = false;
  
  /**
   * Indicates if this instance has been applied to the database. There is a
   * difference between this field and isNew. isNew is used for constraint
   * validation.
   */
  protected boolean appliedToDB          = true;

  /**
   * Only to be used by transaction management on rollbacks for {@link ElementDAO}.
   */
  protected String oldSequenceNumber     = "";
  

  protected DAOState(Map<String, Attribute> _attributeMap)
  {
    this.attributeMap   = _attributeMap;
  }
  
  /**
   * @return the attribute map
   */
  public abstract Map<String, Attribute> getAttributeMap();
  
  
  /**
   * @return savepoint it
   */
  public abstract Integer getSavepointId();

  public abstract void setSavepointId(Integer _savepointId);
  
  public abstract void clearSavepoint();
  
  public abstract String getProblemNotificationId();

  public abstract void setProblemNotificationId(String _problemNotificationId);

  public abstract boolean isNew();
   
  public abstract boolean isAppliedToDB();

  public abstract void setAppliedToDB(boolean appliedToDB);
  
  /**
   * Do not call this method unless you know what you are doing.  Sets the new state of the object.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   */
  public abstract void setIsNew(boolean isNew);
  
  public abstract String getOldSequenceNumber();

  public abstract void setOldSequenceNumber(String _oldSequenceNumber);
  
}
