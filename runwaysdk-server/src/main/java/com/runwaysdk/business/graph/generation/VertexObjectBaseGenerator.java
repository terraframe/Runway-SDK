package com.runwaysdk.business.graph.generation;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;

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

    // addAddChildMethods();
    //
    // addAddParentMethods();
    //
    // getInstance();
    //
    // addGetEnumerationMethod();
  }

  private void getInstance()
  {
    // // get for the java class
    // getWriter().writeLine("public static " + this.getSubClassName() + "
    // get(String oid)");
    // getWriter().openBracket();
    // getWriter().writeLine("return (" + this.getSubClassName() + ") " +
    // VertexObject.class.getName() + ".get(oid);");
    // getWriter().closeBracket();
    // getWriter().writeLine("");
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

  // public String getRelationshipClass(MdRelationshipDAOIF mdRelationship)
  // {
  // if (mdRelationship.isGenerateSource())
  // {
  // return mdRelationship.definesType() + ".CLASS";
  // }
  //
  // // A source class doesn't exist to reference so we must return the
  // hardcoded
  // // string of the relationship type instead of referencing the CLASS
  // variable
  // return "\"" + mdRelationship.definesType() + "\"";
  // }
  //
  // private void addAddChildMethods()
  // {
  // MdVertexDAOIF mdVertexDAOIF = (MdVertexDAOIF) this.getMdTypeDAOIF();
  //
  // for (MdRelationshipDAOIF mdRelationship :
  // mdVertexDAOIF.getParentMdRelationshipsOrdered())
  // {
  // VisibilityModifier visibility = mdRelationship.getChildVisibility();
  //
  // MdVertexDAOIF childMdVertex = mdRelationship.getChildMdVertex();
  //
  // if (GenerationUtil.isHardcodedType(childMdVertex))
  // {
  // continue;
  // }
  //
  // String childType = this.getVertexType(childMdVertex);
  // String lowercase =
  // CommonGenerationUtil.lowerFirstCharacter(childMdVertex.getTypeName());
  // String childMethod = mdRelationship.getChildMethod();
  //
  // String relType = this.getRelationshipType(mdRelationship);
  // String relTypeClass = this.getRelationshipClass(mdRelationship);
  //
  // // Add a new Relationship of this type
  // getWriter().writeLine(visibility.getJavaModifier() + " " + relType + " add"
  // + childMethod + "(" + childType + " " + lowercase + ")");
  // getWriter().openBracket();
  // getWriter().writeLine("return (" + relType + ") addChild(" + lowercase + ",
  // " + relTypeClass + ");");
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  //
  // // Remove Relationship(s) with a specific child
  // getWriter().writeLine(visibility.getJavaModifier() + " void remove" +
  // childMethod + "(" + childType + " " + lowercase + ")");
  // getWriter().openBracket();
  // getWriter().writeLine("removeAllChildren(" + lowercase + ", " +
  // relTypeClass + ");");
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  //
  // // Get all children (including duplicates) that have a realtionship with
  // // this parent
  // getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
  // getWriter().writeLine(visibility.getJavaModifier() + " " +
  // OIterator.class.getName() + "<? extends " + childType + "> getAll" +
  // childMethod + "()");
  // getWriter().openBracket();
  // getWriter().writeLine("return (" + OIterator.class.getName() + "<? extends
  // " + childType + ">) getChildren(" + relTypeClass + ");");
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  //
  // // Get all Relationships of this specific type
  // getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
  // getWriter().writeLine(visibility.getJavaModifier() + " " +
  // OIterator.class.getName() + "<? extends " + relType + "> getAll" +
  // childMethod + "Rel()");
  // getWriter().openBracket();
  // getWriter().writeLine("return (" + OIterator.class.getName() + "<? extends
  // " + relType + ">) getChildRelationships(" + relTypeClass + ");");
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  //
  // // Get Relationship(s) with a specific parent
  // // Graphs and Trees should return one object, not a list
  // if (mdRelationship instanceof MdGraphDAOIF)
  // {
  // getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
  // getWriter().writeLine(visibility.getJavaModifier() + " " + relType + " get"
  // + childMethod + "Rel(" + childType + " " + lowercase + ")");
  // getWriter().openBracket();
  // getWriter().writeLine(OIterator.class.getName() + "<? extends " + relType +
  // "> iterator = (" + OIterator.class.getName() + "<? extends " + relType +
  // ">) getRelationshipsWithChild(" + lowercase + ", " + relTypeClass + ");");
  // getWriter().writeLine("try");
  // getWriter().openBracket();
  // getWriter().writeLine("if (iterator.hasNext())");
  // getWriter().writeLine("{");
  // getWriter().writeLine(" return iterator.next();");
  // getWriter().writeLine("}");
  // getWriter().writeLine("else");
  // getWriter().writeLine("{");
  // getWriter().writeLine(" return null;");
  // getWriter().writeLine("}");
  // getWriter().closeBracket();
  // getWriter().writeLine("finally");
  // getWriter().openBracket();
  // getWriter().writeLine("iterator.close();");
  // getWriter().closeBracket();
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  // }
  // else
  // {
  // getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
  // getWriter().writeLine(visibility.getJavaModifier() + " " +
  // OIterator.class.getName() + "<? extends " + relType + "> get" + childMethod
  // + "Rel(" + childType + " " + lowercase + ")");
  // getWriter().openBracket();
  // getWriter().writeLine("return (" + OIterator.class.getName() + "<? extends
  // " + relType + ">) getRelationshipsWithChild(" + lowercase + ", " +
  // relTypeClass + ");");
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  // }
  // }
  // }
  //
  // public String getVertexType(MdVertexDAOIF childMdVertex)
  // {
  // if (childMdVertex.isGenerateSource())
  // {
  // return childMdVertex.definesType();
  // }
  // else
  // {
  // return VertexObject.class.getName();
  // }
  // }
  //
  // public String getRelationshipType(MdRelationshipDAOIF mdRelationship)
  // {
  // if (mdRelationship.isGenerateSource())
  // {
  // return mdRelationship.definesType();
  // }
  // else
  // {
  // return Relationship.class.getName();
  // }
  // }
  //
  // private void addAddParentMethods()
  // {
  // MdVertexDAOIF mdVertexIF = (MdVertexDAOIF) this.getMdTypeDAOIF();
  //
  // for (MdRelationshipDAOIF mdRelationship :
  // mdVertexIF.getChildMdRelationshipsOrdered())
  // {
  // VisibilityModifier visibility = mdRelationship.getParentVisibility();
  //
  // MdVertexDAOIF parentMdVertexIF = mdRelationship.getParentMdVertex();
  //
  // if (GenerationUtil.isHardcodedType(parentMdVertexIF))
  // {
  // continue;
  // }
  //
  // String parentType = this.getVertexType(parentMdVertexIF);
  // String parentMethod = mdRelationship.getParentMethod();
  // String lowercase =
  // CommonGenerationUtil.lowerFirstCharacter(parentMdVertexIF.getTypeName());
  // String relType = this.getRelationshipType(mdRelationship);
  // String relTypeClass = this.getRelationshipClass(mdRelationship);
  //
  // // Add a new Relationship of this type
  // getWriter().writeLine(visibility.getJavaModifier() + " " + relType + " add"
  // + parentMethod + "(" + parentType + " " + lowercase + ")");
  // getWriter().openBracket();
  // getWriter().writeLine("return (" + relType + ") addParent(" + lowercase +
  // ", " + relTypeClass + ");");
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  //
  // // Remove Relationship(s) with a specific parent
  // getWriter().writeLine(visibility.getJavaModifier() + " void remove" +
  // parentMethod + "(" + parentType + " " + lowercase + ")");
  // getWriter().openBracket();
  // getWriter().writeLine("removeAllParents(" + lowercase + ", " + relTypeClass
  // + ");");
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  //
  // // Get all parents (including duplicates) that have a realtionship with
  // // this child
  // getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
  // getWriter().writeLine(visibility.getJavaModifier() + " " +
  // OIterator.class.getName() + "<? extends " + parentType + "> getAll" +
  // parentMethod + "()");
  // getWriter().openBracket();
  // getWriter().writeLine("return (" + OIterator.class.getName() + "<? extends
  // " + parentType + ">) getParents(" + relTypeClass + ");");
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  //
  // // Get all Relationships of this specific type
  // getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
  // getWriter().writeLine(visibility.getJavaModifier() + " " +
  // OIterator.class.getName() + "<? extends " + relType + "> getAll" +
  // parentMethod + "Rel()");
  // getWriter().openBracket();
  // getWriter().writeLine("return (" + OIterator.class.getName() + "<? extends
  // " + relType + ">) getParentRelationships(" + relTypeClass + ");");
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  //
  // // Get Relationship(s) with a specific parent
  // // Graphs and Trees should return one object, not a list
  // if (mdRelationship instanceof MdGraphDAOIF)
  // {
  // getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
  // getWriter().writeLine(visibility.getJavaModifier() + " " + relType + " get"
  // + parentMethod + "Rel(" + parentType + " " + lowercase + ")");
  // getWriter().openBracket();
  // getWriter().writeLine(OIterator.class.getName() + "<? extends " + relType +
  // "> iterator = (" + OIterator.class.getName() + "<? extends " + relType +
  // ">) getRelationshipsWithParent(" + lowercase + ", " + relTypeClass + ");");
  // getWriter().writeLine("try");
  // getWriter().openBracket();
  // getWriter().writeLine("if (iterator.hasNext())");
  // getWriter().writeLine("{");
  // getWriter().writeLine(" return iterator.next();");
  // getWriter().writeLine("}");
  // getWriter().writeLine("else");
  // getWriter().writeLine("{");
  // getWriter().writeLine(" return null;");
  // getWriter().writeLine("}");
  // getWriter().closeBracket();
  // getWriter().writeLine("finally");
  // getWriter().openBracket();
  // getWriter().writeLine("iterator.close();");
  // getWriter().closeBracket();
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  // }
  // else
  // {
  // getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
  // getWriter().writeLine(visibility.getJavaModifier() + " " +
  // OIterator.class.getName() + "<? extends " + relType + "> get" +
  // parentMethod + "Rel(" + parentType + " " + lowercase + ")");
  // getWriter().openBracket();
  // getWriter().writeLine("return (" + OIterator.class.getName() + "<? extends
  // " + relType + ">) getRelationshipsWithParent(" + lowercase + ", " +
  // relTypeClass + ");");
  // getWriter().closeBracket();
  // getWriter().writeLine("");
  // }
  // }
  // }
}
