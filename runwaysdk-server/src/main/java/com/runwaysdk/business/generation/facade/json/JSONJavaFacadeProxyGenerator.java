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
package com.runwaysdk.business.generation.facade.json;

import java.util.List;

import com.runwaysdk.business.generation.AbstractClientGenerator;
import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.ClientConversionFacadeINFO;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;

public class JSONJavaFacadeProxyGenerator extends AbstractClientGenerator
{
  public JSONJavaFacadeProxyGenerator(MdFacadeDAOIF mdFacade)
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
    return this.getSerialVersionUID("JSON_PROXY", this.getSignature());
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
    String stringType = String.class.getName();
    String className = this.getMdTypeDAOIF().getPackage() + "." + getGeneratedName(this.getMdTypeDAOIF());

    getWriter().writeLine("public static final String CLASS = \"" + className + "\";");
    getWriter().writeLine("");
    getWriter().writeLine("private " + stringType + " label;");
    getWriter().writeLine("private " + stringType + " address;");
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
    List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();
    String parameters = String.class.getName() + " sessionId" + GenerationUtil.buildJSONParameters(list, false);
    String adapterName = this.getMdTypeDAOIF().getPackage()+"."+JSONJavaAdapterGenerator.getGeneratedName(this.getMdTypeDAOIF());
    String callString = "sessionId" + GenerationUtil.buildCallParameter(list, "", false);

    String methodCall = "method.invoke(null, " + callString + ");";

    String paramTypes = "java.lang.String.class,";
    for(int i=0; i<list.size(); i++)
    {
      paramTypes += "java.lang.String.class,";
    }
    paramTypes = paramTypes.replaceFirst(",$", "");

    getWriter().writeLine("public " + String.class.getName() + " " + methodName + "(" + parameters + ")");
    getWriter().openBracket();
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine("Class clazz = "+LoaderDecorator.class.getName()+".load(\""+adapterName+"\");");
    getWriter().writeLine("java.lang.reflect.Method method = clazz.getMethod(\""+methodName+"\", "+paramTypes+");");
    getWriter().writeLine("return ("+String.class.getName()+")" + methodCall);
    getWriter().closeBracket();
    getWriter().writeLine("catch (java.lang.Throwable e)");
    getWriter().openBracket();
    getWriter().writeLine("throw "+ClientConversionFacadeINFO.CLASS+".buildJSONThrowable(e, sessionId, false);");
    getWriter().closeBracket();
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  public static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return "JSONJava" + mdFacade.getValue(MdFacadeInfo.NAME) + "Proxy";
  }
}
