package com.runwaysdk.dataaccess.io;

import java.util.LinkedList;
import java.util.List;

public class ExcelSheetMetadata
{
  private LinkedList<String> values;

  public ExcelSheetMetadata()
  {
    this.values = new LinkedList<String>();
  }

  public List<String> getValues()
  {
    return this.values;
  }

  public void setValues(String... values)
  {
    for (String value : values)
    {
      this.values.push(value);
    }
  }

}
