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

import java.util.Locale;

import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdLocalizableDAOIF;
import com.runwaysdk.generation.CommonGenerationUtil;

public abstract class LocalizableBaseGenerator extends TransientBaseGenerator
{
  public LocalizableBaseGenerator(MdLocalizableDAOIF mdLocalizableIF)
  {
    super(mdLocalizableIF);
  }

  @Override
  protected void addConstructor()
  {
    String baseTypeName = this.getBaseClassName();

    // Constructor()
    getWriter().writeLine("public " + baseTypeName + "()");
    getWriter().openBracket();
    getWriter().writeLine("super();");
    // We'll need to initialize Reference objects here
    getWriter().closeBracket();
    getWriter().writeLine("");

    // Constructor(String developerMessage)
    getWriter().writeLine("public " + baseTypeName + "(java.lang.String developerMessage)");
    getWriter().openBracket();
    getWriter().writeLine("super(developerMessage);");
    // We'll need to initialize Reference objects here
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  @Override
  protected void addMethods()
  {
    super.addMethods();

    String string = String.class.getName();
    String locale = Locale.class.getName();
    // Generate the localize method
    getWriter().writeLine("public " + string + " localize(" + locale + " locale)");
    getWriter().openBracket();
    getWriter().writeLine(string + " message = super.localize(locale);");

    for(MdAttributeDAOIF mdAttribute : this.getMdTypeDAOIF().definesAttributesOrdered())
    {
      // Don't try to call a getter that isn't visible
      if (!mdAttribute.getGetterVisibility().equals(VisibilityModifier.PUBLIC))
        continue;

      String attributeName = mdAttribute.definesAttribute();
      getWriter().writeLine("message = replace(message, \"{" + attributeName + "}\", this.get" +
          CommonGenerationUtil.upperFirstCharacter(attributeName) + "());");
    }

    getWriter().writeLine("return message;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }
}
