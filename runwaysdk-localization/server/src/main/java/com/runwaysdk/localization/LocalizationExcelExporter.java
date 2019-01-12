package com.runwaysdk.localization;

import java.io.OutputStream;

import com.runwaysdk.localization.configuration.SpreadsheetConfiguration;

public class LocalizationExcelExporter
{
  protected SpreadsheetConfiguration config;
  
  protected OutputStream stream;

  public LocalizationExcelExporter(SpreadsheetConfiguration config, OutputStream stream)
  {
    this.config = config;
    this.stream = stream;
  }

  public void export()
  {
    config.doExport();
    
    config.write(stream);
  }
}
