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
package com.runwaysdk.dataaccess.attributes.tranzient;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;

public class AttributeDate extends AttributeMoment
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 5668731195056505006L;

  /**
   * Inherited constrcutor, sets <code>name</code> and <code>definingTransientType</code>.
   * 
   * @param name The name of this time attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType The type that defines this attribute.
   */
  protected AttributeDate(String name, String mdAttributeKey, String definingTransientType)
  {
    super(name, mdAttributeKey, definingTransientType);
  }

  /**
   * Inherited constrcutor, sets <code>name</code>, <code>definingTransientType</code>, and
   * <code>value</code>.
   * 
   * @param name The name of this date attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType The name of the class that defines this attribute.
   * @param value The value of this date. "<code>true</code>" or "<code>false</code>"
   */
  protected AttributeDate(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    super(name, mdAttributeKey, definingTransientType, value);
    
    this.value = com.runwaysdk.dataaccess.attributes.entity.AttributeDate.formatSQLToJavaDate(this.value);
  }
  
  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined 
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the 
   * concrete attribute it references is returned.
   * 
   * @return MdAttributeDateDAOIF that defines the this attribute
   */
  public MdAttributeDateDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeDateDAOIF)super.getMdAttributeConcrete();
  }
  
  /**
   * Test if the input String is valid as a Date.
   * 
   * <br/><b>Precondition: </b> valueToValidate != null 
   * <br/><b>Postcondition: </b> true
   * 
   * @param valueToValidate the date attribute value to be validated
   * @param mdAttribute the defining Metadata object of the class that contains this
   *          Attribute
   */
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttribute)
  {
    super.validate(valueToValidate, mdAttribute);
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeDate.isValid(valueToValidate))
    {
      String error = "Value [" + valueToValidate + "] is invalid for attribute [" + getName()
          + "] on type [" + getDefiningClassType() + "]";
      throw new AttributeValueException(error, this, valueToValidate);
    }
  }
  
}
