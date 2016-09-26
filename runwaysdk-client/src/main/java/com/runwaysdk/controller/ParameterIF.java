package com.runwaysdk.controller;

import com.runwaysdk.mvc.ParseType;

public interface ParameterIF
{
  public String getName();

  public String getType();

  public ParseType getParseType();
}