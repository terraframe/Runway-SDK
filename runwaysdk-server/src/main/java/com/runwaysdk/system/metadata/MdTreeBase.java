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

@com.runwaysdk.business.ClassSignature(hash = 2088734020)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to MdTree.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class MdTreeBase extends com.runwaysdk.system.metadata.MdGraph
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdTree";
  private static final long serialVersionUID = 2088734020;
  
  public MdTreeBase()
  {
    super();
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static MdTree get(String id)
  {
    return (MdTree) com.runwaysdk.business.Business.get(id);
  }
  
  public static MdTree getByKey(String key)
  {
    return (MdTree) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static MdTree lock(java.lang.String id)
  {
    MdTree _instance = MdTree.get(id);
    _instance.lock();
    
    return _instance;
  }
  
  public static MdTree unlock(java.lang.String id)
  {
    MdTree _instance = MdTree.get(id);
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