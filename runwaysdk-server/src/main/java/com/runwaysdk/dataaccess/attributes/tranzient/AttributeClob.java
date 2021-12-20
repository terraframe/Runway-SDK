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

import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;

public class AttributeClob extends AttributeChar
{
  /**
   *
   */
  private static final long serialVersionUID = -940178444923635338L;

  /**
   * Inherited constructor, sets <code>name</code> and
   * <code>definingTransientType</code>.
   *
   * @param name The name of this CLOB attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType Name of the class that defines this attribute.
   */
  protected AttributeClob(String name, String mdAttributeKey, String definingTransientType)
  {
    super(name, mdAttributeKey, definingTransientType);
  }

  /**
   * Inherited constructor, sets <code>name</code>, <code>definingTransientType</code>,
   * and <code>value</code>.
   *
   * @param name
   *          The name of this CLOB attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType
   *          Name of the class that defines this attribute.
   * @param value
   *          The value of this CLOB. "<code>true</code>" or "<code>false</code>"
   */
  protected AttributeClob(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    super(name, mdAttributeKey, definingTransientType, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined
   * by a concrete attribute, this object is returned.  If it is a virtual attribute, then the
   * concrete attribute it references is returned.
   *
   * @return {@link MdAttributeClobDAOIF} that defines the this attribute
   */
  public MdAttributeClobDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeClobDAOIF)super.getMdAttributeConcrete();
  }
}
