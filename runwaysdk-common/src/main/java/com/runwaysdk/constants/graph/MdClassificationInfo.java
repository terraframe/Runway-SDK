/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.constants.graph;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MetadataInfo;

public interface MdClassificationInfo extends MetadataInfo
{
  /**
   * Class {@link MdClassificationInfo}.
   */
  public static final String CLASS         = Constants.METADATA_PACKAGE + ".MdClassification";

  /**
   * Name of the attribute that stores the name of the relationship that is
   * defined.
   */
  public static final String NAME          = "typeName";

  /**
   * Name of the attribute that stores the name of the package of the
   * relationship that is defined.
   */
  public static final String PACKAGE       = "packageName";

  /**
   * Name of the attribute that stores the label of this metadata object.
   */
  public static final String DISPLAY_LABEL = "displayLabel";

  /**
   * MdVertex used by the classification
   */
  public static final String MD_VERTEX     = "mdVertex";

  /**
   * MdEdge used by the classification
   */
  public static final String MD_EDGE       = "mdEdge";

  /**
   * OID.
   */
  public static final String ID_VALUE      = "af02ce48-050f-3449-b7d2-8add4f00003a";

}
