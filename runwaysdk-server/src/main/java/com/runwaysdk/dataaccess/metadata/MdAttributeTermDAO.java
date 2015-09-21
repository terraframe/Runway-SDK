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
/**
 * 
 */
package com.runwaysdk.dataaccess.metadata;

import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class MdAttributeTermDAO extends MdAttributeReferenceDAO implements MdAttributeTermDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -3204754608933074792L;

  public MdAttributeTermDAO()
  {
    super();
  }

  /**
   * Constructs a MdAttributeReference from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   * 
   * 
   * @param attributeMap
   * @param classType
   */
  public MdAttributeTermDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeTermDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeTermDAO(attributeMap, classType);
  }

  /**
   * Returns the metadata object that defines the MdBusiness type that this
   * attribute referenes, or null if it does not reference anything.
   * 
   * @return the metadata object that defines the MdBusiness type that this
   *         attribute referenes, or null if it does not reference anything.
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
  public MdAttributeTermDAO getBusinessDAO()
  {
    return (MdAttributeTermDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributeReference. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   * 
   * @return MdAttributeReference.
   */
  public static MdAttributeTermDAO newInstance()
  {
    return (MdAttributeTermDAO) BusinessDAO.newInstance(MdAttributeTermInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdAttributeTermDAOIF get(String id)
  {
    return (MdAttributeTermDAOIF) BusinessDAO.get(id);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitTerm(this);
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

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.MdAttributeTermDAOIF#addAttributeRoot(com.runwaysdk
   * .dataaccess.BusinessDAO)
   */
  @Override
  public void addAttributeRoot(BusinessDAO term, Boolean selectable)
  {
    String relationshipType = this.getAttributeRootRelationshipType();

    RelationshipDAO relationship = RelationshipDAO.newInstance(this.getId(), term.getId(), relationshipType);
    relationship.setValue(MdAttributeTermInfo.SELECTABLE, selectable.toString());
    relationship.apply();
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
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.TermAttributeDAOIF#getAttributeRootRelationshipType()
   */
  @Override
  public String getAttributeRootRelationshipType()
  {
    MdTermDAOIF mdTerm = this.getReferenceMdBusinessDAO();
    String relationshipType = mdTerm.getTermAttributeRootsRelationshipType();
    
    return relationshipType;
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeTermDAOIF.class.getName();
  }
}
