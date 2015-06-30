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

import java.security.NoSuchAlgorithmException;

import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.dataaccess.AttributeHashIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeLengthByteException;
import com.runwaysdk.dataaccess.attributes.EncryptionException;
import com.runwaysdk.util.EncryptionUtil;

public class AttributeHash extends AttributeEncryption implements AttributeHashIF
{
  private static final long serialVersionUID = 679479692549775299L;

  /**
   * @param name
   * @param mdAttributeKey
   *          key of the defining attribute metadata
   * @param definingEntityType
   */
  protected AttributeHash(String name, String mdAttributeKey, String definingEntityType)
  {
    super(name, mdAttributeKey, definingEntityType);
  }

  /**
   * @param name
   * @param mdAttributeKey
   *          key of the defining attribute metadata
   * @param definingEntityType
   * @param value
   */
  protected AttributeHash(String name, String mdAttributeKey, String definingEntityType, String value)
  {
    super(name, mdAttributeKey, definingEntityType, value);
  }

  /**
   * Returns the MdAttributeHashIF that defines the this attribute.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return MdAttributeHashIF that defines the this attribute
   */
  public MdAttributeHashDAOIF getMdAttribute()
  {
    return (MdAttributeHashDAOIF) super.getMdAttribute();
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by aa concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return MdAttributeHashDAOIF that defines the this attribute
   */
  public MdAttributeHashDAOIF getMdAttributeConcrete()
  {
    return this.getMdAttribute();
  }

  /**
   * @see com.runwaysdk.dataaccess.attributes.entity.AttributeEncryption#encryptionEquals(String,
   *      boolean)
   */
  public boolean encryptionEquals(String compareValue, boolean alreadyEncrypted)
  {
    return encryptionEquals(this, compareValue, alreadyEncrypted);
  }

  /**
   * @see com.runwaysdk.dataaccess.attributes.entity.AttributeEncryption#encryptionEquals(String,
   *      boolean)
   */
  public static boolean encryptionEquals(AttributeHashIF attributeHashIF, String compareValue, boolean alreadyEncrypted)
  {
    if (!alreadyEncrypted)
    {
      compareValue = digest(attributeHashIF, compareValue);
    }

    if (compareValue != null && compareValue.equals(attributeHashIF.getRawValue()))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Digests a message using a specified algorithm.
   * 
   * @param valueToHash
   * @return the hashed string
   */
  public static String digest(AttributeHashIF attributeHashIF, String valueToHash)
  {
    // find the correct hashing method
    String type = attributeHashIF.getMdAttributeConcrete().getEncryptionMethod();
    String hash = null;

    // hash the value according to the hashing method
    try
    {
      hash = EncryptionUtil.digestMethod(valueToHash, type);
    }
    catch (NoSuchAlgorithmException e)
    {
      String error = "Attribute [" + attributeHashIF.getName() + "] on [" + attributeHashIF.getDefiningClassType() + "] has an invalid hashing method.";
      throw new EncryptionException(error, e, attributeHashIF.getMdAttributeConcrete());
    }

    return hash;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.attributes.Attribute#importValue(java.lang
   * .String)
   * 
   * @param value The base 64 represenation of the hash value.
   */
  public void importValue(String value)
  {  
    // Do not hash, as the value is already hashed
    if (!this.value.equals(value))
    {
      this.value = value;
      this.setModified(true);
    }
  }

  /**
   * Sets the value of this attribute by hashing it, then calling
   * super.setValue() to complete the process.
   * 
   * @param value
   */
  public void setValue(String value)
  {
    // do not hash an empty string (keep the old value)
    if (value.length() != 0)
    {
      String hash = digest(this, value);
      validateHashSize(this, hash);
      super.setValue(hash);
    }
    else
    {
      super.setValue(value);
    }
  }

  /**
   * Returns true if the given value is different than the value of this
   * attribute.
   * 
   * @return true if the given value is different than the value of this
   *         attribute.
   */
  protected boolean valueIsDifferent(String _value)
  {
    return !_value.trim().equals("");
  }

  /**
   * Returns a clone of this attribute.
   */
  public Attribute attributeClone()
  {
    return new AttributeHash(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), new String(this.getRawValue()));
  }

  /**
   * Makes sure that the hashed value isn't too large for the database.
   * 
   * @param valueToValidate
   */
  public static void validateHashSize(AttributeHashIF attributeHashIF, String valueToValidate)
  {
    if (valueToValidate.length() > MdAttributeHashInfo.HASH_SIZE)
    {
      String error = "AttributeHash [" + attributeHashIF.getName() + "] is too long - it cannot be bigger than " + MdAttributeHashInfo.HASH_SIZE + " bytes.";
      throw new AttributeLengthByteException(error, attributeHashIF, MdAttributeHashInfo.HASH_SIZE);
    }
  }
}
