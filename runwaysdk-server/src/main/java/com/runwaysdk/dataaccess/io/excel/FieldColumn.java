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

import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebAttributeDAOIF;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.session.Session;

public abstract class FieldColumn extends ExcelColumn
{
  private MdWebAttributeDAOIF mdField;

  public FieldColumn(MdWebAttributeDAOIF mdField)
  {
    this(mdField, 0);
  }

  public FieldColumn(MdWebAttributeDAOIF mdField, int index)
  {
    super(mdField.getDefiningMdAttribute().getValue(MdAttributeConcreteInfo.NAME), mdField.getDisplayLabel(Session.getCurrentLocale()), mdField.getDescription(Session.getCurrentLocale()), index, Boolean.parseBoolean(mdField.isRequired()));

    this.mdField = mdField;
  }

  /**
   * @return The fully qualified java type of this column
   */
  public abstract String javaType();

  protected abstract int getExpectedFieldType();

  protected abstract Object getCellValue(HSSFCell cell) throws Exception;

  protected abstract void setCellValue(HSSFCell cell, String value);

  public Object getValue(HSSFCell cell) throws Exception
  {
    int fieldType = this.getExpectedFieldType();

    if (cell.getCellType() == fieldType)
    {
      return this.getCellValue(cell);
    }
    else if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK)
    {
      return null;
    }
    else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
    {
      String value = cell.getRichStringCellValue().getString();

      if (value == null || value.length() == 0)
      {
        return null;
      }
    }

    int row = cell.getRowIndex();
    String attributeName = this.getAttributeName();
    String msg = "Conversion exception on row (" + row + ", " + attributeName + ") expected type [" + fieldType + "] actual type [" + cell.getCellType() + "]";

    throw new FieldConversionException(msg, this);
  }

  @Override
  public void setValue(HSSFCell cell, String value)
  {
    if (value != null && value.length() > 0)
    {
      this.setCellValue(cell, value);
    }
  }

  /**
   * @return The name of the setter method (in the generated source) for this
   *         column
   */
  public String getSetterMethodName()
  {
    MdAttributeDAOIF mdAttribute = mdField.getDefiningMdAttribute();
    String name = mdAttribute.getMdAttributeConcrete().getValue(MdAttributeConcreteInfo.NAME);

    return "set" + CommonGenerationUtil.upperFirstCharacter(name);
  }

  public MdFieldDAOIF getMdField()
  {
    return mdField;
  }
}
