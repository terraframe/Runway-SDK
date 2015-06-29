/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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

public interface MdActionInfo extends MetadataInfo
{
  public static final String ID_VALUE                  = "NM20081011000000000000000000011300000000000000000000000000000001";

  public static final String CLASS                     = Constants.METADATA_PACKAGE + ".MdAction";

  public static final String NAME                      = "actionName";

  /**
   * Name of the attribute that stores the label of this metadata object.
   */
  public static final String DISPLAY_LABEL             = "displayLabel";

  public static final String ENCLOSING_MD_CONTROLLER   = "enclosingMdController";

  public static final String IS_POST                   = "isPost";

  public static final String IS_QUERY                  = "isQuery";

  public static final String SORT_ATTRIBUTE            = "sortAttribute";

  public static final String IS_ASCENDING              = "isAscending";

  public static final String PAGE_NUMBER               = "pageNumber";

  public static final String PAGE_SIZE                 = "pageSize";

  /**
   * The required suffix for all actions invokable through a controller.
   */
  public static final String ACTION_SUFFIX             = ".mojo";

  public static final String AJAX_ACTION_SUFFIX        = ".mojax";

  public static final String FORM_OBJECT_ACTION_SUFFIX = ".mofo";

  public static final String MULTIPART_FILE_PARAMETER  = "com.runwaysdk.controller.MultipartFileParameter";
}
