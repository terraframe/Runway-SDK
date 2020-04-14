/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.dataaccess.io.excel;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ErrorSheet
{
  public int    HEADER_COLUMN = 1;

  private int   count;

  /**
   * A sheet to capture and store any rows from the import sheet that error out
   */
  private Sheet errorSheet;

  public ErrorSheet(Sheet errorSheet)
  {
    this.errorSheet = errorSheet;
    this.count = 0;
  }

  public void addRow(Row _row)
  {
    Row row = this.errorSheet.createRow(count++);
    row.setZeroHeight(_row.getZeroHeight());
    row.setHeight(_row.getHeight());

    CellStyle style = _row.getRowStyle();

    if (style != null)
    {
      Workbook workbook = row.getSheet().getWorkbook();

      CellStyle clone = workbook.createCellStyle();
      clone.cloneStyleFrom(style);

      row.setRowStyle(clone);
    }

    Iterator<Cell> cellIterator = _row.cellIterator();
    while (cellIterator.hasNext())
    {
      Cell oldCell = cellIterator.next();
      Cell newCell = row.createCell(oldCell.getColumnIndex());

      CellType cellType = oldCell.getCellType();

      if (cellType.equals(CellType.FORMULA))
      {
        cellType = oldCell.getCachedFormulaResultType();
      }

      if (cellType.equals(CellType.BOOLEAN))
      {
        newCell.setCellValue(oldCell.getBooleanCellValue());
      }
      else if (cellType.equals(CellType.NUMERIC))
      {
        newCell.setCellValue(oldCell.getNumericCellValue());
      }
      else if (cellType.equals(CellType.STRING))
      {
        newCell.setCellValue(oldCell.getRichStringCellValue());
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
