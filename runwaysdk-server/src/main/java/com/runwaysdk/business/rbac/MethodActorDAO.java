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
package com.runwaysdk.business.rbac;

import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.MethodActorInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;

public class MethodActorDAO extends SingleActorDAO implements MethodActorDAOIF
{
  /**
   * Auto generated eclipse serial id
   */
  private static final long serialVersionUID = 2195486982087552872L;

  /**
   * The default constructor, does not set any attributes
   */
  public MethodActorDAO()
  {
    super();
  }

  /**
   * @param attributeMap
   */
  public MethodActorDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MethodActorDAO getBusinessDAO()
  {
    return (MethodActorDAO) super.getBusinessDAO();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MethodActorDAO create(Map<String, Attribute> attributeMap, String type)
  {
    return new MethodActorDAO(attributeMap, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.rbac.MethodActorIF#getMdMethod()
   */
  public MdMethodDAOIF getMdMethodDAO()
  {
    String id = this.getValue(MethodActorInfo.MD_METHOD);

    MdMethodDAOIF mdMethodIF = MdMethodDAO.get(id);
    return mdMethodIF;
  }

  public static MethodActorDAO newInstance()
  {
    return (MethodActorDAO) BusinessDAO.newInstance(MethodActorInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String,
   * java.lang.String)
   */
  public static MethodActorDAOIF get(String id)
  {
    return (MethodActorDAOIF) BusinessDAO.get(id);
  }

  @Override
  public String getSingleActorName()
  {
    MdMethodDAOIF mdMethod = getMdMethodDAO();

    return mdMethod.getName();
  }

  @Override
  public String apply()
  {
    MdMethodDAOIF mdMethodDAOIF = this.getMdMethodDAO();
    String mdMethodKey = buildKey(mdMethodDAOIF.getEnclosingMdTypeDAO().definesType(), mdMethodDAOIF.getName());
    this.setKey(mdMethodKey);
      
    boolean appliedToDB = this.isAppliedToDB();
    
    String id = super.apply();

    if (this.isNew() && !appliedToDB)
    {
      if (!this.isImport())
      {
        String mdMethodId = this.getValue(MethodActorInfo.MD_METHOD);
        String relationshipType = RelationshipTypes.MD_METHOD_METHOD_ACTOR.getType();
        RelationshipDAO relationshipDAO = RelationshipDAO.newInstance(mdMethodId, id, relationshipType);
        relationshipDAO.setKey(buildKeyForMdMethodMethodActor(mdMethodKey));
        relationshipDAO.apply();
      }
    }
    else
    {
      Attribute keyAttribute = this.getAttribute(MethodActorInfo.KEY);

      if (keyAttribute.isModified())
      {
        List<RelationshipDAOIF> relList = this.getParents(RelationshipTypes.MD_METHOD_METHOD_ACTOR.getType());
        for (RelationshipDAOIF relationshipDAOIF : relList)
        {
          RelationshipDAO relationshipDAO = relationshipDAOIF.getRelationshipDAO();
          relationshipDAO.setKey(buildKeyForMdMethodMethodActor(mdMethodKey));
          relationshipDAO.apply();
        }
      }
    }

    return id;
  }

  private static String buildKeyForMdMethodMethodActor(String mdMethodKey)
  {
    return mdMethodKey+"."+RelationshipTypes.MD_METHOD_METHOD_ACTOR.getTableName();
  }
  
  public static String buildKey(String methodEnclosingDefiningType, String methodName)
  {
    return methodEnclosingDefiningType + "." + methodName;
  }
}
