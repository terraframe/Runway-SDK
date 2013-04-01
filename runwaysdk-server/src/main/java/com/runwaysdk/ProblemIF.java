/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk;

import java.util.Locale;

public interface ProblemIF
{  
  /**
   * Sets the locale used for the localized message.
   * @param locale
   */
  public void setLocale(Locale locale);
  
  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   */
  public String getLocalizedMessage();
  
  /**
   * Returns a message set by developers for developers.
   * @return a message set by developers for developers.
   */
  public String getDeveloperMessage();
  
  /**
   * Finalizes the Problem.  Transaction will obtain a reference
   * to this problem and prevent the transaction from completing.
   */
  public void throwIt();
}
