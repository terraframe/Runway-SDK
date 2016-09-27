/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis;

import java.util.Locale;

import com.runwaysdk.gis.GISCommonExceptionMessageLocalizer;
import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.controller.ParseProblemDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;

public class AttributeMultiPolygonParseProblemDTO extends ParseProblemDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = -1972750880051952412L;

  public AttributeMultiPolygonParseProblemDTO(ComponentDTO component, AttributeMdDTO attributeMd,
      Locale locale, String value)
  {
    super(component, attributeMd, locale, value);
  }

  public AttributeMultiPolygonParseProblemDTO(String attributeName, Locale locale, String value)
  {
    super(attributeName, locale, value);
  }

  @Override
  public String getMessage()
  {
    return GISCommonExceptionMessageLocalizer.attributeMultiPolygonParseException(this.getLocale(), this.getAttributeLabel(), this.getValue());
  }
}
