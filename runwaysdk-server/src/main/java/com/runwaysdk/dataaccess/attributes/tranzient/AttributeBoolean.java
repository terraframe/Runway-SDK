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

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;

public class AttributeBoolean extends Attribute implements AttributeBooleanIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 4647945632263107144L;

  /**
   * Inherited constructor, sets <code>name</code> and <code>definingTransientType</code>.
   * 
   * @param name The name of this boolean attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType The class that defines this attribute.
   */
  public AttributeBoolean(String name, String mdAttributeKey, String definingTransientType)
  {
    super(name, mdAttributeKey, definingTransientType);
  }

  /**
   * Inherited constrcutor, sets <code>name</code>, <code>definingTransientType</code>, and
   * <code>value</code>.
   * 
   * @param name The name of this boolean attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType The class that defines this attribute.
   * @param value The value of this boolean. "<code>true</code>" or "<code>false</code>"
   */
  public AttributeBoolean(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    super(name, mdAttributeKey, definingTransientType);

    if ( !( value.trim().length() == 0 ))
    {
      String formattedValue = com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean.formatAndValidate(value);
      this.value = com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean.formatAndValidate(formattedValue);
    }
  }
  
  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined 
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the 
   * concrete attribute it references is returned.
   * 
   * @return MdAttributeDAOIF that defines the this attribute
   */
  public MdAttributeBooleanDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeBooleanDAOIF)super.getMdAttributeConcrete();
  }
  
  /**
   * Tests the string value of the bolean, and returns a primitive
   * <code><b>boolean</b></code>.
   * 
   * @return <code><b>true</b></code> if the boolean attribute is MdAttributeBooleanIF.TRUE
   */
  public boolean isTrue()
  {  
    if (this.value.equals("1"))
    {
      return true;
    }
    return false;
  }

  /**
   * Tests the string value of the bolean, and returns a primitive
   * <code><b>boolean</b></code>.  Equivalent to <code>!value</code>.
   * 
   * @return <code><b>true</b></code> if the boolean attribute is MdAttributeBooleanIF.FALSE
   */
  public boolean isFalse()
  {
    if (this.value.equals("0"))
    {
      return false;
    }
    return true;
  }

  /**
   * Returns the Java primitive type of the value.
   * 
   * @return the Java primitive type of the value.
   */
  public Boolean getTypeSafeValue()
  {
    return this.getBooleanValue();
  }
  
  /**
   * Returns the boolean value of this attribute.
   * @return boolean value of this attribute.
   */
  public boolean getBooleanValue()
  {
    if (this.value.equals("1"))
    {
      return true;
    }
    return false;
  }

  /**
   *Returns 1 if the given attributeBoolean is true, 0.
   *
   * @return 1 if the given attributeBoolean is true, 0.
   */
  public int getBooleanValueInt()
  {
    return Integer.parseInt(this.value);
  }
  
  
  /**
   * Calls toLowerCase on the String before passing it back to the overridden abstract
   * parent method.
   * 
   * @see com.runwaysdk.dataaccess.attributes.entity.Attribute#setValue(java.lang.String)
   */
  public void setValue(String value)
  {
    super.setValue(com.runwaysdk.constants.MdAttributeBooleanUtil.format(value));
  }

  /**
   * @see com.runwaysdk.dataaccess.attributes.entity.Attribute#setValue(java.lang.String)
   */
  public void setValue(boolean value)
  {
    if (value)
    {
      this.setValue(MdAttributeBooleanInfo.TRUE);
    }
    else
    {
      this.setValue(MdAttributeBooleanInfo.FALSE);
    }
  }
  
  /**
   * Returns the formatted value of the attribute.  Some attributes format
   * this value to something other than what is stored in the database.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   * 
   * @return value of the attribute.
   */
  public String getValue()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.convertIntToString(this.value);
  }
  
  /**
   * Test if the input String is valid as a value in an AttributeBoolen (either MdAttributeBooleanIF.TRUE or
   * MdAttributeBooleanIF.FALSE).
   * 
   * <br/><b>Precondition: </b> valueToValidate != null 
   * <br/><b>Postcondition: </b> true
   * 
   * @param valueToValidate the primitive attribute value to be validated
   * @param mdAttribute the defining Metadata object of the class that contains this
   *          Attribute
   */
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttribute)
  {
    super.validate(valueToValidate, mdAttribute);
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean.validateValue(valueToValidate))
    {
      String error = "[" + valueToValidate + "] is an invalid value for the Boolean attribute ["
          + getName() + "] on type [" + getDefiningClassType() + "].";
      throw new AttributeValueException(error, this, value);
    }
  }  
  
  
}
