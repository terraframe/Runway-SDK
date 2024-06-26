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
/*
 * Created January 12, 2006
 */
package com.runwaysdk.dataaccess.attributes.entity;

import java.math.BigDecimal;

import com.runwaysdk.constants.MdAttributeDecInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.dataaccess.AttributeDecimalIF;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeDecimalCommonIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;

public class AttributeDecimal extends AttributeNumber implements AttributeDecimalIF, AttributeDecimalCommonIF
{
  /**
   * Generated by Eclipse
   */
  private static final long serialVersionUID = -5815387552418410672L;

  /**
   * Display length of the entire number (ie, 3.14 has DisplayLength = 3).
   */
  private int               displayLength;

  /**
   * The number of decimal digits to the right of the decimal (ie, 3.14 has
   * DecimalDigits = 2).
   *
   */
  private int               decimalDigits;

  /**
   * Inherited constrcutor, sets <code>name</code> and
   * <code>definingEntityType</code>.
   *
   * @param name
   *          The name of this decimal attribute.
   * @param mdAttributeKey
   *          key of the defining attribute metadata.
   * @param definingEntityType
   *          the name of the type that defines this attribute.
   */
  protected AttributeDecimal(String name, String mdAttributeKey, String definingEntityType)
  {
    super(name, mdAttributeKey, definingEntityType);

    this.displayLength = 0;
    this.decimalDigits = 0;
  }

  /**
   * Inherited constrcutor, sets <code>name</code>,
   * <code>definingEntityType</code>, and <code>value</code>.
   *
   * @param name
   *          The name of this decimal attribute.
   * @param mdAttributeKey
   *          key of the defining attribute metadata.
   * @param definingEntityType
   *          the name of the class that defines this attribute.
   * @param value
   *          The value of this decimal. "<code>true</code>" or
   *          "<code>false</code>"
   */
  protected AttributeDecimal(String name, String mdAttributeKey, String definingEntityType, String value)
  {
    super(name, mdAttributeKey, definingEntityType, value);

    // Oracle seems to prepend some numbers with a black space
    this.value = this.value.trim();
  }

  /**
   * Some attributes store objects instead of strings.
   * 
   * @param name
   * @return object stored on the attribute.
   */
  public BigDecimal getTypeSafeValue()
  {
    if (this.getValue().trim().equals(""))
    {
      return null;
    }
    else
    {
      return new BigDecimal(this.getValue());
    }
  }

  @Override
  public void setValue(Object value)
  {
    if (value != null && value instanceof BigDecimal)
    {
      this.setValue(value.toString());
    }
    else
    {
      super.setValue(value);
    }
  }

  @Override
  public Object getObjectValue()
  {
    return this.getTypeSafeValue();
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
   * different from the old value
   *
   * @param value
   *          new value of the attribute
   *
   * @throws DataAccessException
   *           if the value is invalid for this Attribute
   */
  public void setValue(String newValue)
  {
    super.setValue(newValue);

    this.value = setDecimalAndLength(this);
  }

  public static String setDecimalAndLength(AttributeDecimalCommonIF attributeDecimalIF)
  {
    String value = attributeDecimalIF.getValue();
    int displayLength = attributeDecimalIF.getDisplayLength();
    int decimalDigits = attributeDecimalIF.getDecimalDigits();

    // set Display_Length and Decimal_Length
    if (attributeDecimalIF.getValue().contains("."))
    {
      displayLength = value.length() - 1;
      decimalDigits = displayLength - value.indexOf(".");
    }
    else
    {
      displayLength = value.length();
      decimalDigits = 0;
    }

    // if the value contains a minus sign, dock off an extra increment
    if (value.contains("-") || value.contains("+"))
    {
      displayLength--;
    }

    // Find out how many decimalDigits the database is using
    int databaseDecimal = Integer.parseInt(attributeDecimalIF.getMdAttributeConcrete().getValue(MdAttributeDecInfo.DECIMAL));

    // If the database has fewer digits than the current value, we need to round
    if (databaseDecimal < decimalDigits)
    {
      // Round the value
      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(databaseDecimal, BigDecimal.ROUND_HALF_UP);
      value = bd.toPlainString();

      // And fix the now-altered displayLength and decimalDigits variables
      displayLength -= ( decimalDigits - databaseDecimal );
      decimalDigits = databaseDecimal;
    }

    attributeDecimalIF.setDisplayLength(displayLength);
    attributeDecimalIF.setDecimalDigits(decimalDigits);

    return value;
  }

  /**
   * Test if the input String is valid as a value in an AttributeDecimal. If the
   * value has too many decimal places, it will still pass on the assumption
   * that the value will be rounded before storage
   *
   * <br/>
   * <b>Precondition: </b> valueToValidate != null <br/>
   * <b>Postcondition:</b> true
   *
   * @param valueToValidate
   *          value to validate.
   * @param mdAttribute
   *          the defining Metadata object of the class that contains this
   *          Attribute
   */
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttribute)
  {
    super.validate(valueToValidate, mdAttribute);

    if (!AttributeDecimal.isValid(valueToValidate, (MdAttributeDecimalDAOIF) mdAttribute))
    {
      String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType() + "] is invalid.";
      throw new AttributeValueException(error, this, valueToValidate);
    }

  }

  /**
   * Test if the input String is valid as a value in an AttributeDecimal. If the
   * value has too many decimal places, it will still pass on the assumption
   * that the value will be rounded before storage
   *
   * <br/>
   * <b>Precondition: </b> valueToValidate != null <br/>
   * <b>Postcondition:</b> true
   *
   * @param value
   *          value to validate.
   */
  public static boolean isValid(String value)
  {
    if (!value.trim().equals(""))
    {
      try
      {
        // make sure it's a valid number
        Double.parseDouble(value);

        return true;
      }
      catch (NumberFormatException e)
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Test if the input String is valid as a value in an AttributeDecimal. If the
   * value has too many decimal places, it will still pass on the assumption
   * that the value will be rounded before storage
   *
   * <br/>
   * <b>Precondition: </b> valueToValidate != null <br/>
   * <b>Postcondition:</b> true
   *
   * @param value
   *          value to validate.
   * @param mdAttribute
   *          TODO
   */
  public static boolean isValid(String value, MdAttributeDecimalDAOIF mdAttribute)
  {
    if (!value.trim().equals(""))
    {
      try
      {
        // make sure it's a valid number
        double d = Double.parseDouble(value);

        int length = Integer.parseInt(mdAttribute.getValue(MdAttributeDecimalInfo.LENGTH));
        int decimal = Integer.parseInt(mdAttribute.getValue(MdAttributeDecimalInfo.DECIMAL));

        // Calculate the number of digits in the double
        int digits = 1 + (int) ( Math.log(d) / Math.log(10) );

        // Ensure that the number of digits does not exceed its allotted amount.
        int len = length - decimal;

        return digits <= ( len == 0 ? 1 : len );
      }
      catch (NumberFormatException e)
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns the DisplayLength
   */
  public int getDisplayLength()
  {
    return displayLength;
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
    return decimalDigits;
  }

  /**
   * Returns the decimalDigits
   */
  public void setDecimalDigits(int decimalDigits)
  {
    this.decimalDigits = decimalDigits;
  }

  /**
   * Returns a deep clone of this attribute.
   *
   * <br/>
   * <b>Precondition: </b> true <br/>
   * <b>Postcondition: </b> true
   *
   * @return a deep clone of this Attribute
   */
  public AttributeDecimal attributeClone()
  {
    return new AttributeDecimal(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), this.getRawValue());
  }
}
