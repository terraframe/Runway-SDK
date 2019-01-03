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
package com.runwaysdk.business.generation.dto;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;

/**
 * Generates methods for the DTO class of a MdEntities. Employs the template
 * pattern to generate type specific methods for sub types of MdEntity.
 *
 * @author Justin Smethie
 *
 */
public abstract class EntityDTOBaseGenerator extends ClassDTOBaseGenerator
{
  public EntityDTOBaseGenerator(MdEntityDAOIF mdEntity)
  {
    super(mdEntity);
  }

  @Override
  protected void write()
  {
    writeFields();

    // Write the getter and setters for the attributes
    super.write();

    // write the apply method
    writeApply();

    // write the delete method
    writeDelete();

    // Write the getAllInstances method
    writeGetAllInstances();
  }

  /**
   * Writes the apply method.
   */
  protected abstract void writeApply();

  /**
   * Write the method to unlock the current object
   */
  protected void writeDelete()
  {
    getWriter().writeLine("public void delete()");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().delete(this.getOid());");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  protected void writeFields()
  {
  }

  private void writeGetAllInstances()
  {
    String type = this.getMdTypeDAOIF().definesType();
    String typeDTO = type + DTO_SUFFIX;
    String queryType = type + QUERY_DTO_SUFFIX;

    //get for the java class
    getWriter().writeLine("public static " + queryType + " getAllInstances(" + ClientRequestIF.class.getName() + " clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)");
    getWriter().openBracket();
    getWriter().writeLine("return (" + queryType + ") clientRequest.getAllInstances(" + typeDTO  + ".CLASS, sortAttribute, ascending, pageSize, pageNumber);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }


}
