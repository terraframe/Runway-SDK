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

@com.runwaysdk.business.ClassSignature(hash = -347211403)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdBusiness.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdBusinessBase extends com.runwaysdk.system.metadata.MdElement
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdBusiness";
  public final static java.lang.String CACHEALGORITHM = "cacheAlgorithm";
  public final static java.lang.String SUPERMDBUSINESS = "superMdBusiness";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -347211403;
  
  public MdBusinessBase()
  {
    super();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.ClassCache> getCacheAlgorithm()
  {
    return (java.util.List<com.runwaysdk.system.metadata.ClassCache>) getEnumValues(CACHEALGORITHM);
  }
  
  public void addCacheAlgorithm(com.runwaysdk.system.metadata.ClassCache value)
  {
    if(value != null)
    {
      addEnumItem(CACHEALGORITHM, value.getOid());
    }
  }
  
  public void removeCacheAlgorithm(com.runwaysdk.system.metadata.ClassCache value)
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
  
  public static com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF getCacheAlgorithmMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdBusiness.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdClassIF.definesAttribute(CACHEALGORITHM);
  }
  
  public com.runwaysdk.system.metadata.MdBusiness getSuperMdBusiness()
  {
    if (getValue(SUPERMDBUSINESS).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdBusiness.get(getValue(SUPERMDBUSINESS));
    }
  }
  
  public String getSuperMdBusinessOid()
  {
    return getValue(SUPERMDBUSINESS);
  }
  
  public void validateSuperMdBusiness()
  {
    this.validateAttribute(SUPERMDBUSINESS);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getSuperMdBusinessMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdBusiness.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(SUPERMDBUSINESS);
  }
  
  public void setSuperMdBusiness(com.runwaysdk.system.metadata.MdBusiness value)
  {
    if(value == null)
    {
      setValue(SUPERMDBUSINESS, "");
    }
    else
    {
      setValue(SUPERMDBUSINESS, value.getOid());
    }
  }
  
  public void setSuperMdBusinessId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(SUPERMDBUSINESS, "");
    }
    else
    {
      setValue(SUPERMDBUSINESS, oid);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.system.metadata.EnumerationAttribute addMdEnumeration(com.runwaysdk.system.metadata.MdEnumeration mdEnumeration)
  {
    return (com.runwaysdk.system.metadata.EnumerationAttribute) addChild(mdEnumeration, com.runwaysdk.system.metadata.EnumerationAttribute.CLASS);
  }
  
  public void removeMdEnumeration(com.runwaysdk.system.metadata.MdEnumeration mdEnumeration)
  {
    removeAllChildren(mdEnumeration, com.runwaysdk.system.metadata.EnumerationAttribute.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdEnumeration> getAllMdEnumeration()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdEnumeration>) getChildren(com.runwaysdk.system.metadata.EnumerationAttribute.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.EnumerationAttribute> getAllMdEnumerationRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.EnumerationAttribute>) getChildRelationships(com.runwaysdk.system.metadata.EnumerationAttribute.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.metadata.EnumerationAttribute getMdEnumerationRel(com.runwaysdk.system.metadata.MdEnumeration mdEnumeration)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.EnumerationAttribute> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.EnumerationAttribute>) getRelationshipsWithChild(mdEnumeration, com.runwaysdk.system.metadata.EnumerationAttribute.CLASS);
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
  
  public com.runwaysdk.system.metadata.BusinessInheritance addSubClass(com.runwaysdk.system.metadata.MdBusiness mdBusiness)
  {
    return (com.runwaysdk.system.metadata.BusinessInheritance) addChild(mdBusiness, com.runwaysdk.system.metadata.BusinessInheritance.CLASS);
  }
  
  public void removeSubClass(com.runwaysdk.system.metadata.MdBusiness mdBusiness)
  {
    removeAllChildren(mdBusiness, com.runwaysdk.system.metadata.BusinessInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdBusiness> getAllSubClass()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdBusiness>) getChildren(com.runwaysdk.system.metadata.BusinessInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.BusinessInheritance> getAllSubClassRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.BusinessInheritance>) getChildRelationships(com.runwaysdk.system.metadata.BusinessInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.metadata.BusinessInheritance getSubClassRel(com.runwaysdk.system.metadata.MdBusiness mdBusiness)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.BusinessInheritance> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.BusinessInheritance>) getRelationshipsWithChild(mdBusiness, com.runwaysdk.system.metadata.BusinessInheritance.CLASS);
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
  
  public com.runwaysdk.system.metadata.BusinessInheritance addInheritsFromClass(com.runwaysdk.system.metadata.MdBusiness mdBusiness)
  {
    return (com.runwaysdk.system.metadata.BusinessInheritance) addParent(mdBusiness, com.runwaysdk.system.metadata.BusinessInheritance.CLASS);
  }
  
  public void removeInheritsFromClass(com.runwaysdk.system.metadata.MdBusiness mdBusiness)
  {
    removeAllParents(mdBusiness, com.runwaysdk.system.metadata.BusinessInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdBusiness> getAllInheritsFromClass()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdBusiness>) getParents(com.runwaysdk.system.metadata.BusinessInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.BusinessInheritance> getAllInheritsFromClassRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.BusinessInheritance>) getParentRelationships(com.runwaysdk.system.metadata.BusinessInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.metadata.BusinessInheritance getInheritsFromClassRel(com.runwaysdk.system.metadata.MdBusiness mdBusiness)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.BusinessInheritance> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.BusinessInheritance>) getRelationshipsWithParent(mdBusiness, com.runwaysdk.system.metadata.BusinessInheritance.CLASS);
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
  
  public static MdBusiness get(String oid)
  {
    return (MdBusiness) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdBusiness getByKey(String key)
  {
    return (MdBusiness) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdBusiness lock(java.lang.String oid)
  {
    MdBusiness _instance = MdBusiness.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdBusiness unlock(java.lang.String oid)
  {
    MdBusiness _instance = MdBusiness.get(oid);
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
