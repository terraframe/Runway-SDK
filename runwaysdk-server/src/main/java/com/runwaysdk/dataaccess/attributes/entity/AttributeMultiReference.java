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
/**
 * 
 */
package com.runwaysdk.dataaccess.attributes.entity;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.dataaccess.AttributeMultiReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.attributes.AttributeSet;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class AttributeMultiReference extends Attribute implements AttributeMultiReferenceIF, AttributeSet
{
  /**
   * 
   */
  private static final long serialVersionUID = 5952556989900694653L;

  private Set<String>       itemIdSet;

  /**
   * 
   * @param name
   * @param mdAttributeKey
   *          key of the defining attribute metadata
   * @param definingEntity
   * @param value
   */
  public AttributeMultiReference(String name, String mdAttributeKey, String definingEntity, String value)
  {
    super(name, mdAttributeKey, definingEntity, value);

    this.initSetId();

    this.itemIdSet = new TreeSet<String>();
  }

  /**
   * 
   * @param name
   * @param mdAttributeKey
   *          key of the defining attribute metadata
   * @param definingEntityType
   * @param value
   */
  public AttributeMultiReference(String name, String mdAttributeKey, String definingEntityType, String value, Set<String> itemIdSet)
  {
    super(name, mdAttributeKey, definingEntityType, value);

    this.initSetId();

    this.itemIdSet = itemIdSet;
  }

  /**
   * Initializes the setId, which is the value of this attribute.
   * 
   */
  private void initSetId()
  {
    if (this.value.trim().equals(""))
    {
      this.value = ServerIDGenerator.nextID();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.AttributeMultiReferenceIF#getItemIdList()
   */
  @Override
  public Set<String> getItemIdList()
  {
    this.refreshEnumIdList(true);

    return this.itemIdSet;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.AttributeMultiReferenceIF#getCachedItemIdSet()
   */
  @Override
  public Set<String> getCachedItemIdSet()
  {
    return this.itemIdSet;
  }

  /**
   * Refresh the local cache from the database;
   * 
   * @param useCache
   *          true if the cache has been initialized.
   */
  private void refreshEnumIdList(boolean useCache)
  {
    MdAttributeMultiReferenceDAOIF mdAttributeMultiReferenceIF = (MdAttributeMultiReferenceDAOIF) this.getMdAttribute();

    this.itemIdSet = Database.getEnumItemIds(mdAttributeMultiReferenceIF.getTableName(), this.getValue());
  }

  /**
   * Sets the multi reference to contain only <code>value</code>, regardless of
   * multiplicity or previous value.
   * 
   * @param value
   *          New value to be set for <b><code>this</b></code>
   * @return <code>true</code> if the value is set successfully
   * @throws DataAccessException
   */
  public synchronized void setValue(String value)
  {
    if (itemIdSet.size() == 1 && itemIdSet.contains(value))
    {
      return;
    }
    else
    {
      MdAttributeMultiReferenceDAOIF mdAttribute = this.getMdAttribute();

      this.validate(value, mdAttribute, true);

      if (!this.getContainingComponent().isNew())
      {
        // refresh the local id mapping cache
        this.refreshEnumIdList(true);
      }

      this.clearItems();

      itemIdSet.add(value);

      this.setModified(true);
    }
  }

  /**
   * Returns the BusinessDAO that defines the this attribute.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return BusinessDAO that defines the this attribute
   */
  public MdAttributeMultiReferenceDAOIF getMdAttribute()
  {
    return (MdAttributeMultiReferenceDAOIF) super.getMdAttribute();
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by aa concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return MdAttributeMultiReferenceDAOIF that defines the this attribute
   */
  public MdAttributeMultiReferenceDAOIF getMdAttributeConcrete()
  {
    return this.getMdAttribute();
  }

  /**
   * Sets the default value of the attribute, if any is defined. Basically this
   * skips the mutible attribute test, as that test will crash if called before
   * the containing object has been defined.
   * 
   * @param value
   *          New value to be set for <b><code>this</b></code>
   * @return <code>true</code> if the value is set successfully
   * @throws DataAccessException
   */
  public synchronized void setDefaultValue(String value)
  {
    MdAttributeMultiReferenceDAOIF mdAttribute = (MdAttributeMultiReferenceDAOIF) this.getMdAttribute();

    this.validate(value, mdAttribute, false);

    this.clearItems();

    itemIdSet.add(value);

    this.setModified(true);
  }

  /**
   * Adds an item to an multi reference atribute.
   * 
   * @param itemID
   *          The ID of the item to be added to the multi reference attribute
   * @return <code>true</code> if the item is successfully added
   */
  public synchronized boolean addItem(String itemID)
  {
    if (itemIdSet.contains(itemID))
    {
      return true;
    }

    MdAttributeMultiReferenceDAOIF mdAttribute = (MdAttributeMultiReferenceDAOIF) this.getMdAttribute();
    this.validate(itemID, mdAttribute, true);

    itemIdSet.add(itemID);

    this.setModified(true);
    return true;
  }

  /**
   * Replaces the items of a multi reference attribute.
   * 
   * @param itemIDs
   *          Collection of referenced business ids
   * 
   * @return Flag indicating if a modification to the existing enumerated item
   *         occured.
   */
  public synchronized boolean replaceItems(Collection<String> itemIDs)
  {
    MdAttributeMultiReferenceDAOIF mdAttribute = (MdAttributeMultiReferenceDAOIF) this.getMdAttribute();

    // Validate all of the items in the collection
    for (String enumItemID : itemIDs)
    {
      this.validate(enumItemID, mdAttribute, true);
    }

    boolean intersectModification = itemIdSet.retainAll(itemIDs);
    boolean addModification = itemIdSet.addAll(itemIDs);

    this.setModified(intersectModification || addModification);

    return ( intersectModification || addModification );
  }

  /**
   * Removes an item from a multi reference attribute
   * 
   * @param itemID
   *          The ID of the item to be removed from the enumerated attribute
   */
  public synchronized void removeItem(String itemID)
  {
    this.itemIdSet.remove(itemID);
    this.setModified(true);
  }

  /**
   * 
   */
  public synchronized void clearItems()
  {
    this.itemIdSet.clear();
    this.setModified(true);
  }

  /**
   *
   */
  public synchronized void validateRequired(String valueToValidate, MdAttributeConcreteDAOIF mdAttributeIF)
  {
    // make sure a referenced business has been mapped to this value if a value
    // is required
    if (mdAttributeIF.isRequired())
    {
      if (itemIdSet.size() == 0)
      {
        String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType() + "] requires a value";
        EmptyValueProblem problem = new EmptyValueProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
        problem.throwIt();
      }
    }
  }

  /**
   * This is used to validate the default value of the given {@link#
   * MdAttributeIF}. Runs validation tests that are common to all Attribute
   * classes, but not the required attribute test.
   * 
   * @param mdAttributeIF
   * @param valueToValidate
   *          the String value to be validated
   * @return true if the value is valid for all common tests
   * @throws AttributeException
   *           if the attribute is not valid.
   */
  public void validate(MdAttributeDAOIF mdAttributeIF, String valueToValidate)
  {
    super.validate(valueToValidate, mdAttributeIF);

    validate(valueToValidate, (MdAttributeMultiReferenceDAOIF) mdAttributeIF, false);
  }

  /**
   * <br>
   * <b>Precondition</b> this.getContainingComponent() != null && checkMutable
   * == true
   * 
   * @param itemID
   * @param mdAttribute
   * @return
   */
  private void validate(String itemID, MdAttributeMultiReferenceDAOIF mdAttribute, boolean checkMutable)
  {
    if (checkMutable)
    {
      EntityDAO containingEntity = (EntityDAO) this.getContainingComponent();
      if (containingEntity.isAppliedToDB())
      {
        this.validateMutable(mdAttribute);
      }
    }

    AttributeReference.validateReference(itemID, mdAttribute);
  }

  /**
   * 
   * @return
   */
  public synchronized BusinessDAOIF[] dereference()
  {
    BusinessDAOIF[] array = new BusinessDAOIF[this.itemIdSet.size()];
    Set<String> refreshedItemIdSet = new TreeSet<String>();
    int i = 0;

    for (String itemId : this.itemIdSet)
    {
      // structs cannot be used to define a multi reference
      try
      {
        if (itemId != null)
        {
          array[i++] = BusinessDAO.get((String) itemId);
          refreshedItemIdSet.add(itemId);
        }
        else
        {
          i++;
        }
      }
      catch (DataNotFoundException ex)
      {
        // Cached enums can still hold items that have been dropped. Ignore
        // items that aren't found.
        ex.printStackTrace();
      }
    }

    this.itemIdSet = refreshedItemIdSet;

    return array;
  }

  /**
   * Clears all referenced business items.
   * 
   */
  protected void reset()
  {
    MdAttributeMultiReferenceDAOIF mdAttribute = (MdAttributeMultiReferenceDAOIF) this.getMdAttribute();

    Database.deleteSetIdFromLinkTable(mdAttribute.getTableName(), this.getValue());
  }

  /**
   * Cleans up any object that references this attribute in some way. Removes
   * mappings between this attribute and any referenced business items it
   * pointed to. <b>Precondition: </b> this attribute MUST be a member of the
   * given EntityDAO <br>
   */
  public void removeReferences(EntityDAO enitityObject, boolean businessContext)
  {
    super.removeReferences(enitityObject, businessContext);

    this.reset();
  }

  /**
   * Create mapping in the database between this attribute and the referenced
   * business items it points to.
   * 
   * <b>Precondition: </b> this MdAttribute MUST be the metadata of this
   * attribute. <br>
   * <b>Precondition: </b> this.enumItemIdList has been refreshed and is not a
   * stale cache, meaning every item it contains represents a valid mapping to a
   * valid item.<br>
   */
  public synchronized void initReferences(MdAttributeConcreteDAOIF mdAttributeIF)
  {
    MdAttributeMultiReferenceDAOIF mdAttributeMultiReferenceIF = (MdAttributeMultiReferenceDAOIF) mdAttributeIF;

    // validate the local cache.
    Set<String> refreshedItemIdList = new TreeSet<String>();

    for (String itemId : this.itemIdSet)
    {
      // structs cannot be used to define an enumeration
      try
      {
        BusinessDAO.get(itemId);
        // Exception not thrown, meaning that the item has not been deleted.
        refreshedItemIdList.add(itemId);
      }
      catch (DataNotFoundException ex)
      {
        // Cached enums can still hold items that have been dropped. Ignore
        // items that aren't found.
      }
    }

    this.itemIdSet = refreshedItemIdList;

    List<String> sqlInsertStatements = new LinkedList<String>();

    for (String itemId : this.itemIdSet)
    {
      sqlInsertStatements.add(Database.buildAddItemStatement(mdAttributeMultiReferenceIF.getTableName(), this.getValue(), itemId));
    }

    Database.executeBatch(sqlInsertStatements);
  }

  /**
   * Updates the mapping in the database between this attribute and the
   * referenced business items it points to.
   * 
   * <b>Precondition: </b> this MdAttribute MUST be the metadata of this
   * attribute. <br>
   */
  public synchronized void updateReferences(MdAttributeConcreteDAOIF mdAttribute)
  {
    // Clear any existing enumeration item mappings
    this.reset();

    // Create the mappings with the enumeration items that this attribute
    // currently points to.
    this.initReferences(mdAttribute);
  }

  /**
   * Some attributes store objects instead of strings.
   * 
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue()
  {
    return this.getCachedItemIdSet();
  }

  /**
   * Returns a deep clone of this attribute.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a deep clone of this Attribute
   */
  public synchronized Attribute attributeClone()
  {
    Set<String> cloneSet = new TreeSet<String>(this.itemIdSet);

    return new AttributeMultiReference(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), new String(this.getRawValue()), cloneSet);
  }

  /**
   * Returns a deep clone of this attribute.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a deep clone of this Attribute
   */
  public synchronized Attribute attributeCopy()
  {
    Set<String> cloneSet = new TreeSet<String>(this.itemIdSet);

    return new AttributeMultiReference(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), ServerIDGenerator.nextID(), cloneSet);
  }
}