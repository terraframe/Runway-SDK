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

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdClassDimensionInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdClassDimensionDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.util.IdParser;

public class MdClassDimensionDAO extends MetadataDAO implements MdClassDimensionDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 5874183721975001343L;

  public MdClassDimensionDAO()
  {
    super();
  }

  public MdClassDimensionDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public MdClassDimensionDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdClassDimensionDAO(attributeMap, MdClassDimensionInfo.CLASS);
  }

  @Override
  public String apply()
  {
    this.buildAndSetKey();

    if (this.isNew() && this.isAppliedToDB() == false && !this.isImport())
    {
      String newId = IdParser.buildId(ServerIDGenerator.generateId(this.getKey()), this.getMdClassDAO().getRootId());
      this.getAttribute(EntityInfo.OID).setValue(newId);
    }

    return super.apply();
  }

  private void buildAndSetKey()
  {
    MdClassDAOIF mdClass = this.definingMdClass();
    MdDimensionDAOIF mdDimension = this.definingMdDimension();

    String key = buildKey(mdDimension, mdClass);
    this.setKey(key);
  }

  private String buildKey(MdDimensionDAOIF mdDimension, MdClassDAOIF mdClass)
  {
    if (mdDimension != null && mdClass != null)
    {
      return mdDimension.getKey() + "-" + mdClass.getKey();
    }

    return this.getOid();
  }

  public String save(boolean businessContext)
  {
    boolean applied = !this.isNew() || this.isAppliedToDB();

    String oid = super.save(businessContext);
    
    // Create the MdDimension relationship
    MdDimensionDAOIF mdDimension = this.definingMdDimension();
    MdClassDAOIF mdClass = this.definingMdClass();
        
    if (this.isNew() && !applied)
    {
      if (!this.isImport())
      { 
        // Create the MdDimension - to - MdClassDimension relationship      
        String mdDimensionRelationshipKey = buildDimensionHasClassKey(mdDimension, mdClass);
        
        RelationshipDAO mdDimensionRelationship = RelationshipDAO.newInstance(mdDimension.getOid(), oid, RelationshipTypes.DIMENSION_HAS_CLASS.getType());
        mdDimensionRelationship.setKey(mdDimensionRelationshipKey);
        mdDimensionRelationship.apply();       

        // Create the MdClass - to - MdClassDimension relationship       
        String mdClassRelationshipKey = buildClassHasDimensionKey(mdClass, mdDimension);
        RelationshipDAO mdClassRelationship = RelationshipDAO.newInstance(mdClass.getOid(), oid, RelationshipTypes.CLASS_HAS_DIMENSION.getType());
        mdClassRelationship.setKey(mdClassRelationshipKey);
        mdClassRelationship.save(true); 
      }
    }
    else
    {
      List<RelationshipDAOIF> relList = RelationshipDAO.get(mdClass.getOid(), oid, RelationshipTypes.CLASS_HAS_DIMENSION.getType());
      
      String mdClassRelationshipKey = buildClassHasDimensionKey(mdClass, mdDimension);
        
      for (RelationshipDAOIF mdClassRelationshipDAOIF : relList)
      {
        RelationshipDAO mdClassRelationshipDAO = mdClassRelationshipDAOIF.getRelationshipDAO();
        mdClassRelationshipDAO.setKey(mdClassRelationshipKey);
        mdClassRelationshipDAO.save(true); 
      }
        
      relList = RelationshipDAO.get(mdDimension.getOid(), oid, RelationshipTypes.DIMENSION_HAS_CLASS.getType());
        
      mdClassRelationshipKey = buildDimensionHasClassKey(mdDimension, mdClass);
      
      for (RelationshipDAOIF mdClassRelationshipDAOIF : relList)
      {
        RelationshipDAO mdClassRelationshipDAO = mdClassRelationshipDAOIF.getRelationshipDAO();
        mdClassRelationshipDAO.setKey(mdClassRelationshipKey);
        mdClassRelationshipDAO.save(true); 
      }
    }

    return oid;
  }

  
  /**
   * Builds the key for the <code>RelationshipTypes.DIMENSION_HAS_CLASS.getType()</code> relationship.
   * 
   * @return key for the <code>RelationshipTypes.DIMENSION_HAS_CLASS.getType()</code> relationship.
   */
  private static String buildDimensionHasClassKey(MdDimensionDAOIF mdDimensionDAOIF, MdClassDAOIF mdClassDAOIF)
  {
    return mdDimensionDAOIF.getKey() + "-" + mdClassDAOIF.getKey();
  }
  
  /**
   * Builds the key for the <code>RelationshipTypes.CLASS_HAS_DIMENSION.getType()</code> relationship.
   * 
   * @return key for the <code>RelationshipTypes.CLASS_HAS_DIMENSION.getType()</code> relationship.
   */
  private static String buildClassHasDimensionKey(MdClassDAOIF mdClassDAOIF, MdDimensionDAOIF mdDimensionDAOIF)
  {
    return mdClassDAOIF.getKey() + "-" + mdDimensionDAOIF.getKey();
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
    return this.definingMdClass().getDisplayLabel(locale);
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
    return this.definingMdClass().getDisplayLabels();
  }

  public String getSignature()
  {
    return this.definingMdClass().getSignature();
  }

  public MdClassDAOIF definingMdClass()
  {
    return (MdClassDAOIF) ( (AttributeReferenceIF) this.getAttributeIF(MdClassDimensionInfo.DEFINING_MD_CLASS) ).dereference();
  }

  public void setDefiningMdClass(MdClassDAOIF mdClassDAOIF)
  {
    this.getAttribute(MdClassDimensionInfo.DEFINING_MD_CLASS).setValue(mdClassDAOIF.getOid());
  }

  public MdDimensionDAOIF definingMdDimension()
  {
    return (MdDimensionDAOIF) ( (AttributeReferenceIF) this.getAttributeIF(MdClassDimensionInfo.DEFINING_MD_DIMENSION) ).dereference();
  }

  public void setDefiningMdDimension(MdDimensionDAOIF mdDimensionDAOIF)
  {
    this.getAttribute(MdClassDimensionInfo.DEFINING_MD_DIMENSION).setValue(mdDimensionDAOIF.getOid());
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdClassDimensionDAO getBusinessDAO()
  {
    return (MdClassDimensionDAO) super.getBusinessDAO();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdClassDimensionDAOIF get(String oid)
  {
    return (MdClassDimensionDAOIF) BusinessDAO.get(oid);
  }

  public static MdClassDimensionDAO newInstance()
  {
    return (MdClassDimensionDAO) BusinessDAO.newInstance(MdClassDimensionInfo.CLASS);
  }

  @Override
  public String getPermissionKey()
  {
    MdClassDAOIF mdClass = this.definingMdClass();
    MdDimensionDAOIF mdDimension = this.definingMdDimension();

    return MdClassDimensionDAO.getPermissionKey(mdClass, mdDimension);
  }

  public static String getPermissionKey(MdClassDAOIF mdClass, MdDimensionDAOIF mdDimension)
  {
    return mdClass.getPermissionKey() + "#" + mdDimension.getPermissionKey();
  }

}
