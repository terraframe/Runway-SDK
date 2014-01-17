package com.runwaysdk.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BasicParameter implements Parameter
{
  private List<String> values;

  public BasicParameter()
  {
    this.values = new LinkedList<String>();
  }

  public BasicParameter(String[] values)
  {
    this.values = new LinkedList<String>(Arrays.asList(values));
  }

  public void add(String value)
  {
    this.values.add(value);
  }

  public List<String> getValues()
  {
    return values;
  }

  public String[] getValuesAsArray()
  {
    return this.values.toArray(new String[this.values.size()]);
  }

  public String getSingleValue()
  {
    if (this.values.size() > 0)
    {
      return this.values.get(0);
    }

    return null;
  }
}
