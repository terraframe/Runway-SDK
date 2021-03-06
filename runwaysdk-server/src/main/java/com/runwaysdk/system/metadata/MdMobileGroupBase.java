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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 543301056)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdMobileGroup.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdMobileGroupBase extends com.runwaysdk.system.metadata.MdMobileField
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdMobileGroup";
  private static final long serialVersionUID = 543301056;
  
  public MdMobileGroupBase()
  {
    super();
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.system.metadata.MobileGroupField addMobileGroups(com.runwaysdk.system.metadata.MdMobileField mdMobileField)
  {
    return (com.runwaysdk.system.metadata.MobileGroupField) addChild(mdMobileField, com.runwaysdk.system.metadata.MobileGroupField.CLASS);
  }
  
  public void removeMobileGroups(com.runwaysdk.system.metadata.MdMobileField mdMobileField)
  {
    removeAllChildren(mdMobileField, com.runwaysdk.system.metadata.MobileGroupField.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdMobileField> getAllMobileGroups()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdMobileField>) getChildren(com.runwaysdk.system.metadata.MobileGroupField.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MobileGroupField> getAllMobileGroupsRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MobileGroupField>) getChildRelationships(com.runwaysdk.system.metadata.MobileGroupField.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.metadata.MobileGroupField getMobileGroupsRel(com.runwaysdk.system.metadata.MdMobileField mdMobileField)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MobileGroupField> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MobileGroupField>) getRelationshipsWithChild(mdMobileField, com.runwaysdk.system.metadata.MobileGroupField.CLASS);
    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }
      else
      {
        return null;
      }
    }
    finally
    {
      iterator.close();
    }
  }
  
  public static MdMobileGroup get(String oid)
  {
    return (MdMobileGroup) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdMobileGroup getByKey(String key)
  {
    return (MdMobileGroup) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdMobileGroup lock(java.lang.String oid)
  {
    MdMobileGroup _instance = MdMobileGroup.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdMobileGroup unlock(java.lang.String oid)
  {
    MdMobileGroup _instance = MdMobileGroup.get(oid);
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
