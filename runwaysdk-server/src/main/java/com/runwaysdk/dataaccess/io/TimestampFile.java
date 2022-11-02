/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimestampFile
{
  private static final String DATE_PATTEN  = "\\d{13,}";

  private static final String NAME_PATTERN = "^[A-Za-z_\\-\\d\\.\\s]*\\((" + DATE_PATTEN + ")\\)[A-Za-z_\\-\\d\\.\\s]*.xml$";

  private File                file;

  public TimestampFile(File file)
  {
    this.file = file;
  }

  public Long getTimestamp()
  {
    Pattern namePattern = Pattern.compile(NAME_PATTERN);
    Matcher nameMatcher = namePattern.matcher(file.getName());

    if (nameMatcher.find())
    {
      Long timeStamp = Long.parseLong(nameMatcher.group(1));
      return timeStamp;
    }

    return null;
  }

  public Date getDate()
  {
    Long timeStamp = getTimestamp();

    if (timeStamp != null)
    {
      return new Date(timeStamp);
    }

    return null;
  }

  public int compareTo(TimestampFile file)
  {
    return this.getTimestamp().compareTo(file.getTimestamp());
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof TimestampFile)
    {
      return this.getTimestamp().equals( ( (TimestampFile) obj ).getTimestamp());
    }

    return super.equals(obj);
  }

  public static List<File> getTimestampedFiles(File directory)
  {
    List<File> list = new LinkedList<File>();

    if (directory.isDirectory())
    {
      Pattern namePattern = Pattern.compile(NAME_PATTERN);

      File[] files = directory.listFiles();

      if (files != null)
      {
        for (File file : files)
        {
          String name = file.getName();
          Matcher nameMatcher = namePattern.matcher(name);

          if (nameMatcher.find())
          {
            list.add(file);
          }
        }
      }
    }

    return list;
  }
}
