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
package com.runwaysdk.controller;

import java.util.Locale;

import com.runwaysdk.ClientExceptionMessageLocalizer;
import com.runwaysdk.RunwayExceptionDTO;

public class UnknownServletException extends RunwayExceptionDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = -1962819927380684052L;

  private Locale locale;
  
  private String controller;
  
  public UnknownServletException(String developerMessage, Locale locale, String controller)
  {
    super(UnknownServletException.class.getName(), "", developerMessage);
    this.controller = controller;
    this.locale = locale;
  }
  
  protected Locale getLocale()
  {
    return this.locale;
  }
  
  @Override
  public String getMessage()
  {
    return ClientExceptionMessageLocalizer.unknownServletException(this.getLocale(), controller);
  }

}
