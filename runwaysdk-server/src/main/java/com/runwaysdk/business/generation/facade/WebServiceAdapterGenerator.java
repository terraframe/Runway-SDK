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

import java.rmi.RemoteException;
import java.util.List;

import org.w3c.dom.Document;

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
import com.runwaysdk.facade.WebServiceAdapter;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.transport.conversion.ConversionFacade;

public class WebServiceAdapterGenerator extends AbstractServerGenerator
{

  public WebServiceAdapterGenerator(MdFacadeDAOIF mdFacade)
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
    return this.getSerialVersionUID("WS_ADAPTER", this.getSignature());
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

  private String getDocumentConversionCode(String suffix, String parameterName, String type)
  {
    String facadeName = ConversionFacade.class.getName();
    return type + " " + suffix + parameterName + " = (" + type + ") " + facadeName + ".getObjectFromDocument(null, " + parameterName + ");";

  }

  private void writeMethod(MdMethodDAOIF mdMethod)
  {
    String methodName = mdMethod.getValue(MdMethodInfo.NAME);
    List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();
    Type returnType = mdMethod.getReturnType();
    String parameters = "String sessionId" + GenerationUtil.buildDocumentParameters(list, false);
    String callString = "sessionId" + GenerationUtil.buildCallParameter(list, "_", false);
    String facadeName = this.getMdTypeDAOIF().definesType() + TypeGeneratorInfo.BASE_SUFFIX;

    if(!returnType.isVoid())
    {
      getWriter().writeLine("public static " + Document.class.getName() + " " + methodName + "(" + parameters + ") throws "+RemoteException.class.getName());
    }
    else
    {
      getWriter().writeLine("public static void " + methodName + "(" + parameters + ") throws "+RemoteException.class.getName());
    }
    getWriter().openBracket();

    for(MdParameterDAOIF mdParameter : list)
    {
      String genericType = mdParameter.getParameterType().getGenericDTOType();
      String name = mdParameter.getParameterName();

      String code = getDocumentConversionCode("_", name, genericType);

      getWriter().writeLine(code);
    }

    getWriter().writeLine("");

    if(!returnType.isVoid())
    {
      getWriter().writeLine(Document.class.getName() + " ___document = null;");
    }

    getWriter().writeLine("try");
    getWriter().openBracket();
    if(!returnType.isVoid())
    {
      getWriter().writeLine("Object __output = " + facadeName + "." + methodName + "Base(" + callString + ");");
      getWriter().writeLine("___document = " + ConversionFacade.class.getName() + ".getDocumentFromObject(__output, true);");
      getWriter().writeLine("");
      getWriter().writeLine("return ___document;");
    }
    else
    {
      getWriter().writeLine(facadeName + "." + methodName + "Base(" + callString + ");");
    }
    getWriter().closeBracket();
    getWriter().writeLine("catch ("+RuntimeException.class.getName()+" e)");
    getWriter().openBracket();

    if(!returnType.isVoid())
    {
      getWriter().writeLine(WebServiceAdapter.class.getName()+".buildAxisFault(e, ___document);");
    }
    else
    {
      getWriter().writeLine(WebServiceAdapter.class.getName()+".buildAxisFault(e, null);");
    }
    getWriter().closeBracket();

    if(!returnType.isVoid())
    {
      getWriter().writeLine("return null;");
    }

    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  public static void generateAdapter(MdFacadeDAOIF mdFacadeIF)
  {
    WebServiceAdapterGenerator generator = new WebServiceAdapterGenerator(mdFacadeIF);
    generator.go(false);
  }

  public static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return "WebService" + mdFacade.getValue(MdFacadeInfo.NAME) + "Adapter";
  }
}
