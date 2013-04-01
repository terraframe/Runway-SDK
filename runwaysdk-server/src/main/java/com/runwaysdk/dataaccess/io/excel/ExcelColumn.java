/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.io.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;

public class ExcelColumn
{
  protected String attributeName;

  protected String displayLabel;

  private String   description = null;

  private int      index;

  private boolean  required;

  public ExcelColumn(String attributeName, String displayLabel, String description, int index, boolean required)
  {
    this.attributeName = attributeName;
    this.displayLabel = displayLabel;
    this.description = description;
    this.index = index;
    this.required = required;
  }

  public ExcelColumn(String attributeName, String displayLabel, int index)
  {
    this(attributeName, displayLabel, null, index, false);
  }

  public ExcelColumn(String attributeName, String displayLabel, boolean required)
  {
    this(attributeName, displayLabel, null, 0, required);
  }

  public ExcelColumn(String attributeName, String displayLabel)
  {
    this(attributeName, displayLabel, null, 0, false);
  }

  public String getDisplayLabel()
  {
    return displayLabel;
  }

  public void setDisplayLabel(String displayLabel)
  {
    this.displayLabel = displayLabel;
  }

  public String getAttributeName()
  {
    return attributeName;
  }
  
  public void setAttributeName(String attributeName)
  {
    this.attributeName = attributeName;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public int getIndex()
  {
    return index;
  }

  public void setIndex(int index)
  {
    this.index = index;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired(boolean required)
  {
    this.required = required;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ExcelColumn)
    {
      ExcelColumn other = (ExcelColumn) obj;
      return other.getAttributeName().equals(this.getAttributeName());
    }
    return false;
  }

  @Override
  public String toString()
  {
    return "(" + attributeName + ", " + index + ")";
  }

  public void setValue(HSSFCell cell, String value)
  {
    cell.setCellValue(new HSSFRichTextString(value));
  }
}
