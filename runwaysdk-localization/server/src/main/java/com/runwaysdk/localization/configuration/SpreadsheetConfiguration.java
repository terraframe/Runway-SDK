package com.runwaysdk.localization.configuration;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.runwaysdk.SystemException;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.localization.LocaleDimension;
import com.runwaysdk.localization.progress.LocalizationImportProgressMonitorIF;
import com.runwaysdk.localization.progress.LocalizationImportProgressMonitorLogger;

public class SpreadsheetConfiguration
{
  private LocalizationImportProgressMonitorIF progressMonitor;
  
  private ArrayList<TabConfiguration> tabs = new ArrayList<TabConfiguration>();
  
  private Workbook workbook;
  
  protected List<LocaleDimension> dimensions;
  
  public SpreadsheetConfiguration()
  {
    this.dimensions = new ArrayList<LocaleDimension>();
    this.addLocaleDimensions(MdAttributeLocalInfo.DEFAULT_LOCALE);
  }
  
  private void addLocaleDimensions(String localeString)
  {
    dimensions.add(new LocaleDimension(localeString));
    for (MdDimensionDAOIF dim : MdDimensionDAO.getAllMdDimensions())
    {
      dimensions.add(new LocaleDimension(localeString, dim));
    }
  }
  
  public void addTab(TabConfiguration tab)
  {
    this.tabs.add(tab);
  }
  
  protected void buildTabs()
  {
    for (TabConfiguration tab : tabs)
    {
      tab.setDimensions(dimensions);
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
    catch (EncryptedDocumentException | InvalidFormatException | IOException e)
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
