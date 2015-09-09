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
/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdFieldInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;

public class FieldConditionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public static class NoneConditionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public NoneConditionHandler(ImportManager manager)
    {
      super(manager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      ConditionListIF conditions = (ConditionListIF) context.getObject(ConditionListIF.CONDITIONS);
      MdFieldDAO mdField = (MdFieldDAO) context.getObject(MdFieldInfo.CLASS);

      conditions.addCondition(new NoneConditionAttribute(mdField));
    }
  }

  public static class ConditionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public ConditionHandler(ImportManager manager)
    {
      super(manager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      ConditionListIF conditions = (ConditionListIF) context.getObject(ConditionListIF.CONDITIONS);
      MdFieldDAO mdField = (MdFieldDAO) context.getObject(MdFieldInfo.CLASS);
      MdWebFormDAO mdForm = (MdWebFormDAO) context.getObject(MdTypeInfo.CLASS);

      conditions.addCondition(new ConditionAttribute(localName, attributes, mdForm, mdField));
    }
  }

  public static class AndConditionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AndConditionHandler(ImportManager manager)
    {
      super(manager);
      
      this.addHandler(XMLTags.FIRST_CONDITION_TAG, new FieldConditionHandler(manager, this));
      this.addHandler(XMLTags.SECOND_CONDITION_TAG, new FieldConditionHandler(manager, this));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      ConditionListIF conditions = (ConditionListIF) context.getObject(ConditionListIF.CONDITIONS);
      MdFieldDAO mdField = (MdFieldDAO) context.getObject(MdFieldInfo.CLASS);

      CompositeConditionAttribute condition = new CompositeConditionAttribute(localName, mdField);

      conditions.addCondition(condition);

      context.setObject(ConditionListIF.CONDITIONS, condition);
      context.setObject(MdFieldInfo.CLASS, null);
    }
  }

  public FieldConditionHandler(ImportManager manager)
  {
    this(manager, null);
  }
  
  public FieldConditionHandler(ImportManager manager, AndConditionHandler handler)
  {
    super(manager);
    
    this.addHandler(XMLTags.NONE_TAG, new NoneConditionHandler(manager));
    this.addHandler(XMLTags.DATE_TAG, new ConditionHandler(manager));
    this.addHandler(XMLTags.CHARACTER_TAG, new ConditionHandler(manager));
    this.addHandler(XMLTags.DOUBLE_TAG, new ConditionHandler(manager));
    this.addHandler(XMLTags.LONG_TAG, new ConditionHandler(manager));
    
    if(handler == null)
    {
      this.addHandler(XMLTags.AND_TAG, new AndConditionHandler(manager));
    }
    else
    {
      this.addHandler(XMLTags.AND_TAG, handler);      
    }
    
  }
}
