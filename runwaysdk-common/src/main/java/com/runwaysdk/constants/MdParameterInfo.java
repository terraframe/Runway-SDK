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

/**
 * @author jsmethie
 *
 */
public interface MdParameterInfo extends MetadataInfo
{
  /**
   * Class.
   */
  public static final String CLASS   = Constants.METADATA_PACKAGE+".MdParameter";
  
  /**
   * The name of the 'name' attribute on a MdParameter
   */
  public static final String NAME               = "parameterName";
  
  /**
   * Name of the attribute that stores the label of this metadata object.
   */
  public static final String DISPLAY_LABEL      = "displayLabel";
  
  /**
   * The name of the type attribute on a MdParameter
   */
  public static final String TYPE               = "parameterType";
  
  /**
   * The name of the order attribute on a MdParameter 
   */
  public static final String ORDER              = "parameterOrder";
  
  /**
   * The name of the attribute that references the MdMethod on which the MdParameter is generated.
   */
  public static final String ENCLOSING_METADATA = "metadata";
}
