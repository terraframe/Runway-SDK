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

import com.runwaysdk.business.RunwayProblemDTO;
import com.runwaysdk.localization.CommonExceptionMessageLocalizer;

public class RequiredAttributeProblemDTO extends RunwayProblemDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = 6321049278249059387L;

  private String            attributeName;

  private Locale            locale;

  public RequiredAttributeProblemDTO(String attributeName, Locale locale)
  {
    super(RequiredAttributeProblemDTO.class.getName(), "");

    this.attributeName = attributeName;
    this.locale = locale;
  }

  protected Locale getLocale()
  {
    return locale;
  }

  /**
   * @see com.runwaysdk.AttributeNotificationDTO#getAttributeName()
   */
  public String getAttributeName()
  {
    return attributeName;
  }

  @Override
  public String getMessage()
  {
    return CommonExceptionMessageLocalizer.requiredParameterException(this.getLocale(), this.getAttributeName());
  }
}
