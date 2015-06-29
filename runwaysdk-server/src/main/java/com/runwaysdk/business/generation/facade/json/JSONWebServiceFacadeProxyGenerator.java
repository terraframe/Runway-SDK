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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.runwaysdk.business.generation.AbstractClientGenerator;
import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.facade.WebServiceAdapterGenerator;
import com.runwaysdk.business.generation.facade.WebServiceFacadeProxyGenerator;
import com.runwaysdk.constants.ClientConversionFacadeINFO;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.generation.loader.Reloadable;

public class JSONWebServiceFacadeProxyGenerator extends AbstractClientGenerator
{
  public JSONWebServiceFacadeProxyGenerator(MdFacadeDAOIF mdFacade)
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
    writeFields();
    addSerialVersionUID();
    writeConstructor();
    writeNewCall();
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
    return this.getSerialVersionUID("JSON_WS_FACADE_PROXY", this.getSignature());
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
    getWriter().writeLine("");
    getWriter().writeLine("private " + String.class.getName() + " label;");
    getWriter().writeLine("private " + String.class.getName() + " address;");
    getWriter().writeLine("");
  }


  private void writeConstructor()
  {
    getWriter().writeLine("public " + getFileName() + "(String label, String address)");
    getWriter().openBracket();
    getWriter().writeLine("this.label = label;");
    getWriter().writeLine("this.address = address;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  private void writeNewCall()
  {
    String facadeProxyName = this.getMdTypeDAOIF().getPackage()+"."+WebServiceAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF());

    getWriter().writeLine("private " + Call.class.getName() + " newCall()");
    getWriter().openBracket();
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine(Service.class.getName() + " service = new " + Service.class.getName() + "();");
    getWriter().writeLine(Call.class.getName() + " call = (" + Call.class.getName() + ") service.createCall();");
    getWriter().writeLine("call.setTargetEndpointAddress(new " + URL.class.getName() + "(this.address + \""+facadeProxyName+"\"));");
    getWriter().writeLine("return call;");
    getWriter().closeBracket();
    getWriter().writeLine("catch (" + ServiceException.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("throw new " + ExceptionConstants.WebServiceClientRequestException.getExceptionClass() + "(e);");
    getWriter().closeBracket();
    getWriter().writeLine("catch (" + MalformedURLException.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("throw new " + ExceptionConstants.WebServiceClientRequestException.getExceptionClass() + "(e);");
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
    String parameters = "String sessionId" + GenerationUtil.buildJSONParameters(list, false);

    getWriter().writeLine("public " + String.class.getName() + " " + methodName + "(" + parameters + ")");
    getWriter().openBracket();
    getWriter().writeLine("try");
    getWriter().openBracket();

    //Write the code that gets the facade from the WebServiceLocator
    getWriter().writeLine(Call.class.getName() + " ___call = newCall();");
    getWriter().writeLine("Object[] ___params = {sessionId" + GenerationUtil.buildCallParameter(list, "", false) + "};");
    String callString = "___call.invoke(\"" + methodName + "\", ___params);";

    getWriter().writeLine(" return (" + String.class.getName() + ") " + callString);

    getWriter().closeBracket();
    getWriter().writeLine("catch(" + Exception.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("throw " + ClientConversionFacadeINFO.CLASS + ".buildJSONThrowable(e, sessionId, true);");
    getWriter().closeBracket();
    //Method bracket
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  public static void generateFacadeProxy(MdFacadeDAOIF mdFacadeIF)
  {
    WebServiceFacadeProxyGenerator generator = new WebServiceFacadeProxyGenerator(mdFacadeIF);
    generator.go(false);
  }

  public static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return "JSONWebService" + mdFacade.getValue(MdFacadeInfo.NAME) + "ClientRequest";
  }

}
