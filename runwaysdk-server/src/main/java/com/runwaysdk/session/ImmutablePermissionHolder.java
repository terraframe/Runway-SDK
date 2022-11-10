package com.runwaysdk.session;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.SingleActorDAOIF;

public class ImmutablePermissionHolder implements Serializable
{
  private static final long           serialVersionUID = 1L;

  /**
   * A hash map of the users metadata permissions
   */
  private Map<String, Set<Operation>> permissions;

  /**
   * Flag denoting if the entity inherits the administrator role
   */
  private boolean                     isAdmin;

  /**
   * A reference to the user of the session
   */
  private SingleActorDAOIF            user;

  /**
   * Locale used by the session.
   */
  private Locale                      locale;

  /**
   * Key of the dimension that the user is logged into.
   */
  private String                      dimensionKey;

  /**
   * All roles the user participates in either implicitly or explicitly.
   */
  private Map<String, RoleDAOIF>      authorizedRoleMap;

  public ImmutablePermissionHolder()
  {
    this.isAdmin = false;
  }

  public ImmutablePermissionHolder(Map<String, Set<Operation>> permissions, SingleActorDAOIF user)
  {
    this.isAdmin = user.isAdministrator();    
    this.permissions = Collections.unmodifiableMap(permissions);
    this.user = user;
    this.locale = null;
    this.dimensionKey = null;
    this.authorizedRoleMap = Collections.unmodifiableMap(new HashMap<String, RoleDAOIF>());
  }

  public ImmutablePermissionHolder(Map<String, Set<Operation>> permissions, SingleActorDAOIF user, Locale locale)
  {
    this.isAdmin = user.isAdministrator();    
    this.permissions = Collections.unmodifiableMap(permissions);
    this.user = user;
    this.locale = locale;
    this.dimensionKey = null;
    this.authorizedRoleMap = Collections.unmodifiableMap(new HashMap<String, RoleDAOIF>());
  }

  public ImmutablePermissionHolder(Map<String, Set<Operation>> permissions, SingleActorDAOIF user, Locale locale, String dimensionKey, Map<String, RoleDAOIF> authorizedRoleMap)
  {
    this.isAdmin = user.isAdministrator();    
    this.permissions = Collections.unmodifiableMap(permissions);
    this.user = user;
    this.locale = locale;
    this.dimensionKey = dimensionKey;
    this.authorizedRoleMap = Collections.unmodifiableMap(authorizedRoleMap);
  }

  public ImmutablePermissionHolder(SingleActorDAOIF user, Locale locale, String dimensionKey, Map<String, RoleDAOIF> authorizedRoleMap)
  {
    this.isAdmin = user.isAdministrator();    
    this.permissions = Collections.unmodifiableMap(new HashMap<>());
    this.user = user;
    this.locale = locale;
    this.dimensionKey = dimensionKey;
    this.authorizedRoleMap = Collections.unmodifiableMap(authorizedRoleMap);
  }

  public Map<String, Set<Operation>> getPermissions()
  {
    return permissions;
  }

  public boolean isAdmin()
  {
    return isAdmin;
  }

  public SingleActorDAOIF getUser()
  {
    return user;
  }

  public Locale getLocale()
  {
    return locale;
  }

  public String getDimensionKey()
  {
    return dimensionKey;
  }

  public Map<String, RoleDAOIF> getAuthorizedRoleMap()
  {
    return authorizedRoleMap;
  }

  public ImmutablePermissionHolder setDimensionKey(String dimensionKey)
  {
    ImmutablePermissionHolder holder = new ImmutablePermissionHolder();
    holder.dimensionKey = dimensionKey;
    holder.authorizedRoleMap = this.authorizedRoleMap;
    holder.isAdmin = this.isAdmin;
    holder.locale = this.locale;
    holder.permissions = this.permissions;
    holder.user = this.user;

    return holder;
  }

}
