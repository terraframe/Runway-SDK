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

@com.runwaysdk.business.ClassSignature(hash = -2019077461)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to EnumerationAttributeItem.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class EnumerationAttributeItemBase extends com.runwaysdk.system.metadata.MetadataRelationship
{
  public final static String CLASS = "com.runwaysdk.system.metadata.EnumerationAttributeItem";
  private static final long serialVersionUID = -2019077461;
  
  public EnumerationAttributeItemBase(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.system.metadata.MdEnumeration getParent()
  {
    return (com.runwaysdk.system.metadata.MdEnumeration) super.getParent();
  }
  
  public com.runwaysdk.system.EnumerationMaster getChild()
  {
    return (com.runwaysdk.system.EnumerationMaster) super.getChild();
  }
  
  public static EnumerationAttributeItem get(String oid)
  {
    return (EnumerationAttributeItem) com.runwaysdk.business.Relationship.get(oid);
  }
  
  public static EnumerationAttributeItem getByKey(String key)
  {
    return (EnumerationAttributeItem) com.runwaysdk.business.Relationship.get(CLASS, key);
  }
  
  public static EnumerationAttributeItem lock(java.lang.String oid)
  {
    EnumerationAttributeItem _instance = EnumerationAttributeItem.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static EnumerationAttributeItem unlock(java.lang.String oid)
  {
    EnumerationAttributeItem _instance = EnumerationAttributeItem.get(oid);
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
