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
package com.runwaysdk.business.generation.dto;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.generation.ClassStubGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.EntityDTOInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.dataaccess.metadata.Type;
import com.runwaysdk.generation.CommonGenerationUtil;

/**
 * The concrete implementation of the template pattern defined
 * by EntityDTOGenerator. Generates type safe BusinessDTO objects.
 *
 * @author Justin Smethie
 *
 */
public class BusinessDTOBaseGenerator extends ElementDTOBaseGenerator
{
  /**
   * @param mdEntity
   */
  public BusinessDTOBaseGenerator(MdBusinessDAOIF mdBusinessIF)
  {
    super(mdBusinessIF);
  }

  /**
   * The general algorithm used to generate the DTOs
   */
  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    // Do not generate a DTO if this class is not published.
    if (!this.getMdTypeDAOIF().isPublished())
    {
      return;
    }
    else
    {
      super.go(forceRegeneration);
    }
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.generation.DTOGenerator#write()
   */
  @Override
  protected void writeConcrete()
  {
    writeChildMethods();
    writeParentMethods();
    writeTransitions();
    writeGet();
  }

  /**
   * Write the static get method for the generated BusinessDTO
   */
  private void writeGet()
  {
    getWriter().writeLine("public static " + this.getDTOStubClassType() + " get(" + ClientRequestIF.class.getName() + " clientRequest, String id)");
    getWriter().openBracket();
    getWriter().writeLine(EntityDTOInfo.CLASS + " dto = ("+EntityDTOInfo.CLASS+")clientRequest.get(id);");
    getWriter().writeLine("");
    getWriter().writeLine("return (" + this.getDTOStubClassType() + ") dto;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  @Override
  protected void writeConstructor()
  {
    super.writeConstructor();

    getWriter().writeLine("/**");
    getWriter().writeLine("* Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.");
    getWriter().writeLine("* ");
    getWriter().writeLine("* @param businessDTO The BusinessDTO to duplicate");
    getWriter().writeLine("* @param clientRequest The clientRequest this DTO should use to communicate with the server.");
    getWriter().writeLine("*/");
    getWriter().writeLine("protected " + getFileName() + "(" + BusinessDTO.class.getName() + " businessDTO, " + ClientRequestIF.class.getName() + " clientRequest)");
    getWriter().openBracket();
    getWriter().writeLine("super(businessDTO, clientRequest);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the apply() method that creates the DTO
   * if it is a new instance or updates it if not.
   */
  protected void writeApply()
  {
    getWriter().writeLine("public void apply()");
    getWriter().openBracket();
    getWriter().writeLine("if(isNewInstance())");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().createBusiness(this);");
    getWriter().closeBracket();
    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().update(this);");
    getWriter().closeBracket();
    getWriter().closeBracket();
  }

  /**
   * Write all of the Children methods for all relationships which this
   * type is the parent participant.
   */
  private void writeChildMethods()
  {
    for (MdRelationshipDAOIF rel : this.getMdTypeDAOIF().getParentMdRelationshipsOrdered())
    {
      // do not generate the method if the child class is not published.
      if (!rel.isPublished() ||
          !rel.getChildMdBusiness().isPublished() ||
          !rel.getChildVisibility().equals(VisibilityModifier.PUBLIC))
      {
        continue;
      }

      if (GenerationUtil.isHardcodedType(rel.getChildMdBusiness()))
      {
        continue;
      }

      if (GenerationUtil.isStatus(this.getMdTypeDAOIF(), rel) || GenerationUtil.isStateMachine(rel))
      {
        continue;
      }

      writeGetAllChildren(rel);
      writeGetAllChildrenRelationships(rel);
      writeAddChild(rel);
      writeRemoveChild(rel);
      writeRemoveChildren(rel);
    }
  }

  /**
   * Write the getAllChildRelationships method for a given MdRelationship.
   *
   * @param mdRelationship
   */
  private void writeGetAllChildrenRelationships(MdRelationshipDAOIF mdRelationship)
  {
    String methodName = CommonGenerationUtil.upperFirstCharacter(mdRelationship.getChildMethod());
    String relationshipType = getDTOStubClassTypeHardcoded(mdRelationship);
    String relationshipName = mdRelationship.definesType();
    String relTypeClass = relationshipName+TypeGeneratorInfo.DTO_SUFFIX+".CLASS";

    getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
    getWriter().writeLine("public java.util.List<? extends " + relationshipType + "> getAll" + methodName + "Relationships()");
    getWriter().openBracket();
    getWriter().writeLine("return (java.util.List<? extends " + relationshipType + ">) getRequest().getChildRelationships(this.getId(), " + relTypeClass + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
    getWriter().writeLine("public static java.util.List<? extends " + relationshipType + "> getAll" + methodName + "Relationships("+ClientRequestIF.class.getName()+" clientRequestIF, String id)");
    getWriter().openBracket();
    getWriter().writeLine("return (java.util.List<? extends " + relationshipType + ">) clientRequestIF.getChildRelationships(id, " + relTypeClass + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Writes the getAllChildren method for a given MdRelationship
   *
   * @param mdRelationship
   */
  private void writeGetAllChildren(MdRelationshipDAOIF mdRelationship)
  {
    String methodName = CommonGenerationUtil.upperFirstCharacter(mdRelationship.getChildMethod());
    String childType = getDTOStubClassTypeHardcoded(mdRelationship.getChildMdBusiness());
    String relationshipName = mdRelationship.definesType();
    String relTypeClass = relationshipName+TypeGeneratorInfo.DTO_SUFFIX+".CLASS";

    getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
    getWriter().writeLine("public java.util.List<? extends " + childType + "> getAll" + methodName + "()");
    getWriter().openBracket();
    getWriter().writeLine("return (java.util.List<? extends " + childType + ">) getRequest().getChildren(this.getId(), " + relTypeClass + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
    getWriter().writeLine("public static java.util.List<? extends " + childType + "> getAll" + methodName + "("+ClientRequestIF.class.getName()+" clientRequestIF, String id)");
    getWriter().openBracket();
    getWriter().writeLine("return (java.util.List<? extends " + childType + ">) clientRequestIF.getChildren(id, " + relTypeClass + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");

  }

  /**
   * Write the addChild method for a given MdRelationship.
   * @param mdRelationship
   */
  private void writeAddChild(MdRelationshipDAOIF mdRelationship)
  {
    MdBusinessDAOIF mdChild = mdRelationship.getChildMdBusiness();
    String childType = EntityDTOBaseGenerator.getDTOStubClassTypeHardcoded(mdChild);
    String reltionshipDTOType = EntityDTOBaseGenerator.getDTOStubClassTypeHardcoded(mdRelationship);
    String relationshipName = mdRelationship.definesType();
    String relTypeClass = relationshipName+TypeGeneratorInfo.DTO_SUFFIX+".CLASS";
    String methodName = "add" + CommonGenerationUtil.upperFirstCharacter(mdRelationship.getChildMethod());

    getWriter().writeLine("public " + reltionshipDTOType + " " + methodName + "(" + childType + " child)");
    getWriter().openBracket();
    getWriter().writeLine("return ("+reltionshipDTOType+") getRequest().addChild(this.getId(), child.getId(), "+relTypeClass+");");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public static " + reltionshipDTOType + " " + methodName + "("+ClientRequestIF.class.getName()+" clientRequestIF, String id, " + childType + " child)");
    getWriter().openBracket();
    getWriter().writeLine("return ("+reltionshipDTOType+") clientRequestIF.addChild(id, child.getId(), "+relTypeClass+");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the removeChild method for a given MdRelationship
   *
   * @param mdRelationship
   */
  private void writeRemoveChild(MdRelationshipDAOIF mdRelationship)
  {
    String methodName = "remove" + CommonGenerationUtil.upperFirstCharacter(mdRelationship.getChildMethod());
    String relationshipName = getDTOStubClassTypeHardcoded(mdRelationship);

    getWriter().writeLine("public void " + methodName + "(" + relationshipName + " relationship)");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().deleteChild(relationship.getId());");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public static void " + methodName + "("+ClientRequestIF.class.getName()+" clientRequestIF, " + relationshipName + " relationship)");
    getWriter().openBracket();
    getWriter().writeLine("clientRequestIF.deleteChild(relationship.getId());");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  private void writeRemoveChildren(MdRelationshipDAOIF mdRelationship)
  {
    String methodName = "removeAll" + CommonGenerationUtil.upperFirstCharacter(mdRelationship.getChildMethod());
    String relationshipType = mdRelationship.definesType();
    String relTypeClass = relationshipType+TypeGeneratorInfo.DTO_SUFFIX+".CLASS";

    getWriter().writeLine("public void " + methodName + "()");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().deleteChildren(this.getId(), " + relTypeClass + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public static void " + methodName + "("+ClientRequestIF.class.getName()+" clientRequestIF, String id)");
    getWriter().openBracket();
    getWriter().writeLine("clientRequestIF.deleteChildren(id, " + relTypeClass + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write all of the Parent methods for all relationships which this
   * type is the child participant.
   */
  private void writeParentMethods()
  {
    for (MdRelationshipDAOIF rel : this.getMdTypeDAOIF().getChildMdRelationshipsOrdered())
    {
      // do not generate the method if the parent class is not published.
      if (!rel.isPublished() ||
          !rel.getParentMdBusiness().isPublished() ||
          !rel.getParentVisibility().equals(VisibilityModifier.PUBLIC))
      {
        continue;
      }

      if (GenerationUtil.isHardcodedType(rel.getParentMdBusiness()))
      {
        continue;
      }

      if (GenerationUtil.isStatus(this.getMdTypeDAOIF(), rel) || GenerationUtil.isStateMachine(rel)) continue;

      writeGetAllParent(rel);
      writeGetAllParentRelationships(rel);
      writeAddParent(rel);
      writeRemoveParent(rel);
      writeRemoveParents(rel);
    }
  }

  /**
   * Write the getAllParentRelationships method for a given MdRelationship.
   *
   * @param mdRelationship
   */
  private void writeGetAllParentRelationships(MdRelationshipDAOIF mdRelationship)
  {
    String methodName = CommonGenerationUtil.upperFirstCharacter(mdRelationship.getParentMethod());
    String relationshipType = getDTOStubClassTypeHardcoded(mdRelationship);
    String relationshipName = mdRelationship.definesType();
    String relTypeClass = relationshipName+TypeGeneratorInfo.DTO_SUFFIX+".CLASS";

    getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
    getWriter().writeLine("public java.util.List<? extends " + relationshipType + "> getAll" + methodName + "Relationships()");
    getWriter().openBracket();
    getWriter().writeLine("return (java.util.List<? extends " + relationshipType + ">) getRequest().getParentRelationships(this.getId(), " + relTypeClass + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
    getWriter().writeLine("public static java.util.List<? extends " + relationshipType + "> getAll" + methodName + "Relationships("+ClientRequestIF.class.getName()+" clientRequestIF, String id)");
    getWriter().openBracket();
    getWriter().writeLine("return (java.util.List<? extends " + relationshipType + ">) clientRequestIF.getParentRelationships(id, " + relTypeClass + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Writes the getAllParent method for a given MdRelationship.
   *
   * @param mdRelationship
   */
  private void writeGetAllParent(MdRelationshipDAOIF mdRelationship)
  {
    String methodName = CommonGenerationUtil.upperFirstCharacter(mdRelationship.getParentMethod());
    String parentType = getDTOStubClassTypeHardcoded(mdRelationship.getParentMdBusiness());
    String relationshipName = mdRelationship.definesType();
    String relTypeClass = relationshipName+TypeGeneratorInfo.DTO_SUFFIX+".CLASS";

    getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
    getWriter().writeLine("public java.util.List<? extends " + parentType + "> getAll" + methodName + "()");
    getWriter().openBracket();
    getWriter().writeLine("return (java.util.List<? extends " + parentType + ">) getRequest().getParents(this.getId(), " + relTypeClass + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
    getWriter().writeLine("public static java.util.List<? extends " + parentType + "> getAll" + methodName + "("+ClientRequestIF.class.getName()+" clientRequestIF, String id)");
    getWriter().openBracket();
    getWriter().writeLine("return (java.util.List<? extends " + parentType + ">) clientRequestIF.getParents(id, " + relTypeClass + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the addParent method for a given MdRelationship.
   * @param mdRelationship
   */
  private void writeAddParent(MdRelationshipDAOIF mdRelationship)
  {
    MdBusinessDAOIF mdParent = mdRelationship.getParentMdBusiness();
    String parentType = EntityDTOBaseGenerator.getDTOStubClassTypeHardcoded(mdParent);
    String reltionshipDTOType = EntityDTOBaseGenerator.getDTOStubClassTypeHardcoded(mdRelationship);
    String relationshipName = mdRelationship.definesType();
    String relTypeClass = relationshipName+TypeGeneratorInfo.DTO_SUFFIX+".CLASS";
    String methodName = "add" + CommonGenerationUtil.upperFirstCharacter(mdRelationship.getParentMethod());

    getWriter().writeLine("public " + reltionshipDTOType + " " + methodName + "(" + parentType + " parent)");
    getWriter().openBracket();
    getWriter().writeLine("return ("+reltionshipDTOType+") getRequest().addParent(parent.getId(), this.getId(), "+relTypeClass+");");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public static " + reltionshipDTOType + " " + methodName + "("+ClientRequestIF.class.getName()+" clientRequestIF, String id, " + parentType + " parent)");
    getWriter().openBracket();
    getWriter().writeLine("return ("+reltionshipDTOType+") clientRequestIF.addParent(parent.getId(), id, "+relTypeClass+");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the removeChild method for a given MdRelationship.
   * @param mdRelationship
   */
  private void writeRemoveParent(MdRelationshipDAOIF mdRelationship)
  {
    String methodName = "remove" + CommonGenerationUtil.upperFirstCharacter(mdRelationship.getParentMethod());
    String relationshipType = getDTOStubClassTypeHardcoded(mdRelationship);

    getWriter().writeLine("public void " + methodName + "(" + relationshipType + " relationship)");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().deleteParent(relationship.getId());");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public static void " + methodName + "("+ClientRequestIF.class.getName()+" clientRequestIF, " + relationshipType + " relationship)");
    getWriter().openBracket();
    getWriter().writeLine("clientRequestIF.deleteParent(relationship.getId());");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  private void writeRemoveParents(MdRelationshipDAOIF mdRelationship)
  {
    String methodName = "removeAll" + CommonGenerationUtil.upperFirstCharacter(mdRelationship.getParentMethod());
    String relationshipName = mdRelationship.definesType();
    String relTypeClass = relationshipName+TypeGeneratorInfo.DTO_SUFFIX+".CLASS";

    getWriter().writeLine("public void " + methodName + "()");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().deleteParents(this.getId(), " + relTypeClass + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public static void " + methodName + "("+ClientRequestIF.class.getName()+" clientRequestIF, String id)");
    getWriter().openBracket();
    getWriter().writeLine("clientRequestIF.deleteParents(id, " + relTypeClass + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write all of the Transition method for this type
   */
  private void writeTransitions()
  {
    if (!this.getMdTypeDAOIF().hasStateMachine()) return;

    for (TransitionDAOIF transistion : this.getMdTypeDAOIF().definesMdStateMachine().definesTransitions())
    {
      String methodName = CommonGenerationUtil.lowerFirstCharacter(transistion.getName());
      getWriter().writeLine("public void " + methodName + "()");
      getWriter().openBracket();
      getWriter().writeLine("getRequest().promoteObject(this, \"" + transistion.getName() + "\");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      List<MdParameterDAOIF> list = new LinkedList<MdParameterDAOIF>();
      list.add(GenerationUtil.getMdParameterId());

      writeMdMethod(this.getDTOStubClassType()+".CLASS", list, methodName, new Type(ClassStubGenerator.getGeneratedType(this.getMdTypeDAOIF())), true, true);
    }
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.generation.DTOGenerator#getExtends(MdClassDAOIF)
   */
  @Override
  protected String getExtends(MdClassDAOIF parent)
  {
    if (parent == null)
    {
      return BusinessDTO.class.getName();
    }
    else
    {
      return getParentClass(parent);
    }
  }

  @Override
  protected MdBusinessDAOIF getMdTypeDAOIF()
  {
    return (MdBusinessDAOIF) super.getMdTypeDAOIF();
  }

}
