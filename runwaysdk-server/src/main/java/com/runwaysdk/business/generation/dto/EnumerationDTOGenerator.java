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

import java.util.Map;
import java.util.TreeMap;



import com.runwaysdk.business.EnumerationDTOIF;
import com.runwaysdk.business.generation.AbstractClientGenerator;
import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.business.generation.ClientMarker;
import com.runwaysdk.business.generation.Java5EnumGenerator;
import com.runwaysdk.business.generation.MdEnumerationGenerator;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.metadata.ForbiddenMethodException;
import com.runwaysdk.system.EnumerationMasterDTO;

/**
 * EnumerationDTOs differ only slightly from regular enums. This subclass of the
 * regular {@link MdEnumerationGenerator} changes the type name by adding the
 * suffix 'DTO'.
 *
 * @author Eric Grunzke
 */
public class EnumerationDTOGenerator extends Java5EnumGenerator implements ClientMarker
{
  private static String getGeneratedName(MdEnumerationDAOIF mdEnum)
  {
    return mdEnum.getEnumerationName() + TypeGeneratorInfo.DTO_SUFFIX;
  }

  protected String getRootSourceDirectory()
  {
    return AbstractClientGenerator.getRootClientOrCommonStubDirectory(this.getMdTypeDAOIF());
  }

  @Override
  protected String getRootClassDirectory()
  {
    return AbstractClientGenerator.getRootClientBinDirectory(this.getPackage());
  }

  /**
   * Simple private constructor.
   *
   * @param mdEnumeration
   *          Enum that's being generated
   */

  /**
   * The EnumerationMaster class. Instances of this class are items on the
   * enumeration.
   */
  protected MdBusinessDAOIF mdBusinessIF;

  public EnumerationDTOGenerator(MdEnumerationDAOIF mdEnum)
  {
    super(mdEnum, getGeneratedName(mdEnum));

    this.mdBusinessIF = mdEnum.getMasterListMdBusinessDAO();

    if (!this.mdBusinessIF.getValue(MdBusinessInfo.SUPER_MD_BUSINESS).equals(EnumerationMasterInfo.ID_VALUE))
    {
      String error = "EnumerationGenerator was invoked for MdBusiness [" + this.mdBusinessIF.definesType() + "] which does not extend EnumerationMaster";
      throw new ForbiddenMethodException(error);
    }
  }

  @Override
  protected MdEnumerationDAOIF getMdTypeDAOIF()
  {
    return (MdEnumerationDAOIF) super.getMdTypeDAOIF();
  }

  /**
   * Overrides {@link MdEnumerationGenerator#go()} in order to pass the correct
   * typename (with the 'DTO' suffix) to the appropriate methods.
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
    addEnumName();
    addEnumItems();
    addEnumClass();

    addItem();
    addItems();
    addAllItems();

    addGetName();

    getWriter().closeBracket();
    getWriter().close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("ENUM_DTO", this.getSignature());
  }

  private String getMasterTypeSuffix()
  {
    if (this.mdBusinessIF.isGenerateSource())
    {
      return this.mdBusinessIF.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    }
    else
    {
      return EnumerationMasterDTO.class.getName();
    }
  }

  protected void addEnumClass()
  {
    getWriter().writeLine("public final static String CLASS = \"" + this.getMdTypeDAOIF().definesType() + "\";");
    getWriter().writeLine("");
  }

  /**
   * Overrides {@link MdEnumerationGenerator#addEnumName(String)} in order write
   * the correct typename (with the 'DTO' suffix) and implement the correct
   * interface.
   */
  protected void addEnumName()
  {
    getWriter().write("public enum " + getGeneratedName(this.getMdTypeDAOIF()) + " implements " + EnumerationDTOIF.class.getName());

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

  private void addItem()
  {
    String dtoType = this.getMasterTypeSuffix();

    getWriter().writeLine("");
    getWriter().writeLine("public " + this.getMasterTypeSuffix() + " item(" + ClientRequestIF.class.getName() + " clientRequest)");
    getWriter().openBracket();
    getWriter().writeLine("return (" + dtoType + ") clientRequest.getEnumeration(" + this.getMdTypeDAOIF().definesType() + TypeGeneratorInfo.DTO_SUFFIX + ".CLASS, this.name());");
    getWriter().closeBracket();
  }

  private void addAllItems()
  {
    String dtoType = this.getMasterTypeSuffix();

    getWriter().writeLine("");
    getWriter().writeLine("@java.lang.SuppressWarnings(\"unchecked\")");
    getWriter().writeLine("public static java.util.List<" + this.getMasterTypeSuffix() + "> allItems(" + ClientRequestIF.class.getName() + " clientRequest)");
    getWriter().openBracket();
    getWriter().writeLine("return (java.util.List<" + dtoType + ">) clientRequest.getAllEnumerations(" + this.getMdTypeDAOIF().definesType() + TypeGeneratorInfo.DTO_SUFFIX + ".CLASS);");
    getWriter().closeBracket();
  }

  private void addItems()
  {
    String dtoType = this.getMasterTypeSuffix();

    getWriter().writeLine("");
    getWriter().writeLine("@java.lang.SuppressWarnings(\"unchecked\")");
    getWriter().writeLine("public static java.util.List<" + this.getMasterTypeSuffix() + "> items(" + ClientRequestIF.class.getName() + " clientRequest, " + getGeneratedName(this.getMdTypeDAOIF()) + " ... items)");
    getWriter().openBracket();
    getWriter().writeLine("java.lang.String[] itemNames = new java.lang.String[items.length];");
    getWriter().writeLine("for(int i=0; i<items.length; i++)");
    getWriter().openBracket();
    getWriter().writeLine("itemNames[i] = items[i].name();");
    getWriter().closeBracket();
    getWriter().writeLine("return (java.util.List<" + dtoType + ">) clientRequest.getEnumerations(" + this.getMdTypeDAOIF().definesType() + TypeGeneratorInfo.DTO_SUFFIX + ".CLASS, itemNames);");
    getWriter().closeBracket();
  }

  private void addGetName()
  {
    getWriter().writeLine("");
    getWriter().writeLine("public java.lang.String getName()");
    getWriter().openBracket();
    getWriter().writeLine("return this.name();");
    getWriter().closeBracket();
  }

  /**
   * No longer generating getters for all of the attributes on the enumration
   * master list type. Instead, call the method that returns the BusinessDTO
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
  }

}
