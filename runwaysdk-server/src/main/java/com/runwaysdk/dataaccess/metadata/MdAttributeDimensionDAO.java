/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.metadata;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFactory;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.transaction.TransactionCache;
import com.runwaysdk.util.IdParser;

public class MdAttributeDimensionDAO extends MetadataDAO implements MdAttributeDimensionDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 3029276511292193522L;

  public MdAttributeDimensionDAO()
  {
    super();
  }

  public MdAttributeDimensionDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public MdAttributeDimensionDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeDimensionDAO(attributeMap, MdAttributeDimensionInfo.CLASS);
  }

  @Override
  public String apply()
  {
    this.setAndBuildKey();

    if (this.isNew() && this.isAppliedToDB() == false && !this.isImport())
    {
      String newId = IdParser.buildId(ServerIDGenerator.generateId(this.getKey()), this.getMdClassDAO().getId());
      this.getAttribute(EntityInfo.ID).setValue(newId);
    }

    return super.apply();
  }

  public String save(boolean businessContext)
  {
//    boolean applied = !this.isNew() || this.isAppliedToDB();

    MdAttributeDAOIF mdAttribute = this.definingMdAttribute();
    this.validateDefaultValue(mdAttribute);

    String id = super.save(businessContext);

    MdDimensionDAOIF mdDimension = this.definingMdDimension();
    MdAttributeDAOIF mdAttributeDAOIF = this.definingMdAttribute();
    MdAttributeDAO mdAttributeDAO = (MdAttributeDAO)mdAttributeDAOIF.getBusinessDAO();

    boolean required = ((AttributeBooleanIF)this.getAttributeIF(MdAttributeDimensionInfo.REQUIRED)).getBooleanValue();
    String defaultValue = this.getAttributeIF(MdAttributeDimensionInfo.DEFAULT_VALUE).getValue();
    mdAttributeDAO.addAttributeDimension(this.getId(), required, defaultValue, mdDimension.getId());
    // this apply puts the object onto the transaction cache so as not to corrupt the object in the global cache
    TransactionCache.getCurrentTransactionCache().updateEntityDAO(mdAttributeDAO);
    
// Heads up: optimize
//    if (!applied && !this.isImport())
//    {
//      // Create the MdDimension - to - MdAttributeDimension relationship
//      String mdDimensionRelationshipKey = mdDimension.getKey() + "-" + this.getKey();
//      String mdDimensionRelationshipId = IdParser.buildId(ServerIDGenerator.generateId(mdDimensionRelationshipKey), RelationshipTypes.DIMENSION_HAS_ATTRIBUTES.getId());
//
//      RelationshipDAO mdDimensionRelationship = RelationshipDAO.newInstance(mdDimension.getId(), id, RelationshipTypes.DIMENSION_HAS_ATTRIBUTES.getType());
//      mdDimensionRelationship.setKey(mdDimensionRelationshipKey);
//      mdDimensionRelationship.getAttribute(EntityInfo.ID).setValue(mdDimensionRelationshipId);
//      mdDimensionRelationship.apply();
//
//      // Create the MdAttribute - to - MdAttributeDimension relationship
//      String mdAttributeRelationshipKey = mdAttribute.getKey() + "-" + this.getKey();
//      String mdAttributeRelationshipId = IdParser.buildId(ServerIDGenerator.generateId(mdAttributeRelationshipKey), RelationshipTypes.ATTRIBUTE_HAS_DIMENSION.getId());
//
//      RelationshipDAO mdAttributeRelationship = RelationshipDAO.newInstance(mdAttribute.getId(), id, RelationshipTypes.ATTRIBUTE_HAS_DIMENSION.getType());
//      mdAttributeRelationship.setKey(mdAttributeRelationshipKey);
//      mdAttributeRelationship.getAttribute(EntityInfo.ID).setValue(mdAttributeRelationshipId);
//      mdAttributeRelationship.apply();
//    }

    return id;
  }
  
  
  @Override
  public void delete(boolean businessContext)
  {
    super.delete(businessContext);
    
    MdDimensionDAOIF mdDimensionDAOIF = this.definingMdDimension();
    MdAttributeDAO mdAttributeDAO = (MdAttributeDAO)this.definingMdAttribute().getBusinessDAO();
    mdAttributeDAO.removeMdAttributeDimension(mdDimensionDAOIF.getId());
    // this apply puts the object onto the transaction cache so as not to corrupt the object in the global cache
    TransactionCache.getCurrentTransactionCache().updateEntityDAO(mdAttributeDAO);
  }

  private void validateDefaultValue(MdAttributeDAOIF definingMdAttributeDAOIF)
  {
    if (this.getAttribute(MdAttributeDimensionInfo.DEFAULT_VALUE).isModified())
    {
      String value = this.getAttributeIF(MdAttributeDimensionInfo.DEFAULT_VALUE).getValue();

      MdClassDAOIF definingMdClassDAOIF = definingMdAttributeDAOIF.definedByClass();

      Attribute spoofAttribute = AttributeFactory.createAttribute(definingMdAttributeDAOIF.getKey(), definingMdAttributeDAOIF.getType(), MdAttributeConcreteInfo.DEFAULT_VALUE, definingMdClassDAOIF.definesType(), value);

      spoofAttribute.setContainingComponent(this);

      MdAttributeDAO definingMdAttribute = (MdAttributeDAO) definingMdAttributeDAOIF.getBusinessDAO();

      // IMPORTANT: The default value is never immutable regardless of value for
      // immutable on the definingMdAttributeDAOIF. As such, for the purposes of
      // validating the default value we need to ensure that the immutable value
      // of the definingMdAttributeDAOIF is true. Note, this change is not
      // persisted.
      if (definingMdAttribute.hasAttribute(MdAttributeConcreteInfo.IMMUTABLE))
      {
        definingMdAttribute.setValue(MdAttributeConcreteInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      }

      spoofAttribute.validate(definingMdAttribute, value);
    }
  }

  /**
   * Returns the display label of this metadata object
   * 
   * @param locale
   * 
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale)
  {
    return this.definingMdAttribute().getDisplayLabel(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public Map<String, String> getDisplayLabels()
  {
    return this.definingMdAttribute().getDisplayLabels();
  }

  public String getSignature()
  {
    return this.definingMdAttribute().getSignature();
  }

  /**
   * Return true if the attribute for the dimension is required, false
   * otherwise.
   * 
   * @return true if the attribute for the dimension is required, false
   *         otherwise.
   */
  public boolean isRequired()
  {
    return ( (AttributeBooleanIF) this.getAttributeIF(MdAttributeDimensionInfo.REQUIRED) ).getBooleanValue();
  }

  /**
   * Return true if the attribute for the dimension is required, false
   * otherwise.
   * 
   * @return true if the attribute for the dimension is required, false
   *         otherwise.
   */
  public String getDefaultValue()
  {
    return this.getAttributeIF(MdAttributeDimensionInfo.DEFAULT_VALUE).getValue();
  }

  public MdAttributeDAOIF definingMdAttribute()
  {
    return (MdAttributeDAOIF) ( (AttributeReferenceIF) this.getAttributeIF(MdAttributeDimensionInfo.DEFINING_MD_ATTRIBUTE) ).dereference();
  }

  public void setDefiningMdAttribute(MdAttributeDAOIF mdAttributeDAOIF)
  {
    this.getAttribute(MdAttributeDimensionInfo.DEFINING_MD_ATTRIBUTE).setValue(mdAttributeDAOIF.getId());
  }

  public MdDimensionDAOIF definingMdDimension()
  {
    return (MdDimensionDAOIF) ( (AttributeReferenceIF) this.getAttributeIF(MdAttributeDimensionInfo.DEFINING_MD_DIMENSION) ).dereference();
  }

  public void setDefiningMdDimension(MdDimensionDAOIF mdDimensionDAOIF)
  {
    this.getAttribute(MdAttributeDimensionInfo.DEFINING_MD_DIMENSION).setValue(mdDimensionDAOIF.getId());
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeDimensionDAO getBusinessDAO()
  {    
    return (MdAttributeDimensionDAO) super.getBusinessDAO();
  }

  /**
   * This is a hook method for aspects. It records the resultant key in a cache.
   * 
   */
  private void setAndBuildKey()
  {
    MdAttributeDAOIF mdAttribute = this.definingMdAttribute();
    MdDimensionDAOIF mdDimension = this.definingMdDimension();

    String key = buildKey(mdDimension, mdAttribute);

    this.setKey(key);
  }

  private String buildKey(MdDimensionDAOIF mdDimension, MdAttributeDAOIF mdAttribute)
  {
    if (mdDimension != null && mdAttribute != null)
    {
      return mdDimension.getKey() + "-" + mdAttribute.getKey();
    }

    return this.getId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdAttributeDimensionDAOIF get(String id)
  {
    return (MdAttributeDimensionDAOIF) BusinessDAO.get(id);
  }

  public static MdAttributeDimensionDAO newInstance()
  {
    return (MdAttributeDimensionDAO) BusinessDAO.newInstance(MdAttributeDimensionInfo.CLASS);
  }

  @Override
  public String getPermissionKey()
  {
    MdAttributeDAOIF mdAttribute = this.definingMdAttribute();
    MdDimensionDAOIF mdDimension = this.definingMdDimension();

    return MdAttributeDimensionDAO.getPermissionKey(mdAttribute, mdDimension);
  }

  public static String getPermissionKey(MdAttributeDAOIF mdAttribute, MdDimensionDAOIF mdDimension)
  {
    return mdAttribute.getPermissionKey() + "#" + mdDimension.getPermissionKey();
  }

}
