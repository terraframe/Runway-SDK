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
package com.runwaysdk.business.generation;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.query.QueryFactory;

/**
 * Generates all elements in a java file common to Entities: package, class
 * name, contrustors, attribute accessor methods, etc.
 * 
 * @author Eric G
 */
public abstract class EntityBaseGenerator extends ClassBaseGenerator
{
  public EntityBaseGenerator(MdEntityDAOIF mdEntity)
  {
    super(mdEntity);
  }

  @Override
  protected void addMethods()
  {
    super.addMethods();

    addGetAllInstances();
  }

  private void addGetAllInstances()
  {
    if (this.getMdTypeDAOIF().isPublished() && !this.getMdTypeDAOIF().isSystemPackage())
    {
      String queryType = this.getSubClassName() + EntityQueryAPIGenerator.QUERY_API_SUFFIX;

      // get for the java class
      getWriter().writeLine("public static " + queryType + " getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)");
      getWriter().openBracket();
      getWriter().writeLine(queryType + " query = new " + queryType + "(new " + QueryFactory.class.getName() + "());");
      getWriter().writeLine(EntityInfo.CLASS + ".getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);");
      getWriter().writeLine("return query;");
      getWriter().closeBracket();
      getWriter().writeLine("");
    }
  }

}
