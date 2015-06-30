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
package com.runwaysdk.query;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.runwaysdk.business.View;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.session.Session;

public class ViewArrayExcelExporter extends ExcelExporter
{

  /**
   * <code>ValueQuery</code> to export
   */
  private View[]       array;

  private List<String> attributes;

  private MdViewDAOIF  mdView;

  public ViewArrayExcelExporter(View[] array, List<String> attributes, MdViewDAOIF mdView, String sheetName)
  {
    super(sheetName);

    this.array = array;
    this.attributes = attributes;
    this.mdView = mdView;
  }

  /**
   * Prepares a new sheet (which represents a type) in the workbook. Fills in
   * all necessary information for the sheet.
   *
   * @return
   */
  protected Sheet prepareSheet()
  {
    Sheet sheet = super.createSheet();

    // Row typeRow = sheet.createRow(0);
    // typeRow.createCell(0).setCellValue(new HSSFRichTextString(type));
    Row labelRow = sheet.createRow(0);

    for(int col = 0; col < attributes.size(); col++)
    {
      String attributeName = attributes.get(col);
      MdAttributeDAOIF mdAttribute = this.getMdAttribute(mdView, attributeName);

      if (mdAttribute != null)
      {
        labelRow.createCell(col).setCellValue(new HSSFRichTextString(mdAttribute.getDisplayLabel(Session.getCurrentLocale())));
      }
    }

    for(int row = 0; row < array.length; row++)
    {
      View view = array[row];

      Row valueRow = sheet.createRow(row + 1);

      for(int col = 0; col < attributes.size(); col++)
      {
        String attributeName = attributes.get(col);
        MdAttributeDAOIF mdAttribute = this.getMdAttribute(mdView, attributeName).getMdAttributeConcrete();

        if (mdAttribute != null)
        {
          String value = view.getValue(attributeName);

          if (mdAttribute instanceof MdAttributeBooleanDAOIF)
          {
            MdAttributeBooleanDAOIF mdAttributeBooleanDAOIF = (MdAttributeBooleanDAOIF) mdAttribute;

            populateBooleanCell(valueRow, col, value, mdAttributeBooleanDAOIF);
          }
          else if (mdAttribute instanceof MdAttributeNumberDAOIF)
          {
            populateNumberCell(valueRow, col, value);
          }
          else if (mdAttribute instanceof MdAttributeDateDAOIF)
          {
            populateDateCell(valueRow, col, value);
          }
          else if (mdAttribute instanceof MdAttributeDateTimeDAOIF)
          {
            populateDateTimeCell(valueRow, col, value);
          }
          else if (mdAttribute instanceof MdAttributeTimeDAOIF)
          {
            populateTimeCell(valueRow, col, value);
          }
          else if (mdAttribute instanceof MdAttributeCharacterDAOIF || mdAttribute instanceof MdAttributeReferenceDAOIF)
          {
            populateCharacterCell(valueRow, col, value);
          }
        }
      }
    }

    return sheet;
  }

  private MdAttributeDAOIF getMdAttribute(MdViewDAOIF mdView, String attributeName)
  {
    if (mdView != null)
    {
      MdAttributeDAOIF attribute = mdView.definesAttribute(attributeName);

      if (attribute == null)
      {
        return this.getMdAttribute(mdView.getSuperClass(), attributeName);
      }

      return attribute;
    }

    return null;
  }
}
