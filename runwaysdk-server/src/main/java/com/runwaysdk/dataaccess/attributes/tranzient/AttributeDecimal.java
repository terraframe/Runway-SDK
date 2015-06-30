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
package com.runwaysdk.dataaccess.attributes.tranzient;

import com.runwaysdk.dataaccess.AttributeDecimalIF;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeDecimalCommonIF;

public class AttributeDecimal extends AttributeNumber implements AttributeDecimalIF, AttributeDecimalCommonIF
{
  /**
   *
   */
  private static final long serialVersionUID = -6930280459130796956L;

  /**
   * Display length of the entire number (ie, 3.14 has DisplayLength = 3).
   */
  private int displayLength;

  /**
   * The number of decimal digits to the right of the decimal (ie, 3.14 has DecimalDigits = 2).
   *
   */
  private int decimalDigits;

  /**
   * Inherited constrcutor, sets <code>name</code> and <code>definingTransientType</code>.
   *
   * @param name The name of this decimal attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType the name of the type that defines this attribute.
   */
  protected AttributeDecimal(String name, String mdAttributeKey, String definingTransientType)
  {
    super(name, mdAttributeKey, definingTransientType);

    this.displayLength = 0;
    this.decimalDigits = 0;
  }

  /**
   * Inherited constrcutor, sets <code>name</code>, <code>definingTransientType</code>, and
   * <code>value</code>.
   *
   * @param name The name of this decimal attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType the name of the class that defines this attribute.
   * @param value The value of this decimal. "<code>true</code>" or "<code>false</code>"
   */
  protected AttributeDecimal(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    super(name, mdAttributeKey, definingTransientType, value);

    // Oracle seems to prepend some numbers with a black space
    this.value = this.value.trim();
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the
   * concrete attribute it references is returned.
   *
   * @return MdAttributeNumberDAOIF that defines the this attribute
   */
  public MdAttributeDecimalDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeDecimalDAOIF)super.getMdAttributeConcrete();
  }

  /**
   * Checks the value, and sets it if it is valid.
   *
   * <br>
   * <b>Precondition: </b> value != null <br>
   * <b>Postcondition: </b> this.value = value this.value.equals(value)
   * this.validate(this.value) = <b>true </b>
   *
   * <br>
   * <b>modifies: </b> isModified() returns true, but only if the new value is
   *                   different from the old value
   *
   * @param value new value of the attribute
   *
   * @throws DataAccessException if the value is invalid for this Attribute
   */
  public void setValue(String newValue)
  {
    super.setValue(newValue);

    this.value = com.runwaysdk.dataaccess.attributes.entity.AttributeDecimal.setDecimalAndLength(this);
  }

  /**
   * Test if the input String is valid as a value in an AttributeDecimal. If the
   * value has too many decimal places, it will still pass on the assumption that
   * the value will be rounded before storage
   *
   * <br/><b>Precondition: </b> valueToValidate != null
   * <br/><b>Postcondition:</b> true
   *
   * @param valueToValidate value to validate.
   * @param mdAttribute the defining Metadata object of the class that contains this
   *          Attribute
   */
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttribute)
  {
    super.validate(valueToValidate, mdAttribute);

    MdAttributeDecimalDAOIF mdAttributeDecimal = (MdAttributeDecimalDAOIF) mdAttribute.getMdAttributeConcrete();

    com.runwaysdk.dataaccess.attributes.entity.AttributeDecimal.isValid(valueToValidate, mdAttributeDecimal);
  }

  /**
   * Returns the DisplayLength
   */
  public int getDisplayLength()
  {
    return this.displayLength;
  }

  /**
   * Returns the DisplayLength
   */
  public void setDisplayLength(int displayLength)
  {
    this.displayLength = displayLength;
  }

  /**
   * Returns the decimalDigits
   */
  public int getDecimalDigits()
  {
    return this.decimalDigits;
  }

  /**
   * Returns the decimalDigits
   */
  public void setDecimalDigits(int decimalDigits)
  {
    this.decimalDigits = decimalDigits;
  }

}
