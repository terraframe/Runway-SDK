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
package com.runwaysdk.business.generation.dto;

import com.runwaysdk.business.generation.AbstractClientGenerator;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;

/**
 * Generates type safe DTOs using the template pattern.
 * This class defines the general algorithm to use for
 * generating the DTO java files.
 *
 * @author jsmethie
 *
 */
public abstract class ComponentDTOGenerator extends AbstractClientGenerator implements TypeGeneratorInfo
{
  /**
   * @param mdTypeDAOIF Type for which this generator will generate code artifacts.
   * @param fileName
   */
  public ComponentDTOGenerator(MdTypeDAOIF mdTypeIF, String fileName)
  {
    super(mdTypeIF, fileName);
  }

  /**
   * The general algorithm used to generate the DTOs
   */
  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    // do not generate if the base class is symantically identical
    if (LocalProperties.isKeepBaseSource() && this.hashEquals())
    {
      return;
    }

    //Generate the package and class defintion
    writePackage();

    writeClassName();

    //Write the type specfic methods defined by the sub classes
    write();

    getWriter().closeBracket();
    getWriter().close();
  }


  protected abstract boolean hashEquals();

  /**
   * Returns a list of the fully qualified paths of the files generated.
   *
   * @return
   */
  public String getPath()
  {
    if (this.getMdTypeDAOIF() instanceof MdClassDAOIF)
    {
      if (((MdClassDAOIF)this.getMdTypeDAOIF()).isPublished())
      {
        return super.getPath();
      }
      // No DTOs are generated for unpublished classes.
      else
      {
        return "";
      }
    }
    else
    {
      return super.getPath();
    }
  }

  /**
   * Writs a string constant that contains the type string of the class.
   * @param type string constant that contains the type string of the class.
   */
  protected void writeClassConstant(String type)
  {
    getWriter().writeLine("public final static String CLASS = \""+type+"\";");
  }

  /**
   * Write the package of the DTO .java file being generated
   */
  protected void writePackage()
  {
    getWriter().writeLine("package " + this.getMdTypeDAOIF().getPackage() + ";");
    getWriter().writeLine("");
  }

  /**
   * Write the type safe class name of the DTO class being generated
   */
  protected abstract void writeClassName();

  /**
   * Write the type specific methods of the DTO depending on the type
   * of the MdEntity, e.g. MdBusiness vs MdRelationship vs ...
   */
  protected abstract void write();

}
