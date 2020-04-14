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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.runwaysdk.SystemException;
import com.runwaysdk.localization.progress.LocalizationImportProgressMonitorIF;
import com.runwaysdk.localization.progress.LocalizationImportProgressMonitorLogger;

public class SpreadsheetConfiguration
{
  private LocalizationImportProgressMonitorIF progressMonitor;
  
  private ArrayList<TabConfiguration> tabs = new ArrayList<TabConfiguration>();
  
  private Workbook workbook;
  
  public SpreadsheetConfiguration()
  {
  }
  
  public void addTab(TabConfiguration tab)
  {
    this.tabs.add(tab);
  }
  
  protected void buildTabs()
  {
    for (TabConfiguration tab : tabs)
    {
      tab.setImportProgressMonitor(progressMonitor);
    }
  }
  
  public void doExport()
  {
    workbook = new XSSFWorkbook();
    
    buildTabs();
    
    for (TabConfiguration tab : tabs)
    {
      tab.doExport(workbook);
    }
  }
  
  public void doImport(InputStream stream)
  {
    try
    {
      workbook = WorkbookFactory.create(stream);
      
      buildTabs();
      
      for (TabConfiguration tab : tabs)
      {
        tab.doImport(workbook);
      }
      
      progressMonitor.onFinishImport();
    }
    catch (EncryptedDocumentException | IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void write(OutputStream stream)
  {
    BufferedOutputStream buffer = new BufferedOutputStream(stream);
    
    try
    {
      workbook.write(buffer);
      buffer.flush();
      buffer.close();
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  public void setImportProgressMonitor(LocalizationImportProgressMonitorLogger localizationImportProgressMonitorLogger)
  {
    this.progressMonitor = localizationImportProgressMonitorLogger;
  }
}
