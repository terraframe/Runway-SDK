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
package com.runwaysdk.dataaccess.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.GenerationUtil;

public class SourceWriter
{
  private static final int INDENT_AMOUNT = 2;
  private BufferedWriter writer;
  private int indenting = 0;
     
  /**
   * Constructs a SourceWriter that will write to a file in directory/pack/fileName.java
   * 
   * @param directory The base directory for the destination file
   * @param pack The package structure of the file
   * @param fileName The file name - .java extension is appended
   */
  public SourceWriter(String directory, String pack, String fileName)
  {
    this(directory, pack, fileName, "java");
  }

  /**
   * Constructs a SourceWriter that will write to a file in directory/pack/fileName.extension
   * 
   * @param directory The base directory for the destination file
   * @param pack The package structure of the file
   * @param fileName The file name
   * @param extension The file extenstion
   */
  public SourceWriter(String directory, String pack, String fileName, String extension)
  {
    this(new File(directory + pack + fileName + "." + extension));
  }
  
  /**
   * Constructs a SourceWriter that will write to the given file
   * 
   * @param file
   */
  public SourceWriter(File file)
  {
    file.getParentFile().mkdirs();
    
    try
    {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
    }
    catch (FileNotFoundException e)
    {
      throw new FileWriteException(file, e);
    }
  }
  
  public SourceWriter(BufferedWriter writer)
  {
    this.writer = writer;
  }
  
  protected BufferedWriter getWriter()
  {
    return writer;
  }
  
  /**
   * Write a string to file.
   * 
   * @param s The String to write
   */
  public void write(String s)
  {
    GenerationUtil.write(getWriter(), getIndentation() + s);
  }

  /**
   * Writes a String to a file followed by a newline.
   * 
   * @param s The String to write
   */
  public void writeLine(String s)
  {
    GenerationUtil.writeLine(getWriter(), getIndentation() + s);
  }
  
  /**
   * Closes the BufferedWriter classes for the stub and source files.
   */
  public void close()
  {
    try
    {
      getWriter().close();
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }
  
  /**
   * Writes an open curly brace, a newline, and increases the indenting.
   */
  public void openBracket()
  {
    writeLine("{");
    indenting += INDENT_AMOUNT;
  }
  
  /**
   * Writes a close curly brace, a newline, and decreases the indenting.
   */
  public void closeBracket()
  {
    indenting -= INDENT_AMOUNT;
    
    if(indenting < 0)
    {
      indenting = 0;
    }
    
    writeLine("}");    
  }
  
  private String getIndentation()
  {
    String indent = new String();
    for(int i = 0; i < indenting; i ++)
    {
      indent = indent.concat(" ");
    }
    return indent;
  }  
}
