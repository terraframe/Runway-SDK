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
/**
 * 
 */
package com.runwaysdk.dataaccess.metadata;

import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdAttributeMultiTermDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.AttributeMultiTermMdDTO;

public class MdAttributeMultiTermDAO extends MdAttributeMultiReferenceDAO implements MdAttributeMultiTermDAOIF
{
  /**
  *
  */
  private static final long serialVersionUID = 5175941555575362814L;

  public MdAttributeMultiTermDAO()
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
  public MdAttributeMultiTermDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Map, String)
   */
  public MdAttributeMultiTermDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeMultiTermDAO(attributeMap, classType);
  }

  /**
   * Returns the signature of the metadata.
   * 
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return super.getSignature() + " MultiTerm:" + this.getReferenceMdBusinessDAO().definesType();
  }

  /**
   * Returns the type of AttributeMdDTO this MdAttributeMultiTerm requires at the DTO Layer.
   * 
   * @return class name of the AttributeMdDTO to represent this MdAttributeMultiTerm
   */
  @Override
  public String attributeMdDTOType()
  {
    return AttributeMultiTermMdDTO.class.getName();
  }

  /**
   * Returns the metadata object that defines the MdBusiness type that this attribute referenes, or null if it does not reference anything.
   * 
   * @return the metadata object that defines the MdBusiness type that this attribute referenes, or null if it does not reference anything.
   */
  public MdTermDAOIF getReferenceMdBusinessDAO()
  {
    return (MdTermDAOIF) super.getReferenceMdBusinessDAO();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeMultiTermDAO getBusinessDAO()
  {
    return (MdAttributeMultiTermDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new BusinessDAO. Some attributes will contain default values, as defined in the attribute metadata. Otherwise, the attributes will be blank.
   * 
   * @return BusinessDAO instance of MdAttributeMultiTerm
   */
  public static MdAttributeMultiTermDAO newInstance()
  {
    return (MdAttributeMultiTermDAO) BusinessDAO.newInstance(MdAttributeMultiTermInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdAttributeMultiTermDAOIF get(String id)
  {
    return (MdAttributeMultiTermDAOIF) BusinessDAO.get(id);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitMultiTerm(this);
  }

  /**
   * Validates this metadata object.
   * 
   * @throws DataAccessException
   *           when this MetaData object is not valid.
   */
  protected void validate()
  {
    MdBusinessDAOIF reference = super.getReferenceMdBusinessDAO();

    if (reference != null && ! ( reference instanceof MdTermDAOIF ))
    {
      String message = "Attribute [" + MdAttributeTermInfo.REF_MD_ENTITY + "] must reference a MdTerm";

      throw new InvalidReferenceException(message, this);
    }
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeMultiTermDAOIF.class.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeTermDAOIF#addAttributeRoot(com.runwaysdk .dataaccess.BusinessDAO)
   */
  @Override
  public RelationshipDAO addAttributeRoot(BusinessDAO term, Boolean selectable)
  {
    String relationshipType = this.getAttributeRootRelationshipType();

    RelationshipDAO relationship = RelationshipDAO.newInstance(this.getId(), term.getId(), relationshipType);
    relationship.setValue(MdAttributeTermInfo.SELECTABLE, selectable.toString());
    relationship.apply();

    return relationship;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeTermDAOIF#getAllAttributeRoots()
   */
  @Override
  public List<RelationshipDAOIF> getAllAttributeRoots()
  {
    String relationshipType = this.getAttributeRootRelationshipType();

    return this.getChildren(relationshipType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.TermAttributeDAOIF#getRootRelationshipType()
   */
  @Override
  public String getAttributeRootRelationshipType()
  {
    MdTermDAOIF mdTerm = this.getReferenceMdBusinessDAO();
    String relationshipType = mdTerm.getTermAttributeRootsRelationshipType();

    return relationshipType;
  }

}
