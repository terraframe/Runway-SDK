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
import java.io.StringWriter;
import java.util.List;
import java.util.Vector;


public class Column
{
  /**
   * Header of the Column
   */
  private Header       header;

  /**
   * List of all rows in the column
   */
  private List<Entry>  rows;

  /**
   * Footer of the Column
   */
  private Entry        footer;

  /**
   *
   */
  private StringWriter writer;

  /**
   * Name of the attribute used in the Column
   */
  private String       attributeName;
  
  /**
   * Columnable in which the Column belongs
   */
  private ColumnableIF columnable;

  public Column()
  {
    this(new Header(true));
  }
  
  public Column(Header header)
  {
    this.header = header;
    this.rows = new Vector<Entry>();
    this.footer = new Entry(true);
    this.writer = new StringWriter();
    this.attributeName = null;
    this.columnable = null;
    this.header.setColumn(this);    
  }

  public Header getHeader()
  {
    return header;
  }

  public void setHeader(Header header)
  {
    this.header = header;
    header.setColumn(this);
  }
  
  public boolean isHeaderPopulated()
  {
    return header.isPopulated();
  }
  
  public void setColumnable(ColumnableIF columnable)
  {
    this.columnable = columnable;
  }
  
  public ColumnableIF getColumnable()
  {
    return columnable;
  }

  public List<Entry> getRows()
  {
    return rows;
  }

  public void addRow(Entry row)
  {
    this.rows.add(row);
  }
  
  public boolean hasRows()
  {
    return (this.rows.size() > 0);
  }

  public Entry getFooter()
  {
    return footer;
  }

  public void setFooter(Entry footer)
  {
    this.footer = footer;
  }

  public StringWriter getWriter()
  {
    return writer;
  }

  public String getAttributeName()
  {
    return attributeName;
  }

  public void setAttributeName(String attributeName)
  {
    this.attributeName = attributeName;
  }
  
  public void visitHeader(TableVisitor visitor) throws IOException
  {    
    header.accept(visitor, visitor.getStandardRowspan(), 1);
  }

  public void visitRow(int i, TableVisitor visitor, String classes) throws IOException
  {
    Entry entry = rows.get(i);
    
    entry.setClasses(classes);

    entry.accept(visitor);    
  }
  
  public void visitFooter(TableVisitor visitor) throws IOException
  {
    footer.accept(visitor);
  }

  public void visitStructHeader(TableVisitor visitor) throws IOException
  {
    //Balk: Normal columns do not have any struct headers
  }
}
