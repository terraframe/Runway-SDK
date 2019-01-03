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
package com.runwaysdk.dataaccess;

import java.util.Map;

import com.runwaysdk.constants.IndexAttributeInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.metadata.AttributeInvalidUniquenessConstraintException;
import com.runwaysdk.dataaccess.metadata.InvalidAttributeForIndexDefinitionException;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.RequiredUniquenessConstraintException;

public class IndexAttributeDAO extends TreeDAO implements IndexAttributeIF, SpecializedDAOImplementationIF
{

  /**
   *
   */
  private static final long serialVersionUID = -5872127334611967892L;

  public IndexAttributeDAO(String parentOid, String childOid, Map<String, Attribute> attributeMap, String relationshipType)
  {
    super(parentOid, childOid, attributeMap, relationshipType);
  }

  /**
   * Builds a key for this relationship
   * 
   * @return
   */
  private String buildKey()
  {
    return this.getParentOid()+"-"+this.getChildOid();
  }
  
  public String apply()
  {
    String key = buildKey();
    this.setKey(key);
    
    return super.apply();
  }
  
  /**
   *
   */
  public String save(boolean validateRequired)
  {
    MdIndexDAO mdIndex = ((MdIndexDAO)this.getMdIndexDAO());

    if (this.isNew())
    {
      MdEntityDAOIF mdEntityIF = mdIndex.definesIndexForEntity();

      MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttributeDAO();
      if ( (mdAttributeIF instanceof MdAttributeClobDAOIF) ||
           (mdAttributeIF instanceof MdAttributeEnumerationDAOIF))
      {
        String error = "[" + mdAttributeIF.getMdBusinessDAO().definesType()
        + "] Attributes cannot participate in a uniqueness constraint.";
        throw new AttributeInvalidUniquenessConstraintException(error, mdAttributeIF);
      }

      if (!mdAttributeIF.isRequired() && mdIndex.isUnique())
      {
        String error = "Attribute [" + mdAttributeIF.definesAttribute() + "] on type ["
        + mdEntityIF.definesType()
        + "] cannot participate in a uniqueness constraint because it isn't required.";
        throw new RequiredUniquenessConstraintException(error, mdAttributeIF);
      }

      if (!mdEntityIF.definesAttribute(mdAttributeIF) )
      {
        String devMessage =
          "Invalid index definition.  Type ["+mdEntityIF.definesType()+"] "+
          " does not define attribute["+mdAttributeIF.definesAttribute()+"]";
        throw new InvalidAttributeForIndexDefinitionException(devMessage, mdIndex, mdEntityIF, mdAttributeIF);
      }
    }

    if (mdIndex.isActive() &&
        (this.isNew() || this.isImport() || this.getAttributeIF(IndexAttributeInfo.INDEX_ORDER).isModified()))
    {
      // Drop any existing index.
      // Must be called BEFORE this relationship is created.  Otherwise, the drop
      // cannot be properly rolled back.
      mdIndex.dropIndex(false);
    }

    String oid = super.save(validateRequired);
    
    if (mdIndex.isActive() &&
        (this.isNew() || this.isImport() || this.getAttributeIF(IndexAttributeInfo.INDEX_ORDER).isModified()))
    {
      mdIndex.buildIndex();
    }
    
    return oid;
  }

  public Integer getIndexOrder()
  {
    String intValue =  this.getAttributeIF(IndexAttributeInfo.INDEX_ORDER).getValue();

    if (intValue.trim().length() == 0)
    {
      return null;
    }
    else
    {
      return Integer.parseInt(intValue);
    }
  }

  /**
   * Returns the <code>MdIndexDAO</code>.
   * @return the <code>MdIndexDAO</code>.
   */
  public MdIndexDAOIF getMdIndexDAO()
  {
    return MdIndexDAO.get(this.getParentOid());
  }

  /**
   * Returns the <code>MdAttributeConcreteDAOIF</code>.
   * @return the <code>MdAttributeConcreteDAOIF</code>.
   */
  public MdAttributeConcreteDAOIF getMdAttributeDAO()
  {
    return (MdAttributeConcreteDAOIF)MdAttributeConcreteDAO.get(this.getChildOid());
  }

  /**
   *
   * @param businessContext true if this is being called from a business context, false
   * otherwise. If true then cascading deletes of other Entity objects will happen at the Business
   * layer instead of the data access layer.
   *
   */
  public void delete(boolean businessContext)
  {     
    MdIndexDAO mdIndexDAO = (MdIndexDAO)this.getMdIndexDAO().getBusinessDAO();

    // Must be called BEFORE this relationship is deleted.  Otherwise, the drop
    // cannot be properly rolled back.
    mdIndexDAO.dropIndex(true);

    super.delete(businessContext);
  }

  /**
   * Only called from <code>MdIndexDAO.delete()</code>
   *
   * <b>Precondition</b> given <code>MdIndexDAO</code> is the same index linked to
   * this index attribute.
   *
   * @param businessContext true if this is being called from a business context, false
   * otherwise. If true then cascading deletes of other Entity objects will happen at the Business
   * layer instead of the data access layer.
   *
   */
  public void deleteFromMdIndex(MdIndexDAO mdIndexDAO, boolean businessContext)
  {
    // Must be called BEFORE this relationship is deleted.  Otherwise, the drop
    // cannot be properly rolled back.
    mdIndexDAO.dropIndex(true);

	super.delete(businessContext);
  }

  /**
   * Returns a deep cloned-copy of this Relationship
   */
  public IndexAttributeDAO getRelationshipDAO()
  {
    return (IndexAttributeDAO)super.getRelationshipDAO();
  }
}
