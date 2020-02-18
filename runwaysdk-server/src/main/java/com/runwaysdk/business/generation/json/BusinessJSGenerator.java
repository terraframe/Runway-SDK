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
package com.runwaysdk.business.generation.json;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.BusinessDTOInfo;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.facade.FacadeUtil;
import com.runwaysdk.generation.CommonGenerationUtil;

public class BusinessJSGenerator extends ElementJSGenerator
{
  /**
   * Constructor
   *
   * @param mdBusinessIF
   */
  public BusinessJSGenerator(String sessionId, MdBusinessDAOIF mdBusinessIF)
  {
    super(sessionId, mdBusinessIF);
  }

  @Override
  protected MdBusinessDAOIF getMdTypeIF()
  {
    return (MdBusinessDAOIF) super.getMdTypeIF();
  }

  @Override
  protected List<Declaration> getInstanceMethods()
  {
    List<Declaration> instances = super.getInstanceMethods();

    instances.addAll(writeParentMethods());
    instances.addAll(writeChildMethods());

    return instances;
  }

  /**
   * Adds getter for the parent objects
   */
  private Declaration writeGetAllParents(MdRelationshipDAOIF mdRelationship)
  {
    Declaration method = this.newDeclaration();

    String methodName = mdRelationship.getParentMethod();
    String relationshipName = mdRelationship.definesType();

    method.writeln("getAll" + methodName + " : function(clientRequest)");
    method.openBracketLn();
    method.writeln(JSON.RUNWAY_FACADE.getLabel()+".getParents(clientRequest, this.getOid(), '" + relationshipName + "');");
    method.closeBracket();

    return method;
  }

  /**
   * Adds getter for the parent relationships
   */
  private Declaration writeGetAllParentRelationships(MdRelationshipDAOIF mdRelationship)
  {
    Declaration method = this.newDeclaration();

    String methodName = mdRelationship.getParentMethod();
    String relationshipName = mdRelationship.definesType();

    method.writeln("getAll" + methodName + "Relationships : function(clientRequest)");
    method.openBracketLn();
    method.writeln(JSON.RUNWAY_FACADE.getLabel()+".getParentRelationships(clientRequest, this.getOid(), '" + relationshipName + "');");
    method.closeBracket();

    return method;
  }

  /**
   * Write all of the Children methods for all relationships which this
   * type is the parent participant.
   */
  private List<Declaration> writeChildMethods()
  {
    List<Declaration> method = new LinkedList<Declaration>();

    MdBusinessDAOIF mdBusinessIF = getMdTypeIF();

    for (MdRelationshipDAOIF mdRel : mdBusinessIF.getParentMdRelationshipsOrdered())
    {
      method.add(writeGetAllChildren(mdRel));
      method.add(writeGetAllChildrenRelationships(mdRel));
      method.add(writeAddChild(mdRel));
      method.add(writeRemoveChild(mdRel));
      method.add(writeRemoveChildren(mdRel));
    }

    return method;
  }

  private Declaration writeRemoveChildren(MdRelationshipDAOIF mdRelationship)
  {
    Declaration method = this.newDeclaration();

    String methodName = "removeAll" + mdRelationship.getChildMethod();
    String relationshipName = mdRelationship.definesType();

    method.writeln(methodName + " : function(clientRequest)");
    method.openBracketLn();
    method.writeln(JSON.RUNWAY_FACADE.getLabel()+".deleteChildren(clientRequest, this.getOid(), '" + relationshipName + "');");
    method.closeBracket();

    return method;
  }

  /**
   * Write the removeChild method for a given MdRelationship
   *
   * @param mdRelationship
   */
  private Declaration writeRemoveChild(MdRelationshipDAOIF mdRelationship)
  {
    Declaration method = this.newDeclaration();

    String methodName = "remove" + mdRelationship.getChildMethod();

    method.writeln(methodName + " : function(clientRequest, relationship)");
    method.openBracketLn();
    method.writeln("  var relationshipId = (relationship instanceof Object) ? relationship.getOid() : relationship;");
    method.writeln(JSON.RUNWAY_FACADE.getLabel()+".deleteChild(clientRequest, relationshipId);");
    method.closeBracket();

    return method;
  }

  /**
   * Write the addChild method for a given MdRelationship.
   * @param mdRelationship
   */
  private Declaration writeAddChild(MdRelationshipDAOIF mdRelationship)
  {
    Declaration method = this.newDeclaration();
    String relationshipName = mdRelationship.definesType();

    String methodName = "add" + CommonGenerationUtil.upperFirstCharacter(mdRelationship.getChildMethod());

    method.writeln(methodName + " : function(clientRequest, child)");
    method.openBracketLn();
    method.writeln("var childOid = (child instanceof Object) ? child.getOid() : child;");
    method.writeln(JSON.RUNWAY_FACADE.getLabel()+".addChild(clientRequest, this.getOid(), childOid, '"+relationshipName+"');");
    method.closeBracket();

    return method;
  }

  /**
   * Write the getAllChildRelationships method for a given MdRelationship.
   *
   * @param mdRelationship
   */
  private Declaration writeGetAllChildrenRelationships(MdRelationshipDAOIF mdRelationship)
  {
    Declaration method = this.newDeclaration();

    String methodName = mdRelationship.getChildMethod();
    String relationshipName = mdRelationship.definesType();

    method.writeln("getAll" + methodName + "Relationships : function(clientRequest)");
    method.openBracketLn();
    method.writeln(JSON.RUNWAY_FACADE.getLabel()+".getChildRelationships(clientRequest, this.getOid(), '" + relationshipName + "');");
    method.closeBracket();

    return method;
  }

