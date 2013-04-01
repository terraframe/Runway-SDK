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
package com.runwaysdk.dataaccess.io;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.io.excel.AttributeColumn;
import com.runwaysdk.dataaccess.io.excel.DefaultExcelAttributeFilter;
import com.runwaysdk.dataaccess.io.excel.ExcelColumn;
import com.runwaysdk.dataaccess.io.excel.ExcelUtil;
import com.runwaysdk.dataaccess.io.excel.StructColumn;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;

public class ExcelExportSheet
{
  public static final int           HEADER_ROW_COUNT = 3;

  private List<ExcelExportListener> listeners;

  /**
   * The fully qualified type that's being exported
   */
  private String                    type;

  /**
   * A list of columns that are defined by metadata and thus are expected
   */
  private List<ExcelColumn>         expectedColumns;

  /**
   * A list of columns that are in the file but not associated with metadata
   */
  private List<ExcelColumn>         extraColumns;

  /**
   * A list of entities which represent a row
   */
  private List<ComponentIF>         components;

  public ExcelExportSheet(List<ExcelExportListener> listeners)
  {
    this.listeners = listeners;
    this.expectedColumns = new ArrayList<ExcelColumn>();
    this.extraColumns = new ArrayList<ExcelColumn>();
    this.components = new ArrayList<ComponentIF>();
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public void addExtraColumn(ExcelColumn column)
  {
    this.extraColumns.add(column);
  }

  public void addExpectedColumn(ExcelColumn column)
  {
    this.expectedColumns.add(column);
  }

  /**
   * Specifies the type we want to export. Future exporters will support
   * multiple sheets, thus the term 'add' instead of 'set.'
   * 
   * @param type
   */
  public void addTemplate(String type)
  {
    this.setType(type);

    this.prepareColumns(type);
    this.addColumnsFromListeners();
  }

  protected void addColumnsFromListeners()
  {
    for (ExcelExportListener listener : listeners)
    {
      listener.addColumns(extraColumns);
    }
  }

  public String getType()
  {
    return this.type;
  }

  public List<ExcelColumn> getExpectedColumns()
  {
    return expectedColumns;
  }

  public List<ExcelColumn> getExtraColumns()
  {
    return extraColumns;
  }

  public void addRow(ComponentIF entity)
  {
    this.components.add(entity);
  }

  /**
   * Converts a list of MdAttributes into a list of ColumnInfos, storing any
   * necessary metadata in the process.
   * 
   * @param mdClass
   */
  protected void prepareColumns(String type)
  {
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(type);
    List<? extends MdAttributeDAOIF> mdAttributeDAOs = ExcelUtil.getAttributes(mdClass, new DefaultExcelAttributeFilter());

    // Store relevant information about all the attributes
    for (MdAttributeDAOIF mdAttribute : mdAttributeDAOs)
    {
      if (mdAttribute.getMdAttributeConcrete() instanceof MdAttributeStructDAOIF)
      {
        MdAttributeStructDAOIF struct = (MdAttributeStructDAOIF) mdAttribute.getMdAttributeConcrete();
        MdStructDAOIF mdStruct = struct.getMdStructDAOIF();
        List<? extends MdAttributeDAOIF> structAttributes = ExcelUtil.getAttributes(mdStruct, new DefaultExcelAttributeFilter());

        for (MdAttributeDAOIF structAttribute : structAttributes)
        {
          this.addExpectedColumn(new StructColumn(struct, structAttribute));
        }
      }
      else
      {
        this.addExpectedColumn(new AttributeColumn(mdAttribute));
      }
    }
  }
  
  protected String getFormattedSheetName()
  {
    String sheetName = this.getSheetName();
    sheetName = sheetName.replace("\\", " ");
    sheetName = sheetName.replace("/", " ");
    sheetName = sheetName.replace("?", " ");
    sheetName = sheetName.replace("*", " ");
    sheetName = sheetName.replace("[", " ");
    sheetName = sheetName.replace("]", " ");
    
    return sheetName.substring(0, Math.min(sheetName.length(), 30));
  }

  /**
   * Prepares a new sheet (which represents a type) in the workbook. Fills in
   * all necessary information for the sheet.
   * 
   * @return
   */
  public HSSFSheet createSheet(HSSFWorkbook workbook, HSSFCellStyle boldStyle)
  {
    String sheetName = this.getFormattedSheetName();
    
    HSSFSheet sheet = workbook.createSheet(sheetName);
    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
    HSSFRow typeRow = sheet.createRow(0);
    HSSFRow nameRow = sheet.createRow(1);
    HSSFRow labelRow = sheet.createRow(2);

    int i = 0;
    for (ExcelColumn column : this.getExpectedColumns())
    {
      writeHeader(sheet, patriarch, nameRow, labelRow, i++, column, boldStyle);
    }

    for (ExcelColumn column : this.getExtraColumns())
    {
      writeHeader(sheet, patriarch, nameRow, labelRow, i++, column, boldStyle);
    }

    typeRow.createCell(0).setCellValue(new HSSFRichTextString(this.getType()));

    this.writeRows(sheet);

    return sheet;
  }

  public String getSheetName()
  {
    String[] split = this.getType().split("\\.");
    String typeName = split[split.length - 1];
    return typeName;
  }

  protected void writeHeader(HSSFSheet sheet, HSSFPatriarch patriarch, HSSFRow nameRow, HSSFRow labelRow, int i, ExcelColumn column, HSSFCellStyle boldStyle)
  {
    // Notify the listeners
    for (ExcelExportListener listener : listeners)
    {
      listener.preHeader(column);
    }

    nameRow.createCell(i).setCellValue(new HSSFRichTextString(column.getAttributeName()));

    HSSFCell cell = labelRow.createCell(i);
    cell.setCellValue(new HSSFRichTextString(column.getDisplayLabel()));

    if (column.isRequired() && boldStyle != null)
    {
      cell.setCellStyle(boldStyle);
    }

    if (column.getDescription() != null && column.getDescription().length() > 0)
    {
      HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 4, 4);

      HSSFComment comment = patriarch.createComment(anchor);
      comment.setString(new HSSFRichTextString(column.getDescription()));

      cell.setCellComment(comment);
    }

    sheet.autoSizeColumn((short) i);
  }

  private void writeRows(HSSFSheet sheet)
  {
    List<ExcelColumn> columns = this.getExpectedColumns();

    for (int i = 0; i < components.size(); i++)
    {
      ComponentIF component = components.get(i);
      HSSFRow row = sheet.createRow(HEADER_ROW_COUNT + i);

      for (int j = 0; j < columns.size(); j++)
      {
        ExcelColumn column = columns.get(j);
        String value = component.getValue(column.getAttributeName());
        HSSFCell cell = row.createCell(j);

        column.setValue(cell, value);
      }
    }
  }
}
