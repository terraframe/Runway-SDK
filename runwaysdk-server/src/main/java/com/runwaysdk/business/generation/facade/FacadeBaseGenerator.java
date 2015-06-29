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
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.metadata.ForbiddenMethodException;
import com.runwaysdk.dataaccess.metadata.Type;
import com.runwaysdk.facade.FacadeUtil;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

/**
 * !IMPORTANT!
 * If you're changing the way base classes are generated then its probably
 * a good time to add a generation version number to the class signature. The
 * reason is because you must regenerate all the base classes of applications
 * that depend on runway (even though the metadata may not have changed).
 * If you don't regenerate these base classes, then the app can break at
 * runtime if the generated file is different than what it was copiled against.
 * See DDMS ticket #3298
 *  * !IMPORTANT!
 */
public class FacadeBaseGenerator extends AbstractServerGenerator
{

  private static final String DTO_HELPER = FacadeUtil.class.getName();
  private static final String CONVERT_DTO_TYPE = ".convertDTOToType";

  public FacadeBaseGenerator(MdFacadeDAOIF mdFacade)
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
    writeFacadeMethods();
    getWriter().closeBracket();
    getWriter().close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("BASE", this.getSignature());
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
    getWriter().writeLine( "package " + this.getMdTypeDAOIF().getPackage() + ";");
    getWriter().writeLine( "");
  }

  /**
   * Generates the class name declaration
   */
  private void writeClassName()
  {
    String typeName = this.getMdTypeDAOIF().getTypeName();

    getWriter().write("public class " + typeName + TypeGeneratorInfo.BASE_SUFFIX);
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
    getWriter().writeLine("public static final String CLASS = \"" + facadeName + "\";");
    getWriter().writeLine("");
  }


  private void writeFacadeMethods()
  {
    for(MdMethodDAOIF mdMethod : this.getMdTypeDAOIF().getMdMethodsOrdered())
    {
      writeBaseMethod(mdMethod);
      writeMethod(mdMethod);
    }
  }

  private void writeBaseMethod(MdMethodDAOIF mdMethod)
  {
    String methodName = mdMethod.getValue(MdMethodInfo.NAME);
    Type returnType = mdMethod.getReturnType();
    List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();
    String parameters = "String sessionId" + GenerationUtil.buildGenericDTOParameters(list, false);

    getWriter().writeLine("@" + Request.class.getName() + "("+RequestType.class.getName()+"."+RequestType.SESSION.name()+")");
    getWriter().writeLine("public static " + returnType.getGenericDTOType() + " " + methodName + "Base(" + parameters + ")");
    getWriter().openBracket();

    for(MdParameterDAOIF mdParameter : list)
    {
      Type parameterType = mdParameter.getParameterType();
      String name = mdParameter.getParameterName();
      String type = parameterType.getType();
      String java = parameterType.getJavaClass();

      //If the parameter is not a primitive then it needs to be converted into its
      //business layer representative
      if(!parameterType.isPrimitive())
      {
        getWriter().writeLine(type + " _" + name + " = " + "(" + type + ") " +
          DTO_HELPER + CONVERT_DTO_TYPE + "(sessionId, " +
          "\"" + java + "\", (Object) " + name + ");");
      }
      else
      {
        getWriter().writeLine(type + " _" + name + " = " + name + ";");
      }
    }

    if(list.size() > 0)
    {
      getWriter().writeLine("");
    }

    String callString = "sessionId" + GenerationUtil.buildCallParameter(list, "_", false);
    String methodCall = this.getMdTypeDAOIF().definesType() + "." + methodName + "(" + callString  + ");";

    if(!returnType.isVoid())
    {
      //If the return type is not a primitive it needs to be converted into a generic DTO
      if(!returnType.isPrimitive())
      {
        getWriter().writeLine(returnType.getType() + " _output = " + methodCall);
        getWriter().writeLine("return (" + returnType.getGenericDTOType() +") " + FacadeUtil.class.getName() + ".convertTypeToDTO(sessionId, _output);");
      }
      else
      {
        getWriter().writeLine("return " + methodCall);
      }
    }
    else
    {
      getWriter().writeLine(methodCall);
    }

    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Writes the stub methods the will need to be over written in the stub class
   * @param mdMethod
   */
  private void writeMethod(MdMethodDAOIF mdMethod)
  {
    String methodName = mdMethod.getValue(MdMethodInfo.NAME);
    String returnType = mdMethod.getValue(MdMethodInfo.RETURN_TYPE);
    List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();
    String parameters = "String sessionId" + GenerationUtil.buildBusinessParameters(list, false);

    getWriter().writeLine("protected static " + returnType + " " + methodName + "(" + parameters + ")");
    getWriter().openBracket();
    getWriter().writeLine("String msg = \"This method should never be invoked.  It should be overwritten in " + this.getMdTypeDAOIF().definesType() + ".java\";");
    getWriter().writeLine("throw new " + ForbiddenMethodException.class.getName() + "(msg);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  public static void generateFacade(MdFacadeDAOIF mdFacadeIF, boolean regenerateStub)
  {
    new FacadeBaseGenerator(mdFacadeIF).go(regenerateStub);

    if(regenerateStub)
    {
      new FacadeStubGenerator(mdFacadeIF).go(regenerateStub);
    }
  }

  /**
   * Return the name of the file, without the extension, that this class would generate
   * for a given MdFacade.
   *
   * @param mdFacade
   * @return
   */
  public static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return mdFacade.getTypeName() + TypeGeneratorInfo.BASE_SUFFIX;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.generation.AbstractGenerator#getSourceDirectory()
   */
  protected String getRootSourceDirectory()
  {
    return getRootServerBaseDirectory(this.getPackage());
  }
}
