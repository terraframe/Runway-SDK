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

import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeNumberInfo;
import com.runwaysdk.constants.MdAttributePrimitiveInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeVirtualDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.TermAttributeDAOIF;
import com.runwaysdk.dataaccess.attributes.InvalidAttributeTypeException;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLException;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeHashDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeNumberDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributePrimitiveDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeSymmetricDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeVirtualDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdLocalStructDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;

/**
 * @author Justin Smethie
 * @date 6/01/06
 */
public class MdAttributeHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  protected static class RootTermHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public RootTermHandler(ImportManager manager)
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
      MdAttributeDAO mdAttribute = (MdAttributeDAO) context.getObject(MdAttributeInfo.CLASS);
      MdTermDAOIF mdTerm = ( (TermAttributeDAOIF) mdAttribute ).getReferenceMdBusinessDAO();

      String key = attributes.getValue(XMLTags.KEY_ATTRIBUTE);

      BusinessDAOIF term = this.getTerm(mdAttribute, mdTerm, key);

      String relationshipType = ( (TermAttributeDAOIF) mdAttribute ).getAttributeRootRelationshipType();

      RelationshipDAO relationship = this.getRelationship(mdAttribute, term, relationshipType);

      ImportManager.setValue(relationship, MdAttributeTermInfo.SELECTABLE, attributes, XMLTags.SELECTABLE);

      relationship.apply();
    }

    protected RelationshipDAO getRelationship(MdAttributeDAO mdAttribute, BusinessDAOIF term, String relationshipType)
    {
      String parentId = mdAttribute.getOid();
      String childId = term.getOid();

      RelationshipDAOQuery query = new QueryFactory().relationshipDAOQuery(relationshipType);
      query.WHERE(query.parentId().EQ(parentId));
      query.AND(query.childId().EQ(childId));

      OIterator<RelationshipDAOIF> iterator = query.getIterator();

      try
      {
        if (iterator.hasNext())
        {
          RelationshipDAO relationship = iterator.next().getRelationshipDAO();

          return relationship;
        }
        else
        {
          RelationshipDAO relationship = RelationshipDAO.newInstance(parentId, childId, relationshipType);
          relationship.setValue(RelationshipInfo.KEY, mdAttribute.getKey() + "-" + term.getKey());

          return relationship;
        }
      }
      finally
      {
        iterator.close();
      }
    }

    /**
     * @param mdTerm
     * @param key
     * @return
     */
    private BusinessDAOIF getTerm(MdAttributeDAO mdAttribute, MdTermDAOIF mdTerm, String key)
    {
      BusinessDAOQuery query = new QueryFactory().businessDAOQuery(mdTerm.definesType());
      query.WHERE(query.get(BusinessInfo.KEY).EQ(key));
      OIterator<BusinessDAOIF> iterator = query.getIterator();

      try
      {
        if (iterator.hasNext())
        {
          BusinessDAOIF term = iterator.next();

          return term;
        }
        else
        {
          String[] tags = new String[] { XMLTags.OBJECT_TAG, XMLTags.RELATIONSHIP_TAG };
          SearchHandler.searchEntity(this.getManager(), tags, XMLTags.KEY_ATTRIBUTE, key, mdAttribute.getKey());

          return this.getTerm(mdAttribute, mdTerm, key);
        }
      }
      finally
      {
        iterator.close();
      }
    }
  }

  protected static abstract class AttributeHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    private String type;

    public AttributeHandler(ImportManager manager, String type)
    {
      super(manager);

      this.type = type;
    }

    protected abstract void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes);

    protected MdClassDAO getMdClass(TagContext context)
    {
      return (MdClassDAO) context.getObject(MdTypeInfo.CLASS);
    }

    protected void populate(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      ImportManager.setLocalizedValue(mdAttribute, MdAttributeInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
      ImportManager.setValue(mdAttribute, MdAttributeInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      MdClassDAO mdClass = this.getMdClass(context);
      String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

      MdAttributeDAO mdAttribute = this.getManager().getMdAttribute(mdClass, name, this.type);

      if (! ( mdAttribute.getType().equals(this.type) ))
      {
        String errMsg = "The attribute [" + mdAttribute.definesAttribute() + "] on type [" + mdClass.definesType() + "] is not the correct attribute type.";

        MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(this.type);
        MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttribute.getType());

        throw new InvalidAttributeTypeException(errMsg, mdAttribute, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
      }

      this.configure(mdClass, mdAttribute, attributes);

      this.apply(mdClass, mdAttribute);

      // Import REQIRED for dimension
      String requiredForDimension = attributes.getValue(XMLTags.REQUIRED_FOR_DIMENSION_ATTRIBUTE);

      if (requiredForDimension != null && requiredForDimension.length() > 0)
      {
        String[] dimensions = requiredForDimension.split(",");

        for (String dimension : dimensions)
        {
          MdDimensionDAOIF mdDimension = MdDimensionDAO.getByName(dimension.trim());
          MdAttributeDimensionDAOIF mdAttributeDimensionIF = mdAttribute.getMdAttributeDimension(mdDimension);

          MdAttributeDimensionDAO mdAttributeDimension = mdAttributeDimensionIF.getBusinessDAO();
          mdAttributeDimension.setValue(MdAttributeDimensionInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
          mdAttributeDimension.apply();
        }
      }

      context.setObject(MdAttributeInfo.CLASS, mdAttribute);
    }

    private void apply(MdClassDAO mdClass, MdAttributeDAO mdAttribute)
    {
      if (mdAttribute instanceof MdAttributeConcreteDAOIF)
      {
        mdAttribute.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdClass.getOid());
        mdAttribute.apply();
      }
      else if (mdAttribute instanceof MdAttributeVirtualDAOIF && mdClass instanceof MdViewDAO)
      {
        try
        {
          mdAttribute.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, mdClass.getOid());
          mdAttribute.apply();
        }
        catch (InvalidReferenceException e)
        {
          String errMsg = "Attribute [" + mdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()) + "] on type [" + mdClass.definesType() + "] does not reference a valid [" + MdAttributeConcreteInfo.CLASS + "]";
          throw new InvalidReferenceException(errMsg, e.getMdAttributeReference());
        }
      }
    }
  }

  protected static class AttributeVirtualHandler extends AttributeHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeVirtualHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdClassDAO mdClass, MdAttributeVirtualDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      // Set the Name attribute. This is always required
      String name = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

      if (name != null && name.length() > 0)
      {
        mdAttribute.setValue(MdAttributeVirtualInfo.NAME, name);
      }

      ImportManager.setLocalizedValue(mdAttribute, MdAttributeVirtualInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
      ImportManager.setValue(mdAttribute, MdAttributeVirtualInfo.REQUIRED, attributes, XMLTags.REQUIRED_ATTRIBUTE);
      String rename = attributes.getValue(XMLTags.RENAME_ATTRIBUTE);

      if (rename != null && rename.length() > 0)
      {
        mdAttribute.setValue(MdAttributeVirtualInfo.NAME, rename);
      }

      String concreteType = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);
      String concreteName = attributes.getValue(XMLTags.CONCRETE_ATTRIBUTE);

      if (concreteName != null && concreteType != null)
      {
        if (!MdTypeDAO.isDefined(concreteType))
        {
          String[] search_tags = XMLTags.TYPE_TAGS;
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, concreteType, mdClass.definesType());
        }

        MdClassDAOIF concreteClass = MdClassDAO.getMdClassDAO(concreteType);
        MdAttributeDAOIF concreteAttribute = concreteClass.definesAttribute(concreteName);

        // IMPORTANT: It is possible that the concrete type is defined before this
        // schema was imported. However, the definition of the concrete attribute
        // may not be defined until an update statement in the current xml file.
        // As such it is possible to have the type defined but not have the
        // concrete attribute defined. Therefore, if the attribute is not defined
        // then we need to search and import the definition of the class which
        // exists in the current xml file.
        if (concreteAttribute == null)
        {
          String[] search_tags = XMLTags.TYPE_TAGS;
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, concreteType, concreteClass.definesType());

          concreteAttribute = concreteClass.definesAttribute(concreteName);
        }

        if (concreteAttribute instanceof MdAttributeConcreteDAOIF)
        {
          mdAttribute.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, concreteAttribute.getOid());
        }
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeVirtualDAO) mdAttribute, attributes);
    }
  }

  protected static class AttributeConcreteHandler extends AttributeHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeConcreteHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    private void importIndexType(MdAttributeConcreteDAO mdAttribute, String indexType)
    {
      if (indexType != null)
      {
        // Default to non unique indexing
        String indexingId = IndexTypes.NON_UNIQUE_INDEX.getOid();

        // Change to an everything caching algorithm
        if (indexType.equals(XMLTags.UNIQUE_INDEX_ENUMERATION))
        {
          indexingId = IndexTypes.UNIQUE_INDEX.getOid();
        }
        // Change to a nonthing caching algorithm
        else if (indexType.equals(XMLTags.NO_INDEX_ENUMERATION))
        {
          indexingId = IndexTypes.NO_INDEX.getOid();
        }

        mdAttribute.addItem(MdAttributeConcreteInfo.INDEX_TYPE, indexingId);
      }
    }

    private void importVisibility(MdAttributeConcreteDAO mdAttribute, String value, String attributeName)
    {
      if (value != null && value.equals(XMLTags.PUBLIC_VISIBILITY_ENUMERATION))
      {
        mdAttribute.addItem(attributeName, VisibilityModifier.PUBLIC.getOid());
      }
      else if (value != null && value.equals(XMLTags.PROTECTED_VISIBILITY_ENUMERATION))
      {
        mdAttribute.addItem(attributeName, VisibilityModifier.PROTECTED.getOid());
      }
    }

    protected void populate(MdClassDAO mdClass, MdAttributeConcreteDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      // Set the Name attribute. This is always required
      mdAttribute.setValue(MdAttributeConcreteInfo.NAME, attributes.getValue(XMLTags.NAME_ATTRIBUTE));

      ImportManager.setLocalizedValue(mdAttribute, MdAttributeConcreteInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
      ImportManager.setValue(mdAttribute, MdAttributeConcreteInfo.REQUIRED, attributes, XMLTags.REQUIRED_ATTRIBUTE);
      String rename = attributes.getValue(XMLTags.RENAME_ATTRIBUTE);

      if (rename != null && rename.length() > 0)
      {
        mdAttribute.setValue(MdAttributeConcreteInfo.NAME, rename);
      }

      String columnName = attributes.getValue(XMLTags.COLUMN_ATTRIBUTE);

      if (columnName != null && columnName.trim().length() != 0)
      {
        mdAttribute.setColumnName(columnName);
      }

      ImportManager.setValue(mdAttribute, MdAttributeConcreteInfo.DEFAULT_VALUE, attributes, XMLTags.DEFAULT_VALUE_ATTRIBUTE);
      ImportManager.setValue(mdAttribute, MdAttributeConcreteInfo.IMMUTABLE, attributes, XMLTags.IMMUTABLE_ATTRIBUTE);

      importIndexType(mdAttribute, attributes.getValue(XMLTags.INDEX_TYPE_ATTRIBUTE));
      importVisibility(mdAttribute, attributes.getValue(XMLTags.GETTER_VISIBILITY_ATTRIBUTE), MdAttributeConcreteInfo.GETTER_VISIBILITY);
      importVisibility(mdAttribute, attributes.getValue(XMLTags.SETTER_VISIBILITY_ATTRIBUTE), MdAttributeConcreteInfo.SETTER_VISIBILITY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeConcreteDAO) mdAttribute, attributes);
    }
  }

  protected static class AttributeEnumerationHandler extends AttributeConcreteHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeEnumerationHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    private void importEnumerationDefaultValue(MdClassDAO mdClass, MdAttributeEnumerationDAO mdAttributeEnumerationDAO, String defaultValue)
    {
      if (defaultValue != null)
      {
        // The value is the enumeration item name
        String masterListType = mdAttributeEnumerationDAO.getMdEnumerationDAO().getMasterListMdBusinessDAO().definesType();
        String enumerationItemKey = EnumerationItemDAO.buildKey(masterListType, defaultValue);

        String oid = "";

        try
        {
          oid = EntityDAO.getOidFromKey(masterListType, enumerationItemKey);
        }
        catch (DataNotFoundException e)
        {
          SearchCriteriaIF criteria = new EntitySearchCriteria(masterListType, enumerationItemKey, XMLTags.OBJECT_TAG);

          SearchHandler.searchEntity(this.getManager(), criteria, mdClass.definesType());
        }

        if (oid.equals(""))
        {
          oid = EntityDAO.getOidFromKey(masterListType, enumerationItemKey);
        }

        mdAttributeEnumerationDAO.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, oid);
      }
    }

    private void importEnumerationType(MdClassDAO mdClass, MdAttributeEnumerationDAO mdAttributeEnumerationDAO, String mdEnumerationType)
    {
      if (mdEnumerationType != null)
      {
        // Ensure that the enumeration_filter has already been defined
        if (!MdTypeDAO.isDefined(mdEnumerationType))
        {
          String[] search_tags = { XMLTags.MD_ENUMERATION_TAG };
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, mdEnumerationType, mdClass.definesType());
        }

        MdEnumerationDAOIF mdEnumerationIF = MdEnumerationDAO.getMdEnumerationDAO(mdEnumerationType);
        mdAttributeEnumerationDAO.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, mdEnumerationIF.getOid());
      }
    }

    protected void populate(MdClassDAO mdClass, MdAttributeEnumerationDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      // NOTE: enumeration type must be imported before the default value
      // because the enumeration type is required in order to dereference
      // the actual oid of the default value.
      importEnumerationType(mdClass, mdAttribute, attributes.getValue(XMLTags.TYPE_ATTRIBUTE));

      importEnumerationDefaultValue(mdClass, mdAttribute, attributes.getValue(XMLTags.DEFAULT_VALUE_ATTRIBUTE));

      // Parse the multipleSelect attribute
      ImportManager.setValue(mdAttribute, MdAttributeEnumerationInfo.SELECT_MULTIPLE, attributes, XMLTags.SELECT_MULTIPLE_ATTRIBUTE, "true");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeEnumerationDAO) mdAttribute, attributes);
    }

  }

  protected abstract static class AttributeRefHandler extends AttributeConcreteHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeRefHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void importReferenceDefaultValue(MdClassDAO mdClass, MdAttributeDAO mdAttribute, String defaultValue)
    {
      if (defaultValue != null)
      {
        String referenceType = ( (MdAttributeRefDAOIF) mdAttribute ).getReferenceMdBusinessDAO().definesType();

        String oid = "";

        try
        {
          oid = EntityDAO.getOidFromKey(referenceType, defaultValue);
        }
        catch (DataNotFoundException e)
        {
          SearchCriteriaIF criteria = new EntitySearchCriteria(referenceType, defaultValue, XMLTags.OBJECT_TAG);

          SearchHandler.searchEntity(this.getManager(), criteria, mdClass.definesType());
        }

        if (oid.equals(""))
        {
          oid = EntityDAO.getOidFromKey(referenceType, defaultValue);
        }

        mdAttribute.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, oid);
      }
    }

    protected void importReferenceType(MdClassDAO mdClass, MdAttributeDAO mdAttribute, String referenceType)
    {
      if (referenceType != null)
      {
        // Ensure that the class being reference is defined in the database
        if (!MdTypeDAO.isDefined(referenceType))
        {
          String[] search_tags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_STRUCT_TAG, XMLTags.MD_TERM_TAG };
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, referenceType, mdClass.definesType());
        }

        // Get the databaseID of the enumeration reference
        MdBusinessDAOIF refMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(referenceType);
        mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, refMdBusinessIF.getOid());
      }
    }
  }

  protected static class AttributeReferenceHandler extends AttributeRefHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeReferenceHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdClassDAO mdClass, MdAttributeReferenceDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      this.importReferenceType(mdClass, mdAttribute, attributes.getValue(XMLTags.TYPE_ATTRIBUTE));
      this.importReferenceDefaultValue(mdClass, mdAttribute, attributes.getValue(XMLTags.DEFAULT_KEY_ATTRIBUTE));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeReferenceDAO) mdAttribute, attributes);
    }
  }

  protected static class AttributeTermHandler extends AttributeReferenceHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeTermHandler(ImportManager manager, String type)
    {
      super(manager, type);

      this.addHandler(XMLTags.ROOT_TERM_TAG, new RootTermHandler(manager));
    }
  }

  protected static class AttributeMultiReferenceHandler extends AttributeRefHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeMultiReferenceHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdClassDAO mdClass, MdAttributeMultiReferenceDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      this.importReferenceType(mdClass, mdAttribute, attributes.getValue(XMLTags.TYPE_ATTRIBUTE));
      this.importReferenceDefaultValue(mdClass, mdAttribute, attributes.getValue(XMLTags.DEFAULT_KEY_ATTRIBUTE));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeMultiReferenceDAO) mdAttribute, attributes);
    }
  }

  protected static class AttributeMultiTermHandler extends AttributeMultiReferenceHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeMultiTermHandler(ImportManager manager, String type)
    {
      super(manager, type);

      this.addHandler(XMLTags.ROOT_TERM_TAG, new RootTermHandler(manager));
    }
  }

  protected static class AttributePrimitiveHandler extends AttributeConcreteHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributePrimitiveHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdClassDAO mdClass, MdAttributePrimitiveDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      ImportManager.setValue(mdAttribute, MdAttributePrimitiveInfo.IS_EXPRESSION, attributes, XMLTags.IS_EXPRESSION);
      ImportManager.setValue(mdAttribute, MdAttributePrimitiveInfo.EXPRESSION, attributes, XMLTags.EXPRESSION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributePrimitiveDAO) mdAttribute, attributes);
    }

  }

  protected static class AttributeLocalHandler extends AttributeConcreteHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeLocalHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdClassDAO mdClass, MdAttributeLocalDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      String localType = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

      if (localType != null)
      {
        // Ensure that the class being reference is defined in the database
        if (!MdTypeDAO.isDefined(localType))
        {
          String[] search_tags = { XMLTags.MD_LOCAL_STRUCT_TAG };
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, localType, mdClass.definesType());
        }

        // Get the databaseID of the enumeration reference
        MdLocalStructDAOIF refMdLocalStructIF = MdLocalStructDAO.getMdLocalStructDAO(localType);

        // Reference to a struct class
        mdAttribute.setValue(MdAttributeLocalInfo.MD_STRUCT, refMdLocalStructIF.getOid());
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeLocalDAO) mdAttribute, attributes);
    }
  }

  protected static class AttributeNumberHandler extends AttributePrimitiveHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeNumberHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdClassDAO mdClass, MdAttributeNumberDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      ImportManager.setValue(mdAttribute, MdAttributeNumberInfo.REJECT_NEGATIVE, attributes, XMLTags.REJECT_NEGATIVE_ATTRIBUTE);
      ImportManager.setValue(mdAttribute, MdAttributeNumberInfo.REJECT_POSITIVE, attributes, XMLTags.REJECT_POSITIVE_ATTRIBUTE);
      ImportManager.setValue(mdAttribute, MdAttributeNumberInfo.REJECT_ZERO, attributes, XMLTags.REJECT_ZERO_ATTRIBUTE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeNumberDAO) mdAttribute, attributes);
    }
  }

  protected static class AttributeDecHandler extends AttributeNumberHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeDecHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdClassDAO mdClass, MdAttributeDecDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      ImportManager.setValue(mdAttribute, MdAttributeDecInfo.LENGTH, attributes, XMLTags.LENGTH_ATTRIBUTE);
      ImportManager.setValue(mdAttribute, MdAttributeDecInfo.DECIMAL, attributes, XMLTags.DECIMAL_ATTRIBUTE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeDecDAO) mdAttribute, attributes);
    }
  }

  protected static class AttributeBooleanHandler extends AttributePrimitiveHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeBooleanHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdClassDAO mdClass, MdAttributeBooleanDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      ImportManager.setLocalizedValue(mdAttribute, MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, attributes, XMLTags.POSITIVE_DISPLAY_LABEL_ATTRIBUTE);
      ImportManager.setLocalizedValue(mdAttribute, MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, attributes, XMLTags.NEGATIVE_DISPLAY_LABEL_ATTRIBUTE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeBooleanDAO) mdAttribute, attributes);
    }

  }

  protected static class AttributeCharacterHandler extends AttributePrimitiveHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeCharacterHandler(ImportManager manager, String tag, String type)
    {
      super(manager, type);
    }

    protected void populate(MdClassDAO mdClass, MdAttributeCharacterDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      ImportManager.setValue(mdAttribute, MdAttributeCharacterInfo.SIZE, attributes, XMLTags.SIZE_ATTRIBUTE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeCharacterDAO) mdAttribute, attributes);
    }

  }

  protected static class AttributeStructHandler extends AttributeConcreteHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeStructHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    private void importStructType(MdClassDAO mdClass, MdAttributeDAO mdAttribute, String structType)
    {
      if (structType != null)
      {
        // Ensure that the class being reference is defined in the database
        if (!MdTypeDAO.isDefined(structType))
        {
          String[] search_tags = { XMLTags.MD_STRUCT_TAG };
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, structType, mdClass.definesType());
        }

        // Get the databaseID of the enumeration reference
        MdStructDAOIF refMdStructIF = MdStructDAO.getMdStructDAO(structType);

        // Reference to a struct class
        mdAttribute.setValue(MdAttributeStructInfo.MD_STRUCT, refMdStructIF.getOid());
      }
    }

    protected void populate(MdClassDAO mdClass, MdAttributeStructDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      this.importStructType(mdClass, mdAttribute, attributes.getValue(XMLTags.TYPE_ATTRIBUTE));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeStructDAO) mdAttribute, attributes);
    }
  }

  protected static class AttributeSymmetricHandler extends AttributeConcreteHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeSymmetricHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    private void importSymmetricMethod(MdAttributeDAO mdAttribute, String symmetricMethod)
    {
      if (symmetricMethod != null)
      {
        // Find the oid associated with the of the symmetric method
        QueryFactory qFactory = new QueryFactory();
        BusinessDAOQuery query = qFactory.businessDAOQuery(EntityTypes.SYMMETRIC_METHOD.getType());
        query.WHERE(query.aCharacter(EnumerationMasterInfo.NAME).EQ(symmetricMethod));

        OIterator<BusinessDAOIF> iterator = query.getIterator();

        try
        {

          // If the encryption method has been defined
          if (iterator.hasNext())
          {
            String methodId = iterator.next().getOid();

            // Set the hash value
            mdAttribute.addItem(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, methodId);
          }
          else
          {
            String error = "The symmetric encryption method [" + symmetricMethod + "] is not defined in the symmetric methods enumeration";
            throw new XMLException(error);
          }
        }
        finally
        {
          iterator.close();
        }
      }
    }

    protected void populate(MdClassDAO mdClass, MdAttributeSymmetricDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      // Parse the referenceName attribute
      String symmetricMethod = attributes.getValue(XMLTags.SYMMETRIC_METHOD_ATTRIBUTE);

      this.importSymmetricMethod(mdAttribute, symmetricMethod);

      ImportManager.setValue(mdAttribute, MdAttributeSymmetricInfo.SECRET_KEY_SIZE, attributes, XMLTags.SYMMETRIC_KEYSIZE_ATTRIBUTE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeSymmetricDAO) mdAttribute, attributes);
    }
  }

  protected static class AttributeHashHandler extends AttributeConcreteHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeHashHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    private void importHashMethod(MdAttributeDAO mdAttribute, String hashMethod)
    {
      // Parse the referenceName attribute
      if (hashMethod != null)
      {
        // Find the oid associated with the name of the hash method
        QueryFactory qFactory = new QueryFactory();
        BusinessDAOQuery hashMethodQ = qFactory.businessDAOQuery(EntityTypes.HASH_METHOD.getType());
        hashMethodQ.WHERE(hashMethodQ.aCharacter(EnumerationMasterInfo.NAME).EQ(hashMethod));
        OIterator<BusinessDAOIF> iterator = hashMethodQ.getIterator();

        try
        {
          // If the encryption method has been defined
          if (iterator.hasNext())
          {
            String methodId = iterator.next().getOid();

            // Set the hash value
            mdAttribute.addItem(MdAttributeHashInfo.HASH_METHOD, methodId);
          }
          else
          {
            String error = "The hash encryption method [" + hashMethod + "] is not defined in the hash methods enumeration";
            throw new XMLException(error);
          }
        }
        finally
        {
          iterator.close();
        }
      }
    }

    protected void populate(MdClassDAO mdClass, MdAttributeHashDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      this.importHashMethod(mdAttribute, attributes.getValue(XMLTags.HASH_METHOD_ATTRIBUTE));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO, com.runwaysdk.dataaccess.metadata.MdAttributeDAO,
     * org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeHashDAO) mdAttribute, attributes);
    }
  }

  /**
   * @param manager
   * 
   */
  public MdAttributeHandler(ImportManager manager)
  {
    super(manager);

    // Setup default handlers
    this.addHandler(XMLTags.LOCAL_CHARACTER_TAG, new AttributeLocalHandler(manager, MdAttributeLocalCharacterInfo.CLASS));
    this.addHandler(XMLTags.LOCAL_TEXT_TAG, new AttributeLocalHandler(manager, MdAttributeLocalTextInfo.CLASS));
    this.addHandler(XMLTags.BLOB_TAG, new AttributeConcreteHandler(manager, MdAttributeBlobInfo.CLASS));
    this.addHandler(XMLTags.BOOLEAN_TAG, new AttributeBooleanHandler(manager, MdAttributeBooleanInfo.CLASS));
    this.addHandler(XMLTags.CHARACTER_TAG, new AttributeCharacterHandler(manager, XMLTags.CHARACTER_TAG, MdAttributeCharacterInfo.CLASS));
    this.addHandler(XMLTags.DATE_TAG, new AttributePrimitiveHandler(manager, MdAttributeDateInfo.CLASS));
    this.addHandler(XMLTags.DATETIME_TAG, new AttributePrimitiveHandler(manager, MdAttributeDateTimeInfo.CLASS));
    this.addHandler(XMLTags.TIME_TAG, new AttributePrimitiveHandler(manager, MdAttributeTimeInfo.CLASS));
    this.addHandler(XMLTags.DECIMAL_TAG, new AttributeDecHandler(manager, MdAttributeDecimalInfo.CLASS));
    this.addHandler(XMLTags.DOUBLE_TAG, new AttributeDecHandler(manager, MdAttributeDoubleInfo.CLASS));
    this.addHandler(XMLTags.FLOAT_TAG, new AttributeDecHandler(manager, MdAttributeFloatInfo.CLASS));
    this.addHandler(XMLTags.TEXT_TAG, new AttributePrimitiveHandler(manager, MdAttributeTextInfo.CLASS));
    this.addHandler(XMLTags.CLOB_TAG, new AttributePrimitiveHandler(manager, MdAttributeClobInfo.CLASS));
    this.addHandler(XMLTags.INTEGER_TAG, new AttributeNumberHandler(manager, MdAttributeIntegerInfo.CLASS));
    this.addHandler(XMLTags.LONG_TAG, new AttributeNumberHandler(manager, MdAttributeLongInfo.CLASS));
    this.addHandler(XMLTags.VIRTUAL_TAG, new AttributeVirtualHandler(manager, MdAttributeVirtualInfo.CLASS));
    this.addHandler(XMLTags.ENUMERATION_TAG, new AttributeEnumerationHandler(manager, MdAttributeEnumerationInfo.CLASS));
    this.addHandler(XMLTags.FILE_TAG, new AttributeConcreteHandler(manager, MdAttributeFileInfo.CLASS));
    this.addHandler(XMLTags.REFERENCE_TAG, new AttributeReferenceHandler(manager, MdAttributeReferenceInfo.CLASS));
    this.addHandler(XMLTags.TERM_TAG, new AttributeTermHandler(manager, MdAttributeTermInfo.CLASS));
    this.addHandler(XMLTags.MULTI_REFERENCE_TAG, new AttributeMultiReferenceHandler(manager, MdAttributeMultiReferenceInfo.CLASS));
    this.addHandler(XMLTags.MULTI_TERM_TAG, new AttributeMultiTermHandler(manager, MdAttributeMultiTermInfo.CLASS));
    this.addHandler(XMLTags.STRUCT_TAG, new AttributeStructHandler(manager, MdAttributeStructInfo.CLASS));
    this.addHandler(XMLTags.SYMMETRIC_TAG, new AttributeSymmetricHandler(manager, MdAttributeSymmetricInfo.CLASS));
    this.addHandler(XMLTags.HASH_TAG, new AttributeHashHandler(manager, MdAttributeHashInfo.CLASS));
  }
}
