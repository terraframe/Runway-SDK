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

public class MobileGroupField extends MobileGroupFieldBase
{
  private static final long serialVersionUID = 484989314;
  
  public MobileGroupField(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public MobileGroupField(com.runwaysdk.system.metadata.MdMobileGroup parent, com.runwaysdk.system.metadata.MdMobileField child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
