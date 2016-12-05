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
package com.runwaysdk.business;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.ElementDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.dataaccess.transaction.LockObject;
import com.runwaysdk.session.Ownable;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;


/**
 *
 * @author Nathan
 */
public abstract class Element extends Entity implements MutableWithStructs, Ownable
{
  /**
   * 
   */
  private static final long serialVersionUID = -7718572189379971369L;
  
  public final static String CLASS = Element.class.getName();

  public Element()
  {
    super();
  }

  /**
   * A generic, type-unsafe getter for struct attributes that takes the
   * attribute and struct names as Strings, and returns the value as a
   * String
   *
   * @param structName String name of the struct
   * @param name String name of the desired attribute
   * @return String representation of the struct value
   */
  public String getStructValue(String structName, String attributeName)
  {
    return entityDAO.getStructValue(structName, attributeName);
  }

  /**
   * Returns the value for the attribute that matches the given locale (or a best fit).
   *
   * @param localAttributeName
   * @param local
   * @return the value of a local attribute
   */
  public String getLocalValue(String localAttributeName, Locale locale)
  {
    return entityDAO.getLocalValue(localAttributeName, locale);
  }

  /**
   * A generic, type-unsafe getter for struct blob attributes that takes the
   * attribute and struct names as Strings, and returns the value as a
   * byte array
   *
   * @param structName String name of the struct
   * @param blobName String name of the desired blob attribute
   * @return byte[] representation of the struct value
   */
  public byte[] getStructBlob(String structName, String blobName)
  {
    return entityDAO.getStructBlob(structName, blobName);
  }

  /**
   * Returns a list of selected values for the given enumerated attribute. The
   * declared type of the list is BusinessEnumeration, but each entry is
   * instantiated through reflection, which allows for accurate actual types.
   *
   * @param name Name of the attribute enumeration
   * @return List of typesafe enumeration options that are selected
   */
  public List<? extends BusinessEnumeration> getStructEnumValues(String structName, String attributeName)
  {
    AttributeStructIF struct = (AttributeStructIF) entityDAO.getAttributeIF(structName);
    AttributeEnumerationIF attribute = (AttributeEnumerationIF) struct.getAttributeIF(attributeName);

    Set<String> ids = (attribute).getCachedEnumItemIdSet();
    MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttributeConcrete();

    return loadEnumValues(ids, mdAttribute);
  }

  /**
   * Returns the Struct associated with an AttributeStruct
   *
   * @param structName The name of the AttributeStruct
   * @return A Struct representation of the AttributeStruct
   */
  public Struct getStruct(String structName)
  {
    Struct struct = Struct.instantiate(this, structName);

    return struct;
  }

  /**
   * Returns the type unsafe Struct (actual type of Struct) associated with an AttributeStruct
   *
   * @param structName The name of the AttributeStruct
   * @return A Struct representation of the AttributeStruct
   */
  public Struct getGenericStruct(String structName)
  {
    MdAttributeDAOIF mdAttributeDAOIF = this.getMdAttributeDAO(structName);

    //The StructDAO that the Struct delegates to
    //must be the same java object as the StructDAO which
    //the AttributeStructIF delegates to or else the apply method
    //will apply a different StructDAO.
    if (mdAttributeDAOIF instanceof MdAttributeLocalDAOIF)
    {
      return new LocalStruct(this, structName);
    }
    else
    {
      return new Struct(this, structName);
    }
  }

  /**
   * A generic, type-unsafe setter for struct attributes that takes the
   * attribute name, struct name, and value as Strings
   *
   * @param structName String name of the struct
   * @param blobName String name of the desired attribute
   * @param vale  String representation of the struct value
   */
  public void setStructBlob(String structAttributeName, String blobName, byte[] value)
  {
    entityDAO.setStructBlob(structAttributeName, blobName, value);
  }

  /**
   * A generic, type-unsafe setter for struct attributes that takes the
   * attribute name, struct name, and value as Strings
   *
   * @param structName String name of the struct
   * @param name String name of the desired attribute
   * @param vale  String representation of the struct value
   */
  public void setStructValue(String structName, String attributeName, String _value)
  {
    String value = "";

    if (_value != null)
    {
      value = _value;
    }

    entityDAO.setStructValue(structName, attributeName, value);
  }

