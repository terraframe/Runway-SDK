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

@com.runwaysdk.business.ClassSignature(hash = -2140983439)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdAttributeDec.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdAttributeDecBase extends com.runwaysdk.system.metadata.MdAttributeNumber
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeDec";
  public final static java.lang.String DATABASEDECIMAL = "databaseDecimal";
  public final static java.lang.String DATABASELENGTH = "databaseLength";
  private static final long serialVersionUID = -2140983439;
  
  public MdAttributeDecBase()
  {
    super();
  }
  
  public Integer getDatabaseDecimal()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(DATABASEDECIMAL));
  }
  
  public void validateDatabaseDecimal()
  {
    this.validateAttribute(DATABASEDECIMAL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getDatabaseDecimalMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdAttributeDec.CLASS);
    return mdClassIF.definesAttribute(DATABASEDECIMAL);
  }
  
  public void setDatabaseDecimal(Integer value)
  {
    if(value == null)
    {
      setValue(DATABASEDECIMAL, "");
    }
    else
    {
      setValue(DATABASEDECIMAL, java.lang.Integer.toString(value));
    }
  }
  
  public Integer getDatabaseLength()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(DATABASELENGTH));
  }
  
  public void validateDatabaseLength()
  {
    this.validateAttribute(DATABASELENGTH);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getDatabaseLengthMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdAttributeDec.CLASS);
    return mdClassIF.definesAttribute(DATABASELENGTH);
  }
  
  public void setDatabaseLength(Integer value)
  {
    if(value == null)
    {
      setValue(DATABASELENGTH, "");
    }
    else
    {
      setValue(DATABASELENGTH, java.lang.Integer.toString(value));
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MdAttributeDec get(String oid)
  {
    return (MdAttributeDec) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdAttributeDec getByKey(String key)
  {
    return (MdAttributeDec) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdAttributeDec lock(java.lang.String oid)
  {
    MdAttributeDec _instance = MdAttributeDec.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdAttributeDec unlock(java.lang.String oid)
  {
    MdAttributeDec _instance = MdAttributeDec.get(oid);
    _instance.unlock();
    
    return _instance;
  }
  
  public String toString()
  {
    if (this.isNew())
    {
      return "New: "+ this.getClassDisplayLabel();
    }
    else
    {
      return super.toString();
    }
  }
}
