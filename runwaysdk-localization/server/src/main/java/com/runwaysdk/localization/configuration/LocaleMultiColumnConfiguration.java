package com.runwaysdk.localization.configuration;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.localization.LocaleDimension;

public class LocaleMultiColumnConfiguration extends ColumnConfiguration
{
  protected List<LocaleDimension> dimensions;

  public LocaleMultiColumnConfiguration(List<LocaleDimension> dimensions)
  {
    super(null, null);
    
    this.dimensions = dimensions;
  }
  
  public void exportHeader(Workbook workbook, Sheet sheet, Row headerRow)
  {
    int i = index;
    
    for (LocaleDimension c : this.dimensions)
    {
      headerRow.createCell(i++).setCellValue(workbook.getCreationHelper().createRichTextString(c.getColumnName()));
    }
  }
  
  public void autoSize(Workbook workbook, Sheet sheet, Row headerRow)
  {
    int i = index;
    
    for (LocaleDimension c : this.dimensions)
    {
      sheet.autoSizeColumn(i++);
    }
  }
  
  public void exportData(Workbook workbook, Sheet sheet, Row row, EntityDAOIF entity, MdLocalStructDAOIF mdLocalStruct, StructDAOIF struct)
  {
    int i = index;
    
    for (LocaleDimension col : dimensions)
    {
      Cell cell = row.createCell(i++);

      if (mdLocalStruct.definesAttribute(col.getAttributeName()) == null)
      {
        continue;
      }

      String value = struct.getValue(col.getAttributeName());
      if (value.trim().length() > 0)
      {
        cell.setCellValue(workbook.getCreationHelper().createRichTextString(value));
      }
    }
  }
}
