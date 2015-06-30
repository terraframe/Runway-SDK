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
package com.runwaysdk.dataaccess.attributes.value;

import java.util.Set;

import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.attributes.AttributeTypeException;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBlob;

public class AttributeStruct extends Attribute implements AttributeStructIF
{
  /**
   *
   */
  private static final long serialVersionUID = 8822338833701526369L;

  private StructDAO structDAO;

  /**
   * Creates an attribute with the given name.
   *
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> value != null <br>
   * <b>Precondition: </b> definingEntityType != null <br>
   * <b>Precondition: </b> !definingEntityType().equals("") <br>
   * <b>Precondition: </b> definingEntityType is the name of a class that defines an attribute with
   * this name
   *
   * @param name name of the attribute
   * @param the value of the attribute
   * @param structDAO
   * @param definingEntityType name of the class that defines this attribute from which the value came
   * @param mdAttributeIF metadata that defines the attribute from which the value came.
   * @param entityMdAttributeIFset all MdAttributes that were involved in constructing this attribute.
   */
  protected AttributeStruct(String name, String value, String definingEntityType, MdAttributeConcreteDAOIF mdAttributeIF, Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset)
  {
    super(name, value, definingEntityType, mdAttributeIF, entityMdAttributeIFset);
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
          + this.getDefiningClassType() + "] is not a blob attribute";

      throw new AttributeTypeException(error);
    }
  }

  /**
   *
   * @param structDAO
   */
  public void setStructDAO(StructDAO structDAO)
  {
    this.structDAO = structDAO;
    this.value = this.structDAO.getId();
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
   * precondition: this.structDAO is initialized
   *
   * @param attributeName
   * @return
   */
  public String getValue(String attributeName)
  {
    return this.getStructDAO().getAttributeIF(attributeName).getValue();
  }

  /**
   * Builds a <code>com.runwaysdk.dataaccess.attributes.tranzient.Attribute</code> for a <code>TransientDAO</code> object.
   * @return <code>com.runwaysdk.dataaccess.attributes.tranzient.Attribute</code> for a <code>TransientDAO</code> object.
   */
  public com.runwaysdk.dataaccess.attributes.tranzient.Attribute buildTransientAttribute(String transientType)
  {
    com.runwaysdk.dataaccess.attributes.tranzient.AttributeStruct attributeStruct =
      (com.runwaysdk.dataaccess.attributes.tranzient.AttributeStruct)
      com.runwaysdk.dataaccess.attributes.tranzient.AttributeFactory.createAttribute(this.mdAttributeIF.getType(), this.mdAttributeIF.getKey(), this.getName(),
          transientType, this.getObjectValue());

    attributeStruct.setStructDAO(this.getStructDAO());

    return attributeStruct;
  }
}
