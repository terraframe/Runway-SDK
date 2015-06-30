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
package com.runwaysdk.dataaccess.attributes.entity;

import com.runwaysdk.dataaccess.AttributeEncryptionIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;

public abstract class AttributeEncryption extends Attribute implements AttributeEncryptionIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -3163769833508346083L;

  /**
   * 
   * @param name
   * @param mdAttributeKey key of the defining attribute metadata
   * @param definingEntityType
   */
  protected AttributeEncryption(String name, String mdAttributeKey, String definingEntityType)
  {
    super(name, mdAttributeKey, definingEntityType);
  }
    
  /**
   * 
   * @param name
   * @param mdAttributeKey key of the defining attribute metadata
   * @param definingEntityType
   * @param value
   */
  public AttributeEncryption(String name, String mdAttributeKey, String definingEntityType, String value)
  {
    super(name, mdAttributeKey, definingEntityType, value);
  }
  
  /**
   * Returns the MdAttributeIF that defines the this attribute.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return MdAttributeIF that defines the this attribute
   */
  public MdAttributeEncryptionDAOIF getMdAttribute()
  {
    return (MdAttributeEncryptionDAOIF)super.getMdAttribute();
  }
  
  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined 
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the 
   * concrete attribute it references is returned.
   * 
   * @return MdAttributeEncryptionDAOIF that defines the this attribute
   */
  public MdAttributeEncryptionDAOIF getMdAttributeConcrete()
  {
    return this.getMdAttribute();
  }
  
  /**
   * Validation method that first does common validation, then does encryption specific size check.
   * @param valueToValidate
   * @param mdAttribute the defining Metadata object of the class that contains this
   *          Attribute
   * @return boolean value representing the validity of the input
   */
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttribute)
  {
    super.validate(valueToValidate, mdAttribute);
  }
}
