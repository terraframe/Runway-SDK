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
package com.runwaysdk.dataaccess.metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.session.Session;
import com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeEnumerationMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeEnumerationDAO extends MdAttributeConcreteDAO implements MdAttributeEnumerationDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = 5175941555575362814L;

  public MdAttributeEnumerationDAO()
  {
    super();
  }

  /**
   * Constructs a BusinessDAO from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   * 
   * 
   * @param attributeMap
   * @param classType
   */
  public MdAttributeEnumerationDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Map, String)
   */
  public MdAttributeEnumerationDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeEnumerationDAO(attributeMap, classType);
  }

  /**
   * Returns the signature of the metadata.
   * 
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return super.getSignature() + " Enumeration:" + this.getMdEnumerationDAO().definesType();
  }

  /**
   * Returns the default value for the attribute that this metadata defines. If
   * no default value has been defined, an empty string is returned.
   * 
   * @return the default value for the attribute that this metadata defines.
   */
  public String getDefaultValue()
  {
    return getDefaultValue(this.getAttributeIF(MdAttributeEnumerationInfo.DEFAULT_VALUE).getValue());
  }

  /**
   * Returns the type of AttributeMdDTO this MdAttributeEnumeration requires at
   * the DTO Layer.
   * 
   * @return class name of the AttributeMdDTO to represent this
   *         MdAttributeEnumeration
   */
  @Override
  public String attributeMdDTOType()
  {
    return AttributeEnumerationMdDTO.class.getName();
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeEnumeration_E(this));
    }
    else
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeEnumeration_S(this));
    }

  }

  /**
   * Returns the metadata object that defines the enumeration that this
   * attribute uses, or null if it does not reference anything.
   * 
   * @return the metadata object that defines the enumeration that this
   *         attribute uses, or null if it does not reference anything.
   */
  public MdEnumerationDAOIF getMdEnumerationDAO()
  {
    if (this.getAttributeIF(MdAttributeEnumerationInfo.MD_ENUMERATION).getValue().trim().equals(""))
    {
      return null;
    }
    else
    {
      AttributeReference attributeReference = (AttributeReference) this.getAttributeIF(MdAttributeEnumerationInfo.MD_ENUMERATION);

      return (MdEnumerationDAOIF) attributeReference.dereference();
    }
  }

  /**
   * Returns true if the attribute can select more than one value from the
   * master list, false otherwise.
   * 
   * @return true if the attribute can select more than one value from the
   *         master list, false otherwise.
   */
  public boolean selectMultiple()
  {
    return ( (AttributeBooleanIF) this.getAttributeIF(MdAttributeEnumerationInfo.SELECT_MULTIPLE) ).isTrue();
  }

  @Override
  protected String generatedServerGetter(String attributeName)
  {
    return generateTypesafeFormatting("getEnumValues(" + attributeName.toUpperCase() + ")");
  }

  /**
   * Typically called for java class generation, but Enum atributes require
   * special logic, which is contained in the generator. Included only to
   * satisfy the interface, this method should never be called, and will throw
   * an exception if it is.
   * 
   * @return nothing
   * @throws ForbiddenMethodException
   *           if called
   */
  @Override
  public String generatedServerSetter()
  {
    throw new ForbiddenMethodException("MdAttributeEnumeration.setString() should never be called.");
  }

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    throw new ForbiddenMethodException("MdAttributeEnumeration.setString() should never be called.");
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.metadata.MdAttribute#generateTypesafeFormatting
   * (java.lang.String)
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    return "(java.util.List<" + getMdEnumerationDAO().definesEnumeration() + ">) " + formatMe;
  }

  /**
   * Returns a string representing the query attribute class for attributes of
   * this type.
   * 
   * @return string representing the query attribute class for attributes of
   *         this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableEnumeration.class.getName();
  }

  /**
   * Returns the metadata object that defines the MdBusiness type that this
   * attribute referenes, or null if it does not reference anything.
   * 
   * @return the metadata object that defines the MdBusiness type that this
   *         attribute referenes, or null if it does not reference anything.
   */
  public MdBusinessDAOIF getReferenceMdBusinessDAO()
  {
    return this.getMdEnumerationDAO().getMasterListMdBusinessDAO();
  }

  /**
   * Called for java class generation. Returns the java type of this attribute
   * (String), which is used in the generated classes for type safety.
   * 
   * @return The java type of this attribute (String)
   */
  public String javaType(boolean isDTO)
  {
    return this.getMdEnumerationDAO().definesEnumeration();
  }

  /**
   *
   */
  public void setRandomValue(EntityDAO object)
  {
    MdEnumerationDAOIF mdEnum = getMdEnumerationDAO();
    List<BusinessDAOIF> items = mdEnum.getAllEnumItems();

    // If this enumeration is required, ensure that at least one random item is
    // selected
    if (this.isRequired())
      object.addItem(this.definesAttribute(), items.get(EntityGenerator.getRandom().nextInt(items.size())).getId());

    // If this is single select, we've already made a selection
    if (!selectMultiple())
      return;

    // In a multi-select, each enumeration item has a 50% chance of being
    // selected
    for (BusinessDAOIF item : items)
      if (EntityGenerator.getRandom().nextBoolean())
        object.addItem(this.definesAttribute(), item.getId());
  }

  /**
   * Returns the name of the database column that caches enumeration mappings.
   * 
   * @return the name of the database column that caches enumeration mappings.
   */
  public String getCacheColumnName()
  {
    if (this.getObjectState().getHashedEnumCacheColumnName() != null)
    {
      return this.getObjectState().getHashedEnumCacheColumnName();
    }
    else
    {
      return this.getAttributeIF(MdAttributeEnumerationInfo.CACHE_COLUMN_NAME).getValue();
    }
  }

  /**
   * Returns the name of the column in the database for cached enum items as it
   * is in this metadata.
   * 
   * @return name of the column in the database for cached enum items as it is
   *         in this metadata.
   */
  public String getDefinedCacheColumnName()
  {
    return this.getAttributeIF(MdAttributeEnumerationInfo.CACHE_COLUMN_NAME).getValue();
  }

  /**
   * Sets the name of a uniquely hashed temporary column for the cache column.
   * Sometimes during the middle of a transaction a temporary column is used,
   * but then cleaned up at the end of the transaction.
   * 
   * @param _hashedEnumTempCacheColumnName
   */
  public void setHashedTempCacheColumnName(String _hashedEnumTempCacheColumnName)
  {
    this.getObjectState().setHashedEnumCacheColumnName(_hashedEnumTempCacheColumnName);
  }

  /**
   * Sets isNew = false and sets all attributes to isModified = false.
   * 
   */
  public void setCommitState()
  {
    super.setCommitState();
    this.getObjectState().setHashedEnumCacheColumnName(null);
  }

  /**
   * Deletes the MdAttributeEnumeration records but does not delete mappings
   * between instances of this attribute and items in the enumeration list.
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  public void deleteButDoNotDeleteMappingInstances(boolean businessContext)
  {
    MdAttributeConcreteStrategy mdAttributeStrategy = this.getMdAttributeStrategy();

    if (mdAttributeStrategy instanceof MdAttributeEnumeration_E)
    {
      MdAttributeEnumeration_E mdAttributeEnumeration_E = (MdAttributeEnumeration_E) mdAttributeStrategy;
      mdAttributeEnumeration_E.deleteInstances = false;
    }

    this.delete(businessContext);
  }

  /**
   * Sets appliedToDB to false if the object is new, as the database will
   * rollback any newly inserted records.
   * 
   */
  public void rollbackState()
  {
    super.rollbackState();

    MdAttributeConcreteStrategy mdAttributeStrategy = this.getMdAttributeStrategy();

    if (mdAttributeStrategy instanceof MdAttributeEnumeration_E)
    {
      MdAttributeEnumeration_E mdAttributeEnumeration_E = (MdAttributeEnumeration_E) mdAttributeStrategy;
      mdAttributeEnumeration_E.deleteInstances = true;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeEnumerationDAO getBusinessDAO()
  {
    return (MdAttributeEnumerationDAO) super.getBusinessDAO();
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeEnumerationDAOIF.class.getName();
  }

  /**
  *
  */
  public String save(boolean validateRequired)
  {
    return super.save(validateRequired);
  }

  /**
   * Returns the name of the database column that caches enumeration mappings
   * for this MdAttributeEnumeration.
   * 
   * @return name of the database column that caches enumeration mappings for
   *         this MdAttributeEnumeration.
   */
  public static String getCacheDbColumnName(String enumColumnName)
  {
    return enumColumnName + MdAttributeEnumerationDAOIF.CACHE_COLUMN_DELIMITER;
  }

  /**
   * Returns a new BusinessDAO. Some attributes will contain default values, as
   * defined in the attribute metadata. Otherwise, the attributes will be blank.
   * 
   * @return BusinessDAO instance of MdAttributeEnumeration
   */
  public static MdAttributeEnumerationDAO newInstance()
  {
    return (MdAttributeEnumerationDAO) BusinessDAO.newInstance(MdAttributeEnumerationInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String,
   * java.lang.String)
   */
  public static MdAttributeEnumerationDAOIF get(String id)
  {
    return (MdAttributeEnumerationDAOIF) BusinessDAO.get(id);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitEnumeration(this);
  }

  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession()
  {

    HashMap<String, String> enumNameMap = new HashMap<String, String>();
    for (BusinessDAOIF item : this.getMdEnumerationDAO().getAllEnumItemsOrdered())
    {
      String enumName = item.getValue(EnumerationMasterInfo.NAME);
      String enumDisplayLabel = ( (AttributeLocalIF) item.getAttributeIF(EnumerationMasterInfo.DISPLAY_LABEL) ).getValue(Session.getCurrentLocale());
      enumNameMap.put(enumName, enumDisplayLabel);
    }

    AttributeEnumerationMdSession attrSes = new AttributeEnumerationMdSession(this.selectMultiple(), this.getMdEnumerationDAO().definesEnumeration(), enumNameMap);
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }
}
