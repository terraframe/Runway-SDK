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
package com.runwaysdk.controller.table;

import java.io.IOException;

/**
 * Subclass of Header such that the header is a link
 * which results in the table data being sorted by
 * the header attribute.
 * 
 * @author jsmethie
 */
public class SortableHeader extends Header
{  
  
  public SortableHeader()
  {
    super();
  }

  public SortableHeader(boolean populated)
  {
    super(populated);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.controller.table.Header#accept(com.runwaysdk.controller.table.TableVisitor, int, int)
   */
  public void accept(TableVisitor visitor, int rowspan, int colspan) throws IOException
  {
    visitor.visitSortableHeader(this, rowspan, colspan);
  }

  /**
   * @return The value of the GET 'sortAttribute' parameter
   */
  public String getSortAttribute()
  {
    Column column = this.getColumn();
    ColumnableIF columnable = column.getColumnable();
    String attribute = column.getAttributeName();
    
    if(columnable instanceof StructColumn)
    {
      StructColumn structColumn = ((StructColumn) columnable);
      
      attribute = structColumn.getAttributeName() + "-" + attribute;      
    }
    
    return attribute;
  }
}
