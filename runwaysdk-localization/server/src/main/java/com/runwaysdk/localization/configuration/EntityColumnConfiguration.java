package com.runwaysdk.localization.configuration;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.StructDAOIF;

/**
 * A column which grabs its data from the Entity
 */
public class EntityColumnConfiguration extends ColumnConfiguration
{
  public EntityColumnConfiguration(String headerLabel, String dataAttribute)
  {
    super(headerLabel, dataAttribute);
  }

  public void exportData(Workbook workbook, Sheet sheet, Row row, EntityDAOIF entity, MdLocalStructDAOIF mdLocalStruct, StructDAOIF struct)
  {
    row.createCell(index).setCellValue(workbook.getCreationHelper().createRichTextString(entity.getValue(this.getDataAttribute())));
  }
}
