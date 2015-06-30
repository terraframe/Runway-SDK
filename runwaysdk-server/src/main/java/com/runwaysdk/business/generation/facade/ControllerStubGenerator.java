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

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.business.generation.AbstractClientGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.view.NewRelationshipComponentListener;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.GeneratedActions;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdActionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;
import com.runwaysdk.dataaccess.metadata.Type;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.Reloadable;

public class ControllerStubGenerator extends AbstractClientGenerator implements ControllerStubGeneratorIF
{
  public ControllerStubGenerator(MdControllerDAOIF mdController)
  {
    super(mdController, getGeneratedName(mdController));
  }

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business
    // classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    if (forceRegeneration)
    {
      generate();
      return;
    }

    // This cast is OK, as the mdController is not modified here, just read.
    AttributeIF stubSource = ( (MdControllerDAO) this.getMdTypeDAOIF() ).getAttributeIF(MdControllerInfo.STUB_SOURCE);
    boolean empty = stubSource.getValue().trim().equals("");

    // If the database contains new source, just write that to the file system
    if (stubSource.isModified() && !empty)
    {
      getWriter().write(this.getMdTypeDAOIF().getAttributeIF(MdControllerInfo.STUB_SOURCE).getValue());
      getWriter().close();
      return;
    }

    // If we're keeping existing stub source, and a file exists, leave it
    if (LocalProperties.isKeepSource() && new File(getPath()).exists())
      return;

