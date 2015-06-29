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

public interface MdWebDateInfo extends MdWebMomentInfo
{
  public static final String CLASS                  = Constants.METADATA_PACKAGE + ".MdWebDate";

  public static final String AFTER_TODAY_EXCLUSIVE  = "afterTodayExclusive";

  public static final String AFTER_TODAY_INCLUSIVE  = "afterTodayInclusive";

  public static final String BEFORE_TODAY_EXCLUSIVE = "beforeTodayExclusive";

  public static final String BEFORE_TODAY_INCLUSIVE = "beforeTodayInclusive";

  public static final String END_DATE               = "endDate";

  public static final String START_DATE             = "startDate";
}
