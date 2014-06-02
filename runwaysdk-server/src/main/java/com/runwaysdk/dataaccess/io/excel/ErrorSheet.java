/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.dataaccess.io.excel;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ErrorSheet
{
  public int        HEADER_COLUMN = 1;

  private int       count;

  /**
   * A sheet to capture and store any rows from the import sheet that error out
   */
  private Sheet errorSheet;

  public ErrorSheet(Sheet errorSheet)
  {
    this.errorSheet = errorSheet;
    this.count = 0;
  }

  public void addRow(Row row)
  {
    Row newRow = this.errorSheet.createRow(count++);

    Iterator<Cell> cellIterator = row.cellIterator();
    while (cellIterator.hasNext())
    {
      Cell oldCell = cellIterator.next();
      Cell newCell = newRow.createCell(oldCell.getColumnIndex());

      int cellType = oldCell.getCellType();

      if (cellType == Cell.CELL_TYPE_FORMULA)
      {
        cellType = oldCell.getCachedFormulaResultType();
      }

      switch (cellType)
      {
        case Cell.CELL_TYPE_BOOLEAN:
          newCell.setCellValue(oldCell.getBooleanCellValue());
          break;
        case Cell.CELL_TYPE_NUMERIC:
          newCell.setCellValue(oldCell.getNumericCellValue());
          break;
        case Cell.CELL_TYPE_STRING:
          newCell.setCellValue(oldCell.getRichStringCellValue());
          break;
      }
    }
  }

  public void autoSize()
  {
    Row row = this.errorSheet.getRow(HEADER_COLUMN);
    if (row != null)
    {
      for (short s = row.getFirstCellNum(); s < row.getLastCellNum(); s++)
      {
        this.errorSheet.autoSizeColumn(s);
      }
    }
  }

  public int getCount()
  {
    return this.count;
  }
}
