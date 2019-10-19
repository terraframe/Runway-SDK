package com.runwaysdk.business.graph.generation;

import java.util.List;

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

    for (MdEdgeDAOIF mdRelationship : mdVertexIF.getChildMdEdges())
    {
      VisibilityModifier visibility = VisibilityModifier.PUBLIC;

      MdVertexDAOIF parentMdVertexIF = mdRelationship.getParentMdVertex();

      String parentType = this.getVertexType(parentMdVertexIF);
      String lowercase = CommonGenerationUtil.lowerFirstCharacter(parentMdVertexIF.getTypeName());
      String edgeType = mdRelationship.definesType();

      // Add a new Relationship of this type
      getWriter().writeLine(visibility.getJavaModifier() + " void addParent(" + parentType + " " + lowercase + ")");
      getWriter().openBracket();
      getWriter().writeLine("super.addParent(" + lowercase + ", " + edgeType + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Remove Relationship(s) with a specific parent
      getWriter().writeLine(visibility.getJavaModifier() + " void removeParent(" + parentType + " " + lowercase + ")");
      getWriter().openBracket();
      getWriter().writeLine("super.addParent(" + lowercase + ", " + edgeType + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Get all parents (including duplicates) that have a realtionship with
      // this child
      getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
      getWriter().writeLine(visibility.getJavaModifier() + " " + List.class.getName() + "<" + parentType + "> getParent" + parentMdVertexIF.getTypeName() + "s()");
      getWriter().openBracket();
      getWriter().writeLine("return (" + List.class.getName() + "<" + parentType + ">) super.getParents(" + edgeType + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");
    }
  }

  private void addChildMethods()
  {
    MdVertexDAOIF mdVertexIF = (MdVertexDAOIF) this.getMdTypeDAOIF();

    for (MdEdgeDAOIF mdRelationship : mdVertexIF.getParentMdEdges())
    {
      VisibilityModifier visibility = VisibilityModifier.PUBLIC;

      MdVertexDAOIF parentMdVertexIF = mdRelationship.getChildMdVertex();

      String parentType = this.getVertexType(parentMdVertexIF);
      String lowercase = CommonGenerationUtil.lowerFirstCharacter(parentMdVertexIF.getTypeName());
      String edgeType = mdRelationship.definesType();

      // Add a new Relationship of this type
      getWriter().writeLine(visibility.getJavaModifier() + " void addChild(" + parentType + " " + lowercase + ")");
      getWriter().openBracket();
      getWriter().writeLine("super.addChild(" + lowercase + ", " + edgeType + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Remove Relationship(s) with a specific parent
      getWriter().writeLine(visibility.getJavaModifier() + " void removeChild(" + parentType + " " + lowercase + ")");
      getWriter().openBracket();
      getWriter().writeLine("super.addChild(" + lowercase + ", " + edgeType + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Get all parents (including duplicates) that have a realtionship with
      // this child
      getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
      getWriter().writeLine(visibility.getJavaModifier() + " " + List.class.getName() + "<" + parentType + "> getChild" + parentMdVertexIF.getTypeName() + "s()");
      getWriter().openBracket();
      getWriter().writeLine("return (" + List.class.getName() + "<" + parentType + ">) super.getChildren(" + edgeType + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");
    }
  }
}
