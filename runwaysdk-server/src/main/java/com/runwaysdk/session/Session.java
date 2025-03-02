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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.Element;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.MdDimensionInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.InvalidIdException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.metadata.DomainTupleDAO;
import com.runwaysdk.transport.conversion.ConversionFacade;

/**
 * The Session object
 * 
 * @author Justin Smethie
 * @date 6/30/06
 */
public class Session extends PermissionEntity implements Comparable<Session>, Serializable, SessionIF
{
  /**
   * Auto generated eclipse OID
   */
  private static final long      serialVersionUID  = -510449784842549964L;

  /**
   * Id of the default session for users who are not logged in.
   */
  public static final String     PUBLIC_SESSION_ID = ServerConstants.PUBLIC_USER_ID;

  /**
   * Offset used when determining the {@link Session} order in a
   * {@link PriorityQueue}. The {@link PriorityQueue} implementation differs
   * between Java 1.5 and 1.6. The offset corrects the {@link PriorityQueue}
   * discrepancy. We no longer support Java 1.5 so by default we are going to
   * use the Java 1.6+ offset.
   */
  private static int             comparatorOffset  = 1;

  /**
   * The number of milliseconds in a second
   */
  public static final long       SECOND            = 1000;

  /**
   * The time to live for the session
   */
  private static volatile long   timeToLive        = CommonProperties.getSessionTime() * SECOND;

  /**
   * The unique oid of the session
   */
  private volatile String        oid;

  /**
   * The time in which the session expires. If set to 0 then the session never
   * expires.
   */
  private long                   expirationTime;

  /**
   * Key: object Id Value: Mutable
   */
  private Map<String, Object>    mutableMap;

  /**
   * Flag indicating if the session should close at the end of it's first
   * transaction.
   */
  private boolean                closeOnEndOfRequest;

  /**
   * The first {@link MdMethodDAOIF encountered in the request (if any). Access
   * to this variable needs to be synchronized.
   */
  private volatile MdMethodDAOIF firstMethodInRequest;

  /**
   * Creates a new session object of an anonymous user
   * 
   * @param locales
   *          Possible locales of the session.
   */
  public Session(Locale[] locales)
  {
    this(new LocaleManager(locales).getBestFitLocale());
  }

  public Session(Locale locale)
  {
    this(locale, UserDAO.getPublicUser(), null);
  }

  /**
   * Creates a new session object of an anonymous user
   * 
   * @param locales
   *          Possible locales of the session.
   */
  public Session(Locale[] locales, SingleActorDAOIF user, String dimensionKey)
  {
    this(new LocaleManager(locales).getBestFitLocale(), user, dimensionKey);
  }

