package com.runwaysdk.mvc;

import java.lang.reflect.Parameter;

import com.runwaysdk.controller.ParameterIF;

public class ParameterDecorator implements ParameterIF
{
  private String    name;

  private String    type;

  private ParseType parseType;

  public ParameterDecorator(Parameter parameter)
  {
    this.type = parameter.getType().getName();

    RequestParamter annotation = parameter.getAnnotation(RequestParamter.class);

    if (annotation != null)
    {
      this.name = annotation.name();
      this.parseType = annotation.parser();
    }
    else
    {
      if (parameter.isNamePresent())
      {
        this.name = parameter.getName();
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
