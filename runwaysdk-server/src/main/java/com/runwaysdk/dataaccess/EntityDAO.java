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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.attributes.AttributeSet;
import com.runwaysdk.dataaccess.attributes.AttributeTypeException;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBlob;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.EntityDAOFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.resolver.IEntityContainer;
import com.runwaysdk.dataaccess.transaction.TransactionCache;
import com.runwaysdk.dataaccess.transaction.TransactionCacheIF;
import com.runwaysdk.dataaccess.transaction.TransactionState;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;

public abstract class EntityDAO extends ComponentDAO implements EntityDAOIF, Serializable, IEntityContainer
{

  /**
   * 
   */
  private static final long        serialVersionUID      = 7578346295961644414L;

  /**
   * Stores the state of this {@link EntityDAO} object.
   */
  private DAOState objectState;
  
  /**
   * Indicates if this instance is being imported to the database from an
   * external source. Many auto-generated attributes and encryptions are
   * bypassed when importing.
   */
  private boolean                  isImport;

  /**
   * True when this object is being applied within the context of an import, but
   * the object itself may not be imported. Sometimes objects that are already
   * local (but possibly mastered elsewhere) need to be modified in order to fix
   * a conflict that occurred as a result of import. Some things need to be
   * bypassed when we are modifying an object within the context of import
   * conflict resolution.
   */
  private boolean                  importResolution;

  /**
   * Indicates whether the delete method has completed execution and the object
   * has been deleted;
   */
  private boolean                  isDeleted             = false;
  
  /**
   * Either cache MRU.
   */
  private boolean                  isFromCacheMRU        = false;

  /**
   * From cache all.
   */
  private boolean                  isFromCacheAll        = false;

  /**
   * Flag denoting if this represents a non-persisting disconnected object
   */
  private boolean                  disconnected          = false;

  /**
   * The old ID of the object should the ID ever need to change due to a new key
   * value from which the ID is hashed. This is used to update caches with the
   * the new ID.
   */
  private String                   oldId                 = null;

  /**
   * True if <code>this.hasIdChanged()</code> is true and the new id has been
   * applied to the database.
   */
  private boolean                  newIdApplied          = false;

  /**
   * The old KEY of the object should the KEY ever need to change. This is used
   * to update caches with the the new KEY.
   */
  private String                   oldKey                = null;

  /**
   * The default constructor, does not set any attributes
   */
  public EntityDAO()
  {
    super();

    this.objectState = new DAOStateDefault(null);
    this.isImport = false;
    this.importResolution = false;
  }

  /**
   * Constructs a EntityDAO from the given {@link HashMap} of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> entityType != nullwww.
   * 
   * @param attributeMap
   * @param entityType
   */
  public EntityDAO(Map<String, Attribute> attributeMap, String entityType)
  {
    super(entityType);

    this.objectState = new DAOStateDefault(attributeMap);
    this.isImport = false;
    this.importResolution = false;

    this.linkAttributes();
  }
  
  /**
   * Returns a boolean that indicates if this is a new instance (i.e. has not been committed to the database).
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   *
   * @return a boolean that indicates if this is a new instance
   */
  public boolean isNew()
  {
    return this.getObjectState().isNew();
  }

  /**
   * Do not call this method unless you know what you are doing.  Sets the new state of the object.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   */
  public void setIsNew(boolean isNew)
  {
    this.getObjectState().setIsNew(isNew);
  }
  
  /**
   * @return the state of this {@link EntityDAO} object.
   */
  protected DAOState getObjectState()
  {
    return this.objectState;
  }
  
  /**
   * Sets the internal state of the object.
   * 
   * @param _objectState
   */
  protected void setObjectState(DAOState daoState)
  {
    this.objectState = daoState;
  }
  
  /**
   * Sets the internal state of the object.
   * 
   * @param _objectState
   */
  void setTransactionState()
  {
    this.objectState = new DAOStatePostTransaction(this, this.objectState);
  }
  
  /**
   * Return the old ID of the object should the ID ever need to change due to a
   * new key value from which the ID is hashed. This is used to update caches
   * with the the new ID.
   */
  public String getOldId()
  {
    return this.oldId;
  }

  /**
   * Clears the previous id, if any.
   */
  public void clearOldId()
  {
    this.oldId = null;
    this.newIdApplied = false;
  }

