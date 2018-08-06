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
package com.runwaysdk.dataaccess.io.instance;

public interface XMLTags
{
  public static final String INSTANCE_SCHEMA_TAG = "http://runwaysdk.com/schema/instance.xsd";

  /**
   *  Instance Element tags, see datatype.xsd for full descriptions
   */
  public static final String OBJECT_TAG               = "object";
  public static final String RELATIONSHIP_TAG         = "relationship";
  public static final String VALUE_TAG                = "value";
  public static final String ENUMERATION_TAG          = "enumeration";
  public static final String STRUCT_REF_TAG           = "struct";

  /**
   *  Attributes of the instance_value tag, see datatype.xsd for full descriptions
   */
  public static final String ATTRIBUTE_TAG            = "attribute";
  public static final String ATTRIBUTE_VALUE_TAG      = "value";

  public static final String PARENT_ID_TAG            = "parentId";
  public static final String CHILD_ID_TAG             = "childId";

  /**
   *  Attributes of the reference tag, see datatype.xsd for full description
   */
  public static final String TYPE_TAG                 = "type";

  /**
   *   Attributes of the enum_instance tag, see PrototypeMetaData.xsd for full description
   */
  public static final String ID_TAG                   = "oid";

}
