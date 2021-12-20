/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.attributes.AttributeTypeException;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBlob;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;

public class AttributeStruct extends Attribute implements AttributeStructIF
{
  /**
   *
   */
  private static final long serialVersionUID = -844308892522386517L;

  private StructDAO structDAO;

  /**
   * @param name
   * @param mdAttributeKey key of the defining metadata.
   * @param definingType
   * @param value
   */
  public AttributeStruct(String name, String mdAttributeKey, String definingType, String value)
  {
     super(name, mdAttributeKey, definingType, value);
  }

  /**
   *
   * @param name
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType
   * @param value
   * @param structDAO
   */
  public AttributeStruct(String name, String mdAttributeKey, String definingTransientType, String value, StructDAO structDAO)
  {
     super(name, mdAttributeKey, definingTransientType, value);
     this.structDAO = structDAO;
  }

  /**
   * Always returns true if the containing component is new, false otherwise.  The oid to the
   * struct never changes after it has been persisted.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @return true if the containing component is new, false otherwise.  The oid to the
   * struct never changes after it has been persisted.
   */
  public boolean isModified()
  {
    if (this.getContainingComponent().isNew())
    {
      return true;
    }
    else
    {
      return this.getStructDAO().isModified();
    }
  }

  /**
   * Some attributes store objects instead of strings.
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue()
  {
    return this.structDAO;
  }

  /**
   * Returns the <code>MdStructDAOIF</code> that defines the type that this attribute references.
   *
   *  Preconditions:  this.structDAO has been initialized.
   *
   */
  public MdStructDAOIF getMdStructDAOIF()
  {
    return this.structDAO.getMdStructDAO();
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the
   * concrete attribute it references is returned.
   *
   * @return MdAttributeStructDAOIF that defines the this attribute
   */
  public MdAttributeStructDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeStructDAOIF)super.getMdAttributeConcrete();
  }

  /**
   *
   * @return
   */
  public StructDAO getStructDAO()
  {
    if (this.structDAO != null)
    {
      return this.structDAO;
    }
    else
    {
      String error = "StructDAO [" + this.getDefiningClassType()
          + "] was not initialized for the struct attribute [" + this.getName() + "]";
      throw new CoreException(error);
    }
  }

  /**
   *
   * @param structDAO
   */
  public void setStructDAO(StructDAO structDAO)
  {
    this.structDAO = structDAO;
    this.value = this.structDAO.getOid();
  }

  /**
   *
   * precondition: this.structDAO is initialized
   *
   * @param attributeName
   * @param value
   */
  public void setValue(String attributeName, String value)
  {
    // This cast is OK
    ((StructDAO)this.getStructDAO()).getAttribute(attributeName).setValue(value);

    this.setModified(true);
  }

  /**
   * precondition: this.structDAO is initialized
   *
   * @param blobName The name of the blob attribute
   * @param value The byte[] array to set
   */
  public void setBlob(String blobName, byte[] value)
  {
    try
    {
       AttributeBlob blob = (AttributeBlob)
         // This cast is OK
         ((StructDAO)this.getStructDAO()).getAttribute(blobName);
       blob.setBlobAsBytes(value);
    }
    catch(ClassCastException e)
    {
      String error = "Attribute [" + blobName + "] on struct [" + getName() + "] on type ["
      + getDefiningClassType() + "] is not a blob attribute";

      throw new AttributeTypeException(error);
    }

    this.setModified(true);
  }


  /**
   * precondition: this.structDAO is initialized
   *
   * @param blobName The name of the blob attribute
   * @return
   */
  public byte[] getBlob(String blobName)
  {
    try
    {
      AttributeBlob blob = (AttributeBlob)
        // This cast is OK
        ((StructDAO)this.getStructDAO()).getAttribute(blobName);
      return blob.getBlobAsBytes();
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + blobName + "] on struct [" + getName() + "] on type ["
          + getDefiningClassType() + "] is not a blob attribute";

      throw new AttributeTypeException(error);
    }
  }


  /**
   * Adds an item to an Enumerated Attribute.
   *
   * @param name
   *          Name of the enumerated attribute
   * @param value
   *          Value to be added to the attribute
   */
  public void addItem(String name, String value)
  {
    try
    {
      AttributeEnumeration attrEnum = (AttributeEnumeration)
        // This cast is OK
        ((StructDAO)this.getStructDAO()).getAttribute(name);
      attrEnum.addItem(value);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on struct [" + getName() + "] on type ["
          + getDefiningClassType() + "] is not an enumerated attribute";
      throw new AttributeTypeException(error);
    }

    this.setModified(true);
  }


  /**
   * Removes an item from an Enumerated Attribute.
   *
   * @param name
   *          Name of the enumerated attribute
   * @param value
   *          Value to be added to the attribute
   */
  public void removeItem(String name, String value)
  {
    try
    {
      AttributeEnumeration attrEnum = (AttributeEnumeration)
        // This cast is OK
        ((StructDAO)this.getStructDAO()).getAttribute(name);
      attrEnum.removeItem(value);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on struct [" + getName() + "] on type ["
          + getDefiningClassType() + "] is not an enumerated attribute";
      throw new AttributeTypeException(error);
    }

    this.setModified(true);
  }

  /**
   * Replaces the items of an enumerated attribute. If the attribute does not
   * allow multiplicity, then the {@code enumItemIDs} collection must contain
   * only one item.
   *
   * @param name
   *            Name of the enumerated attribute
   * @param values
   *            Collection of enumerated item ids
   *
   */
  public void replaceItems(String name, Collection<String> values)
  {
    try
    {
      AttributeEnumeration attrEnum = (AttributeEnumeration)this.getStructDAO().getAttribute(name);
      boolean modified = attrEnum.replaceItems(values);

      this.setModified(modified);
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on struct [" + getName() + "] on type ["
          + getDefiningClassType() + "] is not an enumerated attribute";
      throw new AttributeTypeException(error);
    }
  }

  /**
   * Removes all items from an Enumerated Attribute.
   *
   * @param name Name of the enumerated attribute
   */
  public void clearItems(String name)
  {
    try
    {
      AttributeEnumeration attrEnum = (AttributeEnumeration)
      // This cast is OK
      ((StructDAO)this.getStructDAO()).getAttribute(name);
      attrEnum.clearItems();
    }
    catch (ClassCastException e)
    {
      String error = "Attribute [" + name + "] on struct [" + getName() + "] on type ["
          + getDefiningClassType() + "] is not an enumerated attribute";
      throw new AttributeTypeException(error);
    }

    this.setModified(true);
  }

  /**
   *
   * precondition: this.structDAO is initialized
   *
   * @param attributeName
   * @return
   */
  protected AttributeIF getAttribute(String attributeName)
  {
    // this may or may not be true, but there is no way to inform this attribute if a client
    // modified a subattribute
    this.setModified(true);
    return this.getStructDAO().getAttributeIF(attributeName);
  }

  /**
   *
   * precondition: this.structDAO is initialized
   *
   * @param attributeName
   * @return
   */
  public String getValue(String attributeName)
  {
    // this may or may not be true, but there is no way to inform this attribute if a client
    // modified a subattribute
    this.setModified(true);
    return this.getStructDAO().getAttributeIF(attributeName).getValue();
  }

  /**
   *
   * precondition: this.structDAO is initialized
   *
   * @return
   */
  public AttributeIF[] getAttributeArrayIF()
  {
    return this.getStructDAO().getAttributeArrayIF();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.AttributeStructIF#getAttributeIF(java.lang.String)
   */
  public AttributeIF getAttributeIF(String attributeName)
  {
    return this.getStructDAO().getAttributeIF(attributeName);
  }
}
