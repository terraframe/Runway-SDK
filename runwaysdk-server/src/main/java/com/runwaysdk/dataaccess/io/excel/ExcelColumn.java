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
package com.runwaysdk.dataaccess.io.excel;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.io.ColumnTransform;

public class ExcelColumn
{
  protected String        attributeName;

  protected String        displayLabel;

  private String          description = null;

  private int             index;

  private boolean         required;

  private ColumnTransform transform;

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

  /**
   * @param transform
   *          the transform to set
   */
  public void setTransform(ColumnTransform transform)
  {
    this.transform = transform;
  }

  /**
   * @return the transform
   */
  public ColumnTransform getTransform()
  {
    return transform;
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

  public void setValue(Cell cell, String value)
  {
    CreationHelper helper = cell.getSheet().getWorkbook().getCreationHelper();

    cell.setCellValue(helper.createRichTextString(value));
  }

  /**
   * @param component
   * @param overrides
   * @return
   */
  public String getValue(ComponentIF component, Map<String, String> overrides)
  {
    String attributeName = this.getAttributeName();

    if (overrides.containsKey(attributeName))
    {
      return overrides.get(attributeName);
    }

    String value = component.getValue(attributeName);

    if (this.transform != null)
    {
      value = (String) this.transform.transform(value);
    }

    return value;
  }
}
