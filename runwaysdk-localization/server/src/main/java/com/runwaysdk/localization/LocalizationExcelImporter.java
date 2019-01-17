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
package com.runwaysdk.localization;

import java.io.InputStream;

import com.runwaysdk.localization.configuration.SpreadsheetConfiguration;
import com.runwaysdk.localization.progress.LocalizationImportProgressMonitorLogger;

public class LocalizationExcelImporter
{
  protected SpreadsheetConfiguration config;
  
  protected InputStream stream;
  
  public LocalizationExcelImporter(SpreadsheetConfiguration config, InputStream stream)
  {
    this.config = config;
    this.stream = stream;
  }
  
  public void doImport()
  {
    config.setImportProgressMonitor(new LocalizationImportProgressMonitorLogger());
    
    config.doImport(stream);
  }
}
