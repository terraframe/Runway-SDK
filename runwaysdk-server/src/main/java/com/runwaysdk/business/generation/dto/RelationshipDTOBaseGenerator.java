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
package com.runwaysdk.business.generation.dto;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.QueryConditions;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;

/**
 * The concrete implementation of the template pattern, defined by
 * EntityDTOGenerator. Generates type safe RelationshipDTO objects.
 * 
 * @author Justin Smethie
 * 
 */
public class RelationshipDTOBaseGenerator extends ElementDTOBaseGenerator
{
  /**
   * The type of the parent DTO
   */
  private String          parentDTO;

  /**
   * The type of the child DTO
   */
  private String          childDTO;

  private MdBusinessDAOIF parentMdBuisnessIF;

  private MdBusinessDAOIF childMdBuisnessIF;

  /**
   * @param mdType
   */
  public RelationshipDTOBaseGenerator(MdRelationshipDAOIF mdRelationship)
  {
    super(mdRelationship);

    this.parentMdBuisnessIF = mdRelationship.getParentMdBusiness();
    this.childMdBuisnessIF = mdRelationship.getChildMdBusiness();

    this.parentDTO = EntityDTOBaseGenerator.getDTOStubClassTypeHardcoded(parentMdBuisnessIF);
    this.childDTO = EntityDTOBaseGenerator.getDTOStubClassTypeHardcoded(childMdBuisnessIF);

  }

