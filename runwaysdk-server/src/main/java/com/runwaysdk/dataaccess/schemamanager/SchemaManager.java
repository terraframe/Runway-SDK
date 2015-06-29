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
package com.runwaysdk.dataaccess.schemamanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.runwaysdk.dataaccess.io.FileStreamSource;
import com.runwaysdk.dataaccess.io.TimestampFile;
import com.runwaysdk.dataaccess.io.Versioning;
import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.xml.SMSAXImporter;
import com.runwaysdk.dataaccess.schemamanager.xml.SMXMLFilter;
import com.runwaysdk.dataaccess.schemamanager.xml.SchemaExportVisitor;

/**
 * 
 * Driver for the merger and the visualizer
 * 
 * @author Aritra
 * 
 */
public class SchemaManager
{
  public static final boolean DEBUG = false;

  public static void main(String[] args)
  {
    Mode commandLineMode = null;
    if (args[0].equals("-file"))
    {
      commandLineMode = new SchemaManager.FileMode(args);
    }
    else if (args[0].equals("-dir"))
    {
      commandLineMode = new SchemaManager.DirMode(args);
    }
    else if (args[0].equals("-filter"))
    {
      commandLineMode = new SchemaManager.FilterMode(args);
    }
    else if (args[0].equals("-timestamp"))
    {
      commandLineMode = new SchemaManager.TimestampMode(args);
    }
    else
    {
      throw new RuntimeException("Invalid first parameter [" + args[0] + "]. Expected one of [-file, -dir, -filter, -timestamp].");
    }

    ListIterator<File> schemaIterator = commandLineMode.schemaFilesIterator();
    merge(schemaIterator, commandLineMode.xsdLocation(), commandLineMode.destinationFileLocation());

  }

  public static void merge(List<File> schemas, String xsdLocation, String destinationFileLocation)
  {
    ListIterator<File> schemaIterator = schemas.listIterator();
    merge(schemaIterator, xsdLocation, destinationFileLocation);
  }

  public static void merge(String schemaDirectory, String xsdLocation, String destinationFileLocation)
  {
    Versioning versioning = new Versioning(schemaDirectory, xsdLocation);
    ListIterator<File> schemaIterator = ( new ArrayList<File>(versioning.orderedFileSet()) ).listIterator();
    merge(schemaIterator, xsdLocation, destinationFileLocation);
  }

  public static void merge(MergeSchema doItSchema, MergeSchema undoItSchema, ListIterator<File> schemaIterator, String xsdLocation, String destinationFileLocation)
  {

    while (schemaIterator.hasNext())
    {
      File currentFile = schemaIterator.next();

      doItSchema.changeFile(new FileStreamSource(currentFile));

      if (SchemaManager.DEBUG)
      {
        System.out.println("Merging File " + currentFile.getAbsolutePath() + ".............");
      }

      SMSAXImporter.runImport(currentFile, xsdLocation, doItSchema, new SMXMLFilter(XMLTags.DO_IT_TAG));
    }

    while (schemaIterator.hasPrevious())
    {
      File previousFile = schemaIterator.previous();
      undoItSchema.changeFile(new FileStreamSource(previousFile));
      SMSAXImporter.runImport(previousFile, xsdLocation, undoItSchema, new SMXMLFilter(XMLTags.UNDO_IT_TAG));
    }

    SchemaExportVisitor exporter = new SchemaExportVisitor(destinationFileLocation, xsdLocation, doItSchema, undoItSchema);
    exporter.export();

  }

  public static void merge(ListIterator<File> schemaIterator, String xsdLocation, String destinationFileLocation)
  {
    MergeSchema doItSchema = new MergeSchema(xsdLocation);
    MergeSchema undoItSchema = new MergeSchema(xsdLocation);

    merge(doItSchema, undoItSchema, schemaIterator, xsdLocation, destinationFileLocation);
  }

  /**
   * 
   * Abstract representation of a command line mode
   * 
   * @author Aritra
   * 
   */
  protected abstract static class Mode
  {

    protected String[] arguments;

    public Mode(String[] arguments)
    {
      this.arguments = arguments;
    }

    public abstract ListIterator<File> schemaFilesIterator();

