/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.gis.geo;

@com.runwaysdk.business.ClassSignature(hash = -230302276)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to GeoNodeEntity.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class GeoNodeEntityBase extends com.runwaysdk.system.gis.geo.GeoNode
{
  public final static String CLASS = "com.runwaysdk.system.gis.geo.GeoNodeEntity";
  private static final long serialVersionUID = -230302276;
  
  public GeoNodeEntityBase()
  {
    super();
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static GeoNodeEntity get(String oid)
  {
    return (GeoNodeEntity) com.runwaysdk.business.Business.get(oid);
  }
  
  public static GeoNodeEntity getByKey(String key)
  {
    return (GeoNodeEntity) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static GeoNodeEntity lock(java.lang.String oid)
  {
    GeoNodeEntity _instance = GeoNodeEntity.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static GeoNodeEntity unlock(java.lang.String oid)
  {
    GeoNodeEntity _instance = GeoNodeEntity.get(oid);
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
