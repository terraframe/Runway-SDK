package com.runwaysdk.localization.configuration;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.system.metadata.MdAttributeLocal;

public class AttributeLocalAttributeColumnConfiguration extends ColumnConfiguration
{
  public AttributeLocalAttributeColumnConfiguration(String headerLabel)
  {
    super(headerLabel, MdAttributeLocal.ATTRIBUTENAME);
  }

  public void exportData(Workbook workbook, Sheet sheet, Row row, EntityDAOIF entity, MdLocalStructDAOIF mdLocalStruct, StructDAOIF struct, String attributeName)
  {
    row.createCell(index).setCellValue(workbook.getCreationHelper().createRichTextString(attributeName));
  }
}
