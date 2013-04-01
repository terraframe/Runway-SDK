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

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.rbac.MethodActorDAOIF;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.ValueObject;

/**
 * Singleton cache of the Method permissions.
 * 
 * @author Justin Smethie
 */
public class MethodCache
{
  
  /**
   * Singelton reference to the cache
   */
  private static MethodCache methodCache = null;

  /**
   * Gards to ensure that invariants between mulitple state fields hold.
   */
  private final ReentrantLock               methodCacheLock;

  /**
   * A mapping between the MdMethodId and its permissions
   */
  private HashMap<String, MethodSession> cache;
  
  /**
   * Singleton constructor
   */
  private MethodCache()
  {
    cache = new HashMap<String, MethodSession>();
    methodCacheLock = new ReentrantLock();
  }

  /**
   * Returns an MethodActorIF for the given MdMethodIF.
   * @param mdMethodIF
   * @return MethodActorIF for the given MdMethodIF.
   */
  protected MethodActorDAOIF getMethodActorIF(MdMethodDAOIF mdMethodIF)
  {
    methodCacheLock.lock();
    try
    {
      MethodSession methodSession = cache.get(mdMethodIF.getId());
    
      if (methodSession == null)
      {
        methodSession = getMethod(mdMethodIF);
      }
    
      if (methodSession == null)
      {
        return null;
      }
      else
      {
        return methodSession.getMethodActorIF();
      }
      }
      finally
      {
        methodCacheLock.unlock();
      }
    }
  
  /**
   * Returns the Method associated with the given MdMethod.
   * If the Method is not in the cache then loads the Method
   * and stores it in the cache.  However, if the MdMethod does 
   * not have any permissions then null is returned.
   * 
   * @param mdMethod1
   * @return
   */
  private MethodSession getMethod(MdMethodDAOIF mdMethodIF)
  {
    methodCacheLock.lock();
    try
    {
      String key = mdMethodIF.getId();

      //If the cache does not contain the permissions for the 
      //MdMethod then load the permissions.
      if (!cache.containsKey(key))
      {
        MethodActorDAOIF value = (MethodActorDAOIF) mdMethodIF.getMethodActor();
        
        if(value != null)
        {
          MethodSession methodSession = new MethodSession(value);
          methodSession.register();

          cache.put(key, methodSession);
        }
        else
        {
          return null;
        }
      }

      return cache.get(key);
    }
    finally
    {
      methodCacheLock.unlock();
    }
  }
  
