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

public interface MdMethodInfo extends MetadataInfo
{
  /**
   * OID.
   */
  public static final String ID_VALUE  = "20070427JS00000000000000000015140058";  

  /**
   * Class.
   */
  public static final String CLASS                       = Constants.METADATA_PACKAGE+".MdMethod"; 
  
  /**
   * The name of the 'name' attribute for MdMethods
   */
  public static final String NAME                        = "methodName";

  /**
   * Name of the attribute that stores the label of this metadata object.
   */
  public static final String DISPLAY_LABEL               = "displayLabel";

  /**
   * The name of the return type attribute for MdMethods
   */
  public static final String RETURN_TYPE                 = "returnType";
  
  /**
   * The name of the attribute that references the MdType on which the MdMethod is generated.
   */
  public static final String REF_MD_TYPE                 = "mdType";
  
  /**
   * The name of the isStatic attribute fore MdMethods.
   * This attribute is ignored if REF_MD_TYPE refers to a
   * MdFacade.  This is due to the fact that all methods 
   * defined for a MdFacade must be static.
   */
  public static final String IS_STATIC                   = "isStatic";
}
