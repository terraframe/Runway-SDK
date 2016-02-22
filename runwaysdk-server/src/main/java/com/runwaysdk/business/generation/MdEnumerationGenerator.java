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
package com.runwaysdk.business.generation;

import java.util.Map;
import java.util.TreeMap;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessEnumeration;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.metadata.ForbiddenMethodException;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.EnumerationMaster;

/**
 * Generates Java5 enum source code that represents an MdEnumeration. All items
 * for the MdEnumeration are generated, including attributes on the items.
 * Attribute values are hard-coded, so enums are re-generated when changes are
 * made to items.
 *
 * @author Eric Grunzke
 */
public class MdEnumerationGenerator extends Java5EnumGenerator implements ServerMarker
{
  /**
   * The EnumerationMaster class. Instances of this class are items on the
   * enumeration.
   */
  protected MdBusinessDAOIF mdBusinessIF;

  /**
   * Sets up class variables. This constructor is protected only to allow
   * visibility to extending classes. Clients should call
   * {@link #generateEnum(MdEnumerationDAOIF, boolean)} instead of instantiating
   * this directly.
   *
   * @param mdEnumerationIF
   *          The enum that will be generated
   */
  public MdEnumerationGenerator(MdEnumerationDAOIF mdEnum)
  {
    super(mdEnum, mdEnum.getTypeName());

    this.mdBusinessIF = mdEnum.getMasterListMdBusinessDAO();

    if (!mdBusinessIF.getValue(MdBusinessInfo.SUPER_MD_BUSINESS).equals(EnumerationMasterInfo.ID_VALUE))
    {
      String error = "EnumerationGenerator was invoked for mdbusiness [" + mdBusinessIF.definesType() + "] which does not extend EnumerationMaster";
      throw new ForbiddenMethodException(error);
    }
  }

  @Override
  protected MdEnumerationDAOIF getMdTypeDAOIF()
  {
    return (MdEnumerationDAOIF) super.getMdTypeDAOIF();
  }

  private String getMasterType()
  {
    if (this.mdBusinessIF.isGenerateSource())
    {
      return mdBusinessIF.definesType();
    }

    return EnumerationMaster.class.getName();
  }

  /**
   * The primary driver method, go() calls all of the methods needed to actually
   * generate the .java file
   */
  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business
    // classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    // Do regenerate if the existing file is symantically the same
    if (LocalProperties.isKeepBaseSource() && AbstractGenerator.hashEquals(this.getSerialVersionUID(), this.getPath()))
    {
      return;
    }

    addPackage(this.getMdTypeDAOIF().getPackage());
    addSignatureAnnotation();
    addEnumName(this.getMdTypeDAOIF().getEnumerationName());
    addEnumItems();
    addFields();
    addLoadEnumeration();
    addSetEnumeration();
    addGetters();
    addGet(this.getMdTypeDAOIF().getEnumerationName());
    getWriter().closeBracket();
    getWriter().close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("ENUM", this.getSignature());
  }

  /**
   * Overrides {@link Java5EnumGenerator#addEnumName(String)} to add the
   * <code>implements</code> statement
   */
  protected void addEnumName(String enumName)
  {
    getWriter().write("public enum " + enumName + " implements " + BusinessEnumeration.class.getName());

    if (!this.getMdTypeDAOIF().isSystemPackage())
    {
      getWriter().write(", " + Reloadable.class.getName());
    }
    getWriter().writeLine("");

    getWriter().openBracket();
  }

  /**
   * Writes the enum items, which includes all of the attribtues on each item
   */
  protected void addEnumItems()
  {
    Map<String, String> map = new TreeMap<String, String>();

    for (BusinessDAOIF item : this.getMdTypeDAOIF().getAllEnumItemsOrdered())
    {
      map.put(item.getValue(EnumerationMasterInfo.NAME), new String());
    }

    writeEnumItems(map);
  }

  /**
   * Each attribute in an enumeration item is stored as a private class
   * variable. This method writes the declarations of those variables.
   */
  protected void addFields()
  {
    getWriter().writeLine("public static final " + String.class.getName() + " CLASS = \"" + this.getMdTypeDAOIF().definesType() + "\";");
    addField(this.getMasterType(), "enumeration");
  }

  protected void addLoadEnumeration()
  {
    String type = this.getMasterType();

    getWriter().writeLine("private synchronized void loadEnumeration()");
    getWriter().openBracket();

    if (this.mdBusinessIF.isGenerateSource())
    {
      getWriter().writeLine(type + " enu = " + type + ".getEnumeration(this.name());");
    }
    else
    {
      getWriter().writeLine(type + " enu = (" + type + ") " + Business.class.getName() + ".getEnumeration(\"" + this.mdBusinessIF.definesType() + "\", this.name());");
    }

    getWriter().writeLine("setEnumeration(enu);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  private void addSetEnumeration()
  {
    getWriter().writeLine("private synchronized void setEnumeration(" + this.getMasterType() + " enumeration)");
    getWriter().openBracket();
    getWriter().writeLine("this.enumeration = enumeration;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates the getters for each attribute on an item
   */
  protected void addGetters()
  {
    for (MdAttributeDAOIF attribute : mdBusinessIF.definesAttributesOrdered())
    {
      if (!GenerationUtil.isSpecialCaseSetter(attribute))
      {
        addGetter(attribute);
      }
    }

    addGetter(String.class.getName(), ComponentInfo.ID);
    addGetter(String.class.getName(), EnumerationMasterInfo.NAME);
    addDisplayLabelGetter();
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
  protected void addDisplayLabelGetter()
  {
    getWriter().writeLine("public " + String.class.getName() + " get" + upperFirstCharacter(EnumerationMasterInfo.DISPLAY_LABEL) + "()");
    getWriter().openBracket();
    getWriter().writeLine("loadEnumeration();");
    getWriter().writeLine("return enumeration.get" + upperFirstCharacter(EnumerationMasterInfo.DISPLAY_LABEL) + "().getValue(" + Session.class.getName() + ".getCurrentLocale());");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates the getter for an attribute
   *
   * @param attribute
   */
  protected void addGetter(MdAttributeDAOIF attribute)
  {
    addGetter(attribute.javaType(false), attribute.definesAttribute());
  }

  @Override
  protected String getRootSourceDirectory()
  {
    String pack = CommonGenerationUtil.replacePackageDotsWithSlashes(this.getMdTypeDAOIF().getPackage());
    return AbstractServerGenerator.getRootServerStubDirectory(pack);
  }

  @Override
  protected String getRootClassDirectory()
  {
    return AbstractServerGenerator.getRootServerBinDirectory(getPackage());
  }
}
