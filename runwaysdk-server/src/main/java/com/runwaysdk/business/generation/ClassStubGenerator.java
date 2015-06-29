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
package com.runwaysdk.business.generation;

import java.io.File;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.generation.loader.Reloadable;

public abstract class ClassStubGenerator extends TypeGenerator
{

  public ClassStubGenerator(MdClassDAOIF mdClass)
  {
    super(mdClass, getGeneratedName(mdClass));
  }

  @Override
  protected String getRootSourceDirectory()
  {
    return getRootServerStubDirectory(this.getPackage());
  }

  static String getGeneratedName(MdClassDAOIF mdClass)
  {
    if (GenerationUtil.isHardcodedType(mdClass))
    {
      return mdClass.getTypeName() + TypeGeneratorInfo.SYSTEM_SUFFIX;
    }
    else
    {
      return mdClass.getTypeName();
    }
  }

  public static String getGeneratedType(MdClassDAOIF mdClass)
  {
    if (GenerationUtil.isHardcodedType(mdClass))
    {
      return mdClass.definesType() + TypeGeneratorInfo.SYSTEM_SUFFIX;
    }
    else
    {
      return mdClass.definesType();
    }
  }

  protected String getClassName()
  {
    return getGeneratedName(this.getMdTypeDAOIF());
  }

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    if (forceRegeneration)
    {
      generate();
      return;
    }

    // This cast is OK, as the mdClass is not modified here, just read.
    AttributeIF stubSource = ((MdClassDAO)getMdTypeDAOIF()).getAttributeIF(MdEntityInfo.STUB_SOURCE);
    boolean empty = stubSource.getValue().trim().equals("");

    // If the database contains new source, just write that to the file system
    if (stubSource.isModified() && !empty)
    {
      getWriter().write(stubSource.getValue());
      getWriter().close();
      return;
    }

    // If we're keeping existing stub source, and a file exists, leave it
    if (LocalProperties.isKeepSource() && new File(getPath()).exists())
    {
      return;
    }

    if (this.getMdTypeDAOIF().isNew() || empty)
    {
      generate();
      return;
    }
  }

  @Override
  protected MdClassDAOIF getMdTypeDAOIF()
  {
    return (MdClassDAOIF)super.getMdTypeDAOIF();
  }

  private void generate()
  {
    addPackage();
    addClassName();

    addSerialVersionUID();
    addAttributes();
    addConstructor();
    addMethods();
    getWriter().closeBracket();
    getWriter().close();
  }
  
  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("STUB", this.getSignature());
  }
  
  protected void addAttributes()
  {
    // MdClasses have no attributes, however this method may be overwritten in child classes
  }

  protected void addMethods()
  {
    // MdClasses have no methods, however this method will be overwritten in child classes
  }

  /**
   * Generate the package declaration
   */
  private void addPackage()
  {
    getWriter().writeLine("package " + this.getMdTypeDAOIF().getPackage() + ";");
    getWriter().writeLine("");
  }

  /**
   * Generates the class name declaration
   */
  private void addClassName()
  {
    String typeName = this.getClassName();
    String modifier = "public class";
    String baseFileName = ClassBaseGenerator.getGeneratedName(this.getMdTypeDAOIF());

    if (this.getMdTypeDAOIF().isAbstract() || GenerationUtil.isHardcodedType(this.getMdTypeDAOIF()))
    {
      modifier = "public abstract class";
    }
    
    String impls = "";
    if (getImplements() != null) {
      impls = " implements " + getImplements();
    }

    getWriter().write(modifier + " " + typeName + " extends " + baseFileName + impls);

    if (!this.getMdTypeDAOIF().isSystemPackage())
    {
      getWriter().write(Reloadable.IMPLEMENTS);
    }
    getWriter().writeLine("");

    getWriter().openBracket();
  }

  protected abstract void addConstructor();
  
  protected String getImplements() {
    return null;
  }

  @Override
  public String getClassAttribute()
  {
    return MdClassInfo.STUB_CLASS;
  }

  @Override
  public String getSourceAttribute()
  {
    return MdClassInfo.STUB_SOURCE;
  }
}