  /**
   * Adds an item to an enumerated struct attribute.
   *
   * @param structName The name of the struct
   * @param attributeName The name of the attribute (inside the struct)
   * @param value The value to set
   */
  public void addStructItem(String structName, String attributeName, String value)
  {
    ((ElementDAO) entityDAO).addStructItem(structName, attributeName, value);
  }

  /**
   * Replaces the items of an enumerated struct attribute. If the attribute
   * does not allow multiplicity, then the {@code values} collection must
   * contain only one item.
   *
   * @param structName The name of the struct
   * @param attributeName The name of the attribute (inside the struct)
   * @param values Collection of enumerated it ids
   */
  public void replaceStructItems(String structName, String attributeName, Collection<String> values)
  {
    ((ElementDAO) entityDAO).replaceStructItems(structName, attributeName, values);
  }

  /**
   * Remove an item for an enumerated struct attribute.
   *
   * @param structName The name of the struct
   * @param attributeName The name of the attribute (inside the struct)
   * @param value The value to set
   */
  public void removeStructItem(String structName, String attributeName, String value)
  {
    ((ElementDAO) entityDAO).removeStructItem(structName, attributeName, value);
  }

  /**
   * Clears all the values of a struct enumeration attribute.
   *
   * @param structName The name of the struct
   * @param attributeName The name of the attribute (inside the struct)
   */
  public void clearStructItems(String structName, String attributeName)
  {
    ((ElementDAO) entityDAO).clearStructItems(structName, attributeName);
  }

  /**
   * Returns a UserIF object that represents the user that has a lock on this entity, or null if the entity is not locked.
   *
   * @return UserIF object that represents the user that has a lock on this entity, or null if the entity is not locked.
   */
  protected SingleActorDAOIF getLockedByDAO()
  {
    if (entityDAO.getAttributeIF(ElementInfo.LOCKED_BY).getValue().trim().equals(""))
    {
      return null;
    }
    else
    {
      return (SingleActorDAOIF)((AttributeReference)entityDAO.getAttributeIF(ElementInfo.LOCKED_BY)).dereference();
    }
  }

  /**
   * Returns the Actor Data Access Object that owns this object.
   * @return Actor Data Access Object that owns this object.
   */
  public ActorDAOIF getOwnerDAO()
  {
    if (entityDAO.getAttributeIF(ElementInfo.OWNER).getValue().trim().equals(""))
    {
      return null;
    }
    else
    {
      return (ActorDAOIF)((AttributeReference)entityDAO.getAttributeIF(ElementInfo.OWNER)).dereference();
    }
  }
  
  /**
   * Returns the Actor Id that owns this object.
   * 
   * @see com.runwaysdk.session.Ownable#getOwnerId()
   */
  public String getOwnerId()
  {
    return getValue(ElementInfo.OWNER);
  }


  /**
   * Locks the given Entity by the given user.  This method does not use reflection.
   *
   * <br/>
   * <b>Precondition:</b> userId != null <br/>
   * <b>Precondition:</b> !userId.trim().equals("") <br/>
   *
   * @param userId
   *          id of the user locking the Entity
   * @throws DataAccessException
   *           if the Entity is locked by another user
   */
  public void userLock(String userId)
  {   
    (LockObject.getLockObject()).appLock(this.getId());
    
    this.setDataEntity((EntityDAO.get(this.entityDAO.getId())).getEntityDAO());
    
    // make sure there is no existing user lock
    if (this.hasUserLockFromDifferentUser(userId))
    {
      // Release the Java lock, as it is already locked by another user.
      (LockObject.getLockObject()).releaseAppLock(this.getId());

      String lockedBy = this.entityDAO.getValue(ElementInfo.LOCKED_BY);

      String errMsg = "User [" + UserDAO.get(userId).getSingleActorName() + "] cannot lock entity ["
          + this.getId() + "] because it is already locked by user ["
          + UserDAO.get(lockedBy).getSingleActorName() + "]";
      throw new LockException(errMsg, this, "ExistingLockException");
    }

    this.entityDAO.getAttribute(ElementInfo.LOCKED_BY).setValue(userId);
    this.entityDAO.apply();
  }


