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
package com.runwaysdk.dataaccess.attributes.value;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.metadata.ForbiddenMethodException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAOVisitor;
import com.runwaysdk.session.Session;

public abstract class MdAttributeConcrete_Q implements MdAttributeConcreteDAOIF
{
  /**
   *
   */
  private static final long          serialVersionUID = -5477994465332165409L;

  protected MdAttributeConcreteDAOIF mdAttributeConcreteIF;

  protected String                   displayLabel;

  protected String                   definedAttributeName;

  protected String                   columnName;

  /**
   * Used in value objects with attributes that contain values that are the
   * result of functions, where the function result data type does not match the
   * datatype of the column.
   * 
   * @param mdAttributeConcreteIF
   *          metadata that defines the column.
   */
  public MdAttributeConcrete_Q(MdAttributeConcreteDAOIF mdAttributeConcreteIF)
  {
    this.mdAttributeConcreteIF = mdAttributeConcreteIF;
    this.displayLabel = mdAttributeConcreteIF.getDisplayLabel(Session.getCurrentLocale());
    this.definedAttributeName = mdAttributeConcreteIF.definesAttribute();
    this.columnName = mdAttributeConcreteIF.getColumnName();
  }

  /**
   * The object's key is the hash code.
   * 
   */
  public int hashCode()
  {
    return this.getKey().hashCode();
  }

  /**
   * Returns the signature of the metadata.
   * 
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    String errorMsg = "Signatures are not supported on temporary query metadata";
    throw new UnsupportedOperationException(errorMsg);
  }

  /**
   * Returns the site from which this object is mastered.
   * 
   * @return site from which this object is mastered.
   */
  public String getSiteMaster()
  {
    return this.mdAttributeConcreteIF.getSiteMaster();
  }

  /**
   * Returns the concrete attribute representing this attribute. This is a
   * concrete attribute so itself is returned.
   * 
   * @return concrete attribute representing this attribute.
   */
  public MdAttributeConcreteDAOIF getMdAttributeConcrete()
  {
    return this;
  }

  /**
   * Returns true if this object originated from a cache, false otherwise.
   * 
   * @return true if this object originated from a cache, false otherwise.
   */
  public boolean isFromCache()
  {
    return false;
  }

  @Override
  public boolean isAppliedToDB()
  {
    return false;
  }

