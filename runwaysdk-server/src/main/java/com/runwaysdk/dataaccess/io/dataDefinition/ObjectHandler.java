/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

/**
 * Parses instance tags and adds instances of a defined Class to the database
 * 
 * @author Justin Smethie
 * @date 6/02/06
 */
public class ObjectHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public ObjectHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.ATTRIBUTE_TAG, new AttributeHandler(manager));
    this.addHandler(XMLTags.ATTRIBUTE_REFERENCE_TAG, new AttributeReferenceHandler(manager));
    this.addHandler(XMLTags.ATTRIBUTE_ENUMERATION_TAG, new AttributeEnumerationHandler(manager));
    this.addHandler(XMLTags.ATTRIBUTE_STRUCT_TAG, new StructAttributeHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    String key = attributes.getValue(XMLTags.KEY_ATTRIBUTE);
    String type = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

    // Ensure that the class being referenced has already been defined
    if (!MdTypeDAO.isDefined(type))
    {
      String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG, XMLTags.ENUMERATION_MASTER_TAG, XMLTags.MD_STRUCT_TAG, XMLTags.MD_LOCAL_STRUCT_TAG };

      SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, type, key);
    }

    EntityDAO entity = this.getManager().getEntityDAO(type, key).getEntityDAO();
    // this.addHandler(XMLTags.ATTRIBUTE_ENUMERATION_TAG, new AttributeEnumerationHandler(attributes, reader, this, manager, current));

    String newKey = attributes.getValue(XMLTags.NEW_KEY_ATTRIBUTE);

    if (newKey != null)
    {
      key = newKey;
    }

    entity.setKey(key);

    context.setObject(EntityInfo.CLASS, entity);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    if (localName.equals(XMLTags.OBJECT_TAG))
    {
      EntityDAO object = (EntityDAO) context.getObject(EntityInfo.CLASS);

      // Ensure that the instance has not already been added into the database
      if (this.getManager().isCreateState())
      {
        try
        {
          EntityDAO.getIdFromKey(object.getType(), object.getKey());
        }
        catch (DataNotFoundException e)
        {
          object.apply();

          this.getManager().addImportedObject(object.getId());
        }
      }
      else
      {
        object.apply();

        this.getManager().addImportedObject(object.getId());
      }
    }
  }
}
