/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.mvc;

import java.lang.reflect.Parameter;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ParameterIF;
import com.runwaysdk.request.ServletRequestIF;

public class ParameterDecorator implements ParameterIF
{
  private String    name;

  private String    type;

  private ParseType parseType;

  private boolean   required;

  public ParameterDecorator(Parameter parameter)
  {
    Class<?> type = parameter.getType();

    this.type = type.getName();

    RequestParamter annotation = parameter.getAnnotation(RequestParamter.class);

    if (annotation != null)
    {
      this.name = annotation.name();
      this.parseType = annotation.parser();
      this.required = annotation.required();
    }
    else
    {
      if (parameter.isNamePresent())
      {
        this.name = parameter.getName();
      }
      else if (ClientRequestIF.class.isAssignableFrom(type))
      {
        this.name = "#request";
      }
      else if (ClientSession.class.isAssignableFrom(type))
      {
        this.name = "#session";
      }
      else if (ServletRequestIF.class.isAssignableFrom(type))
      {
        this.name = "#servletrequest";
      }
      else
      {
        // TODO Change exception type
        String message = "A parameter on a controller endpoint was compiled without the -parameters compile argument and does not include a @RequestParamter annotation";
        throw new RuntimeException(message);
      }

      this.parseType = ParseType.NONE;
    }
  }

  @Override
  public String getName()
  {
    return this.name;
  }
  
  @Override
  public boolean isRequired()
  {
    return required;
  }

  @Override
  public String getType()
  {
    return this.type;
  }

  @Override
  public ParseType getParseType()
  {
    return this.parseType;
  }
}
