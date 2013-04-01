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
package com.runwaysdk.business.generation.json;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.EnumDTOInfo;
import com.runwaysdk.constants.EnumerationDTOInfo;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.session.Session;
import com.runwaysdk.transport.conversion.ConversionException;

public class EnumerationJSGenerator extends TypeJSGenerator
{
  /**
   * Constructor
   *
   * @param mdEnum
   */
  public EnumerationJSGenerator(String sessionId, MdEnumerationDAOIF mdEnum)
  {
    super(sessionId, mdEnum);
  }

  @Override
  protected MdEnumerationDAOIF getMdTypeIF()
  {
    return (MdEnumerationDAOIF) super.getMdTypeIF();
  }

  @Override
  protected List<Declaration> getInstanceMethods()
  {
    List<Declaration> instances = new LinkedList<Declaration>();

    Declaration item = this.newDeclaration();

    MdEnumerationDAOIF mdEnum = getMdTypeIF();
    item.writeln("item : function(clientRequest)");
    item.openBracketLn();
    item.writeln(JSON.RUNWAY_FACADE.getLabel()+".getEnumeration(clientRequest, '"+mdEnum.definesType()+"', this.name());");
    item.closeBracket();

    instances.add(item);

    return instances;
  }

  @Override
  protected String getParent()
  {
    return EnumerationDTOInfo.CLASS;
  }

  @Override
  protected List<Declaration> getStaticMethods()
  {
    List<Declaration> statics = new LinkedList<Declaration>();

    MdEnumerationDAOIF mdEnum = getMdTypeIF();

    // values() method to return all enum items.
    Declaration values = this.newDeclaration();
    values.writeln("values : function()");
    values.openBracketLn();
    values.writeln("var values = [];");
    for(BusinessDAOIF item : mdEnum.getAllEnumItemsOrdered())
    {
      values.writeln("values.push("+getType()+"."+item.getValue(EnumerationMasterInfo.NAME)+");");
    }
    values.writeln("return values;");
    values.closeBracket();

    statics.add(values);

    // get method to return the enum based on its name
    Declaration get = this.newDeclaration();

    get.writeln("get : function(name)");
    get.openBracketLn();
    get.writeln("return "+getType()+"[name];");
    get.closeBracket();

    statics.add(get);

    return statics;
  }

  /**
   * Writes the enum items.
   */
  @Override
  protected void postProcess(Writer writer)
  {
    MdEnumerationDAOIF mdEnum = getMdTypeIF();

    for(BusinessDAOIF item : mdEnum.getAllEnumItemsOrdered())
    {
      EnumerationItemDAO itemDAO = (EnumerationItemDAO) item;

      String name = itemDAO.getValue(EnumerationMasterInfo.NAME);

      // create an EnumDTO to wrap the enum item
      JSONObject obj = new JSONObject();
      try
      {
        String displayLabel = itemDAO.getLocalValue(EnumerationMasterInfo.DISPLAY_LABEL, Session.getCurrentLocale());

        obj.put(JSON.ENUMERATION_DTO_IF_NAME.getLabel(), name);
        obj.put(JSON.DTO_TYPE.getLabel(), EnumDTOInfo.CLASS);
        obj.put(JSON.ENUM_DTO_TYPE.getLabel(), mdEnum.definesType());
        obj.put(JSON.ENUM_DTO_DISPLAY_LABEL.getLabel(), displayLabel);
      }
      catch (JSONException e)
      {
        String error = "The enum name ["+name+"] could not be added to the MdEnumeration ["+getType()+"] for Javascript.";
        throw new ConversionException(error);
      }

      writer.writeln(getType()+"['"+item.getValue(EnumerationMasterInfo.NAME) + "'] = (new "+getType()+"("+obj.toString()+"));");

    }

  }

  @Override
  protected String getClassName()
  {
    return getMdTypeIF().definesType();
  }

  @Override
  protected List<Declaration> getConstants()
  {
    return null;
  }

  @Override
  protected boolean isAbstract()
  {
    return false;
  }
}
