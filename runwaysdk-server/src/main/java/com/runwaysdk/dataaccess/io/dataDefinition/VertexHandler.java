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
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

/**
 * Parses instance tags and adds instances of a defined Class to the database
 * 
 * @author Justin Smethie
 * @date 6/02/06
 */
public class VertexHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public VertexHandler(ImportManager manager)
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
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java
   * .lang.String, org.xml.sax.Attributes,
   * com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    String key = attributes.getValue(XMLTags.KEY_ATTRIBUTE);
    String type = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

    // Ensure that the class being referenced has already been defined
    if (!MdVertexDAO.isDefined(type))
    {
      String[] search_tags = { XMLTags.MD_VERTEX_TAG };

      SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, type, key);
    }

    VertexObjectDAO entity = VertexObjectDAO.newInstance(MdVertexDAO.getMdVertexDAO(type));
    // this.addHandler(XMLTags.ATTRIBUTE_ENUMERATION_TAG, new
    // AttributeEnumerationHandler(attributes, reader, this, manager, current));

    context.setObject(EntityInfo.CLASS, entity);
    context.setObject(EntityInfo.KEY, key);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onEndElement(java.
   * lang.String, java.lang.String, java.lang.String,
   * com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    if (localName.equals(XMLTags.VERTEX_TAG))
    {
      VertexObjectDAO object = (VertexObjectDAO) context.getObject(EntityInfo.CLASS);
      String key = (String) context.getObject(EntityInfo.KEY);

      if (this.getManager().getKeyMapping(key) == null)
      {

        // Ensure that the instance has not already been added into the database
        object.apply();

        this.getManager().addImportedObject(object.getOid());
        this.getManager().addKeyMapping(key, object.getOid());
      }
    }
  }
}
