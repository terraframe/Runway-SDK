/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.dataaccess.StructDAO;

public class AttributeLocalText extends AttributeLocal
{
  /**
   * 
   */
  private static final long serialVersionUID = -4286451196774770045L;

  /**
   * @param name
   * @param mdAttributeKey key of the defining metadata.
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
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType
   * @param value
   * @param structDAO
   */
  public AttributeLocalText(String name, String mdAttributeKey, String definingTransientType, String value, StructDAO structDAO)
  {
     super(name, mdAttributeKey, definingTransientType, value, structDAO);
  }
}
