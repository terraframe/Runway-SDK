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
package com.runwaysdk.business.generation.facade;

import java.util.List;

import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.business.generation.AbstractServerGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.metadata.Type;
import com.runwaysdk.generation.loader.Reloadable;

public class JavaAdapterGenerator extends AbstractServerGenerator
{

  public JavaAdapterGenerator(MdFacadeDAOIF mdFacade)
  {
    super(mdFacade, getGeneratedName(mdFacade));
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
    writeFacadeMethods();
    getWriter().closeBracket();
    getWriter().close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("ADAPTER", this.getSignature());
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
    getWriter().writeLine("package " + this.getMdTypeDAOIF().getPackage() + ";");
    getWriter().writeLine("");
  }


  /**
   * Generates the class name declaration
   */
  private void writeClassName()
  {
    String adapterName = this.getFileName();

    getWriter().write("public class " + adapterName);
    if (!this.getMdTypeDAOIF().isSystemPackage())
    {
      getWriter().write(Reloadable.IMPLEMENTS);
    }
    getWriter().writeLine("");

    getWriter().openBracket();
  }

  private void writeFacadeMethods()
  {
    for(MdMethodDAOIF mdMethod : this.getMdTypeDAOIF().getMdMethodsOrdered())
    {
      writeMethod(mdMethod);
    }
  }

  private void writeMethod(MdMethodDAOIF mdMethod)
  {
    Type returnType = mdMethod.getReturnType();
    String methodName = mdMethod.getValue(MdMethodInfo.NAME);
    List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();
    String parameters = "String sessionId" + GenerationUtil.buildGenericDTOParameters(list, false);

    getWriter().writeLine("public static " + returnType.getGenericDTOType() + " " + methodName + "(" + parameters + ")");
    getWriter().openBracket();

    String facadeName = this.getMdTypeDAOIF().definesType() + TypeGeneratorInfo.BASE_SUFFIX;
    String callString = "sessionId" + GenerationUtil.buildCallParameter(list, "", false);

    if(!returnType.isVoid())
    {
      getWriter().writeLine("return " + facadeName + "." + methodName + "Base(" + callString + ");");
    }
    else
    {
      getWriter().writeLine(facadeName + "." + methodName + "Base(" + callString + ");");
    }

    getWriter().closeBracket();
    getWriter().writeLine("");
  }


  public static void generateAdapter(MdFacadeDAOIF mdFacadeIF)
  {
    JavaAdapterGenerator generator = new JavaAdapterGenerator(mdFacadeIF);
    generator.go(false);
  }

  public static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return "Java" + mdFacade.getValue(MdFacadeInfo.NAME) + "Adapter";
  }
}