    if (this.getMdTypeDAOIF().isNew() || empty)
    {
      generate();
      return;
    }
  }

  @Override
  protected MdControllerDAOIF getMdTypeDAOIF()
  {
    return (MdControllerDAOIF) super.getMdTypeDAOIF();
  }

  protected void generate()
  {
    writePackage();
    writeClassName();
    writeFields();
    writeConstructor();

    if (this.getMdTypeDAOIF().getMdEntity() != null)
    {
      writeControllerActions();
    }

    getWriter().closeBracket();
    getWriter().close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("STUB", this.getSignature());
  }

  protected void writePackage()
  {
    getWriter().writeLine("package " + this.getMdTypeDAOIF().getPackage() + ";");
    getWriter().writeLine("");
  }

  /**
   * Generates the class name declaration
   */
  protected void writeClassName()
  {
    String typeName = ControllerStubGenerator.getGeneratedName(this.getMdTypeDAOIF());
    String baseName = ControllerBaseGenerator.getGeneratedName(this.getMdTypeDAOIF());

    getWriter().write("public class " + typeName + " extends " + baseName);
    if (!this.getMdTypeDAOIF().isSystemPackage())
    {
      getWriter().write(Reloadable.IMPLEMENTS);
    }
    getWriter().writeLine("");

    getWriter().openBracket();
  }

  protected void writeFields()
  {
    MdEntityDAOIF mdEntity = this.getMdTypeDAOIF().getMdEntity();

    if (mdEntity != null)
    {
      String jsp = CommonGenerationUtil.replacePackageDotsWithSlashes(mdEntity.definesType());

      getWriter().writeLine("public static final String JSP_DIR = \"/WEB-INF/" + jsp + "\";");
      getWriter().writeLine("public static final String LAYOUT = \"WEB-INF/templates/layout.jsp\";");
      getWriter().writeLine("");
    }
  }

  protected void writeConstructor()
  {
    MdEntityDAOIF mdEntity = this.getMdTypeDAOIF().getMdEntity();
    String controllerName = this.getFileName();
    String requestName = HttpServletRequest.class.getName();
    String responseName = HttpServletResponse.class.getName();
    String booleanName = Boolean.class.getName();

    getWriter().writeLine("public " + controllerName + "(" + requestName + " req, " + responseName + " resp, " + booleanName + " isAsynchronous)");
    getWriter().openBracket();

    if (mdEntity != null)
    {
      getWriter().writeLine("super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);");
    }
    else
    {
      getWriter().writeLine("super(req, resp, isAsynchronous);");
    }

    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  protected void writeControllerActions()
  {
    for (MdActionDAOIF mdAction : this.getMdTypeDAOIF().getMdActionDAOsOrdered())
    {
      writeAction(mdAction);

      writeFailure(mdAction);
    }
  }

  protected void writeAction(MdActionDAOIF mdAction)
  {
    MdEntityDAOIF mdEntity = this.getMdTypeDAOIF().getMdEntity();
    String actionName = mdAction.getName();
    List<MdParameterDAOIF> list = mdAction.getMdParameterDAOs();
    String parameters = GenerationUtil.buildDTOParameters(list, true);
    String throwsClause = " throws " + IOException.class.getName() + ", " + ServletException.class.getName();

    getWriter().writeLine("public void " + actionName + "(" + parameters + ")" + throwsClause);
    getWriter().openBracket();

    StringBuffer buffer = new StringBuffer();
    for (MdParameterDAOIF mdParameter : list)
    {
      buffer.append(", " + mdParameter.getParameterName());
    }
    String arguments = buffer.toString().replaceFirst(", ", "");

    if (actionName.equals(GeneratedActions.CANCEL_ACTION.getName()))
    {
      writeCancelAction(mdEntity);
    }
    else if (actionName.equals(GeneratedActions.VIEW_ACTION.getName()))
    {
      writeViewAction(mdEntity);
    }
    else if (actionName.equals(GeneratedActions.VIEW_PAGE_ACTION.getName()))
    {
      writeViewPageAction(mdEntity);
    }
    else if (actionName.equals(GeneratedActions.VIEW_ALL_ACTION.getName()))
    {
      writeViewAllAction(mdEntity);
    }
    else if (actionName.equals(GeneratedActions.NEW_INSTANCE_ACTION.getName()))
    {
      writeNewInstanceAction(mdEntity);
    }
    else if (actionName.equals(GeneratedActions.CREATE_ACTION.getName()))
    {
      writeCreateAction(arguments);
    }
    else if (actionName.equals(GeneratedActions.UPDATE_ACTION.getName()))
    {
      writeUpdateAction(arguments);
    }
    else if (actionName.equals(GeneratedActions.EDIT_ACTION.getName()))
    {
      writeEditAction(mdEntity);
    }
    else if (actionName.equals(GeneratedActions.DELETE_ACTION.getName()))
    {
      writeDeleteAction(arguments);
    }
    else if (actionName.equals(GeneratedActions.NEW_RELATIONSHIP_ACTION.getName()))
    {
      writeNewRelationshipAction(mdEntity);
    }
    else if (actionName.equals(GeneratedActions.PARENT_QUERY_ACTION.getName()))
    {
      writeParentQueryAction(mdEntity);
    }
    else if (actionName.equals(GeneratedActions.CHILD_QUERY_ACTION.getName()))
    {
      writeChildQueryAction(mdEntity);
    }

    getWriter().closeBracket();
  }

  protected void writeRender(String title, String page)
  {
    getWriter().writeLine("render(\"" + page + "\");");
  }

  protected void writeNewRelationshipAction(MdEntityDAOIF mdEntity)
  {
    if (mdEntity instanceof MdRelationshipDAOIF)
    {
      MdRelationshipDAOIF mdRelationship = (MdRelationshipDAOIF) mdEntity;

      // Write the parent reference list
      MdBusinessDAOIF parent = mdRelationship.getParentMdBusiness();
      String parentInstances = parent.definesType() + TypeGeneratorInfo.DTO_SUFFIX + ".getAllInstances" + "(super.getClientSession().getRequest(), \"keyName\", true, 0, 0).getResultSet()";
      getWriter().writeLine("req.setAttribute(\"" + NewRelationshipComponentListener.PARENT_ATTRIBUTE + "\", " + parentInstances + ");");

      // Write the child reference list
      MdBusinessDAOIF child = mdRelationship.getChildMdBusiness();
      String childInstances = child.definesType() + TypeGeneratorInfo.DTO_SUFFIX + ".getAllInstances" + "(super.getClientSession().getRequest(), \"keyName\", true, 0, 0).getResultSet()";
      getWriter().writeLine("req.setAttribute(\"" + NewRelationshipComponentListener.CHILD_ATTRIBUTE + "\", " + childInstances + ");");

      writeRender("Parent/Child Selection", "newRelationshipComponent.jsp");
    }
  }

  protected void writeParentQueryAction(MdEntityDAOIF mdEntity)
  {
    if (mdEntity instanceof MdRelationshipDAOIF)
    {
      String clientRequest = ClientRequestIF.class.getName();
      String queryType = mdEntity.definesType() + "QueryDTO";
      String dtoType = mdEntity.definesType() + TypeGeneratorInfo.DTO_SUFFIX;

      getWriter().writeLine(clientRequest + " clientRequest = super.getClientRequest();");
      getWriter().writeLine(queryType + " query = " + dtoType + ".parentQuery(clientRequest, parentId);");
      getWriter().writeLine("req.setAttribute(\"query\", query);");

      writeRender("View All " + mdEntity.getTypeName() + " Objects", "viewAllComponent.jsp");
    }
  }

  protected void writeChildQueryAction(MdEntityDAOIF mdEntity)
  {
    if (mdEntity instanceof MdRelationshipDAOIF)
    {
      String clientRequest = ClientRequestIF.class.getName();
      String queryType = mdEntity.definesType() + "QueryDTO";
      String dtoType = mdEntity.definesType() + TypeGeneratorInfo.DTO_SUFFIX;

      getWriter().writeLine(clientRequest + " clientRequest = super.getClientRequest();");
      getWriter().writeLine(queryType + " query = " + dtoType + ".childQuery(clientRequest, childId);");
      getWriter().writeLine("req.setAttribute(\"query\", query);");

      writeRender("View All " + mdEntity.getTypeName() + " Objects", "viewAllComponent.jsp");
    }
  }

  protected void writeDeleteAction(String args)
  {
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine("dto.delete();");
    getWriter().writeLine("this.viewAll();");
    getWriter().closeBracket();
    getWriter().writeLine("catch(" + ProblemExceptionDTO.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("this.failDelete(" + args + ");");
    getWriter().closeBracket();

  }

  protected void writeCreateAction(String args)
  {
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine("dto.apply();");
    getWriter().writeLine("this.view(dto.getId());");
    getWriter().closeBracket();
    getWriter().writeLine("catch(" + ProblemExceptionDTO.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("this.failCreate(" + args + ");");
    getWriter().closeBracket();
  }

  protected void writeUpdateAction(String args)
  {
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine("dto.apply();");
    getWriter().writeLine("this.view(dto.getId());");
    getWriter().closeBracket();
    getWriter().writeLine("catch(" + ProblemExceptionDTO.class.getName() + " e)");
    getWriter().openBracket();
    getWriter().writeLine("this.failUpdate(" + args + ");");
    getWriter().closeBracket();
  }

  protected void writeViewAllAction(MdEntityDAOIF mdEntity)
  {
    String clientRequest = ClientRequestIF.class.getName();
    String queryType = mdEntity.definesType() + "QueryDTO";
    String dtoType = mdEntity.definesType() + TypeGeneratorInfo.DTO_SUFFIX;

    getWriter().writeLine(clientRequest + " clientRequest = super.getClientRequest();");
    getWriter().writeLine(queryType + " query = " + dtoType + ".getAllInstances(clientRequest, null, true, 20, 1);");
    getWriter().writeLine("req.setAttribute(\"query\", query);");

    writeRender("View All " + mdEntity.getTypeName() + " Objects", "viewAllComponent.jsp");
  }

  protected void writeViewPageAction(MdEntityDAOIF mdEntity)
  {
    String clientRequest = ClientRequestIF.class.getName();
    String queryType = mdEntity.definesType() + "QueryDTO";
    String dtoType = mdEntity.definesType() + TypeGeneratorInfo.DTO_SUFFIX;

    getWriter().writeLine(clientRequest + " clientRequest = super.getClientRequest();");
    getWriter().writeLine(queryType + " query = " + dtoType + ".getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);");
    getWriter().writeLine("req.setAttribute(\"query\", query);");

    writeRender("View All " + mdEntity.getTypeName() + " Objects", "viewAllComponent.jsp");
  }

  protected void writeViewAction(MdEntityDAOIF mdEntity)
  {
    String clientRequest = ClientRequestIF.class.getName();

    getWriter().writeLine(clientRequest + " clientRequest = super.getClientRequest();");

    // Load options for Enumeration and Reference attributes
    this.generateRequestsForReferencesAndEnumerations(mdEntity);

    getWriter().writeLine("req.setAttribute(\"item\", " + mdEntity.definesType() + "DTO.get(clientRequest, id));");

    writeRender("View " + mdEntity.getTypeName(), "viewComponent.jsp");
  }

  protected void writeCancelAction(MdEntityDAOIF mdEntity)
  {
    if (! ( mdEntity instanceof MdStructDAOIF ))
    {
      getWriter().writeLine("dto.unlock();");
    }

    getWriter().writeLine("this.view(dto.getId());");
  }

  protected void writeEditAction(MdEntityDAOIF mdEntity)
  {
    String type = mdEntity.definesType() + TypeGeneratorInfo.DTO_SUFFIX;

    if (! ( mdEntity instanceof MdStructDAOIF ))
    {
      getWriter().writeLine(type + " dto = " + type + ".lock(super.getClientRequest(), id);");
    }
    else
    {
      getWriter().writeLine(type + " dto = " + type + ".get(super.getClientRequest(), id);");
    }

    // Load options for Enumeration and Reference attributes
    generateRequestsForReferencesAndEnumerations(mdEntity);

    getWriter().writeLine("req.setAttribute(\"item\", dto);");

    writeRender("Edit " + mdEntity.getTypeName(), "editComponent.jsp");
  }

  protected void generateRequestsForReferencesAndEnumerations(MdEntityDAOIF mdEntity)
  {
    for (MdEntityDAOIF entity : mdEntity.getSuperClasses())
    {
      for (MdAttributeDAOIF mdAttribute : entity.definesAttributesOrdered())
      {
        String attriubteName = mdAttribute.definesAttribute();

        if (!mdAttribute.isSystem() && mdAttribute instanceof MdAttributeEnumerationDAOIF)
        {
          generateEnumerationList(mdAttribute);
        }
        else
        {
          if (!mdAttribute.isSystem() && mdAttribute instanceof MdAttributeReferenceDAOIF && !attriubteName.equals(ElementInfo.OWNER) && !attriubteName.equals(ElementInfo.DOMAIN))
          {
            generateReferenceList(mdAttribute);
          }
        }
      }
    }
  }

  protected void generateReferenceList(MdAttributeDAOIF mdAttribute)
  {
    MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) mdAttribute;
    MdBusinessDAOIF mdBusiness = mdAttributeReference.getReferenceMdBusinessDAO();
    
    // If the referenced MdBusinessDAO is not published it is impossible to
    // populate
    if (mdBusiness.isPublished())
    {
      String dtoType = mdBusiness.definesType() + TypeGeneratorInfo.DTO_SUFFIX;
      String methodName = "getAllInstances";
      String allInstances = dtoType + "." + methodName + "(super.getClientSession().getRequest(), \"keyName\", true, 0, 0).getResultSet()";

      getWriter().writeLine("req.setAttribute(\"_" + mdAttribute.definesAttribute() + "\", " + allInstances + ");");
    }

  }

  protected void generateEnumerationList(MdAttributeDAOIF mdAttribute)
  {
    MdEnumerationDAOIF mdEnumeration = ( (MdAttributeEnumerationDAOIF) mdAttribute ).getMdEnumerationDAO();

    String allInstances = mdEnumeration.definesType() + TypeGeneratorInfo.DTO_SUFFIX + ".allItems(super.getClientSession().getRequest())";
    getWriter().writeLine("req.setAttribute(\"_" + mdAttribute.definesAttribute() + "\", " + allInstances + ");");
  }

  protected void writeNewInstanceAction(MdEntityDAOIF mdEntity)
  {
    String clientRequest = ClientRequestIF.class.getName();
    String dtoType = mdEntity.definesType() + TypeGeneratorInfo.DTO_SUFFIX;

    getWriter().writeLine(clientRequest + " clientRequest = super.getClientRequest();");

    if (mdEntity instanceof MdRelationshipDAOIF)
    {
      getWriter().writeLine(dtoType + " dto = new " + dtoType + "(clientRequest, parentId, childId);");
    }
    else
    {
      getWriter().writeLine(dtoType + " dto = new " + dtoType + "(clientRequest);");
    }

    // Load options for Enumeration and Reference attributes
    this.generateRequestsForReferencesAndEnumerations(mdEntity);

    getWriter().writeLine("req.setAttribute(\"item\", dto);");

    writeRender("Create " + mdEntity.getTypeName(), "createComponent.jsp");
    // this.writeForward(component, jsp);
  }

  protected void writeFailure(MdActionDAOIF mdAction)
  {
    MdEntityDAOIF mdEntity = this.getMdTypeDAOIF().getMdEntity();
    String actionName = mdAction.getName();
    List<MdParameterDAOIF> list = mdAction.getMdParameterDAOs();
    String throwsClause = " throws " + IOException.class.getName() + ", " + ServletException.class.getName();
    StringBuffer parameters = new StringBuffer();

    for (MdParameterDAOIF mdParameter : list)
    {
      Type parameterType = mdParameter.getParameterType();

      if (parameterType.isPrimitive())
      {
        parameterType = new Type(String.class.getName(), parameterType.getDimensions());
      }

      parameters.append(", " + parameterType.getDTOType() + " " + mdParameter.getParameterName());
    }

    getWriter().writeLine("public void fail" + CommonGenerationUtil.upperFirstCharacter(actionName) + "(" + parameters.toString().replaceFirst(", ", "") + ")" + throwsClause);
    getWriter().openBracket();

    if (actionName.equals(GeneratedActions.VIEW_ACTION.getName()))
    {
      writeViewAndNewInstanceFail();
    }
    else if (actionName.equals(GeneratedActions.NEW_INSTANCE_ACTION.getName()))
    {
      writeViewAndNewInstanceFail();
    }
    else if (actionName.equals(GeneratedActions.EDIT_ACTION.getName()))
    {
      writeEditFail(mdEntity);
    }
    else if (actionName.equals(GeneratedActions.UPDATE_ACTION.getName()))
    {
      writeUpdateFail(mdEntity);
    }
    else if (actionName.equals(GeneratedActions.CREATE_ACTION.getName()))
    {
      writeCreateFail(mdEntity);
    }
    else if (actionName.equals(GeneratedActions.DELETE_ACTION.getName()))
    {
      writeDeleteFail(mdEntity);
    }
    else if (actionName.equals(GeneratedActions.CANCEL_ACTION.getName()))
    {
      writeCancelFail(mdEntity);
    }
    else
    {
      getWriter().writeLine("resp.sendError(500);");
    }

    getWriter().closeBracket();
  }

  protected void writeCancelFail(MdEntityDAOIF mdEntity)
  {
    getWriter().writeLine("this.edit(dto.getId());");
  }

  protected void writeCreateFail(MdEntityDAOIF mdEntity)
  {
    // Load options for Enumeration and Reference attributes
    this.generateRequestsForReferencesAndEnumerations(mdEntity);

    getWriter().writeLine("req.setAttribute(\"item\", dto);");

    // this.writeForward(component, jsp);
    writeRender("Create " + mdEntity.getTypeName(), "createComponent.jsp");
  }

  protected void writeUpdateFail(MdEntityDAOIF mdEntity)
  {
    // Load options for Enumeration and Reference attributes
    generateRequestsForReferencesAndEnumerations(mdEntity);

    getWriter().writeLine("req.setAttribute(\"item\", dto);");

    // this.writeForward(component, jsp);
    writeRender("Update " + mdEntity.getTypeName(), "editComponent.jsp");
  }

  protected void writeEditFail(MdEntityDAOIF mdEntity)
  {
    getWriter().writeLine("this.view(id);");
  }

  protected void writeDeleteFail(MdEntityDAOIF mdEntity)
  {
    // Load options for Enumeration and Reference attributes
    generateRequestsForReferencesAndEnumerations(mdEntity);

    getWriter().writeLine("req.setAttribute(\"item\", dto);");

    // this.writeForward(component, jsp);
    writeRender("Edit " + mdEntity.getTypeName(), "editComponent.jsp");
  }

  protected void writeViewAndNewInstanceFail()
  {
    // When failing while trying to view redirect to the view all page
    getWriter().writeLine("this.viewAll();");
  }

  @Override
  protected String getRootSourceDirectory()
  {
    return AbstractClientGenerator.getRootClientStubDirectory(this.getMdTypeDAOIF());
  }

  static String getGeneratedName(MdControllerDAOIF mdController)
  {
    return mdController.getTypeName();
  }

  public String getClassAttribute()
  {
    return MdControllerInfo.STUB_CLASS;
  }

  public String getClassColumnName()
  {
    return MdControllerDAOIF.STUB_CLASS_COLUMN;
  }

  public String getSourceAttribute()
  {
    return MdControllerInfo.STUB_SOURCE;
  }

  public String getSourceColumnName()
  {
    return MdControllerDAOIF.STUB_SOURCE_COLUMN;
  }

  public String getClassFile()
  {
    String fileSystemPack = GenerationUtil.getPackageForFileSystem(this.getMdTypeDAOIF());
    return AbstractClientGenerator.getRootClientBinDirectory(fileSystemPack) + fileSystemPack + this.getFileName() + ".class";
  }
}
