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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.ClassSignature;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFileDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

public abstract class ComponentQueryAPIGenerator implements GeneratorIF, ServerMarker
{
  private MdClassDAOIF     mdClassIF;

  protected String         queryTypeName;

  protected BufferedWriter srcBuffer;

  /**
   * Signature of the metadata from which we are generating code artifacts;
   */
  private String           signature = null;

  /**
   * 
   * @param mdClassIF
   */
  public ComponentQueryAPIGenerator(MdClassDAOIF mdClassIF)
  {
    this.mdClassIF = mdClassIF;
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

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business
    // classes for metadata.
    if (this.mdClassIF.isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    String pack = GenerationUtil.getPackageForFileSystem(this.getMdClassIF());
    new File(ServerMarker.SOURCE_DIRECTORY + pack).mkdirs();
    new File(ServerMarker.BASE_DIRECTORY + pack).mkdirs();
  }

  /**
   * Returns the reference to the MdClassIF object that defines the entity type
   * for which this object generates a query API object for.
   * 
   * @return reference to the MdClassIF object that defines the entity type for
   *         which this object generates a query API object for.
   */
  protected MdClassDAOIF getMdClassIF()
  {
    return this.mdClassIF;
  }

  /**
   * Returns the qualified name of the class that implements the custom query
   * API for the given type.
   * 
   * @param mdClassIF
   * @return qualified name of the class that implements the custom query API
   *         for the given type.
   */
  public static String getQueryClass(MdClassDAOIF mdClassIF)
  {
    if (mdClassIF instanceof MdEntityDAOIF)
    {
      return EntityQueryAPIGenerator.getQueryClass( ( (MdEntityDAOIF) mdClassIF ).definesType());
    }
    else if (mdClassIF instanceof MdViewDAOIF)
    {
      return ViewQueryStubAPIGenerator.getQueryStubClass( ( (MdViewDAOIF) mdClassIF ).definesType());
    }
    else
    {
      String errMsg = "Class [" + mdClassIF.getClass().getName() + "] does not have a query class";
      throw new ProgrammingErrorException(errMsg);
    }
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
    // long serialUID = this.getSerialVersionUID();

    // this.writeLine(this.srcBuffer, "@SuppressWarnings(\"unused\")");
    // this.writeLine(this.srcBuffer,
    // "private static final long serialVersionUID = "+serialUID+";");
    this.writeLine(this.srcBuffer, "");
  }

  protected void addSerialVersionUIDForInnerClasses(String prefix, String signature)
  {
    // long serialUID = (prefix+signature).hashCode();

    // this.writeLine(this.srcBuffer, "@SuppressWarnings(\"unused\")");
    // this.writeLine(this.srcBuffer,
    // "private static final long serialVersionUID = "+serialUID+";");
    this.writeLine(this.srcBuffer, "");
  }

  protected long getSerialVersionUID(String prefix, String signature)
  {
    return ( prefix + signature ).hashCode();
  }

  public abstract long getSerialVersionUID();

  protected void addSignatureAnnotation()
  {
    this.writeLine(this.srcBuffer, "@" + ClassSignature.class.getName() + "(hash = " + this.getSerialVersionUID() + ")");
  }

  /**
   * Generates the class name declaration
   */
  protected void addGetClassTypeMethod()
  {
    writeLine(this.srcBuffer, "  public String getClassType()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    return " + this.getMdClassIF().definesType() + ".CLASS;");
    writeLine(this.srcBuffer, "  }");
  }

  /**
   * 
   * @param parent
   */
  protected abstract void addExtends(MdClassDAOIF parentMdClassIF);

  /**
   * Generates the class name declaration
   */
  protected void addClassName()
  {
    MdClassDAOIF parentMdClassIF = this.getMdClassIF().getSuperClass();
    String typeName = this.getMdClassIF().getTypeName();

    // Add a javadoc to the base file that yells at the user to not make changes
    this.writeLine(this.srcBuffer, "/**");
    this.writeLine(this.srcBuffer, " * This class is generated automatically.");
    this.writeLine(this.srcBuffer, " * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN");
    this.writeLine(this.srcBuffer, " * Custom business logic should be added to " + typeName + ".java");
    this.writeLine(this.srcBuffer, " *");
    this.writeLine(this.srcBuffer, " * @author Autogenerated by RunwaySDK");
    this.writeLine(this.srcBuffer, " */");
    this.write(this.srcBuffer, "public " + this.getClassAbstract() + " class " + this.queryTypeName + " extends ");
    this.addExtends(parentMdClassIF);

    this.writeLine(this.srcBuffer, "");

    this.writeLine(this.srcBuffer, "{");
  }

  protected String getClassAbstract()
  {
    return "";
  }

  /**
   *
   *
   */
  protected void addAccessors()
  {
    for (MdAttributeDAOIF mdAttributeIF : this.getMdClassIF().definesAttributesOrdered())
    {
      MdAttributeConcreteDAOIF mdAttributeConcreteIF = mdAttributeIF.getMdAttributeConcrete();

      if (! ( mdAttributeConcreteIF instanceof MdAttributeEnumerationDAOIF ) && ! ( mdAttributeConcreteIF instanceof MdAttributeMultiReferenceDAOIF ) && ! ( mdAttributeConcreteIF instanceof MdAttributeStructDAOIF ) && ! ( mdAttributeConcreteIF instanceof MdAttributeEncryptionDAOIF ) && ! ( mdAttributeConcreteIF instanceof MdAttributeFileDAOIF ) && ! ( mdAttributeConcreteIF instanceof MdAttributeReferenceDAOIF ))
      {
        this.addAccessor(this.srcBuffer, mdAttributeIF);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeStructDAOIF)
      {
        this.addStructAccessor(this.srcBuffer, mdAttributeIF);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeEnumerationDAOIF)
      {
        this.addEnumAccessor(this.srcBuffer, mdAttributeIF);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeMultiReferenceDAOIF)
      {
        this.addMultiReferenceAccessor(this.srcBuffer, mdAttributeIF);
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeRefDAOIF)
      {
        this.addRefAccessor(this.srcBuffer, mdAttributeIF);
      }
    }
  }

  /**
   * General case generation of getter for an attribute
   * 
   * @param mdAttributeIF
   *          Attribute to generate accessor methods for
   */
  protected abstract void addAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeIF);

  /**
   * Generation of getter for an attribute struct
   * 
   * @param mdAttributeStructIF
   *          Attribute to generate accessor methods for
   */
  protected abstract void addStructAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeStructIF);

  /**
   * Generation of getter for an attribute reference
   * 
   * @param mdAttributeRefIF
   *          Attribute to generate accessor methods for
   */
  protected abstract void addRefAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeRefIF);

  /**
   * Generation of getter for an attribute enumeration
   * 
   * @param mdAttributeEnumerationIF
   *          Attribute to generate accessor methods for
   */
  protected abstract void addEnumAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeEnumerationIF);

  protected abstract void addMultiReferenceAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeMultiReferenceIF);

  /**
   * Creates emthods that will return type safe iterators of the query result.
   * 
   */
  protected abstract void createIteratorMethods();

  /**
   * Writes a string and an OS-specific newline to the given writer. Much like
   * System.out.println(). This is the preferred way to write newlines.
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

    closeBuffer();
  }

  protected void closeBuffer()
  {
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
   * 
   * @return the java type that is generated.
   */
  public String getJavaType()
  {
    return this.getMdClassIF().getPackage() + "." + this.queryTypeName;
  }
}
