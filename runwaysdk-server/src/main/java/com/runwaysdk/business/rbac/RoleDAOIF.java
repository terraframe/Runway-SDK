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
package com.runwaysdk.business.rbac;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.Constants;


public interface RoleDAOIF extends ActorDAOIF
{

  /**
   * The type of the roleInheritance relationship
   */
  public static final String ROLE_INHERITANCE = Constants.SYSTEM_PACKAGE + ".RoleInheritance";

  /**
   * A constant for the type name of the Roles type.
   */
  public static final String TYPENAME = "Roles";

  /**
   * A constant for the Roles type.
   */
  public static final String CLASS = Constants.SYSTEM_PACKAGE + "." + TYPENAME;

  /**
   *
   * A constant for the name of the roleName attribute in the Roles table
   */
  public static final String ROLENAME = "roleName";

  /**
   *
   * A constant for the name of the display label attribute in the Roles table
   */
  public static final String DISPLAY_LABEL = "displayLabel";

  /**
   * The name of the public role, all anymous logins get the permissions of the public role
   */
  public static String PUBLIC_ROLE = "PUBLIC";

  public static String PUBLIC_ROLE_ID = "0000000000000000000000000000097500000000000000000000000000000507";

  /**
   * The name of the role that allows a User to access the admin screen.
   */
  public static String ADMIN_SCREEN_ROLE = "AdminScreenAccess";

  /**
   * The name of the administrator role, the administrator automatically has permisisons to everything.
   */
  public static String ADMIN_ROLE = "Administrator";

  public static String ADMIN_ROLE_ID = "0000000000000000000000000000070100000000000000000000000000000507";

  /**
   * The name of the role adminstrator role.
   */
  public static String ROLE_ADMIN_ROLE = "RoleAdministrator";

  public static String ROLE_ADMIN_ROLE_ID = "NM20080620000000000000000000000200000000000000000000000000000507";

  /**
   * The name of the developer role.
   */
  public static String DEVELOPER_ROLE = "Developer";

  public static String DEVELOPER_ROLE_ID = "NM20080620000000000000000000000100000000000000000000000000000507";

  /**
   * The name of the owner role, The owner role represents the permisisons entitled to the owner of an instance
   */
  public static String OWNER_ROLE = "OWNER";

  public static String OWNER_ID = "0000000000000000000000000000070300000000000000000000000000000507";


  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public RoleDAO getBusinessDAO();

  public String getRoleName();

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getDisplayLabels();

  /**
   * Returns the display label of this metadata object
   *
   * @param locale
   *
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale);

  /**
   * Returns a list of all the objects in which the
   * role has been granted permission.

   * @return A set of metadata ids which the role has permissions on
   */
  //public Set<String> permissions();

  /**
   * Returns all of the singleactors assigned to a role
   *
   * @return A list of all singleactors that participate in a role
   */
  public Set<SingleActorDAOIF> assignedActors();

  /**
   * Assigns a singleActor member to a role
   *
   * @pre: singleActor != null
   *
   * @param singleActor
   *          The singleActor member to assign to the role
   */
  public void assignMember(SingleActorDAOIF singleActor);

  /**
   * Returns all of the operations a role has on a object
   * @pre get(mdTypeId) instanceof Metadata
   *
   * @param mdTypeId The oid of the MdType
   * @return A list of all operations the role has permissions for
   */
  //public Set<Operation> operationsOnObject(String mdTypeId);

  /**
   * Returns the set of singleactors members directly assigned to a given role as well as those who
   * were members of roles that inherited the give role
   *
   * @return The set of singleactors members the belong to this role
   */
  public Set<SingleActorDAOIF> authorizedActors();

  /**
   * Returns a list of conflict of intrest sets the role is involved in
   *
   * @return A set of SSD sets the role belongs to
   */
  public Set<SDutyDAOIF> roleSSDSet();

  /**
   * Returns all of the roles which a role inherits
   *
   * @param roleId The oid of the role
   * @return A list of all role ids which the given role inherits
   */
  public Set<RoleDAOIF> getSuperRoles();

  /**
   * Returns all of the roles which inherit from a role
   *
   * @param roleId The oid of the role
   * @return A list of all role ids which inherit the given role
   */
  public Set<RoleDAOIF> getSubRoles();

  /**
   * Returns true if a singleactors memeber is an authorized user of a role
   *
   * @param singleactors The singleactors member to validate
   * @return If a singleactors member is an authorized user of a role
   */
  public boolean isAuthorizedMember(SingleActorDAOIF singleActorIF);

  /**
   * Check to determine if a singleactors member belongs to a role that conflicts with the roleId
   *
   * @param singleactors The singleactors member to validate
   * @return Returns true if user does not belong to a role that conflicts with the give role
   */
  public boolean checkMember(SingleActorDAOIF singleActorIF);

}
