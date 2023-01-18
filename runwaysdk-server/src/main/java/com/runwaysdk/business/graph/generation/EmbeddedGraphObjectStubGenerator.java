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
package com.runwaysdk.business.graph.generation;

import com.runwaysdk.dataaccess.MdEmbeddedGraphClassDAOIF;

public class EmbeddedGraphObjectStubGenerator extends GraphObjectStubGenerator
{
  public EmbeddedGraphObjectStubGenerator(MdEmbeddedGraphClassDAOIF mdVertexIF)
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
