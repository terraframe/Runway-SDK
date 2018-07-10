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
package com.runwaysdk.query.sql;

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
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdTableClassIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.metadata.ForbiddenMethodException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAOVisitor;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public abstract class MdAttributeConcrete_SQL implements MdAttributeConcreteDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -1896729468253905009L;

  protected String          displayLabel;

  protected String          attributeName;

  protected String          unsupportedOperationMessage;

  protected String          spoofedId;

  /**
   * Used to spoof metadata for writing SQL directly to the {@link ValueQuery}
   * API.
   * 
   * @param attributeName
   * @param displayLabel
   */
  public MdAttributeConcrete_SQL(String attributeName, String displayLabel)
  {
    this.attributeName = attributeName;
    this.displayLabel = displayLabel;

    this.unsupportedOperationMessage = this.getClass().getName() + " is used for custom SQL and has no associated data.";

    this.spoofedId = ServerIDGenerator.nextID();
  }

  /**
   * The object's id is the hash code.
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
    String errorMsg = "Signatures are not supported on temporary SQL metadata";
    throw new UnsupportedOperationException(errorMsg);
  }

  /**
   * Returns the site from which this object is mastered.
   * 
   * @return site from which this object is mastered.
   */
  public String getSiteMaster()
  {
    String errorMsg = "Site master is not supported on temporary SQL metadata";
    throw new UnsupportedOperationException(errorMsg);
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
   * Sets the name of the attribute that this metadata defines.
   * 
   * @param attributeName
   */
  public void setDefinedAttributeName(String attributeName)
  {
    this.attributeName = attributeName;
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

  public String attributeMdDTOType()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public MdTableClassIF definedByClass()
  {
    return null;
  }

  public String definesAttribute()
  {
    return this.attributeName;
  }

  /**
   * Returns the name of the column in the database.
   * 
   * @return the name of the column in the database.
   */
  public String getColumnName()
  {
    return this.definesAttribute();
  }

  /**
   * Returns the name of the column in the database as it is in this metadata.
   * 
   * @return name of the column in the database as it is in this metadata.
   */
  public String getDefinedColumnName()
  {
    return this.definesAttribute();
  }

  public abstract String getType();

  public String getDefaultValue()
  {
    return "";
  }

  /**
   * If a default value has been defined for a dimension attached to this
   * session, then that value is returned, otherwise the default value assigned
   * to the attribute definition is returned.
   * 
   * @return default value
   */
  public String getAttributeInstanceDefaultValue()
  {
    return "";
  }

  public MdAttributeConcreteDAOIF getMdAttributeConcrete()
  {
    return this;
  }

  public boolean isImmutable()
  {
    return false;
  }

  public boolean isRequired()
  {
    return false;
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
    return false;
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
    return false;
  }

  public boolean isSystem()
  {
    return false;
  }

  public boolean isRemovable()
  {
    return false;
  }

  public boolean isNew()
  {
    return false;
  }

  public String getDisplayLabel(Locale locale)
  {
    return this.displayLabel;
  }

  public String getDescription(Locale locale)
  {
    return "";
  }

  public boolean isPartOfIndexedAttributeGroup()
  {
    return false;
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#isUnique()
   */
  public boolean isUnique()
  {
    return false;
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#hasNonUniqueIndex()
   */
  public boolean hasNonUniqueIndex()
  {
    return false;
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#hasIndividualIndex()
   */
  public boolean hasIndividualIndex()
  {
    return false;
  }

  public void setRandomValue(EntityDAO object)
  {
  }

  public VisibilityModifier getGetterVisibility()
  {
    return VisibilityModifier.PUBLIC;
  }

  public VisibilityModifier getSetterVisibility()
  {
    return VisibilityModifier.PUBLIC;
  }

  public String generatedClientGetter()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public String generatedClientSetter()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public String generatedServerGetter()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public String generatedServerSetter()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public Map<String, String> getDisplayLabels()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public String javaType(boolean isDTO)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public String queryAttributeClass()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public String setterWrapper(String value)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public Map<String, String> getDescriptions()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public StateMasterDAOIF currentState()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public List<RelationshipDAOIF> getAllChildren()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public List<RelationshipDAOIF> getAllParents()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public AttributeIF getAttributeIF(String name)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public BusinessDAO getBusinessDAO()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public List<RelationshipDAOIF> getChildren(String relationshipType)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public MdBusinessDAOIF getMdBusinessDAO()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public List<RelationshipDAOIF> getParents(String relationshipType)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public String getStructValue(String structAttributeName, String attributeName)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public boolean hasState()
  {
    return false;
  }

  public AttributeIF[] getAttributeArrayIF()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public byte[] getBlob(String attributeName)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public EntityDAO getEntityDAO()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public List<? extends MdAttributeConcreteDAOIF> getMdAttributeDAOs()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public MdEntityDAOIF getMdClassDAO()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public long getSequence()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public boolean hasOwner()
  {
    return false;
  }

  /**
   * This is a spoofed id.
   */
  public String getId()
  {
    return this.spoofedId;
  }

  /**
   * 
   * This is a spoofed id.
   */
  public String getRootId()
  {
    return this.getId();
  }

  public String getKey()
  {
    return this.getId();
  }

  public MdAttributeDAOIF getMdAttributeDAO(String name)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public Object getObjectValue(String name)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public String getValue(String name)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public void printAttributes()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  /**
   * Throws ProgrammingErrorException as this method is not supported.
   * 
   * @throws ProgrammingErrorException
   *           as this method is not supported.
   */
  public BusinessDAO copy()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  public boolean getGenerateAccessor()
  {
    return true;
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
    throw new ForbiddenMethodException("Attribute visitor is not implemented for sql attributes yet.");
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    throw new UnsupportedOperationException();
  }
  
  /**
   * @throws UnsupportedOperationException
   */
  @Override
  public String getInterfaceClassName()
  {
    throw new UnsupportedOperationException();
  }
}
