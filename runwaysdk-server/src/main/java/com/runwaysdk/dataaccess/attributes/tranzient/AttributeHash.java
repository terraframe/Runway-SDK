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

import com.runwaysdk.dataaccess.AttributeHashIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;

public class AttributeHash extends AttributeEncryption implements AttributeHashIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 4371074567601249932L;

  /**
   * @param name
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType
   */
  protected AttributeHash(String name, String mdAttributeKey, String definingTransientType)
  {
    super(name, mdAttributeKey, definingTransientType);
  }

  /**
   * @param name
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType
   * @param value
   */
  protected AttributeHash(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    super(name, mdAttributeKey, definingTransientType, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined 
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the 
   * concrete attribute it references is returned.
   * 
   * @return MdAttributeHashDAOIF that defines the this attribute
   */
  public MdAttributeHashDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeHashDAOIF)super.getMdAttributeConcrete();
  }
  

  /**
   * @see com.runwaysdk.dataaccess.attributes.entity.AttributeEncryption#encryptionEquals(String, boolean)
   */
  public boolean encryptionEquals(String compareValue, boolean alreadyEncrypted)
  {
    return com.runwaysdk.dataaccess.attributes.entity.AttributeHash.encryptionEquals(this, compareValue, alreadyEncrypted);
  }

  /**
   * Sets the value of this attribute by hashing it, then calling
   * super.setValue() to complete the process.
   * 
   * @param value
   */
  public void setValue(String value)
  {
    String hash = com.runwaysdk.dataaccess.attributes.entity.AttributeHash.digest(this, value);
    com.runwaysdk.dataaccess.attributes.entity.AttributeHash.validateHashSize(this, hash);
    super.setValue(hash);
  }
}
