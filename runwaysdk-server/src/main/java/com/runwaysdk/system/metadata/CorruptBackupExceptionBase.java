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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -805731982)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to CorruptBackupException.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class CorruptBackupExceptionBase extends com.runwaysdk.business.SmartException
{
  public final static String CLASS = "com.runwaysdk.system.metadata.CorruptBackupException";
  public final static java.lang.String BACKUPNAME = "backupName";
  public final static java.lang.String OID = "oid";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -805731982;
  
  public CorruptBackupExceptionBase()
  {
    super();
  }
  
  public CorruptBackupExceptionBase(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public CorruptBackupExceptionBase(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public CorruptBackupExceptionBase(java.lang.Throwable cause)
  {
    super(cause);
  }
  
  public String getBackupName()
  {
    return getValue(BACKUPNAME);
  }
  
  public void validateBackupName()
  {
    this.validateAttribute(BACKUPNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getBackupNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.CorruptBackupException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(BACKUPNAME);
  }
  
  public void setBackupName(String value)
  {
    if(value == null)
    {
      setValue(BACKUPNAME, "");
    }
    else
    {
      setValue(BACKUPNAME, value);
    }
  }
  
  public String getOid()
  {
    return getValue(OID);
  }
  
  public void validateOid()
  {
    this.validateAttribute(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.CorruptBackupException.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public java.lang.String localize(java.util.Locale locale)
  {
    java.lang.String message = super.localize(locale);
    message = replace(message, "{backupName}", this.getBackupName());
    message = replace(message, "{oid}", this.getOid());
    return message;
  }
  
}
