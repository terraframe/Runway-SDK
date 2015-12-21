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

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.database.RelationshipDAOFactory;
import com.runwaysdk.dataaccess.database.StructDAOFactory;
import com.runwaysdk.dataaccess.transaction.TransactionState;

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
public class DAOStatePostTransaction extends DAOState implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1913821984139234393L;
  
  private EntityDAO        entityDAO;
  private Boolean          initialized;
  private String           transactionId;
  
  protected DAOStatePostTransaction(EntityDAO _entityDAO, DAOState _daoState)
  {
    super(_daoState.attributeMap);
    this.entityDAO = _entityDAO;
    this.initialized = false;
    
    this.savepointId = _daoState.savepointId;
    this.problemNotificationId = _daoState.problemNotificationId;
    this.isNew = _daoState.isNew;
    
    // For elements only
    this.oldSequenceNumber = _daoState.oldSequenceNumber;
    
    TransactionState transactionState = TransactionState.getCurrentTransactionState();
    
    if (transactionState != null)
    {
      this.transactionId = transactionState.getTransactionId();
    }
    else
    {
      this.transactionId = null;
    }
    
  }
  
  @Override
  synchronized public Map<String, Attribute> getAttributeMap()
  {
    this.checkAndCopyObjectState();
     
    return this.attributeMap;
  }
  
  /**
   * @return savepoint it
   */
  synchronized public Integer getSavepointId()
  {
    this.checkAndCopyObjectState();
    
    return this.savepointId;
  }
   
  
  synchronized public void clearSavepoint()
  {
    this.checkAndCopyObjectState();
    
    this.savepointId = null;
  }
  
  synchronized public void setSavepointId(Integer _savepointId)
  {
    this.checkAndCopyObjectState();
    
    this.savepointId = _savepointId;
  }
  
  synchronized public String getProblemNotificationId()
  {
    this.checkAndCopyObjectState();
    
    return problemNotificationId;
  }

  synchronized public void setProblemNotificationId(String _problemNotificationId)
  {
    this.checkAndCopyObjectState();
    
    this.problemNotificationId = _problemNotificationId;
  }
  
  synchronized public boolean isNew()
  {
    this.checkAndCopyObjectState();
    
    return this.isNew;
  }
  
  /**
   * Do not call this method unless you know what you are doing.  Sets the new state of the object.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   */
  synchronized public void setIsNew(boolean isNew)
  {
    this.checkAndCopyObjectState();
    
    this.isNew = isNew;
  }
  
  synchronized public boolean isAppliedToDB()
  {
    this.checkAndCopyObjectState();
    
    return this.appliedToDB;
  }

  synchronized public void setAppliedToDB(boolean appliedToDB)
  {
    this.checkAndCopyObjectState();
    
    this.appliedToDB = appliedToDB;
  }
  
  synchronized public String getOldSequenceNumber()
  {
    this.checkAndCopyObjectState();

    return oldSequenceNumber;
  }

  synchronized public void setOldSequenceNumber(String _oldSequenceNumber)
  {
    this.checkAndCopyObjectState();
    
    this.oldSequenceNumber = _oldSequenceNumber;
  }
  
  synchronized private void checkAndCopyObjectState()
  {
    // Check to see if this is being executed within a transaction. If not,
    // then refresh the state of the object. If it is, check to see if it
    // is the original transaction
    TransactionState currentTransactionState = TransactionState.getCurrentTransactionState();
       
    if (currentTransactionState == null)
    {
      if (!this.initialized)
      {
        this.copyDefaultObjectState();
        this.initialized = true;
      }
    }
    // currentTransactionState != null
    else if (this.transactionId != null && !currentTransactionState.getTransactionId().equals(this.transactionId))
    {
      if (!this.initialized)
      {
        this.copyTransactionObjectState();
        this.initialized = true;
      }
    }
  }
  
  
  /**
   * Copies the object state from a {@link DAOStatePostTransaction} transaction back to a {@link DAOStateDefault} default object.
   */
  synchronized private void copyDefaultObjectState()
  {
    EntityDAO entityDAO = this.fetchEndityDAOandRefreshAttributeMap();
      
    DAOState daoState = new DAOStateDefault(this.attributeMap);
    
    this.populateDAOStateWithEntityDAO(entityDAO, daoState);
  }

  /**
   * Copies the object state from a {@link DAOStatePostTransaction} transaction to a different {@link DAOStatePostTransaction} in a different transaction.
   */
  synchronized private void copyTransactionObjectState()
  {
    EntityDAO entityDAO = this.fetchEndityDAOandRefreshAttributeMap();

    DAOState daoState = new DAOStatePostTransaction(entityDAO, this);
    
    this.populateDAOStateWithEntityDAO(entityDAO, daoState);
  }
  

  /**
   * Fetches an {@link EntityDAO} object from the database, which has the attribute state information and 
   * also the this.attributeMap on this object is refreshed.
   * <p/>
   * @Postcondition this.attribteMap is updated if the fetched {@link EntityDAO} object is not null.
   * 
   * @return {@link EntityDAO} object
   */
  synchronized private EntityDAO fetchEndityDAOandRefreshAttributeMap()
  {
    EntityDAO entityDAO = null;
 
    String entityDAOid = this.entityDAO.getObjectState().attributeMap.get(ComponentInfo.ID).getValue();   
        
    if (this.entityDAO instanceof BusinessDAO)
    {
      entityDAO = (EntityDAO)BusinessDAOFactory.get(entityDAOid);
    }
    else if (this.entityDAO instanceof RelationshipDAO)
    {
      entityDAO = (EntityDAO)RelationshipDAOFactory.get(entityDAOid);
    }
    else
    {
      entityDAO = (EntityDAO)StructDAOFactory.get(entityDAOid);
    }
    
    // If the object is null, then the creation of a new object was rolled back.
    if (entityDAO != null)
    {
      this.attributeMap = entityDAO.getObjectState().attributeMap;
    }
    
    return entityDAO;
  }
  

  /**
   * Populates the state of the given {@link DAOState} object with the given {@link EntityDAO}
   * 
   * @param entityDAO
   * @param daoState
   */
  synchronized private void populateDAOStateWithEntityDAO(EntityDAO entityDAO, DAOState daoState)
  {
    // If the object is null, then the creation of a new object was rolled back.
    if (entityDAO != null)
    {
      // not sure if setting these two artributes are necessary
      daoState.savepointId = entityDAO.getObjectState().savepointId;
      daoState.problemNotificationId = entityDAO.getObjectState().problemNotificationId;
      
      daoState.isNew = entityDAO.getObjectState().isNew;
      daoState.appliedToDB = entityDAO.getObjectState().appliedToDB;
    }
    else
    {
      daoState.clearSavepoint();      
      daoState.setProblemNotificationId("");
      daoState.isNew = true;
      daoState.oldSequenceNumber = this.oldSequenceNumber;
      daoState.appliedToDB = false;
    }
    
    this.entityDAO.setObjectState(daoState);
  }
}
