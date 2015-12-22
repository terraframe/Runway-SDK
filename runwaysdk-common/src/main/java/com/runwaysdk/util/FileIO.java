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
package com.runwaysdk.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.runwaysdk.CommonExceptionMessageLocalizer;
import com.runwaysdk.constants.CommonProperties;

public class FileIO
{
  private static final int BUFFER = 2048;

  /**
   * Reads the given file into a String
   * 
   * @param fileName
   *          Name of the file to read
   * @return String containing the file's contents
   */
  public static String readString(String fileName) throws IOException
  {
    return readString(new File(fileName));
  }

  /**
   * Reads the given file into a String
   * 
   * @param file
   *          File to read
   * @return String containing the file's contents
   */
  public static String readString(File file) throws IOException
  {
    // We read bytes directly and force a utf8 decoding
    return new String(readBytes(file), "utf8");
  }

  public static List<String> readLines(File file) throws IOException
  {
    BufferedReader reader = null;
    try
    {
      List<String> lines = new LinkedList<String>();
      reader = new BufferedReader(new FileReader(file));
      while (reader.ready())
        lines.add(reader.readLine());
      return lines;
    }
    finally
    {
      if (reader != null)
        reader.close();
    }
  }

  /**
   * Reads the given file into a byte[]
   * 
   * @param fileName
   *          Name of the file to read
   * @return byte[] containing the file's contents
   */
  public static byte[] readBytes(String fileName) throws IOException
  {
    return readBytes(new File(fileName));
  }

  /**
   * Reads the given file into a byte[]
   * 
   * @param file
   *          File to read
   * @return byte[] containing the file's contents
   */
  public static byte[] readBytes(File file) throws IOException
  {
    BufferedInputStream input = null;
    try
    {
      input = new BufferedInputStream(new FileInputStream(file));
      byte[] bytes = new byte[(int) file.length()];

      input.read(bytes);
      input.close();
      return bytes;
    }
    catch (IOException e)
    {
      throw e;
    }
    finally
    {
      if (input != null)
      {
        input.close();
      }
    }
  }

  /**
   * Writes a String to a file. Note that this does not change the content of
   * the String. For example, newlines will be written exactly as they are
   * found, not adapted to the current operating system.
   * 
   * @param fileName
   *          Name of the file to read
   * @param data
   *          Content to write into the file
   */
  public static void write(String fileName, String data) throws IOException
  {
    write(new File(fileName), data, false);
  }

  public static void write(String fileName, String data, boolean append) throws IOException
  {
    write(new File(fileName), data, append);
  }

  /**
   * Writes a String to a file. Note that this does not change the content of
   * the String. For example, newlines will be written exactly as they are
   * found, not adapted to the current operating system.
   * 
   * @param file
   *          The file to write to
   * @param data
   *          Content to write into the file
   */
  public static void write(File file, String data) throws IOException
  {
    write(file, data, false);
  }

