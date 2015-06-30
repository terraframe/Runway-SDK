/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io.excel;

import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.UnexpectedTypeException;
import com.runwaysdk.dataaccess.io.ExcelImporter.ImportContext;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;

public class ContextBuilder implements ContextBuilderIF
{

  @Override
  public ImportContext createContext(Sheet sheet, String sheetName, Workbook errorWorkbook, String type)
  {
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(type);
    if (! ( mdClass instanceof MdViewDAO ) && ! ( mdClass instanceof MdBusinessDAO ))
    {
      throw new UnexpectedTypeException("Excel Importer does not support type [" + mdClass.definesType() + "]");
    }

    Sheet error = errorWorkbook.createSheet(sheetName);
    return new ImportContext(sheet, sheetName, error, mdClass);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void configure(ImportContext currentContext, Row typeRow, Row nameRow, Row labelRow)
  {
    // Copy the type, name, and label rows to the error sheet
    currentContext.addErrorRow(typeRow);
    currentContext.addErrorRow(nameRow);
    currentContext.addErrorRow(labelRow);

    // To start, assume that everything is an extra column. We'll move expected
    // ones to the correct list soon
    Iterator<Cell> nameIterator = nameRow.cellIterator();
    Iterator<Cell> labelIterator = labelRow.cellIterator();
    while (nameIterator.hasNext())
    {
      Cell name = nameIterator.next();
      Cell label = labelIterator.next();
      currentContext.addExtraColumn(new ExcelColumn(ExcelUtil.getString(name), ExcelUtil.getString(label), name.getColumnIndex()));
    }

    // Build columns for all of the expected attributes
    List<? extends MdAttributeDAOIF> attributes = this.getAttributes(currentContext);

    for (MdAttributeDAOIF mdAttribute : attributes)
    {
      this.buildAttributeColumn(currentContext, mdAttribute);
    }

    // Map the index for the expected types
    Iterator<AttributeColumn> expectedIterator = currentContext.getExpectedColumns().iterator();
    while (expectedIterator.hasNext())
    {
      ExcelColumn expected = expectedIterator.next();
      boolean match = false;

      Iterator<ExcelColumn> extraIterator = currentContext.getExtraColumns().iterator();
      while (extraIterator.hasNext())
      {
        ExcelColumn extra = extraIterator.next();
        if (extra.equals(expected))
        {
          extraIterator.remove();
          expected.setIndex(extra.getIndex());
          match = true;
          break;
        }
      }

      // No matches found for the expected column. We need to remove it.
      if (!match)
      {
        expectedIterator.remove();
      }
    }
    // At this point every column is either in the expected list or the extra
    // list.
  }

  protected List<? extends MdAttributeDAOIF> getAttributes(ImportContext currentContext)
  {
    return (List<? extends MdAttributeDAOIF>) ExcelUtil.getAttributes(currentContext.getMdClass(), new DefaultExcelAttributeFilter());
  }

  protected void buildAttributeColumn(ImportContext context, MdAttributeDAOIF mdAttribute)
  {
    if (mdAttribute.getMdAttributeConcrete() instanceof MdAttributeStructDAOIF)
    {
      MdAttributeStructDAOIF mdAttributeStruct = (MdAttributeStructDAOIF) mdAttribute.getMdAttributeConcrete();
      MdStructDAOIF mdStruct = mdAttributeStruct.getMdStructDAOIF();
      List<? extends MdAttributeDAOIF> structAttributes = ExcelUtil.getAttributes(mdStruct, new DefaultExcelAttributeFilter());

      for (MdAttributeDAOIF structAttribute : structAttributes)
      {
        context.addExpectedColumn(new StructColumn(mdAttributeStruct, structAttribute));
      }
    }
    else
    {
      context.addExpectedColumn(new AttributeColumn(mdAttribute));
    }
  }
}
