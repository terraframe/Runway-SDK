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

import com.runwaysdk.dataaccess.AttributeEncryptionIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;

public abstract class AttributeEncryption extends Attribute implements AttributeEncryptionIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -3495914093166133161L;

  /**
   * 
   * @param name
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType
   */
  protected AttributeEncryption(String name, String mdAttributeKey, String definingTransientType)
  {
    super(name, mdAttributeKey, definingTransientType);
  }
  
  /**
   * 
   * @param name
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType
   * @param value
   */
  public AttributeEncryption(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    super(name, mdAttributeKey, definingTransientType, value);
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
    return (MdAttributeEncryptionDAOIF)super.getMdAttributeConcrete();
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
