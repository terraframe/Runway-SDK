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
package com.runwaysdk.controller.table;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class StructColumn extends Column implements ColumnableIF
{
  /**
   * List of columns in the struct
   */
  private List<Column> columns;

  public StructColumn()
  {
    this(new Header(false));
  }
  
  public StructColumn(Header header)
  {
    super(header);
    
    this.columns = new LinkedList<Column>();    
  }

  public void addColumn(Column column)
  {
    columns.add(column);
    column.setColumnable(this);
  }
  
  @Override
  public void visitHeader(TableVisitor visitor) throws IOException
  {
    this.getHeader().accept(visitor, 1, columns.size());
  }
  
  @Override
  public void visitStructHeader(TableVisitor visitor) throws IOException
  {
    for (Column column : columns)
    {
      column.visitHeader(visitor);
    }
  }

  @Override
  public void visitRow(int i, TableVisitor visitor, String classes) throws IOException
  {
    for (Column column : columns)
    {
      column.visitRow(i, visitor, classes);
    }
  }

  @Override
  public void visitFooter(TableVisitor visitor) throws IOException
  {
    for (Column column : columns)
    {
      column.visitFooter(visitor);
    }
  }
}
