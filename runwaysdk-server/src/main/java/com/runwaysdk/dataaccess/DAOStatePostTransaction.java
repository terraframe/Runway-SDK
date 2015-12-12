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
  
  private EntityDAO  entityDAO;
  private Boolean    initialized;
  
  protected DAOStatePostTransaction(EntityDAO _entityDAO, DAOState _daoState)
  {
    super(_daoState.getAttributeMap());
    this.entityDAO = _entityDAO;
    this.initialized = false;
    
    this.savepointId = _daoState.savepointId;
    this.problemNotificationId = _daoState.problemNotificationId;
    this.isNew = _daoState.isNew;
    
    // For elements only
    this.oldSequenceNumber = _daoState.oldSequenceNumber;
    
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
    // then refresh the state of the object
    if (TransactionState.getCurrentTransactionState() == null)
    {
      if (!this.initialized)
      {
        this.copyObjectState();
        this.initialized = true;
      }
    }
  }
  
  
  synchronized private void copyObjectState()
  {
    String entityDAOid = this.entityDAO.getObjectState().attributeMap.get(ComponentInfo.ID).getValue();
    
    EntityDAO entityDAO;// = EntityDAO.get(entityDAOid).getEntityDAO();
    // Refresh the object state from the database
        
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
      
    DAOStateDefault daoStateDefault = new DAOStateDefault(this.attributeMap);
    
    // If the object is null, then the creation of a new object was rolled back.
    if (entityDAO != null)
    {
      daoStateDefault.savepointId = entityDAO.getObjectState().savepointId;
      daoStateDefault.problemNotificationId = entityDAO.getObjectState().problemNotificationId;
      daoStateDefault.isNew = entityDAO.getObjectState().isNew;
      
      // old sequence number is automatically reset
      //oldSequenceNumber
    }
    else
    {
      daoStateDefault.clearSavepoint();      
      daoStateDefault.setProblemNotificationId("");
      daoStateDefault.isNew = true;
      daoStateDefault.oldSequenceNumber = this.oldSequenceNumber;
      this.entityDAO.setAppliedToDB(false);
    }
    
    this.entityDAO.setObjectState(daoStateDefault);
  }

}
