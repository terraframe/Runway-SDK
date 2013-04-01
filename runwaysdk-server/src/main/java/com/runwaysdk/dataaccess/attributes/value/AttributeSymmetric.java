/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.attributes.value;

import java.util.Set;

import javax.crypto.Cipher;

import com.runwaysdk.dataaccess.AttributeSymmetricIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;

public class AttributeSymmetric extends AttributeEncryption implements AttributeSymmetricIF
{
  /**
   *
   */
  private static final long serialVersionUID = -5162217824448179940L;

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
   * @param definingEntityType name of the class that defines this attribute from which the value came
   * @param mdAttributeIF metadata that defines the attribute from which the value came.
   * @param entityMdAttributeIFset all MdAttributes that were involved in constructing this attribute
   */
  protected AttributeSymmetric(String name, String value, String definingEntityType, MdAttributeConcreteDAOIF mdAttributeIF, Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset)
  {
    super(name, value, definingEntityType, mdAttributeIF, entityMdAttributeIFset);
  }

  /**
   * Returns the MdAttribute that defines the attribute from which the value came.
   *
   * @return MdAttribute that defines the attribute from which the value came.
   */
  public MdAttributeSymmetricDAOIF getMdAttribute()
  {
    return (MdAttributeSymmetricDAOIF)this.mdAttributeIF;
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the
   * concrete attribute it references is returned.
   *
   * @return MdAttributeSymmetricDAOIF that defines the this attribute
   */
  public MdAttributeSymmetricDAOIF getMdAttributeConcrete()
  {
    return this.getMdAttribute();
  }

  public String getValue()
  {
    String value = super.getValue();
    if (value.length() == 0)
      return value;
    else
      return decrypt(value);
  }

  /**
   * Encrypts the input value and returns the result.
   *
   * @param value
   * @return
   */
  public String encrypt(String value)
  {
    return com.runwaysdk.dataaccess.attributes.entity.AttributeSymmetric.cipher(this, value, Cipher.ENCRYPT_MODE);
  }

  /**
   * Decrypts the input value and returns the result.
   *
   * @param value
   * @return
   */
  public String decrypt(String value)
  {
    return com.runwaysdk.dataaccess.attributes.entity.AttributeSymmetric.cipher(this, value, Cipher.DECRYPT_MODE);
  }

  /**
   * Returns the decrypted value of this attribute using the secret key.
   */
  public String getDecryptedData()
  {
    //Must call this.getValue instead of super.getValue because
    //super.getValue returns the encrypted value instead of the
    //decrypted one.
    return this.getValue();
  }

  /**
   * @see com.runwaysdk.dataaccess.attributes.entity.AttributeEncryption#encryptionEquals(String,
   *      boolean)
   */
  public boolean encryptionEquals(String compareValue, boolean alreadyEncrypted)
  {
    if (alreadyEncrypted)
      compareValue = decrypt(compareValue);

    if (compareValue.equals(getValue()))
      return true;
    else
      return false;
  }

}
