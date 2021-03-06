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

@com.runwaysdk.business.ClassSignature(hash = -1694143040)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdWebReference.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdWebReferenceBase extends com.runwaysdk.system.metadata.MdWebAttribute
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdWebReference";
  public static java.lang.String SHOWONSEARCH = "showOnSearch";
  private static final long serialVersionUID = -1694143040;
  
  public MdWebReferenceBase()
  {
    super();
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.MdWebReference.CLASS);
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
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MdWebReference get(String oid)
  {
    return (MdWebReference) com.runwaysdk.business.Business.get(oid);
  }
  
  public static MdWebReference getByKey(String key)
  {
    return (MdWebReference) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdWebReference lock(java.lang.String oid)
  {
    MdWebReference _instance = MdWebReference.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdWebReference unlock(java.lang.String oid)
  {
    MdWebReference _instance = MdWebReference.get(oid);
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
