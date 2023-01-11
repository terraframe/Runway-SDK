/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.dataaccess.graph.attributes;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.gis.AttributeMultiPointParseException;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF;
import com.runwaysdk.session.Session;
import org.locationtech.jts.geom.MultiPoint;

public class AttributeMultiPoint extends Attribute
{

  /**
   * 
   */
  private static final long serialVersionUID = 356523685120439629L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeMultiPoint(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, String)
   */
  protected AttributeMultiPoint(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, MultiPoint value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeMultiPointDAOIF} that defines the this attribute
   */
  public MdAttributeMultiPointDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeMultiPointDAOIF) super.getMdAttributeConcrete();
  }

  /**
   * @see Attribute#validate(Object)
   *
   */
  @Override
  public void validate(Object valueToValidate)
  {
    MdAttributeMultiPointDAOIF mdAttributeIF = this.getMdAttributeConcrete();

    // First verify that the object is of the correct type.
    if (valueToValidate != null && ! ( valueToValidate instanceof MultiPoint ))
    {
      String devMessage = "Value is not a " + MultiPoint.class.getName();
      throw new AttributeMultiPointParseException(devMessage, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), valueToValidate.getClass().getName());
    }

    super.validate(valueToValidate);
  }
}
