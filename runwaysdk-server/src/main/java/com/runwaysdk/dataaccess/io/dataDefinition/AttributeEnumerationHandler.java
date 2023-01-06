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

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.attributes.InvalidAttributeTypeException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;

/**
 * Parses selection tags to instianted an attribute with multiple reference to other instances
 * 
 * @author Justin
 * @date 06/09/06
 */
public class AttributeEnumerationHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  private static class EnumeratedItemHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public EnumeratedItemHandler(ImportManager manager)
    {
      super(manager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onStartElement(String qName, Attributes attributes, TagContext context)
    {
      String attributeName = (String) context.getObject(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);
      String structAttributeName = (String) context.getObject(StructAttributeHandler.STRUCT_ATTRIBUTE_NAME);
      EntityDAO entity = (EntityDAO) context.getObject(EntityInfo.CLASS);
      MdBusinessDAOIF mdEnumerationMaster = (MdBusinessDAOIF) context.getObject(MdEnumerationInfo.MASTER_MD_BUSINESS);

      String masterListType = mdEnumerationMaster.definesType();
      String enumName = attributes.getValue(XMLTags.ENUM_NAME_ATTRIBUTE);
      String enumItemKey = EnumerationItemDAO.buildKey(masterListType, enumName);

      String dataID = "";

      try
      {
        dataID = EntityDAO.getOidFromKey(masterListType, enumItemKey);
      }
      catch (DataNotFoundException e)
      {
        // Define the instance
        String[] search_tags = { XMLTags.OBJECT_TAG };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.KEY_ATTRIBUTE, enumItemKey, entity.getKey());
      }

      if (dataID.equals(""))
      {
        dataID = EntityDAO.getOidFromKey(masterListType, enumItemKey);
      }

      if (structAttributeName != null)
      {
        entity.addStructItem(structAttributeName, attributeName, dataID);
      }
      else
      {
        entity.addItem(attributeName, dataID);
      }
    }
  }

  public AttributeEnumerationHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.ENUMERATED_ITEM_TAG, new EnumeratedItemHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String qName, Attributes attributes, TagContext context)
  {
    String attributeName = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);

    EntityDAO entity = (EntityDAO) context.getObject(EntityInfo.CLASS);

    MdAttributeDAOIF mdAttribute = entity.getAttributeIF(attributeName).getMdAttribute();

    if (! ( mdAttribute instanceof MdAttributeEnumerationDAOIF ))
    {
      String errMsg = "The attribute [" + attributeName + "] on type [" + entity.getType() + "] is not an enumeration attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeReferenceInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdEnumerationDAOIF mdEnumeration = MdEnumerationDAO.get(mdAttribute.getValue(MdAttributeEnumerationInfo.MD_ENUMERATION));
    MdBusinessDAOIF enumerationMaster = mdEnumeration.getMasterListMdBusinessDAO();

    context.setObject(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE, attributeName);
    context.setObject(MdEnumerationInfo.MASTER_MD_BUSINESS, enumerationMaster);
  }
}
