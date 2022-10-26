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
package com.runwaysdk.business.generation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.dto.ComponentQueryDTOGenerator;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBlob;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.dataaccess.io.FileWriteException;
import com.runwaysdk.util.FileIO;

/**
 * Manages the storage and retrieval of an arbitrary number of .class files into
 * a single {@link AttributeBlob}.
 * 
 * @author Eric
 */
public class ClassManager
{
  /**
   * Reads all base .class files for the given mdType, and packages them into a
   * single byte[] for storage into the database.
   * 
   * @param mdType
   *          mdType whose base .class files are being stored
   * @return byte[] containing all of the Base .class files for this type
   */
  public static byte[] readBaseClasses(MdTypeDAOIF mdType)
  {
    ClassFilter classFilter = new ClassFilter(mdType.getTypeName() + TypeGeneratorInfo.BASE_SUFFIX);
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdType);
    File dir = new File(AbstractServerGenerator.getRootServerBinDirectory(fileSystemPackage), fileSystemPackage);
    return readClasses(dir, classFilter);
  }

  public static byte[] readClasses(AbstractGenerator generator)
  {
    ClassFilter classFilter = new ClassFilter(generator.getFileName());
    File dir = new File(generator.getClassDirectory());

    return readClasses(dir, classFilter);
  }

  /**
   * Reads all stub .class files for the given mdType, and packages them into a
   * single byte[] for storage into the database.
   * 
   * @param mdType
   *          mdType whose stub .class files are being stored
   * @return byte[] containing all of the stub .class files for this type
   */
  public static byte[] readStubClasses(MdTypeDAOIF mdType)
  {
    ClassFilter classFilter = new ClassFilter(mdType.getTypeName());
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdType);
    File dir = new File(AbstractServerGenerator.getRootServerBinDirectory(fileSystemPackage), fileSystemPackage);
    return readClasses(dir, classFilter);
  }

  /**
   * Reads all query API .class files for the given mdEntityIF, and packages
   * them into a single byte[] for storage into the database.
   * 
   * @param mdEntityIF
   *          mdEntityIF whose query .class files are being stored
   * @return byte[] containing all of the query API .class files for this type
   */
  public static byte[] readQueryAPIclasses(MdEntityDAOIF mdEntityIF)
  {
    ClassFilter classFilter = new ClassFilter(EntityQueryAPIGenerator.getQueryClassName(mdEntityIF));
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdEntityIF);
    File dir = new File(AbstractServerGenerator.getRootServerBinDirectory(fileSystemPackage), fileSystemPackage);
    return readClasses(dir, classFilter);
  }

  /**
   * Reads all query base API .class files for the given mdViewIF, and packages
   * them into a single byte[] for storage into the database.
   * 
   * @param mdViewIF
   *          mdViewIF whose query .class files are being stored
   * @return byte[] containing all of the query base API .class files for this
   *         type
   */
  public static byte[] readQueryBaseAPIclasses(MdViewDAOIF mdViewIF)
  {
    ClassFilter classFilter = new ClassFilter(ViewQueryBaseAPIGenerator.getQueryBaseClassName(mdViewIF));
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdViewIF);
    File dir = new File(AbstractServerGenerator.getRootServerBinDirectory(fileSystemPackage), fileSystemPackage);
    return readClasses(dir, classFilter);
  }

  /**
   * Reads all query stub API .class files for the given mdViewIF, and packages
   * them into a single byte[] for storage into the database.
   * 
   * @param mdViewIF
   *          mdViewIF whose query .class files are being stored
   * @return byte[] containing all of the query stub API .class files for this
   *         type
   */
  public static byte[] readQueryStubAPIclasses(MdViewDAOIF mdViewIF)
  {
    ClassFilter classFilter = new ClassFilter(ViewQueryStubAPIGenerator.getQueryStubClassName(mdViewIF));
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdViewIF);
    File dir = new File(AbstractServerGenerator.getRootServerBinDirectory(fileSystemPackage), fileSystemPackage);
    return readClasses(dir, classFilter);
  }

  /**
   * Reads all query DTO .class files for the given mdType, and packages them
   * into a single byte[] for storage into the database.
   * 
   * @param mdType
   *          mdType whose query .class files are being stored
   * @return byte[] containing all of the query DTO .class files for this type
   */
  public static byte[] readQueryDTOclasses(MdTypeDAOIF mdType)
  {
    ClassFilter classFilter = new ClassFilter(ComponentQueryDTOGenerator.getQueryClassName(mdType));
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdType);
    File dir = new File(AbstractClientGenerator.getRootClientBinDirectory(fileSystemPackage), fileSystemPackage);
    return readClasses(dir, classFilter);
  }

  /**
   * Reads all base .class files for the given mdType, and packages them into a
   * single byte[] for storage into the database.
   * 
   * @param mdType
   *          mdType whose base .class files are being stored
   * @return byte[] containing all of the Base .class files for this type
   */
  public static byte[] readDTObaseClasses(MdTypeDAOIF mdType)
  {
    ClassFilter classFilter = new ClassFilter(mdType.getTypeName() + TypeGeneratorInfo.DTO_BASE_SUFFIX);
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdType);
    File dir = new File(AbstractClientGenerator.getRootClientBinDirectory(fileSystemPackage), fileSystemPackage);
    return readClasses(dir, classFilter);
  }

  /**
   * Reads all stub .class files for the given mdType, and packages them into a
   * single byte[] for storage into the database.
   * 
   * @param mdType
   *          mdType whose stub .class files are being stored
   * @return byte[] containing all of the stub .class files for this type
   */
  public static byte[] readDTOstubClasses(MdTypeDAOIF mdType)
  {
    ClassFilter classFilter = new ClassFilter(mdType.getTypeName() + TypeGeneratorInfo.DTO_SUFFIX);
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdType);
    File dir = new File(AbstractClientGenerator.getRootClientBinDirectory(fileSystemPackage), fileSystemPackage);
    return readClasses(dir, classFilter);
  }

  /**
   * The method that actually gets the work done. Reads the .class files
   * according to the package and filter provided, adds them to a map, and then
   * returns the serialized byte[].
   * 
   * @param pack
   *          Package (and, consequently, directory) of the mdType
   * @param filenameFilter
   *          Filters out .class files for this type
   * @return byte[] of all the appropriate .clas files
   */
  private static byte[] readClasses(File dir, FilenameFilter filenameFilter)
  {
    Map<String, byte[]> map = new TreeMap<String, byte[]>();
    String[] fileNames = dir.list(filenameFilter);

    if (fileNames != null)
    {

      for (String fileName : fileNames)
      {
        File file = new File(dir, fileName);
        try
        {
          byte[] bytes = FileIO.readBytes(file);
          map.put(fileName, bytes);
        }
        catch (IOException e)
        {
          throw new FileReadException(file, e);
        }
      }

      try
      {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
        objectOut.writeObject(map);
        objectOut.close();
        return byteOut.toByteArray();
      }
      catch (IOException e)
      {
        throw new SystemException(e);
      }
    }

    return null;
  }

  /**
   * Writes .class files from a byte[] onto the filesystem. If the byte array is
   * null or has a size of 0, then nothing is written to the file system.
   * 
   * @param directory
   *          The directory to write the classes into
   * @param bytes
   *          The byte[] value from the database
   */
  @SuppressWarnings("unchecked")
  public static void writeClasses(File directory, byte[] bytes)
  {
    // Sometimes classes don't have any information. Don't crash, just quit.
    if (bytes == null || bytes.length == 0)
      return;

    ObjectInputStream input;
    Map<String, byte[]> map;
    try
    {
      input = new ObjectInputStream(new ByteArrayInputStream(bytes));
      map = (Map<String, byte[]>) input.readObject();
    }
    catch (Exception e)
    {
      throw new SystemException(e);
    }

    for (Entry<String, byte[]> entry : map.entrySet())
    {
      File file = new File(directory, entry.getKey());
      try
      {
        FileIO.write(file, entry.getValue());
      }
      catch (IOException e)
      {
        throw new FileWriteException("error", file, e);
      }
    }
  }

  /**
   * Deletes the class file for the class with the given type name, as well as
   * all nested classes (with a $).
   * 
   * @param directory
   * @param type
   *          name of the Java class.
   */
  public static void deleteClasses(File directory, String type)
  {
    ClassFilter filter = new ClassFilter(type);
    File[] listFiles = directory.listFiles(filter);
    deleteFiles(listFiles);
  }

  private static void deleteFiles(File[] listFiles)
  {
    if (listFiles == null)
      return;
    for (File file : listFiles)
    {
      try
      {
        FileIO.deleteFile(file);
      }
      catch (IOException e)
      {
        throw new SystemException("Could not delete file [" + file.getAbsolutePath() + "]", e);
      }
    }
  }
}

/**
 * ClassFilter is passed into {@link File#list(FilenameFilter)}. Given the type,
 * it accepts:
 * <ul>
 * <li>type.class
 * <li>type$*.class (Where * is a wildcard)
 * </ul>
 * 
 * So, given a type "Book", ClassFilter would accpet Book.class and
 * Book$inner.class, but not BookBase.class.
 * 
 * @author Eric
 */
class ClassFilter implements FilenameFilter
{
  String type;

  ClassFilter(String type)
  {
    this.type = type;
  }

  public boolean accept(File dir, String name)
  {
    if (name.equalsIgnoreCase(type + ".class"))
      return true;
    if (name.startsWith(type + '$'))
      return true;
    return false;
  }
}