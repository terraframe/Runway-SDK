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

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;

public class AttributeDouble extends AttributeNumber
{
  /**
   * 
   */
  private static final long serialVersionUID = -1203747036840710273L;


  /**
   * Inherited constructor, sets <code>name</code> and <code>definingTransientType</code>.
   * 
   * @param name The name of this double attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType the name of the class that defines this attribute.
   */
  protected AttributeDouble(String name, String mdAttributeKey, String definingTransientType)
  {
    super(name, mdAttributeKey, definingTransientType);
  }

  /**
   * Inherited constructor, sets <code>name</code>, <code>definingTransientType</code>, and
   * <code>value</code>.
   * 
   * @param name The name of this double attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType the name of the class that defines this attribute.
   * @param value The value of this double. "<code>true</code>" or "<code>false</code>"
   */
  protected AttributeDouble(String name, String mdAttributeKey, String definingTransientType, String value)
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
   * @return MdAttributeDoubleDAOIF that defines the this attribute
   */
  public MdAttributeDoubleDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeDoubleDAOIF)super.getMdAttributeConcrete();
  }
  
  /**
   * Returns the Java primitive type of the value.
   * 
   * @return the Java primitive type of the value.
   */
  public Double getTypeSafeValue()
  {
    if (this.getValue().trim().equals(""))
    {
      return null;
    }
    else
    {
      return new Double(this.getValue());
    }
  }
  
  /**
   * Test if the input String is a valid double.
   * 
   * <br/><b>Precondition: </b> valueToValidate != null 
   * <br/><b>Postcondition: </b> true
   * 
   * @param valueToValidate the primitive attribute value to be validated
   * @param mdAttributeIF the defining Metadata object of the class that contains this
   *          Attribute
   */
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttributeIF)
  {
    super.validate(valueToValidate, mdAttributeIF);
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeDouble.isValid(valueToValidate))
    {
      String error = "Value [" + valueToValidate + "] is invalid for attribute [" + getName()
          + "] on type [" + getDefiningClassType() + "]";
      throw new AttributeValueException(error, this, valueToValidate);
    }
  }

}
