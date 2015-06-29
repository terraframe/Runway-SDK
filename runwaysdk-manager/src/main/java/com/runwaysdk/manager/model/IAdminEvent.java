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
package com.runwaysdk.manager.model;

public interface IAdminEvent
{
  public static final int    EDIT               = 0;

  public static final int    VIEW               = 1;

  public static final int    SEARCH             = 2;

  public static final int    CLOSE              = 4;

  public static final int    EXPORT_TRANSACTION = 5;

  public static final int    IMPORT_TRANSACTION = 6;

  public static final int    APPLY              = 7;

  public static final int    DELETE             = 8;

  public static final String OBJECT             = "OBJECT";

  public Object getData(String key);

  public void setData(String key, Object object);

  public int getType();
}
