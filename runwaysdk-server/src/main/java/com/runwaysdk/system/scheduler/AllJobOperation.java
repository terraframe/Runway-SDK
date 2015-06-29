/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.system.scheduler;

/**
 * @deprecated
 * DO NOT USE, THIS ONLY EXISTS FOR DDMS.
 */
@com.runwaysdk.business.ClassSignature(hash = 1274316501)
public enum AllJobOperation implements com.runwaysdk.business.BusinessEnumeration
{
  CANCEL(),
  
  PAUSE(),
  
  RESUME(),
  
  START(),
  
  STOP();
  
  public static final java.lang.String CLASS = "com.runwaysdk.system.scheduler.AllJobOperation";
  private com.runwaysdk.system.scheduler.JobOperation enumeration;
  
  private synchronized void loadEnumeration()
  {
    com.runwaysdk.system.scheduler.JobOperation enu = com.runwaysdk.system.scheduler.JobOperation.getEnumeration(this.name());
    setEnumeration(enu);
  }
  
  private synchronized void setEnumeration(com.runwaysdk.system.scheduler.JobOperation enumeration)
  {
    this.enumeration = enumeration;
  }
  
  public java.lang.String getId()
  {
    loadEnumeration();
    return enumeration.getId();
  }
  
  public java.lang.String getEnumName()
  {
    loadEnumeration();
    return enumeration.getEnumName();
  }
  
  public java.lang.String getDisplayLabel()
  {
    loadEnumeration();
    return enumeration.getDisplayLabel().getValue(com.runwaysdk.session.Session.getCurrentLocale());
  }
  
  public static AllJobOperation get(String id)
  {
    for (AllJobOperation e : AllJobOperation.values())
    {
      if (e.getId().equals(id))
      {
        return e;
      }
    }
    return null;
  }
  
}
