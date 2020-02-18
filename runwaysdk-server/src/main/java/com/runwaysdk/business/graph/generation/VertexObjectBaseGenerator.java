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
package com.runwaysdk.business.graph.generation;

import java.util.List;

import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.generation.CommonGenerationUtil;

public class VertexObjectBaseGenerator extends GraphObjectBaseGenerator
{

  public VertexObjectBaseGenerator(MdVertexDAOIF mdVertexIF)
  {
    super(mdVertexIF);
  }

  @Override
  protected String getExtends(MdClassDAOIF parent)
  {
    if (parent == null)
    {
      return VertexObject.class.getName();
    }
    else
    {
      return getParentClass(parent);
    }
  }

  @Override
  protected void addConstructor()
  {
    String baseTypeName = this.getBaseClassName();

    // Constructor for the base class
    getWriter().writeLine("public " + baseTypeName + "()");
    getWriter().openBracket();
    getWriter().writeLine("super();");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  protected void addMethods()
  {
    super.addMethods();

    addChildMethods();

    addParentMethods();

    getInstance();
  }

  private void getInstance()
  {
    // get for the java class
    getWriter().writeLine("public static " + this.getSubClassName() + " get(String oid)");
    getWriter().openBracket();
    getWriter().writeLine("return (" + this.getSubClassName() + ") " + VertexObject.class.getName() + ".get(CLASS, oid);");
    getWriter().closeBracket();
    getWriter().writeLine("");
    //
    // getWriter().writeLine("public static " + this.getSubClassName() + "
    // getByKey(String key)");
    // getWriter().openBracket();
    // getWriter().writeLine("return (" + this.getSubClassName() + ") " +
    // VertexObject.class.getName() + ".get(CLASS, key);");
    // getWriter().closeBracket();
    // getWriter().writeLine("");
    //
  }

  public String getVertexType(MdVertexDAOIF childMdVertex)
  {
    if (childMdVertex.isGenerateSource())
    {
      return childMdVertex.definesType();
    }
    else
    {
      return VertexObject.class.getName();
    }
  }

  private void addParentMethods()
  {
    MdVertexDAOIF mdVertexIF = (MdVertexDAOIF) this.getMdTypeDAOIF();

    for (MdEdgeDAOIF mdEdge : mdVertexIF.getChildMdEdges())
    {
      VisibilityModifier visibility = VisibilityModifier.PUBLIC;

      MdVertexDAOIF parentMdVertexIF = mdEdge.getParentMdVertex();

      String parentType = this.getVertexType(parentMdVertexIF);
      String lowercase = CommonGenerationUtil.lowerFirstCharacter(parentMdVertexIF.getTypeName());
      String edgeType = mdEdge.definesType();

      // Add a new Relationship of this type
      getWriter().writeLine(visibility.getJavaModifier() + " " + EdgeObject.class.getName() + " add" + mdEdge.getTypeName() + "Parent(" + parentType + " " + lowercase + ")");
      getWriter().openBracket();
      getWriter().writeLine("return super.addParent(" + lowercase + ", \"" + edgeType + "\");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Remove Relationship(s) with a specific parent
      getWriter().writeLine(visibility.getJavaModifier() + " void remove" + mdEdge.getTypeName() + "Parent(" + parentType + " " + lowercase + ")");
      getWriter().openBracket();
      getWriter().writeLine("super.removeParent(" + lowercase + ", \"" + edgeType + "\");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Get all parents (including duplicates) that have a realtionship with
      // this child
      getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
      getWriter().writeLine(visibility.getJavaModifier() + " " + List.class.getName() + "<" + parentType + "> get" + mdEdge.getTypeName() + "Parent" + parentMdVertexIF.getTypeName() + "s()");
      getWriter().openBracket();
      getWriter().writeLine("return super.getParents(\"" + edgeType + "\", " + parentType + ".class);");
      getWriter().closeBracket();
      getWriter().writeLine("");
    }
  }

  private void addChildMethods()
  {
    MdVertexDAOIF mdVertexIF = (MdVertexDAOIF) this.getMdTypeDAOIF();

    for (MdEdgeDAOIF mdEdge : mdVertexIF.getParentMdEdges())
    {
      VisibilityModifier visibility = VisibilityModifier.PUBLIC;

      MdVertexDAOIF childMdVertexIF = mdEdge.getChildMdVertex();

      String childType = this.getVertexType(childMdVertexIF);
      String lowercase = CommonGenerationUtil.lowerFirstCharacter(childMdVertexIF.getTypeName());
      String edgeType = mdEdge.definesType();

      // Add a new Relationship of this type
      getWriter().writeLine(visibility.getJavaModifier() + " " + EdgeObject.class.getName() + " add" + mdEdge.getTypeName() + "Child(" + childType + " " + lowercase + ")");
      getWriter().openBracket();
      getWriter().writeLine("return super.addChild(" + lowercase + ", \"" + edgeType + "\");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Remove Relationship(s) with a specific parent
      getWriter().writeLine(visibility.getJavaModifier() + " void remove" + mdEdge.getTypeName() + "Child(" + childType + " " + lowercase + ")");
      getWriter().openBracket();
      getWriter().writeLine("super.removeChild(" + lowercase + ", \"" + edgeType + "\");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Get all parents (including duplicates) that have a realtionship with
      // this child
      getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
      getWriter().writeLine(visibility.getJavaModifier() + " " + List.class.getName() + "<" + childType + "> get" + mdEdge.getTypeName() + "Child" + childMdVertexIF.getTypeName() + "s()");
      getWriter().openBracket();
      getWriter().writeLine("return super.getChildren(\"" + edgeType + "\"," + childType + ".class);");
      getWriter().closeBracket();
      getWriter().writeLine("");
    }
  }
}