  public Session(Locale locale, SingleActorDAOIF user, String dimensionKey)
  {
    super();

    // Get a new session Id
    this.oid = ServerIDGenerator.nextID().replaceAll("-", "");
    this.closeOnEndOfRequest = false;
    this.expirationTime = System.currentTimeMillis() + timeToLive;
    this.mutableMap = new ConcurrentHashMap<String, Object>();
    this.firstMethodInRequest = null;

    if (locale == null)
    {
      String msg = "Session must have a locale";

      throw new NoLocaleException(msg, user);
    }

    this.buildPermissionHolder(user, locale, dimensionKey);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.SessionIF#getOid()
   */
  public String getOid()
  {
    return oid;
  }

  @Override
  public SingleActorDAOIF getUser()
  {
    return this.getHolder().getUser();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.SessionIF#getLocale()
   */
  public Locale getLocale()
  {
    return this.getHolder().getLocale();
  }

  /**
   * Returns the dimension set for this session, or null if no dimension has
   * been set.
   * 
   * @return dimension set for this session, or null if no dimension has been
   *         set.
   */
  public MdDimensionDAOIF getDimension()
  {
    String dimensionKey = this.getHolder().getDimensionKey();

    if (dimensionKey != null && ObjectCache.contains(MdDimensionInfo.CLASS, dimensionKey))
    {
      return (MdDimensionDAOIF) BusinessDAO.get(MdDimensionInfo.CLASS, dimensionKey);
    }

    return null;
  }

  /**
   * 
   * @see com.runwaysdk.session.SessionIF#hasUser()
   */
  public boolean hasUser()
  {
    return !this.getHolder().getUser().getOid().equals(UserDAOIF.PUBLIC_USER_ID);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.PermissionEntity#checkTypeAccess(com.runwaysdk.
   * business .rbac.Operation, java.lang.String)
   */
  public boolean checkTypeAccess(Operation o, String type)
  {
    if (this.getHolder().isAdmin())
    {
      return true;
    }

    if (super.checkTypeAccess(o, type))
    {
      return true;
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.PermissionEntity#checkTypeAccess(com.runwaysdk.
   * business .rbac.Operation, com.runwaysdk.dataaccess.MdTypeIF)
   */
  public boolean checkTypeAccess(Operation o, MdTypeDAOIF mdTypeIF)
  {
    if (this.getHolder().isAdmin())
    {
      return true;
    }

    if (super.checkTypeAccess(o, mdTypeIF))
    {
      return true;
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.session.PermissionEntity#checkAccess(com.runwaysdk.business
   * .rbac.Operation, com.runwaysdk.business.Mutable)
   */
  @Override
  public boolean checkAccess(Operation o, Mutable mutable)
  {
    if (this.getHolder().isAdmin())
    {
      return true;
    }

    if (super.checkAccess(o, mutable))
    {
      return true;
    }

    return false;
  }

  @Override
  protected Set<Operation> getOperations(Mutable component, MdClassDAOIF mdClassIF)
  {
    Set<Operation> operations = super.getOperations(component, mdClassIF);

    if (component instanceof Ownable && this.checkOwner(component))
    {
      ConcurrentHashMap<String, Set<Operation>> ownerPermissions = PermissionCache.getOwnerPermissions();

      // Load owner type permissions
      if (ownerPermissions.containsKey(mdClassIF.getOid()))
      {
        operations.addAll(ownerPermissions.get(mdClassIF.getOid()));
      }

      // Load owner domain-type permissions
      if (component instanceof Element)
      {
        String domainId = component.getValue(ElementInfo.DOMAIN);

        if (!domainId.equals(""))
        {
          String key = DomainTupleDAO.buildKey(domainId, mdClassIF.getOid(), null);

          if (ownerPermissions.containsKey(key))
          {
            operations.addAll(ownerPermissions.get(key));
          }
        }
      }
    }

    return operations;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.PermissionEntity#checkAttributeTypeAccess(com.
   * runwaysdk .business.rbac.Operation, com.runwaysdk.dataaccess.MdAttributeIF)
   */
  public boolean checkAttributeTypeAccess(Operation operation, MdAttributeDAOIF mdAttribute)
  {
    if (this.getHolder().isAdmin())
    {
      return true;
    }

    if (super.checkAttributeAccess(operation, mdAttribute))
    {
      return true;
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * 
   * 
   * 
   * 
   * @seecom.runwaysdk.session.PermissionEntity#checkAttributeAccess(com.
   * runwaysdk .business.rbac.Operation, com.runwaysdk.business.Mutable,
   * com.runwaysdk.dataaccess.MdAttributeIF)
   */
  @Override
  public boolean checkAttributeAccess(Operation operation, Mutable mutable, MdAttributeDAOIF mdAttribute)
  {
    if (this.getHolder().isAdmin())
    {
      return true;
    }

    if (super.checkAttributeAccess(operation, mutable, mdAttribute))
    {
      return true;
    }

    // if (mutable instanceof Ownable)
    // {
    // StateMasterIF state = Session.getState(mutable);
    // String tupleReference = null;
    //
    // // If this businessDAO has a state then get it to use in the owner
    // check
    // if (state != null)
    // {
    // tupleReference = PermissionTuple.buildKey(mdAttribute.getOid(),
    // state.getOid());
    // }
    //
    // // Check if permissions exist on the State-MdAttribute pairing
    // if (checkOwnerAttribute(operation, mutable, mdAttribute,
    // tupleReference))
    // {
    // return true;
    // }
    // }
    //
    return false;
  }

  @Override
  protected Set<Operation> getAttributeOperations(Mutable component, MdAttributeDAOIF mdAttribute)
  {
    Set<Operation> operations = super.getAttributeOperations(component, mdAttribute);

    if (component instanceof Ownable && checkOwner(component))
    {
      // Check if permissions exist of the mdAttribute type
      ConcurrentHashMap<String, Set<Operation>> ownerPermissions = PermissionCache.getOwnerPermissions();

      // Load owner mdAttribute permissions
      if (ownerPermissions.containsKey(mdAttribute.getOid()))
      {
        operations.addAll(ownerPermissions.get(mdAttribute.getOid()));
      }

      // Load owner domain-mdAttribute permissions
      if (component instanceof Element)
      {
        String domainId = component.getValue(ElementInfo.DOMAIN);

        // Load owner domain-attribute permissions
        if (!domainId.equals(""))
        {
          String key = DomainTupleDAO.buildKey(domainId, mdAttribute.getOid(), null);

          if (ownerPermissions.containsKey(key))
          {
            operations.addAll(ownerPermissions.get(key));
          }
        }
      }
    }

    return operations;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.session.PermissionEntity#checkRelationshipAttributeAccess
   * (com.runwaysdk.business.rbac.Operation, com.runwaysdk.business.Business,
   * com.runwaysdk.dataaccess.MdAttributeIF)
   */
  public boolean checkRelationshipAttributeAccess(Operation operation, Business business, MdAttributeDAOIF mdAttribute)
  {
    if (this.getHolder().isAdmin())
    {
      return true;
    }

    if (super.checkRelationshipAttributeAccess(operation, business, mdAttribute))
    {
      return true;
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.runwaysdk.session.PermissionEntity#checkAttributeAccess(com.
   * runwaysdk.business.rbac.Operation, com.runwaysdk.business.Struct,
   * com.runwaysdk.dataaccess.MdAttributeIF)
   */
  @Override
  public boolean checkAttributeAccess(Operation operation, Struct struct, MdAttributeDAOIF mdAttribute)
  {
    // Structs do not have states or owners, thus only perform the check on
    // MdAttributeIF permissions
    return checkAttributeAccess(operation, mdAttribute);
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.runwaysdk.session.PermissionEntity#checkAttributeAccess(com.
   * runwaysdk.business.rbac.Operation, com.runwaysdk.dataaccess.MdAttributeIF)
   */
  @Override
  public boolean checkAttributeAccess(Operation operation, MdAttributeDAOIF mdAttribute)
  {
    return super.checkAttributeAccess(operation, mdAttribute);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.PermissionEntity#checkRelationshipAccess(com
   * .runwaysdk.business.rbac.Operation, com.runwaysdk.business.Business,
   * java.lang.String)
   */
  @Override
  public boolean checkRelationshipAccess(Operation o, Business business, String mdRelationshipId)
  {
    if (this.getHolder().isAdmin())
    {
      return true;
    }
    // ADD/DELETE/READ PARENT/CHILD permission.
    if (super.checkRelationshipAccess(o, business, mdRelationshipId))
    {
      return true;
    }

    if (this.getOwnerRelationshipOperations(business, mdRelationshipId).contains(o))
    {
      return true;
    }

    return false;
  }

  public boolean checkEdgeAccess(Operation o, VertexObject vertex, String mdEdgeId)
  {
    if (this.getHolder().isAdmin())
    {
      return true;
    }
    // ADD/DELETE/READ PARENT/CHILD permission.
    if (super.checkEdgeAccess(o, vertex, mdEdgeId))
    {
      return true;
    }

    return false;
  }

  private Set<Operation> getOwnerRelationshipOperations(Business business, String mdRelationshipId)
  {
    Set<Operation> operations = new TreeSet<Operation>();

    // Perform the owner check on the business object and not the
    // relationship.
    // If the current user owns the business object load directional
    // permissions:
    if (checkOwner(business))
    {
      ConcurrentHashMap<String, Set<Operation>> ownerPermissions = PermissionCache.getOwnerPermissions();

      // load owner permissions
      if (ownerPermissions.containsKey(mdRelationshipId))
      {
        operations.addAll(ownerPermissions.get(mdRelationshipId));
      }

      // Load owner domain permissions
      String domainId = business.getValue(ElementInfo.DOMAIN);

      if (!domainId.equals(""))
      {
        String key = DomainTupleDAO.buildKey(domainId, mdRelationshipId, null);

        if (ownerPermissions.containsKey(key))
        {
          operations.addAll(ownerPermissions.get(key));
        }
      }
    }

    return operations;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.SessionIF#checkExecuteAccess(
   * com.runwaysdk.business.rbac.Operation, com.runwaysdk.business.Mutable,
   * com.runwaysdk.dataaccess.MdMethodIF)
   */
  public boolean checkMethodAccess(Operation operation, MdMethodDAOIF mdMethod)
  {
    // Admin check
    if (this.getHolder().isAdmin())
    {
      return true;
    }

    // Check if the user has permissions
    return super.checkMethodAccess(operation, mdMethod);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.SessionIF#checkExecuteAccess(
   * com.runwaysdk.business.rbac.Operation, com.runwaysdk.business.Mutable,
   * com.runwaysdk.dataaccess.MdMethodIF)
   */
  public boolean checkMethodAccess(Operation operation, Mutable mutable, MdMethodDAOIF mdMethod)
  {
    // Admin check
    if (this.getHolder().isAdmin())
    {
      return true;
    }

    // Check if the user has permissions
    if (super.checkMethodAccess(operation, mdMethod))
    {
      return true;
    }

    if (checkOwner(mutable))
    {
      ConcurrentHashMap<String, Set<Operation>> ownerPermissions = PermissionCache.getOwnerPermissions();
      Set<Operation> operations = new TreeSet<Operation>();

      // Load owner method
      if (ownerPermissions.containsKey(mdMethod.getOid()))
      {
        operations.addAll(ownerPermissions.get(mdMethod.getOid()));
      }

      // Load owner domain-method
      if (mutable instanceof Element)
      {
        String domainId = mutable.getValue(ElementInfo.DOMAIN);

        if (!domainId.equals(""))
        {
          String key = DomainTupleDAO.buildKey(domainId, mdMethod.getOid(), null);

          if (ownerPermissions.containsKey(key))
          {
            operations.addAll(ownerPermissions.get(domainId));
          }
        }
      }

      if (operations.contains(operation))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns if the user of this session is the owner of the given entity
   * object, but will return true if the mutable is not an instance of Ownable.
   * 
   * @param entity
   *          The mutable object to check owner of
   * 
   * @return Returns if the user of this session is the owner of the given
   *         mutable object
   */
  private boolean checkOwner(Mutable mutable)
  {
    if (mutable instanceof Ownable)
    {
      String ownerId = ( (Ownable) mutable ).getOwnerId();

      if (ownerId != null && !ownerId.equals(""))
      {
        return ownerId.equals(this.getHolder().getUser().getOid());
      }
      else
      {
        return true;
      }
    }
    else
    {
      return true;
    }
  }

  public int hashCode()
  {
    return this.oid.hashCode();
  }

  private void buildPermissionHolder(SingleActorDAOIF user, Locale locale, String dimensionKey)
  {
    if (locale == null)
    {
      if (user == null || user.getOid().equals(UserDAOIF.PUBLIC_USER_ID))
      {
        locale = CommonProperties.getDefaultLocale();
      }
      else
      {
        locale = ConversionFacade.getLocale(user.getLocale());
      }
    }

    if (user != null)
    {
      if (user.getOid().equals(UserDAOIF.PUBLIC_USER_ID))
      {
        this.setHolder(new ImmutablePermissionHolder(PermissionCache.getPublicPermissions(), user, locale));
      }
      else
      {
        // Set the locale to the one configured for the user if no valid locale
        // was set.
        Map<String, RoleDAOIF> authorizedRoleMap = new HashMap<String, RoleDAOIF>();

        for (RoleDAOIF roleIF : user.authorizedRoles())
        {
          authorizedRoleMap.put(roleIF.getRoleName(), roleIF);
        }

        // If the user is an administrator there is no need to load permissions
        if (user.isAdministrator())
        {
          this.setHolder(new ImmutablePermissionHolder(user, locale, dimensionKey, authorizedRoleMap));
        }
        else
        {
          PermissionMap map = user.getOperations();
          map.join(new PermissionMap(PermissionCache.getPublicPermissions()), false);

          this.setHolder(new ImmutablePermissionHolder(map.getPermissions(), user, locale, dimensionKey, authorizedRoleMap));
        }
      }
    }
  }

  public void reloadPermissions()
  {
    this.buildPermissionHolder(this.getUser(), this.getLocale(), this.getHolder().getDimensionKey());
  }

  public void setLocale(Locale locale)
  {
    this.buildPermissionHolder(this.getUser(), locale, this.getHolder().getDimensionKey());
  }

  public void setUser(SingleActorDAOIF user)
  {
    this.buildPermissionHolder(user, this.getLocale(), this.getHolder().getDimensionKey());
  }

  /**
   * Set the dimension for this session.
   * 
   * @param dimension
   *          for this session.
   */
  public void setDimension(MdDimensionDAOIF mdDimensionDAOIF)
  {
    if (mdDimensionDAOIF == null)
    {
      this.setHolder(this.getHolder().setDimensionKey(null));
    }
    else
    {
      this.setHolder(this.getHolder().setDimensionKey(mdDimensionDAOIF.getKey()));
    }
  }

  /**
   * Set the dimension for this session.
   * 
   * @param dimension
   *          key for this session.
   * 
   * @throws {@link
   *           DataNotFoundException} if the key does not represent a valid
   *           dimension if the given key is not null.
   */
  public void setDimension(String dimensionKey)
  {
    if (dimensionKey == null)
    {
      this.setDimension((MdDimensionDAOIF) null);
    }
    else
    {
      this.setDimension((MdDimensionDAOIF) BusinessDAO.get(MdDimensionInfo.CLASS, dimensionKey));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.SessionIF#userHasRole(java.lang.String)
   */
  public boolean userHasRole(String roleName)
  {
    if (this.getHolder().isAdmin())
    {
      return true;
    }
    else
    {
      return this.getHolder().getAuthorizedRoleMap().containsKey(roleName);
    }
  }

  /**
   * Returns a Map representing all of the roles assigned to the given user,
   * either implicitly or explicitly.
   * 
   * @return Map representing all of the roles assigned to the given user,
   *         either implicitly or explicitly.
   */
  public Map<String, String> getUserRoles()
  {
    Map<String, String> roleMap = new HashMap<String, String>();

    Map<String, RoleDAOIF> map = this.getHolder().getAuthorizedRoleMap();

    for (Entry<String, RoleDAOIF> entry : map.entrySet())
    {
      roleMap.put(entry.getKey(), entry.getValue().getDisplayLabel(this.getLocale()));
    }

    return roleMap;
  }

  @Override
  public void notify(PermissionEvent e)
  {
    // Balk:
    // At this moment no action is required for
    // PermissionEvents because Session permissions
    // are only reloaded during a log in.
  }

  // Mutable actions

  /**
   * Sets the time (represented as a long) in which this session expires.
   * 
   * @param time
   */
  void setExpirationTime(long time)
  {
    try (CloseableReentrantLock lock = this.getPermissionLock().open())
    {
      this.expirationTime = time;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.SessionIF#get(java.lang.String)
   */
  public Mutable get(String _id)
  {
    if (_id == null || _id.trim().equals(""))
    {
      String errorMessage = "Id [" + _id + "] is invalid.";
      throw new InvalidIdException(errorMessage, _id);
    }

    return (Mutable) this.mutableMap.get(_id);
  }

  /**
   * Adds the given object to the session. Object is stored in a map where the
   * key is the oid of the object.
   * 
   * @param mutable
   */
  public void put(Mutable mutable)
  {
    this.mutableMap.put(mutable.getOid(), mutable);
  }

  /**
   * Adds the given object to the session stored in a map using the given key.
   * Object is stored in a map where the key is the oid of the object.
   * 
   * @param mutable
   */
  public void put(String key, Mutable mutable)
  {
    this.mutableMap.put(key, mutable);
  }

  /**
   * Removes the Mutable object with the given key that has been stored in this
   * session.
   * 
   * @param key
   *          .
   */
  public void remove(String key)
  {
    this.mutableMap.remove(key);
  }

  /**
   * Sets the flag indicating if this session should be closed at the end of its
   * current request
   * 
   * @param closeOnEndOfRequest
   *          Boolean flag.
   */
  public void setCloseOnEndOfRequest(boolean closeOnEndOfRequest)
  {
    try (CloseableReentrantLock lock = this.getPermissionLock().open())
    {
      this.closeOnEndOfRequest = closeOnEndOfRequest;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.SessionIF#closeOnEndOfRequest()
   */
  public boolean closeOnEndOfRequest()
  {
    try (CloseableReentrantLock lock = this.getPermissionLock().open())
    {
      return this.closeOnEndOfRequest;
    }
  }

  /**
   * Sets the first {@link MdMethodDAOIF encountered in the request (if any).
   *
   * @param mdMethodDAOIF
   *          first {@link MdMethodDAOIF encountered in the request (if any).
   */
  public void setFirstMdMethodDAOIF(MdMethodDAOIF mdMethodDAOIF)
  {
    try (CloseableReentrantLock lock = this.getPermissionLock().open())
    {
      // Only set it once for the request
      if (this.firstMethodInRequest == null || mdMethodDAOIF == null)
      {
        this.firstMethodInRequest = mdMethodDAOIF;
      }
    }
  }

  /**
   * SingleActorDAOIF returns the first {@link MdMethodDAOIF encountered in the
   * request (if any).
   *
   * @return the first {@link MdMethodDAOIF encountered in the request (if any).
   */
  public MdMethodDAOIF getFirstMdMethodDAOIF()
  {
    return this.firstMethodInRequest;
  }

  /**
   * Get the time in milliseconds when the session expires
   * 
   * @return The time in milliseconds when the session expires
   */
  protected long getExpiration()
  {
    try (CloseableReentrantLock lock = this.getPermissionLock().open())
    {
      return expirationTime;
    }
  }

  /**
   * Renews the time to live of the session
   */
  protected void renew()
  {
    try (CloseableReentrantLock lock = this.getPermissionLock().open())
    {
      this.expirationTime = System.currentTimeMillis() + timeToLive;
    }
  }

  /**
   * Sets the expiration time of this session to the current time.
   */
  protected void expire()
  {
    try (CloseableReentrantLock lock = this.getPermissionLock().open())
    {
      this.expirationTime = System.currentTimeMillis() - 1;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.session.SessionIF#isExpired(long)
   */
  public boolean isExpired(long time)
  {
    long expiration = this.getExpiration();

    return ( ( expiration != -1 ) && ( expiration < time ) );
  }

  /**
   * Forces an ordering on sessions where the item with the lowest expiration
   * time Is the foremost session
   * 
   * @param arg0
   *          The session to compare against
   * @return The order of the two sessions
   */
  public int compareTo(Session s0)
  {
    long expiration = this.getExpiration();

    // IMPORTANT: This ordering works for the PriorityQueue implementation in
    // Java 1.6 but it must be reversed to work with the PriorityQueue
    // implementation in Java 1.5
    if (expiration == -1 || s0.getExpiration() < expiration)
    {
      return -1 * comparatorOffset; // Java 1.5: return 1;
    }
    else if (s0.getExpiration() == -1 || s0.getExpiration() > expiration)
    {
      return 1 * comparatorOffset; // Java 1.5: return -1;
    }

    return this.getOid().compareTo(s0.getOid());
  }

  public boolean equals(Object s0)
  {
    if (! ( s0 instanceof Session ))
    {
      return false;
    }

    if (this.compareTo((Session) s0) == 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Hook method for aspects. This is used to create a mapping between the oid
   * of an object that is generated on a new <code>MutableDTO</code> to the oid
   * that is generated on the <code>Mutable</code> object to which the values of
   * the DTO are copied.
   * 
   * @param oldTempId
   *          original temporary oid for new instances.
   * @param newId
   */
  public static void mapNewInstanceTempId(String oldTempId, String newId)
  {
  }

  /**
   * Returns the current session object.
   * 
   * @return current session object.
   */
  public static SessionIF getCurrentSession()
  {
    // AbstractRequestManagement will return the current session.
    return null;
  }

  /**
   * Sets the amount of time a session has before it expires
   * 
   * @param seconds
   *          The amount of time in seconds
   */
  public static void setSessionTime(long seconds)
  {
    timeToLive = seconds * SECOND;
  }

  /**
   * Returns the default amount of time a session has before it expires
   * 
   * @return The seconds of default time to live
   */
  public static long getSessionTime()
  {
    return timeToLive / SECOND;
  }

  /**
   * Returns the locale for the current session.
   * 
   * @return locale for the current session.
   */
  public static Locale getCurrentLocale()
  {
    SessionIF sessionIF = getCurrentSession();

    if (sessionIF != null)
    {
      return sessionIF.getLocale();
    }
    else
    {
      return CommonProperties.getDefaultLocale();
    }
  }

  /**
   * Returns the dimension for the current session, or null if no dimension has
   * been set for this session.
   * 
   * @return dimension for the current session, or null if no dimension has been
   *         set for this session.
   */
  public static MdDimensionDAOIF getCurrentDimension()
  {
    SessionIF sessionIF = getCurrentSession();

    if (sessionIF != null)
    {
      return sessionIF.getDimension();
    }
    else
    {
      return null;
    }
  }
}