  public static void write(File file, String data, boolean append) throws IOException
  {
    if (data == null || data.trim().equals(""))
      return;

    makeDirectories(file);
    BufferedWriter writer = null;
    try
    {
      writer = new BufferedWriter(new FileWriter(file, append));
      writer.write(data);
      writer.flush();
      writer.close();
      return;
    }
    catch (IOException e)
    {
      throw e;
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }
    }
  }

  /**
   * Writes a String to a file with a specified encoding. Note that this does
   * not change the content of the String. For example, newlines will be written
   * exactly as they are found, not adapted to the current operating system.
   * 
   * @param file
   *          The file to write to
   * @param data
   *          Content to write into the file
   * @param encoding
   *          Encoding for written file
   */
  public static void encodedWrite(File file, String data, String encoding) throws IOException
  {
    encodedWrite(file, data, false, encoding);
  }

  public static void encodedWrite(File file, String data, boolean append, String encoding) throws IOException
  {
    if (data == null || data.trim().equals(""))
      return;

    makeDirectories(file);
    OutputStreamWriter writer = null;
    try
    {
      writer = new OutputStreamWriter(new FileOutputStream(file, append), encoding);
      writer.write(data);
      writer.flush();
      writer.close();
      return;
    }
    catch (IOException e)
    {
      throw e;
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }
    }
  }

  /**
   * Writes a byte[] to a file.
   * 
   * @param fileName
   *          Name of the file to read
   * @param data
   *          Content to write into the file
   */
  public static void write(String fileName, byte[] data) throws IOException
  {
    write(fileName, data, null);
  }

  /**
   * Writes a byte[] to a file.
   * 
   * @param fileName
   *          Name of the file to read
   * @param data
   *          Content to write into the file
   * @param logPrintStream
   */
  public static void write(String fileName, byte[] data, PrintStream logPrintStream) throws IOException
  {
    write(new File(fileName), data);
  }

  /**
   * Writes a byte[] to a file. If the byte array is null or has a length of 0,
   * then nothing is written to the file system.
   * 
   * @param file
   *          The file to write to
   * @param data
   *          Content to write into the file
   */
  public static void write(File file, byte[] data) throws IOException
  {
    write(file, data, null);
  }

  /**
   * Writes a byte[] to a file. If the byte array is null or has a length of 0,
   * then nothing is written to the file system.
   * 
   * @param file
   *          The file to write to
   * @param data
   *          Content to write into the file
   * @param logPrintStream
   */
  public static void write(File file, byte[] data, PrintStream logPrintStream) throws IOException
  {
    if (data == null || data.length == 0)
    {
      return;
    }

    if (logPrintStream != null)
    {
      logPrintStream.println(CommonExceptionMessageLocalizer.writingFileMessage(CommonProperties.getDefaultLocale(), file.getPath()));
    }

    makeDirectories(file);
    BufferedOutputStream output = null;
    try
    {
      output = new BufferedOutputStream(new FileOutputStream(file));
      output.write(data);
      output.flush();
      output.close();
      return;
    }
    catch (IOException e)
    {
      throw e;
    }
    finally
    {
      if (output != null)
      {
        output.close();
      }
    }
  }

  /**
   * Creates the directories for a File if they don't exist.
   * 
   * @param file
   */
  private static void makeDirectories(File file)
  {
    if (file.isDirectory())
      file.mkdirs();
    else
    {
      File parentFile = file.getParentFile();

      // If the file has no parent, then no directories need to be made
      if (parentFile == null)
        return;

      parentFile.mkdirs();
    }
  }

  /**
   * Terrible hack method to try and fix buggy file deletion.
   * 
   * @param deleteMe
   *          File to be deleted
   */
  public static void deleteFile(File deleteMe) throws IOException
  {
    // Bail out if the file doesn't exist
    if (!deleteMe.exists())
      return;

    // We're willing to try this whole cycle up to 3 times
    for (int i = 0; i < 3; i++)
    {
      // First off we try a regular delete
      if (!deleteMe.delete())
      {
        // Regular delete failed. Garbage Collect and try again
        System.gc();
        if (!deleteMe.delete())
        {
          // Still not deleted - sleep for a bit and try again
          try
          {
            Thread.sleep(100);
          }
          catch (InterruptedException e)
          {
          }

          // Return statments to cover successful deletes.
          if (deleteMe.delete())
            return;
        }
        else
          return;
      }
      else
        return;
    }

    // Still no joy, even after several tries. Just give up.
    String error = "Unable to delete file [" + deleteMe.getAbsolutePath() + "]";
    throw new IOException(error);
  }

  /**
   * Recursively copies the directory and all sub-directories into the
   * destination.
   * 
   * @param source
   *          Original directory
   * @param destination
   *          New directory (will be created)
   * 
   * @return true if the directory was copied successfully
   */
  public static boolean copyFolder(File source, File destination)
  {
    return copyFolder(source, destination, (PrintStream) null);
  }

  /**
   * Recursively copies the directory and all sub-directories into the
   * destination.
   * 
   * @param source
   *          Original directory
   * @param destination
   *          New directory (will be created)
   * @param logPrintStream
   * 
   * @return true if the directory was copied successfully
   */
  public static boolean copyFolder(File source, File destination, PrintStream logPrintStream)
  {
    for (String string : source.list())
    {
      File newSource = new File(source, string);
      File newDest = new File(destination, string);
      if (newSource.isDirectory())
      {
        copyFolder(newSource, newDest, logPrintStream);
      }
      else if (newSource.isFile())
      {
        try
        {
          copyFile(newSource, newDest, logPrintStream);
        }
        catch (IOException e)
        {
          return false;
        }
      }
      else
      {
        // Not a file or a folder... weird.
        return false;
      }
    }
    return true;
  }

  /**
   * Recursively copies a file or directory into the destination file or
   * directory. If the source is a directory then all sub-directories into the
   * are also copied.
   * 
   * @param source
   *          Original file or directory
   * @param destination
   *          New file or directory (will be created)
   * 
   */
  public static void copy(File source, File destination) throws IOException
  {
    if (source.isDirectory())
    {
      copyFolder(source, destination);
    }
    else
    {
      copyFile(source, destination);
    }
  }

  /**
   * Recursively copies the directory and all sub-directories into the
   * destination.
   * 
   * @param source
   *          Original directory
   * @param destination
   *          New directory (will be created)
   * @param filenameFilter
   * 
   * @return true if the directory was copied successfully
   */
  public static boolean copyFolder(File source, File destination, FilenameFilter filenameFilter)
  {
    return copyFolder(source, destination, filenameFilter, null);
  }

  /**
   * Recursively copies the directory and all sub-directories into the
   * destination.
   * 
   * @param source
   *          Original directory
   * @param destination
   *          New directory (will be created)
   * @param filenameFilter
   * @param logPrintStream
   * 
   * @return true if the directory was copied successfully
   */
  public static boolean copyFolder(File source, File destination, FilenameFilter filenameFilter, PrintStream logPrintStream)
  {
    for (String string : source.list(filenameFilter))
    {
      File newSource = new File(source, string);
      File newDest = new File(destination, string);
      if (newSource.isDirectory())
      {
        copyFolder(newSource, newDest, filenameFilter, logPrintStream);
      }
      else if (newSource.isFile())
      {
        try
        {
          copyFile(newSource, newDest, logPrintStream);
        }
        catch (IOException e)
        {
          return false;
        }
      }
      else
      {
        // Not a file or a folder... weird.
        return false;
      }
    }
    return true;
  }

  /**
   * Gets a full-depth list of all the folders that reside under the given root.
   * If the root is a file the folder it resides in is used as the root.
   * 
   * @param root
   * @return
   */
  public static List<File> getFolderTree(File root)
  {
    // If the file passed in isn't a directory, det the directory it's in
    LinkedList<File> files = new LinkedList<File>();
    if (!root.isDirectory())
      root = root.getParentFile();

    // Don't include svn directories
    if (root.getAbsolutePath().endsWith(".svn"))
      return files;

    // Add the root, then recursively add all the children
    files.add(root);
    for (File child : root.listFiles())
      if (child.isDirectory())
        files.addAll(getFolderTree(child));

    return files;
  }

  /**
   * Searches the classpath for the given filename, and returns the directory it
   * was found in.
   * 
   * @param fileName
   * @return
   * @throws URISyntaxException
   */
  public static File getDirectory(String fileName) throws URISyntaxException
  {
    URL url = FileIO.class.getResource(fileName);
    File base = new File(url.toURI());
    return base.getParentFile();
  }

  /**
   * Gets a list of all the files in the given folder, and all subfolders. If
   * the root is a file then a list containing just that file is returned.
   * 
   * @param root
   * @return
   */
  public static List<File> listFilesRecursively(File root)
  {
    FileFilter fileFilter = new FileFilter()
    {
      public boolean accept(File pathname)
      {
        return true;
      }
    };

    return listFilesRecursively(root, fileFilter);
  }

  /**
   * Gets a list of all the files in the given folder, and all subfolders. If
   * the root is a file then a list containing just that file is returned.
   * 
   * @param root
   * @param fileFilter
   * @return
   */
  public static List<File> listFilesRecursively(File root, FileFilter fileFilter)
  {
    LinkedList<File> list = new LinkedList<File>();

    // Recursion termination: if the given root is a file, return just it.
    if (!root.isDirectory())
    {
      list.add(root);
      return list;
    }

    // Exclude .svn directories
    if (root.getName().equals(".svn"))
    {
      return list;
    }

    // The root is a folder - get all of its files
    for (File child : root.listFiles(fileFilter))
      list.addAll(listFilesRecursively(child, fileFilter));
    return list;
  }

  /**
   * Copies a file
   * 
   * @param source
   *          Original file
   * @param dest
   *          New copy of the original
   * @throws IOException
   */
  public static void copyFile(File source, File dest) throws IOException
  {
    copyFile(source, dest, null);
  }

  /**
   * Copies a file
   * 
   * @param source
   *          Original file
   * @param dest
   *          New copy of the original
   * @param logPrintStream
   * @throws IOException
   */
  public static void copyFile(File source, File dest, PrintStream logPrintStream) throws IOException
  {
    if (logPrintStream != null)
    {
      logPrintStream.println(CommonExceptionMessageLocalizer.writingFileMessage(CommonProperties.getDefaultLocale(), dest.getPath()));
    }

    FileIO.makeDirectories(dest);

    FileIO.write(new FileOutputStream(dest), new FileInputStream(source));
  }

  /**
   * Converts an array of primitive bytes into an array of Byte objects.
   * 
   * @param bytes
   * @return
   */
  public static Byte[] convertToBytes(byte[] bytes)
  {
    Byte[] array = new Byte[bytes.length];

    for (int i = 0; i < bytes.length; i++)
    {
      array[i] = Byte.valueOf(bytes[i]);
    }

    return array;
  }

  /**
   * Converts array of java Byte objects into an array of primitive bytes.
   * 
   * @param bytes
   *          Array of Byte objects
   * @return
   */
  public static byte[] convertFromBytes(Byte[] bytes)
  {
    byte[] array = new byte[bytes.length];

    for (int i = 0; i < bytes.length; i++)
    {
      array[i] = bytes[i].byteValue();
    }

    return array;
  }

  /**
   * Recursivley deletes all the files, sub-directories, and sub-files of a
   * given directory
   * 
   * @param directory
   *          The directory
   */
  public static void deleteDirectory(File directory) throws IOException
  {
    if (directory.isDirectory())
    {
      File[] files = directory.listFiles();

      if (files != null)
      {
        for (File file : files)
        {
          deleteDirectory(file);
        }
      }
    }

    FileIO.deleteFile(directory);
  }

  public static void deleteFolderContent(File directory, FileFilter filter) throws IOException
  {
    if (directory.isDirectory())
    {
      File[] files = directory.listFiles(filter);

      if (files != null)
      {
        for (File file : files)
        {
          deleteDirectory(file);
        }
      }
    }
  }

  public static final FileFilter getDirectoryFilter()
  {
    return new FileFilter()
    {
      public boolean accept(File pathname)
      {
        return pathname.isDirectory();
      }
    };
  }

  public static final Byte[] getBytesFromStream(InputStream stream) throws IOException
  {
    BufferedInputStream input = null;
    List<Byte> bytes = new LinkedList<Byte>();

    try
    {
      input = new BufferedInputStream(stream);

      byte[] buf = new byte[1024];
      int bytesRead = 0;

      while ( ( bytesRead = input.read(buf) ) >= 0)
      {
        for (int i = 0; i < bytesRead; i++)
        {
          bytes.add(buf[i]);
        }
      }

      return bytes.toArray(new Byte[bytes.size()]);
    }
    finally
    {
      if (input != null)
      {
        input.close();
      }
    }
  }

  public static long write(OutputStream outputStream, InputStream inputStream) throws IOException
  {
    BufferedInputStream input = null;
    BufferedOutputStream output = null;
    long size = 0;
    try
    {
      input = new BufferedInputStream(inputStream);
      output = new BufferedOutputStream(outputStream);

      byte[] buf = new byte[2048];
      int bytesRead = 0;

      while ( ( bytesRead = input.read(buf) ) >= 0)
      {
        output.write(buf, 0, bytesRead);
        size += bytesRead;
      }
      output.flush();

      return size;
    }
    finally
    {
      try
      {
        if (input != null)
        {
          input.close();
        }
      }
      finally
      {
        if (output != null)
        {
          output.close();
        }
      }
    }
  }

  public static void write(ZipFile zipfile, String dest) throws IOException
  {
    Enumeration<? extends ZipEntry> e = zipfile.entries();

    while (e.hasMoreElements())
    {
      ZipEntry entry = e.nextElement();

      InputStream in = zipfile.getInputStream(entry);
      
      String repSep = File.separator;
      if (File.separator.equals("\\"))
      {
        repSep = "\\\\";
      }
      
      String entryPath = entry.getName().replaceAll("\\\\", repSep).replaceAll("/", repSep);

      // This is a more cross platform implementation of isDirectory (since windows is \ and linux is /)
      if (entryPath.endsWith(File.separator))
      {
        File directory = new File(dest + File.separator + entryPath);
        directory.mkdir();
      }
      else
      {
        int lastDirIndex = entryPath.lastIndexOf(File.separator);

        // there is no directory
        if (lastDirIndex > 0)
        {
          String dirs = entryPath.substring(0, entryPath.lastIndexOf(File.separator));

          File directory = new File(dest + File.separator + dirs);
          directory.mkdirs();
        }
        
        FileOutputStream fos = new FileOutputStream(dest + File.separator + entryPath);
        FileIO.write(fos, in);
      }
    }
  }

  /**
   * Zips the files in a directory, including all sub-directories, to the
   * specified destination.
   * 
   * @param source
   *          Source directory for files to zip.
   * @param filter
   *          Filter to include and remove files from the zip
   * @param dest
   *          Destination of the zip file.
   */
  public static void zip(File source, FileFilter filter, File dest) throws IOException
  {
    // Zip the contents
    List<File> fileList = FileIO.listFilesRecursively(source, filter);

    FileOutputStream fos = new FileOutputStream(dest);
    ZipOutputStream output = new ZipOutputStream(new BufferedOutputStream(fos));

    byte data[] = new byte[BUFFER];

    for (File file : fileList)
    {
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(file), BUFFER);

      String path = file.getPath();
      String sourcePath = source.getPath();
      String relativePath = path.substring(sourcePath.length());

      ZipEntry entry = new ZipEntry(relativePath);
      output.putNextEntry(entry);

      int count;
      while ( ( count = in.read(data, 0, BUFFER) ) != -1)
      {
        output.write(data, 0, count);
      }
      in.close();
    }
    output.close();
  }
}
