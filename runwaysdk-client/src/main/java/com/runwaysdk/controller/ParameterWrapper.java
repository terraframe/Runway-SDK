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
  public ParseType getParseType()
  {
    return ParseType.NONE;
  }
}
