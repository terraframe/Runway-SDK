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

public class ReferenceParseProblemDTO extends ParseProblemDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = -8731459123935906085L;
  
  private String type;

  public ReferenceParseProblemDTO(ComponentDTO component, AttributeMdDTO attributeMd, Locale locale, String value, String type)
  {
    super(component, attributeMd, locale, value);
    
    this.type = type;
  }

  public ReferenceParseProblemDTO(String attributeName, Locale locale, String value, String type)
  {
    super(attributeName, locale, value);
    
    this.type = type;
  }

  @Override
  public String getMessage()
  {
    if(this.getValue() != null)
    {
      return CommonExceptionMessageLocalizer.referenceParseException(this.getLocale(), this.getAttributeLabel(), this.getValue(), type);      
    }
    
    return CommonExceptionMessageLocalizer.referenceParseException(this.getLocale(), this.getAttributeLabel(), type);      
  }
}
