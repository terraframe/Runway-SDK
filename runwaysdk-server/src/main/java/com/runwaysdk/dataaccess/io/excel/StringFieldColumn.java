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

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdWebAttributeDAOIF;

public class StringFieldColumn extends FieldColumn
{
  private MdAttributeDAOIF mdAttribute;

  public StringFieldColumn(MdWebAttributeDAOIF mdField)
  {
    super(mdField);

    this.mdAttribute = mdField.getDefiningMdAttribute();
  }

  public Object getCellValue(HSSFCell cell) throws Exception
  {
    return ExcelUtil.getString(cell);
  }

  @Override
  public void setCellValue(HSSFCell cell, String value)
  {
    cell.setCellValue(new HSSFRichTextString(value));
  }

  /**
   * @return The fully qualified java type of this column
   */
  public String javaType()
  {
    String javaType = mdAttribute.javaType(false);

    if (javaType.equals(String.class.getSimpleName()))
    {
      return String.class.getName();
    }

    return javaType;
  }

  @Override
  protected int getExpectedFieldType()
  {
    return HSSFCell.CELL_TYPE_STRING;
  }

}
