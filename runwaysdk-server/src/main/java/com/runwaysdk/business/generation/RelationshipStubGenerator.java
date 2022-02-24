/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;

public class RelationshipStubGenerator extends EntityStubGenerator
{
  public RelationshipStubGenerator(MdRelationshipDAOIF mdRelationship)
  {
    super(mdRelationship);
  }

  protected void addConstructor()
  {
    String typeName = this.getClassName();

    MdRelationshipDAOIF mdRel = (MdRelationshipDAOIF) this.getMdTypeDAOIF();

    String parentClass = this.getReturnType(mdRel.getParentMdBusiness());
    String childClass = this.getReturnType(mdRel.getChildMdBusiness());

    getWriter().writeLine("public " + typeName + "(String parentOid, String childOid)");
    getWriter().openBracket();
    getWriter().writeLine("super(parentOid, childOid);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public " + typeName + '(' + parentClass + " parent, " + childClass + " child)");
    getWriter().openBracket();
    getWriter().writeLine("this(parent.getOid(), child.getOid());");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  public String getReturnType(MdBusinessDAOIF mBusiness)
  {
    String parentClass = Business.class.getName();

    if (mBusiness.isGenerateSource())
    {
      parentClass = mBusiness.definesType();
    }

    return parentClass;
  }
}
