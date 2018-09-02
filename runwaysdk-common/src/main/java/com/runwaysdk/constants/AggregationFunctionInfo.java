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

public interface AggregationFunctionInfo extends EnumerationMasterInfo
{
  public static final String CLASS_NAME                      = "AggregationFunction";
  
  public static final String CLASS                           = Constants.METADATA_PACKAGE+"."+CLASS_NAME;
  
  public static final String TABLE                           = "aggretation_function";
  
  public static final String ID_VALUE                        = "ii7sig2clgy9z81k9zxs901sout0qs8q0058";

  public static final String FUNCTION                        = "function";
  
  // The name of the enumeration used by ratios
  public static final String INDICATOR_FUNCTION_ENUM_CLASS_NAME       = "IndicatorAggregateFunction";

  public static final String INDICATOR_FUNCTION_ENUM_CLASS   = Constants.METADATA_PACKAGE+"."+INDICATOR_FUNCTION_ENUM_CLASS_NAME;

  public static final String INDICATOR_FUNCTION_ENUM_CLASS_ID_VALUE   = "i0mpeef4ubwk29ntr0po98lywhtzoi4m0075";

  public static final String SUM                             = "SUM";

  public static final String AVG                             = "AVG";
  
  public static final String COUNT                           = "COUNT";
  
  public static final String MIN                             = "MIN";
  
  public static final String MAX                             = "MAX";
  
  public static final String STDEV                           = "STDEV";
}
