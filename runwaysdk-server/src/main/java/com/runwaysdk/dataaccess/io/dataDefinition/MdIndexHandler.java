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
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

public class MdIndexHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public static class IndexAttributeHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public IndexAttributeHandler(ImportManager manager)
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
      MdIndexDAO mdIndex = (MdIndexDAO) context.getObject(MdIndexInfo.CLASS);

      String attributeName = attributes.getValue(XMLTags.INDEX_NAME_ATTRIBUTE);
      int indexOrder = Integer.parseInt(attributes.getValue(XMLTags.INDEX_ORDER_ATTRIBUTE));
      String parentOid = mdIndex.getValue(MdIndexInfo.MD_ENTITY);

      MdElementDAOIF mdEntity = MdElementDAO.get(parentOid);
      MdAttributeDAOIF mdAttribute = mdEntity.definesAttribute(attributeName);

      // IMPORTANT: The mdAttribute may not be defined yet. It could be defined
      // in an update tag later on in the same file, thus if mdAttribute == null
      // we need to ensure that the type of the mdIndex is imported.
      if (mdAttribute == null)
      {
        String type = mdEntity.definesType();
        String[] search_tags = XMLTags.TYPE_TAGS;
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, type, PREFIX + type);

        mdAttribute = mdEntity.definesAttribute(attributeName);
      }

      mdIndex.addAttribute(mdAttribute, indexOrder);
    }

  }

  /**
   * A prefix that guarantees that an indexName will be unique in the isCreated mapping, if it has not yet been created.
   */
  private static final String PREFIX = "~INDEX_";

  public MdIndexHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.INDEX_ATTRIBUTE_TAG, new IndexAttributeHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory#supports(com.runwaysdk.dataaccess.io.dataDefinition.TagContext, java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String localName)
  {
    String indexName = (String) context.getObject(MdIndexInfo.INDEX_NAME);

    if (indexName != null && this.getManager().isCreated(indexName))
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
    MdIndexDAO mdIndex = MdIndexDAO.newInstance();
    String active = attributes.getValue(XMLTags.INDEX_ACTIVE_ATTRIBUTE);

    this.importMdIndex(mdIndex, attributes);

    String indexName = PREFIX + mdIndex.getIndexName();

    // Make sure the MdIndex has not already been defined
    if (!this.getManager().isCreated(indexName))
    {
      mdIndex.apply();
    }

    context.setObject(MdIndexInfo.CLASS, mdIndex);
    context.setObject(MdIndexInfo.INDEX_NAME, indexName);
    context.setObject(MdIndexInfo.ACTIVE, active);
  }

  /**
   * Creates an MdIndex from the parse of the class tag attributes.
   * 
   * @param attributes
   *          The attributes of an class tag.
   * @return MdIndex from the parse of the class tag attributes.
   */
  private final void importMdIndex(MdIndexDAO mdIndex, Attributes attributes)
  {
    String entityType = attributes.getValue(XMLTags.INDEX_ENTITY_TYPE_ATTRIBUTE);

    if (!MdTypeDAO.isDefined(entityType))
    {
      String[] searchTags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG, XMLTags.MD_STRUCT_TAG, XMLTags.MD_LOCAL_STRUCT_TAG, XMLTags.MD_RELATIONSHIP_TAG, XMLTags.MD_TREE_TAG, XMLTags.MD_GRAPH_TAG, XMLTags.MD_TERM_RELATIONSHIP_TAG };
      SearchHandler.searchEntity(this.getManager(), searchTags, XMLTags.NAME_ATTRIBUTE, entityType, mdIndex.getKey());
    }

    MdElementDAOIF mdEntity = MdElementDAO.getMdElementDAO(entityType);

    mdIndex.setValue(MdIndexInfo.MD_ENTITY, mdEntity.getOid());
    mdIndex.setValue(MdIndexInfo.UNIQUE, attributes.getValue(XMLTags.INDEX_UNIQUE_ATTRIBUTE));
    mdIndex.setValue(MdIndexInfo.ACTIVE, MdAttributeBooleanInfo.FALSE);
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, attributes.getValue(XMLTags.DISPLAY_LABEL_ATTRIBUTE));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    MdIndexDAO mdIndex = (MdIndexDAO) context.getObject(MdIndexInfo.CLASS);
    String active = (String) context.getObject(MdIndexInfo.ACTIVE);
    String indexName = (String) context.getObject(MdIndexInfo.INDEX_NAME);

    mdIndex.setValue(MdIndexInfo.ACTIVE, active);
    mdIndex.apply();

    this.getManager().endImport(indexName);
  }

}
