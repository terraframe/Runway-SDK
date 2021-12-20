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

import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;

public abstract class MomentParseExceptionDTO extends ParseProblemDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = 2891742233669118405L;
  
  private String format;

  public MomentParseExceptionDTO(ComponentDTO component, AttributeMdDTO attributeMd, Locale locale, String value, String format)
  {
    super(component, attributeMd, locale, value);
    
    this.format = format;
  }

  public MomentParseExceptionDTO(String attributeName, Locale locale, String value, String format)
  {
    super(attributeName, locale, value);
    
    this.format = format;
  }
  
  protected String getFormat()
  {
    return this.format;
  }
}
