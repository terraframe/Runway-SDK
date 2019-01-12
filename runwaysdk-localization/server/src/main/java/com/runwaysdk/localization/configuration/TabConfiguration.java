package com.runwaysdk.localization.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.localization.LocaleDimension;
import com.runwaysdk.localization.exception.ExpectedColumnException;
import com.runwaysdk.localization.exception.ExpectedSheetException;
import com.runwaysdk.localization.progress.LocalizationImportProgressMonitorIF;

public abstract class TabConfiguration
{
  protected String sheetName;
  
  protected Sheet sheet;
  
  protected ArrayList<ColumnConfiguration> columns = new ArrayList<ColumnConfiguration>();
  
  protected Workbook workbook;
  
  protected Row headerRow;
  
  protected List<LocaleDimension> dimensions;

  protected LocalizationImportProgressMonitorIF importProgressMonitor;
  
  public TabConfiguration(String sheetName, List<ColumnConfiguration> columns)
  {
    this.sheetName = sheetName;
    
    this.columns.addAll(columns);
  }
  
  public List<LocaleDimension> getDimensions()
  {
    return dimensions;
  }

  public void setDimensions(List<LocaleDimension> dimensions)
  {
    this.dimensions = dimensions;
  }
  
  protected void buildColumns()
  {
    LocaleMultiColumnConfiguration locales = new LocaleMultiColumnConfiguration(dimensions);
    columns.add(locales);
    
    for (int i = 0; i < columns.size(); i++)
    {
      ColumnConfiguration column = columns.get(i);
      
      column.setIndex(i);
    }
  }
  
  public ColumnConfiguration getColumnByAttribute(String attributeName)
  {
    for (ColumnConfiguration column : columns)
    {
      if (column.getDataAttribute() != null && column.getDataAttribute().equals(attributeName))
      {
        return column;
      }
    }
    
    return null;
  }
  
  public ColumnConfiguration getColumnByLabel(String headerLabel)
  {
    for (ColumnConfiguration column : columns)
    {
      if (column.getHeaderLabel() != null && column.getHeaderLabel().equals(headerLabel))
      {
        return column;
      }
    }
    
    return null;
  }

  public void doExport(Workbook workbook)
  {
    this.workbook = workbook;
    
    this.sheet = this.workbook.createSheet(this.sheetName);
    
    buildColumns();
    
    exportHeaders();
    
    exportData();
    
    for (int i = 0; i < columns.size(); i++)
    {
      ColumnConfiguration column = columns.get(i);
      
      column.autoSize(this.workbook, this.sheet, this.headerRow);
    }
  }
  
  public void doImport(Workbook workbook)
  {
    this.workbook = workbook;
    
    this.sheet = this.workbook.getSheet(this.sheetName);
    
    if (this.sheet == null)
    {
      ExpectedSheetException ex = new ExpectedSheetException();
      ex.setSheetName(this.sheetName);
      throw ex;
    }
    
    buildColumns();
    
    importHeaders();
    
    importData();
  }
  
  protected void importHeaders()
  {
    Row row = sheet.getRow(0);
    Iterator<Cell> cellIterator = row.cellIterator();

    int index = 0;
    while (cellIterator.hasNext())
    {
      Cell cell = cellIterator.next();
      
      ColumnConfiguration colConfig = this.getColumnByLabel(cell.getStringCellValue());
      if (colConfig != null)
      {
        colConfig.setIndex(index);
      }
      
      index++;
    }
    
    for (ColumnConfiguration colConfig : this.columns)
    {
      if (colConfig.getIndex() == null && colConfig.getDataAttribute() != null)
      {
        ExpectedColumnException ex = new ExpectedColumnException();
        ex.setSheetName(this.sheetName);
        ex.setColumnName(colConfig.getHeaderLabel());
        throw ex;
      }
    }
  }
  
  protected void exportHeaders()
  {
    this.headerRow = sheet.createRow(0);
    
    for (short i = 0; i < columns.size(); ++i)
    {
      ColumnConfiguration column = columns.get(i);
      
      column.exportHeader(this.workbook, this.sheet, this.headerRow);
    }
  }
  
  protected void importData()
  {
    
  }
  
  protected void exportData()
  {
    
  }

  public void setImportProgressMonitor(LocalizationImportProgressMonitorIF progressMonitor)
  {
    this.importProgressMonitor = progressMonitor;
  }
}
