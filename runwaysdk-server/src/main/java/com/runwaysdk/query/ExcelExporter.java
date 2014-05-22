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
package com.runwaysdk.query;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.runwaysdk.SystemException;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.session.Session;

public abstract class ExcelExporter implements ExporterIF
{
  /**
   * The in memory representation of the xls file
   */
  private HSSFWorkbook  workbook;

  private HSSFCellStyle dateStyle;

  /**
   * Name of the sheet.
   */
  private String        sheetName;

  public ExcelExporter(String sheetName)
  {
    workbook = new HSSFWorkbook();

    dateStyle = workbook.createCellStyle();
    dateStyle.setDataFormat((short) 0xe);

    this.sheetName = sheetName;
  }


  public InputStream exportStream()
  {
    this.prepareSheet();

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    try
    {
      BufferedOutputStream buffer = new BufferedOutputStream(bytes);

      workbook.write(buffer);
      buffer.flush();
      buffer.close();

      return new ByteArrayInputStream(bytes.toByteArray());
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  public Byte[] export()
  {
    this.prepareSheet();

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    try
    {
      BufferedOutputStream buffer = new BufferedOutputStream(bytes);

      workbook.write(buffer);
      buffer.flush();
      buffer.close();

      byte[] byteArray = bytes.toByteArray();
      Byte[] bigByteArray = new Byte[byteArray.length];

      for (int i = 0; i < byteArray.length; i++)
      {
        bigByteArray[i] = Byte.valueOf(byteArray[i]);
      }

      return bigByteArray;
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }
  
  protected HSSFCellStyle getStyle()
  {
    return dateStyle;
  }
  
  protected HSSFSheet createSheet()
  {
    return workbook.createSheet(this.sheetName);
  }
  
  protected void populateCharacterCell(HSSFRow valueRow, int col, String value)
  {
    valueRow.createCell(col).setCellValue(new HSSFRichTextString(value));
  }

  protected void populateTimeCell(HSSFRow valueRow, int col, String value)
  {
    if (value != null && !value.equals(""))
    {
      SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMAT);

      Date date = dateFormat.parse(value, new java.text.ParsePosition(0));

      // Precondition - assumes value is a valid couble.
      valueRow.createCell(col).setCellValue(date);
    }
  }

  protected void populateDateTimeCell(HSSFRow valueRow, int col, String value)
  {
    if (value != null && !value.equals(""))
    {
      SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT);

      Date date = dateFormat.parse(value, new java.text.ParsePosition(0));

      // Precondition - assumes value is a valid couble.
      valueRow.createCell(col).setCellValue(date);
    }
  }

  protected void populateDateCell(HSSFRow valueRow, int col, String value)
  {
    if (value != null && !value.equals(""))
    {
      SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

      Date date = dateFormat.parse(value, new java.text.ParsePosition(0));

      // Precondition - assumes value is a valid couble.
      HSSFCell cell = valueRow.createCell(col);
      cell.setCellValue(date);
      cell.setCellStyle(this.getStyle());
    }
  }

  protected void populateNumberCell(HSSFRow valueRow, int col, String value)
  {
    if (value != null && !value.equals(""))
    {
      // Precondition - assumes value is a valid couble.
      valueRow.createCell(col).setCellValue(Double.valueOf(value));
    }
  }

  protected void populateBooleanCell(HSSFRow valueRow, int col, String value, MdAttributeBooleanDAOIF mdAttributeBooleanDAOIF)
  {
    String displayLabel;

    if (value.equals(MdAttributeBooleanDAOIF.DB_TRUE))
    {
      displayLabel = mdAttributeBooleanDAOIF.getPositiveDisplayLabel(Session.getCurrentLocale());
    }
    else
    {
      displayLabel = mdAttributeBooleanDAOIF.getNegativeDisplayLabel(Session.getCurrentLocale());
    }

    populateCharacterCell(valueRow, col, displayLabel);
  }

  
  protected abstract HSSFSheet prepareSheet();
}
