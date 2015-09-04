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
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

/**
 * Imports instance of relationships
 * 
 * @author Justin Smethie
 * @date 6/13/06
 */
public class RelationshipHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{

  public RelationshipHandler(ImportManager manager)
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

    RelationshipDAO current = null;

    // Get the puesdo xml ids
    if (this.getManager().isCreateState())
    {
      current = this.createRelationship(key, type, attributes);
    }
    else if (this.getManager().isCreateOrUpdateState())
    {
      try
      {
        current = RelationshipDAO.get(type, key).getRelationshipDAO();
      }
      catch (DataNotFoundException e)
      {
        this.createRelationship(key, type, attributes);
      }
    }
    else
    {
      current = RelationshipDAO.get(type, key).getRelationshipDAO();
    }

    String newKey = attributes.getValue(XMLTags.NEW_KEY_ATTRIBUTE);

    if (newKey != null)
    {
      key = newKey;
    }

    current.setKey(key);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    if (localName.equals(XMLTags.RELATIONSHIP_TAG))
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
        }
      }
      else
      {
        object.apply();
      }
    }
  }

  private final RelationshipDAO createRelationship(String key, String type, Attributes attributes)
  {
    MdRelationshipDAOIF mdRelationshipDAOIF = MdRelationshipDAO.getMdRelationshipDAO(type);

    MdBusinessDAOIF parentMdBusinessDAOIF = mdRelationshipDAOIF.getParentMdBusiness();
    String parentType = parentMdBusinessDAOIF.definesType();

    MdBusinessDAOIF childMdBusinessDAOIF = mdRelationshipDAOIF.getChildMdBusiness();
    String childType = childMdBusinessDAOIF.definesType();

    String parentKey = attributes.getValue(XMLTags.PARENT_KEY_TAG);
    String childKey = attributes.getValue(XMLTags.CHILD_KEY_TAG);

    // Get the database ID of a XML parent key
    String dataParentId = "";
    try
    {
      dataParentId = EntityDAO.getIdFromKey(parentType, parentKey);
    }
    catch (DataNotFoundException e)
    {
      String[] search_tags = { XMLTags.OBJECT_TAG, XMLTags.RELATIONSHIP_TAG };
      // SearchHandler.searchEntity(manager, search_tags, XMLTags.KEY_ATTRIBUTE,
      // parentType, parentKey);
      SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.KEY_ATTRIBUTE, parentKey, key);
    }

    if (dataParentId.equals(""))
    {
      dataParentId = EntityDAO.getIdFromKey(parentType, parentKey);
    }

    // Get the database ID of a XML child key
    String dataChildId = "";
    try
    {
      dataChildId = EntityDAO.getIdFromKey(childType, childKey);
    }
    catch (DataNotFoundException e)
    {
      String[] search_tags = { XMLTags.OBJECT_TAG, XMLTags.RELATIONSHIP_TAG };
      SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.KEY_ATTRIBUTE, childKey, key);
      // SearchHandler.searchEntity(manager, search_tags, XMLTags.KEY_ATTRIBUTE,
      // childType, childKey);
    }

    if (dataChildId.equals(""))
    {
      dataChildId = EntityDAO.getIdFromKey(childType, childKey);
    }

    // Ensure that the class being referenced has already been defined
    if (!MdTypeDAO.isDefined(type))
    {
      String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG, XMLTags.ENUMERATION_MASTER_TAG, XMLTags.MD_STRUCT_TAG, XMLTags.MD_LOCAL_STRUCT_TAG };
      SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, type, key);
    }

    RelationshipDAO relationship = RelationshipDAO.newInstance(dataParentId, dataChildId, type);

    if (key != null && !key.equals(""))
    {
      relationship.setKey(key);
    }

    return relationship;
  }
}
