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
package com.runwaysdk.session;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.business.rbac.MethodActorDAOIF;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;

/**
 * A class that caches the permissions of a given MdMethod
 * and performs access checks against the cached permissions.
 * 
 * @author Justin Smethie
 */
public class MethodSession extends PermissionEntity implements Serializable
{
  /**
   * Auto generated eclipse UID.
   */
  private static final long serialVersionUID = -8427448167325930083L;

  /**
   * The Actor which contains the permissions information
   */
  private final MethodActorDAOIF methodActorIF;
  
  public MethodSession(MethodActorDAOIF method)
  {
    super();
    
    this.methodActorIF = method;
    this.isAdmin = method.isAdministrator();
        
    this.permissions = this.methodActorIF.getOperations().getPermissions();
  }
  
  /**
   * Returns the MethodActorIF actor for this method.
   * @return MethodActorIF actor for this method.
   */
  protected MethodActorDAOIF getMethodActorIF()
  {
    return this.methodActorIF;
  }
  
  @Override
  public MethodActorDAOIF getUser()
  {
    return methodActorIF;
  }
  
  @Override
  public void notify(PermissionEvent e)
  { 
    if(e.equals(PermissionEvent.PERMISSION_CHANGE))
    {
      reloadPermissions();
    }
  }

  /**
   * Reloads all of the permissions of the method
   */
  public void reloadPermissions()
  {
    ConcurrentHashMap<String, Set<Operation>> temp = this.methodActorIF.getOperations().getPermissions();

    permissionLock.lock();
   
    this.permissions = temp;
    
    permissionLock.unlock();
  }

  @Override
  public MdDimensionDAOIF getDimension()
  {
    return Session.getCurrentDimension();
  }
}
