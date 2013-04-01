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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.runwaysdk.CommonExceptionProcessor;

/**
 * Writes an XML documents which takes care of incrementing and
 * decrementing indencies
 *
 * @author Justin Smethie
 * @date 06/09/06
 */
public class FileMarkupWriter extends MarkupWriter
{
  /**
   * The file the .xml is written to.
   * Primarily used for Exception handling.
   */
  private File               file;

  /**
   * @param directory Directory of the markup file
   * @param fileName name of the markup file
   * @param extension Extension of the markup file
   *
   * @throws IOException
   */
  public FileMarkupWriter(String directory, String fileName, String extension)
  {
    this(new File(directory + fileName + "." + extension));
  }

  /**
   * @param path Fully qualified path of the markup file to create
   * @throws IOException
   */
  public FileMarkupWriter(String path)
  {
    this(new File(path));
  }

  /**
   * @param file File information of the markup file to create
   * @throws IOException
   */
  public FileMarkupWriter(File file)
  {
    try
    {
      if(file.getParentFile() != null)
      {
        file.getParentFile().mkdirs();
      }

      OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)), "UTF8");
      init(out);
    }
    catch (IOException e)
    {
      CommonExceptionProcessor.fileWriteException(file, e);
    }
  }

  /**
   * Instantiates XMLWriter to a given a file with a specific encryption type
   * @param outfile name of the xml file to be created
   * @param encryption the type of encryption to be used
   * @throws Exception if unable to create the file
   */
  public FileMarkupWriter(String outfile, String encryption) throws IOException
  {
    OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(outfile)), encryption);
    init(out);
  }

  /**
   * Throws custom exception for the given <code>IOException</code> depending
   * on what the output stream is writing to.
   *
   * @param e
   */
  protected void throwIOException(IOException e)
  {
    CommonExceptionProcessor.fileWriteException(file, e);
  }
}
