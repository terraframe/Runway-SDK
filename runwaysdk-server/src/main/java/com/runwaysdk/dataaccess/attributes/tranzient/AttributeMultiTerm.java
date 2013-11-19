/**
 * 
 */
package com.runwaysdk.dataaccess.attributes.tranzient;

import com.runwaysdk.dataaccess.AttributeMultiTermIF;
import com.runwaysdk.dataaccess.MdAttributeMultiTermDAOIF;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class AttributeMultiTerm extends AttributeMultiReference implements AttributeMultiTermIF
{
  /**
   *
   */
  private static final long serialVersionUID = -3714374725074431419L;

  /**
   * 
   * @param name
   * @param mdAttributeKey
   *          key of the defining metadata.
   * @param definingTransientType
   * @param value
   */
  public AttributeMultiTerm(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    super(name, mdAttributeKey, definingTransientType, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by aa concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return MdAttributeMultiTermDAOIF that defines the this attribute
   */
  public MdAttributeMultiTermDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeMultiTermDAOIF) super.getMdAttributeConcrete();
  }

}