  /**
   * Writes the getAllChildren method for a given MdRelationship
   *
   * @param mdRelationship
   */
  private Declaration writeGetAllChildren(MdRelationshipDAOIF mdRelationship)
  {
    Declaration method = this.newDeclaration();

    String methodName = mdRelationship.getChildMethod();
    String relationshipName = mdRelationship.definesType();

    method.writeln("getAll" + methodName + " : function(clientRequest)");
    method.openBracketLn();
    method.writeln(JSON.RUNWAY_FACADE.getLabel()+".getChildren(clientRequest, this.getOid(), '" + relationshipName + "');");
    method.closeBracket();

    return method;
  }

  /**
   * Writes all the parent methods.
   */
  private List<Declaration> writeParentMethods()
  {
    List<Declaration> methods = new LinkedList<Declaration>();

    MdBusinessDAOIF mdBusinessIF = getMdTypeIF();

    for(MdRelationshipDAOIF mdRel : mdBusinessIF.getChildMdRelationshipsOrdered())
    {
      methods.add(writeGetAllParents(mdRel));
      methods.add(writeGetAllParentRelationships(mdRel));
      methods.add(writeAddParent(mdRel));
      methods.add(writeRemoveParent(mdRel));
      methods.add(writeRemoveParents(mdRel));
    }

    return methods;
  }

  private Declaration writeRemoveParents(MdRelationshipDAOIF mdRelationship)
  {
    Declaration method = this.newDeclaration();

    String methodName = "removeAll" + mdRelationship.getParentMethod();
    String relationshipName = mdRelationship.definesType();

    method.writeln(methodName + " : function(clientRequest)");
    method.openBracketLn();
    method.writeln(JSON.RUNWAY_FACADE.getLabel()+".deleteParents(clientRequest, this.getOid(), '" + relationshipName + "');");
    method.closeBracket();

    return method;
  }

  /**
   * Write the removeChild method for a given MdRelationship.
   * @param mdRelationship
   */
  private Declaration writeRemoveParent(MdRelationshipDAOIF mdRelationship)
  {
    Declaration method = this.newDeclaration();

    String methodName = "remove" + mdRelationship.getParentMethod();

    method.writeln(methodName + " : function(clientRequest, relationship)");
    method.openBracketLn();
    method.writeln("var relationshipId = (relationship instanceof Object) ? relationship.getOid() : relationship;");
    method.writeln(JSON.RUNWAY_FACADE.getLabel()+".deleteParent(clientRequest, relationshipId);");
    method.closeBracket();

    return method;
  }

  /**
   * Write the addParent method for a given MdRelationship.
   * @param mdRelationship
   */
  private Declaration writeAddParent(MdRelationshipDAOIF mdRelationship)
  {
    Declaration method = this.newDeclaration();

    String relationshipName = mdRelationship.definesType();

    String methodName = "add" + CommonGenerationUtil.upperFirstCharacter(mdRelationship.getParentMethod());

    method.writeln(methodName + " : function(clientRequest, parent)");
    method.openBracketLn();
    method.writeln("var parentOid = (parent instanceof Object) ? parent.getOid() : parent;");
    method.writeln(JSON.RUNWAY_FACADE.getLabel()+".addParent(clientRequest, parentOid, this.getOid(), '"+relationshipName+"');");
    method.closeBracket();

    return method;
  }

  @Override
  protected String getParent()
  {
    MdBusinessDAOIF superEntity = getMdTypeIF().getSuperClass();
    if(superEntity == null)
    {
      return BusinessDTOInfo.CLASS;
    }
    else
    {
      return superEntity.definesType();
    }
  }

  /**
   * Writes the constructor. If an object obj is specified in the constructor,
   * then a new instance is created using that. Otherwise, a default object is used.
   */
  @Override
  protected Declaration getInitialize()
  {
    Declaration method = this.newDeclaration();

    MdBusinessDAOIF mdBusinessIF = getMdTypeIF();

    method.writeln("initialize : function(obj)");
    method.openBracketLn();

    if(!mdBusinessIF.isAbstract())
    {
      String definesType = mdBusinessIF.definesType();

      Business business = BusinessFacade.newBusiness(definesType);
      BusinessDTO newInstance = (BusinessDTO) FacadeUtil.populateComponentDTOIF(getSessionId(), business, true);

      // get the JSON string, but escape all single quotes because the new instance is held
      // in a string wrapped by single quotes (double quotes are used internally within the json)
      String jsonNewInstance = StringEscapeUtils.escapeJavaScript(JSONFacade.getJSONFromComponentDTO(newInstance).toString());

      method.writeln("if(obj == null)");
      method.openBracketLn();
      method.writeln("var json = '"+jsonNewInstance+"';");
      method.writeln("obj = "+JSON.RUNWAY_DTO_GET_OBJECT.getLabel()+"(json);");
      method.closeBracketLn();
    }

    method.writeln("this.$initialize(obj);");
    method.closeBracket();

    return method;
  }

}
