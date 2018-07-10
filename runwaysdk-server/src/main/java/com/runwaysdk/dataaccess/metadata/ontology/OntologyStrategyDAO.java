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
package com.runwaysdk.dataaccess.metadata.ontology;

import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

/**
 * Class that contains behavior that is in common 
 */
public class OntologyStrategyDAO extends BusinessDAO
{

  /**
   * 
   */
  private static final long serialVersionUID = -6362468261692064228L;

  /**
   * The default constructor, does not set any attributes
   */
  public OntologyStrategyDAO(String type)
  {
    super();
    this.setTypeName(type);
  }

  /**
   * Constructs a <code>OntologyStrategyDAO</code> from the given hashtable of
   * Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public OntologyStrategyDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public OntologyStrategyDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new OntologyStrategyDAO(attributeMap, classType);
  }

  /**
   * Returns a new <code>OntologyStrategyDAO</code>. Some attributes will contain
   * default values, as defined in the attribute metadata. Otherwise, the
   * attributes will be blank.
   * 
   * @return instance of <code>MdBusinessDAO</code>.
   */
//  public static OntologyStrategyDAO newInstance()
//  {
//    return (OntologyStrategyDAO) BusinessDAO.newInstance(OntologyStrategyInfo.CLASS);
//  }

  /**
   *
   */
  public String apply()
  {
//    QueryFactory qf = new QueryFactory();
//    BusinessDAOQuery q = qf.businessDAOQuery(MdTermInfo.CLASS);
//    q.WHERE(q.aReference(MdTermInfo.STRATEGY).EQ(this));
//    
//    OIterator<BusinessDAOIF> i = q.getIterator();
//    
//    boolean keyHasBeenSet = false;
//    // Update this key
//    while(i.hasNext())
//    {
//      MdTermDAOIF mdTermDAOIF = (MdTermDAOIF)i.next();
//      this.setKey(mdTermDAOIF.getKey());
//    }
//    
//    if (!keyHasBeenSet)
//    {
      this.setKey(this.getId());
//    }
    
    return super.apply();
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public OntologyStrategyDAO getBusinessDAO()
  {
    return (OntologyStrategyDAO) super.getBusinessDAO();
  }

  
}
