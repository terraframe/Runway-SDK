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

import com.runwaysdk.constants.CommonProperties;

public abstract class RunwayProblem implements ProblemIF
{
  private Locale locale;

  private String developerMessage;

  protected RunwayProblem(String developerMessage)
  {
    this.developerMessage = developerMessage;
  }

  /**
   * Finalizes the Problem.  Transaction will obtain a reference
   * to this problem and prevent the transaction from completing.
   */
  public void throwIt()
  {
    // marker method.
  }

  /**
   * Sets the locale used for the localized message.
   * @param locale
   */
  public void setLocale(Locale locale)
  {
    this.locale = locale;
  }

  /**
   * Returns the Locale of this FrameworkProblem. If the locale is null,
   * it is set to the default locale specified in the common properties.
   *
   * @return
   */
  public Locale getLocale()
  {
    if(this.locale == null)
    {
      this.locale = CommonProperties.getDefaultLocale();
    }

    return this.locale;
  }

  /**
   * Returns a message set by developers for developers.
   * @return a message set by developers for developers.
   */
  public String getDeveloperMessage()
  {
    return this.developerMessage;
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   *
   */
  public String getLocalizedMessage()
  {
    return CommonExceptionMessageLocalizer.runwayException(this.getLocale());
  }
}
