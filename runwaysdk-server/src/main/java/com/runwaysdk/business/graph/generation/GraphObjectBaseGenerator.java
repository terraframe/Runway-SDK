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

import com.runwaysdk.business.Business;
import com.runwaysdk.business.generation.ClassBaseGenerator;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.generation.CommonGenerationUtil;

public abstract class GraphObjectBaseGenerator extends ClassBaseGenerator
{
  public GraphObjectBaseGenerator(MdGraphClassDAOIF mdGraphClass)
  {
    super(mdGraphClass);
  }

  protected void addEquals(MdAttributeDAOIF m)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Generates a Getter for the given attribute in the base .java file
   * 
   * @param m
   *          MdAttribute to generate
   */
  protected void addGetter(MdAttributeDAOIF m)
  {
    VisibilityModifier getterVisibility = m.getGetterVisibility();

    String attributeName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());
    getWriter().writeLine(getterVisibility.getJavaModifier() + " " + m.javaType(false) + " " + CommonGenerationUtil.GET + attributeName + "()");
    getWriter().openBracket();
    getWriter().writeLine("return (" + m.javaType(false) + ") this.getObjectValue(" + m.definesAttribute().toUpperCase() + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates a Getter for the given attribute in the base .java file
   * 
   * @param m
   *          MdAttribute to generate
   */
  protected void addReferenceGetter(MdAttributeReferenceDAOIF m)
  {
    VisibilityModifier getterVisibility = m.getGetterVisibility();

    MdBusinessDAOIF referenceMdBusiness = m.getReferenceMdBusinessDAO();

    String attributeName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());
    getWriter().writeLine(getterVisibility.getJavaModifier() + " " + m.javaType(false) + " get" + attributeName + "()");
    getWriter().openBracket();

    getWriter().writeLine("if (this.getObjectValue(" + m.definesAttribute().toUpperCase() + ") == null)");
    getWriter().openBracket();
    getWriter().writeLine("return null;");
    getWriter().closeBracket();

    getWriter().writeLine("else");
    getWriter().openBracket();
    if (referenceMdBusiness.isGenerateSource())
    {
      getWriter().writeLine("return " + referenceMdBusiness.definesType() + ".get( (String) this.getObjectValue(" + m.definesAttribute().toUpperCase() + "));");
    }
    else
    {
      getWriter().writeLine("return " + Business.class.getName() + ".get( (String) this.getObjectValue(" + m.definesAttribute().toUpperCase() + "));");
    }
    getWriter().closeBracket();

    getWriter().closeBracket();
    getWriter().writeLine("");

    // Generate an accessor that returns the reference oid
    String refAttributeIdName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute()) + CommonGenerationUtil.upperFirstCharacter(ComponentInfo.OID);
    getWriter().writeLine(getterVisibility.getJavaModifier() + " String get" + refAttributeIdName + "()");
    getWriter().openBracket();
    getWriter().writeLine("return (String) this.getObjectValue(" + m.definesAttribute().toUpperCase() + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates a setter for the given attribute in the base .java file
   * 
   * @param m
   *          MdAttribute to generate
   */
  protected void addSetter(MdAttributeDAOIF m)
  {
    // do not generate a setter for a system attribute.
    if (m.isSystem())
    {
      return;
    }

    VisibilityModifier setterVisibility = m.getSetterVisibility();

    String attributeName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());
    getWriter().writeLine(setterVisibility.getJavaModifier() + " void set" + attributeName + "(" + m.javaType(false) + " value)");
    getWriter().openBracket();

    if (m instanceof MdAttributeReferenceDAOIF)
    {
      getWriter().writeLine("this.setValue(" + m.definesAttribute().toUpperCase() + ", value.getOid());");
    }
    else
    {
      getWriter().writeLine("this.setValue(" + m.definesAttribute().toUpperCase() + ", value);");
    }

    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates a setter for the given attribute in the base .java file
   * 
   * @param m
   *          MdAttribute to generate
   */
  protected void addReferenceSetter(MdAttributeDAOIF m)
  {
    // do not generate a setter for a system attribute.
    if (m.isSystem())
    {
      return;
    }

    VisibilityModifier setterVisibility = m.getSetterVisibility();

    String attributeName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());
    getWriter().writeLine(setterVisibility.getJavaModifier() + " void set" + attributeName + "Id(" + String.class.getName() + " oid)");
    getWriter().openBracket();
    getWriter().writeLine("this.setValue(" + m.definesAttribute().toUpperCase() + ", oid);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates a Validate method for the given attribute in the base .java file
   * 
   * @param m
   *          MdAttribute to generate
   */
  protected void addValidator(MdAttributeDAOIF m)
  {
    // VisibilityModifier setterVisibility = m.getSetterVisibility();
    //
    // String attributeName =
    // CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());
    // getWriter().writeLine(setterVisibility.getJavaModifier() + " void
    // validate" + attributeName + "()");
    // getWriter().openBracket();
    // getWriter().writeLine("this.validateAttribute(" +
    // m.definesAttribute().toUpperCase() + ");");
    // getWriter().closeBracket();
    // getWriter().writeLine("");
  }

}
