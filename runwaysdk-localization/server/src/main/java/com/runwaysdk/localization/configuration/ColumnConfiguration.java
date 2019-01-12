package com.runwaysdk.localization.configuration;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.StructDAOIF;

abstract public class ColumnConfiguration
{
  protected String headerLabel;
  
  protected String dataAttribute;
  
  protected Integer index;

  public ColumnConfiguration(String headerLabel, String dataAttribute)
  {
    this.headerLabel = headerLabel;
    this.dataAttribute = dataAttribute;
    this.index = null;
  }
  
  public Integer getIndex()
  {
    return index;
  }

  public void setIndex(Integer index)
  {
    this.index = index;
  }

  public String getHeaderLabel()
  {
    return headerLabel;
  }

  public void setHeaderLabel(String header)
  {
    this.headerLabel = header;
  }

  public String getDataAttribute()
  {
    return dataAttribute;
  }

  public void setDataAttribute(String dataAttribute)
  {
    this.dataAttribute = dataAttribute;
  }

  public void exportHeader(Workbook workbook, Sheet sheet, Row headerRow)
  {
    headerRow.createCell(index).setCellValue(workbook.getCreationHelper().createRichTextString(headerLabel));
  }

  public void autoSize(Workbook workbook, Sheet sheet, Row headerRow)
  {
    sheet.autoSizeColumn(index);
  }

  abstract public void exportData(Workbook workbook, Sheet sheet, Row row, EntityDAOIF entity, MdLocalStructDAOIF mdLocalStruct, StructDAOIF struct);
}
