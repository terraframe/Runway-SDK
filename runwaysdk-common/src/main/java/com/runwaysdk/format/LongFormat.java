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
package com.runwaysdk.format;

import java.util.Locale;


public class LongFormat extends NumberFormat<Long>
{

  public LongFormat()
  {
    super();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.NumberFormat#convert(java.lang.Number)
   */
  @Override
  protected Long convert(Number number)
  {
    return Long.valueOf(number.longValue());
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.format.NumberFormat#getInstance(java.util.Locale)
   */
  @Override
  protected java.text.NumberFormat getInstance(Locale locale)
  {
    return java.text.NumberFormat.getNumberInstance(locale);
  }

}
