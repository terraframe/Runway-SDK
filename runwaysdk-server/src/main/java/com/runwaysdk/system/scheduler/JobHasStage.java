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
package com.runwaysdk.system.scheduler;

public class JobHasStage extends JobHasStageBase
{
  private static final long serialVersionUID = 1982204482;
  
  public JobHasStage(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public JobHasStage(com.runwaysdk.system.scheduler.MultiStageJob parent, com.runwaysdk.system.scheduler.AbstractJob child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
