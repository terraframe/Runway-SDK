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

public interface MdAttributeVirtualInfo extends MdAttributeInfo
{
  /**
   * Class MetaData.
   */
   public static final String CLASS                       = Constants.METADATA_PACKAGE+".MdAttributeVirtual";
  
  /**
   * OID.
   */
   public static final String ID_VALUE  = "a4068afb-6bfd-32c6-9437-59526300003a";

   /**
    * Name of the attribute that this metadata defines.
    */
   public static final String NAME                        = "attributeName";
   /**
    * Name of the attribute that indicates if the attribute requires a value.
    */
   public static final String REQUIRED                    = "required";
   /**
    * Name of the attribute that stores the label of this metadata object.
    */
   public static final String DISPLAY_LABEL               = "displayLabel";
   /**
    * Name of the attribute that references the meta data type to which this
    * attribute belongs.
    */
   public static final String DEFINING_MD_VIEW            = "definingMdView";
   
   /**
    * Concrete attribute that this virtual attribute references.
    */
   public static final String MD_ATTRIBUTE_CONCRETE       = "mdAttributeConcrete";
}
