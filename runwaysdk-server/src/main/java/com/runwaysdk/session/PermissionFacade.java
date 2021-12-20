/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;

/**
 * Checks permissions on both the user and the method actor if the method actor exists.
 * @author nathan
 *
 */
public class PermissionFacade
{
  /**
   * Check if the {@link UserDAO} of the {@link Session} or
   * the method actor has read permission on the method.
   *
   * @param sessionId The oid of the session
   * @param mutable The entity to check permissions on
   *
   * @return if the {@link UserDAO} of the {@link Session} or
   * the method actor has read permission on the method.
   */
  public static boolean checkReadAccess(String sessionId, Mutable mutable)
  {
    boolean userpermission = SessionFacade.checkAccess(sessionId, Operation.READ, mutable);

    if (userpermission)
    {
      return true;
    }
    else
    {
      boolean methodPermission = false;

      MdMethodDAOIF mdMethodDAOIF = SessionFacade.getSessionForRequest(sessionId).getFirstMdMethodDAOIF();
      if (mdMethodDAOIF != null)
      {
        methodPermission = MethodFacade.checkAccess(mdMethodDAOIF, Operation.READ, mutable);
      }

      return methodPermission;
    }
  }

  /**
   * Checks all types involved in building a {@link ValueObject} to see if
   * the read permission exists for that type.
   *
   * @param sessionId The oid of the session
   * @param valueObject The object to test permissions on
   *
   * @return If access has been granted
   */
  public static boolean checkTypeReadAccess(String sessionId, ValueObject valueObject)
  {
    boolean userPermission = SessionFacade.checkTypeAccess(sessionId, Operation.READ, valueObject);

    if (userPermission)
    {
      return true;
    }
    else
    {
      boolean methodPermission = false;

      SessionCache cache = SessionFacade.getSessionCache();
      Session session = cache.getSession(sessionId);

      MdMethodDAOIF mdMethodDAOIF = session.getFirstMdMethodDAOIF();
      if (mdMethodDAOIF != null)
      {
        methodPermission = MethodFacade.checkTypeAccess(mdMethodDAOIF, Operation.READ, valueObject);
      }
      return methodPermission;
    }
  }

  /**
   * Checks to see if the read permission exists for that type.
   *
   * @param sessionId The oid of the session
   * @param mdTypeIF The object to test permissions on
   *
   * @return If access has been granted
   */
  public static boolean checkTypeReadAccess(String sessionId, MdTypeDAOIF mdTypeIF)
  {
    if (mdTypeIF == null)
    {
      return true;
    }

    boolean userPermission = SessionFacade.checkTypeAccess(sessionId, Operation.READ, mdTypeIF);

    if (userPermission)
    {
      return true;
    }
    else
    {
      SessionCache cache = SessionFacade.getSessionCache();
      Session session = cache.getSession(sessionId);

      boolean methodPermission = false;

      MdMethodDAOIF mdMethodDAOIF = session.getFirstMdMethodDAOIF();
      if (mdMethodDAOIF != null)
      {
        methodPermission = MethodFacade.checkTypeAccess(mdMethodDAOIF, Operation.READ, mdTypeIF);
      }

      return methodPermission;
    }
  }

  /**
   * Performs a check if the {@link UserDAO} of the {@link Session} has attribute
   * read permissions for a given {@link Operation}. Checks for permissions on the
   * {@link MetadataDAO} of the {@link MdAttributeDAOIF} as well as permissions of the
   * method actor, if any.
   *
   * @param sessionId The oid of the {@link Session} to check
   * @param mutabale The Component to check
   * @param mdAttribute The {@link MdAttributeDAOIF} which defines the given attribute
   *
   * @return If the {@link UserDAO} has access permissions for a given operation on a given attribute
   */
  public static boolean checkAttributeReadAccess(String sessionId, Mutable mutabale, MdAttributeDAOIF mdAttribute)
  {
    boolean userPermission = SessionFacade.checkAttributeAccess(sessionId, Operation.READ, mutabale, mdAttribute);

    if (userPermission)
    {
      return true;
    }
    else
    {
      boolean methodPermission = false;

      SessionCache cache = SessionFacade.getSessionCache();
      Session session = cache.getSession(sessionId);

      MdMethodDAOIF mdMethodDAOIF = session.getFirstMdMethodDAOIF();
      if (mdMethodDAOIF != null)
      {
        methodPermission = MethodFacade.checkAttributeAccess(mdMethodDAOIF, Operation.READ, mutabale, mdAttribute);
      }

      return methodPermission;
    }
  }

  /**
   * Performs a check if the {@link UserDAO} of the {@link Session} has attribute
   * type read permissions for a given {@link Operation}. Checks for permissions on the
   * {@link MetadataDAO} of the {@link MdAttributeDAOIF}.
   *
   * @param sessionId The oid of the {@link Session} to check
   * @param mdAttribute The {@link MdAttributeDAOIF} which defines the given attribute
   *
   * @return If the {@link UserDAO} has access permissions for a given operation on a given attribute
   */
  public static boolean checkAttributeTypeReadAccess(String sessionId, MdAttributeDAOIF mdAttribute)
  {
    boolean userPermission = SessionFacade.checkAttributeTypeAccess(sessionId, Operation.READ, mdAttribute);

    if (userPermission)
    {
      return true;
    }
    else
    {
      boolean methodPermission = false;

      SessionCache cache = SessionFacade.getSessionCache();
      Session session = cache.getSession(sessionId);

      MdMethodDAOIF mdMethodDAOIF = session.getFirstMdMethodDAOIF();
      if (mdMethodDAOIF != null)
      {
        methodPermission = MethodFacade.checkAttributeTypeAccess(mdMethodDAOIF, Operation.READ, mdAttribute);
      }

      return methodPermission;
    }
  }

  /**
   * Check if the {@link UserDAO} of the {@link Session} has attribute
   * read permissions for a given {@link Operation}, or the method actor if any.  This method only
   * checks against the {@link MetadataDAO}.  It does not take into
   * account permissions that exist on entity state.
   *
   * @param sessionId The oid of the {@link Session} to check
   * @param mdAttribute The {@link MdAttributeDAOIF} which defines the given attribute
   *
   * @return If the {@link UserDAO} has access permissions for a given {@link Operation} on a given attribute
   */
  public static boolean checkAttributeReadAccess(String sessionId, MdAttributeDAOIF mdAttribute)
  {
    boolean userPermission = SessionFacade.checkAttributeAccess(sessionId, Operation.READ, mdAttribute);

    if (userPermission)
    {
      return true;
    }
    else
    {
      boolean methodPermission = false;

      SessionCache cache = SessionFacade.getSessionCache();
      Session session = cache.getSession(sessionId);

      MdMethodDAOIF mdMethodDAOIF = session.getFirstMdMethodDAOIF();
      if (mdMethodDAOIF != null)
      {
        methodPermission = MethodFacade.checkAttributeAccess(mdMethodDAOIF, Operation.READ, mdAttribute);
      }

      return methodPermission;
    }
  }

}
