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
package com.runwaysdk.business.generation.facade;

import java.io.File;

import com.runwaysdk.business.generation.AbstractServerGenerator;
import com.runwaysdk.business.generation.StubMarker;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdFacadeDAO;
import com.runwaysdk.generation.loader.Reloadable;

public class FacadeStubGenerator extends AbstractServerGenerator implements StubMarker
{

  public FacadeStubGenerator(MdFacadeDAOIF mdFacadeIF)
  {
    super(mdFacadeIF, getGeneratedName(mdFacadeIF));
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

    AttributeIF stubSource = ((MdFacadeDAO)getMdTypeDAOIF()).getAttributeIF(MdEntityInfo.STUB_SOURCE);
    boolean empty = stubSource.getValue().trim().equals("");

    // This cast is OK, as the object is not modified here.  Just need to check
    // if the stub source has changed.  If so, it needs to be copied to the file system.
    if (stubSource.isModified() && !empty)
    {
      getWriter().write(this.getMdTypeDAOIF().getAttributeIF(MdEntityInfo.STUB_SOURCE).getValue());
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
  protected MdFacadeDAOIF getMdTypeDAOIF()
  {
    return (MdFacadeDAOIF)super.getMdTypeDAOIF();
  }

  private void generate()
  {
    writePackage();
    writeClassName();

    addSerialVersionUID();
    getWriter().closeBracket();
    getWriter().close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("STUB", this.getSignature());
  }

  private void writePackage()
  {
    getWriter().writeLine("package " + this.getMdTypeDAOIF().getPackage() + ";");
    getWriter().writeLine("");
  }

  /**
   * Generates the class name declaration
   */
  private void writeClassName()
  {
    String typeName = this.getMdTypeDAOIF().getTypeName();

    getWriter().writeLine("public class " + typeName + " extends " + typeName + TypeGeneratorInfo.BASE_SUFFIX);
    if (!this.getMdTypeDAOIF().isSystemPackage())
    {
      getWriter().write(Reloadable.IMPLEMENTS);
    }
    getWriter().writeLine("");
    getWriter().openBracket();
  }

  static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return mdFacade.getTypeName();
  }

  @Override
  protected String getRootSourceDirectory()
  {
    return getRootServerStubDirectory(this.getPackage());
  }
}
