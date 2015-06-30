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
package com.runwaysdk.dataaccess.cache;

import java.util.Hashtable;

import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;


public class RoleStrategy extends CacheAllBusinessDAOstrategy
{

  /**
   * 
   */
  private static final long serialVersionUID = -2273432977596883353L;
  
  /**
   * Maps table names to the Role objects that use them to store object instances.
   * The key is the name of the role.
   */
  private Hashtable<String, RoleDAO> roleMap;

  /**
   * Initializes the EntityDAOCollection with the all Role objects.
   *
   * <br/><b>Precondition:</b>  entityName != null
   * <br/><b>Precondition:</b>  !entityName.trim().equals("")
   *
   * @param type name of the type of the objects in this collection )
   */
  public RoleStrategy(String type)
  {
    super(type);
    this.roleMap = new Hashtable<String, RoleDAO>();
  }


  /**
   *Returns the Role object with the given name.
   *
   * <br/><b>Precondition:</b>  roleName != null
   * <br/><b>Precondition:</b>  !roleNametrim().equals("")
   * <br/><b>Precondition:</b>  return value is not null
   *
   * @param  roleName Name of the Role.
   * @return Role with the given name.
   */
  public synchronized RoleDAOIF getRole(String roleName)
  {
    if (roleName==null)
    {
      String error = "Role Name is null";
      throw new DataNotFoundException(error, MdTypeDAO.getMdTypeDAO(RoleDAOIF.CLASS));
    }

    if (reload == true)
    {
      this.reload();
    }

    RoleDAO role = this.roleMap.get(roleName);

    if (role == null)
    {
      QueryFactory qFactory = new QueryFactory();
      BusinessDAOQuery roleQ = qFactory.businessDAOQuery(RoleDAOIF.CLASS);
      roleQ.WHERE(roleQ.aCharacter(RoleDAOIF.ROLENAME).EQ(roleName));

      OIterator<BusinessDAOIF> roleIterator = roleQ.getIterator();

      if (roleIterator.hasNext())
      {
        role = (RoleDAO) roleIterator.next();
      }
      roleIterator.close();
    }

    // If it is not in the cache, perhapse it was marked to be refreshed by removing it
    // from the cache.
    if (role == null)
    {
      String error = "Role [" + roleName + "] is not defined.";
      throw new DataNotFoundException(error, MdTypeDAO.getMdTypeDAO(RoleDAOIF.CLASS));
    }
    else
    {
      this.roleMap.put(role.getRoleName(), role);
    }

    return role;
  }

  /**
   *Reloads the cache of this collection.  The cache is cleared.  All EntityDAOs of this
   * type stored in this collection are instantiated an put in the cache.  This method
   * uses the database instead of the metadata cache.
   *
   * <br/><b>Precondition:</b>   true
   *
   * <br/><b>Postcondition:</b>  Cache contains all EntityDAOs of this type that are to be stored in this
   *        collection
   *
   */
  public synchronized void reload()
  {
    super.reload();

    this.roleMap.clear();
  }

  /**
   * Places the given EntityDAO into the cache.
   *
   * <br/><b>Precondition:</b>  mdEntity != null
   * <br/><b>Precondition:</b>  mdEntity.getTypeName().equals(RoleIF.CLASS)
   *
   * <br/><b>Postcondition:</b> cache contains the given EntityDAO
   *
   * @param  mdEntity EntityDAO to add to this collection
   */
  public void updateCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.updateCache(entityDAO);

      this.roleMap.put(((RoleDAO)entityDAO).getRoleName(), (RoleDAO)entityDAO);
    }
  }


  /**
   * Removes the given EntityDAO from the cache.
   *
   * <br/><b>Precondition:</b>  entityDAO != null
   * <br/><b>Precondition:</b>  entityDAO.getTypeName().equals(RoleIF.CLASS)
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given EntityDAO
   *
   * @param  mdEntity EntityDAO to remove from this collection
   */
  public void removeCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.removeCache(entityDAO);

      this.roleMap.remove(((RoleDAO)entityDAO).getRoleName());
    }
  }
}
