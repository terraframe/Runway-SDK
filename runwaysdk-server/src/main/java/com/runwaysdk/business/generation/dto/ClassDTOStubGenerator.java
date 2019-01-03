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
package com.runwaysdk.business.generation.dto;

import java.io.File;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.StubMarker;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;

public abstract class ClassDTOStubGenerator extends ComponentDTOGenerator implements StubMarker
{
  /**
   * @param mdClassDAOIF
   */
  public ClassDTOStubGenerator(MdClassDAOIF mdClassDAOIF)
  {
    super(mdClassDAOIF, getGeneratedName(mdClassDAOIF));
  }

  static String getGeneratedName(MdClassDAOIF mdClass)
  {
    if (GenerationUtil.isHardcodedType(mdClass))
    {
      return mdClass.getTypeName() + TypeGeneratorInfo.DTO_SYSTEM_SUFFIX;
    }
    else if (mdClass.isGenerateSource())
    {
      return mdClass.getTypeName() + DTO_SUFFIX;
    }
    else if (mdClass instanceof MdBusinessDAOIF)
    {
      return BusinessDTO.class.getSimpleName();
    }

    return EntityDTO.class.getSimpleName();
  }

  static String getGeneratedType(MdClassDAOIF mdClass)
  {
    if (GenerationUtil.isHardcodedType(mdClass))
    {
      return mdClass.definesType() + TypeGeneratorInfo.DTO_SYSTEM_SUFFIX;
    }
    else if (mdClass.isGenerateSource())
    {
      return mdClass.definesType() + DTO_SUFFIX;
    }
    else if (mdClass instanceof MdBusinessDAOIF)
    {
      return BusinessDTO.CLASS;
    }

    return EntityDTO.CLASS;
  }

  static String getGeneratedTypeHardcoded(MdClassDAOIF mdClass)
  {
    if (mdClass.isGenerateSource())
    {
      return mdClass.definesType() + DTO_SUFFIX;
    }
    else if (mdClass instanceof MdBusinessDAOIF)
    {
      return BusinessDTO.CLASS;
    }

    return EntityDTO.CLASS;
  }

  /**
   * The general algorithm used to generate the DTOs
   */
  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business
    // classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    if (forceRegeneration)
    {
      super.go(forceRegeneration);
      return;
    }

    // This cast is OK, as the mdClass is not modified here, just read.
    AttributeIF stubSource = ( (MdClassDAO) this.getMdTypeDAOIF() ).getAttributeIF(MdClassInfo.DTO_STUB_SOURCE);
    boolean empty = stubSource.getValue().trim().equals("");

    // If the database contains new source, just write that to the file system
    if (stubSource.isModified() && !empty)
    {
      getWriter().write(this.getMdTypeDAOIF().getAttributeIF(MdClassInfo.DTO_STUB_SOURCE).getValue());
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
      super.go(forceRegeneration);
      return;
    }
  }

  protected boolean hashEquals()
  {
    return false;
  }

  /**
   * Writes the package of the DTO
   */
  protected void writePackage()
  {
    getWriter().writeLine("package " + this.getMdTypeDAOIF().getPackage() + ";");
    getWriter().writeLine("");
  }

  @Override
  protected void write()
  {
    // Write fields
    addSerialVersionUID();

    // Write constructors
    writeConstructor();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("DTO_STUB", this.getSignature());
  }

  @Override
  protected void writeClassName()
  {
    String baseName = ClassDTOBaseGenerator.getGeneratedName(this.getMdTypeDAOIF());
    String prefix = "public class";

    if (this.getMdTypeDAOIF().isAbstract() || GenerationUtil.isHardcodedType(this.getMdTypeDAOIF()))
    {
      prefix = "public abstract class";
    }

    getWriter().writeLine(prefix + " " + getFileName() + " extends " + baseName);

    getWriter().openBracket();
  }

  /**
   * Write the default constructor for the generated EntityDTO source class
   */
  protected void writeConstructor()
  {
    getWriter().writeLine("public " + getFileName() + "(" + ClientRequestIF.class.getName() + " clientRequest)");
    getWriter().openBracket();
    getWriter().writeLine("super(clientRequest);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  @Override
  protected String getRootSourceDirectory()
  {
    return getRootClientOrCommonStubDirectory(this.getMdTypeDAOIF());
  }

  @Override
  protected MdClassDAOIF getMdTypeDAOIF()
  {
    return (MdClassDAOIF) super.getMdTypeDAOIF();
  }

  @Override
  public String getClassAttribute()
  {
    return MdClassInfo.DTO_STUB_CLASS;
  }

  @Override
  public String getSourceAttribute()
  {
    return MdClassInfo.DTO_STUB_SOURCE;
  }
}
