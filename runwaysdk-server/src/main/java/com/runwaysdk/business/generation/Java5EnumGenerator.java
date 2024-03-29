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
package com.runwaysdk.business.generation;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.runwaysdk.dataaccess.MdTypeDAOIF;

/**
 * Abstract generator for Java5 enumerations. Extended for generation of
 * MdEnumerations, State Machines, etc.
 * 
 * @author Eric Grunzke
 */
public abstract class Java5EnumGenerator extends AbstractGenerator
{
  /**
   * Empty constructor so child classes can super()
   * 
   * @param mdTypeDAOIF
   *          Type for which this generator will generate code artifacts.
   * @param fileName
   *          The name of the file to generate.
   */
  protected Java5EnumGenerator(MdTypeDAOIF mdTypeDAOIF, String fileName)
  {
    super(mdTypeDAOIF, fileName);
  }

  /**
   * Writes the package name and a standard warning to not modify the file.
   * 
   * @param pack
   *          The package of the generated enum
   */
  protected void addPackage(String pack)
  {
    getWriter().writeLine("package " + pack + ";");
    getWriter().writeLine("");
    getWriter().writeLine("/**");
    getWriter().writeLine(" * This class is generated automatically.");
    getWriter().writeLine(" * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN");
    getWriter().writeLine(" *");
    getWriter().writeLine(" * @author Autogenerated by RunwaySDK");
    getWriter().writeLine(" */");
  }

  /**
   * Writes the declaration of the enum. Concrete implementations that require
   * <code>extends</code> or <code>implements</code> must override this method.
   * 
   * @param enumName
   */
  protected void addEnumName(String enumName)
  {
    getWriter().write("public enum " + enumName);
    getWriter().writeLine("");

    getWriter().openBracket();
  }

  /**
   * Writes the items in this enumeration, which includes parameters that get
   * passed into its constructor.
   * 
   * @param names
   * @param parameters
   */
  protected void writeEnumItems(Map<String, String> items)
  {
    if (items.size() == 0)
    {
      getWriter().writeLine(";");
      getWriter().writeLine("");
      return;
    }

    Iterator<Entry<String, String>> iterator = items.entrySet().iterator();
    while (iterator.hasNext())
    {
      Entry<String, String> entry = iterator.next();
      String item = entry.getKey();
      String parameters = entry.getValue();

      StringBuilder line = new StringBuilder(item + "(" + parameters + ")");
      if (iterator.hasNext())
        line.append(",");
      else
        line.append(";");

      getWriter().writeLine(line.toString());
      getWriter().writeLine("");
    }
  }

  /**
   * Writes the declaration of a private class variable.
   * 
   * @param type
   * @param name
   */
  protected void addField(String type, String name)
  {
    getWriter().writeLine("private " + type + ' ' + name + ';');
    getWriter().writeLine("");
  }

  /**
   * Writes the constructor's declaration, but not body
   * 
   * @param enumType
   *          The name of the generate enum
   * @param parameterTypes
   *          Types of the parameters for the constructor
   * @param parameterNames
   *          Names of the parameters for the constructor
   */
  protected void addConstructorDeclaration(String enumType, String[] parameterTypes, String[] parameterNames)
  {
    StringBuffer parameters = new StringBuffer();

    for (int i = 0; i < parameterTypes.length; i++)
    {
      parameters.append(parameterTypes[i] + " " + parameterNames[i]);

      if (i != parameterTypes.length - 1)
      {
        parameters.append(", ");
      }
    }

    getWriter().writeLine("private " + enumType + "(" + parameters.toString() + ")");
  }

  /**
   * Writes the body of the constructor. Converts paramters to the correct type
   * and assigns them to class variables.
   * 
   * @param attributes
   *          Names of the class variables
   * @param values
   *          Code that converts parameters to the correct type for assignment
   *          to variables
   */
  protected void addConstructorBody(String[] attributes, String[] values)
  {
    getWriter().openBracket();

    for (int i = 0; i < attributes.length; i++)
    {
      getWriter().writeLine("    this." + attributes[i] + " = " + values[i] + ';');
    }

    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates a getter for access to a class variable. This method simply
   * returns a stored class variable. If any customization is necessary, use
   * {@link #addGetter(String, String, String)
   * 
   * @param type
   *          Return type for the getter
   * @param attribute
   *          Field name
   */
  protected void addGetter(String type, String attribute)
  {
    addGetter(type, attribute, attribute);
  }

  /**
   * Generates a getter for access to a class variable. Allows for customization
   * of the return statement.
   * 
   * @param type
   *          Return type for the getter
   * @param attribute
   *          Field name
   * @param customReturn
   *          Customized return statement
   */
  protected void addGetter(String type, String attribute, String customReturn)
  {
    getWriter().writeLine("public " + type + " get" + upperFirstCharacter(attribute) + "()");
    getWriter().openBracket();
    getWriter().writeLine("loadEnumeration();");
    getWriter().writeLine("return enumeration.get" + upperFirstCharacter(attribute) + "();");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Writes the dereference method, which takes a String OID as a parameter and
   * returns the associated enum item, or <code>null</code> if the oid isn't
   * recognized
   * 
   * @param name
   *          Name of the enum
   */
  protected void addGet(String name)
  {
    getWriter().writeLine("public static " + name + " get(String oid)");
    getWriter().openBracket();
    getWriter().writeLine("for (" + name + " e : " + name + ".values())");
    getWriter().openBracket();
    getWriter().writeLine("if (e.getOid().equals(oid))");
    getWriter().openBracket();
    getWriter().writeLine("return e;");
    getWriter().closeBracket();
    getWriter().closeBracket();
    getWriter().writeLine("return null;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Simple helper method that capitalizes the first character in a String
   * 
   * @param string
   * @return
   */
  protected String upperFirstCharacter(String string)
  {
    return string.substring(0, 1).toUpperCase() + string.substring(1);
  }

  @Override
  public String getClassAttribute()
  {
    return "";
  }

  @Override
  public String getSourceAttribute()
  {
    return "";
  }
}
