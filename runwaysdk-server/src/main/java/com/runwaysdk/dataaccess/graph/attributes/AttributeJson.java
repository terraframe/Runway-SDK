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
package com.runwaysdk.dataaccess.graph.attributes;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeJsonDAOIF;

public class AttributeJson extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 356523685120439629L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeJson(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, String)
   */
  protected AttributeJson(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, String value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeJsonDAOIF} that defines the this attribute
   */
  public MdAttributeJsonDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeJsonDAOIF) super.getMdAttributeConcrete();
  }

  /**
   * @see Attribute#validateRequired(Object, MdAttributeDAOIF)
   * 
   *      <br>
   *      <b>Precondition: </b> value is of type String <br>
   *
   */
  @Override
  public void validateRequired(Object valueToValidate, MdAttributeDAOIF mdAttributeIF)
  {
    String stringValue = (String) valueToValidate;

    boolean blankValue = false;
    if (stringValue == null || stringValue.trim().equals(""))
    {
      blankValue = true;
    }

    // make sure a value is provided if a value is required
    if (mdAttributeIF.isRequired() && blankValue)
    {
      this.throwEmptyValueProblem(mdAttributeIF);
    }
  }

}
