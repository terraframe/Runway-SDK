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

import java.util.Map;

import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdDomainDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class DomainTupleDAO extends TypeTupleDAO implements DomainTupleDAOIF
{
  /**
   * Eclipse auto generated serial OID 
   */
  private static final long serialVersionUID = -6190657962345427054L;

  public DomainTupleDAO()
  {
    super();
  }

  public DomainTupleDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public DomainTupleDAO getBusinessDAO()
  {
    return (DomainTupleDAO) super.getBusinessDAO();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public DomainTupleDAO create(Map<String, Attribute> attributeMap, String type)
  {
    return new DomainTupleDAO(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static DomainTupleDAOIF get(String oid)
  {
    EntityDAOIF entityDAO = EntityDAO.get(oid);
    DomainTupleDAOIF attributeObject = (DomainTupleDAOIF) entityDAO;

    return attributeObject;
  }

  /**
   * Create a new instance of a TypeTuple
   * 
   * @return A blank TypeTuple object
   */
  public static DomainTupleDAO newInstance()
  {
    return (DomainTupleDAO) BusinessDAO.newInstance(DomainTupleDAOIF.CLASS);
  }
  
  public MdDomainDAOIF getDomain()
  {
    AttributeReferenceIF domain = (AttributeReferenceIF) this.getAttributeIF(DomainTupleDAOIF.DOMAIN);
    return (MdDomainDAOIF) domain.dereference();
  }
  
  @Override
  protected void validateUniqueness()
  {
    String metadataId = ( this.getAttribute(DomainTupleDAOIF.METADATA).getValue().equals("") ? null : this.getAttribute(DomainTupleDAOIF.METADATA).getValue());
    String statemasterId = ( this.getAttribute(DomainTupleDAOIF.STATE_MASTER).getValue().equals("") ? null : this.getAttribute(DomainTupleDAOIF.STATE_MASTER).getValue());    
    String domainId = this.getAttribute(DomainTupleDAOIF.DOMAIN).getValue();
    
    DomainTupleDAOIF tuple = DomainTupleDAO.findTuple(domainId, metadataId, statemasterId );
    
    if(tuple != null)
    {
      String msg = "A [" + this.getType() + "] already exists with the combination of [" + metadataId + "] and [" + statemasterId + "] for the domain [" + domainId + "]";      
      throw new RuntimeException(msg);
    }
  }
  
  @Override
  protected void validateRequired()
  {
    //Overwrite PermissionTuple: Metadata and state attributes are not required on Domain Tuples
  }
  
  @Override
  protected void validateMetadata()
  {
    //Overwrite PermissionTuple: Metadata does not have any restrictions in Domain Tuples
  }
  
  public static DomainTupleDAOIF findTuple(String domainId, String metadataId, String stateMasterId)
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(DomainTupleDAOIF.CLASS);
    query.WHERE(query.aReference(DomainTupleDAOIF.DOMAIN).EQ(domainId));
    
    if(metadataId != null)
    {
      query.WHERE(query.aReference(DomainTupleDAOIF.METADATA).EQ(metadataId));
    }
    
    if(stateMasterId != null)
    {
      query.WHERE(query.aReference(DomainTupleDAOIF.STATE_MASTER).EQ(stateMasterId));
    }

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    if (iterator.hasNext())
    {
      DomainTupleDAOIF domainTuple = (DomainTupleDAOIF) iterator.next();
      iterator.close();
      return domainTuple;
    }

    return null;
  }
  
  public String getPermissionKey()
  {
    String metadataId = this.getAttribute(DomainTupleDAOIF.METADATA).getValue();
    String stateId = this.getAttribute(DomainTupleDAOIF.STATE_MASTER).getValue(); 
    String domainId = this.getAttribute(DomainTupleDAOIF.DOMAIN).getValue();
    
    return DomainTupleDAO.buildKey(domainId, metadataId, stateId);
  }
  
  public static String buildKey(String domainId, String metadataId, String stateId)
  {
    StringBuffer key = new StringBuffer();
    key.append(domainId);
    
    if(metadataId != null && !metadataId.equals(""))
    {
      key.append(DELIMETER + metadataId);
    }
    
    if(stateId != null && !stateId.equals(""))
    {
      key.append(DELIMETER + stateId);
    }
    
    return key.toString();
  }

}
