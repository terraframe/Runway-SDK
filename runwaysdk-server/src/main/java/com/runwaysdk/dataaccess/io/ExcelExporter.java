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
package com.runwaysdk.dataaccess.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.SystemException;

/**
 * Exports an excel template that can later be imported with
 * {@link ExcelImporter}. The first cell bears the name of the fully qualified
 * type to import. Each cell in the second row much holds the name of an
 * attribute on that type (or struct.attributeName). The third row shows the
 * display label for the attribute in that column. Actual data starts with the
 * fourth row.
 * 
 * @author Eric
 */
public class ExcelExporter
{
  /**
   * List of registered listeners for this exporter.
   */
  private List<ExcelExportListener> listeners;

  /**
   * The in memory representation of the xls file
   */
  private Workbook                  workbook;

  private CellStyle                 boldStyle = null;

  private List<ExcelExportSheet>    sheets;

  /**
   * Simple constructor that sets up the workbook
   */
  public ExcelExporter()
  {
    this.workbook = new HSSFWorkbook();
    this.listeners = new LinkedList<ExcelExportListener>();
    this.sheets = new LinkedList<ExcelExportSheet>();

    Font font = workbook.createFont();
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);

    this.boldStyle = workbook.createCellStyle();
    this.boldStyle.setFont(font);
  }

  public ExcelExportSheet addTemplate(String type)
  {
    return this.addTemplate(type, listeners);
  }

  public ExcelExportSheet addTemplate(String type, List<ExcelExportListener> listeners)
  {
    ExcelExportSheet sheet = new ExcelExportSheet(listeners);
    sheet.addTemplate(type);

    this.addSheet(sheet);

    return sheet;
  }

  public void addSheet(ExcelExportSheet sheet)
  {
    this.sheets.add(sheet);
  }

  /**
   * Adds the given listener to this Exporter
   * 
   * @param listener
   */
  public void addListener(ExcelExportListener listener)
  {
    listeners.add(listener);
  }

  public List<ExcelExportListener> getListeners()
  {
    return listeners;
  }

  /**
   * Writes the workbook to a byte array, which can then be written to the
   * filesystem or streamed across the net.
   * 
   * @return
   */
  public byte[] write()
  {
    for (ExcelExportSheet sheet : sheets)
    {
      sheet.createSheet(workbook, boldStyle);
    }

    // Notify the listeners
    for (ExcelExportListener listener : listeners)
    {
      listener.preWrite(workbook);
    }

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedOutputStream buffer = new BufferedOutputStream(bytes);
    try
    {
      workbook.write(buffer);
      buffer.flush();
      buffer.close();
      return bytes.toByteArray();
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }
}
