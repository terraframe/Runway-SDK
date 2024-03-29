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

@com.runwaysdk.business.ClassSignature(hash = -111523699)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdClass.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdClassBase extends com.runwaysdk.system.metadata.MdType
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdClass";
  public final static java.lang.String PUBLISH = "publish";
  public final static java.lang.String STUBCLASS = "stubClass";
  public final static java.lang.String STUBDTOCLASS = "stubDTOclass";
  public final static java.lang.String STUBDTOSOURCE = "stubDTOsource";
  public final static java.lang.String STUBSOURCE = "stubSource";
  private static final long serialVersionUID = -111523699;
  
  public MdClassBase()
  {
    super();
  }
  
  public Boolean getPublish()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(PUBLISH));
  }
  
  public void validatePublish()
  {
    this.validateAttribute(PUBLISH);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getPublishMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdClass.CLASS);
    return mdClassIF.definesAttribute(PUBLISH);
  }
  
  public void setPublish(Boolean value)
  {
    if(value == null)
    {
      setValue(PUBLISH, "");
    }
    else
    {
      setValue(PUBLISH, java.lang.Boolean.toString(value));
    }
  }
  
  public byte[] getStubClass()
  {
    return getBlob(STUBCLASS);
  }
  
  public void validateStubClass()
  {
    this.validateAttribute(STUBCLASS);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getStubClassMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdClass.CLASS);
    return mdClassIF.definesAttribute(STUBCLASS);
  }
  
  public byte[] getStubDTOclass()
  {
    return getBlob(STUBDTOCLASS);
  }
  
  public void validateStubDTOclass()
  {
    this.validateAttribute(STUBDTOCLASS);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getStubDTOclassMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdClass.CLASS);
    return mdClassIF.definesAttribute(STUBDTOCLASS);
  }
  
  public String getStubDTOsource()
  {
    return getValue(STUBDTOSOURCE);
  }
  
  public void validateStubDTOsource()
  {
    this.validateAttribute(STUBDTOSOURCE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getStubDTOsourceMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdClass.CLASS);
    return mdClassIF.definesAttribute(STUBDTOSOURCE);
  }
  
  public void setStubDTOsource(String value)
  {
    if(value == null)
    {
      setValue(STUBDTOSOURCE, "");
    }
    else
    {
      setValue(STUBDTOSOURCE, value);
    }
  }
  
  public String getStubSource()
  {
    return getValue(STUBSOURCE);
  }
  
  public void validateStubSource()
  {
    this.validateAttribute(STUBSOURCE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getStubSourceMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdClass.CLASS);
    return mdClassIF.definesAttribute(STUBSOURCE);
  }
  
  public void setStubSource(String value)
  {
    if(value == null)
    {
      setValue(STUBSOURCE, "");
    }
    else
    {
      setValue(STUBSOURCE, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.system.metadata.ClassAttribute addAttribute(com.runwaysdk.system.metadata.MdAttribute mdAttribute)
  {
    return (com.runwaysdk.system.metadata.ClassAttribute) addChild(mdAttribute, com.runwaysdk.system.metadata.ClassAttribute.CLASS);
  }
  
  public void removeAttribute(com.runwaysdk.system.metadata.MdAttribute mdAttribute)
  {
    removeAllChildren(mdAttribute, com.runwaysdk.system.metadata.ClassAttribute.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdAttribute> getAllAttribute()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdAttribute>) getChildren(com.runwaysdk.system.metadata.ClassAttribute.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassAttribute> getAllAttributeRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassAttribute>) getChildRelationships(com.runwaysdk.system.metadata.ClassAttribute.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.metadata.ClassAttribute getAttributeRel(com.runwaysdk.system.metadata.MdAttribute mdAttribute)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassAttribute> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassAttribute>) getRelationshipsWithChild(mdAttribute, com.runwaysdk.system.metadata.ClassAttribute.CLASS);
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
  
  public com.runwaysdk.system.metadata.ClassAttributeConcrete addConcreteAttribute(com.runwaysdk.system.metadata.MdAttributeConcrete mdAttributeConcrete)
  {
    return (com.runwaysdk.system.metadata.ClassAttributeConcrete) addChild(mdAttributeConcrete, com.runwaysdk.system.metadata.ClassAttributeConcrete.CLASS);
  }
  
  public void removeConcreteAttribute(com.runwaysdk.system.metadata.MdAttributeConcrete mdAttributeConcrete)
  {
    removeAllChildren(mdAttributeConcrete, com.runwaysdk.system.metadata.ClassAttributeConcrete.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdAttributeConcrete> getAllConcreteAttribute()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdAttributeConcrete>) getChildren(com.runwaysdk.system.metadata.ClassAttributeConcrete.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassAttributeConcrete> getAllConcreteAttributeRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassAttributeConcrete>) getChildRelationships(com.runwaysdk.system.metadata.ClassAttributeConcrete.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.metadata.ClassAttributeConcrete getConcreteAttributeRel(com.runwaysdk.system.metadata.MdAttributeConcrete mdAttributeConcrete)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassAttributeConcrete> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassAttributeConcrete>) getRelationshipsWithChild(mdAttributeConcrete, com.runwaysdk.system.metadata.ClassAttributeConcrete.CLASS);
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
  
  public com.runwaysdk.system.metadata.ClassHasDimension addMdClassDimensions(com.runwaysdk.system.metadata.MdClassDimension mdClassDimension)
  {
    return (com.runwaysdk.system.metadata.ClassHasDimension) addChild(mdClassDimension, com.runwaysdk.system.metadata.ClassHasDimension.CLASS);
  }
  
  public void removeMdClassDimensions(com.runwaysdk.system.metadata.MdClassDimension mdClassDimension)
  {
    removeAllChildren(mdClassDimension, com.runwaysdk.system.metadata.ClassHasDimension.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdClassDimension> getAllMdClassDimensions()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdClassDimension>) getChildren(com.runwaysdk.system.metadata.ClassHasDimension.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassHasDimension> getAllMdClassDimensionsRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassHasDimension>) getChildRelationships(com.runwaysdk.system.metadata.ClassHasDimension.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.metadata.ClassHasDimension getMdClassDimensionsRel(com.runwaysdk.system.metadata.MdClassDimension mdClassDimension)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassHasDimension> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassHasDimension>) getRelationshipsWithChild(mdClassDimension, com.runwaysdk.system.metadata.ClassHasDimension.CLASS);
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
  
  public com.runwaysdk.system.metadata.ClassInheritance addSubEntity(com.runwaysdk.system.metadata.MdClass mdClass)
  {
    return (com.runwaysdk.system.metadata.ClassInheritance) addChild(mdClass, com.runwaysdk.system.metadata.ClassInheritance.CLASS);
  }
  
  public void removeSubEntity(com.runwaysdk.system.metadata.MdClass mdClass)
  {
    removeAllChildren(mdClass, com.runwaysdk.system.metadata.ClassInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdClass> getAllSubEntity()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdClass>) getChildren(com.runwaysdk.system.metadata.ClassInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassInheritance> getAllSubEntityRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassInheritance>) getChildRelationships(com.runwaysdk.system.metadata.ClassInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.metadata.ClassInheritance getSubEntityRel(com.runwaysdk.system.metadata.MdClass mdClass)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassInheritance> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassInheritance>) getRelationshipsWithChild(mdClass, com.runwaysdk.system.metadata.ClassInheritance.CLASS);
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
  
  public com.runwaysdk.system.metadata.ClassInheritance addInheritsFromEntity(com.runwaysdk.system.metadata.MdClass mdClass)
  {
    return (com.runwaysdk.system.metadata.ClassInheritance) addParent(mdClass, com.runwaysdk.system.metadata.ClassInheritance.CLASS);
  }
  
  public void removeInheritsFromEntity(com.runwaysdk.system.metadata.MdClass mdClass)
  {
    removeAllParents(mdClass, com.runwaysdk.system.metadata.ClassInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdClass> getAllInheritsFromEntity()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdClass>) getParents(com.runwaysdk.system.metadata.ClassInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassInheritance> getAllInheritsFromEntityRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassInheritance>) getParentRelationships(com.runwaysdk.system.metadata.ClassInheritance.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.metadata.ClassInheritance getInheritsFromEntityRel(com.runwaysdk.system.metadata.MdClass mdClass)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassInheritance> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.ClassInheritance>) getRelationshipsWithParent(mdClass, com.runwaysdk.system.metadata.ClassInheritance.CLASS);
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
  
  public static MdClass get(String oid)
  {
    return (MdClass) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdClass getByKey(String key)
  {
    return (MdClass) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdClass lock(java.lang.String oid)
  {
    MdClass _instance = MdClass.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdClass unlock(java.lang.String oid)
  {
    MdClass _instance = MdClass.get(oid);
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
