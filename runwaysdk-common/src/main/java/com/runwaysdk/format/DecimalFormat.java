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
package com.runwaysdk.format;

import java.math.BigDecimal;
import java.util.Locale;

public class DecimalFormat extends NumberFormat<BigDecimal>
{
  public DecimalFormat()
  {
    super();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.NumberFormat#convert(java.lang.Number)
   */
  @Override
  protected BigDecimal convert(Number number)
  {
    // According to the documentation, the BigDecimal(String) constructor is the most predictable even
    // if it not's the most intuitive. Let's trust the documentation and use it regardless. 
    return new BigDecimal(Double.toString(number.doubleValue()));
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.format.NumberFormat#getInstance(java.util.Locale)
   */
  @Override
  protected java.text.NumberFormat getInstance(Locale locale)
  {
    return java.text.DecimalFormat.getInstance(locale);
  }

}
