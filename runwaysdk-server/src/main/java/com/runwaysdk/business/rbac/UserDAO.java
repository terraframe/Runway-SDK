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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.SupportedLocale;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeHash;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;


public class UserDAO extends SingleActorDAO implements UserDAOIF
{
  /**
   * Auto generation serial number
   */
  private static final long  serialVersionUID = -6391082994963896632L;

  /**
   * The default constructor, does not set any attributes
   */
  public UserDAO()
  {
    super();
  }

  /**
   * @param attributeMap
   */
  public UserDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  @Override
  public String apply()
  {
    if(this.isNew() && !this.isAppliedToDB())
    {
      this.setKey(buildKey(this.getSingleActorName()));
      
      //Validate the username
      try
      {
        UserDAO.findUser(this.getUsername());

        String msg = "A user already exists with the username [" + this.getUsername() + "]";
        List<AttributeIF> attributes = new LinkedList<AttributeIF>();
        List<String> values = new LinkedList<String>();

        attributes.add(this.getAttributeIF(UserInfo.USERNAME));
        values.add(this.getUsername());

        throw new DuplicateDataException(msg, this.getMdClassDAO(), attributes, values) ;
      }
      catch(DataNotFoundException e)
      {
        //This is good, a user doesn't exist with the given username
      }
    }

    return super.apply();
  }


  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public UserDAO getBusinessDAO()
  {
    return (UserDAO) super.getBusinessDAO();
  }

  @Override
  public String getSingleActorName()
  {
    return super.getValue(UserInfo.USERNAME);
  }

  /**
   * Set the required username of the user
   *
   * @param value The username, must be unique
   */
  public void setUsername(String value)
  {
    super.setValue(UserInfo.USERNAME, value);
  }

  /**
   * Compares the input string password to the current user password.
   *
   * @return true if the passwords match, false otherwise.
   */
  public boolean compareToPassword(String passwordToCompare)
  {
    AttributeHash passwordHash = (AttributeHash) this.getAttribute(UserInfo.PASSWORD);
    return passwordHash.encryptionEquals(passwordToCompare, false);
  }

  /**
   * Set the required password of the user
   *
   * @param value The new password
   */
  public void setPassword(String value)
  {
    super.setValue(UserInfo.PASSWORD, value);
  }

  /**
   * Returns the locale of the user
   *
   * @return the String name of of the locale
   */
  public String getLocale()
  {
    AttributeEnumerationIF attributeEnum = (AttributeEnumerationIF)this.getAttributeIF(UserInfo.LOCALE);
    BusinessDAOIF[] enumItemArray = attributeEnum.dereference();

    if (enumItemArray.length > 0)
    {
      return enumItemArray[0].getValue(EnumerationMasterInfo.NAME);
    }
    else
    {
      return CommonProperties.getDefaultLocaleString();
    }
  }

  /**
   * Set the locale for this user
   *
   * @param value for the locale
   */
  public void setLocale(SupportedLocale locale)
  {
    super.addItem(UserInfo.LOCALE, locale.getOid());
  }

  /**
   * Return the maximum number of sessions a user can have open concurrently
   *
   * @return The maximum number of sessions a user can have open concurrently
   */
  public int getSessionLimit()
  {
    return Integer.parseInt(super.getValue(UserInfo.SESSION_LIMIT));
  }

  /**
   * Set the maximum number of sessions a user can have open concurrently
   *
   * @param value The new session limit, must be an number
   */
  public void setSessionLimit(String value)
  {
    super.setValue(UserInfo.SESSION_LIMIT, value);
  }

  /**
   * Returns all of the operations a uses has on a object
   * @pre get(mdTypeId) instanceof Metadata
   *
   * @param metadata The oid of the MdType
   * @return A list of all operations the user has access for
   */
  public Set<Operation> getAllPermissions(MetadataDAOIF metadata)
  {
    //Get the permissions for the individual user
    Set<Operation> operations = new TreeSet<Operation>();

    operations.addAll(super.getAllPermissions(metadata));

    Set<RoleDAOIF> set = this.assignedRoles();

    //Get the permissions for al the roles the user is part of
    for(RoleDAOIF role : set)
    {
      operations.addAll(role.getAllPermissions(metadata));
    }

    return operations;
  }

  /**
   * Returns the instance of Users associated with the public user.
   * @param username The username to search for
   * @return The BusinessDAO associated with the public user.
   */
  @Request
  public static UserDAOIF getPublicUser()
  {
    return UserDAO.get(UserDAOIF.PUBLIC_USER_ID);
  }

  /**
   * Returns the instance of Users associated with a given username
   * @param username The username to search for
   * @return The BusinessDAO associated with the username
   */
  public static UserDAOIF findUser(String username)
  {
    if(username == null || username.equals(""))
    {
      String error = "Cannot find a user with an empty username";
      throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(UserInfo.CLASS));
    }

    QueryFactory qFactory = new QueryFactory();
    BusinessDAOQuery userQ = qFactory.businessDAOQuery(UserInfo.CLASS);
    userQ.WHERE(userQ.aCharacter(UserInfo.USERNAME).EQi(username));

    OIterator<BusinessDAOIF> userIterator = userQ.getIterator();

    UserDAOIF user = null;

    if(userIterator.hasNext())
    {
      user = (UserDAOIF) userIterator.next();
      userIterator.close();
    }
    else
    {
      userIterator.close();
      String error = "A user of the username [" + username +"] does not exist";
      throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(UserInfo.CLASS));
    }

    return user;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public UserDAO create(Map<String, Attribute> attributeMap, String type)
  {
    return new UserDAO(attributeMap, type);
  }

  public static UserDAO newInstance()
  {
    return (UserDAO) BusinessDAO.newInstance(UserInfo.CLASS);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static UserDAOIF get(String oid)
  {
    return (UserDAOIF) BusinessDAO.get(oid);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.rbac.UserIF#getInactive()
   */
  public boolean getInactive()
  {
    return Boolean.parseBoolean(getValue(UserInfo.INACTIVE));
  }

  /**
   * Set the inactive flag of the user
   *
   * @param inactive
   */
  public void setInactive(boolean inactive)
  {
    setValue(UserInfo.INACTIVE, Boolean.toString(inactive));
  }

  /**
   * Returns the user's username.
   *
   * @return The username
   */
  public String getUsername()
  {
    return getValue(UserInfo.USERNAME);
  }

  /**
   * Creates a key: {@link UserInfo#TYPENAME}.[userName].
   * @param userName
   * @return
   */
  public static String buildKey(String userName)
  {
    return ActorDAO.buildKey(UserInfo.TYPENAME, userName);
  }
}
