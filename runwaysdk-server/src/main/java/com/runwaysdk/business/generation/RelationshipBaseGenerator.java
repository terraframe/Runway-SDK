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
package com.runwaysdk.business.generation;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;

public class RelationshipBaseGenerator extends ElementBaseGenerator
{
  public RelationshipBaseGenerator(MdRelationshipDAOIF mdRelationship)
  {
    super(mdRelationship);
  }

  protected void addMethods()
  {
    super.addMethods();

    // Add typesafe getParent() and getChild()
    MdRelationshipDAOIF mdRel = (MdRelationshipDAOIF) this.getMdTypeDAOIF();

    String parentClass = this.getReturnType(mdRel.getParentMdBusiness());
    String childClass = this.getReturnType(mdRel.getChildMdBusiness());

    getWriter().writeLine("public " + parentClass + " getParent()");
    getWriter().openBracket();
    getWriter().writeLine("return (" + parentClass + ") super.getParent();");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public " + childClass + " getChild()");
    getWriter().openBracket();
    getWriter().writeLine("return (" + childClass + ") super.getChild();");
    getWriter().closeBracket();
    getWriter().writeLine("");

    String subClassName = this.getSubClassName();

    getWriter().writeLine("public static " + subClassName + " get(String oid)");
    getWriter().openBracket();
    getWriter().writeLine("return (" + subClassName + ") " + RelationshipInfo.CLASS + ".get(oid);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public static " + this.getSubClassName() + " getByKey(String key)");
    getWriter().openBracket();
    getWriter().writeLine("return (" + this.getSubClassName() + ") " + RelationshipInfo.CLASS + ".get(CLASS, key);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  public String getReturnType(MdBusinessDAOIF mdBusiness)
  {
    String parentClass = Business.class.getName();

    if (mdBusiness.isGenerateSource())
    {
      parentClass = mdBusiness.definesType();
    }

    return parentClass;
  }

  @Override
  protected String getExtends(MdClassDAOIF parent)
  {
    if (parent == null)
    {
      return Relationship.class.getName();
    }
    else
    {
      return getParentClass(parent);
    }
  }

  protected void addConstructor()
  {
    String baseTypeName = this.getBaseClassName();

    getWriter().writeLine("public " + baseTypeName + "(String parentId, String childId)");
    getWriter().openBracket();
    getWriter().writeLine("super(parentId, childId);");
    addStructInitializers();
    getWriter().closeBracket();
    getWriter().writeLine("");
  }
}
