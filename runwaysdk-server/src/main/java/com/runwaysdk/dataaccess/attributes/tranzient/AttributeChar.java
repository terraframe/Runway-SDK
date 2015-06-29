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

public abstract class AttributeChar extends Attribute
{
  /**
   * 
   */
  private static final long serialVersionUID = 6880998985160669165L;

  /**
   * Inherited constrcutor, sets <code>name</code> and <code>definingTransientType</code>.
   * 
   * @param name The name of this character attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType The class that defines this attribute.
   */
  protected AttributeChar(String name, String mdAttributeKey, String definingTransientType)
  {
    super(name, mdAttributeKey, definingTransientType);
  }

  /**
   * Inherited constrcutor, sets <code>name</code>, <code>definingTransientType</code>, and
   * <code>value</code>.
   * 
   * @param name The name of this character attribute.
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType The type that defines this attribute.
   * @param value The value of this character. "<code>true</code>" or "<code>false</code>"
   */
  protected AttributeChar(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    super(name, mdAttributeKey, definingTransientType, value);
  }
}
