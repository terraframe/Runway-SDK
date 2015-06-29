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
package com.runwaysdk.dataaccess.attributes.tranzient;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeSet;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;

public class AttributeEnumeration extends Attribute implements AttributeEnumerationIF, AttributeSet
{
  /**
   *
   */
  private static final long serialVersionUID = -3714374725074431419L;

  private Set<String>       enumerationItemIdSet;

  /**
   * 
   * @param name
   * @param mdAttributeKey
   *          key of the defining metadata.
   * @param definingTransientType
   * @param value
   */
  public AttributeEnumeration(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    super(name, mdAttributeKey, definingTransientType, value);

    this.enumerationItemIdSet = new TreeSet<String>();
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by aa concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return MdAttributeEnumerationDAOIF that defines the this attribute
   */
  public MdAttributeEnumerationDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeEnumerationDAOIF) super.getMdAttributeConcrete();
  }

  public Set<String> getCachedEnumItemIdSet()
  {
    return this.enumerationItemIdSet;
  }

  public Set<String> getEnumItemIdList()
  {
    return this.getCachedEnumItemIdSet();
  }

  /**
   * 
   * @return
   */
  public EnumerationItemDAOIF[] dereference()
  {
    EnumerationItemDAOIF[] enumItemDAOArray = new EnumerationItemDAOIF[this.enumerationItemIdSet.size()];

    int loopCount = 0;
    for (String enumItemId : this.enumerationItemIdSet)
    {
      // structs cannot be used to define an enumeration
      enumItemDAOArray[loopCount++] = EnumerationItemDAO.get(enumItemId);
    }

    return enumItemDAOArray;
  }

  /**
   * Sets the enumeration to contain only <code>value</code>, regardless of
   * multiplicity or previous value.
   * 
   * @param enumItemId
   *          New value to be set for <b><code>this</b></code>
   * @return <code>true</code> if the value is set successfully
   * @throws DataAccessException
   */
  public void setValue(String enumItemId)
  {
    if (this.enumerationItemIdSet.contains(enumItemId))
    {
      return;
    }
    else
    {
      this.validate(value, this.getMdAttributeConcrete(), true);
      this.enumerationItemIdSet.add(enumItemId);

      this.setModified(true);
    }
  }

  /**
   * Sets the default value of the attribute, if any is defined. Basically this
   * skips the mutible attribute test, as that test will crash if called before
   * the containing object has been defined.
   * 
   * @param value
   *          New enumItemId to be set for <b><code>this</b></code>
   * @return <code>true</code> if the value is set successfully
   * @throws DataAccessException
   */
  public void setDefaultValue(String enumItemId)
  {
    MdAttributeEnumerationDAOIF mdAttributeConcrete = this.getMdAttributeConcrete();
    this.validate(enumItemId, mdAttributeConcrete, false);

    this.enumerationItemIdSet.add(enumItemId);
    this.setModified(true);
  }

  /**
   * Adds an item to an enumerated atribute. If the attribute does not allow
   * multiplicity, the <code>enumItemID</code> replaces the previous item.
   * 
   * @param enumItemId
   *          The ID of the item to be added to the enumerated attribute
   * @return <code>true</code> if the item is successfully added
   */
  public boolean addItem(String enumItemId)
  {
    if (this.enumerationItemIdSet.contains(enumItemId))
    {
      return true;
    }

    MdAttributeEnumerationDAOIF mdAttribute = this.getMdAttributeConcrete();
    this.validate(enumItemId, mdAttribute, true);

    if (!mdAttribute.selectMultiple())
    {
      this.enumerationItemIdSet.clear();
    }

    this.enumerationItemIdSet.add(enumItemId);

    this.setModified(true);
    return true;
  }

  /**
   * Removes an item from an enumerated attribute
   * 
   * @param enumItemID
   *          The ID of the item to be removed from the enumerated attribute
   */
  public void removeItem(String enumItemID)
  {
    this.enumerationItemIdSet.remove(enumItemID);
    this.setModified(true);
  }

  /**
   * Replaces the items of an enumerated attribute. If the attribute does not
   * allow multiplicity, then the {@code enumItemIDs} collection must contain
   * only one item.
   * 
   * @param name
   *          Name of the enumerated attribute
   * @param enumItemIDs
   *          Collection of enumerated item ids
   * 
   * @return Flag indicating if a modification to the existing enumerated item
   *         occured.
   */
  public synchronized boolean replaceItems(Collection<String> enumItemIDs)
  {
    MdAttributeEnumerationDAOIF mdAttribute = (MdAttributeEnumerationDAOIF) this.getMdAttributeConcrete();

    // Validate all of the items in the collection
    for (String enumItemID : enumItemIDs)
    {
      this.validate(enumItemID, mdAttribute, true);
    }

    if (!mdAttribute.selectMultiple() && enumItemIDs.size() != 1)
    {
      String error = "Unable to add multiple items for the select single enumerated Attribute [" + getName() + "] on type [" + getDefiningClassType() + "]";

      throw new DataAccessException(error);
    }

    boolean intersectModification = enumerationItemIdSet.retainAll(enumItemIDs);
    boolean addModification = enumerationItemIdSet.addAll(enumItemIDs);

    this.setModified(intersectModification || addModification);

    return ( intersectModification || addModification );
  }

  /**
   * 
   * @param enumItemID
   */
  public void clearItems()
  {
    this.enumerationItemIdSet.clear();
    this.setModified(true);
  }

  /**
   *
   */
  public void validateRequired(String valueToValidate, MdAttributeDAOIF mdAttributeIF)
  {
    // make sure an enumeration Item has been mapped to this value if a value is
    // required
    if (mdAttributeIF.isRequired())
    {
      if (enumerationItemIdSet.size() == 0)
      {
        String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType() + "] requires a value";
        EmptyValueProblem problem = new EmptyValueProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
        problem.throwIt();
      }
    }
  }

  /**
   * 
   * <br>
   * <b>Precondition</b> this.getCContainingComponent() != null && checkMutable
   * == true
   * 
   * @param enumItemID
   * @param mdAttributeConcrete
   * @return
   */
  private void validate(String enumItemID, MdAttributeEnumerationDAOIF mdAttributeConcrete, boolean checkMutable)
  {
    MdEnumerationDAOIF mdEnumeration = mdAttributeConcrete.getMdEnumerationDAO();

    if (checkMutable)
    {
      this.validateMutable(mdAttributeConcrete);
    }
    if (mdEnumeration != null)
    {
      if (!mdEnumeration.isValidEnumerationItem(enumItemID))
      {
        String error = "[" + enumItemID + "] is not a valid value for the enumerated Attribute [" + getName() + "] on type [" + getDefiningClassType() + "]";
        throw new AttributeValueException(error, this, enumItemID);
      }
    }
  }

}