  /**
   * The general algorithm used to generate the DTOs
   */
  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business
    // classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    // Do not generate a DTO if this class or one of the parent or child classes
    // is not published.
    if (!this.getMdTypeDAOIF().isPublished() || !this.parentMdBuisnessIF.isPublished()
        || !this.childMdBuisnessIF.isPublished())
    {
      return;
    }
    else
    {
      super.go(forceRegeneration);
    }
  }

  /**
   * Write the default constructor for the generated EntityDTO stub class
   */
  protected void writeConstructor()
  {
    getWriter().writeLine(
        "public " + getFileName() + "(" + ClientRequestIF.class.getName() + " clientRequest, "
            + String.class.getName() + " parentId, " + String.class.getName() + " childId)");
    getWriter().openBracket();
    getWriter().writeLine("super(clientRequest, parentId, childId);");
    getWriter().writeLine("");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("/**");
    getWriter()
        .writeLine(
            "* Copy Constructor: Duplicates the values and attributes of the given RelationshipDTO into a new DTO.");
    getWriter().writeLine("* ");
    getWriter().writeLine("* @param relationshipDTO The RelationshipDTO to duplicate");
    getWriter().writeLine(
        "* @param clientRequest The clientRequest this DTO should use to communicate with the server.");
    getWriter().writeLine("*/");
    getWriter().writeLine(
        "protected " + getFileName() + "(" + RelationshipDTO.class.getName() + " relationshipDTO, "
            + ClientRequestIF.class.getName() + " clientRequest)");
    getWriter().openBracket();
    getWriter().writeLine("super(relationshipDTO, clientRequest);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.generation.DTOGenerator#write()
   */
  @Override
  protected void writeConcrete()
  {
    writeGetParent();
    writeGetChild();
    writeGet();
    writeParentQuery();
    writeChildQuery();
  }

  /**
   * Write the static type safe get method for the generated RelationshipDTO
   */
  private void writeGet()
  {
    getWriter().writeLine(
        "public static " + this.getDTOStubClassType() + " get(" + ClientRequestIF.class.getName()
            + " clientRequest, String id)");
    getWriter().openBracket();
    getWriter().writeLine(
        RelationshipDTO.class.getName() + " dto = (" + RelationshipDTO.class.getName()
            + ") clientRequest.get(id);");
    getWriter().writeLine("");
    getWriter().writeLine("return (" + this.getDTOStubClassType() + ") dto;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  private void writeParentQuery()
  {
    String type = this.getMdTypeDAOIF().definesType();
    String typeDTO = type + DTO_SUFFIX;
    String queryType = type + QUERY_DTO_SUFFIX;
    String relationshipQuery = RelationshipQueryDTO.class.getName();

    getWriter().writeLine("public static " + queryType + " parentQuery(" + ClientRequestIF.class.getName() + " clientRequest, String parentId)");
    getWriter().openBracket();
    getWriter().writeLine(relationshipQuery + " queryDTO = (" + relationshipQuery + ") clientRequest.getQuery(" + typeDTO + ".CLASS);");
    getWriter().writeLine("queryDTO.addCondition(\"" + RelationshipInfo.PARENT_ID + "\", \"" + QueryConditions.EQUALS + "\", parentId);");
    getWriter().writeLine("return (" + queryType + ") clientRequest.queryRelationships(queryDTO);");
    getWriter().closeBracket();
  }

  private void writeChildQuery()
  {
    String type = this.getMdTypeDAOIF().definesType();
    String typeDTO = type + DTO_SUFFIX;
    String queryType = type + QUERY_DTO_SUFFIX;
    String relationshipQuery = RelationshipQueryDTO.class.getName();

    getWriter().writeLine("public static " + queryType + " childQuery(" + ClientRequestIF.class.getName() + " clientRequest, String childId)");
    getWriter().openBracket();
    getWriter().writeLine(relationshipQuery + " queryDTO = (" + relationshipQuery + ") clientRequest.getQuery(" + typeDTO + ".CLASS);");
    getWriter().writeLine("queryDTO.addCondition(\"" + RelationshipInfo.CHILD_ID + "\", \"" + QueryConditions.EQUALS + "\", childId);");
    getWriter().writeLine("return (" + queryType + ") clientRequest.queryRelationships(queryDTO);");
    getWriter().closeBracket();
  }

  /**
   * Writes the apply method that can only update a RelationshipDTO. This
   * apply() method can only update a RelationshipDTO because a RelationshipDTO
   * must be created through an addParent() or addChild() clientRequest call.
   */
  protected void writeApply()
  {
    getWriter().writeLine("public void apply()");
    getWriter().openBracket();
    getWriter().writeLine("if(isNewInstance())");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().createRelationship(this);");
    getWriter().closeBracket();
    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().update(this);");
    getWriter().closeBracket();
    getWriter().closeBracket();
  }

  /**
   * Write the type safe getParent method for the generated RelationshipDTO
   */
  private void writeGetParent()
  {
    if (!this.parentMdBuisnessIF.isPublished())
    {
      return;
    }

    getWriter().writeLine("public " + parentDTO + " getParent()");
    getWriter().openBracket();

    if (parentDTO.equals(BusinessDTO.class.getName()))
    {
      getWriter().writeLine(
          BusinessDTO.class.getName() + " dto = (" + BusinessDTO.class.getName()
              + ") getRequest().get(super.getParentId());");
      getWriter().writeLine("");
      getWriter().writeLine("return dto;");
    }
    else
    {
      getWriter().writeLine("return " + parentDTO + ".get(getRequest(), super.getParentId());");
    }

    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the type safe getChild method for the generated RelationshipDTO
   */
  private void writeGetChild()
  {
    if (!this.childMdBuisnessIF.isPublished())
    {
      return;
    }

    getWriter().writeLine("  public " + childDTO + " getChild()");
    getWriter().openBracket();

    if (childDTO.equals(BusinessDTO.class.getName()))
    {
      getWriter().writeLine(
          "" + BusinessDTO.class.getName() + " dto = (" + BusinessDTO.class.getName()
              + ") getRequest().get(super.getChildId());");
      getWriter().writeLine("");
      getWriter().writeLine("return dto;");
    }
    else
    {
      getWriter().writeLine("return " + childDTO + ".get(getRequest(), super.getChildId());");
    }

    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.generation.DTOGenerator#getExtends()
   */
  @Override
  protected String getExtends(MdClassDAOIF parent)
  {
    if (parent == null)
    {
      return RelationshipDTO.class.getName();
    }
    else
    {
      return getParentClass(parent);
    }
  }
}
