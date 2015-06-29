/**
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
 */
package com.runwaysdk.business.email;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.runwaysdk.constants.EmailKeyInfo;
import com.runwaysdk.constants.MdAttributeDateTimeUtil;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.StructDAOQuery;

/**
 * Provides business-level access to the EmailKey object.
 */
public class EmailKey
{
  /**
   * The StructDAO that this EmailKey delegates to.
   */
  private StructDAO structDAO;
  
  /**
   * Creates a new email key.
   */
  private EmailKey()
  {
    this.structDAO = StructDAO.newInstance(EmailKeyInfo.CLASS);
  }
  
  /**
   * Creates a new email key that delegates to the given StructDAO.
   * 
   * @param structDAO
   */
  private EmailKey(StructDAO structDAO)
  {
    this.structDAO = structDAO;
  }
  
  /**
   * Creates and returns a new EmailKey for the given user. If
   * the user already has an EmailKey entry, this
   * method returns the existing EmailKey.
   * 
   * 
   * @param email
   * @return
   */
  public synchronized static EmailKey createEmailKey(String email)
  {
    EmailKey emailKey = getEmailKey(email);
    if(emailKey == null)
    {
      emailKey = new EmailKey();
      emailKey.setIssuedOnDate(new Date());
      emailKey.setUniqueKey();
      emailKey.setEmail(email);
      emailKey.apply();
    }
    
    return emailKey;
  }
  
  /**
   * Returns the EmailKey that maps to the given unique key.
   * 
   * @param uniqueKey
   * @return The EmailKey or null if no match is found.
   */
  public synchronized static EmailKey getByUniqueKey(String uniqueKey)
  {
    QueryFactory f = new QueryFactory();
    StructDAOQuery query = f.structDAOQuery(EmailKeyInfo.CLASS);
    query.WHERE(query.aCharacter(EmailKeyInfo.UNIQUE_KEY).EQ(uniqueKey));
    
    OIterator<StructDAOIF> iter = query.getIterator();
	    
    try
    {
      if(iter.hasNext())
      {
        StructDAOIF structDAOIF = iter.next();
        return new EmailKey(structDAOIF.getStructDAO());
      }
      else
      {
        return null;
      }
    }
    finally
    {
      iter.close();
    }
  }
  
  /**
   * Returns the EmailKey mapped to the given user. If an EmailKey
   * does not exist for the given user, null is returned.
   * 
   * @param uniqueKey
   * @return
   */
  public synchronized static EmailKey getEmailKey(String email)
  {
    QueryFactory f = new QueryFactory();
    StructDAOQuery query = f.structDAOQuery(EmailKeyInfo.CLASS);
    query.WHERE(query.aCharacter(EmailKeyInfo.EMAIL).EQ(email));
    
    OIterator<StructDAOIF> iter = query.getIterator();
    
    try
    {
      if(iter.hasNext())
      {
        StructDAOIF structDAOIF = iter.next();
        return new EmailKey(structDAOIF.getStructDAO());
      }
      else
      {
        return null;
      }
    }
    finally
    {
      iter.close();
    }
  }
  
  /**
   * Returns the email for this EmailKey.
   * 
   * @return The email value.
   */
  public String getEmail()
  {
    return structDAO.getValue(EmailKeyInfo.EMAIL);
  }
  
  /**
   * Returns the unique key for this EmailKey.
   * 
   * @return The unique key.
   */
  public String getUniqueKey()
  {
    return structDAO.getValue(EmailKeyInfo.UNIQUE_KEY);
  }
  
  /**
   * Returns the issued on date value.
   * 
   * @return A Date object that represents when the EmailKey was issued.
   */
  public Date getIssuedOnDate()
  {
    String dateTime = structDAO.getValue(EmailKeyInfo.ISSUED_ON_DATE);
    return MdAttributeDateTimeUtil.getTypeSafeValue(dateTime);
  }
  
  /**
   * Checks if this EmailKey is valid by comparing the value
   * of the issue date with the current date. If the difference
   * between those two dates is greater than the number of days
   * specified by the value of keyExpire in email.properties then
   * the EmailKey is invalid. If this EmailKey is invalid, it is up
   * to the developer to delete it and re-issue a new one.
   * 
   * @return
   */
  public boolean isDateValid()
  {
    Date issued = getIssuedOnDate(); 
    int expireDays = EmailProperties.getKeyExpire();
    Date expireDate = DateUtils.addDays(issued, expireDays);
    
    Date now = new Date();
    return now.before(expireDate);
  }
  
  /**
   * Checks if this EmailKey is valid by comparing the value
   * of the issue date with the given date. If the difference
   * between those two dates is greater than the number of days
   * specified by the value of keyExpire in email.properties then
   * the EmailKey is invalid. If this EmailKey is invalid, it is up
   * to the developer to delete it and re-issue a new one.
   * 
   * @return
   */  
  public boolean isDateValid(Date date)
  {
    Date issued = getIssuedOnDate(); 
    int expireDays = EmailProperties.getKeyExpire();
    Date expireDate = DateUtils.addDays(issued, expireDays);
    
    return date.before(expireDate);
  }
  
  /**
   * Reissues the EmailKey for the given date. The EmailKey
   * is then applied.
   * 
   * @param date
   */
  public void reissue(Date date)
  {
    setIssuedOnDate(date);
    apply();
  }
  
  /**
   * Reissues the EmailKey for the current date. The EmailKey
   * is then applied.
   */
  public void reissue()
  {
    setIssuedOnDate(new Date());
    apply();
  }
  
  /**
   * Checks if the given unique key is valid by comparing it
   * to the value of the unique key on this EmailKey.
   * 
   * @param uniqueKey
   * @return
   */
  public boolean isKeyValid(String uniqueKey)
  {
    return getUniqueKey().equals(uniqueKey);
  }
  
  /**
   * Internal method that sets the issued on date value.
   * 
   * @param date
   */
  private void setIssuedOnDate(Date date)
  {
    String dateTime = MdAttributeDateTimeUtil.getTypeUnsafeValue(date);
    structDAO.setValue(EmailKeyInfo.ISSUED_ON_DATE, dateTime);
  }
  
  /**
   * Sets the value for the unique key using a unique key generator.
   */
  private void setUniqueKey()
  {
    String uniqueKey = ServerIDGenerator.nextID();
    structDAO.setValue(EmailKeyInfo.UNIQUE_KEY, uniqueKey);
  }
  
  /**
   * Sets the value of the email for the EmailKey.
   * 
   * @param email
   */
  private void setEmail(String email)
  {
    structDAO.setValue(EmailKeyInfo.EMAIL, email);
  }
  
  /**
   * Applies the EmailKey
   */
  private void apply()
  {
    structDAO.apply();
  }
  
  /**
   * Deletes this EmailKey.
   */
  public void delete()
  {
    structDAO.delete();
  }
  
}
