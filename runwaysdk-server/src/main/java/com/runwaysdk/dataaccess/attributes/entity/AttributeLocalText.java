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
package com.runwaysdk.dataaccess.attributes.entity;

import com.runwaysdk.dataaccess.AttributeLocalTextIF;
import com.runwaysdk.dataaccess.StructDAO;

public class AttributeLocalText extends AttributeLocal implements AttributeLocalTextIF
{
  /**
   *
   */
  private static final long serialVersionUID = -7527532059253231530L;

  /**
   * @param name
   * @param mdAttributeKey key of the defining attribute metadata.
   * @param definingType
   * @param value
   */
  public AttributeLocalText(String name, String mdAttributeKey, String definingType, String value)
  {
     super(name, mdAttributeKey, definingType, value);
  }

  /**
   *
   * @param name
   * @param mdAttributeKey key of the defining attribute metadata.
   * @param definingEntityType
   * @param value
   * @param structDAO
   */
  public AttributeLocalText(String name, String mdAttributeKey, String definingEntityType, String value, StructDAO structDAO)
  {
     super(name, mdAttributeKey, definingEntityType, value, structDAO);
  }

  /**
   *
   */
  public Attribute attributeClone()
  {
    return new AttributeLocalText(this.getName(), this.mdAttributeKey, this.getDefiningClassType(),
                                  new String(this.getValue()), (StructDAO)this.getStructDAO().clone());
  }

  /**
   *
   */
  public Attribute attributeCopy()
  {
    return new AttributeLocalText(this.getName(), this.mdAttributeKey, this.getDefiningClassType(),
                                new String(this.getValue()), (StructDAO)this.getStructDAO().copy());
  }
}
