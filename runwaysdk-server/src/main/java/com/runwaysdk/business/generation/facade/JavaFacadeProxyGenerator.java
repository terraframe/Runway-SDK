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

import java.lang.reflect.Method;
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
import com.runwaysdk.transport.conversion.ConversionFacade;

public class JavaFacadeProxyGenerator extends AbstractClientGenerator
{
  /**
   * The name of the convert generic method in the ConversionFacade class
   */
  public static final String CONVERT_GENERIC_CODE = "convertGeneric";

  /**
   * The name of the convertToTypeSafe method in the ConversionFacade class
   */
  public static final String TYPE_SAFE_CODE = "convertToTypeSafe";

  /**
   * Name of the Conversion class
   */
  public static final String CONVERSION_FACADE = ConversionFacade.class.getName();

  public JavaFacadeProxyGenerator(MdFacadeDAOIF mdFacade)
  {
    super(mdFacade, getGeneratedName(mdFacade));
  }

  public static void generateProxy(MdFacadeDAOIF mdFacadeIF)
  {
    JavaFacadeProxyGenerator generator = new JavaFacadeProxyGenerator(mdFacadeIF);
    generator.go(false);
  }

  /**
   * @see com.runwaysdk.business.generation.AbstractGenerator#getRootSourceDirectory()
   */
  protected String getRootSourceDirectory()
  {
    return getRootClientBaseDirectory(this.getMdTypeDAOIF());
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
    writeFacadeMethods();
    getWriter().closeBracket();
    getWriter().close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("JAVA_PROXY", this.getSignature());
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
    getWriter().writeLine("private String label;");
    getWriter().writeLine("private String address;");
    getWriter().writeLine("");
  }

  private void writeConstructor()
  {
    String stringType = String.class.getName();
    getWriter().writeLine("public " + getFileName() + "(" + stringType + " label, " + stringType + " address)");
    getWriter().openBracket();
    getWriter().writeLine("this.label = label;");
    getWriter().writeLine("this.address = address;");
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
      String name = mdParameter.getParameterName();
      Type parameterType = mdParameter.getParameterType();
      String type = parameterType.getType();

      String convertString = type + " _" + name + " = " + name + ";";

      if(!parameterType.isPrimitive())
      {
        String genericType = parameterType.getGenericDTOType();
        convertString = genericType + " _" + name + " = " + "(" + genericType + ") " + CONVERSION_FACADE + "." +
                        CONVERT_GENERIC_CODE + "(" + name + ");";
      }

      getWriter().writeLine(convertString);
    }

    String adapterName = this.getMdTypeDAOIF().getPackage() + "." + JavaAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF());
    String callString = "___clientRequestIF.getSessionId()" + GenerationUtil.buildCallParameter(list, "_", false);

    getWriter().writeLine("");
    getWriter().writeLine(Class.class.getName()+" clazz;");
    getWriter().writeLine(Method.class.getName()+" method;");
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine("clazz = com.runwaysdk.generation.loader.LoaderDecorator.load(\""+adapterName+"\");");

    String paramTypes = "java.lang.String.class,";
    for(MdParameterDAOIF mdParameter : list)
    {
      Type paramType = mdParameter.getParameterType();
      String loadType;
      if(paramType.isPrimitive())
      {
        loadType = paramType.getType();
      }
      else
      {
        loadType = paramType.getGenericDTOType();
      }
      paramTypes += loadType+".class,";
    }
    paramTypes = paramTypes.replaceFirst(",$", "");

    getWriter().writeLine("method = clazz.getMethod(\""+methodName+"\", "+paramTypes+");");

    getWriter().closeBracket();
    getWriter().writeLine("catch (java.lang.Throwable e)");
    getWriter().openBracket();
    getWriter().writeLine("throw new "+ExceptionConstants.ClientProgrammingErrorException.getExceptionClass()+"(e);");
    getWriter().closeBracket();

    String invokeMethod = "method.invoke(null, " + callString + ");";
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

        completeMethodCallStatement = "___output = ("+genericReturnType+") " + invokeMethod;

        getWriter().writeLine(genericReturnType + " ___output = null;");

        String javaType = returnType.getDTOJavaClass();

        returnStatement = "return (" + returnType.getDTOType() +") " + CONVERSION_FACADE + "." +
          TYPE_SAFE_CODE + "(___clientRequestIF, \"" + javaType + "\", ___output);";
      }
      else
      {
        genericReturnType = returnType.getDTOType();
        grabReturnObject = "___output = ("+genericReturnType+")me.getReturnObject();";
        completeMethodCallStatement = "___output = ("+genericReturnType+") " + invokeMethod;

        getWriter().writeLine(genericReturnType + " ___output = null;");

        returnStatement = "return ___output;";
      }
    }
    else
    {
      completeMethodCallStatement = invokeMethod;
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

  public static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return "Java" + mdFacade.getValue(MdFacadeInfo.NAME) + "Proxy";
  }
}
