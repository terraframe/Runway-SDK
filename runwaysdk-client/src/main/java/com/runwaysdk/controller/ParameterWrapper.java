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

import com.runwaysdk.mvc.ParseType;

public class ParameterWrapper implements ParameterIF
{
  private String type;

  private String parameterName;

  public ParameterWrapper(String type, String parameterName)
  {
    this.type = type;
    this.parameterName = parameterName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.ParameterIF#getParameterName()
   */
  @Override
  public String getName()
  {
    return parameterName;
  }

  public void setParameterName(String parameterName)
  {
    this.parameterName = parameterName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.controller.ParameterIF#getType()
   */
  @Override
  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public boolean isRequired()
  {
    return false;
  }

  @Override
  public ParseType getParseType()
  {
    return ParseType.NONE;
  }
}
