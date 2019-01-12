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
