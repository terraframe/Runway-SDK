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
package com.runwaysdk.dataaccess.io;

import java.io.File;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class DatabaseVersioning extends Versioning
{
  /**
   * List of timestamps which have already been imported
   */
  protected Set<Date> timestamps;

  public DatabaseVersioning(String location, String xsd)
  {
    super(location, xsd);

    timestamps = new TreeSet<Date>();

    // Get a list of all the imported versions
    List<String> values = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY);

    for (String timestamp : values)
    {
      timestamps.add(new Date(Long.parseLong(timestamp)));
    }
  }

  @Override
  protected void performDoIt(File file, Date timestamp)
  {
    // Only perform the doIt if this file has not already been imported
    if (!timestamps.contains(timestamp))
    {
      Database.addPropertyValue(Database.VERSION_NUMBER, MdAttributeCharacterInfo.CLASS, new TimeFormat(timestamp.getTime()).format(), Database.VERSION_TIMESTAMP_PROPERTY);

      super.performDoIt(file, timestamp);

      timestamps.add(timestamp);
    }
  }

  @Override
  protected void performUndoIt(File file, Date timestamp)
  {
    // Only perform the undoIt if this file has already been imported
    if (timestamps.contains(timestamp))
    {
      super.performUndoIt(file, timestamp);

      Database.removePropertyValue(Database.VERSION_NUMBER, MdAttributeCharacterInfo.CLASS, new TimeFormat(timestamp.getTime()).format(), Database.VERSION_TIMESTAMP_PROPERTY);

      timestamps.remove(timestamp);
    }
  }


  @Transaction
  public void changeVersion(Date timestamp)
  {
    List<File> after = this.getFilesAfter(timestamp);

    Collections.reverse(after);

    this.performUndoIt(after);

    List<File> before = this.getFilesBefore(timestamp);

    if (map.containsKey(timestamp))
    {
      before.add(map.get(timestamp));
    }

    this.performDoIt(before);
  }

  @Transaction
  public void rollBackNversions(int numToRollback) throws ParseException
  {
    // Get the previous 'numToRollback' schemas which have been imported
    numToRollback = Math.min(numToRollback, timestamps.size());

    List<Date> reversed = new LinkedList<Date>(timestamps);
    Collections.reverse(reversed);

    if (reversed.size() > 0)
    {
      Date timestamp = reversed.get(numToRollback);

      this.changeVersion(timestamp);
    }
  }
}
