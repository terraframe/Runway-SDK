package com.runwaysdk.business.graph.generation;

import com.runwaysdk.dataaccess.MdVertexDAOIF;

public class VertexObjectStubGenerator extends GraphObjectStubGenerator
{
  public VertexObjectStubGenerator(MdVertexDAOIF mdVertexIF)
  {
    super(mdVertexIF);
  }

  @Override
  protected void addConstructor()
  {
    String typeName = this.getClassName();

    // Constructors for the java class
    getWriter().writeLine("public " + typeName + "()");
    getWriter().openBracket();
    getWriter().writeLine("super();");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

}
