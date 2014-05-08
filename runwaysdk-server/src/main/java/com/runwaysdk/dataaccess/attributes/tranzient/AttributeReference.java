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

import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;

public class AttributeReference extends Attribute implements AttributeReferenceIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 5124794912584810096L;

  public AttributeReference(String name, String mdAttributeKey, String definingTransientType)
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
  public AttributeReference(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    super(name, mdAttributeKey, definingTransientType, value);
  }
  
  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined 
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the 
   * concrete attribute it references is returned.
   * 
   * @return MdAttributeReferenceDAOIF that defines the this attribute
   */
  public MdAttributeReferenceDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeReferenceDAOIF)super.getMdAttributeConcrete();
  }
  
  /**
   * 
   * @param mdAttribute the defining Metadata object of the class that contains this
   *          Attribute
   * @return boolean value representing the validity of the input
   */
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttribute)
  {
    super.validate(valueToValidate, mdAttribute);

    MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF)mdAttribute.getMdAttributeConcrete();

    com.runwaysdk.dataaccess.attributes.entity.AttributeReference.validateReference(valueToValidate, mdAttributeReference);
  }

  /**
   * 
   * @return
   */
  public BusinessDAOIF dereference()
  {
    MdAttributeReferenceDAOIF mdAttribute = this.getMdAttributeConcrete();

    return com.runwaysdk.dataaccess.attributes.entity.AttributeReference.dereference(this.getValue(), mdAttribute);
  }
  
}
