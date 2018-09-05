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
package com.runwaysdk.constants;

public interface IndicatorPrimitiveInfo extends IndicatorElementInfo
{
  /**
   * Interface {@link IndicatorPrimitiveInfo}.
   */
  public static final String CLASS_NAME              = "IndicatorPrimitive";
  
  public static final String CLASS                   = Constants.METADATA_PACKAGE + "." + CLASS_NAME;
  
  /**
   * OID.
   */
  public static final String ID_VALUE                = "32e6db39-30f7-3214-9b17-2a170d630058";
  
  /**
   * Reference to an attribute definition.
   */
  public static final String MD_ATTRIBUTE_PRIMITIVE  = "mdAttributePrimitive";
  
  /**
   * A function to be applied on the indicator primitive
   */
  public static final String INDICATOR_FUNCTION      = "indicatorFunction";
  
}
