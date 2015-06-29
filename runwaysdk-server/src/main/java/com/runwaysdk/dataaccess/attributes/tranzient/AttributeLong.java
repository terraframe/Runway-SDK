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

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;

public class AttributeLong extends AttributeNumber
{
  /**
   * 
   */
  private static final long serialVersionUID = -351845587109726158L;

  /**
   * Inherited constrcutor, sets <code>name</code> and <code>definingTransientType</code>.
   * 
   * @param name The name of this long attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType Name of the class that defines this attribute.
   */
  protected AttributeLong(String name, String mdAttributeKey, String definingTransientType)
  {
    super(name, mdAttributeKey, definingTransientType);
  }

  /**
   * Inherited constrcutor, sets <code>name</code>, <code>definingTransientType</code>, and
   * <code>value</code>.
   * 
   * @param name The name of this long attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType Name of the class that defines this attribute.
   * @param value The value of this long. "<code>true</code>" or "<code>false</code>"
   */
  protected AttributeLong(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    super(name, mdAttributeKey, definingTransientType, value);
  }
  
  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined 
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the 
   * concrete attribute it references is returned.
   * 
   * @return MdAttributeLongDAOIF that defines the this attribute
   */
  public MdAttributeLongDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeLongDAOIF)super.getMdAttributeConcrete();
  }
  

  /**
   * <b>Precondition:</b> value is already a valid Long or is an empty string.
   * @return value is already a valid Long or is an empty string.
   */
  public Long getLongValue()
  {
    return com.runwaysdk.dataaccess.attributes.entity.AttributeLong.getLongValue(this.getValue());
  }
  
  /**
   * Test if the input String is a valid Long
   * 
   * <br/><b>Precondition: </b> valueToValidate != null 
   * <br/><b>Postcondition: </b> true
   * 
   * @param mdAttributeIF the defining Metadata object of the class that contains this
   *          Attribute
   */
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttributeIF)
  {
    super.validate(valueToValidate, mdAttributeIF);
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeLong.isValid(valueToValidate))
    {
      String error = "Value [" + valueToValidate + "] is invalid for attribute [" + getName()
          + "] on type [" + getDefiningClassType() + "]";
      throw new AttributeValueException(error, this, valueToValidate);
    }
  }
  
}
