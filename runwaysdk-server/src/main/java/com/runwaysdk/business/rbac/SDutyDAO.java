/**
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
 */
package com.runwaysdk.business.rbac;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.SpecializedDAOImplementationIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;


public class SDutyDAO extends BusinessDAO implements SDutyDAOIF, SpecializedDAOImplementationIF
{
   /**
   * 
   */
  private static final long serialVersionUID = 3909269570483364341L;
  
  /**
   * The default constructor, does not set any attributes
   */
  public SDutyDAO()
  {
    super();
  }

  public SDutyDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }
    
  public void setSSDSetName(String ssdName)
  {
    super.setValue(SDutyDAO.SDNAME, ssdName);
  }
    
  public String getSSDSetName()
  {
    return super.getValue(SDutyDAO.SDNAME);
  }
      
  /**
   * Returns the cardinality of a SSD Set
   * 
   * @return The cardinality of the SSD set
   */
  public int getSSDSetCardinality()
  {
    return Integer.parseInt(super.getValue(SDutyDAO.SDCARDINALITY));
  }
  
  public SDutyDAO getBusinessDAO()
  {
    return (SDutyDAO) super.getBusinessDAO();
  }
  
  /**
   * Creates a new static seperation of duty set.
   * A user cannont participate in more than 'cardinality' roles of the same SSD set.
   * @pre: name is unique with respect to all other SSD names
   * @pre: cardinality > 0
   * 
   * @param name The name of the SSD set to created
   * @param cardinality The cardinality of the SSD set
   * @return The created SSD Set
   */
  public static SDutyDAO createSSDSet(String name, int cardinality)
  {
    SDutyDAO businessDAO = SDutyDAO.newInstance();
    
    businessDAO.setValue(SDNAME, name);
    businessDAO.setValue(SDCARDINALITY, Integer.toString(cardinality));
    
    businessDAO.apply();
    
    return businessDAO;
  }
  
  /**
   * Adds an existing role to an existing SSD set.
   * @pre: role != null
   *
   * @param roleId The role to add to the SSD set
   */
  public synchronized void addSSDRoleMember(RoleDAOIF role)
  {
    //Check to ensure adding a role does not invalidate existing roles setups
    int cardinality = getSSDSetCardinality();
    Set<RoleDAOIF> set = SSDSetRoles();
    
    set.add(role);
    
    if (!validate(set, cardinality))
    {
      String error = "Role [" + role.getRoleName() + "] invalidates existing constraints on SSD ["
          + getSSDSetName() + "]";
      throw new RBACExceptionInvalidSSDConstraint(error, role, this);
    }
    else
    {
      RelationshipDAO relationshipDAO = RelationshipDAO.newInstance(role.getId(), this.getId(), SDCONFLICTINGROLES);
      relationshipDAO.apply();
    }
  } 
  

  /**
   * Removes an existing role from an existing SSD set
   * @pre: role != null
   *  
   * @param roleId The id of the role
   */
  public void deleteSSDRoleMember(RoleDAO role)
  {
    RelationshipDAO relationshipDAO = RelationshipDAO.get(role.getId(), this.getId(), SDCONFLICTINGROLES).get(0).getRelationshipDAO();
    
    relationshipDAO.delete();    
  }
  
  /**
   * Resets the cardianlity of an existing SSD Set 
   * @pre: cardinality > 0
   * 
   * @param cardinality The new cardinality of the set
   */
  public synchronized void setSSDCardinality(int cardinality)
  {
    int oldCardinality = this.getSSDSetCardinality();
    
    //If the new cardinality is greater than the old cardinality skip constraint checks
    if(cardinality >= oldCardinality)
    {
      this.setValue(SDCARDINALITY, Integer.toString(cardinality));
    }
    else if(validate(this.SSDSetRoles(), cardinality))
    {
      //Check if the new cardianlity breaks existing user-assignments
      
      this.setValue(SDCARDINALITY, Integer.toString(cardinality));
    }
    else
    {
      String error = "The new cardinality of [" + cardinality + "] is invalid for SSD ["
          + getSSDSetName() + "] due to existing user-role assignments";
      throw new RBACExceptionInvalidSSDCardinality(error, cardinality, this);
    }
  }
  
  /**
   * Returns a set of roles associated with a SSD Set
   * 
   * @return A set of all roles in the SSD set
   */
  public Set<RoleDAOIF> SSDSetRoles()
  {
    Set<RoleDAOIF> set = new TreeSet<RoleDAOIF>();
    
    List<RelationshipDAOIF> list = this.getParents(SDCONFLICTINGROLES);
    
    for(RelationshipDAOIF relationship : list)
    {
      RoleDAOIF parent = (RoleDAOIF) relationship.getParent();
      set.add(parent);
    }
    
    return set;
  }
        
  /* (non-Javadoc)
   * @see com.runwaysdk.business.rbac.SDutyIF#checkSSD(com.runwaysdk.business.rbac.SingleActorIF)
   */
  public boolean checkSSD(SingleActorDAOIF singleActorIF)
  {
    int count = 0;
    Set<RoleDAOIF> roles = SSDSetRoles();
    int cardinality = this.getSSDSetCardinality();

    for(RoleDAOIF role : roles)
    {
      if(role.isAuthorizedMember(singleActorIF))
      {
        count++;
      }
    }
    
    return (count < cardinality);
  }
    
  /**
   * Get the instance associated with a given SSDSet name
   * 
   * @param ssdName The name of the SSDSet to search for
   * @return The SSDSet instance of the ssdName
   */
  public static SDutyDAOIF findSSDSet(String ssdName)
  {
    QueryFactory qFactory = new QueryFactory();
    
    BusinessDAOQuery sDutyQ = qFactory.businessDAOQuery(CLASS);
    sDutyQ.WHERE(
        sDutyQ.aCharacter(SDNAME).EQ(ssdName));
    OIterator<BusinessDAOIF> sDutyIterator = sDutyQ.getIterator();
    
    SDutyDAO ssd = null;
    
    if (sDutyIterator.hasNext())
    {
      ssd = (SDutyDAO) sDutyIterator.next();
      sDutyIterator.close();
    }   
    else
    {
      sDutyIterator.close();
      String error = "The SSDSet [" + ssdName + "] was not found";
      throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(SDutyDAOIF.CLASS));
    }
    
    return ssd;
  }
  
  /**
   * Returns true if the number of intersections in a set of roles is less than a given cardinality
   * 
   * @param roleIds A list of ids of roles
   * @param cardinality The cardinality amount
   * @return If the number of intersections is less than the cardinality
   */
  protected static boolean validate(Set<RoleDAOIF> roles, int cardinality)
  {    
    RoleDAO[] array = new RoleDAO[roles.size()];
    array = roles.toArray(array);
        
    for(int i = 0; i < array.length; i++)
    {
      int count = 0;
      
      for(int j = i+1; j < array.length; j++)
      {
        if(array[i].intersect(array[j]))
        {
          count++;
        }
      }
      
      if(!(count < cardinality))
      {
        return false;
      }
    }
    
    return true;
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public SDutyDAO create(Map<String, Attribute> attributeMap, String type)
  {
    return new SDutyDAO(attributeMap, type);
  }
  
  /**
   * Returns a new BusinessDAO of type SDutyDAO.
   * 
   * @return new SDutyDAO instance 
   */
  public static SDutyDAO newInstance()
  {
    return (SDutyDAO) BusinessDAO.newInstance(SDutyDAOIF.CLASS);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static SDutyDAOIF get(String id)
  {
    return (SDutyDAOIF) BusinessDAO.get(id);
  }

}
