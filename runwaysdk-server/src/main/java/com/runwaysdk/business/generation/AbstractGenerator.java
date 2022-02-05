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
package com.runwaysdk.business.generation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.runwaysdk.business.ClassSignature;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.io.SourceWriter;
import com.runwaysdk.generation.CommonGenerationUtil;

/**
 * An abstract generator that generates a file in the given directory, based
 * upon the abstract method getSourceDirectory(), and the given package and file
 * name. A file is not actually generated until the go() method is executed.
 * Thus it is safe to construct the generator without worrying about
 * overwritting files.
 * 
 * !IMPORTANT! If you're changing the way base classes are generated then its
 * probably a good time to add a generation version number to the class
 * signature. The reason is because you must regenerate all the base classes of
 * applications that depend on runway (even though the metadata may not have
 * changed). If you don't regenerate these base classes, then the app can break
 * at runtime if the generated file is different than what it was copiled
 * against. See DDMS ticket #3298 * !IMPORTANT!
 *
 * @author Justin Smethie
 */
public abstract class AbstractGenerator implements GeneratorIF
{
  /**
   * The output stream to write with
   */
  private SourceWriter writer;

  /**
   * The package of the file to write
   */
  private String       pack;

  /**
   * The file name of the file to write
   */
  private String       fileName;

  /**
   * Type for which this generator will generate code artifacts.
   */
  private MdTypeDAOIF  mdTypeDAOIF;

  /**
   * Signature of the metadata from which we are generating code artifacts;
   */
  private String       signature = null;

  /**
   * The extension of the file to write
   */
  private String       extension;

  public AbstractGenerator(MdTypeDAOIF mdTypeDAOIF, String fileName)
  {
    this(mdTypeDAOIF, fileName, "java");
  }

  private AbstractGenerator(MdTypeDAOIF mdTypeDAOIF, String fileName, String extension)
  {
    this.mdTypeDAOIF = mdTypeDAOIF;

    this.pack = GenerationUtil.getPackageForFileSystem(mdTypeDAOIF);

    this.fileName = fileName;
    this.extension = extension;
  }

  protected MdTypeDAOIF getMdTypeDAOIF()
  {
    return this.mdTypeDAOIF;
  }

  public static boolean hashEquals(long serialVersionUID, String file)
  {
    DataInputStream in = null;
    BufferedReader br = null;

    try
    {
      FileInputStream fstream = new FileInputStream(file);
      // Get the object of DataInputStream
      in = new DataInputStream(fstream);
      br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      // Read File Line By Line
      while ( ( strLine = br.readLine() ) != null)
      {
        int startIndex = strLine.indexOf("@" + com.runwaysdk.business.ClassSignature.class.getName() + "(hash = ");

        if (startIndex == -1)
        {
          continue;
        }

        int endIndex = strLine.indexOf(")");

        String hashString = strLine.substring(startIndex + 1 + com.runwaysdk.business.ClassSignature.class.getName().length() + 8, endIndex);

        long fileHash = Long.parseLong(hashString);

        if (serialVersionUID == fileHash)
        {
          // System.out.println("SKIPPED! "+file);
          return true;
        }
        else
        {
          // System.out.println("NOT SKIPPED! "+file);
          return false;
        }
      }
    }
    catch (FileNotFoundException e)
    {
      // File has not been generated yet
      // throw new FileWriteException(srcFile, e);
    }
    catch (IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
    finally
    {
      try
      {
        if (br != null)
        {
          br.close();
        }

        if (in != null)
        {
          // Close the input stream
          in.close();
        }
      }
      catch (IOException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return false;
  }

  /**
   * Signature of the metadata from which we are generating code artifacts;
   */
  protected String getSignature()
  {
    if (this.signature == null)
    {
      this.signature = this.getMdTypeDAOIF().getSignature();
    }

    return this.signature;
  }

  /**
   * @return The directory where the generated .java file is written to
   */
  public String getFileName()
  {
    return fileName;
  }

  public String getExtension()
  {
    return extension;
  }

  /**
   * Returns the package of the file to generate
   * 
   * @return
   */
  protected String getPackage()
  {
    return pack;
  }

  protected void addSerialVersionUID()
  {
    long serialUID = getSerialVersionUID();

// Heads up: test    
    getWriter().writeLine("@SuppressWarnings(\"unused\")");
    getWriter().writeLine("private static final long serialVersionUID = " + serialUID + ";");
    getWriter().writeLine("");
  }

  protected long getSerialVersionUID(String prefix, String signature)
  {
    return ( prefix + signature ).hashCode();
  }

  public abstract long getSerialVersionUID();

  protected void addSignatureAnnotation()
  {
    getWriter().writeLine("@" + ClassSignature.class.getName() + "(hash = " + this.getSerialVersionUID() + ")");
  }

  /**
   * Lazy constructs the output stream to write the file with.
   *
   * @return
   */
  public synchronized SourceWriter getWriter()
  {
    if (writer == null)
    {
      String pack = CommonGenerationUtil.replacePackageDotsWithSlashes(getPackage());
      String sourceDirectory = getRootSourceDirectory();
      writer = new SourceWriter(sourceDirectory, pack, getFileName(), getExtension());
    }

    return writer;
  }

  public synchronized void setWriter(SourceWriter writer)
  {
//    if (writer == null)
//    {
      this.writer = writer;
//    }
//    else
//    {
//      throw new ProgrammingErrorException("Writer has already been set");
//    }
  }

  /**
   * @return The name of the .java file generated
   */
  public String getPath()
  {
    return this.getSourceDirectory() + getFileName() + "." + getExtension();
  }

  /**
   * Returns the java type that is generated.
   * 
   * @return the java type that is generated.
   */
  public String getJavaType()
  {
    return this.getMdTypeDAOIF().getPackage() + "." + this.getFileName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.generation.GeneratorIF#getSourceDirectory()
   */

  public String getSourceDirectory()
  {
    return getRootSourceDirectory() + CommonGenerationUtil.replacePackageDotsWithSlashes(getPackage());
  }

  public String getClassFile()
  {
    return this.getClassDirectory() + getFileName() + ".class";
  }

  public String getClassDirectory()
  {
    return getRootClassDirectory() + CommonGenerationUtil.replacePackageDotsWithSlashes(getPackage());
  }

  /**
   * Returns the root directory, the directory in which the package structure is
   * created, of the generated file.
   *
   * @return String
   */
  protected abstract String getRootSourceDirectory();

  /**
   * @return The root directory, the directory in which the package structure is
   *         created, of the generated class file.
   */
  protected abstract String getRootClassDirectory();

  public abstract String getSourceAttribute();

  public abstract String getClassAttribute();
}
