package com.runwaysdk.localization.progress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalizationImportProgressMonitorLogger implements LocalizationImportProgressMonitorIF
{

  private static final Logger logger = LoggerFactory.getLogger(LocalizationImportProgressMonitorLogger.class);
  
  private int recordCount;
  
  public LocalizationImportProgressMonitorLogger()
  {
    recordCount = 0;
  }
  
  @Override
  public void onFinishImport()
  {
    logger.info("Finished importing " + recordCount + " localization records.");
  }

  @Override
  public void onImportRecord()
  {
    recordCount++;
  }

}
