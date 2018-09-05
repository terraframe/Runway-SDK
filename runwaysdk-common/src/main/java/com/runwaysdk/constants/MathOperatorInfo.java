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

public interface MathOperatorInfo extends EnumerationMasterInfo
{
  public static final String CLASS_NAME                      = "MathOperator";
  
  public static final String CLASS                           = Constants.METADATA_PACKAGE+"."+CLASS_NAME;
  
  public static final String TABLE                           = "math_operators";

  public static final String ID_VALUE                        = "40c2c475-7d28-33f8-a37e-c9fd82110058";
  
  public static final String OPERATOR_SYMBOL                 = "operatorSymbol";
  
  // The name of the enumeration used by ratios
  public static final String INDICATOR_ENUM_CLASS_NAME       = "IndicatorOperator";
  
  public static final String INDICATOR_ENUM_CLASS            = Constants.METADATA_PACKAGE+"."+INDICATOR_ENUM_CLASS_NAME;
  
  public static final String INDICATOR_ENUM_CLASS_ID_VALUE   = "8ffda9cc-e149-3ad8-9de2-6bdbf1290075";
  
  public static final String DIVISION_OPERATION_ID           = "fe079d1e-1ccc-3bd4-ad4d-26972bc60237";
  
  public static final String DIVISION_SYMBOL                 = "/";
  
}
