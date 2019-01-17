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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.StructDAOIF;

abstract public class ColumnConfiguration
{
  protected String headerLabel;
  
  protected String dataAttribute;
  
  protected Integer index;

  public ColumnConfiguration(String headerLabel, String dataAttribute)
  {
    this.headerLabel = headerLabel;
    this.dataAttribute = dataAttribute;
    this.index = null;
  }
  
  public Integer getIndex()
  {
    return index;
  }

  public void setIndex(Integer index)
  {
    this.index = index;
  }

  public String getHeaderLabel()
  {
    return headerLabel;
  }

  public void setHeaderLabel(String header)
  {
    this.headerLabel = header;
  }

  public String getDataAttribute()
  {
    return dataAttribute;
  }

  public void setDataAttribute(String dataAttribute)
  {
    this.dataAttribute = dataAttribute;
  }

  public void exportHeader(Workbook workbook, Sheet sheet, Row headerRow)
  {
    headerRow.createCell(index).setCellValue(workbook.getCreationHelper().createRichTextString(headerLabel));
  }

  public void autoSize(Workbook workbook, Sheet sheet, Row headerRow)
  {
    sheet.autoSizeColumn(index);
  }

  abstract public void exportData(Workbook workbook, Sheet sheet, Row row, EntityDAOIF entity, MdLocalStructDAOIF mdLocalStruct, StructDAOIF struct);
}
