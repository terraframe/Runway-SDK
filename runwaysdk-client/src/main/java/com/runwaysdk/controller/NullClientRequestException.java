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
package com.runwaysdk.controller;

import java.util.Locale;

import com.runwaysdk.ClientException;
import com.runwaysdk.ClientExceptionMessageLocalizer;

public class NullClientRequestException extends ClientException
{
  /**
   * 
   */
  private static final long serialVersionUID = 711455113595282669L;
  
  public NullClientRequestException(String devMessage, Throwable cause, Locale locale)
  {
    super(locale, devMessage, cause);
  }

  public NullClientRequestException(String devMessage, Locale locale)
  {
    super(locale, devMessage);
  }

  public NullClientRequestException(Throwable cause, Locale locale)
  {
    super(locale, cause);
  }

  @Override
  public String getMessage()
  {
    return ClientExceptionMessageLocalizer.nullClientRequestException(this.getLocale());
  }

}
