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
package com.runwaysdk.system.metadata;

import java.util.Locale;

import com.runwaysdk.localization.SupportedLocaleIF;
import com.runwaysdk.transport.conversion.ConversionFacade;

public class SupportedLocale extends SupportedLocaleBase implements SupportedLocaleIF
{
  private static final long serialVersionUID = 1285835926;
  
  public SupportedLocale()
  {
    super();
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (obj != null && obj instanceof Locale)
    {
      return this.getLocale().equals(obj);
    }
    else
    {
      return super.equals(obj);
    }
  }
  
  @Override
  public int hashCode()
  {
    return this.getEnumName().hashCode();
  }
  
  @Override
  public String toString()
  {
    return this.getEnumName();
  }
  
  @Override
  public Locale getLocale()
  {
    return ConversionFacade.getLocale(this.getEnumName());
  }

  @Override
  public String getName()
  {
    return this.getEnumName();
  }

  @Override
  public String getCountry()
  {
    return this.getLocale().getCountry();
  }

  @Override
  public String getLanguage()
  {
    return this.getLocale().getLanguage();
  }

  @Override
  public String getVariant()
  {
    return this.getLocale().getVariant();
  }
}