  /**
   * Returns true if the id has changed, false otherwise.
   * 
   * @return true if the id has changed, false otherwise.
   */
  public boolean hasIdChanged()
  {
    if (this.oldId != null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Return the old KEY of the object should the KEY ever need to change. This
   * is used to update caches with the the new KEY.
   */
  public String getOldKey()
  {
    return this.oldKey;
  }

  /**
   * Clears the previous KEY, if any.
   */
  public void clearOldKey()
  {
    this.oldKey = null;
  }

  /**
   * Returns true if the KEY has changed, false otherwise.
   * 
   * @return true if the KEY has changed, false otherwise.
   */
  public boolean hasKeyChanged()
  {
    if (this.oldKey != null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Adds the given attributes to this object -This is an internal method and
   * should not be called.
   */
  public void addAdditionalAttributes(Map<String, Attribute> _attributeMap)
  {
    for (Attribute attribute : _attributeMap.values())
    {
      attribute.setContainingComponent(this);
    }
    this.getObjectState().getAttributeMap().putAll(_attributeMap);
  }

  /**
   * Returns true if this object originated from a cache, false otherwise.
   * 
   * @return true if this object originated from a cache, false otherwise.
   */
  public boolean isFromCache()
  {
    return this.isFromCacheAll || this.isFromCacheMRU;
  }

  /**
   * If true then this object originated from a MRU cache, false otherwise.
   * 
   * @param isFromCache
   */
  public void setIsFromCacheMRU(boolean isFromCacheMRU)
  {
    this.isFromCacheMRU = isFromCacheMRU;
  }

  /**
   * Returns true if this object originated from a cache all cache, false
   * otherwise.
   * 
   * @return true if this object originated from a cache all cache, false
   *         otherwise.
   */
  public boolean isFromCacheAll()
  {
    return this.isFromCacheAll;
  }

  /**
   * If true then this object originated from a cache all cache, false
   * otherwise.
   * 
   * @param isFromCacheAll
   */
  public void setIsFromCacheAll(boolean isFromCacheAll)
  {
    this.isFromCacheAll = isFromCacheAll;
  }

  /**
   * Returns the Id used for AttributeProblems (not messages). New instances
   * that fail will have a different ID on the client.
   * 
   * @return problem notification id.
   */
  public String getProblemNotificationId()
  {
    // If we're using predictive id's then the id may have changed. Return the
    // old id.
    if (this.hasIdChanged())
    {
      return this.oldId;
    }

    if (this.getObjectState().getProblemNotificationId().equals(""))
    {
      return this.getId();
    }
    else
    {
      return this.getObjectState().getProblemNotificationId();
    }
  }

  /**
   * Iterates over the map of Attributes, setting <b>this</b> as their
   * containing Component.<br>
   * <br>
   * <b>Precondition:</b> attributeMap != null<br>
   * <b>Postcondition:</b> attributeMap.get(value).getContainingComponent =
   * <b>this</b>
   */
  private void linkAttributes()
  {
    for (Attribute attribute : this.getObjectState().getAttributeMap().values())
    {
      attribute.setContainingComponent(this);
    }
    return;
  }

  /**
   * Returns the Attribute object with the given name.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> Attribute name is valid for the class of this
   * Component <br/>
   * <b>Postcondition:</b> return value != null
   * 
   * @param name
   *          name of the attribute
   * @return Attribute object with the given name
   * @throws DataAccessException
   *           if the attribute with the given name does not exist for the class
   *           of the Component
   */
  public AttributeIF getAttributeIF(String name)
  {
    AttributeIF returnAttribute = (AttributeIF) this.getObjectState().getAttributeMap().get(name);

    if (returnAttribute == null)
    {
      TransactionCacheIF cache = TransactionCache.getCurrentTransactionCache();

      if (cache != null)
      {
        MdEntityDAOIF mdEntity = this.getMdClassDAO();

        while (mdEntity != null)
        {
          String type = mdEntity.definesType();

          String key = MdAttributeConcreteDAO.buildKey(type, name);

          MdAttributeDAOIF mdAttribute = cache.getAddedMdAttribute(key);

          if (mdAttribute != null)
          {
            Attribute attribute = EntityDAOFactory.createAttributeForEntity(mdEntity, mdAttribute.getMdAttributeConcrete());
            attribute.setContainingComponent(this.getEntityDAO());

            this.getObjectState().getAttributeMap().put(name, attribute);

            return attribute;
          }

          mdEntity = mdEntity.getSuperClass();
        }

      }

      String error = "An attribute named [" + name + "] does not exist on type [" + this.getType() + "]";
      throw new AttributeDoesNotExistException(error, name, this.getMdClassDAO());
    }

    return returnAttribute;
  }

  /**
   * Returns true if the component has an attribute with the given name, false
   * otherwise.
   * 
   * @param name
   * @return true if the component has an attribute with the given name, false
   *         otherwise.
   */
  public boolean hasAttribute(String name)
  {
    if (this.getObjectState().getAttributeMap().get(name) != null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns an array of the Attributes for this Component.<br>
   * <br>
   * <b>Precondition:</b> true<br>
   * <b>Postcondition:</b> array.length = attributeMap.size();
   */
  public AttributeIF[] getAttributeArrayIF()
  {
    AttributeIF[] array = new Attribute[this.getObjectState().getAttributeMap().size()];
    int index = 0;

    for (AttributeIF attribute : this.getObjectState().getAttributeMap().values())
    {
      array[index] = attribute;
      index++;
    }

    return array;
  }

  /**
   *
   */
  public EntityDAO getEntityDAO()
  {
    try
    {
      // Only clone the object if this object comes from a cache.
      if (this.isFromCache())
      {
        return (EntityDAO) this.clone();
      }
      else
      {
        return this;
      }
    }
    catch (CloneNotSupportedException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Not to be called from the constructor.
   */
  public void setTypeName(String entityType)
  {
    this.componentType = entityType;
    if (this.getObjectState().getAttributeMap() != null && this.getObjectState().getAttributeMap().get(EntityInfo.TYPE) != null)
    {
      this.getAttribute(EntityInfo.TYPE).setValue(entityType);
    }
  }

  /**
   * Validates the attribute with the given name. If the attribute is not valid,
   * then an AttributeException exception is thrown.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> An attribute of the given name exists for instances of
   * this class.
   * 
   * @param name
   *          name of the attribute
   * @throws AttributeException
   *           if the attribute is not valid.
   */
  public void validateAttribute(String name)
  {
    Attribute attribute = this.getAttribute(name);

    attribute.validateEverything(attribute.getValue());
  }

  /**
   * Sets the attribute of the given name with the given value.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> value != null <br/>
   * <b>Precondition:</b> Attribute name is valid for this EntityDAO's class <br/>
   * <b>Postcondition:</b> Attribute is set with the given value
   * 
   * <br/>
   * <b>modifies:</b> this.componentType if the attribute being modified is the
   * name of the class.
   * 
   * @param name
   *          name of the attribute
   * @param value
   *          value to assign to the given attribute
   * @throws DataAccessException
   *           if the attribute with the given name does not exist for the class
   *           of the EntityDAO
   */
  public void setValue(String name, String value)
  {
    if (name.trim().equals(EntityInfo.KEY))
    {
      this.setKey(value);
    }
    else
    {
      // Setting and validating are a single method
      Attribute attribute = this.getAttribute(name);

      // Use blob specific validation if necessary.
      if (attribute instanceof AttributeBlob)
      {
        ( (AttributeBlob) attribute ).validate( ( (AttributeBlob) attribute ).getBlobAsBytes(), attribute.getMdAttribute());
      }
      else
      {
        attribute.setValueAndValidate(value);
      }

      if (name.equals(EntityInfo.TYPE))
      {
        this.componentType = value;
      }
    }
  }

  /**
   * Sets the id to the given value, and saves the state of the old id.
   * 
   * @param newId
   */
  // Called by an aspect
  @SuppressWarnings("unused")
  private void setId(String newId)
  {
    if (this.isAppliedToDB() && !this.getId().equals(newId))
    {
      this.oldId = this.getAttribute(EntityInfo.ID).getValue();
    }
    this.getAttribute(EntityInfo.ID).setValue(newId);
  }

  /**
   * Do not call this method unless you know what you are doing.
   * 
   * @param oldId
   */
  public void setOldId(String oldId)
  {
    this.oldId = oldId;
  }

  /**
   * Returns true if the the id has changed and has been applied to the
   * database, false otherwise.
   * 
   * @return true if the the id has changed and has been applied to the
   *         database, false otherwise.
   */
  public boolean isNewIdApplied()
  {
    return newIdApplied;
  }

  /**
   * @param newIdApplied
   *          the newIdApplied to set
   */
  public void setNewIdApplied(boolean newIdApplied)
  {
    this.newIdApplied = newIdApplied;
  }

  /**
   * Sets the KEY to the given value, and saves the state of the old KEY.
   * 
   * @param newId
   */
  public void setKey(String newKey)
  {
    if (!this.getAttribute(ComponentInfo.KEY).getValue().equals(newKey))
    {
      if (!this.getAttribute(ComponentInfo.KEY).getValue().trim().equals(""))
      {
        this.oldKey = this.getAttribute(ComponentInfo.KEY).getValue();        
      }
      this.getAttribute(ComponentInfo.KEY).setValue(newKey);
    }
  }

  /**
   * Some attribute types store objects instead of Strings.
   * 
   * @param name
   * @param _object
   */
  public void setValue(String name, Object _object)
  {
    if (_object instanceof String)
    {
      this.setValue(name, (String) _object);
    }
    else
    {
      Attribute attribute = this.getAttribute(name);

      attribute.setValue(_object);
    }
  }

  /**
   * Adds an item to an enumerated attribute. If the attribute does not allow
   * multiplicity, the <code>value</code> replaces the previous item.
   * 
   * @param name
   *          Name of the set attribute
   * @param value
   *          Value to be added to the attribute
   */
  public void addItem(String name, String value)
  {
    try
    {
      AttributeSet attrSet = (AttributeSet) this.getAttribute(name);
      attrSet.addItem(value);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on type [" + getType() + "] is not a set attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Replaces the items of a set attribute. If the attribute does not allow
   * multiplicity, then the {@code values} collection must contain only one
   * item.
   * 
   * @param name
   *          Name of the set attribute
   * @param values
   *          Collection of set item ids
   */
  public void replaceItems(String name, Collection<String> values)
  {
    try
    {
      AttributeSet attrSet = (AttributeSet) this.getAttribute(name);
      attrSet.replaceItems(values);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on type [" + getType() + "] is not a set attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Deletes an item from a set Attribute.
   * 
   * @param name
   *          Name of the set attribute
   * @param value
   *          Value to be removed from the attribute
   */
  public void removeItem(String name, String value)
  {
    try
    {
      AttributeSet attrSet = (AttributeSet) this.getAttribute(name);
      attrSet.removeItem(value);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on type [" + getType() + "] is not an enumerated attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Deletes an item from a set Attribute.
   * 
   * @param name
   *          Name of the set attribute
   * @param value
   *          Value to be removed from the attribute
   */
  public void clearItems(String name)
  {
    try
    {
      AttributeSet attrSet = (AttributeSet) this.getAttribute(name);
      attrSet.clearItems();
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on type [" + getType() + "] is not an enumerated attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Sets the value of a struct attribute.
   * 
   * @param structAttributeName
   * @param attributeName
   * @param value
   */
  public void setStructValue(String structAttributeName, String attributeName, String value)
  {
    // Setting and validating are a single method
    AttributeIF attribute = this.getAttributeIF(structAttributeName);
    if (attribute instanceof AttributeStruct)
    {
      ( (AttributeStruct) attribute ).setValue(attributeName, value);
    }
    else
    {
      String error = "Attribute [" + structAttributeName + "] is not a struct attribute";
      throw new AttributeException(error);
    }
  }

  /**
   * Sets the value of a struct blob attribute
   * 
   * @param structAttributeName
   * @param blobName
   * @param value
   */
  public void setStructBlob(String structAttributeName, String blobName, byte[] value)
  {
    // Setting and validating are a single method
    AttributeIF attribute = this.getAttributeIF(structAttributeName);
    if (attribute instanceof AttributeStruct)
    {
      ( (AttributeStruct) attribute ).setBlob(blobName, value);
    }
    else
    {
      String error = "Attribute [" + structAttributeName + "] is not a struct attribute";
      throw new AttributeException(error);
    }
  }

  /**
   *
   */
  public byte[] getBlob(String attributeName)
  {
    AttributeBlobIF blob = (AttributeBlobIF) this.getAttributeIF(attributeName);
    return blob.getBlobAsBytes();
  }

  /**
   * 
   * @param attributeName
   * @param value
   */
  public void setBlob(String attributeName, byte[] value)
  {
    AttributeBlob blob = (AttributeBlob) this.getAttribute(attributeName);
    blob.setBlobAsBytes(value);
  }

  /**
   * Returns a LinkedList of MdAttributeIF objects representing metadata for
   * each attribute of this object's class.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a LinkedList of MdAttributeIF objects representing metadata for
   *         each attribute of this object's class.
   */
  public List<? extends MdAttributeConcreteDAOIF> getMdAttributeDAOs()
  {
    return this.getMdClassDAO().getAllDefinedMdAttributes();
  }

  /**
   * Returns a MdAttributeIF representing the attribute metadata of the
   * attribute with the given name.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Postcondition:</b> true
   * 
   * @param name
   *          of the attribute.
   * @return MdAttributeIF representing the attribute metadata of the attribute
   *         with the given name
   */
  public MdAttributeConcreteDAOIF getMdAttributeDAO(String name)
  {
    return (MdAttributeConcreteDAOIF) super.getMdAttributeDAO(name);
  }

  /**
   * Returns the attribute object of the given name.
   * 
   * @param name
   * @return
   */
  public Attribute getAttribute(String name)
  {
    return (Attribute) this.getAttributeIF(name);
  }

  /**
   * Returns an array of attribute objects.
   * 
   * @return array of attribute objects.
   */
  public Attribute[] getAttributeArray()
  {
    return (Attribute[]) getAttributeArrayIF();
  }

  /**
   * Sets isNew = false and sets all attributes to isModified = false.
   * 
   */
  public void setCommitState()
  {
    this.getObjectState().clearSavepoint();

    this.setIsNew(false);

    this.getObjectState().setProblemNotificationId("");

    Attribute[] attributeArray = this.getAttributeArray();

    for (int i = 0; i < attributeArray.length; i++)
    {
      attributeArray[i].setCommitState();
    }
  }

  /**
   * Sets appliedToDB to false if the object is new, as the database will
   * rollback any newly inserted records.
   * 
   */
  public void rollbackState()
  {
    this.getObjectState().clearSavepoint();

    if (this.isNew() == true)
    {
      this.setAppliedToDB(false);
    }

    Attribute[] attributeArray = this.getAttributeArray();

    for (int i = 0; i < attributeArray.length; i++)
    {
      attributeArray[i].rollbackState();
    }
  }

  public void clearSavepoint()
  {
    this.getObjectState().clearSavepoint();
  }

  public Integer getSavepointId()
  {
    return this.getObjectState().getSavepointId();
  }

  /**
   * Call to rollback to a savepoint.
   * 
   */
  public void rollbackState(Integer rollbackSavepointId)
  {
    if (this.isNew() == true)
    {
      TransactionState transactionState = TransactionState.getCurrentTransactionState();

      // If we are rolling back the savepoint that was used to create this
      // object, then we need to set the applied to db flag to false, as the
      // insert statement will be rolled back.
      if (this.getObjectState().getSavepointId() != null)
      {
        boolean isNestedSavepoint = transactionState.isNestedSavepoint(rollbackSavepointId, this.getObjectState().getSavepointId());

        if (isNestedSavepoint)
        {
          this.setAppliedToDB(false);
        }
      }
    }

    Attribute[] attributeArray = this.getAttributeArray();

    for (int i = 0; i < attributeArray.length; i++)
    {
      attributeArray[i].rollbackState(rollbackSavepointId);
    }
  }

  public void rollbackState(boolean isNew, boolean appliedToDB)
  {
    this.rollbackState();

    this.getObjectState().setAppliedToDB(appliedToDB);
    this.setIsNew(isNew);
  }

  /**
   * Applies the state of this EntityDAO to the database. If this is a new
   * EntityDAO, then records are created in the database and an ID is created.
   * If this is not a new EntityDAO, then records are modified in the database.
   * 
   * <br/>
   * <b>Precondition:</b> Attributes must have correct values as defined in
   * their meta data. <br/>
   * <b>Postcondition:</b> state of the EntityDAO is preserved in the database. <br/>
   * <b>Postcondition:</b> return value is not null
   * 
   * @param validateRequired
   *          true if attributes should be checked for required values, false
   *          otherwise. StructDAOs used for struct attributes may or may not
   *          need required attributes validated.
   * @return ID of the EntityDAO.
   * @throws DataAccessException
   *           if an attribute contains a value that is not correct with respect
   *           to the metadata.
   */
  public String save(boolean validateRequired)
  {
    // UserIdAspect will replace the user id with the ID of the user
    // doing this action through the facade

    if (this.isAppliedToDB() == false && !this.isImport())
    {
      // Do not modify the siteMaster field on an import
      Attribute siteMaster = this.getAttribute(ElementInfo.SITE_MASTER);
      String value = siteMaster.getValue();

      if (value == null || value.length() == 0)
      {
        siteMaster.setValue(CommonProperties.getDomain());
      }
    }

    this.validate();

    if (this.isAppliedToDB() == false)
    {
      this.insert(validateRequired);
    }
    else
    {
      this.update(validateRequired);
    }

    this.setAppliedToDB(true);

    return this.getId();
  }

  /**
   * Set the if the entity object is being imported
   * 
   * @param isImport
   */
  private void setImport(boolean isImport)
  {
    this.isImport = isImport;

    Attribute[] attributes = this.getAttributeArray();

    for (Attribute attribute : attributes)
    {
      attribute.setImport(isImport);
    }
  }

  /**
   * Returns true if the EntityDAO is currently in an import state.
   * 
   * @return true if the EntityDAO is currently in an import state.
   */
  public final boolean isImport()
  {
    return this.isImport;
  }

  /**
   * Indicates whether the EntityDAO is currently being modified to resolve an
   * import conflict.
   * 
   * @param importResolution
   */
  public final void setImportResolution(boolean importResolution)
  {
    this.importResolution = importResolution;
  }

  /**
   * Returns true if the EntityDAO is currently being modified to resolve an
   * import conflict.
   * 
   * @return true if the EntityDAO is currently in an modified to resolve an
   *         import conflict.
   */
  public final boolean isImportResolution()
  {
    return this.importResolution;
  }

  /**
   * Returns the site from which this object is mastered.
   * 
   * @return site from which this object is mastered.
   */
  public String getSiteMaster()
  {
    return this.getAttributeIF(EntityInfo.SITE_MASTER).getValue();
  }

  /**
   * Used by the import routine that imports existing data. Attributes such as
   * the create date, created by, etc. should not be overridden, as they are
   * imported from another source.
   * 
   */
  public final String importSave()
  {
    // Set the import flag to true
    this.setImport(true);

    try
    {
      // Apply the object to the database
      return this.apply();
    }
    finally
    {
      // Set the import flag to false, to ensure further updates are tracked
      this.setImport(false);
    }
  }

  /**
   * Validates the object.
   * 
   */
  protected void validate()
  {
    // Check for unique attribute violations
    this.uniquenessTest();

    // Check for duplicate sets of attributes
    this.uniqueAttributeGroupTest();

    if (!this.isNew() && this.isAppliedToDB() == true)
    {
      // Ensure that the current site is the site of creation, but not on an
      // import
      this.validateSite();
    }
  }

  /**
   * Validates the new EntityDAO and creates it in the database.
   * 
   * <br/>
   * <b>Precondition:</b> this.isNew == true
   * 
   * @param validateRequired
   *          ture if attributes should be checked for required values, false
   *          otherwise. StructDAOs used for struct attributes may or may not
   *          need required attributes validated.
   */
  private void insert(boolean validateRequired)
  {
    for (MdAttributeConcreteDAOIF mdAttribute : this.getMdAttributeDAOs())
    {
      // A new attribute may have been added since this object was instantiated.
      if (this.hasAttribute(mdAttribute.definesAttribute()))
      {
        Attribute attribute = this.getAttribute(mdAttribute.definesAttribute());

        // make sure it has a value, if required
        if (validateRequired)
        {
          if (mdAttribute instanceof MdAttributeBlobDAOIF)
          {
            AttributeBlob blob = (AttributeBlob) attribute;
            blob.validateRequired(blob.getBlobAsBytes(), mdAttribute);
          }
          else
          {
            attribute.validateRequired(attribute.getValue(), mdAttribute);
          }
        }
        // Initialize all references to this attribute
        attribute.initReferences(mdAttribute);
      }
    }
    EntityDAOFactory.insert(this, validateRequired);

    Savepoint currentSavepoint = Database.peekCurrentSavepoint();

    if (currentSavepoint != null)
    {
      try
      {
        this.getObjectState().setSavepointId(currentSavepoint.getSavepointId());
      }
      catch (SQLException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

  }

  /**
   * Updates changes made to attributes of the given EntityDAO in the database.
   * 
   * @param validateRequired
   *          ture if attributes should be checked for required values, false
   *          otherwise. StructDAOs used for struct attributes may or may not
   *          need required attributes validated.
   */
  private void update(boolean validateRequired)
  {
    for (MdAttributeConcreteDAOIF mdAttribute : this.getMdAttributeDAOs())
    {
      // A new attribute may have been added since this object was instantiated.
      if (this.hasAttribute(mdAttribute.definesAttribute()))
      {
        Attribute attribute = this.getAttribute(mdAttribute.definesAttribute());

        if (attribute.isModified())
        {
          // make sure it has a value, if required
          if (validateRequired)
          {
            attribute.validateRequired(attribute.getValue(), mdAttribute);
          }
          // Update any references to this modified attribute.
          attribute.updateReferences(mdAttribute);
        }
      }
    }
    EntityDAOFactory.update(this, validateRequired);
  }

  /**
   * Applies the state of this EntityDAO to the database. If this is a new
   * EntityDAO, then records are created in the database and an ID is created.
   * If this is not a new EntityDAO, then records are modified in the database.
   * This method differs from the save method in that an Aspect will verify that
   * the user performing the opperation has a lock on the object.
   * 
   * <br/>
   * <b>Precondition:</b> User performing this operation has a lock on the
   * EntityDAO <br/>
   * <b>Precondition:</b> Attribues must have correct values as defined in their
   * meta data. <br/>
   * <b>Postcondition:</b> state of the EntityDAO is preserved in the database. <br/>
   * <b>Postcondition:</b> return value is not null
   * 
   * @return ID of the EntityDAO.
   * @throws DataAccessException
   *           if an attribute contains a value that is not correct with respect
   *           to the metadata. DataAccessException if the EntityDAO is locked
   *           by another user
   */
  public String apply()
  {
    // UserIdAspect will replace the user id with the ID of the user
    // doing this action through the facade

    if (this.isDisconnected())
    {
      String error = "Object [" + this.getId() + "] can not be applied: It is a disconnected entity.";
      throw new DisconnectedEntityException(error);
    }

    // returns the ID as a string, a new one or the existing one...
    String returnId = this.save(true);

    return returnId;
  }

  /**
   * Returns a copy of the given EntityDAO instance, with a new id and mastered
   * at the current site. The state of the object is new and has not been
   * applied to the database.
   * 
   * @return a copy of the given EntityDAO instance
   */
  public abstract EntityDAO copy();

  /**
   * Deletes the EntityDAO but not from a businessContext. All EntityDAO
   * subclasses should NOT implement this method, rather they should implement
   * the one that takes a boolean parameter.
   * 
   * <br/>
   * <b>Precondition:</b> isNew == false
   * 
   */
  public void delete()
  {
    this.delete(false);
  }

  /**
   * Used by the data importer to delete an object. Developers should not use
   * this method.
   * 
   * <br/>
   * <b>Precondition:</b> isNew == false
   * 
   */
  public final void importDelete()
  {
    this.setImport(true);

    try
    {
      this.delete(false);
    }
    finally
    {
      this.setImport(false);
    }
  }

  /**
   * Sets the value of a struct attribute.
   * 
   * @param structName
   *          The name of the struct
   * @param attributeName
   *          The name of the attribute (inside the struct)
   * @param value
   *          The value to set
   * @throws AttributeException
   *           If the supplied structName isn't really a struct
   */
  public void addStructItem(String structName, String attributeName, String value)
  {
    // Setting and validating are a single method
    AttributeIF attribute = this.getAttributeIF(structName);
    if (attribute instanceof AttributeStruct)
    {
      ( (AttributeStruct) attribute ).addItem(attributeName, value);
    }
    else
    {
      String error = "Attribute [" + structName + "] is not a struct attribute";
      throw new AttributeException(error);
    }
  }

  /**
   * Replaces the items of an enumerated struct attribute. If the attribute does
   * not allow multiplicity, then the {@code values} collection must contain
   * only one item.
   * 
   * @param structName
   *          The name of the struct
   * @param attributeName
   *          The name of the attribute (inside the struct)
   * @param values
   *          Collection of enumerated it ids
   * 
   * @throws AttributeException
   *           If the supplied structName isn't really a struct
   */
  public void replaceStructItems(String structName, String attributeName, Collection<String> values)
  {
    // Setting and validating are a single method
    AttributeIF attribute = this.getAttributeIF(structName);
    if (attribute instanceof AttributeStruct)
    {
      ( (AttributeStruct) attribute ).replaceItems(attributeName, values);
    }
    else
    {
      String error = "Attribute [" + structName + "] is not a struct attribute";
      throw new AttributeException(error);
    }
  }

  /**
   * Remove an item for an enumerated struct attribute.
   * 
   * @param structName
   *          The name of the struct
   * @param attributeName
   *          The name of the attribute (inside the struct)
   * @param value
   *          The value to set
   * @throws AttributeException
   *           If the supplied structName isn't really a struct
   */
  public void removeStructItem(String structName, String attributeName, String value)
  {
    // Setting and validating are a single method
    AttributeIF attribute = this.getAttributeIF(structName);
    if (attribute instanceof AttributeStruct)
    {
      ( (AttributeStruct) attribute ).removeItem(attributeName, value);
    }
    else
    {
      String error = "Attribute [" + structName + "] is not a struct attribute";
      throw new AttributeException(error);
    }
  }

  /**
   * Clears all the values of a struct enumeration attribute.
   * 
   * @param structName
   *          The name of the struct
   * @param attributeName
   *          The name of the attribute (inside the struct)
   * @throws AttributeException
   *           If the supplied structName isn't really a struct
   */
  public void clearStructItems(String structName, String attributeName)
  {
    // Setting and validating are a single method
    AttributeIF attribute = this.getAttributeIF(structName);
    if (attribute instanceof AttributeStruct)
    {
      ( (AttributeStruct) attribute ).clearItems(attributeName);
    }
    else
    {
      String error = "Attribute [" + structName + "] is not a struct attribute";
      throw new AttributeException(error);
    }
  }

  /**
   * Deletes the EntityDAO.
   * 
   * <br/>
   * <b>Precondition:</b> isNew == false
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  public void delete(boolean businessContext)
  {
    if (!this.isAppliedToDB())
    {
      String error = "Object [" + this.getId() + "] has not been applied: " + "instances that have not been applied to the database cannot be deleted.";
      throw new DeleteUnappliedObjectException(error, this);
    }

    // clear any references of all attributes on this object that are still
    // defined
    List<? extends MdAttributeConcreteDAOIF> definedAttributes = this.getMdClassDAO().getAllDefinedMdAttributes();
    for (MdAttributeConcreteDAOIF m : definedAttributes)
    {
      // An attribute may have been deleted from this type after this object was
      // instantiated,
      String attributeName = m.definesAttribute();
      if (this.hasAttribute(attributeName))
      {
        this.getAttribute(attributeName).removeReferences(this, businessContext);
      }
    }

    // Delete the records in the database.
    EntityDAOFactory.delete(this);
  }

  /**
   *
   */
  private void uniquenessTest()
  {
    if (Database.manuallyCheckForDuplicates())
    {
      for (Attribute attribute : this.getAttributeArray())
      {
        MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttribute();
        if (! ( mdAttribute instanceof MdAttributeBlobDAOIF ) && ! ( mdAttribute instanceof MdAttributeHashDAOIF ) && ! ( mdAttribute instanceof MdAttributeSymmetricDAOIF ))
        {
          attribute.validateUnique(attribute.getValue(), mdAttribute);
        }
      }
    }
  }

  /**
   * Checks if a EntityDAO already exists that has a set of attribute value
   * pairs that match the given EntityDAO. The attribute metadata defines the
   * set of attributes that are included in the duplicate test. Returns true if
   * a duplicate exists, false otherwise
   */
  private void uniqueAttributeGroupTest()
  {
    if (Database.manuallyCheckForDuplicates())
    {
      List<? extends MdEntityDAOIF> superMdEntitiesList = this.getMdClassDAO().getSuperClasses();

      for (MdEntityDAOIF superMdEntity : superMdEntitiesList)
      {
        List<MdIndexDAOIF> uniqueMdIndexIFList = superMdEntity.getUniqueIndexes();
        if (uniqueMdIndexIFList.size() > 0)
        {
          this.checkDuplicateAttributes(superMdEntity, uniqueMdIndexIFList);
        }
      }
    }
  }

  /**
   * Checks if a set of attribute value pairs for an instance of the given class
   * matches this object.
   * 
   * @param mdEntity
   * @param uniqueMdIndexIFList
   */
  private void checkDuplicateAttributes(MdEntityDAOIF mdEntity, List<MdIndexDAOIF> mdIndexIFList)
  {
    for (MdIndexDAOIF mdIndexIF : mdIndexIFList)
    {
      if (!mdIndexIF.isActive())
      {
        continue;
      }

      List<AttributeIF> attributeIFList = new LinkedList<AttributeIF>();
      List<String> valuesList = new LinkedList<String>();

      List<MdAttributeConcreteDAOIF> uniqueMdAttributeIFList = mdIndexIF.getIndexedAttributes();

      QueryFactory queryFactory = new QueryFactory();
      EntityQuery entityQuery = queryFactory.entityQuery(this);
      for (int i = 0; i < uniqueMdAttributeIFList.size(); i++)
      {
        MdAttributeConcreteDAOIF mdAttribute = uniqueMdAttributeIFList.get(i);

        AttributeIF attributeIF = this.getAttributeIF(mdAttribute.definesAttribute());
        attributeIFList.add(attributeIF);
        valuesList.add(attributeIF.getValue());

        entityQuery.WHERE(entityQuery.get(mdAttribute.definesAttribute()).EQ(attributeIF.getValue()));
      }

      // Exclude this object from the database
      entityQuery.WHERE(entityQuery.aCharacter(EntityInfo.ID).NE(this.getId()));

      if (entityQuery.getCount() > 0)
      {
        String error = "Duplicate item violation.  Instance [" + this.getId() + "] contains the same values for certain fields: ";

        for (int i = 0; i < uniqueMdAttributeIFList.size(); i++)
        {
          if (i != 0)
          {
            error += ", ";
          }
          MdAttributeConcreteDAOIF mdAttribute = uniqueMdAttributeIFList.get(i);
          error += "[" + mdAttribute.definesAttribute() + "]";
        }

        throw new DuplicateDataException(error, mdEntity, attributeIFList, valuesList);
      }
    }
  }

  /**
   * Sets the given attribute to an empty string for all attributes of the given
   * entity type that contain the given value.
   * 
   * <br/>
   * <b>Precondition:</b> mdEntityIF != null
   * 
   * <br/>
   * <b>Precondition:</b> mdAttributeRO != null
   * 
   * <br/>
   * <b>Precondition:</b> value != null <br/>
   * <b>Precondition:</b> !value().equals("")
   * 
   * @param mdEntityIF
   * @param attrName
   *          Name of the attribute to clear
   * @param value
   *          Attributes of the given name with this value are cleared
   */
  protected static void clearAttributeValues(MdEntityDAOIF mdEntityIF, MdAttributeConcreteDAOIF mdAttributeIF, String value)
  {
    EntityQuery entityQ = mdEntityIF.getEntityQuery();
    QueryFactory qf = entityQ.getQueryFactory();
    ValueQuery q = qf.valueQuery();

    q.SELECT(entityQ.id("entityId"));
    q.WHERE(entityQ.get(mdAttributeIF.definesAttribute()).EQ(value));

    OIterator<ValueObject> i = q.getIterator();

    List<String> columnNames = new LinkedList<String>();
    List<String> prepStmtVars = new LinkedList<String>();
    List<Object> values = new LinkedList<Object>();
    List<String> attributeTypes = new LinkedList<String>();

    List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();

    while (i.hasNext())
    {
      String entityId = i.next().getValue("entityId");

      TransactionCacheIF cache = TransactionCache.getCurrentTransactionCache();
      EntityDAO entityDAO = (EntityDAO) cache.getEntityDAO(entityId);

      // check to see if the object is in the global cache, if so, use that
      // object.
      if (entityDAO == null)
      {
        EntityDAOIF globalCachedObject = ObjectCache.getEntityDAOIFfromCache(entityId);
        if (globalCachedObject != null)
        {
          entityDAO = globalCachedObject.getEntityDAO();
        }
      }

      // If the attribute is required, then we cannot remove it.
      if (mdAttributeIF.isRequired())
      {
        String error = "Cannot clear attribute [" + mdAttributeIF.definesAttribute() + "] on type [" + mdEntityIF.definesType() + "] -  it is required";

        throw new AttributeValueException(error, "");
      }

      columnNames.add(mdAttributeIF.getColumnName());
      prepStmtVars.add(Attribute.getPreparedStatementVariable());
      values.add("");
      attributeTypes.add(mdAttributeIF.getType());

      if (mdEntityIF instanceof MdElementDAOIF)
      {
        MdElementDAOIF mdElementDAOIF = (MdElementDAOIF) mdEntityIF;
        MdAttributeConcreteDAOIF seqMdAttr = mdElementDAOIF.getAllDefinedMdAttributeMap().get(ElementInfo.SEQUENCE.toLowerCase());
        MdElementDAOIF seqMdElementDAOIF = (MdElementDAOIF) seqMdAttr.definedByClass();

        String nextSequence = Database.getNextSequenceNumber();

        if (entityDAO != null)
        {
          entityDAO.getAttribute(ElementInfo.SEQUENCE).setValue(nextSequence);
        }

        // Column being cleared and sequence column are defined in the same
        // table.
        if (mdElementDAOIF.getTableName().equals(seqMdElementDAOIF.getTableName()))
        {
          columnNames.add(seqMdAttr.getColumnName());
          prepStmtVars.add(Attribute.getPreparedStatementVariable());
          values.add(nextSequence);
          attributeTypes.add(seqMdAttr.getType());
        }
        // Sequence column is defined on a separate table
        else
        {
          List<String> seqNumColumnNames = new LinkedList<String>();
          List<String> seqNumPrepStmtVars = new LinkedList<String>();
          List<Object> seqNumValues = new LinkedList<Object>();
          List<String> seqNumAttributeTypes = new LinkedList<String>();

          seqNumColumnNames.add(seqMdAttr.getColumnName());
          seqNumPrepStmtVars.add(Attribute.getPreparedStatementVariable());
          seqNumValues.add(nextSequence);
          seqNumAttributeTypes.add(seqMdAttr.getType());

          PreparedStatement seqPreparedStmt = Database.buildPreparedSQLUpdateStatement(seqMdElementDAOIF.getTableName(), seqNumColumnNames, seqNumPrepStmtVars, seqNumValues, seqNumAttributeTypes, entityId);
          preparedStatementList.add(seqPreparedStmt);
        }
      }

      PreparedStatement preparedStmt = Database.buildPreparedSQLUpdateStatement(mdEntityIF.getTableName(), columnNames, prepStmtVars, values, attributeTypes, entityId);
      preparedStatementList.add(preparedStmt);

      Database.executeStatementBatch(preparedStatementList);

      // Mark the object to be removed from the global cache so that it can be
      // refreshed on the next request.
      if (entityDAO != null)
      {
        entityDAO.getAttribute(mdAttributeIF.definesAttribute()).setValue("");
        cache.updateEntityDAO(entityDAO);
      }
      else
      {
        cache.refreshEntityInGlobalCache(entityId);
      }

      columnNames.clear();
      prepStmtVars.clear();
      values.clear();
      attributeTypes.clear();

      preparedStatementList.clear();
    }
  }

  /**
   * Returns a EntityDAO of the given id in the database.
   * 
   * <br/>
   * <b>Precondition:</b> id != null <br/>
   * <b>Precondition:</b> !id.trim().equals("") <br/>
   * <b>Precondition:</b> given id represents a valid item in the database <br/>
   * <b>Postcondition:</b> return value may not be null <br/>
   * <b>Postcondition:</b> EntityDAO representing the item in the database of
   * the given id is returned
   * 
   * @param id
   *          element id of an item in the database
   * @return EntityDAO instance of the given id, of the given type
   */
  public static EntityDAOIF get(String id)
  {
    return ObjectCache.getEntityDAO(id);
  }

  /**
   * Returns a EntityDAOIF of the given type with the given key in the database.
   * 
   * <br/>
   * <b>Precondition:</b> key != null <br/>
   * <b>Precondition:</b> !key.trim().equals("") <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Postcondition:</b> EntityDAOIF representing the item in the database of
   * the given key and type is returned
   * 
   * @param type
   *          fully qualified type of an item in the database
   * @param key
   *          key of an item in the database
   * 
   * @return EntityDAOIF instance of the given type and key
   */
  public static EntityDAOIF get(String type, String key)
  {
    return (EntityDAOIF) ObjectCache.getEntityDAO(type, key);
  }

  /**
   * Returns a id of the entity of the given type with the given key in the
   * database.
   * 
   * <br/>
   * <b>Precondition:</b> key != null <br/>
   * <b>Precondition:</b> !key.trim().equals("") <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Postcondition:</b> BusinessDAO representing the item in the database of
   * the given key and type is returned
   * 
   * @param type
   *          fully qualified type of an item in the database
   * @param key
   *          key of an item in the database
   * 
   * @return EntityDAOIF instance of the given type and key
   */
  public static String getIdFromKey(String type, String key)
  {
    MdEntityDAOIF mdEntityDAOIF = MdEntityDAO.getMdEntityDAO(type);

    QueryFactory qf = new QueryFactory();
    EntityQuery entityQueryDAO = qf.entityQueryDAO(mdEntityDAOIF);

    ValueQuery vq = new ValueQuery(qf);

    vq.SELECT(entityQueryDAO.id(EntityInfo.ID));
    vq.WHERE(entityQueryDAO.aCharacter(EntityInfo.KEY).EQ(key));

    OIterator<ValueObject> i = vq.getIterator();

    try
    {
      if (i.hasNext())
      {
        return i.next().getValue(EntityInfo.ID);
      }
      else
      {
        String errMsg = "Unable to find id for object of type [" + type + "] with " + EntityInfo.KEY + " [" + key + "]";
        throw new DataNotFoundException(errMsg, mdEntityDAOIF);
      }
    }
    finally
    {
      i.close();
    }
  }

  /**
   * Returns a List Strings for all IDs of EntityDAOs of the given entity type
   * or that are sub entities. CAUTION!!!! This stores all Ids in a List. If the
   * entity type has too many instances, this can eat up your memory in a hurry.
   * 
   * <br/>
   * <b>Precondition: </b> mdEntity != null
   * 
   * @param mdEntity
   * @return List Strings for all IDs of EntityDAOs of the given entity type or
   *         that are sub entities.
   */
  public static List<String> getEntityIdsFromDB(MdEntityDAOIF mdEntity)
  {
    return EntityDAOFactory.getEntityIds(mdEntity);
  }

  /**
   * Returns a List of Strings for all IDs of EntityDAOs of the given type or
   * that are sub entities. Gets the table name for the entity by queriying the
   * database.
   * 
   * <br/>
   * <b>Precondition: </b> type != null <br/>
   * <b>Precondition: </b> !type().equals("")
   * 
   * @param type
   * @return List of Strings for all IDs of EntityDAOs of the given type or that
   *         are sub entities. Gets the table name for the entity by queriying
   *         the database.
   */
  public static List<String> getEntityIdsDB(String type)
  {
    return EntityDAOFactory.getEntityIds(type);
  }

  /**
   * Returns an iterator of all EntityDAOs cached in the collection.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return Iterator of all EntityDAOs cached in the collection
   */
  public static List<? extends EntityDAOIF> getCachedEntityDAOs(String type)
  {
    return ObjectCache.getCachedEntityDAOs(type);
  }

  /**
   * Returns true if this instance has been written to the database. It does not
   * indicate if it has been committed to the database.
   * 
   * @return true if this instance has been written to the database. It does not
   *         indicate if it has been committed to the database.
   */
  public boolean isAppliedToDB()
  {
    return this.getObjectState().isAppliedToDB();
  }

  /**
   * If true then this instance has been written to the database.
   * 
   * @param appliedToDB
   */
  public void setAppliedToDB(boolean appliedToDB)
  {
    this.getObjectState().setAppliedToDB(appliedToDB);
  }

  /**
   * Indicates whether the delete method has completed execution and the object
   * has been deleted;
   */
  public boolean isDeleted()
  {
    return this.isDeleted;
  }

  /**
   * Sets whether the object has been deleted or not. This is set by an aspect.
   * 
   * @param _isDeleted
   */
  protected void setIsDeleted(boolean _isDeleted)
  {
    this.isDeleted = _isDeleted;
  }

  /**
   * Returns the MdEntityIF defining this EntityDAO.
   */
  public MdEntityDAOIF getMdClassDAO()
  {
    return MdEntityDAO.getMdEntityDAO(this.getType());
  }

  /**
   * Returns true if the entity has an owner, false otherwise.
   * 
   * @return if the entity has an owner, false otherwise.
   */
  public boolean hasOwner()
  {
    if (this.getAttribute(ElementInfo.OWNER).getValue().trim().equals(""))
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  /**
   * Validates a site to ensure this domain is the domain that created this
   * object
   */
  protected void validateSite()
  {
    // Do not enforce the site master if this object is imported
    if (!this.isImport() && !this.isImportResolution() && this.getMdClassDAO().getEnforceSiteMaster() && !this.isMasteredHere() && !ServerProperties.getIgnoreSiteMaster())
    {
      // If only the system attributes are modified, then no user is directly
      // trying to modify the object.
      if (!this.onlySystemAttributesAreModified())
      {
        String currentDomain = CommonProperties.getDomain();

        String msg = "Only the create site can update an object.  Object's site: [" + this.getSiteMaster() + "].  This site: [" + currentDomain + "]";
        throw new SiteException(msg, this, currentDomain);
      }
    }
  }

  /**
   * Returns true if the this object is mastered on this node, false otherwise.
   * 
   * @return true if the this object is mastered on this node, false otherwise.
   */
  public boolean isMasteredHere()
  {
    return isMasteredHere(this);
  }

  /**
   * Assuming attributes have been modified and the key, return true if only
   * system attributes have been modified, false otherwise. Returns true if no
   * attributes are modified.
   * 
   * @return Assuming attributes have been modified and the key, return true if
   *         only system attributes have been modified, false otherwise.
   */
  public boolean onlySystemAttributesAreModified()
  {
    boolean onlySystemModified = true;
    for (Attribute attribute : this.getAttributeArray())
    {
      if (attribute.isModified() && !attribute.getMdAttribute().isSystem() && !attribute.getName().equals(EntityInfo.KEY))
      {
        onlySystemModified = false;
        break;
      }
    }

    return onlySystemModified;
  }

  /**
   * Returns true if the given object is mastered on this node, false otherwise.
   * 
   * @return true if the given object is mastered on this node, false otherwise.
   */
  protected static boolean isMasteredHere(EntityDAOIF entityDAOIF)
  {
    if (ServerProperties.getIgnoreSiteMaster())
    {
      return true;
    }
    else
    {
      String currentDomain = CommonProperties.getDomain();
      String createDomain = entityDAOIF.getSiteMaster();

      // This cast is OK. We are not modifying the object.
      if (!currentDomain.equals(createDomain))
      {
        return false;
      }
      else
      {
        return true;
      }
    }
  }

  /**
   * Facade method for accessing an encapsulated variable
   * 
   * @param entityDAOIF
   * @return
   */
  public static String getOldId(EntityDAOIF entityDAOIF)
  {
    return ( (EntityDAO) entityDAOIF ).getOldId();
  }

  /**
   * @return the disconnected
   */
  public boolean isDisconnected()
  {
    return this.disconnected;
  }

  /**
   * @param _disconnected
   *          the disconnected to set
   */
  public void setDisconnected(boolean _disconnected)
  {
    this.disconnected = _disconnected;
  }
}
