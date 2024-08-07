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
package com.runwaysdk.system.metadata.graph;

@com.runwaysdk.business.ClassSignature(hash = -707065066)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to EdgeInheritance.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class EdgeInheritanceBase extends com.runwaysdk.system.metadata.MetadataRelationship
{
  public final static String CLASS = "com.runwaysdk.system.metadata.graph.EdgeInheritance";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -707065066;
  
  public EdgeInheritanceBase(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.system.metadata.MdEdge getParent()
  {
    return (com.runwaysdk.system.metadata.MdEdge) super.getParent();
  }
  
  public com.runwaysdk.system.metadata.MdEdge getChild()
  {
    return (com.runwaysdk.system.metadata.MdEdge) super.getChild();
  }
  
  public static EdgeInheritance get(String oid)
  {
    return (EdgeInheritance) com.runwaysdk.business.Relationship.get(oid);
  }
  
  public static EdgeInheritance getByKey(String key)
  {
    return (EdgeInheritance) com.runwaysdk.business.Relationship.get(CLASS, key);
  }
  
  public static EdgeInheritance lock(java.lang.String oid)
  {
    EdgeInheritance _instance = EdgeInheritance.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static EdgeInheritance unlock(java.lang.String oid)
  {
    EdgeInheritance _instance = EdgeInheritance.get(oid);
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
