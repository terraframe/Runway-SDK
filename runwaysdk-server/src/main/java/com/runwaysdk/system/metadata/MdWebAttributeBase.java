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

@com.runwaysdk.business.ClassSignature(hash = 1431107703)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdWebAttribute.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdWebAttributeBase extends com.runwaysdk.system.metadata.MdWebField
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdWebAttribute";
  public final static java.lang.String DEFININGMDATTRIBUTE = "definingMdAttribute";
  public final static java.lang.String SHOWONSEARCH = "showOnSearch";
  public final static java.lang.String SHOWONVIEWALL = "showOnViewAll";
  private static final long serialVersionUID = 1431107703;
  
  public MdWebAttributeBase()
  {
    super();
  }
  
  public com.runwaysdk.system.metadata.MdAttribute getDefiningMdAttribute()
  {
    if (getValue(DEFININGMDATTRIBUTE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttribute.get(getValue(DEFININGMDATTRIBUTE));
    }
  }
  
  public String getDefiningMdAttributeId()
  {
    return getValue(DEFININGMDATTRIBUTE);
  }
  
  public void validateDefiningMdAttribute()
  {
    this.validateAttribute(DEFININGMDATTRIBUTE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getDefiningMdAttributeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdWebAttribute.CLASS);
    return mdClassIF.definesAttribute(DEFININGMDATTRIBUTE);
  }
  
  public void setDefiningMdAttribute(com.runwaysdk.system.metadata.MdAttribute value)
  {
    if(value == null)
    {
      setValue(DEFININGMDATTRIBUTE, "");
    }
    else
    {
      setValue(DEFININGMDATTRIBUTE, value.getOid());
    }
  }
  
  public Boolean getShowOnSearch()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(SHOWONSEARCH));
  }
  
  public void validateShowOnSearch()
  {
    this.validateAttribute(SHOWONSEARCH);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getShowOnSearchMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdWebAttribute.CLASS);
    return mdClassIF.definesAttribute(SHOWONSEARCH);
  }
  
  public void setShowOnSearch(Boolean value)
  {
    if(value == null)
    {
      setValue(SHOWONSEARCH, "");
    }
    else
    {
      setValue(SHOWONSEARCH, java.lang.Boolean.toString(value));
    }
  }
  
  public Boolean getShowOnViewAll()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(SHOWONVIEWALL));
  }
  
  public void validateShowOnViewAll()
  {
    this.validateAttribute(SHOWONVIEWALL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getShowOnViewAllMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdWebAttribute.CLASS);
    return mdClassIF.definesAttribute(SHOWONVIEWALL);
  }
  
  public void setShowOnViewAll(Boolean value)
  {
    if(value == null)
    {
      setValue(SHOWONVIEWALL, "");
    }
    else
    {
      setValue(SHOWONVIEWALL, java.lang.Boolean.toString(value));
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MdWebAttribute get(String oid)
  {
    return (MdWebAttribute) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdWebAttribute getByKey(String key)
  {
    return (MdWebAttribute) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdWebAttribute lock(java.lang.String oid)
  {
    MdWebAttribute _instance = MdWebAttribute.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdWebAttribute unlock(java.lang.String oid)
  {
    MdWebAttribute _instance = MdWebAttribute.get(oid);
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
