/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.dataaccess.attributes.entity;

import com.runwaysdk.AttributeUUIDParseException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeGraphRefDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.session.Session;

public class AttributeGraphRef extends Attribute
{
  /**
   * 
   */
  private static final long serialVersionUID = 7368938382761174978L;

  public AttributeGraphRef(String name, String mdAttributeKey, String definingEntityType)
  {
    super(name, mdAttributeKey, definingEntityType);
  }

  /**
   * 
   * @param name
   * @param mdAttributeKey
   *          key of the defining attribute metadata.
   * @param definingEntityType
   * @param value
   */
  public AttributeGraphRef(String name, String mdAttributeKey, String definingEntityType, String value)
  {
    super(name, mdAttributeKey, definingEntityType, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeGraphRefDAOIF} that defines the this attribute
   */
  public MdAttributeGraphRefDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeGraphRefDAOIF) super.getMdAttributeConcrete();
  }


  public VertexObjectDAOIF dereference(String referenceId)
  {
    MdAttributeGraphRefDAOIF mdAttribute = getMdAttributeConcrete();
    MdVertexDAOIF mdVertex = (MdVertexDAOIF) mdAttribute.getReferenceMdVertexDAOIF();

    if (referenceId.trim().equals(""))
    {
      return null;
    }
    else
    {
      return VertexObjectDAO.get(mdVertex, referenceId);
    }
  }

  /**
   * @see Attribute#validate(Object)
   *
   */
  @Override
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttribute)
  {
    if (valueToValidate != null)
    {
      MdAttributeGraphRefDAOIF mdAttributeIF = this.getMdAttributeConcrete();

      // First verify that the object is of the correct type.
      if (! ( valueToValidate instanceof String ))
      {
        String devMessage = "Value is not a " + VertexObjectDAO.class.getName();
        throw new AttributeUUIDParseException(devMessage, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), valueToValidate.getClass().getName());
      }
    }

    super.validate(valueToValidate, mdAttribute);
  }
  
  /**
   * Returns a deep clone of this attribute.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a deep clone of this Attribute
   */
  @Override
  public Attribute attributeClone()
  {
    return new AttributeGraphRef(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), new String(this.getRawValue()));
  }
}
