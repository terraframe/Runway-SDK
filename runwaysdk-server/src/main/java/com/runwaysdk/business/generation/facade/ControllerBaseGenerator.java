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

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.runwaysdk.business.generation.AbstractClientGenerator;
import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.controller.ActionParameters;
import com.runwaysdk.dataaccess.MdActionDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.metadata.Type;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.Reloadable;

public class ControllerBaseGenerator extends AbstractClientGenerator implements ControllerGenerator
{
  public ControllerBaseGenerator(MdControllerDAOIF mdController)
  {
    super(mdController, getGeneratedName(mdController));
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
    writeConstructor();
    writeRender();
    writeGetters();
    writeControllerActions();
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
    return this.getSerialVersionUID("BASE", this.getSignature());
  }

  @Override
  protected MdControllerDAOIF getMdTypeDAOIF()
  {
    return (MdControllerDAOIF)super.getMdTypeDAOIF();
  }

  /**
   * Generate the package declaration
   */
  private void writePackage()
  {
    getWriter().writeLine( "package " + this.getMdTypeDAOIF().getPackage() + ";");
    getWriter().writeLine( "");
  }

  /**
   * Generates the class name declaration
   */
  private void writeClassName()
  {
    String typeName = this.getFileName();

    getWriter().write("public class " + typeName);

    if (!this.getMdTypeDAOIF().isSystemPackage())
    {
      getWriter().write(Reloadable.IMPLEMENTS);
    }
    getWriter().writeLine("");

    getWriter().openBracket();
  }

  private void writeFields()
  {
    String facadeName = this.getMdTypeDAOIF().definesType();
    String requestName = HttpServletRequest.class.getName();
    String responseName = HttpServletResponse.class.getName();
    String booleanName = Boolean.class.getName();
    String stringName = String.class.getName();

    getWriter().writeLine("public static final String CLASS = \"" + facadeName + "\";");
    getWriter().writeLine("protected " + requestName + " req;");
    getWriter().writeLine("protected " + responseName + " resp;");
    getWriter().writeLine("protected " + booleanName + " isAsynchronous;");
    getWriter().writeLine("protected " + stringName + " dir;");
    getWriter().writeLine("protected " + stringName + " layout;");
    getWriter().writeLine("");
  }

