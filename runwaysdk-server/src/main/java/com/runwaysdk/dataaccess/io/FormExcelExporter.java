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

import java.util.List;

import com.runwaysdk.dataaccess.io.excel.ColumnFactory;
import com.runwaysdk.dataaccess.io.excel.MdFieldFilter;

public class FormExcelExporter extends ExcelExporter
{
  private MdFieldFilter filter;

  private ColumnFactory factory;

  public FormExcelExporter(MdFieldFilter filter)
  {
    super();

    this.filter = filter;
    this.factory = new ColumnFactory();
  }

  public FormExcelExporter(MdFieldFilter filter, ColumnFactory factory)
  {
    super();

    this.filter = filter;
    this.factory = factory;
  }

  @Override
  public ExcelExportSheet addTemplate(String type)
  {
    ExcelExportSheet sheet = new FormExcelExportSheet(this.getListeners(), filter, factory);
    sheet.addTemplate(type);

    this.addSheet(sheet);

    return sheet;
  }
  
  @Override
  public ExcelExportSheet addTemplate(String type, List<ExcelExportListener> listeners)
  {
    ExcelExportSheet sheet = new FormExcelExportSheet(listeners, filter, factory);
    sheet.addTemplate(type);
    
    this.addSheet(sheet);
    
    return sheet;
  }
}