    public abstract String xsdLocation();

    public abstract String destinationFileLocation();
  }

  /**
   * 
   * A command line mode that specifies the files to merge individually in
   * sequence
   * 
   * @author Aritra
   * 
   */
  protected static class FileMode extends Mode
  {
    public FileMode(String[] arguments)
    {
      super(arguments);
    }

    @Override
    public ListIterator<File> schemaFilesIterator()
    {
      List<File> schemaFiles = new ArrayList<File>();
      for (int i = 1; i < arguments.length - 2; i++)
      {
        schemaFiles.add(new File(arguments[i]));
      }
      return schemaFiles.listIterator();
    }

    @Override
    public String xsdLocation()
    {
      return arguments[arguments.length - 2];
    }

    @Override
    public String destinationFileLocation()
    {
      return arguments[arguments.length - 1];
    }

  }

  protected static class FilterMode extends Mode
  {
    /**
     * @param arguments
     *          [0] -filter
     * @param arguments
     *          [1] directory
     * @param arguments
     *          [...] files to exclude
     * @param arguments
     *          [size-2] xsd location
     * @param arguments
     *          [size-1] destination
     */
    public FilterMode(String[] arguments)
    {
      super(arguments);
    }

    @Override
    public ListIterator<File> schemaFilesIterator()
    {
      String schemaFolderLocation = arguments[1];

      String xsdLocation = xsdLocation();

      Versioning versioning = new Versioning(schemaFolderLocation, xsdLocation);

      List<File> schemaFiles = new ArrayList<File>(versioning.orderedFileSet());

      for (int i = 1; i < arguments.length - 2; i++)
      {
        schemaFiles.remove(new File(arguments[i]));
      }

      return schemaFiles.listIterator();
    }

    @Override
    public String xsdLocation()
    {
      return arguments[arguments.length - 2];
    }

    @Override
    public String destinationFileLocation()
    {
      return arguments[arguments.length - 1];
    }
  }

  /**
   * 
   * A command line mode that specifies an input folder location. Files are read
   * and processed from the folder in the order of their version number.
   * 
   * @author Aritra
   * 
   */
  protected static class DirMode extends Mode
  {

    public DirMode(String[] arguments)
    {
      super(arguments);
    }

    @Override
    public ListIterator<File> schemaFilesIterator()
    {
      String schemaFolderLocation = arguments[1];
      String xsdLocation = xsdLocation();
      Versioning versioning = new Versioning(schemaFolderLocation, xsdLocation);
      return ( new ArrayList<File>(versioning.orderedFileSet()) ).listIterator();
    }

    @Override
    public String destinationFileLocation()
    {
      return arguments[3];
    }

    @Override
    public String xsdLocation()
    {
      return arguments[2];
    }
  }

  /**
   * @param arguments
   *          [0] -filter
   * @param arguments
   *          [1] directory
   * @param arguments
   *          [2] xsd location
   * @param arguments
   *          [3] destination
   * @param arguments
   *          [4] start timestamp
   * @param arguments
   *          [5] end timestamp (optional)
   */
  protected static class TimestampMode extends Mode
  {

    public TimestampMode(String[] arguments)
    {
      super(arguments);
    }

    @Override
    public ListIterator<File> schemaFilesIterator()
    {
      String schemaFolderLocation = arguments[1];
      Long start = Long.parseLong(arguments[4]);
      Long end = ( arguments.length > 5 ? Long.parseLong(arguments[5]) : null );

      String xsdLocation = this.xsdLocation();

      Versioning versioning = new Versioning(schemaFolderLocation, xsdLocation);

      List<File> files = new ArrayList<File>(versioning.orderedFileSet());
      Iterator<File> it = files.iterator();

      while (it.hasNext())
      {
        TimestampFile file = new TimestampFile(it.next());

        Long timestamp = file.getTimestamp();

        if (timestamp < start || ( end != null && timestamp > end ))
        {
          it.remove();
        }
      }

      return files.listIterator();
    }

    @Override
    public String xsdLocation()
    {
      return arguments[2];
    }

    @Override
    public String destinationFileLocation()
    {
      return arguments[3];
    }
  }

}
