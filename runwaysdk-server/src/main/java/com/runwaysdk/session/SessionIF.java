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

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;

/**
 * Read only interface for sessions.
 * 
 * @author Justin Smethie
 */
public interface SessionIF
{
  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.session.PermissionEntity#checkAccess(com.runwaysdk.business
   * .rbac.Operation, com.runwaysdk.business.Mutable)
   */
  public boolean checkAccess(Operation o, Mutable mutable);

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.session.PermissionEntity#checkAttributeAccess(com.runwaysdk
   * .business.rbac.Operation, com.runwaysdk.dataaccess.MdAttributeIF)
   */
  public boolean checkAttributeAccess(Operation operation, MdAttributeDAOIF mdAttribute);

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.session.PermissionEntity#checkAttributeAccess(com.runwaysdk
   * .business.rbac.Operation, com.runwaysdk.business.Mutable,
   * com.runwaysdk.dataaccess.MdAttributeIF)
   */
  public boolean checkAttributeAccess(Operation operation, Mutable mutable, MdAttributeDAOIF mdAttribute);

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.session.PermissionEntity#checkAttributeAccess(com.runwaysdk
   * .business.rbac.Operation, com.runwaysdk.business.Struct,
   * com.runwaysdk.dataaccess.MdAttributeIF)
   */
  public boolean checkAttributeAccess(Operation operation, Struct struct, MdAttributeDAOIF mdAttribute);

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.session.PermissionEntity#checkAttributeTypeAccess(com.runwaysdk
   * .business.rbac.Operation, com.runwaysdk.dataaccess.MdAttributeIF)
   */
  public boolean checkAttributeTypeAccess(Operation operation, MdAttributeDAOIF mdAttribute);

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.SessionIF#checkExecuteAccess(
   * com.runwaysdk.business.rbac.Operation, com.runwaysdk.business.Mutable,
   * com.runwaysdk.dataaccess.MdMethodIF)
   */
  public boolean checkMethodAccess(Operation operation, Mutable mutable, MdMethodDAOIF mdMethod);

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.session.PermissionEntity#checkPromoteAccess(com.runwaysdk
   * .business.Business, java.lang.String)
   */
  public boolean checkPromoteAccess(Business business, String transitionName);

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.session.PermissionEntity#checkRelationshipAccess(com.runwaysdk
   * .business.rbac.Operation, com.runwaysdk.business.Business,
   * java.lang.String)
   */
  public boolean checkRelationshipAccess(Operation o, Business business, String mdRelationshipId);

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.session.PermissionEntity#checkRelationshipAttributeAccess
   * (com.runwaysdk.business.rbac.Operation, com.runwaysdk.business.Business,
   * com.runwaysdk.dataaccess.MdAttributeIF)
   */
  public boolean checkRelationshipAttributeAccess(Operation operation, Business business, MdAttributeDAOIF mdAttribute);

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.session.PermissionEntity#checkTypeAccess(com.runwaysdk.business
   * .rbac.Operation, com.runwaysdk.dataaccess.MdTypeIF)
   */
  public boolean checkTypeAccess(Operation o, MdTypeDAOIF mdTypeIF);

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.session.PermissionEntity#checkTypeAccess(com.runwaysdk.business
   * .rbac.Operation, java.lang.String)
   */
  public boolean checkTypeAccess(Operation o, String type);

  /**
   * @return If this Session should close at the end of its current request
   */
  public boolean closeOnEndOfRequest();

  /**
   * Returns the Mutable object with the given id that has been stored in this
   * session. If the object does not exist, it returns null.
   * 
   * @param id
   *          .
   * @return Mutable object with the given id that has been stored in this
   *         session or null if no object exists.
   */
  public Mutable get(String id);

  /**
   * @return The id of the session
   */
  public String getId();

  /**
   * @return locale used by this session.
   */
  public Locale getLocale();

  /**
   * Returns the dimension set for this session, or null if no dimension has
   * been set.
   * 
   * @return dimension set for this session, or null if no dimension has been
   *         set.
   */
  public MdDimensionDAOIF getDimension();

  /**
   * @return The user of the session
   */
  public SingleActorDAOIF getUser();

  /**
   * Returns a Map representing all of the roles assigned to the given user,
   * either implicitly or explicitly.
   * 
   * @return Map representing all of the roles assigned to the given user,
   *         either implicitly or explicitly.
   */
  public Map<String, String> getUserRoles();

  /**
   * @return If a user has logged in on this session
   */
  public boolean hasUser();

  /**
   * @param time
   *          The time to check for expiration upon.
   * @return If the {@link Session} is expired at the given time.
   */
  public boolean isExpired(long time);

  /**
   * Returns true if the given user is a member of the given role (either
   * explicitly or implicitly), false otherwise.
   * 
   * @param roleName
   * @return true if the given user is a member of the given role (either
   *         explicitly or implicitly), false otherwise.
   */
  public boolean userHasRole(String roleName);
  
  /**
   * @return The number of miliseconds before this user's session expires.
   */
  public long getTimeLeft();
}
