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
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

/**
 * Handles enumeration_filter and children tags, see datatype.xml as a reference
 *
 * @author Justin Smethie
 * @date 6/02/06
 */
public class MdEnumerationHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  private static class IncludeAllHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public IncludeAllHandler(ImportManager manager)
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
      MdEnumerationDAO mdEnumeration = (MdEnumerationDAO) context.getObject(MdTypeInfo.CLASS);
      mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
      mdEnumeration.apply();
    }
  }

  private static class AddEnumItemHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AddEnumItemHandler(ImportManager manager)
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
      MdEnumerationDAO mdEnumeration = (MdEnumerationDAO) context.getObject(MdTypeInfo.CLASS);

      if (MdAttributeBooleanInfo.TRUE.equals(mdEnumeration.getValue(MdEnumerationInfo.INCLUDE_ALL)))
      {
        mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.FALSE);
        mdEnumeration.apply();
      }

      String masterListType = mdEnumeration.getMasterListMdBusinessDAO().definesType();
      String enumName = attributes.getValue(XMLTags.ENUM_NAME_ATTRIBUTE);
      String enumItemKey = EnumerationItemDAO.buildKey(masterListType, enumName);

      String actualId = "";

      try
      {
        actualId = EntityDAO.getOidFromKey(masterListType, enumItemKey);
      }
      catch (DataNotFoundException e)
      {
        String[] search_tags = { XMLTags.OBJECT_TAG };
        // SearchHandler.searchEntity(manager, search_tags, XMLTags.KEY_ATTRIBUTE, masterListType, enumItemKey);
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.KEY_ATTRIBUTE, enumItemKey, mdEnumeration.definesType());
      }

      if (actualId.equals(""))
      {
        actualId = EntityDAO.getOidFromKey(masterListType, enumItemKey);
      }

      if (!mdEnumeration.containsEnumItem(actualId))
      {
        mdEnumeration.addEnumItem(actualId);
      }
    }
  }

  private static class RemoveEnumItemHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public RemoveEnumItemHandler(ImportManager manager)
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
      MdEnumerationDAO mdEnumeration = (MdEnumerationDAO) context.getObject(MdTypeInfo.CLASS);

      if (MdAttributeBooleanInfo.TRUE.equals(mdEnumeration.getValue(MdEnumerationInfo.INCLUDE_ALL)))
      {
        mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.FALSE);
        mdEnumeration.apply();
      }

      String masterListType = mdEnumeration.getMasterListMdBusinessDAO().definesType();
      String enumName = attributes.getValue(XMLTags.ENUM_NAME_ATTRIBUTE);
      String enumItemKey = EnumerationItemDAO.buildKey(masterListType, enumName);

      // Get the database OID of a XML puesdo oid
      String actualId = EntityDAO.getOidFromKey(masterListType, enumItemKey);

      if (mdEnumeration.containsEnumItem(actualId))
      {
        mdEnumeration.removeEnumItem(actualId);
      }
    }
  }

  /**
   * @param manager
   *          TODO
   */
  public MdEnumerationHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.INCLUDEALL_TAG, new IncludeAllHandler(manager));
    this.addHandler(XMLTags.ADD_ENUM_ITEM_TAG, new AddEnumItemHandler(manager));
    this.addHandler(XMLTags.REMOVE_ENUM_ITEM_TAG, new RemoveEnumItemHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory#supports(com.runwaysdk.dataaccess.io.dataDefinition.TagContext, java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String localName)
  {
    MdEnumerationDAO mdEnumeration = (MdEnumerationDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdEnumeration != null && this.getManager().isCreated(mdEnumeration.definesType()))
    {
      return false;
    }

    return super.supports(context, localName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    // Get the MdEnumeration to import, if this is a create then a new instance of MdEnumeration is imported
    MdEnumerationDAO mdEnumeration = (MdEnumerationDAO) this.getManager().getEntityDAO(MdEnumerationInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();
    // Import required attributes
    String enumType = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdEnumeration.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(enumType));
    mdEnumeration.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(enumType));

    ImportManager.setLocalizedValue(mdEnumeration, MdTypeInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdEnumeration, MetadataInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdEnumeration, MetadataInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdEnumeration, MdEnumerationInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);

    String tableName = attributes.getValue(XMLTags.ENUMERATION_TABLE);
    if (tableName != null)
    {
      mdEnumeration.setTableName(tableName);
    }

    // Import required reference attributes
    String masterType = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

    if (masterType != null)
    {
      // Make sure that the class being reference has already been defined
      if (!MdTypeDAO.isDefined(masterType))
      {
        // Check if the class is defined later in the xml document
        String[] search_tags = { XMLTags.ENUMERATION_MASTER_TAG };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, masterType, mdEnumeration.definesType());
      }

      MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(masterType);
      mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, mdBusinessIF.getOid());
    }

    if (!this.getManager().isCreated(mdEnumeration.definesType()))
    {
      mdEnumeration.apply();
      this.getManager().addMapping(mdEnumeration.definesType(), mdEnumeration.getOid());
    }

    context.setObject(MdTypeInfo.CLASS, mdEnumeration);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    if (localName.equals(XMLTags.MD_ENUMERATION_TAG))
    {
      MdEnumerationDAO mdEnumeration = (MdEnumerationDAO) context.getObject(MdTypeInfo.CLASS);

      this.getManager().endImport(mdEnumeration.definesType());
    }
  }
}
