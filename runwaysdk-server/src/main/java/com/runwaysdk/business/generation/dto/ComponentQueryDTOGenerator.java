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
package com.runwaysdk.business.generation.dto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.ClassSignature;
import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.business.generation.ClientMarker;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.io.FileWriteException;

public abstract class ComponentQueryDTOGenerator implements GeneratorIF, ClientMarker
{
  private   MdClassDAOIF      mdClassIF;
  protected String         queryTypeName;
  protected BufferedWriter srcBuffer;

  protected String signature = null;

  /**
   * Returns the name of the class that implements the type safe QueryDTO for the given type.
   * @param mdTypeIF
   * @return name of the class that implements the type safe QueryDTO for the given type.
   */
  public static String getQueryClassName(MdTypeDAOIF mdTypeIF)
  {
    return mdTypeIF.getTypeName()+TypeGeneratorInfo.QUERY_DTO_SUFFIX;
  }

  /**
   * Signature of the metadata from which we are generating code artifacts;
   */
  protected String getSignature()
  {
    if (this.signature == null)
    {
      this.signature = this.mdClassIF.getSignature();
    }

    return this.signature;
  }

  /**
   * Returns the qualified name of the class that implements the type safe QueryDTO for the given type.
   * @param String type string.
   * @return qualified name of the class that implements the type safe QueryDTO for the given type.
   */
  public static String getQueryClass(String type)
  {
    return type+TypeGeneratorInfo.QUERY_DTO_SUFFIX;
  }

  /**
   * Returns the qualified name of the class that implements the type safe QueryDTO for the given type.
   * @param mdTypeIF
   * @return qualified name of the class that implements the type safe QueryDTO for the given type.
   */
  public static String getQueryClass(MdTypeDAOIF mdTypeIF)
  {
    return ComponentQueryDTOGenerator.getQueryClass(mdTypeIF.definesType());
  }

  /**
   *
   * @param mdEntityIF
   */
  public ComponentQueryDTOGenerator(MdClassDAOIF     mdClassIF)
  {
    this.mdClassIF = mdClassIF;
    this.queryTypeName = ComponentQueryDTOGenerator.getQueryClassName(this.mdClassIF);
  }

