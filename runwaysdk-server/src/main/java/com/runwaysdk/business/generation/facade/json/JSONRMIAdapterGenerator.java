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
package com.runwaysdk.business.generation.facade.json;

import java.rmi.RemoteException;
import java.util.List;

import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.metadata.Type;
import com.runwaysdk.generation.loader.Reloadable;

public class JSONRMIAdapterGenerator extends AbstractJSONAdapterGenerator
{

  public JSONRMIAdapterGenerator(MdFacadeDAOIF mdFacade)
  {
    super(mdFacade, getGeneratedName(mdFacade));
  }

  public static void generateAdapter(MdFacadeDAOIF mdFacadeIF)
  {
    JSONRMIAdapterGenerator generator = new JSONRMIAdapterGenerator(mdFacadeIF);
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
    addSerialVersionUID();
    writeConstructor();
    writeFacadeMethods();
    getWriter().closeBracket();
    getWriter().close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("JSON_RMI_ADAPTER", this.getSignature());
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
    String remoteAdapter = JSONRemoteAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF());
    getWriter().write("public class " + getFileName() + " implements " + remoteAdapter);

    if (!this.getMdTypeDAOIF().isSystemPackage())
    {
      getWriter().write(", "+Reloadable.class.getName());
    }
    getWriter().writeLine("");

    getWriter().openBracket();
  }

  private void writeConstructor()
  {
    getWriter().writeLine("public " + getFileName() + "() throws " + RemoteException.class.getName());
    getWriter().openBracket();
    getWriter().closeBracket();
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
    List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();
    String parameters = "String sessionId" + GenerationUtil.buildJSONParameters(list, false);
    Type returnType = mdMethod.getReturnType();
    String methodName = mdMethod.getValue(MdMethodInfo.NAME);

    getWriter().writeLine("public " + String.class.getName() + " " + methodName + "(" + parameters + ") throws " + RemoteException.class.getName());

    super.writeMethodBody(returnType, mdMethod, list, methodName);
  }

  public static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return "JSONRMI" + mdFacade.getValue(MdFacadeInfo.NAME) + "Adapter";
  }
}
