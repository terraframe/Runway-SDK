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
package com.runwaysdk.system.graph;

@com.runwaysdk.business.ClassSignature(hash = 1436877677)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ChangeFrequencyMaster.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class ChangeFrequencyMasterBase extends com.runwaysdk.system.EnumerationMaster
{
  public final static String CLASS = "com.runwaysdk.system.graph.ChangeFrequencyMaster";
  private static final long serialVersionUID = 1436877677;
  
  public ChangeFrequencyMasterBase()
  {
    super();
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static ChangeFrequencyMaster get(String oid)
  {
    return (ChangeFrequencyMaster) com.runwaysdk.business.Business.get(oid);
  }
  
  public static ChangeFrequencyMaster getByKey(String key)
  {
    return (ChangeFrequencyMaster) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static ChangeFrequencyMaster getEnumeration(String enumName)
  {
    return (ChangeFrequencyMaster) com.runwaysdk.business.Business.getEnumeration(com.runwaysdk.system.graph.ChangeFrequencyMaster.CLASS ,enumName);
  }
  
  public static ChangeFrequencyMaster lock(java.lang.String oid)
  {
    ChangeFrequencyMaster _instance = ChangeFrequencyMaster.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static ChangeFrequencyMaster unlock(java.lang.String oid)
  {
    ChangeFrequencyMaster _instance = ChangeFrequencyMaster.get(oid);
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