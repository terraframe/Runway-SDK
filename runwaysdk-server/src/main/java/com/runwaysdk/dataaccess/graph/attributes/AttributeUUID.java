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

import com.runwaysdk.AttributeUUIDParseException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;
import com.runwaysdk.session.Session;

public class AttributeUUID extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 7368938382761174978L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeUUID(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, String)
   */
  protected AttributeUUID(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, String value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeUUIDDAOIF} that defines the this attribute
   */
  public MdAttributeUUIDDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeUUIDDAOIF) super.getMdAttributeConcrete();
  }

  /**
   * @see Attribute#validate(Object)
   *
   */
  @Override
  public void validate(Object valueToValidate)
  {
    if (valueToValidate != null)
    {
      MdAttributeUUIDDAOIF mdAttributeIF = this.getMdAttributeConcrete();

      // First verify that the object is of the correct type.
      if (! ( valueToValidate instanceof String ))
      {
        String devMessage = "Value is not a " + String.class.getName();
        throw new AttributeUUIDParseException(devMessage, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), valueToValidate.getClass().getName());
      }
    }

    super.validate(valueToValidate);
  }

}
