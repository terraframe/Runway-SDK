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

@com.runwaysdk.business.ClassSignature(hash = -756513061)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to Locale.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class LocaleBase extends com.runwaysdk.system.EnumerationMaster
{
  public final static String CLASS = "com.runwaysdk.system.metadata.Locale";
  public final static java.lang.String LOCALELABEL = "localeLabel";
  private static final long serialVersionUID = -756513061;
  
  public LocaleBase()
  {
    super();
  }
  
  public String getLocaleLabel()
  {
    return getValue(LOCALELABEL);
  }
  
  public void validateLocaleLabel()
  {
    this.validateAttribute(LOCALELABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getLocaleLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.metadata.Locale.CLASS);
    return mdClassIF.definesAttribute(LOCALELABEL);
  }
  
  public void setLocaleLabel(String value)
  {
    if(value == null)
    {
      setValue(LOCALELABEL, "");
    }
    else
    {
      setValue(LOCALELABEL, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static Locale get(String oid)
  {
    return (Locale) com.runwaysdk.business.Business.get(oid);
  }
  
  public static Locale getByKey(String key)
  {
    return (Locale) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static Locale getEnumeration(String enumName)
  {
    return (Locale) com.runwaysdk.business.Business.getEnumeration("com.runwaysdk.system.metadata.Locale",enumName);
  }
  
  public static Locale lock(java.lang.String oid)
  {
    Locale _instance = Locale.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static Locale unlock(java.lang.String oid)
  {
    Locale _instance = Locale.get(oid);
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