  /**
   * If true then this object originated from a cache, false otherwise.
   * 
   * @param isFromCache
   */
  public void setIsFromCacheMRU(boolean isFromCache)
  {
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#attributeMdDTOType()
   */
  public abstract String attributeMdDTOType();

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#definedByClass()
   */
  public MdClassDAOIF definedByClass()
  {
    return this.mdAttributeConcreteIF.definedByClass();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#definesAttribute()
   */
  public String definesAttribute()
  {
    return this.definedAttributeName;
  }

  /**
   * Sets the name of the attribute that this metadata defines.
   * 
   * @param attributeName
   */
  public void setDefinedAttributeName(String attributeName)
  {
    this.definedAttributeName = attributeName;
  }

  /**
   * Returns the name of the column in the database.
   * 
   * @return the name of the column in the database.
   */
  public String getColumnName()
  {
    return this.columnName;
  }

  /**
   * Returns the name of the column in the database as it is in this metadata.
   * 
   * @return name of the column in the database as it is in this metadata.
   */
  public String getDefinedColumnName()
  {
    return this.columnName;
  }

  /**
   * Sets the name of the column in the database.
   * 
   * @param columnName
   *          .
   */
  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }

  /**
   * Resets the attribute name and display label to the original value.
   */
  public void resetAttributeNameAndLabel()
  {
    this.definedAttributeName = this.mdAttributeConcreteIF.definesAttribute();
    this.displayLabel = this.mdAttributeConcreteIF.getDisplayLabel(Session.getCurrentLocale());
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#generatedServerGetter()
   */
  public String generatedServerGetter()
  {
    return this.mdAttributeConcreteIF.generatedServerGetter();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#generatedClientGetter()
   */
  public String generatedClientGetter()
  {
    return this.mdAttributeConcreteIF.generatedClientGetter();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#generatedServerSetter()
   */
  public String generatedServerSetter()
  {
    return this.mdAttributeConcreteIF.generatedServerSetter();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#generatedClientSetter()
   */
  public String generatedClientSetter()
  {
    return this.mdAttributeConcreteIF.generatedClientSetter();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeDAOIF#getDefaultValue()
   */
  public String getDefaultValue()
  {
    return this.mdAttributeConcreteIF.getDefaultValue();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeDAOIF#getAttributeInstanceDefaultValue()
   */
  public String getAttributeInstanceDefaultValue()
  {
    return this.mdAttributeConcreteIF.getAttributeInstanceDefaultValue();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getGetterVisibility()
   */
  public VisibilityModifier getGetterVisibility()
  {
    return this.mdAttributeConcreteIF.getGetterVisibility();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getSetterVisibility()
   */
  public VisibilityModifier getSetterVisibility()
  {
    return this.mdAttributeConcreteIF.getSetterVisibility();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#isImmutable()
   */
  public boolean isImmutable()
  {
    return this.mdAttributeConcreteIF.isImmutable();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#isPartOfIndexedAttributeGroup()
   */
  public boolean isPartOfIndexedAttributeGroup()
  {
    return this.mdAttributeConcreteIF.isPartOfIndexedAttributeGroup();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#isRequired()
   */
  public boolean isRequired()
  {
    return this.mdAttributeConcreteIF.isRequired();
  }

  /**
   * Returns true if the current session is associated with a dimension and that
   * dimension requires this attribute.
   * 
   * @return true if the current session is associated with a dimension and that
   *         dimension requires this attribute.
   */
  public boolean isDimensionRequired()
  {
    return this.mdAttributeConcreteIF.isDimensionRequired();
  }

  /**
   * Returns true if the metadata for this attribute on a DTO needs to be marked
   * as required, false otherwise.
   * 
   * @return true if the metadata for this attribute on a DTO needs to be marked
   *         as required, false otherwise.
   */
  public boolean isRequiredForDTO()
  {
    return this.mdAttributeConcreteIF.isRequiredForDTO();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#isSystem()
   */
  public boolean isSystem()
  {
    return this.mdAttributeConcreteIF.isSystem();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#isUnique()
   */
  public boolean isUnique()
  {
    return this.mdAttributeConcreteIF.isUnique();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#hasNonUniqueIndex()
   */
  public boolean hasNonUniqueIndex()
  {
    return this.mdAttributeConcreteIF.hasNonUniqueIndex();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#hasIndividualIndex()
   */
  public boolean hasIndividualIndex()
  {
    return this.mdAttributeConcreteIF.hasIndividualIndex();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#javaType(boolean)
   */
  public String javaType(boolean isDTO)
  {
    return this.mdAttributeConcreteIF.javaType(isDTO);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#queryAttributeClass()
   */
  public String queryAttributeClass()
  {
    return this.mdAttributeConcreteIF.queryAttributeClass();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#setRandomValue(EntityDAO)
   */
  public void setRandomValue(EntityDAO object)
  {
    this.mdAttributeConcreteIF.setRandomValue(object);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#setterWrapper(String)
   */
  public String setterWrapper(String value)
  {
    return this.mdAttributeConcreteIF.setterWrapper(value);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getDescription(locale)
   */
  public String getDescription(Locale locale)
  {
    return this.mdAttributeConcreteIF.getDescription(locale);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getDisplayLabel(Locale)
   */
  public String getDisplayLabel(Locale locale)
  {
    return this.displayLabel;
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
    return this.mdAttributeConcreteIF.getDisplayLabels();
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public Map<String, String> getDescriptions()
  {
    return this.mdAttributeConcreteIF.getDescriptions();
  }

  /**
   * Sets the display label.
   * 
   * @param displayLabel
   */
  public void setDisplayLabel(String displayLabel)
  {
    this.displayLabel = displayLabel;
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#isRemovable()
   */
  public boolean isRemovable()
  {
    return this.mdAttributeConcreteIF.isRemovable();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#currentState()
   */
  public StateMasterDAOIF currentState()
  {
    return this.mdAttributeConcreteIF.currentState();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getAllChildren()
   */
  public List<RelationshipDAOIF> getAllChildren()
  {
    return this.mdAttributeConcreteIF.getAllChildren();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getAllParents()
   */
  public List<RelationshipDAOIF> getAllParents()
  {
    return this.mdAttributeConcreteIF.getAllParents();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getAttributeIF(String)
   */
  public AttributeIF getAttributeIF(String name)
  {
    return this.mdAttributeConcreteIF.getAttributeIF(name);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getBusinessDAO()
   */
  public BusinessDAO getBusinessDAO()
  {
    return this.mdAttributeConcreteIF.getBusinessDAO();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getChildren(String)
   */
  public List<RelationshipDAOIF> getChildren(String relationshipType)
  {
    return this.mdAttributeConcreteIF.getChildren(relationshipType);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getMdBusinessDAO()
   */
  public MdBusinessDAOIF getMdBusinessDAO()
  {
    return this.mdAttributeConcreteIF.getMdBusinessDAO();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getParents(String)
   */
  public List<RelationshipDAOIF> getParents(String relationshipType)
  {
    return this.mdAttributeConcreteIF.getParents(relationshipType);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getStructValue(String,
   *      String)
   */
  public String getStructValue(String structAttributeName, String attributeName)
  {
    return this.mdAttributeConcreteIF.getStructValue(structAttributeName, attributeName);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#hasState()
   */
  public boolean hasState()
  {
    return this.mdAttributeConcreteIF.hasState();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getAttributeArrayIF()
   */
  public AttributeIF[] getAttributeArrayIF()
  {
    return this.mdAttributeConcreteIF.getAttributeArrayIF();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getBlob(String)
   */
  public byte[] getBlob(String attributeName)
  {
    return this.mdAttributeConcreteIF.getBlob(attributeName);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getEntityDAO()
   */
  public EntityDAO getEntityDAO()
  {
    return this.mdAttributeConcreteIF.getEntityDAO();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getMdAttributeDAOs()
   */
  public List<? extends MdAttributeConcreteDAOIF> getMdAttributeDAOs()
  {
    return this.mdAttributeConcreteIF.getMdAttributeDAOs();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getMdClassDAO()
   */
  public MdEntityDAOIF getMdClassDAO()
  {
    return this.mdAttributeConcreteIF.getMdClassDAO();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getSequence()
   */
  public long getSequence()
  {
    return this.mdAttributeConcreteIF.getSequence();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#hasOwner()
   */
  public boolean hasOwner()
  {
    return this.mdAttributeConcreteIF.hasOwner();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getId()
   */
  public String getId()
  {
    return this.mdAttributeConcreteIF.getId();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getRootId()
   */
  public String getRootId()
  {
    return this.mdAttributeConcreteIF.getRootId();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getMdAttributeDAO(String)
   */
  public MdAttributeDAOIF getMdAttributeDAO(String name)
  {
    return this.mdAttributeConcreteIF.getMdAttributeDAO(name);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getType()
   */
  public abstract String getType();

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getKey()
   */
  public String getKey()
  {
    return this.mdAttributeConcreteIF.getKey();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getValue(String)
   */
  public String getValue(String name)
  {
    return this.mdAttributeConcreteIF.getValue(name);
  }

  /**
   * Some attributes store objects instead of strings.
   * 
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue(String name)
  {
    return this.getValue(name);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#isNew()
   */
  public boolean isNew()
  {
    return this.mdAttributeConcreteIF.isNew();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#printAttributes()
   */
  public void printAttributes()
  {
    this.mdAttributeConcreteIF.printAttributes();
  }

  /**
   * Throws UnsupportedOperationException as this method is not supported.
   * 
   * @throws UnsupportedOperationException
   *           as this method is not supported.
   */
  public BusinessDAO copy()
  {
    String errorMsg = "This method copy() cannot be called on [" + this.getClass().getName() + "] classes on query attributes";

    throw new UnsupportedOperationException(errorMsg);
  }

  /**
   * Throws UnsupportedOperationException when an unsupported method in this
   * hierarchy is called.
   * 
   * @throws UnsupportedOperationException
   *           when an unsupported method in this hierarchy is called.
   */
  protected void unsupportedBusinessDAO()
  {
    String errorMsg = "This method getBusinessDAO() cannot be called on [" + this.getClass().getName() + "] classes on query attributes";

    throw new UnsupportedOperationException(errorMsg);
  }

  public boolean getGenerateAccessor()
  {
    return this.mdAttributeConcreteIF.getGenerateAccessor();
  }

  public String getPermissionKey()
  {
    return this.getId();
  }

  public MdAttributeDimensionDAOIF getMdAttributeDimension(MdDimensionDAOIF mdDimension)
  {
    return null;
  }

  public List<MdAttributeDimensionDAOIF> getMdAttributeDimensions()
  {
    return new LinkedList<MdAttributeDimensionDAOIF>();
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    throw new ForbiddenMethodException("Attribute visitor is not implemented for value attributes yet.");
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return this.getMdAttributeConcrete().getInterfaceClassName();
  }
}
