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

@com.runwaysdk.business.ClassSignature(hash = 1857361893)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdAttributeTime.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdAttributeTimeBase extends com.runwaysdk.system.metadata.MdAttributeMoment
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeTime";
  public final static java.lang.String DEFAULTVALUE = "defaultValue";
  private static final long serialVersionUID = 1857361893;
  
  public MdAttributeTimeBase()
  {
    super();
  }
  
  public java.util.Date getDefaultValue()
  {
    return com.runwaysdk.constants.MdAttributeTimeUtil.getTypeSafeValue(getValue(DEFAULTVALUE));
  }
  
  public void validateDefaultValue()
  {
    this.validateAttribute(DEFAULTVALUE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getDefaultValueMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdAttributeTime.CLASS);
    return mdClassIF.definesAttribute(DEFAULTVALUE);
  }
  
  public void setDefaultValue(java.util.Date value)
  {
    if(value == null)
    {
      setValue(DEFAULTVALUE, "");
    }
    else
    {
      setValue(DEFAULTVALUE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.TIME_FORMAT).format(value));
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MdAttributeTime get(String oid)
  {
    return (MdAttributeTime) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdAttributeTime getByKey(String key)
  {
    return (MdAttributeTime) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdAttributeTime lock(java.lang.String oid)
  {
    MdAttributeTime _instance = MdAttributeTime.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdAttributeTime unlock(java.lang.String oid)
  {
    MdAttributeTime _instance = MdAttributeTime.get(oid);
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
