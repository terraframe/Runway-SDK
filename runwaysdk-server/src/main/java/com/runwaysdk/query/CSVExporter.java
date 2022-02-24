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
package com.runwaysdk.query;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;

public abstract class CSVExporter implements ExporterIF
{
  protected static String DELIMETER = ",";

  private StringBuffer  buffer;
  
  private DateFormat dateFormat;
  private DateFormat dateTimeFormat;
  private DateFormat timeFormat;

  public CSVExporter()
  {
    this(null, null, null);
  }
  
  public CSVExporter(DateFormat dateFormat, DateFormat dateTimeFormat, DateFormat timeFormat)
  {
    this.buffer = new StringBuffer();
    this.dateFormat = dateFormat;
    this.dateTimeFormat = dateTimeFormat;
    this.timeFormat = timeFormat;
  }

  public InputStream exportStream()
  {
    String csv = this.buffer.toString();

    try
    {
      return new ByteArrayInputStream(csv.getBytes("UTF-8"));
    }
    catch (UnsupportedEncodingException e)
    {
      return new ByteArrayInputStream(csv.getBytes());
    }
  }

  public Byte[] export()
  {
    String csv = this.buffer.toString();

    byte[] byteArray = csv.getBytes();
    Byte[] bigByteArray = new Byte[byteArray.length];

    for (int i = 0; i < byteArray.length; i++)
    {
      bigByteArray[i] = Byte.valueOf(byteArray[i]);
    }

    return bigByteArray;
  }
  
  protected void addRow(StringBuffer row)
  {
    buffer.append(row.toString().replaceFirst(DELIMETER, ""));
  }

  protected void populateCharacterCell(StringBuffer row, String value)
  {
    row.append(DELIMETER + "\"" + value + "\"");
  }

  protected void populateTimeCell(StringBuffer row, String value)
  {
    if (value != null && !value.equals(""))
    {
      SimpleDateFormat parser = new SimpleDateFormat(Constants.TIME_FORMAT);

      Date date = parser.parse(value, new java.text.ParsePosition(0));

      String exportValue = date.toString();
      
      if(timeFormat != null)
      {
        exportValue = timeFormat.format(date);
      }

      // Precondition - assumes value is a valid time.
      row.append(DELIMETER + exportValue);
    }
    else
    {
      row.append(DELIMETER + "\"\"");
    }
  }

  protected void populateDateTimeCell(StringBuffer row, String value)
  {
    if (value != null && !value.equals(""))
    {
      SimpleDateFormat parser = new SimpleDateFormat(Constants.DATETIME_FORMAT);

      Date date = parser.parse(value, new java.text.ParsePosition(0));

      String exportValue = date.toString();
      
      if(dateTimeFormat != null)
      {
        exportValue = dateTimeFormat.format(date);
      }

      // Precondition - assumes value is a valid date time.
      row.append(DELIMETER + exportValue);
    }
    else
    {
      row.append(DELIMETER + "\"\"");
    }
  }

  protected void populateDateCell(StringBuffer row, String value)
  {
    if (value != null && !value.equals(""))
    {
      SimpleDateFormat parser = new SimpleDateFormat(Constants.DATE_FORMAT);
      
      Date date = parser.parse(value, new java.text.ParsePosition(0));
      
      String exportValue = date.toString();
      
      if(dateFormat != null)
      {
        exportValue = dateFormat.format(date);
      }

      // Precondition - assumes value is a valid date.
      row.append(DELIMETER + exportValue);
    }
    else
    {
      row.append(DELIMETER + "\"\"");
    }
  }

  protected void populateNumberCell(StringBuffer row, String value)
  {
    if (value != null && !value.equals(""))
    {
      // Precondition - assumes value is a valid double.
      row.append(DELIMETER + Double.valueOf(value));
    }
    else
    {
      row.append(DELIMETER + "\"\"");
    }
  }

  protected void populateBooleanCell(StringBuffer row, String value, MdAttributeBooleanDAOIF mdAttributeBooleanDAOIF)
  {
    String displayLabel;

    // exports as 1 and 0 as per #2735
    if(value == null || value.trim().length() == 0)
    {
      displayLabel = "";
    }
    else if (Boolean.parseBoolean(value))
    {
      displayLabel = MdAttributeBooleanDAOIF.DB_TRUE;
    }
    else
    {
      displayLabel = MdAttributeBooleanDAOIF.DB_FALSE;
    }

    populateCharacterCell(row, displayLabel);
  }
}
