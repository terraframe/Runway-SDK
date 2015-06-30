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
package com.runwaysdk.business.generation.facade.json;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import com.runwaysdk.business.generation.AbstractClientGenerator;
import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.ClientConversionFacadeINFO;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.generation.loader.Reloadable;

public class JSONRMIFacadeProxyGenerator extends AbstractClientGenerator
{
  public JSONRMIFacadeProxyGenerator(MdFacadeDAOIF mdFacade)
  {
    super(mdFacade, getGeneratedName(mdFacade));
  }

  public static void generateFacadeProxy(MdFacadeDAOIF mdFacadeIF)
  {
    JSONRMIFacadeProxyGenerator generator = new JSONRMIFacadeProxyGenerator(mdFacadeIF);
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
    addSerialVersionUID();
    writeConstructor();
    wirteUnbindProxy();
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
    return this.getSerialVersionUID("JSON_RMI_FACADE", this.getSignature());
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
    String abstractName = JSONFacadeProxyGenerator.getGeneratedName(this.getMdTypeDAOIF());
    getWriter().write("public class " + getFileName() + " extends " + abstractName);

    if (!this.getMdTypeDAOIF().isSystemPackage())
    {
      getWriter().write(Reloadable.IMPLEMENTS);
    }
    getWriter().writeLine("");

    getWriter().openBracket();
  }

  private void writeFields()
  {
    String adapterType = JSONRemoteAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF());
    String className = this.getMdTypeDAOIF().getPackage() + "." + getGeneratedName(this.getMdTypeDAOIF());

    getWriter().writeLine("public static final String CLASS = \"" + className + "\";");
    getWriter().writeLine("");
    getWriter().writeLine("private java.lang.String label;");
    getWriter().writeLine("private java.lang.String address;");
    getWriter().writeLine("private " + adapterType + " adapter;");
    getWriter().writeLine("");
  }

  private void writeConstructor()
  {
    String adapterType = JSONRemoteAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF());

    getWriter().writeLine("public " + getFileName() + "(java.lang.String label, java.lang.String address)");
    getWriter().openBracket();
    getWriter().writeLine("this.label = label;");
    getWriter().writeLine("this.address = address;");
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine("this.adapter = (" + adapterType + ") " + Naming.class.getName() + ".lookup(this.address + " + JSONRemoteAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF()) + ".SERVICE_NAME);");
    getWriter().closeBracket();
    getWriter().writeLine("catch (" + MalformedURLException.class.getName() +" e)");
    getWriter().openBracket();
    getWriter().writeLine("throw new " + ExceptionConstants.RMIClientException.getExceptionClass() +"(e);");
    getWriter().closeBracket();
    getWriter().writeLine("catch (" + RemoteException.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("throw new " + ExceptionConstants.RMIClientException.getExceptionClass() +"(e);");
    getWriter().closeBracket();
    getWriter().writeLine("catch (" + NotBoundException.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("throw new " + ExceptionConstants.RMIClientException.getExceptionClass() +"(e);");
    getWriter().closeBracket();
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  private void wirteUnbindProxy()
  {
    getWriter().writeLine("public void unbindRMIProxy()");
    getWriter().openBracket();
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine(Naming.class.getName() + ".unbind(address + " + JSONRemoteAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF()) + ".SERVICE_NAME);");
    getWriter().closeBracket();
    getWriter().writeLine("catch (" + MalformedURLException.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("throw new " + ExceptionConstants.RMIClientException.getExceptionClass() + "(e);");
    getWriter().closeBracket();
    getWriter().writeLine("catch (" + RemoteException.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("throw new " + ExceptionConstants.RMIClientException.getExceptionClass() +"(e);");
    getWriter().closeBracket();
    getWriter().writeLine("catch (" + NotBoundException.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("throw new " + ExceptionConstants.RMIClientException.getExceptionClass() + "(e);");
    getWriter().closeBracket();
    getWriter().closeBracket();
    getWriter().writeLine("");
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
    String methodName = mdMethod.getValue(MdMethodInfo.NAME);
    List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();
    String parameters = String.class.getName() + " sessionId" + GenerationUtil.buildJSONParameters(list, false);
    String callString = "sessionId" + GenerationUtil.buildCallParameter(list, "", false);
    String methodCall = "adapter." + methodName + "(" + callString + ");";

    getWriter().writeLine("public " + String.class.getName() + " " + methodName + "(" + parameters + ")");
    getWriter().openBracket();
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine("return "+methodCall);
    getWriter().closeBracket();

    getWriter().writeLine("catch(" + RuntimeException.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("throw " + ClientConversionFacadeINFO.CLASS + ".buildJSONThrowable(e, sessionId, false);");
    getWriter().closeBracket();
    getWriter().writeLine("catch(" + RemoteException.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("throw " + ClientConversionFacadeINFO.CLASS + ".buildJSONThrowable(e, sessionId, false);");
    getWriter().closeBracket();
    getWriter().closeBracket();
    getWriter().writeLine("");

  }

  public static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return "JSONRMI" + mdFacade.getValue(MdFacadeInfo.NAME) + "Proxy";
  }
}
