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
package com.runwaysdk.controller.table;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Defines and Entry in a Column.  An Entry can be a Header, Row, or Footer object
 * 
 * @author jsmethie
 */
public class Entry
{
  /**
   * Flag denoting if the Entry has been populated
   */
  private boolean      populated;

  /**
   * Writer used to capture any pre or post html generated when defining this object.
   */
  private StringWriter writer;
  
  /**
   * The Column in which the Entry belongs
   */
  private Column       column;
  
  private String       classes;

  public Entry()
  {
    this(false);
  }

  public Entry(boolean populated)
  {
    this.populated = populated;
    this.writer = new StringWriter();
    this.classes = null;
  }

  public void setColumn(Column column)
  {
    this.column = column;
  }
  
  public Column getColumn()
  {
    return column;
  }
  
  public void setPopulated(boolean populated)
  {
    this.populated = populated;
  }

  public boolean isPopulated()
  {
    return populated;
  }
  
  public String getClasses()
  {
    return classes;
  }

  public void setClasses(String classes)
  {
    this.classes = classes;
  }

  public StringWriter getWriter()
  {
    return writer;
  }

  public void accept(TableVisitor visitor) throws IOException
  {
    visitor.visitEntry(this);
  }
}
