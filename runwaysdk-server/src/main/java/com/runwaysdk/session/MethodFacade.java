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
package com.runwaysdk.session;

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
 * A convience Facade of the Method Cache
 * 
 * @author Justin Smethie
 */
public class MethodFacade
{
  /**
   * See {@link MethodCache#getMethodActorIF(MdMethodDAOIF)} 
   */
  public static MethodActorDAOIF getMethodActorIF(MdMethodDAOIF mdMethod)
  {
    MethodCache cache = MethodCache.getMethodCache();
    
    return cache.getMethodActorIF(mdMethod);
  }
  
  /**
   * Checks if the {@link UserDAO} of the {@link Session} has
   * permissions to execute an {@link Operation} on a type.
   *
   * @param o The {@link Operation} to execute
   * @param type The type to test permissions on
   *
   * @return If access has been granted
   */
  public static boolean checkTypeAccess(MdMethodDAOIF mdMethod, Operation o, String type)
  {
    MethodCache cache = MethodCache.getMethodCache();
    
    return cache.checkTypeAccess(mdMethod, o, type);
  }
  
  /**
   * Checks to see if the given permission exists for that type.
   *
   * @param sessionId The id of the session
   * @param o The operation to execute
   * @param mdTypeIF The object to test permissions on
   *
   * @return If access has been granted
   */
  public static boolean checkTypeAccess(MdMethodDAOIF mdMethod, Operation o, MdTypeDAOIF mdTypeIF)
  {
    MethodCache cache = MethodCache.getMethodCache();
    
    return cache.checkTypeAccess(mdMethod, o, mdTypeIF);
  }

  /**
   * Checks all types involved in building a {@link ValueObject} to see if
   * the given permission exists for that type.
   *
   * @param sessionId The id of the session
   * @param o The operation to execute
   * @param valueObject The object to test permissions on
   *
   * @return If access has been granted
   */
  public static boolean checkTypeAccess(MdMethodDAOIF mdMethod, Operation o, ValueObject valueObject)
  {
    MethodCache cache = MethodCache.getMethodCache();    

    return cache.checkTypeAccess(mdMethod, o, valueObject);
  }
  
  /**
   * See {@link MethodCache#checkAccess(MdMethodDAOIF, Operation, Mutable)} 
   */
  public static boolean checkAccess(MdMethodDAOIF mdMethod, Operation operation, Mutable mutable)
  {
    MethodCache cache = MethodCache.getMethodCache();
    
    return cache.checkAccess(mdMethod, operation, mutable);
  }
  
  /**
   * See {@link MethodCache#checkAttributeAccess(MdMethodDAOIF, Operation, Mutable, MdAttributeDAOIF)} 
   */
  public static boolean checkAttributeAccess(MdMethodDAOIF mdMethod, Operation operation, Mutable mutable, MdAttributeDAOIF mdAttribute)
  {
    MethodCache cache = MethodCache.getMethodCache();
    
    return cache.checkAttributeAccess(mdMethod, operation, mutable, mdAttribute);
  } 
  
  public static boolean checkAttributeAccess(MdMethodDAOIF mdMethod, Operation operation, MdAttributeDAOIF mdAttribute)
  {
    MethodCache cache = MethodCache.getMethodCache();
    
    return cache.checkAttributeAccess(mdMethod, operation, mdAttribute);        
  }


  
  /**
   * See {@link MethodCache#checkAttributeAccess(MdMethodDAOIF, Operation, Struct, MdAttributeDAOIF)} 
   */
  public static boolean checkAttributeAccess(MdMethodDAOIF mdMethod, Operation operation, Struct struct, MdAttributeDAOIF mdAttribute)
  {
    MethodCache cache = MethodCache.getMethodCache();
    
    return cache.checkAttributeAccess(mdMethod, operation, struct, mdAttribute);
  }
  
  public static boolean checkAttributeTypeAccess(MdMethodDAOIF mdMethod, Operation operation, MdAttributeDAOIF mdAttribute)
  {
    MethodCache cache = MethodCache.getMethodCache();
    
    return cache.checkAttributeTypeAccess(mdMethod, operation, mdAttribute);    
  }

  
  /**
   * See {@link MethodCache#checkRelationshipAccess2(MdMethodDAOIF mdMethod, Operation operation, Business business, String mdRelationshipId)} 
   */
  public static boolean checkRelationshipAccess(MdMethodDAOIF mdMethod, Operation operation, Business business, String mdRelationshipId)
  {
    MethodCache cache = MethodCache.getMethodCache();
    
    return cache.checkRelationshipAccess(mdMethod, operation, business, mdRelationshipId);
  }
  
  /**
   * See {@link MethodCache#checkExecuteAccess(MdMethodDAOIF, MdMethodDAOIF) 
   */
  public static boolean checkExecuteAccess(MdMethodDAOIF mdMethod, MdMethodDAOIF methodToExecute)
  {
    MethodCache cache = MethodCache.getMethodCache();
    
    return cache.checkExecuteAccess(mdMethod, methodToExecute);         
  }
  
  /**
   * See {@link MethodCache#closeMethod(MdMethodDAOIF)} 
   */
  public static void closeMethod(MdMethodDAOIF mdMethod)
  {
    MethodCache cache = MethodCache.getMethodCache();
    
    cache.closeMethod(mdMethod);        
  }
  
  /**
   * See {@link MethodCache#clear()} 
   */
  public static void clear()
  {
    MethodCache cache = MethodCache.getMethodCache();
    
    cache.clear();    
  }
}
