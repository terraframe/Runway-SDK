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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.w3c.dom.Document;

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
import com.runwaysdk.transport.conversion.ConversionFacade;

public class WebServiceFacadeProxyGenerator extends AbstractClientGenerator
{
  public WebServiceFacadeProxyGenerator(MdFacadeDAOIF mdFacade)
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
    return this.getSerialVersionUID("WS_FACADE_PROXY", this.getSignature());
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
    String adapterName = this.getMdTypeDAOIF().getPackage()+"."+WebServiceAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF());

    getWriter().writeLine("private " + Call.class.getName() + " newCall()");
    getWriter().openBracket();
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine(Service.class.getName() + " service = new " + Service.class.getName() + "();");
    getWriter().writeLine(Call.class.getName() + " call = (" + Call.class.getName() + ") service.createCall();");
    getWriter().writeLine("call.setTargetEndpointAddress(new " + URL.class.getName() + "(this.address + \""+adapterName+"\"));");
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
    Type returnType = mdMethod.getReturnType();
    String methodName = mdMethod.getValue(MdMethodInfo.NAME);
    List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();
    String parameters = ClientRequestIF.class.getName()+" ___clientRequestIF" + GenerationUtil.buildDTOParameters(list, false);

    getWriter().writeLine("public " + returnType.getDTOType() + " " + methodName + "(" + parameters + ")");
    getWriter().openBracket();

    getWriter().writeLine(ClientRequestIF.CLASS+".clearNotifications(("+ClientRequestIF.CLASS+")___clientRequestIF);");

    //Write the code that converts the parameters to their generic and then document form.
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

      String documentString = Document.class.getName() + " __" + name + " = " + ConversionFacade.class.getName() + ".getDocumentFromObject(_" + name + ", false);";

      getWriter().writeLine(convertString);
      getWriter().writeLine(documentString);
      getWriter().writeLine("");
    }

    //Write the code that gets the facade from the WebServiceLocator
    getWriter().writeLine(Call.class.getName() + " ___call = newCall();");
    getWriter().writeLine("Object[] ___params = {___clientRequestIF.getSessionId()" + GenerationUtil.buildCallParameter(list, "__", false) + "};");

    if(!returnType.isVoid())
    {
      if(!returnType.isPrimitive())
      {
        getWriter().writeLine("Object ___generic = null;");
      }
      else
      {
        getWriter().writeLine(returnType.getType()+" ___primitiveObject = null;");
      }
    }

    String callString = "___call.invoke(\"" + methodName + "\", ___params);";
    String completeCallStatement;


    if(!returnType.isVoid())
    {
      getWriter().writeLine(Document.class.getName() + " ___output = null;");

      completeCallStatement = "___output = (" + Document.class.getName() + ") " + callString;
    }
    else
    {
      completeCallStatement = callString;
    }

    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine(completeCallStatement);
    getWriter().closeBracket();
    getWriter().writeLine("catch("+Throwable.class.getName()+" e)");
    getWriter().openBracket();

    getWriter().writeLine(RuntimeException.class.getName()+" rte = "+ClientConversionFacadeINFO.CLASS+".buildThrowable(e, ___clientRequestIF, true);");
    getWriter().writeLine("if (rte instanceof "+MessageExceptionDTO.class.getName()+")");
    getWriter().openBracket();
    getWriter().writeLine(MessageExceptionDTO.class.getName()+" me = ("+MessageExceptionDTO.class.getName()+")rte;");
    if (!returnType.isVoid())
    {
      if (!returnType.isPrimitive())
      {
        getWriter().writeLine("___generic = me.getReturnObject();");
      }
      else
      {
        getWriter().writeLine("___primitiveObject = ("+returnType.getType()+")me.getReturnObject();");
      }
    }

    getWriter().writeLine(ClientRequestIF.CLASS+".setMessages(("+ClientRequestIF.CLASS+")___clientRequestIF, me);");
    getWriter().closeBracket();
    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine("throw rte;");
    getWriter().closeBracket();
    getWriter().closeBracket();

    if (!returnType.isVoid())
    {
      if (!returnType.isPrimitive())
      {
        getWriter().writeLine("if (___generic == null)");
        getWriter().openBracket();
        getWriter().writeLine("___generic = " + ConversionFacade.class.getName() + ".getObjectFromDocument(___clientRequestIF, ___output);");
        getWriter().closeBracket();

        String javaType = returnType.getDTOJavaClass();
        getWriter().writeLine("return (" + returnType.getDTOType() +") " + JavaFacadeProxyGenerator.CONVERSION_FACADE + "." +
            JavaFacadeProxyGenerator.TYPE_SAFE_CODE + "(___clientRequestIF, \"" + javaType + "\", ___generic);");
      }
      else
      {
        getWriter().writeLine("if (___primitiveObject == null)");
        getWriter().openBracket();
        getWriter().writeLine("return (" + returnType.getType() + ")" + ConversionFacade.class.getName() + ".getObjectFromDocument(___clientRequestIF, ___output);");
        getWriter().closeBracket();
        getWriter().writeLine("else");
        getWriter().openBracket();
        getWriter().writeLine("return ___primitiveObject;");
        getWriter().closeBracket();
      }
    }

    //Method bracket
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  public static void generateProxy(MdFacadeDAOIF mdFacadeIF)
  {
    WebServiceFacadeProxyGenerator generator = new WebServiceFacadeProxyGenerator(mdFacadeIF);
    generator.go(false);
  }

  public static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return "WebService" + mdFacade.getValue(MdFacadeInfo.NAME) + "Proxy";
  }
}
