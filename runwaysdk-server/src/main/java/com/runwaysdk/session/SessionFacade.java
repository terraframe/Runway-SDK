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


import java.util.List;
import java.util.Locale;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;
import com.runwaysdk.system.Users;
import com.runwaysdk.system.metadata.MdDimension;
import com.runwaysdk.system.metadata.Metadata;

public class SessionFacade
{
  /**
   * Singleton instance of the {@link SessionFacade}
   */
  private static SessionFacade sessionFacade;

  /**
   * {@link SessionCache} used by the singleton {@link SessionFacade}
   */
  private SessionCache cache;

  /**
   *
   */
  @Inject
  protected SessionFacade(SessionCache cache)
  {
    this.cache = cache;
  }

  protected static synchronized void setCache(SessionCache cache)
  {
    sessionFacade.cache = cache;
  }

  static synchronized SessionCache getCache()
  {
    return sessionFacade.cache;
  }

  /**
   * Restores the cache to the empty default cache
   */
  static synchronized void reloadCache()
  {
    Injector injector = SessionCacheInjector.getInjector();
    sessionFacade = injector.getInstance(SessionFacade.class);
  }

  /**
   * Returns the {@link SessionCache} used by the singleton {@link SessionFacade}.
   *
   * @return Returns the {@link SessionCache} used by the singleton {@link SessionFacade}.
   */
  static synchronized SessionCache getSessionCache()
  {
    if(sessionFacade == null)
    {
      Injector injector = SessionCacheInjector.getInjector();
      sessionFacade = injector.getInstance(SessionFacade.class);
    }

    return sessionFacade.cache;
  }

  /**
   * Logs a {@link UserDAO} into a new {@link Session} and loads
   * the permissions of the {@link UserDAO}.  In addition, this
   * method ensures that the {@link UserDAO} is not at it's
   * {@link Session} limit.
   *
   * @param username The name of the {@link UserDAO}
   * @param password The password of the {@link UserDAO}
   * @param locales locale of the user
   *
   * @return id of the newly created {@link Session}.
   */
  public static String logIn(String username, String password, Locale[] locales)
  {
    SessionCache cache = SessionFacade.getSessionCache();

    return cache.logIn(username, password, locales);
  }

  /**
   * Logs a {@link UserDAO} into a new {@link Session} and loads
   * the permissions of the {@link UserDAO}.  In addition, this
   * method ensures that the {@link UserDAO} is not at it's
   * {@link Session} limit.
   *
   * @param username The name of the {@link UserDAO}
   * @param password The password of the {@link UserDAO}
   * @param dimensionKey The dimension key of the dimension set to this session.
   * @param locales locale of the user
   *
   * @return id of the newly created {@link Session}.
   */
  public static String logIn(String username, String password, String dimensionKey, Locale[] locales)
  {
    SessionCache cache = SessionFacade.getSessionCache();

    return cache.logIn(username, password, dimensionKey, locales);
  }

  /**
   * Sets the dimension of an existing {@link Session}.
   * 
   * @param dimensionKey key of a {@link MdDimension}.
   * @param sessionId The id of the {@link Session}.
   */
  public static void setDimension(String dimensionKey, String sessionId)
  {
    SessionCache cache = SessionFacade.getSessionCache();

    cache.setDimension(sessionId, dimensionKey);
  }
  
  /**
   * Changes the {@link UserDAO} of an existing {@link Session} and loads
   * the permissions of the new {@link UserDAO}.  In addition, this
   * method ensures that the {@link UserDAO} is not at it's {@link Session} limit.
   *
   * @param sessionId The id of the {@link Session}
   * @param username The name of the {@link UserDAO}
   * @param password The password of the {@link UserDAO}
   */
  public static void changeLogin(String sessionId, String username, String password)
  {
    SessionCache cache = SessionFacade.getSessionCache();

    cache.changeLogin(username, password, sessionId);
  }


  /**
   * Forces the {@link SessionCache} to check for and remove expired {@link Session}s
   */
  public static void cleanUp()
  {
    SessionCache cache = SessionFacade.getSessionCache();
    cache.cleanUp();
  }

