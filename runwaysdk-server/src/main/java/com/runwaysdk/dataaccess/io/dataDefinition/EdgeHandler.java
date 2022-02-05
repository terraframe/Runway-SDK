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
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.EdgeObjectDAO;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;

/**
 * Imports instance of relationships
 * 
 * @author Justin Smethie
 * @date 6/13/06
 */
public class EdgeHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{

  public EdgeHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.ATTRIBUTE_TAG, new AttributeHandler(manager));
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
    String type = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);
    String key = attributes.getValue(XMLTags.KEY_ATTRIBUTE);

    EdgeObjectDAO relationship = this.createRelationship(key, type, attributes);

    context.setObject(EntityInfo.CLASS, relationship);
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
    if (localName.equals(XMLTags.EDGE_TAG))
    {
      GraphObjectDAO object = (GraphObjectDAO) context.getObject(EntityInfo.CLASS);
      object.apply();
    }
  }

  private final EdgeObjectDAO createRelationship(String key, String type, Attributes attributes)
  {
    // Ensure that the class being referenced has already been defined
    if (!MdTypeDAO.isDefined(type))
    {
      String[] search_tags = { XMLTags.MD_EDGE_TAG };
      SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, type, key);
    }

    MdEdgeDAOIF mdEdge = MdEdgeDAO.getMdEdgeDAO(type);

    MdVertexDAOIF parentMdVertex = mdEdge.getParentMdVertex();

    MdVertexDAOIF childMdVertex = mdEdge.getChildMdVertex();

    String parentKey = attributes.getValue(XMLTags.PARENT_KEY_TAG);
    String childKey = attributes.getValue(XMLTags.CHILD_KEY_TAG);

    // Get the database OID of a XML parent key
    if (this.getManager().getKeyMapping(parentKey) == null)
    {
      String[] search_tags = { XMLTags.VERTEX_TAG };

      SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.KEY_ATTRIBUTE, parentKey, key);
    }

    if (this.getManager().getKeyMapping(childKey) == null)
    {
      String[] search_tags = { XMLTags.VERTEX_TAG };

      SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.KEY_ATTRIBUTE, childKey, key);
    }

    VertexObjectDAOIF parentVertex = VertexObjectDAO.get(parentMdVertex, this.getManager().getKeyMapping(parentKey));
    VertexObjectDAOIF childVertex = VertexObjectDAO.get(childMdVertex, this.getManager().getKeyMapping(childKey));

    EdgeObjectDAO relationship = EdgeObjectDAO.newInstance(parentVertex, childVertex, type);

    return relationship;
  }
}
