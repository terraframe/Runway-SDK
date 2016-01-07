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
package com.runwaysdk.dataaccess.io;

import java.io.File;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler.Action;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

/**
 * Performs the doIt or undoIt command of all the schema files in a given
 * directory.
 * 
 * @author Justin Smethie
 */
public class Versioning
{
  class VersionComparator implements Comparator<File>
  {
    public int compare(File arg0, File arg1)
    {
      return new TimestampFile(arg0).compareTo(new TimestampFile(arg1));
    }
  }

  /**
   * List of all schema files in the given location in order from earliest to
   * latest
   */
  protected Set<File>       ordered;

  /**
   * Mapping between a file and its timestamp
   */
  protected Map<Date, File> map;

  /**
   * Resource location of the xsd file.
   */
  protected String          xsd;

  public Versioning(String location, String xsd)
  {
    this.xsd = xsd;

    this.map = new HashMap<Date, File>();
    this.ordered = new TreeSet<File>(new VersionComparator());

    File directory = new File(location);

    for (File file : TimestampFile.getTimestampedFiles(directory))
    {
      ordered.add(file);

      map.put(new TimestampFile(file).getDate(), file);
    }
  }

  /**
   * @param timestamp
   * @return
   */
  protected boolean containsTimestamp(Date timestamp)
  {
    return map.containsKey(timestamp);
  }

  protected void remove(Date timestamp)
  {
    File file = map.get(timestamp);

    if (file != null)
    {
      ordered.remove(file);
      map.remove(timestamp);
    }
  }

  protected List<File> getFiles(Date lowerLimit, Date upperLimit)
  {
    List<File> list = new LinkedList<File>();
    Set<File> set = new TreeSet<File>(new VersionComparator());

    for (Date date : map.keySet())
    {
      if (date.after(lowerLimit) && date.before(upperLimit))
      {
        set.add(map.get(date));
      }
    }

    list.addAll(set);

    return list;
  }

  protected List<File> getFilesBefore(Date timestamp)
  {
    List<File> list = new LinkedList<File>();
    Set<File> set = new TreeSet<File>(new VersionComparator());

    for (Date date : map.keySet())
    {
      if (date.before(timestamp))
      {
        set.add(map.get(date));
      }
    }

    list.addAll(set);

    return list;
  }

  protected List<File> getFilesAfter(Date timestamp)
  {
    List<File> list = new LinkedList<File>();
    Set<File> set = new TreeSet<File>(new VersionComparator());

    for (Date date : map.keySet())
    {
      if (date.after(timestamp))
      {
        set.add(map.get(date));
      }
    }

    list.addAll(set);

    return list;
  }

  @Transaction
  public void performDoIt(List<File> files)
  {
    for (File file : files)
    {
      Date date = new TimestampFile(file).getDate();

      this.performDoIt(file, date);
    }
  }

  @Transaction
  public void performUndoIt(List<File> files)
  {
    for (File file : files)
    {
      Date date = new TimestampFile(file).getDate();

      this.performUndoIt(file, date);
    }
  }

  public void undoAll() throws ParseException
  {
    List<File> list = new LinkedList<File>(ordered);

    Collections.reverse(list);

    this.performUndoIt(list);
  }

  @Transaction
  public void doAll() throws ParseException
  {
    List<File> list = new LinkedList<File>(ordered);

    this.performDoIt(list);
  }

  public void changeVersion(Date timestamp)
  {
    List<File> after = this.getFilesAfter(timestamp);

    Collections.reverse(after);

    this.performUndoIt(after);
    this.performDoIt(this.getFilesBefore(timestamp));
  }

  protected void performDoIt(File file, Date date)
  {
    System.out.println("Importing " + file.getName() + "  [" + date + "]");

    VersionHandler.runImport(file, Action.DO_IT, xsd);
  }

  protected void performUndoIt(File file, Date timestamp)
  {
    System.out.println("Undoing " + file.getName() + "  [" + timestamp + "]");

    VersionHandler.runImport(file, Action.UNDO_IT, xsd);
  }

  public Set<File> orderedFileSet()
  {
    return ordered;
  }

  public static void main(String[] args)
  {
    try
    {
      Versioning.run(args);
    }
    finally
    {
      CacheShutdown.shutdown();
    }
  }

  /**
   * @param args
   */
  @Request
  public static void run(String[] args)
  {
    boolean record = true;

    if (args.length < 1)
    {
      String errMsg = "At least one argument is required for Versioning:\n" + "  1) Location of the folder containing the schema(version date).xml files\n" + "  2) xsd file to use (optional)";
      throw new CoreException(errMsg);
    }

    String xsd;
    if (args.length == 1)
    {
      xsd = null;
    }
    else
    {
      if (args[1] == null || args[1].equals("") || args[1].equals("null")) {
        xsd = null;
      }
      else {
        xsd = args[1];
      }
    }

    if (args.length > 2)
    {
      record = Boolean.parseBoolean(args[2]);
    }

    try
    {
      if (record)
      {
        new DatabaseVersioning(args[0], xsd).doAll();
      }
      else
      {
        new Versioning(args[0], xsd).doAll();
      }
    }
    catch (ParseException e)
    {
      throw new CoreException(e);
    }
  }

}
