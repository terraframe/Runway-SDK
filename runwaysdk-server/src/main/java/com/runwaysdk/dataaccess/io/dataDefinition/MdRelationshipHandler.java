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

import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.MdGraphInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

public class MdRelationshipHandler extends MdEntityHandler implements TagHandlerIF, HandlerFactoryIF
{
  public static class ParentHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public ParentHandler(ImportManager manager)
    {
      super(manager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onStartElement(String qName, Attributes attributes, TagContext context)
    {
      MdRelationshipDAO mdRelationship = (MdRelationshipDAO) context.getObject(MdTypeInfo.CLASS);

      String parentType = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

      // Make sure that the class being reference has already been defined
      if (!MdTypeDAO.isDefined(parentType))
      {
        String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG, XMLTags.MD_STRUCT_TAG, XMLTags.MD_LOCAL_STRUCT_TAG };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, parentType, mdRelationship.definesType());
      }

      MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(parentType);
      mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, mdBusinessIF.getOid());
      ImportManager.setLocalizedValue(mdRelationship, MdRelationshipInfo.PARENT_DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
      ImportManager.setValue(mdRelationship, MdRelationshipInfo.PARENT_CARDINALITY, attributes, XMLTags.CARDINALITY_ATTRIBUTE);
      ImportManager.setValue(mdRelationship, MdRelationshipInfo.PARENT_METHOD, attributes, XMLTags.RELATIONSHIP_METHOD_TAG);
    }

  }

  public static class ChildHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public ChildHandler(ImportManager manager)
    {
      super(manager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onStartElement(String qName, Attributes attributes, TagContext context)
    {
      MdRelationshipDAO mdRelationship = (MdRelationshipDAO) context.getObject(MdTypeInfo.CLASS);

      String childType = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

      // Make sure that the class being reference has already been defined
      if (!MdTypeDAO.isDefined(childType))
      {
        String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_TERM_TAG, XMLTags.MD_STRUCT_TAG, XMLTags.MD_LOCAL_STRUCT_TAG };

        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, childType, mdRelationship.definesType());
      }

      MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(childType);

      mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, mdBusinessIF.getOid());
      ImportManager.setLocalizedValue(mdRelationship, MdRelationshipInfo.CHILD_DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
      ImportManager.setValue(mdRelationship, MdRelationshipInfo.CHILD_CARDINALITY, attributes, XMLTags.CARDINALITY_ATTRIBUTE);
      ImportManager.setValue(mdRelationship, MdRelationshipInfo.CHILD_METHOD, attributes, XMLTags.RELATIONSHIP_METHOD_TAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onEndElement(String uri, String qName, String name, TagContext context)
    {
      MdRelationshipDAO mdRelationship = (MdRelationshipDAO) context.getObject(MdTypeInfo.CLASS);

      // Ensure that the MdRelationshipDAO has been applied
      if (mdRelationship.getValue(MdRelationshipInfo.PARENT_MD_BUSINESS).length() > 0 && mdRelationship.getValue(MdRelationshipInfo.CHILD_MD_BUSINESS).length() > 0 && !mdRelationship.isAppliedToDB())
      {
        mdRelationship.apply();
      }
    }
  }

  public static class Decorator extends TagHandlerDecorator implements TagHandlerIF
  {
    public Decorator(TagHandlerIF handler)
    {
      super(handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerDecorator#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onStartElement(String qName, Attributes attributes, TagContext context)
    {
      MdRelationshipDAO mdRelationship = (MdRelationshipDAO) context.getObject(MdTypeInfo.CLASS);

      // Ensure that the MdRelationshipDAO has been applied
      if (!mdRelationship.isAppliedToDB())
      {
        mdRelationship.apply();
      }

      super.onStartElement(qName, attributes, context);
    }
  }

  public MdRelationshipHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(this));
    this.addHandler(XMLTags.PARENT_TAG, new ParentHandler(manager));
    this.addHandler(XMLTags.CHILD_TAG, new ChildHandler(manager));
    this.addHandler(XMLTags.ATTRIBUTES_TAG, new Decorator(new MdAttributeHandler(manager)));
    this.addHandler(XMLTags.MD_METHOD_TAG, new Decorator(new MdMethodHandler(manager)));
    this.addHandler(XMLTags.STUB_SOURCE_TAG, new SourceHandler(manager, XMLTags.STUB_SOURCE_TAG, MdClassInfo.STUB_SOURCE));
    this.addHandler(XMLTags.DTO_STUB_SOURCE_TAG, new SourceHandler(manager, XMLTags.DTO_STUB_SOURCE_TAG, MdClassInfo.DTO_STUB_SOURCE));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory#supports(com.runwaysdk.dataaccess.io.dataDefinition.TagContext, java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String qName)
  {
    MdRelationshipDAO mdRelationship = (MdRelationshipDAO) context.getObject(MdTypeInfo.CLASS);

    if (mdRelationship != null && this.getManager().isCreated(mdRelationship.definesType()))
    {
      return false;
    }

    return super.supports(context, qName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String qName, Attributes attributes, TagContext context)
  {
    String key = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    MdRelationshipDAO mdRelationshipDAO = this.createMdRelationship(qName, key);
    this.importMdRelationship(mdRelationshipDAO, attributes);

    context.setObject(MdTypeInfo.CLASS, mdRelationshipDAO);
  }

  /**
   * Creates a new instance of a MdRelationshipDAO. Depending on the given tag, the MdRelationshipDAO created is either a MdTree, a MdGraph, or a MdRelationshipDAO.
   * 
   * @param type
   *          The xml tag corresponding to the type of MdRelationshipDAO to create
   */
  private MdRelationshipDAO createMdRelationship(String type, String key)
  {
    if (type.equals(XMLTags.MD_RELATIONSHIP_TAG))
    {
      return (MdRelationshipDAO) this.getManager().getEntityDAO(MdRelationshipInfo.CLASS, key).getEntityDAO();
    }
    else if (type.equals(XMLTags.MD_TREE_TAG))
    {
      return (MdRelationshipDAO) this.getManager().getEntityDAO(MdTreeInfo.CLASS, key).getEntityDAO();
    }
    else if (type.equals(XMLTags.MD_TERM_RELATIONSHIP_TAG))
    {
      return (MdTermRelationshipDAO) this.getManager().getEntityDAO(MdTermRelationshipInfo.CLASS, key).getEntityDAO();
    }
    else
    {
      return (MdRelationshipDAO) this.getManager().getEntityDAO(MdGraphInfo.CLASS, key).getEntityDAO();
    }
  }

  /**
   * Creates an MdRelationshipDAO from the parse of the class tag attributes.
   * 
   * @param attributes
   *          The attributes of an class tag
   * @return MdRelationshipDAO from the parse of the class tag attributes.
   */
  private final void importMdRelationship(MdRelationshipDAO mdRelationshipDAO, Attributes attributes)
  {
    // Import the required attributes
    String type = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdRelationshipDAO.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(type));
    mdRelationshipDAO.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(type));

    // Import optional attributes
    ImportManager.setLocalizedValue(mdRelationshipDAO, MdTypeInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MetadataInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdRelationshipDAO, MetadataInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdElementInfo.EXTENDABLE, attributes, XMLTags.EXTENDABLE_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdElementInfo.ABSTRACT, attributes, XMLTags.ABSTRACT_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdElementInfo.GENERATE_SOURCE, attributes, XMLTags.GENERATE_SOURCE);
    ImportManager.setValue(mdRelationshipDAO, MdRelationshipInfo.COMPOSITION, attributes, XMLTags.COMPOSITION_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdRelationshipInfo.ENFORCE_SITE_MASTER, attributes, XMLTags.ENFORCE_SITE_MASTER_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdRelationshipInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);
    ImportManager.setValue(mdRelationshipDAO, MdEntityInfo.HAS_DETERMINISTIC_IDS, attributes, XMLTags.HAS_DETERMINISTIC_ID);

    // Import optional reference attributes
    String extend = attributes.getValue(XMLTags.EXTENDS_ATTRIBUTE);
    String cacheAlgorithm = attributes.getValue(XMLTags.CACHE_ALGORITHM_ATTRIBUTE);

    if (extend != null)
    {
      // Ensure the parent class has already been defined in the database
      if (!MdTypeDAO.isDefined(extend))
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { XMLTags.MD_RELATIONSHIP_TAG, XMLTags.MD_TERM_RELATIONSHIP_TAG };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, extend, mdRelationshipDAO.definesType());
      }

      MdRelationshipDAOIF superMdRelationship = MdRelationshipDAO.getMdRelationshipDAO(extend);
      mdRelationshipDAO.setValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP, superMdRelationship.getOid());
    }
    else
    {
      // Only set the publish flag if this defines a MdRelationshipDAO which is
      // the
      // root of its hierarchy
      ImportManager.setValue(mdRelationshipDAO, MdBusinessInfo.PUBLISH, attributes, XMLTags.PUBLISH_ATTRIBUTE);
    }

    if (cacheAlgorithm != null)
    {
      // Change to an everything caching algorithm
      if (cacheAlgorithm.equals(XMLTags.EVERYTHING_ENUMERATION))
      {
        mdRelationshipDAO.addItem(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getOid());
      }
      // Change to a nonthing caching algorithm
      else if (cacheAlgorithm.equals(XMLTags.NOTHING_ENUMERATION))
      {
        mdRelationshipDAO.addItem(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
      }
    }

    String tableName = attributes.getValue(XMLTags.ENTITY_TABLE);
    if (tableName != null)
    {
      mdRelationshipDAO.setTableName(tableName);
    }

    // If this is a MdTermRelationship then import the assocation type attribute
    if (mdRelationshipDAO instanceof MdTermRelationshipDAO)
    {
      String associationType = attributes.getValue(XMLTags.ASSOCIATION_TYPE_ATTRIBUTE);

      if (associationType != null)
      {
        if (associationType.equals(XMLTags.RELATIONSHIP_OPTION))
        {
          mdRelationshipDAO.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.RELATIONSHIP.getOid());
        }
        else if (associationType.equals(XMLTags.TREE_OPTION))
        {
          mdRelationshipDAO.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.TREE.getOid());
        }
        else if (associationType.equals(XMLTags.GRAPH_OPTION))
        {
          mdRelationshipDAO.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.GRAPH.getOid());
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String qName, String name, TagContext context)
  {
    MdRelationshipDAO mdRelationshipDAO = (MdRelationshipDAO) context.getObject(MdTypeInfo.CLASS);

    if (!this.getManager().isCreated(mdRelationshipDAO.definesType()))
    {
      mdRelationshipDAO.apply();

      this.getManager().addMapping(mdRelationshipDAO.definesType(), mdRelationshipDAO.getOid());
    }

    // Make sure the name has not already been defined
    this.getManager().endImport(mdRelationshipDAO.definesType());
  }
}
