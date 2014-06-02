/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.io.excel;

import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.business.Entity;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.dataaccess.io.ExcelExportListener;

public class ExcelAdapter implements ExcelExportListener, ImportApplyListener
{

  @Override
  public void addColumns(List<ExcelColumn> extraColumns)
  {
  }

  @Override
  public void preHeader(ExcelColumn columnInfo)
  {
  }

  @Override
  public void preWrite(Workbook workbook)
  {
  }

  @Override
  public void beforeApply(Mutable instance)
  {
  }

  @Override
  public void afterApply(Mutable instance)
  {
  }

  @Override
  public void handleExtraColumns(Mutable instance, List<ExcelColumn> extraColumns, Row row) throws Exception
  {
  }

  @Override
  public void addAdditionalEntities(HashMap<String, List<Entity>> newParam)
  {
  }

  @Override
  public void validate(Mutable instance, HashMap<String, List<Entity>> entities)
  {
  }
}