  /**
   * Locks the given Entity by the current session user.  Aspects
   * take care determining the session user.
   *
   * @throws DataAccessException
   *           if the Entity is locked by another user
   */
  public void lock()
  {
    SessionIF session = Session.getCurrentSession();
    
    if(session != null)
    {
      this.userLock(session.getUser().getId());
    }
  }


  /**
   * Unlocks the this Entity if the given user has a lock on the object.
   *
   * <br/>
   * <b>Precondition:</b> userId != null <br/>
   * <b>Precondition:</b> !userId.trim().equals("") <br/>
   *
   * @param userId
   *          id of the user locking the Entity
   * @throws BusinessException
   *           if the Entity is locked by another user
   */
  public void releaseUserLock(String userId)
  {
    (LockObject.getLockObject()).appLock(this.getId());

    // If this object has been modified during this transaction, then this should return a reference to the
    // same entityDAO object.
    this.setDataEntity((EntityDAO.get(this.entityDAO.getId()).getEntityDAO()));

    String lockedBy = this.entityDAO.getAttributeIF(ElementInfo.LOCKED_BY).getValue();

    // ComponentIF.LOCKED_BY has no value.
    if (!lockedBy.equals(""))
    {
      // Entity is locked by a different user
      if (!lockedBy.equals(userId))
      {
        String error = "User [" + UserDAO.get(userId).getSingleActorName() + "] cannot unlock Entity ["
            + this.getId() + "] because it is locked by user [" + UserDAO.get(lockedBy).getSingleActorName()
            + "]";

        throw new LockException(error, this, "ExistingUnlockException");
      }
    }

    // user has the lock on this item so he or she can unlock it
    this.entityDAO.getAttribute(ElementInfo.LOCKED_BY).setValue("");
    this.entityDAO.apply();

    // Release the lock on this object
    (LockObject.getLockObject()).releaseAppLock(this.getId());
  }


  /**
   * Unlocks the given Entity for the current session user.  Aspects
   * take care determining the session user.
   *
   * @throws DataAccessException
   *           if the Entity is locked by another user
   */
  public void unlock()
  {
    SessionIF session = Session.getCurrentSession();
    if(session != null)
    {
      this.releaseUserLock(session.getUser().getId());
    }
  }

  /**
   * Locks the given Entity by the current treads.
   *
   * @param userId
   * @throws DataAccessException
   *           if the Entity is locked by another user
   */
  public void appLock(String userId)
  {
    // StructDAO does not have the ComponentIF.LOCKED_BY attribute
    if (!this.entityDAO.hasAttribute(ElementInfo.LOCKED_BY))
    {
      return;
    }

    // Wait until this thread attains a lock on this object
    (LockObject.getLockObject()).appLock(this.getId());

    this.setDataEntity((EntityDAO.get(this.entityDAO.getId())).getEntityDAO());

    // make sure there is no existing user lock
    if (this.hasUserLockFromDifferentUser(userId))
    {
      // Release the Java lock, as it is already locked by another user.
      (LockObject.getLockObject()).releaseAppLock(this.getId());

      String lockedBy = this.entityDAO.getValue(ElementInfo.LOCKED_BY);

      String errMsg = "Entity ["
          + this.getId() + "] cannot be locked because it is already locked by user ["
          + UserDAO.get(lockedBy).getSingleActorName() + "]";
      throw new LockException(errMsg, this, "ExistingLockException");
    }

  }

  /**
   * Returns true if the lockedby field  is populated, meaning a user lock exists on this
   * object by a different user, false otherwise.
   * @param userId id of the user who wants to get a lock on this object
   * @return true if the lockedby field  is populated, meaning a user lock exists on this
   * object by a different user, false otherwise.
   */
  protected boolean hasUserLockFromDifferentUser(String userId)
  {
    String lockedBy = this.entityDAO.getValue(ElementInfo.LOCKED_BY);

    if (!lockedBy.equals(userId) &&
        !lockedBy.equals(""))
    {
      Date lastUpdateDate = this.getLastUpdateDate();
      int lockTimeout = ServerProperties.getLockTimeout() * 60 * 1000;

      long now = new Date().getTime();

      // Only error out if the lock has not expired
      if (now < (lastUpdateDate.getTime() + lockTimeout))
      {
        return true;
      }
    }

    return false;
  }



