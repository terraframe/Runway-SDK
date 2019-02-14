/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.localization.configuration;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.localization.LocaleDimension;

public class LocaleMultiColumnConfiguration extends ColumnConfiguration
{
  protected List<LocaleDimension> dimensions;

  public LocaleMultiColumnConfiguration(List<LocaleDimension> dimensions)
  {
    super(null, null);
    
    this.dimensions = dimensions;
  }
  
  public void exportHeader(Workbook workbook, Sheet sheet, Row headerRow)
  {
    int i = index;
    
    for (LocaleDimension c : this.dimensions)
    {
      headerRow.createCell(i++).setCellValue(workbook.getCreationHelper().createRichTextString(c.getColumnName()));
    }
  }
  
  public void autoSize(Workbook workbook, Sheet sheet, Row headerRow)
  {
    int i = index;
    
    for (LocaleDimension c : this.dimensions)
    {
      sheet.autoSizeColumn(i++);
    }
  }
  
  public void exportData(Workbook workbook, Sheet sheet, Row row, EntityDAOIF entity, MdLocalStructDAOIF mdLocalStruct, StructDAOIF struct, String attributeName)
  {
    int i = index;
    
    for (LocaleDimension col : dimensions)
    {
      Cell cell = row.createCell(i++);

      if (mdLocalStruct.definesAttribute(col.getAttributeName()) == null)
      {
        continue;
      }

      String value = struct.getValue(col.getAttributeName());
      if (value.trim().length() > 0)
      {
        cell.setCellValue(workbook.getCreationHelper().createRichTextString(value));
      }
    }
  }
}
