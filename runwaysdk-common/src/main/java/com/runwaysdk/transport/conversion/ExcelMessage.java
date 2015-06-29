/**
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
 */
package com.runwaysdk.transport.conversion;

import java.io.Serializable;

public class ExcelMessage implements Serializable
{
  private static final long serialVersionUID = -6131410504465824577L;

  /**
   * The row number in the error workbook
   */
  private int row;
  
  /**
   * The display label for the error column.  May be empty for some messages
   */
  private String column;
  
  /**
   * The localized message from the root exception
   */
  private String message;
  
  /**
   * The name of the Attribute this message is about.  Can be null.
   */
  private transient String attributeName;
  
  public ExcelMessage(int row, String message)
  {
    this(row, "", message);
  }
  
  public ExcelMessage(int row, String column, String message)
  {
    this(row, column, message, null);
  }
  
  public ExcelMessage(int row, String column, String message, String attributeName)
  {
    this.row = row;
    this.column = column;
    this.message = message;
    this.attributeName = attributeName;
  }

  public int getRow()
  {
    return row;
  }

  public String getColumn()
  {
    return column;
  }

  public String getMessage()
  {
    return message;
  }
  
  public String getMdAttribute()
  {
    return attributeName;
  }
}
