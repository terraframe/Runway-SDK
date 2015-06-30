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

import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Cell;

import com.runwaysdk.dataaccess.MdWebDecimalDAOIF;

public class DecimalFieldColumn extends FieldColumn
{

  public DecimalFieldColumn(MdWebDecimalDAOIF mdField)
  {
    super(mdField);
  }

  public Object getCellValue(Cell cell) throws Exception
  {
    return new BigDecimal(cell.getNumericCellValue());
  }

  @Override
  public void setCellValue(Cell cell, String value)
  {
    cell.setCellValue(Double.parseDouble(value));
  }

  /**
   * @return The fully qualified java type of this column
   */
  public String javaType()
  {
    return BigDecimal.class.getName();
  }

  @Override
  protected int getExpectedFieldType()
  {
    return Cell.CELL_TYPE_NUMERIC;
  }

}
