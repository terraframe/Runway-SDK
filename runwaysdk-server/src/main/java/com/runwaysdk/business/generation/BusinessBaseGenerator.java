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

import com.runwaysdk.business.Business;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdGraphDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.query.OIterator;

/**
 * Generates .java files to represent {@link MdBusinessDAO} objects. Two files
 * are created, Type.java and TypeBase.java - the base file includes all getters
 * and setters. The plain (or stub) file extends the base and therefore inherits
 * all of the functionality. Custom business logic should be added to the stub
 * file, which does not need to be regenereated if a change occurs to the type.
 *
 *
 * !IMPORTANT! If you're changing the way base classes are generated then its
 * probably a good time to add a generation version number to the class
 * signature. The reason is because you must regenerate all the base classes of
 * applications that depend on runway (even though the metadata may not have
 * changed). If you don't regenerate these base classes, then the app can break
 * at runtime if the generated file is different than what it was copiled
 * against. See DDMS ticket #3298 * !IMPORTANT!
 *
 *
 * @author Eric G
 */
public class BusinessBaseGenerator extends ElementBaseGenerator
{

  public BusinessBaseGenerator(MdBusinessDAOIF mdBusinessIF)
  {
    super(mdBusinessIF);
  }

  @Override
  protected String getExtends(MdClassDAOIF parent)
  {
    if (parent == null)
    {
      return Business.class.getName();
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
    addStructInitializers();
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  protected void addMethods()
  {
    super.addMethods();

    addAddChildMethods();

    addAddParentMethods();

    getInstance();

    addGetEnumerationMethod();
  }

  private void getInstance()
  {
    // get for the java class
    getWriter().writeLine("public static " + this.getSubClassName() + " get(String oid)");
    getWriter().openBracket();
    getWriter().writeLine("return (" + this.getSubClassName() + ") " + BusinessInfo.CLASS + ".get(oid);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public static " + this.getSubClassName() + " getByKey(String key)");
    getWriter().openBracket();
    getWriter().writeLine("return (" + this.getSubClassName() + ") " + BusinessInfo.CLASS + ".get(CLASS, key);");
    getWriter().closeBracket();
    getWriter().writeLine("");

  }

  private void addGetEnumerationMethod()
  {
    // getEnum for the java class if its an EnumerationMaster
    MdClassDAOIF superClass = this.getMdTypeDAOIF().getSuperClass();

    if (superClass != null && superClass.definesType().equals(EnumerationMasterInfo.CLASS))
    {
      getWriter().writeLine("public static " + this.getSubClassName() + " getEnumeration(String enumName)");
      getWriter().openBracket();
      getWriter().writeLine("return (" + this.getSubClassName() + ") " + BusinessInfo.CLASS + ".getEnumeration(" + this.getMdTypeDAOIF().definesType() + ".CLASS ,enumName);");
      getWriter().closeBracket();
      getWriter().writeLine("");
    }
  }

  public String getRelationshipClass(MdRelationshipDAOIF mdRelationship)
  {
    if (mdRelationship.isGenerateSource())
    {
      return mdRelationship.definesType() + ".CLASS";
    }

    // A source class doesn't exist to reference so we must return the hardcoded
    // string of the relationship type instead of referencing the CLASS variable
    return "\"" + mdRelationship.definesType() + "\"";
  }

  private void addAddChildMethods()
  {
    MdBusinessDAOIF mdBusinessDAOIF = (MdBusinessDAOIF) this.getMdTypeDAOIF();

    for (MdRelationshipDAOIF mdRelationship : mdBusinessDAOIF.getParentMdRelationshipsOrdered())
    {
      VisibilityModifier visibility = mdRelationship.getChildVisibility();

      MdBusinessDAOIF childMdBusiness = mdRelationship.getChildMdBusiness();

      if (GenerationUtil.isHardcodedType(childMdBusiness))
      {
        continue;
      }

      String childType = this.getBusinessType(childMdBusiness);
      String lowercase = CommonGenerationUtil.lowerFirstCharacter(childMdBusiness.getTypeName());
      String childMethod = mdRelationship.getChildMethod();

      String relType = this.getRelationshipType(mdRelationship);
      String relTypeClass = this.getRelationshipClass(mdRelationship);

      // Add a new Relationship of this type
      getWriter().writeLine(visibility.getJavaModifier() + " " + relType + " add" + childMethod + "(" + childType + " " + lowercase + ")");
      getWriter().openBracket();
      getWriter().writeLine("return (" + relType + ") addChild(" + lowercase + ", " + relTypeClass + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Remove Relationship(s) with a specific child
      getWriter().writeLine(visibility.getJavaModifier() + " void remove" + childMethod + "(" + childType + " " + lowercase + ")");
      getWriter().openBracket();
      getWriter().writeLine("removeAllChildren(" + lowercase + ", " + relTypeClass + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Get all children (including duplicates) that have a realtionship with
      // this parent
      getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
      getWriter().writeLine(visibility.getJavaModifier() + " " + OIterator.class.getName() + "<? extends " + childType + "> getAll" + childMethod + "()");
      getWriter().openBracket();
      getWriter().writeLine("return (" + OIterator.class.getName() + "<? extends " + childType + ">) getChildren(" + relTypeClass + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Get all Relationships of this specific type
      getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
      getWriter().writeLine(visibility.getJavaModifier() + " " + OIterator.class.getName() + "<? extends " + relType + "> getAll" + childMethod + "Rel()");
      getWriter().openBracket();
      getWriter().writeLine("return (" + OIterator.class.getName() + "<? extends " + relType + ">) getChildRelationships(" + relTypeClass + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Get Relationship(s) with a specific parent
      // Graphs and Trees should return one object, not a list
      if (mdRelationship instanceof MdGraphDAOIF)
      {
        getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
        getWriter().writeLine(visibility.getJavaModifier() + " " + relType + " get" + childMethod + "Rel(" + childType + " " + lowercase + ")");
        getWriter().openBracket();
        getWriter().writeLine(OIterator.class.getName() + "<? extends " + relType + "> iterator = (" + OIterator.class.getName() + "<? extends " + relType + ">) getRelationshipsWithChild(" + lowercase + ", " + relTypeClass + ");");
        getWriter().writeLine("try");
        getWriter().openBracket();
        getWriter().writeLine("if (iterator.hasNext())");
        getWriter().writeLine("{");
        getWriter().writeLine("  return iterator.next();");
        getWriter().writeLine("}");
        getWriter().writeLine("else");
        getWriter().writeLine("{");
        getWriter().writeLine("  return null;");
        getWriter().writeLine("}");
        getWriter().closeBracket();
        getWriter().writeLine("finally");
        getWriter().openBracket();
        getWriter().writeLine("iterator.close();");
        getWriter().closeBracket();
        getWriter().closeBracket();
        getWriter().writeLine("");
      }
      else
      {
        getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
        getWriter().writeLine(visibility.getJavaModifier() + " " + OIterator.class.getName() + "<? extends " + relType + "> get" + childMethod + "Rel(" + childType + " " + lowercase + ")");
        getWriter().openBracket();
        getWriter().writeLine("return (" + OIterator.class.getName() + "<? extends " + relType + ">) getRelationshipsWithChild(" + lowercase + ", " + relTypeClass + ");");
        getWriter().closeBracket();
        getWriter().writeLine("");
      }
    }
  }

  public String getBusinessType(MdBusinessDAOIF childMdBusiness)
  {
    if (childMdBusiness.isGenerateSource())
    {
      return childMdBusiness.definesType();
    }
    else
    {
      return Business.class.getName();
    }
  }

  public String getRelationshipType(MdRelationshipDAOIF mdRelationship)
  {
    if (mdRelationship.isGenerateSource())
    {
      return mdRelationship.definesType();
    }
    else
    {
      return Relationship.class.getName();
    }
  }

  private void addAddParentMethods()
  {
    MdBusinessDAOIF mdBusinessIF = (MdBusinessDAOIF) this.getMdTypeDAOIF();

    for (MdRelationshipDAOIF mdRelationship : mdBusinessIF.getChildMdRelationshipsOrdered())
    {
      VisibilityModifier visibility = mdRelationship.getParentVisibility();

      MdBusinessDAOIF parentMdBusinessIF = mdRelationship.getParentMdBusiness();

      if (GenerationUtil.isHardcodedType(parentMdBusinessIF))
      {
        continue;
      }

      String parentType = this.getBusinessType(parentMdBusinessIF);
      String parentMethod = mdRelationship.getParentMethod();
      String lowercase = CommonGenerationUtil.lowerFirstCharacter(parentMdBusinessIF.getTypeName());
      String relType = this.getRelationshipType(mdRelationship);
      String relTypeClass = this.getRelationshipClass(mdRelationship);

      // Add a new Relationship of this type
      getWriter().writeLine(visibility.getJavaModifier() + " " + relType + " add" + parentMethod + "(" + parentType + " " + lowercase + ")");
      getWriter().openBracket();
      getWriter().writeLine("return (" + relType + ") addParent(" + lowercase + ", " + relTypeClass + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Remove Relationship(s) with a specific parent
      getWriter().writeLine(visibility.getJavaModifier() + " void remove" + parentMethod + "(" + parentType + " " + lowercase + ")");
      getWriter().openBracket();
      getWriter().writeLine("removeAllParents(" + lowercase + ", " + relTypeClass + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Get all parents (including duplicates) that have a realtionship with
      // this child
      getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
      getWriter().writeLine(visibility.getJavaModifier() + " " + OIterator.class.getName() + "<? extends " + parentType + "> getAll" + parentMethod + "()");
      getWriter().openBracket();
      getWriter().writeLine("return (" + OIterator.class.getName() + "<? extends " + parentType + ">) getParents(" + relTypeClass + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Get all Relationships of this specific type
      getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
      getWriter().writeLine(visibility.getJavaModifier() + " " + OIterator.class.getName() + "<? extends " + relType + "> getAll" + parentMethod + "Rel()");
      getWriter().openBracket();
      getWriter().writeLine("return (" + OIterator.class.getName() + "<? extends " + relType + ">) getParentRelationships(" + relTypeClass + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Get Relationship(s) with a specific parent
      // Graphs and Trees should return one object, not a list
      if (mdRelationship instanceof MdGraphDAOIF)
      {
        getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
        getWriter().writeLine(visibility.getJavaModifier() + " " + relType + " get" + parentMethod + "Rel(" + parentType + " " + lowercase + ")");
        getWriter().openBracket();
        getWriter().writeLine(OIterator.class.getName() + "<? extends " + relType + "> iterator = (" + OIterator.class.getName() + "<? extends " + relType + ">) getRelationshipsWithParent(" + lowercase + ", " + relTypeClass + ");");
        getWriter().writeLine("try");
        getWriter().openBracket();
        getWriter().writeLine("if (iterator.hasNext())");
        getWriter().writeLine("{");
        getWriter().writeLine("  return iterator.next();");
        getWriter().writeLine("}");
        getWriter().writeLine("else");
        getWriter().writeLine("{");
        getWriter().writeLine("  return null;");
        getWriter().writeLine("}");
        getWriter().closeBracket();
        getWriter().writeLine("finally");
        getWriter().openBracket();
        getWriter().writeLine("iterator.close();");
        getWriter().closeBracket();
        getWriter().closeBracket();
        getWriter().writeLine("");
      }
      else
      {
        getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
        getWriter().writeLine(visibility.getJavaModifier() + " " + OIterator.class.getName() + "<? extends " + relType + "> get" + parentMethod + "Rel(" + parentType + " " + lowercase + ")");
        getWriter().openBracket();
        getWriter().writeLine("return (" + OIterator.class.getName() + "<? extends " + relType + ">) getRelationshipsWithParent(" + lowercase + ", " + relTypeClass + ");");
        getWriter().closeBracket();
        getWriter().writeLine("");
      }
    }
  }
}
