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

@com.runwaysdk.business.ClassSignature(hash = 1049319841)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdStruct.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdStructBase extends com.runwaysdk.system.metadata.MdEntity
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdStruct";
  public final static java.lang.String CACHEALGORITHM = "cacheAlgorithm";
  private static final long serialVersionUID = 1049319841;
  
  public MdStructBase()
  {
    super();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.StructCache> getCacheAlgorithm()
  {
    return (java.util.List<com.runwaysdk.system.metadata.StructCache>) getEnumValues(CACHEALGORITHM);
  }
  
  public void addCacheAlgorithm(com.runwaysdk.system.metadata.StructCache value)
  {
    if(value != null)
    {
      addEnumItem(CACHEALGORITHM, value.getOid());
    }
  }
  
  public void removeCacheAlgorithm(com.runwaysdk.system.metadata.StructCache value)
  {
    if(value != null)
    {
      removeEnumItem(CACHEALGORITHM, value.getOid());
    }
  }
  
  public void clearCacheAlgorithm()
  {
    clearEnum(CACHEALGORITHM);
  }
  
  public void validateCacheAlgorithm()
  {
    this.validateAttribute(CACHEALGORITHM);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getCacheAlgorithmMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdStruct.CLASS);
    return mdClassIF.definesAttribute(CACHEALGORITHM);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MdStruct get(String oid)
  {
    return (MdStruct) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdStruct getByKey(String key)
  {
    return (MdStruct) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdStruct lock(java.lang.String oid)
  {
    MdStruct _instance = MdStruct.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdStruct unlock(java.lang.String oid)
  {
    MdStruct _instance = MdStruct.get(oid);
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