  /**
   * Check if a MdMethod has the premissions to execute an operation on a given object
   * 
   * @param mdMethod
   *            The MdMethodIF to check
   * @param operation
   *            The operation to check access on
   * @param component
   *            The Component to check permissions on
   * @param metdataReference
   *            The reference of the Metatdata object
   * @return If a MdMethod has permission to execute an operation on an object
   */
  protected boolean checkAccess(MdMethodDAOIF mdMethod, Operation operation, Mutable component)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkAccess(operation, component);
  }
  
  protected boolean checkAttributeAccess(MdMethodDAOIF mdMethod, Operation o, String stateId)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkAccess(o, stateId);
  }
  
  /**
   * Checks if the {@link UserDAO} of the {@link Session} has
   * permissions to execute an {@link Operation} on a type.
   * 
   * @param mdMethod
   * @param o
   * @param type
   * @return
   */
  protected boolean checkTypeAccess(MdMethodDAOIF mdMethod, Operation o, String type)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkTypeAccess(o, type);
  }
  
  protected boolean checkTypeAccess(MdMethodDAOIF mdMethod, Operation o, ValueObject valueObject)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkTypeAccess(o, valueObject);
  }

  
  /**
   * Checks if the {@link UserDAO} of the {@link Session} has
   * permissions to execute an {@link Operation} on a type.
   * 
   * @param mdMethod
   * @param o
   * @param type
   * @return
   */
  protected boolean checkTypeAccess(MdMethodDAOIF mdMethod, Operation o, MdTypeDAOIF type)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkTypeAccess(o, type);
  }
  
  /**
   * Check if a MdMethod has attribute permissions for a given operation
   * 
   * @param mdMethod
   *            The MdMethodIF to check
   * @param operation
   *            The operation to check access for
   * @param component
   *            The Component the attribute is a struct of
   * @param mdAttribute
   *            The MdAttribute which defines the given attribute
   * 
   * @return If a MdMethod has access permissions for a given operation on a given
   *         attribute
   */
  protected boolean checkAttributeAccess(MdMethodDAOIF mdMethod, Operation operation, Mutable component, MdAttributeDAOIF mdAttribute)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkAttributeAccess(operation, component, mdAttribute);
  }

  /**
   * Check if a MdMethod has attribute permissions for a given operation
   * 
   * @param mdMethod
   *            The MdMethodIF to check
   * @param operation
   *            The operation to check access for
   * @param struct
   *            The struct the attribute is a member of
   * @param mdAttribute
   *            The MdAttribute which defines the given attribute
   * 
   * @return If a MdMethod has access permissions for a given operation on a given
   *         attribute
   */
  protected boolean checkAttributeAccess(MdMethodDAOIF mdMethod, Operation operation, Struct struct, MdAttributeDAOIF mdAttribute)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkAttributeAccess(operation, struct, mdAttribute);
  }
  
  protected boolean checkAttributeAccess(MdMethodDAOIF mdMethod, Operation operation, MdAttributeDAOIF mdAttribute)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkAttributeAccess(operation, mdAttribute);
  }

  
  protected boolean checkAttributeAccess(MdMethodDAOIF mdMethod, Operation operation, String stateId, MdAttributeDAOIF mdAttribute)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkAttributeAccess(operation, stateId, mdAttribute);
  }
  
  protected boolean checkAttributeTypeAccess(MdMethodDAOIF mdMethod, Operation operation, MdAttributeDAOIF mdAttribute)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkAttributeTypeAccess(operation, mdAttribute);
  }


  /**
   * Check access on a ADD_CHILD, ADD_PARENT, DELETE_CHILD, and DELETE_PARENT operation
   * 
   * @param mdMethod
   *            The MdMethodIF to check
   * @param o
   *          The operation to check access for
   * @param business
   *          The Business object the relationship is being added/removed to/from.
   * @param mdRelationshipId
   *          The relationship metadata for the relationship being created
   * @return If the user of this session has permission to execute the given
   *         operation on the given object
   */
  protected boolean checkRelationshipAccess(MdMethodDAOIF mdMethod, Operation operation, Business business, String mdRelationshipId)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkRelationshipAccess(operation, business, mdRelationshipId);
  }  

  /**
   * Check if a MdMethod has the permissions to execute another MdMethod
   * 
   * @param mdMethod The MdMethod to check permissions upon
   * @param methodToExecute The MdMethod to execute
   * @return
   */
  protected boolean checkExecuteAccess(MdMethodDAOIF mdMethod, MdMethodDAOIF methodToExecute)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkMethodAccess(Operation.EXECUTE, methodToExecute);
  }

  /**
   * Check if a MdMethod of a given session has promote access on a transition
   * 
   * @pre o == Operation.PROMOTE
   * 
   * @param mdMethod
   *            The MdMethodIF to check
   * @param entity
   *            The entity object ot check acces on
   * @param transitionName
   *            The name of the transition to check promotion on
   * @return If a MdMethod has the permission to execute the given
   *         operation on the given transition
   */
  protected boolean checkPromoteAccess(MdMethodDAOIF mdMethod, Business business, String transitionName)
  {
    MethodSession methodSession = getMethod(mdMethod);
    
    if(methodSession == null)
    {
      return false;
    }
    
    return methodSession.checkPromoteAccess(business, transitionName);
  }

  /**
   * Removes the permissions of a MdMethod from the cache
   * 
   * @param MdMethodDAOIF
   *            The MdMethodIF to close
   */
  protected void closeMethod(MdMethodDAOIF mdMethod)
  {
    methodCacheLock.lock();
    try
    {
      String key = mdMethod.getId();
      if(cache.containsKey(key))
      {
        MethodSession method = cache.get(key);
        method.unregister();

        cache.remove(key);
      }      
    }
    finally
    {
      methodCacheLock.unlock();
    }
  }
  
  /**
   * Returns the size of the cache.  This method is used for 
   * testing only and is not included in the MethodFacade.
   * @return
   */
  int size()
  {
    methodCacheLock.lock();
    
    try
    {
      return cache.size();
    }
    finally
    {
      methodCacheLock.unlock();
    }
  }
  
  /**
   * Returns if the permissions for a MdMethod are contained in the 
   * cache.  This method is used for testing only and is not included
   * in the MethodFacade.
   * 
   * @param mdMethod
   * @return
   */
  boolean contains(MdMethodDAOIF mdMethod)
  {
    methodCacheLock.lock();
    
    try
    {
      return cache.containsKey(mdMethod.getId());
    }
    finally
    {
      methodCacheLock.unlock();
    }    
  }

  /**
   * Removes all MdMethod permissions from the cache
   */
  protected void clear()
  {
    methodCacheLock.lock();
    try
    {
      //Unregister all Methods from the PermissionManager
      for(String key : cache.keySet())
      {
        MethodSession methodSession = cache.get(key);
        methodSession.unregister();
      }
      
      //Empty the cache
      cache.clear();
    }
    finally
    {
      methodCacheLock.unlock();
    }
  }
  
  /**
   * If the method cache does not already exist then returns a new method cache
   * otherwise it returns the existing cache.
   * 
   * @return The singleton method cache
   */
  public static synchronized MethodCache getMethodCache()
  {
    if(methodCache == null)
    {
      methodCache = new MethodCache();
    }
    
    return methodCache;
  }
}