  /**
   * Returns the reference to the MdClassDAOIF object that defines the entity type
   * for which this object generates a query API object for.
   * @return reference to the MdClassDAOIF object that defines the entity type
   * for which this object generates a query API object for.
   */
  protected MdClassDAOIF getMdClassIF()
  {
    return this.mdClassIF;
  }

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business classes for metadata.
    if (this.getMdClassIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    if (!this.getMdClassIF().isPublished())
    {
      return;
    }
    
    // Do not regenerate if the existing file is semantically the same
    if (LocalProperties.isKeepBaseSource() &&
        AbstractGenerator.hashEquals(this.getSerialVersionUID(), TypeGenerator.getQueryDTOsourceFilePath(this.getMdClassIF())))
    {
      return;
    }
    
    String pack = GenerationUtil.getPackageForFileSystem(this.getMdClassIF());
    new File(ClientMarker.SOURCE_DIRECTORY + pack).mkdirs();
    new File(ClientMarker.BASE_DIRECTORY + pack).mkdirs();

    // First set the base writer
    File srcFile = new File(TypeGenerator.getQueryDTOsourceFilePath(this.getMdClassIF()));
    try
    {
      this.srcBuffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(srcFile)));
    }
    catch (FileNotFoundException e)
    {
      throw new FileWriteException(srcFile, e);
    }

    this.addPackage();
    this.addSignatureAnnotation();
    this.addClassName();
    this.addSerialVersionUID();
    this.addConstructor();
    this.addResultSetGetter();

    this.close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("DTO_QUERY", this.getSignature());
  }

  /**
   * Generate the package declaration
   */
  protected void addPackage()
  {
    this.writeLine(this.srcBuffer, "package " + this.getMdClassIF().getPackage() + ";");
    this.writeLine(this.srcBuffer, "");
  }

  protected void addSerialVersionUID()
  {
    long serialUID = getSerialVersionUID();
    
// Heads up: test
    this.writeLine(this.srcBuffer, "@SuppressWarnings(\"unused\")");   
    this.writeLine(this.srcBuffer, "private static final long serialVersionUID = "+serialUID+";");
    this.writeLine(this.srcBuffer, "");
  }

  protected void addSignatureAnnotation()
  {
    this.writeLine(this.srcBuffer,"@"+ClassSignature.class.getName()+"(hash = "+this.getSerialVersionUID()+")");
  }

  protected long getSerialVersionUID(String prefix, String signature)
  {
    return (prefix+signature).hashCode();
  }

  /**
   * Generates the class name declaration
   */
  protected void addClassName()
  {
    MdClassDAOIF parentMdClassIF =  this.getMdClassIF().getSuperClass();
    String typeName = this.getMdClassIF().getTypeName();

    // Add a javadoc to the base file that yells at the user to not make changes
    this.writeLine(this.srcBuffer, "/**");
    this.writeLine(this.srcBuffer, " * This class is generated automatically.");
    this.writeLine(this.srcBuffer, " * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN");
    this.writeLine(this.srcBuffer, " * Custom business logic should be added to " + typeName + ".java");
    this.writeLine(this.srcBuffer, " *");
    this.writeLine(this.srcBuffer, " * @author Autogenerated by RunwaySDK");
    this.writeLine(this.srcBuffer, " */");
    this.write(this.srcBuffer, "public class " + this.queryTypeName + " extends ");
    this.addExtends(parentMdClassIF);

    this.writeLine(this.srcBuffer, "{");
  }

  /**
   *
   * @param parent
   */
  protected abstract void addExtends(MdClassDAOIF parentMdClassIF);

  /**
   *
   *
   */
  protected void addConstructor()
  {
    //Constructor for the class
    writeLine(this.srcBuffer, "  protected " + this.queryTypeName + "(String type)");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    super(type);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");
  }

  protected void addResultSetGetter()
  {
    String dtoType = this.getMdClassIF().definesType()+TypeGeneratorInfo.DTO_SUFFIX;

    writeLine(srcBuffer, "@SuppressWarnings(\"unchecked\")");
    writeLine(srcBuffer, "public "+List.class.getName()+"<? extends "+dtoType+"> getResultSet()");
    writeLine(srcBuffer, "{");
    writeLine(srcBuffer, "  return ("+List.class.getName()+"<? extends "+dtoType+">)super.getResultSet();");
    writeLine(srcBuffer, "}");
  }

  /**
   * Returns a list of the fully qualified paths of the files generated.
   *
   * @return
   */
  public String getPath()
  {
    if (this.getMdClassIF().isPublished())
    {
      return TypeGenerator.getQueryDTOsourceFilePath(this.getMdClassIF());
    }
    else
    {
      return "";
    }
  }

  /**
   * Writes a string and an OS-specific newline to the given writer. Much like
   * System.out.println().  This is the preferred way to write newlines.
   *
   * @param _writer
   *          The BufferedWriter to write to
   * @param _string
   *          The String to write
   */
  protected void writeLine(BufferedWriter _writer, String _string)
  {
    try
    {
      _writer.write(_string);
      _writer.newLine();
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Writes a string to the given writer. Much like System.out.print()
   *
   * @param _writer
   *          The BufferedWriter to write to
   * @param _string
   *          The String to write
   */
  protected void write(BufferedWriter _writer, String _string)
  {
    try
    {
      _writer.write(_string);
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Closes the BufferedWriter classes for the stub and source files.
   */
  protected void close()
  {
    writeLine(this.srcBuffer, "}");

    try
    {
      this.srcBuffer.close();
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Returns the java type that is generated.
   * @return the java type that is generated.
   */
  public String getJavaType()
  {
    return this.getMdClassIF().getPackage()+"."+this.queryTypeName;
  }
}