  /**
   * Renews the amount of time a {@link Session} has till it expires.
   *
   * @param sessionId The id of the {@link Session} to renew
   */
  public static void renewSession(String sessionId)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    cache.renewSession(sessionId);
  }

  /**
   * Sets the amount of time a {@link Session} has till it expires.
   * 
   * @param sessionId The id of the {@link Session} to renew
   * @param time The time the session has until it expires.
   */
  public static void setSessionTime(String sessionId, int time) {
    SessionCache cache = SessionFacade.getSessionCache();
    
    Session session = cache.getSession(sessionId);
    session.setExpirationTime(time);
    cache.closeSession(sessionId);
    cache.addSession(session);
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
  public static boolean checkTypeAccess(String sessionId, Operation o, String type)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    return session.checkTypeAccess(o, type);
  }

  /**
   * Check if the {@link UserDAO} of the {@link Session} has the
   * permissions to execute an {@link Session} on an object.
   *
   * @param sessionId The id of the session
   * @param o The {@link Operation} to check access on
   * @param mutable The entity to check permissions on
   *
   * @return If the user has permission to execute an {@link Session} on an object
   */
  public static boolean checkAccess(String sessionId, Operation o, Mutable mutable)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    return session.checkAccess(o, mutable);
  }

  /**
   * Checks if the session has permissions on the given state
   *
   * @param o The operation to execute
   * @param stateId id of the state
   *
   * @return If access has been granted
   */
  public static boolean checkAccess(String sessionId, Operation o, String stateId)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    return session.checkAccess(o, stateId);
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
  public static boolean checkTypeAccess(String sessionId, Operation o, ValueObject valueObject)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    return session.checkTypeAccess(o, valueObject);
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
  public static boolean checkTypeAccess(String sessionId, Operation o, MdTypeDAOIF mdTypeIF)
  {
    if (mdTypeIF == null)
    {
      return true;
    }

    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    return session.checkTypeAccess(o, mdTypeIF);
  }

  /**
   * Performs a check if the {@link UserDAO} of the {@link Session} has attribute
   * permissions for a given {@link Operation}. Checks for permissions on the
   * {@link MetadataDAO} of the {@link MdAttributeDAOIF} as well as permissions of the
   * entity owner and entity state.
   *
   * @param sessionId The id of the {@link Session} to check
   * @param operation The {@link Operation} to check
   * @param mutabale The Component to check
   * @param mdAttribute The {@link MdAttributeDAOIF} which defines the given attribute
   *
   * @return If the {@link UserDAO} has access permissions for a given operation on a given attribute
   */
  public static boolean checkAttributeAccess(String sessionId, Operation operation, Mutable mutabale, MdAttributeDAOIF mdAttribute)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    boolean userPermission = session.checkAttributeAccess(operation, mutabale, mdAttribute);

    if (userPermission)
    {
      return true;
    }
    else
    {
      return session.checkAttributeAccess(operation, mutabale, mdAttribute.getMdAttributeConcrete());
    }
  }

  /**
   * Returns true if permission exists for the given operation on the attribute and the given defining type state.
   *
   * @param sessionId The id of the {@link Session} to check
   * @param operation
   * @param stateId
   * @param mdAttribute
   * @return operations for the given attribute and the state of the type that defines it.
   */
  public static boolean checkAttributeAccess(String sessionId, Operation operation, String stateId, MdAttributeDAOIF mdAttribute)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    boolean userPermission = session.checkAttributeAccess(operation, stateId, mdAttribute);

    if (userPermission)
    {
      return true;
    }
    else
    {
      return session.checkAttributeAccess(operation, stateId, mdAttribute.getMdAttributeConcrete());
    }
  }


  /**
   * Performs a check if the {@link UserDAO} of the {@link Session} has attribute
   * type permissions for a given {@link Operation}. Checks for permissions on the
   * {@link MetadataDAO} of the {@link MdAttributeDAOIF}.
   *
   * @param sessionId The id of the {@link Session} to check
   * @param operation The {@link Operation} to check
   * @param mdAttribute The {@link MdAttributeDAOIF} which defines the given attribute
   *
   * @return If the {@link UserDAO} has access permissions for a given operation on a given attribute
   */
  public static boolean checkAttributeTypeAccess(String sessionId, Operation operation, MdAttributeDAOIF mdAttribute)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    boolean userPermission = session.checkAttributeTypeAccess(operation, mdAttribute);

    if (userPermission)
    {
      return true;
    }
    else
    {
      return session.checkAttributeTypeAccess(operation, mdAttribute.getMdAttributeConcrete());
    }
  }

  /**
   * Performs a check if the {@link UserDAO} of the {@link Session} has
   * attribute permissions for a given {@link Operation}. Checks for
   * permissions on the {@link Metadata} of the {@link MdAttributeDAOIF}
   * as well as permissions of the {@link Struct} owner.
   *
   * @param sessionId The id of the {@link Session} to check
   * @param operation The {@link Operation} to check
   * @param struct The {@link Struct} to check
   * @param mdAttribute The {@link MdAttributeDAOIF} which defines the given attribute
   *
   * @return If the {@link UserDAO} has access permissions for a given {@link Operation} on a given attribute
   */
  public static boolean checkAttributeAccess(String sessionId, Operation operation, Struct struct, MdAttributeDAOIF mdAttribute)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    boolean userPermission = session.checkAttributeAccess(operation, struct, mdAttribute);

    if (userPermission)
    {
      return true;
    }
    else
    {
      return session.checkAttributeAccess(operation, struct, mdAttribute.getMdAttributeConcrete());
    }
  }

  /**
   * Check if the {@link UserDAO} of the {@link Session} has attribute
   * permissions for a given {@link Operation}.  This method only
   * checks against the {@link MetadataDAO}.  It does not take into
   * account permissions that exist on entity state.
   *
   * @param sessionId The id of the {@link Session} to check
   * @param operation The {@link Operation} to check
   * @param mdAttribute The {@link MdAttributeDAOIF} which defines the given attribute
   *
   * @return If the {@link UserDAO} has access permissions for a given {@link Operation} on a given attribute
   */
  public static boolean checkAttributeAccess(String sessionId, Operation operation, MdAttributeDAOIF mdAttribute)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    return checkAttributeAccess(session, operation, mdAttribute);
  }

  /**
   * Check if the {@link UserDAO} of the {@link Session} has attribute
   * permissions for a given {@link Operation}.  This method only
   * checks against the {@link MetadataDAO}.  It does not take into
   * account permissions that exist on entity state.
   *
   * @param session The {@link Session} to check
   * @param operation The {@link Operation} to check
   * @param mdAttribute The {@link MdAttributeDAOIF} which defines the given attribute
   *
   * @return If the {@link UserDAO} has access permissions for a given {@link Operation} on a given attribute
   */
  public static boolean checkAttributeAccess(Session session, Operation operation, MdAttributeDAOIF mdAttribute)
  {
    boolean userPermission = session.checkAttributeAccess(operation, mdAttribute);

    if (userPermission)
    {
      return true;
    }
    else
    {
      return session.checkAttributeAccess(operation, mdAttribute.getMdAttributeConcrete());
    }
  }

  /**
   * Checks access for the {@link UserDAO} of the {@link Session} on an
   * ADD_CHILD, ADD_PARENT, DELETE_CHILD, and DELETE_PARENT {@link Operation}.
   *
   * @param sessionId The id of the {@link Session} to check
   * @param o The {@link Operation} to check
   * @param business The {@link Business} object the relationship is being added to.
   * @param mdRelationshipId The relationship metadata for the relationship being created
   *
   * @return If the {@link UserDAO} of this {@link Session} has permission
   *         to execute the given {@link Operation} on the given object.
   */
  public static boolean checkRelationshipAccess(String sessionId, Operation o, Business business, String mdRelationshipId)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    return session.checkRelationshipAccess(o, business, mdRelationshipId);
  }


  /**
   * Check if the {@link UserDAO} of the {@link Session} has
   * {@link Operation#PROMOTE} access on a transition.
   *
   * @parm sessionId The id of the {@link Session} to check
   * @param entity The {@link Business} object to check acces on
   * @param transitionName The name of the transition to check promotion on
   *
   * @return If the {@link UserDAO} of the {@link Session} has the permission
   *         to execute {@link Operation#PROMOTE} on the given transition
   */
  public static boolean checkPromoteAccess(String sessionId, Business business, String transitionName)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    return session.checkPromoteAccess(business, transitionName);
  }

  /**
   * Check if the {@link UserDAO} of the {@link Session} has
   * {@link Operation} access on an instance
   * {@link MdMethodDAO}.
   *
   * @param sessionId The id of the {@link Session} to check
   * @param operation operation to check
   * @param entity The {@link Mutable} object which will invoke the method.
   * @param mdMethodIF The {@link MdMethodDAO} of the method to be executed.
   *
   * @return If the {@link UserDAO} of the {@link Session} has the permission
   *         to execute {@link Operation} on the given method.
   */
  public static boolean checkMethodAccess(String sessionId, Operation operation, Mutable mutable, MdMethodDAOIF mdMethodIF)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    return session.checkMethodAccess(operation, mutable, mdMethodIF);
  }

  /**
   * Check if the {@link UserDAO} of the {@link Session} has
   * {@link Operation} access on a static {@link MdMethodDAO}.
   *
   * @parm sessionId The id of the {@link Session} to check
   * @param mdMethodIF The {@link MdMethodDAO} of the method to be executed.
   *
   * @return If the {@link UserDAO} of the {@link Session} has the permission
   *         to execute {@link Operation} on the given method.
   */
  public static boolean checkMethodAccess(String sessionId, Operation operation, MdMethodDAOIF mdMethodIF)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    return session.checkMethodAccess(operation, mdMethodIF);
  }

  /**
   * Closes a {@link Session} in the {@link SessionCache}.  If the {@link Session}
   * does not exist then this method does nothing.
   *
   * @param sessionId The id of a {@link Session}.
   */
  public static void closeSession(String sessionId)
  {
    SessionCache cache = SessionFacade.getSessionCache();

    cache.closeSession(sessionId);
  }

  /**
   * Removes all {@link Session}s from the {@link SessionCache}.
   */
  public static void clearSessions()
  {
    SessionCache cache = SessionFacade.getSessionCache();

    cache.clearSessions();
  }

  /**
   * Checks if a {@link Session} of the given id exists
   * in the {@link SessionCache}.
   *
   * @param sessionId The id of the {@link Session}.
   *
   * @return true if the {@link Session} exists, false otherwise.
   */
  public static boolean containsSession(String sessionId)
  {
    if(sessionId == null)
    {
      return false;
    }

    SessionCache cache = SessionFacade.getSessionCache();

    return cache.containsSession(sessionId);
  }

  /**
   * Returns the {@link UserDAO} which has logged into the given {@link Session}.
   * If there is no {@link UserDAO} logged in then this method returns null.
   *
   * @param sessionId The id of the {@link Session}
   *
   * @return The {@link UserDAO} which has logged into the given {@link Session}.
   */
  public static UserDAOIF getUser(String sessionId)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    return session.getUser();
  }
  
  public static List<Users> getAllLoggedInUsers()
  {
//    SessionCache cache = SessionFacade.getSessionCache();
    return null;
  }

  /**
   * Sets the user of a session
   *
   * @param sessionId The id of the session
   * @param user The user of the new session
   */
  public static void setUser(String sessionId, String userId)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    UserDAOIF user = UserDAO.get(userId);

    cache.setUser(sessionId, user);
  }

  /**
   * Returns the {@link Session} corresponding to the given {@link Session} id.
   * This method breaks the protection provided by the {@link SessionFacade} and
   * can lead to inconsistencies in the {@link SessionCache} if changes are
   * written to the {@link Session}. For example, renewing or setting the
   * {@link UserDAO} of a {@link Session} through this method will not have an
   * effect if the {@link SessionCache} is a {@link FileSessionCache}. This
   * method should only be used when the {@link Session} is guranteed to be read
   * only.
   *
   * @param sessionId The id of the {@link Session}
   *
   * @return {@link Session} with the given session id.
   */
  static SessionIF getSession(String sessionId)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    return cache.getSession(sessionId);
  }

  /**
   * @return The singleton public {@link Session}
   */
  public static SessionIF getPublicSession()
  {
    SessionCache cache = SessionFacade.getSessionCache();
    return cache.getPublicSession();
  }

  /**
   * Sets the flag denoting if the {@link Session} corresponding to the session
   * id should close at the end of its current request.
   *
   * @param sessionId Id of session
   * @param closeOnRequest Boolean flag
   */
  public static void setCloseOnEndOfRequest(String sessionId, boolean closeOnEndOfRequest)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    cache.setCloseOnEndOfRequest(sessionId, closeOnEndOfRequest);
  }

  /**
   * Informs the {@link SessionCache} that a request using the given
   * {@link Session} is finished.
   *
   * @param sessionId
   */
  static void endOfRequest(String sessionId)
  {
    SessionCache cache = SessionFacade.getSessionCache();

    cache.endOfRequest(sessionId);
  }
  
  /**
   * Retrieves a {@link Session} which will be used in a request.
   *
   * @param sessionId The id of the session to retrieve
   * @return {@link Session} corresponding to the session id
   */
  public static Session getSessionForRequest(String sessionId)
  {
    SessionCache cache = SessionFacade.getSessionCache();

    return cache.getSessionForRequest(sessionId);
  }

  /**
   * @param sessionId Id of the {@link Session}
   * @param id Id of the desired {@link Mutable} object
   *
   * @return A {@link Mutable} object stored in the given {@link Session}
   */
  public static Mutable get(String sessionId, String id)
  {
    SessionCache cache = SessionFacade.getSessionCache();
    Session session = cache.getSession(sessionId);

    return session.get(id);
  }

  /**
   * Stores a {@link Mutable} object in the given {@link Session}
   *
   * @param sessionId Id of session
   * @param mutable {@link Mutable} object to store.
   */
  public static void put(String sessionId, Mutable mutable)
  {
    SessionCache cache = SessionFacade.getSessionCache();

    cache.put(sessionId, mutable);
  }
  
  /**
   * @see com.runwaysdk.session.SessionCache#getIterator()
   */
  public static SessionIterator getIterator()
  {
    return SessionFacade.getSessionCache().getIterator();
  }
}