  private void writeConstructor()
  {
    String controllerName = this.getFileName();
    String requestName = HttpServletRequest.class.getName();
    String responseName = HttpServletResponse.class.getName();
    String booleanName = Boolean.class.getName();
    String stringName = String.class.getName();

    getWriter().writeLine("public " + controllerName + "(" + requestName + " req, " + responseName + " resp, " + booleanName + " isAsynchronous)");
    getWriter().openBracket();
    getWriter().writeLine("this(req, resp, isAsynchronous, \"\",\"\");");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public " + controllerName + "(" + requestName + " req, " + responseName + " resp, " + booleanName + " isAsynchronous, " + stringName + " dir, " + stringName + " layout)");
    getWriter().openBracket();
    getWriter().writeLine("this.req = req;");
    getWriter().writeLine("this.resp = resp;");
    getWriter().writeLine("this.isAsynchronous = isAsynchronous;");
    getWriter().writeLine("this.dir = dir;");
    getWriter().writeLine("this.layout = layout;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  private void writeGetters()
  {
    String requestName = HttpServletRequest.class.getName();
    String responseName = HttpServletResponse.class.getName();
    String clientRequest = ClientRequestIF.class.getName();
    String clientSession = ClientConstants.CLIENT_REQUEST_CLASS;
    String clientConstants = ClientConstants.class.getName();
    String booleanName = Boolean.class.getName();

    //Write request getter
    getWriter().writeLine("public " + requestName + " getRequest()");
    getWriter().openBracket();
    getWriter().writeLine("return this.req;");
    getWriter().closeBracket();
    getWriter().writeLine("");

    //Write response getter
    getWriter().writeLine("public " + responseName + " getResponse()");
    getWriter().openBracket();
    getWriter().writeLine("return this.resp;");
    getWriter().closeBracket();
    getWriter().writeLine("");

    //Write isAsynchronous getter
    getWriter().writeLine("public " + booleanName + " isAsynchronous()");
    getWriter().openBracket();
    getWriter().writeLine("return this.isAsynchronous;");
    getWriter().closeBracket();
    getWriter().writeLine("");

    //Write convience method to return the ClientRequest object
    getWriter().writeLine("public " + clientRequest + " getClientRequest()");
    getWriter().openBracket();
    getWriter().writeLine("return (" + clientRequest + ") req.getAttribute(" + clientConstants + ".CLIENTREQUEST);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public " + clientSession + " getClientSession()");
    getWriter().openBracket();
    getWriter().writeLine("return (" + clientSession + ") req.getSession().getAttribute(" + clientConstants + ".CLIENTSESSION);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  private void writeRender()
  {
    String throwsClause = " throws " + IOException.class.getName() + ", " + ServletException.class.getName();

    getWriter().writeLine("protected void render(String jsp)" + throwsClause);
    getWriter().openBracket();
    getWriter().writeLine("if(!resp.isCommitted())");
    getWriter().openBracket();
    getWriter().writeLine("if(this.isAsynchronous())");
    getWriter().openBracket();
    getWriter().writeLine("req.getRequestDispatcher(dir+jsp).forward(req, resp);");
    getWriter().closeBracket();
    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine("req.setAttribute(" + ClientConstants.JSP_FETCHER_CLASS + ".INNER_JSP, dir+jsp);");
    getWriter().writeLine("req.getRequestDispatcher(layout).forward(req, resp);");
    getWriter().closeBracket();
    getWriter().closeBracket();
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  private void writeControllerActions()
  {
    for(MdActionDAOIF mdAction : this.getMdTypeDAOIF().getMdActionDAOsOrdered())
    {
      writeAnnotation(mdAction);
      writeAction(mdAction);

      writeFailureAnnotation(mdAction);
      writeFailure(mdAction);
    }
  }


  private void writeAnnotation(MdActionDAOIF mdAction)
  {
    String annotationName = ActionParameters.class.getName();
    StringBuffer buffer = new StringBuffer();

    for(MdParameterDAOIF mdParameter : mdAction.getMdParameterDAOs())
    {
      buffer.append(", " + mdParameter.getParameterType().getDTOJavaClass() + ":" + mdParameter.getParameterName());
    }

    String parameters = "parameters=\"" + buffer.toString().replaceFirst(", ", "") + "\"";
    String post = "post=" + mdAction.getValue(MdActionInfo.IS_POST);
    getWriter().writeLine("@" + annotationName + "(" + parameters + ", " + post + ")");
  }

  private void writeAction(MdActionDAOIF mdAction)
  {
    String controllerName = this.getMdTypeDAOIF().definesType();
    String actionName = mdAction.getName();

    List<MdParameterDAOIF> list = mdAction.getMdParameterDAOs();
    String parameters = GenerationUtil.buildDTOParameters(list, true);
    String throwsClause = " throws " + IOException.class.getName() + ", " + ServletException.class.getName();

    getWriter().writeLine("public void " + actionName + "(" + parameters + ")" + throwsClause);
    getWriter().openBracket();
    getWriter().writeLine("String msg = \"This method should never be invoked.  It should be overwritten in " + this.getMdTypeDAOIF().definesType() + ".java\";");
    getWriter().writeLine("throw new " + ExceptionConstants.UndefinedControllerActionException.getExceptionClass() + "(msg, req.getLocale(), \"" + controllerName + "." + actionName + "\");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  private void writeFailureAnnotation(MdActionDAOIF mdAction)
  {
    String annotationName = ActionParameters.class.getName();
    StringBuffer buffer = new StringBuffer();

    for(MdParameterDAOIF mdParameter : mdAction.getMdParameterDAOs())
    {
      Type parameterType = mdParameter.getParameterType();

      if(parameterType.isPrimitive())
      {
        parameterType = new Type(String.class.getName(), parameterType.getDimensions());
      }

      buffer.append(", " + parameterType.getDTOJavaClass() + ":" + mdParameter.getParameterName());
    }

    String parameters = "parameters=\"" + buffer.toString().replaceFirst(", ", "") + "\"";
    String post = "post=" + mdAction.getValue(MdActionInfo.IS_POST);
    getWriter().writeLine("@" + annotationName + "(" + parameters + ", " + post + ")");
  }

  private void writeFailure(MdActionDAOIF mdAction)
  {
    String controllerName = this.getMdTypeDAOIF().definesType();
    String actionName = mdAction.getName();
    String methodName = "fail" + CommonGenerationUtil.upperFirstCharacter(actionName);

    List<MdParameterDAOIF> list = mdAction.getMdParameterDAOs();
    String throwsClause = " throws " + IOException.class.getName() + ", " + ServletException.class.getName();

    StringBuffer parameters = new StringBuffer();

    for(MdParameterDAOIF mdParameter : list)
    {
      Type parameterType = mdParameter.getParameterType();

      if(parameterType.isPrimitive())
      {
        parameterType = new Type(String.class.getName(), parameterType.getDimensions());
      }

      parameters.append(", " + parameterType.getDTOType() + " " + mdParameter.getParameterName());
    }


    getWriter().writeLine("public void " + methodName + "(" + parameters.toString().replaceFirst(", ", "") + ")" + throwsClause);
    getWriter().openBracket();
    getWriter().writeLine("String msg = \"This method should never be invoked.  It should be overwritten in " + this.getMdTypeDAOIF().definesType() + ".java\";");
    getWriter().writeLine("throw new " + ExceptionConstants.UndefinedControllerActionException.getExceptionClass() + "(msg, req.getLocale(), \"" + controllerName + "." + methodName + "\");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Return the name of the file, without the extension, that this class would generate
   * for a given MdController.
   *
   * @param mdController
   * @return
   */
  public static String getGeneratedName(MdControllerDAOIF mdController)
  {
    return mdController.getTypeName() + TypeGeneratorInfo.BASE_SUFFIX;
  }

  public String getClassAttribute()
  {
    return MdControllerInfo.BASE_CLASS;
  }

  public String getClassColumnName()
  {
    return MdControllerDAOIF.BASE_CLASS_COLUMN;
  }

  public String getSourceAttribute()
  {
    return MdControllerInfo.BASE_SOURCE;
  }

  public String getSourceColumnName()
  {
    return MdControllerDAOIF.BASE_SOURCE_COLUMN;
  }

  public String getClassFile()
  {
    String fileSystemPack = GenerationUtil.getPackageForFileSystem(this.getMdTypeDAOIF());
    return AbstractClientGenerator.getRootClientBinDirectory(fileSystemPack) + fileSystemPack + this.getFileName() + ".class";
  }
}
