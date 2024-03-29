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

@com.runwaysdk.business.ClassSignature(hash = -1227958354)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdAttributeStruct.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdAttributeStructBase extends com.runwaysdk.system.metadata.MdAttributeConcrete
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeStruct";
  public final static java.lang.String MDSTRUCT = "mdStruct";
  private static final long serialVersionUID = -1227958354;
  
  public MdAttributeStructBase()
  {
    super();
  }
  
  public com.runwaysdk.system.metadata.MdStruct getMdStruct()
  {
    if (getValue(MDSTRUCT).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdStruct.get(getValue(MDSTRUCT));
    }
  }
  
  public String getMdStructId()
  {
    return getValue(MDSTRUCT);
  }
  
  public void validateMdStruct()
  {
    this.validateAttribute(MDSTRUCT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getMdStructMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdAttributeStruct.CLASS);
    return mdClassIF.definesAttribute(MDSTRUCT);
  }
  
  public void setMdStruct(com.runwaysdk.system.metadata.MdStruct value)
  {
    if(value == null)
    {
      setValue(MDSTRUCT, "");
    }
    else
    {
      setValue(MDSTRUCT, value.getOid());
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MdAttributeStruct get(String oid)
  {
    return (MdAttributeStruct) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdAttributeStruct getByKey(String key)
  {
    return (MdAttributeStruct) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdAttributeStruct lock(java.lang.String oid)
  {
    MdAttributeStruct _instance = MdAttributeStruct.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdAttributeStruct unlock(java.lang.String oid)
  {
    MdAttributeStruct _instance = MdAttributeStruct.get(oid);
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