  /**
   * Checks if the current session user has a lock on this object.
   *
   * Throws an exception if the currently logged in user does not have a lock on the object.
   */
  public void validateLock()
  {
    if (!this.checkUserLock())
    {
      String errMsg = "User needs a lock in order to modify ["+this.toString()+"]";
      throw new LockException(errMsg, this, "NeedLockException");
    }
  }

  /**
   * Checks if the current session user has a lock on this object.
   *
   * @return true if the current user has a lock on this object, false otherwise.
   */
  public boolean checkUserLock()
  {
    String userLockId = entityDAO.getValue(ElementInfo.LOCKED_BY);
    // Set the lock value that denotes if the current user
    SessionIF session = Session.getCurrentSession();
    if(session != null)
    {
      String currentUserId = session.getUser().getId();

      if(currentUserId.equals(userLockId))
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else
    {
      return false;
    }
  }

  /**
   * Deletes this entity from the database. Any attempt to
   * {@link Entity#apply()} this entity will throw an exception, so it
   * is the responsibility of the developer to remove references to deleted
   * instances of Entity.
   */
  public void delete()
  {
    // Make sure we avoid a stale entity exception
    this.appLock();

    SessionIF session = Session.getCurrentSession();
    if(session != null)
    {
      String currentUserId = session.getUser().getId();
      // make sure there is no existing user lock
      if (this.hasUserLockFromDifferentUser(currentUserId))
      {
        // Release the Java lock, as it is already locked by another user.
        (LockObject.getLockObject()).releaseAppLock(this.getId());

        String lockedBy = this.entityDAO.getValue(ElementInfo.LOCKED_BY);

        String errMsg = "Entity ["
            + this.getId() + "] cannot be deleted because it is locked by user ["
            + UserDAO.get(lockedBy).getSingleActorName() + "]";
        throw new LockException(errMsg, this, "ExistingLockException");
      }
    }
    super.delete();

    this.releaseAppLock();
  }

  /**********************************************************************************/
  /**                      Generated accessor methods                              **/
  /**********************************************************************************/
  public static java.lang.String TYPE = "type";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static java.lang.String LOCKEDBY = "lockedBy";
  public static java.lang.String SEQ = "seq";
  public static java.lang.String OWNER = "owner";
  public static java.lang.String ENTITYDOMAIN = "entityDomain";

  public String getType()
  {
    return getValue(TYPE);
  }

  public void validateType()
  {
    this.validateAttribute(TYPE);
  }

  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.business.Element.CLASS);
    return mdClassIF.definesAttribute(TYPE);
  }

  public void setType(String value)
  {
    if(value == null)
    {
      setValue(TYPE, "");
    }
    else
    {
      setValue(TYPE, value);
    }
  }

  public com.runwaysdk.system.SingleActor getCreatedBy()
  {
    try
    {
      return com.runwaysdk.system.SingleActor.get(getValue(CREATEDBY));
    }
    catch (com.runwaysdk.dataaccess.cache.DataNotFoundException e)
    {
      return null;
    }
  }

  public void validateCreatedBy()
  {
    this.validateAttribute(CREATEDBY);
  }

  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getCreatedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.business.Element.CLASS);
    return mdClassIF.definesAttribute(CREATEDBY);
  }

  public void setCreatedBy(com.runwaysdk.system.SingleActor value)
  {
    if(value == null)
    {
      setValue(CREATEDBY, "");
    }
    else
    {
      setValue(CREATEDBY, value.getId());
    }
  }

  public java.util.Date getCreateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(CREATEDATE));
  }

  public void validateCreateDate()
  {
    this.validateAttribute(CREATEDATE);
  }

  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getCreateDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.business.Element.CLASS);
    return mdClassIF.definesAttribute(CREATEDATE);
  }

  public void setCreateDate(java.util.Date value)
  {
    if(value == null)
    {
      setValue(CREATEDATE, "");
    }
    else
    {
      setValue(CREATEDATE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATETIME_FORMAT).format(value));
    }
  }

  public com.runwaysdk.system.SingleActor getLastUpdatedBy()
  {
    try
    {
      return com.runwaysdk.system.SingleActor.get(getValue(LASTUPDATEDBY));
    }
    catch (com.runwaysdk.dataaccess.cache.DataNotFoundException e)
    {
      return null;
    }
  }

  public void validateLastUpdatedBy()
  {
    this.validateAttribute(LASTUPDATEDBY);
  }

  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getLastUpdatedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.business.Element.CLASS);
    return mdClassIF.definesAttribute(LASTUPDATEDBY);
  }

  public void setLastUpdatedBy(com.runwaysdk.system.SingleActor value)
  {
    if(value == null)
    {
      setValue(LASTUPDATEDBY, "");
    }
    else
    {
      setValue(LASTUPDATEDBY, value.getId());
    }
  }

  public java.util.Date getLastUpdateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTUPDATEDATE));
  }

  public void validateLastUpdateDate()
  {
    this.validateAttribute(LASTUPDATEDATE);
  }

  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getLastUpdateDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.business.Element.CLASS);
    return mdClassIF.definesAttribute(LASTUPDATEDATE);
  }

  public void setLastUpdateDate(java.util.Date value)
  {
    if(value == null)
    {
      setValue(LASTUPDATEDATE, "");
    }
    else
    {
      setValue(LASTUPDATEDATE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATETIME_FORMAT).format(value));
    }
  }

  public com.runwaysdk.system.SingleActor getLockedBy()
  {
    try
    {
      return com.runwaysdk.system.SingleActor.get(getValue(LOCKEDBY));
    }
    catch (com.runwaysdk.dataaccess.cache.DataNotFoundException e)
    {
      return null;
    }
  }

  public void validateLockedBy()
  {
    this.validateAttribute(LOCKEDBY);
  }

  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getLockedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.business.Element.CLASS);
    return mdClassIF.definesAttribute(LOCKEDBY);
  }

  public void setLockedBy(com.runwaysdk.system.Users value)
  {
    if(value == null)
    {
      setValue(LOCKEDBY, "");
    }
    else
    {
      setValue(LOCKEDBY, value.getId());
    }
  }

  public Long getSeq()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(SEQ));
  }

  public void validateSeq()
  {
    this.validateAttribute(SEQ);
  }

  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getSeqMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.business.Element.CLASS);
    return mdClassIF.definesAttribute(SEQ);
  }

  public void setSeq(Long value)
  {
    if(value == null)
    {
      setValue(SEQ, "");
    }
    else
    {
      setValue(SEQ, java.lang.Long.toString(value));
    }
  }

  public com.runwaysdk.system.Actor getOwner()
  {
    try
    {
      return com.runwaysdk.system.Actor.get(getValue(OWNER));
    }
    catch (com.runwaysdk.dataaccess.cache.DataNotFoundException e)
    {
      return null;
    }
  }

  public void validateOwner()
  {
    this.validateAttribute(OWNER);
  }

  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getOwnerMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.business.Element.CLASS);
    return mdClassIF.definesAttribute(OWNER);
  }

  public void setOwner(com.runwaysdk.system.Actor value)
  {
    if(value == null)
    {
      setValue(OWNER, "");
    }
    else
    {
      setValue(OWNER, value.getId());
    }
  }

  public com.runwaysdk.system.metadata.MdDomain getEntityDomain()
  {
    try
    {
      return com.runwaysdk.system.metadata.MdDomain.get(getValue(ENTITYDOMAIN));
    }
    catch (com.runwaysdk.dataaccess.cache.DataNotFoundException e)
    {
      return null;
    }
  }

  public void validateEntityDomain()
  {
    this.validateAttribute(ENTITYDOMAIN);
  }

  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getEntityDomainMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.business.Element.CLASS);
    return mdClassIF.definesAttribute(ENTITYDOMAIN);
  }

  public void setEntityDomain(com.runwaysdk.system.metadata.MdDomain value)
  {
    if(value == null)
    {
      setValue(ENTITYDOMAIN, "");
    }
    else
    {
      setValue(ENTITYDOMAIN, value.getId());
    }
  }

}
