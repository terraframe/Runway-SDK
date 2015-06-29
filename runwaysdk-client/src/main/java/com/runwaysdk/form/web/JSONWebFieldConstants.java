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
package com.runwaysdk.form.web;

public interface JSONWebFieldConstants
{
  // general keys
  public static final String JS_CLASS     = "js_class";

  public static final String READABLE     = "readable";

  public static final String WRITABLE     = "writable";

  public static final String TYPE         = "type";

  public static final String ID           = "id";

  public static final String DATA_ID      = "dataId";

  public static final String MODIFIED     = "modified";

  public static final String FORM_OBJECT  = "formObject";

  public static final String FIELDS       = "fields";

  public static final String CONDITION    = "condition";

  // FormObject keys
  public static final String NEW_INSTANCE = "newInstance";

  public static final String DISCONNECTED = "disconnected";

  public static final String FORM_MD      = "formMd";

  // FormMd keys

  // Field keys
  public static final String VALUE        = "value";

  public static final String FIELD_MD     = "fieldMd";

  // Condition keys
  public static final String CONDITION_MD = "conditionMd";
}
