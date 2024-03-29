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

@com.runwaysdk.business.ClassSignature(hash = -1151834134)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdTransient.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdTransientBase extends com.runwaysdk.system.metadata.MdClass
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdTransient";
  public final static java.lang.String EXTENDABLE = "extendable";
  public final static java.lang.String ISABSTRACT = "isAbstract";
  private static final long serialVersionUID = -1151834134;
  
  public MdTransientBase()
  {
    super();
  }
  
  public Boolean getExtendable()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(EXTENDABLE));
  }
  
  public void validateExtendable()
  {
    this.validateAttribute(EXTENDABLE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getExtendableMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdTransient.CLASS);
    return mdClassIF.definesAttribute(EXTENDABLE);
  }
  
  public void setExtendable(Boolean value)
  {
    if(value == null)
    {
      setValue(EXTENDABLE, "");
    }
    else
    {
      setValue(EXTENDABLE, java.lang.Boolean.toString(value));
    }
  }
  
  public Boolean getIsAbstract()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISABSTRACT));
  }
  
  public void validateIsAbstract()
  {
    this.validateAttribute(ISABSTRACT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getIsAbstractMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdTransient.CLASS);
    return mdClassIF.definesAttribute(ISABSTRACT);
  }
  
  public void setIsAbstract(Boolean value)
  {
    if(value == null)
    {
      setValue(ISABSTRACT, "");
    }
    else
    {
      setValue(ISABSTRACT, java.lang.Boolean.toString(value));
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MdTransient get(String oid)
  {
    return (MdTransient) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdTransient getByKey(String key)
  {
    return (MdTransient) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdTransient lock(java.lang.String oid)
  {
    MdTransient _instance = MdTransient.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdTransient unlock(java.lang.String oid)
  {
    MdTransient _instance = MdTransient.get(oid);
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
