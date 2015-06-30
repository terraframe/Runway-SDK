/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.CommonExceptionMessageLocalizer;
import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;

public class DateTimeParseProblemDTO extends MomentParseExceptionDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = 5283207790238093955L;

  public DateTimeParseProblemDTO(ComponentDTO component, AttributeMdDTO attributeMd, Locale locale, String value, String format)
  {
    super(component, attributeMd, locale, value, format);
  }

  public DateTimeParseProblemDTO(String attributeName, Locale locale, String value, String format)
  {
    super(attributeName, locale, value, format);
  }
  
  @Override
  public String getMessage()
  {
    return CommonExceptionMessageLocalizer.dateTimeParseException(this.getLocale(), this.getAttributeLabel(), this.getValue(), this.getFormat());
  }
}
