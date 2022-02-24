/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.dataaccess.io;

import java.util.List;

import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdFormDAOIF;
import com.runwaysdk.dataaccess.MdWebAttributeDAOIF;
import com.runwaysdk.dataaccess.io.excel.ColumnFactory;
import com.runwaysdk.dataaccess.io.excel.MdFieldFilter;
import com.runwaysdk.dataaccess.metadata.MdFormDAO;

public class FormExcelExportSheet extends ExcelExportSheet
{
  private MdFieldFilter filter;

  private ColumnFactory factory;

  public FormExcelExportSheet(List<ExcelExportListener> listeners, MdFieldFilter filter, ColumnFactory factory)
  {
    super(listeners);

    this.filter = filter;
    this.factory = factory;
  }

  public FormExcelExportSheet(ExcelSheetMetadata metadata, List<ExcelExportListener> listeners, MdFieldFilter filter, ColumnFactory factory)
  {
    super(metadata, listeners);
    
    this.filter = filter;
    this.factory = factory;
  }
  
  @Override
  public void addTemplate(String type)
  {
    MdFormDAOIF mdForm = (MdFormDAOIF) MdFormDAO.getMdTypeDAO(type);
    MdClassDAOIF mdClass = mdForm.getFormMdClass();

    this.setType(mdClass.definesType());
    this.prepareColumns(type);
    this.addColumnsFromListeners();
  }

  /**
   * Converts a list of MdAttributes into a list of ColumnInfos, storing any
   * necessary metadata in the process.
   * 
   * @param mdClass
   */
  protected void prepareColumns(String type)
  {
    MdFormDAOIF mdForm = (MdFormDAOIF) MdFormDAO.getMdTypeDAO(type);

    List<? extends MdFieldDAOIF> mdFields = mdForm.getOrderedMdFields();

    for (MdFieldDAOIF mdField : mdFields)
    {
      if (filter.accept(mdField) && mdField instanceof MdWebAttributeDAOIF)
      {
        this.addExpectedColumn(factory.getColumn((MdWebAttributeDAOIF) mdField));
      }
    }
  }
}
