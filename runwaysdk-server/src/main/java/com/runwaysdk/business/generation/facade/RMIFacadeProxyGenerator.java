/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.business.generation.facade;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import com.runwaysdk.MessageExceptionDTO;
import com.runwaysdk.business.generation.AbstractClientGenerator;
import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.ClientConversionFacadeINFO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.metadata.Type;
import com.runwaysdk.generation.loader.Reloadable;

public class RMIFacadeProxyGenerator extends AbstractClientGenerator
{
  public static final String UNBIND_METHOD_NAME = "unbindRMIProxy";

  public RMIFacadeProxyGenerator(MdFacadeDAOIF mdFacade)
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
    return this.getSerialVersionUID("RMI_FACADE_PROXY", this.getSignature());
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
    String abstractName = FacadeProxyGenerator.getGeneratedName(this.getMdTypeDAOIF());
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
    String adapterType = RemoteAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF());

    getWriter().writeLine("");
    getWriter().writeLine("private java.lang.String label;");
    getWriter().writeLine("private java.lang.String address;");
    getWriter().writeLine("private " + adapterType + " adapter;");
    getWriter().writeLine("");
  }

  private void writeConstructor()
  {
    String adapterType = RemoteAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF());

    getWriter().writeLine("public " + getFileName() + "(java.lang.String label, java.lang.String address)");
    getWriter().openBracket();
    getWriter().writeLine("this.label = label;");
    getWriter().writeLine("this.address = address;");
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine("this.adapter = (" + adapterType + ") " + Naming.class.getName() + ".lookup(this.address + " + RemoteAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF()) + ".SERVICE_NAME);");
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
    getWriter().writeLine("public void " + UNBIND_METHOD_NAME +
    		"()");
    getWriter().openBracket();
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine(Naming.class.getName() + ".unbind(address + " + RemoteAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF()) + ".SERVICE_NAME);");
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
    Type returnType = mdMethod.getReturnType(); //.getDTOType();
    List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();
    String parameters = ClientRequestIF.class.getName()+" ___clientRequestIF" + GenerationUtil.buildDTOParameters(list, false);

    getWriter().writeLine("public " + returnType.getDTOType() + " " + methodName + "(" + parameters + ")");
    getWriter().openBracket();

    getWriter().writeLine(ClientRequestIF.CLASS+".clearNotifications(("+ClientRequestIF.CLASS+")___clientRequestIF);");

    for(MdParameterDAOIF mdParameter : list)
    {
      Type parameterType = mdParameter.getParameterType();
      String name = mdParameter.getParameterName();
      String type = parameterType.getType();

      String convertString = type + " _" + name + " = " + name + ";";

      if(!parameterType.isPrimitive())
      {
        String genericType = parameterType.getGenericDTOType();
        convertString = genericType + " _" + name + " = " + "(" + genericType + ") " + JavaFacadeProxyGenerator.CONVERSION_FACADE + "." +
                        JavaFacadeProxyGenerator.CONVERT_GENERIC_CODE +"(" + name + ");";
      }

      getWriter().writeLine(convertString);
    }

    if(list.size() > 0)
    {
      getWriter().writeLine("");
    }

    String callString = "___clientRequestIF.getSessionId()" + GenerationUtil.buildCallParameter(list, "_", false);
    String methodCallCode = "adapter." + methodName + "(" + callString + ");";

    String grabTypeSafeMessages = ClientRequestIF.CLASS+".setMessages(("+ClientRequestIF.CLASS+")___clientRequestIF, me);";

    String genericReturnType = "";
    String completeMethodCallStatement = "";
    String grabReturnObject = "";
    String returnStatement = "";

    if(!returnType.isVoid())
    {
      //If the return type is not primitve write the code to convert the generic DTO to its type safe form
      if(!returnType.isPrimitive())
      {
        genericReturnType = returnType.getGenericDTOType();
        grabReturnObject = "___output = ("+genericReturnType+")me.getReturnObject();";
        completeMethodCallStatement = "___output = ("+genericReturnType+")" + methodCallCode;
        getWriter().writeLine(genericReturnType+" ___output = null;");

        String javaType = returnType.getDTOJavaClass();

        returnStatement =  "return (" + returnType.getDTOType() +") " + JavaFacadeProxyGenerator.CONVERSION_FACADE + "." +
          JavaFacadeProxyGenerator.TYPE_SAFE_CODE + "(___clientRequestIF, \"" + javaType + "\", ___output);";
      }
      else
      {
        genericReturnType = returnType.getDTOType();
        grabReturnObject = "___output = ("+genericReturnType+")me.getReturnObject();";
        completeMethodCallStatement = "___output = ("+genericReturnType+")" + methodCallCode;
        getWriter().writeLine(genericReturnType+" ___output = null;");

        returnStatement = "return ___output;";
      }
    }
    else
    {
      completeMethodCallStatement = methodCallCode;
    }

    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine(completeMethodCallStatement);
    getWriter().closeBracket();
    // Catch message exception and convert object
    getWriter().writeLine("catch("+Throwable.class.getName()+" e)");
    getWriter().openBracket();

    getWriter().writeLine(RuntimeException.class.getName()+" rte = "+ClientConversionFacadeINFO.CLASS+".buildThrowable(e, ___clientRequestIF, false);");
    getWriter().writeLine("if (rte instanceof "+MessageExceptionDTO.class.getName()+")");
    getWriter().openBracket();
    getWriter().writeLine(MessageExceptionDTO.class.getName()+" me = ("+MessageExceptionDTO.class.getName()+")rte;");
    getWriter().writeLine(grabReturnObject);
    getWriter().writeLine(grabTypeSafeMessages);
    getWriter().closeBracket();
    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine("throw rte;");
    getWriter().closeBracket();

    getWriter().closeBracket();

    getWriter().writeLine(returnStatement);

    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  public static void generateProxy(MdFacadeDAOIF mdFacadeIF)
  {
    RMIFacadeProxyGenerator generator = new RMIFacadeProxyGenerator(mdFacadeIF);
    generator.go(false);
  }

  public static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return "RMI" + mdFacade.getValue(MdFacadeInfo.NAME) + "Proxy";
  }

}
