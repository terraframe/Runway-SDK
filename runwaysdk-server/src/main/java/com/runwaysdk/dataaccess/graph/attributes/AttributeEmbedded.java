/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.dataaccess.graph.attributes;

import java.util.Collection;
import java.util.Date;

import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEmbeddedDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeSet;
import com.runwaysdk.dataaccess.attributes.AttributeTypeException;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;

public class AttributeEmbedded extends Attribute
{
  /**
   * 
   */
  private static final long serialVersionUID = -4481544615179246050L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeEmbedded(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, ComponentDAO)
   */
  protected AttributeEmbedded(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, ComponentDAO value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeEmbeddedDAOIF} that defines the this attribute
   */
  public MdAttributeEmbeddedDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeEmbeddedDAOIF) super.getMdAttributeConcrete();
  }

  /**
   * @see AttributeIF#getObjectValue()
   */
  @Override
  public ComponentDAO getObjectValue()
  {
    return (ComponentDAO) super.getObjectValue();
  }

  @Override
  public String toString()
  {
    String stringValue = super.toString();

    stringValue += "\n-----------EMBEDDED OBJECT-------------\n";

    stringValue += this.getObjectValue().toString();

    return stringValue;
  }

  /**
   * 
   * precondition: this.componentDAO is initialized
   * 
   * @param attributeName
   * @param value
   */
  public void setValue(String attributeName, Object value)
  {
    this.getAttribute(attributeName).setValue(value);
  }

  /**
   * 
   * precondition: this.componentDAO is initialized
   * 
   * @param attributeName
   * @param value
   */
  public void setValue(String attributeName, Object value, Date startDate, Date endDate)
  {
    ValueOverTime vt = this.getValueOvertTime(startDate, endDate);

    if (vt != null)
    {
      GraphObjectDAO object = (GraphObjectDAO) vt.getValue();
      object.setValue(attributeName, value);
    }
    else
    {
      VertexObjectDAO object = VertexObjectDAO.newInstance((MdVertexDAOIF) this.getMdAttributeConcrete().getEmbeddedMdClassDAOIF());
      object.setValue(attributeName, value);

      this.setValue(object, startDate, endDate);
    }
  }

  /**
   * Adds an item to a set Attribute.
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
      String error = "Attribute [" + name + "] on struct [" + getName() + "] on type [" + getDefiningClassType() + "] is not a set attribute";
      throw new AttributeTypeException(error);
    }

    this.setModified(true);
  }

  /**
   * Replaces the items of a set attribute. If the attribute does not allow
   * multiplicity, then the {@code values} collection must contain only one
   * item.
   * 
   * @param name
   *          Name of the set attribute
   * @param values
   *          Collection of item ids
   * 
   */
  public void replaceItems(String name, Collection<String> values)
  {
    try
    {
      AttributeSet attrSet = (AttributeSet) this.getAttribute(name);
      boolean modified = attrSet.replaceItems(values);

      this.setModified(modified);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on struct [" + getName() + "] on type [" + getDefiningClassType() + "] is not a set attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Removes an item from a set Attribute.
   * 
   * @param name
   *          Name of the set attribute
   * @param value
   *          Value to be added to the attribute
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
      String error = "Attribute [" + name + "] on struct [" + getName() + "] on type [" + getDefiningClassType() + "] is not a set attribute";
      throw new AttributeTypeException(error);
    }

    this.setModified(true);
  }

  /**
   * Removes all items from a set Attribute.
   * 
   * @param name
   *          Name of the set attribute
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
      String error = "Attribute [" + name + "] on struct [" + getName() + "] on type [" + getDefiningClassType() + "] is not a set attribute";
      throw new AttributeTypeException(error);
    }

    this.setModified(true);
  }

  /**
   * 
   * precondition: this.componentDAO is initialized
   * 
   * @param attributeName
   * @return
   */
  public Attribute getAttribute(String attributeName)
  {
    // this may or may not be true, but there is no way to inform this attribute
    // if a client
    // modified a subattribute
    this.setModified(true);

    return (Attribute) this.getObjectValue().getAttributeIF(attributeName);
  }

  /**
   * 
   * precondition: this.componentDAO is initialized
   * 
   * @param attributeName
   * @return
   */
  public Object getValue(String attributeName)
  {
    return this.getAttribute(attributeName).getValue();
  }

  /**
   * 
   * precondition: this.componentDAO is initialized
   * 
   * @param attributeName
   * @param value
   */
  public Object getValue(String attributeName, Date date)
  {
    GraphObjectDAO object = (GraphObjectDAO) this.getObjectValue(date);

    if (object != null)
    {
      return object.getObjectValue(attributeName);
    }

    return null;
  }
}
