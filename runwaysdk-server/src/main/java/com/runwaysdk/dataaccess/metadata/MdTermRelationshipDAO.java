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

package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.MdTermRelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;

public class MdTermRelationshipDAO extends MdGraphDAO implements MdTermRelationshipDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 2388573942698619502L;

  public MdTermRelationshipDAO()
  {
    super();
  }

  public MdTermRelationshipDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdTermRelationshipDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdTermRelationshipDAO(attributeMap, MdTermRelationshipInfo.CLASS);
  }

  /**
   * Returns a new MdTermRelationship. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   * 
   * @return MdTermRelationship.
   */
  public static MdTermRelationshipDAO newInstance()
  {
    return (MdTermRelationshipDAO) BusinessDAO.newInstance(MdTermRelationshipInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public com.runwaysdk.business.ontology.MdTermRelationshipDAO getBusinessDAO()
  {
    return (com.runwaysdk.business.ontology.MdTermRelationshipDAO) super.getBusinessDAO();
  }

  /**
   * Returns the MdTermRelationshipIF that defines the relationship type with
   * the given type.
   * 
   * <br/>
   * <b>Precondition:</b> relationshipType != null <br/>
   * <b>Precondition:</b> !relationshipType.trim().equals("") <br/>
   * <b>Precondition:</b> relationshipType is a valid relationship type defined
   * in the database <br/>
   * <b>Postcondition:</b> return value is not null
   * 
   * @param relationshipType
   *          type of the relationship.
   * @return MdTermRelationshipIF that defines the relationship type with the
   *         given type
   * @throws DataAccessException
   *           if the relationship type is not valid.
   */
  public static MdTermRelationshipDAOIF getMdTermRelationshipDAO(String relationshipType)
  {
    return (MdTermRelationshipDAOIF) ObjectCache.getMdEntityDAO(relationshipType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.metadata.MdRelationshipDAO#validate()
   */
  @Override
  protected void validate()
  {
    super.validate();

    MdBusinessDAOIF mdParent = this.getParentMdBusiness();

    // Ensure that the parent MdBusiness is a MdTerm
    if (! ( mdParent instanceof MdTermDAOIF ))
    {
      String parentType = mdParent.getTypeName();
      String error = "Relationship [" + definesType() + "] cannot specify [" + parentType + "] as the parent type.";
      throw new RelationshipDefinitionException(error);
    }

    MdBusinessDAOIF mdChild = this.getChildMdBusiness();

    // Ensure that the parent MdBusiness is a MdTerm
    if (! ( mdChild instanceof MdTermDAOIF ))
    {
      String childType = mdChild.getTypeName();
      String error = "Relationship [" + definesType() + "] cannot specify [" + childType + "] as the child type.";
      throw new RelationshipDefinitionException(error);
    }

  }

}
