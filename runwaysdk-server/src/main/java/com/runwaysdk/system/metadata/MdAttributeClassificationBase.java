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

@com.runwaysdk.business.ClassSignature(hash = -820646828)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdAttributeClassification.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdAttributeClassificationBase extends com.runwaysdk.system.metadata.MdAttributeConcrete
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeClassification";
  public static java.lang.String REFERENCEMDCLASSIFICATION = "referenceMdClassification";
  public static java.lang.String ROOT = "root";
  private static final long serialVersionUID = -820646828;
  
  public MdAttributeClassificationBase()
  {
    super();
  }
  
  public com.runwaysdk.system.metadata.MdClassification getReferenceMdClassification()
  {
    if (getValue(REFERENCEMDCLASSIFICATION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdClassification.get(getValue(REFERENCEMDCLASSIFICATION));
    }
  }
  
  public String getReferenceMdClassificationOid()
  {
    return getValue(REFERENCEMDCLASSIFICATION);
  }
  
  public void validateReferenceMdClassification()
  {
    this.validateAttribute(REFERENCEMDCLASSIFICATION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getReferenceMdClassificationMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdAttributeClassification.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(REFERENCEMDCLASSIFICATION);
  }
  
  public void setReferenceMdClassification(com.runwaysdk.system.metadata.MdClassification value)
  {
    if(value == null)
    {
      setValue(REFERENCEMDCLASSIFICATION, "");
    }
    else
    {
      setValue(REFERENCEMDCLASSIFICATION, value.getOid());
    }
  }
  
  public void setReferenceMdClassificationId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(REFERENCEMDCLASSIFICATION, "");
    }
    else
    {
      setValue(REFERENCEMDCLASSIFICATION, oid);
    }
  }
  
  public com.runwaysdk.system.AbstractClassification getRoot()
  {
    return (com.runwaysdk.system.AbstractClassification)com.runwaysdk.business.graph.VertexObject.get("com.runwaysdk.system.AbstractClassification", getValue(ROOT));
  }
  
  public void validateRoot()
  {
    this.validateAttribute(ROOT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF getRootMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdAttributeClassification.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF)mdClassIF.definesAttribute(ROOT);
  }
  
  public void setRoot(com.runwaysdk.system.AbstractClassification value)
  {
    if(value == null)
    {
      setValue(ROOT, "");
    }
    else
    {
      setValue(ROOT, value.getOid());
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MdAttributeClassification get(String oid)
  {
    return (MdAttributeClassification) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdAttributeClassification getByKey(String key)
  {
    return (MdAttributeClassification) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdAttributeClassification lock(java.lang.String oid)
  {
    MdAttributeClassification _instance = MdAttributeClassification.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdAttributeClassification unlock(java.lang.String oid)
  {
    MdAttributeClassification _instance = MdAttributeClassification.get(oid);
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