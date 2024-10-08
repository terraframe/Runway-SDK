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

@com.runwaysdk.business.ClassSignature(hash = 924904173)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdUtil.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdUtilBase extends com.runwaysdk.system.metadata.MdSession
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdUtil";
  public final static java.lang.String SUPERMDUTIL = "superMdUtil";
  private static final long serialVersionUID = 924904173;
  
  public MdUtilBase()
  {
    super();
  }
  
  public com.runwaysdk.system.metadata.MdUtil getSuperMdUtil()
  {
    if (getValue(SUPERMDUTIL).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdUtil.get(getValue(SUPERMDUTIL));
    }
  }
  
  public String getSuperMdUtilId()
  {
    return getValue(SUPERMDUTIL);
  }
  
  public void validateSuperMdUtil()
  {
    this.validateAttribute(SUPERMDUTIL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getSuperMdUtilMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdUtil.CLASS);
    return mdClassIF.definesAttribute(SUPERMDUTIL);
  }
  
  public void setSuperMdUtil(com.runwaysdk.system.metadata.MdUtil value)
  {
    if(value == null)
    {
      setValue(SUPERMDUTIL, "");
    }
    else
    {
      setValue(SUPERMDUTIL, value.getOid());
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.system.metadata.UtilInheritance addChildClasses(com.runwaysdk.system.metadata.MdUtil mdUtil)
  {
    return (com.runwaysdk.system.metadata.UtilInheritance) addChild(mdUtil, com.runwaysdk.system.metadata.UtilInheritance.CLASS);
  }
  
  public void removeChildClasses(com.runwaysdk.system.metadata.MdUtil mdUtil)
  {
    removeAllChildren(mdUtil, com.runwaysdk.system.metadata.UtilInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdUtil> getAllChildClasses()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdUtil>) getChildren(com.runwaysdk.system.metadata.UtilInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.UtilInheritance> getAllChildClassesRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.UtilInheritance>) getChildRelationships(com.runwaysdk.system.metadata.UtilInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.metadata.UtilInheritance getChildClassesRel(com.runwaysdk.system.metadata.MdUtil mdUtil)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.UtilInheritance> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.UtilInheritance>) getRelationshipsWithChild(mdUtil, com.runwaysdk.system.metadata.UtilInheritance.CLASS);
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
  
  public com.runwaysdk.system.metadata.UtilInheritance addSuperClass(com.runwaysdk.system.metadata.MdUtil mdUtil)
  {
    return (com.runwaysdk.system.metadata.UtilInheritance) addParent(mdUtil, com.runwaysdk.system.metadata.UtilInheritance.CLASS);
  }
  
  public void removeSuperClass(com.runwaysdk.system.metadata.MdUtil mdUtil)
  {
    removeAllParents(mdUtil, com.runwaysdk.system.metadata.UtilInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdUtil> getAllSuperClass()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdUtil>) getParents(com.runwaysdk.system.metadata.UtilInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.UtilInheritance> getAllSuperClassRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.UtilInheritance>) getParentRelationships(com.runwaysdk.system.metadata.UtilInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.metadata.UtilInheritance getSuperClassRel(com.runwaysdk.system.metadata.MdUtil mdUtil)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.UtilInheritance> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.UtilInheritance>) getRelationshipsWithParent(mdUtil, com.runwaysdk.system.metadata.UtilInheritance.CLASS);
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
  
  public static MdUtil get(String oid)
  {
    return (MdUtil) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdUtil getByKey(String key)
  {
    return (MdUtil) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdUtil lock(java.lang.String oid)
  {
    MdUtil _instance = MdUtil.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdUtil unlock(java.lang.String oid)
  {
    MdUtil _instance = MdUtil.get(oid);
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
