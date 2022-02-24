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
package com.runwaysdk.constants;

public interface MathOperatorInfo extends EnumerationMasterInfo
{
  public static final String CLASS_NAME                      = "MathOperator";
  
  public static final String CLASS                           = Constants.METADATA_PACKAGE+"."+CLASS_NAME;
  
  public static final String TABLE                           = "math_operators";

  public static final String ID_VALUE                        = "6d8736af-ba04-35ee-85a2-d391bb00003a";
  
  public static final String OPERATOR_SYMBOL                 = "operatorSymbol";
  
  // The name of the enumeration used by ratios
  public static final String INDICATOR_ENUM_CLASS_NAME       = "IndicatorOperator";
  
  public static final String INDICATOR_ENUM_CLASS            = Constants.METADATA_PACKAGE+"."+INDICATOR_ENUM_CLASS_NAME;
  
  public static final String INDICATOR_ENUM_CLASS_ID_VALUE   = "c8018f6b-9e6c-3574-af28-f4eb5c00004b";
  
  public static final String DIVISION_OPERATION_ID           = "1a80bebd-86ae-30d5-ace3-e453cb0000ed";
  
  public static final String DIVISION_SYMBOL                 = "/";
  
}
