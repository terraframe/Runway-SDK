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

import java.util.List;

import com.runwaysdk.business.generation.AbstractClientGenerator;
import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ClientRequestMarker;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.generation.loader.Reloadable;

public class FacadeProxyIFGenerator extends AbstractClientGenerator
{
  public FacadeProxyIFGenerator(MdFacadeDAOIF mdFacade)
  {
    super(mdFacade, getGeneratedName(mdFacade));
  }

  public static void generateProxy(MdFacadeDAOIF mdFacadeIF)
  {
    FacadeProxyIFGenerator generator = new FacadeProxyIFGenerator(mdFacadeIF);
    generator.go(false);
  }

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    // Do regenerate if the existing file is symantically the same
    if (LocalProperties.isKeepBaseSource() &&
        AbstractGenerator.hashEquals(this.getSerialVersionUID(), this.getPath()))
    {
      return;
    }

    writePackage();
    addSignatureAnnotation();
    writeClassName();
    writeFields();
    writeFacadeMethods();
    getWriter().closeBracket();
    getWriter().close();
  }

  /**
   * @see com.runwaysdk.business.generation.AbstractGenerator#getRootSourceDirectory()
   */
  protected String getRootSourceDirectory()
  {
    return getRootClientBaseDirectory(this.getMdTypeDAOIF());
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("PROXYIF", this.getSignature());
  }

  @Override
  protected MdFacadeDAOIF getMdTypeDAOIF()
  {
    return (MdFacadeDAOIF)super.getMdTypeDAOIF();
  }

  /**
   * Generate the package declaration
   */
  private void writePackage()
  {
    getWriter().writeLine("package " + getMdTypeDAOIF().getPackage() + ";");
    getWriter().writeLine("");
  }

  /**
   * Generates the class name declaration
   */
  private void writeClassName()
  {
    getWriter().write("public interface " + getFileName() + " extends " + ClientRequestMarker.class.getName());

    if (!this.getMdTypeDAOIF().isSystemPackage())
    {
      getWriter().write(", " + Reloadable.class.getName());
    }

    getWriter().writeLine("");

    getWriter().openBracket();
  }

  private void writeFields()
  {
    String className = getMdTypeDAOIF().definesType();

    getWriter().writeLine("public static final String CLASS = \"" + className + "\";");
  }

  private void writeFacadeMethods()
  {
    for(MdMethodDAOIF mdMethod : getMdTypeDAOIF().getMdMethodsOrdered())
    {
      writeMethod(mdMethod);
    }
  }

  public void writeMethod(MdMethodDAOIF mdMethod)
  {
    String methodName = mdMethod.getValue(MdMethodInfo.NAME);
    String returnType = mdMethod.getReturnType().getDTOType();
    List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();
    String parameters = ClientRequestIF.class.getName()+" ___clientRequestIF" + GenerationUtil.buildDTOParameters(list, false);

    getWriter().writeLine("public " + returnType + " " + methodName + "(" + parameters + ");");
    getWriter().writeLine("");
  }

  public static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return mdFacade.getValue(MdFacadeInfo.NAME) + "ClientRequestIF";
  }
}
