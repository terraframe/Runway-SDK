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
package com.runwaysdk.dataaccess.metadata;

public class DeleteContext
{
  private boolean businessContext;

  private boolean enforceRemovable;

  private boolean removeValues;

  private boolean executeImmediately;

  public DeleteContext()
  {
    this(false);
  }

  public DeleteContext(boolean businessContext)
  {
    this.businessContext = businessContext;
    this.enforceRemovable = true;
    this.removeValues = true;
    this.executeImmediately = false;
  }

  public boolean isBusinessContext()
  {
    return businessContext;
  }

  public DeleteContext setBusinessContext(boolean businessContext)
  {
    this.businessContext = businessContext;

    return this;
  }

  public boolean isEnforceRemovable()
  {
    return enforceRemovable;
  }

  public DeleteContext setEnforceRemovable(boolean enforceRemovable)
  {
    this.enforceRemovable = enforceRemovable;

    return this;
  }

  public boolean isRemoveValues()
  {
    return removeValues;
  }

  public DeleteContext setRemoveValues(boolean removeValues)
  {
    this.removeValues = removeValues;

    return this;
  }

  public boolean isExecuteImmediately()
  {
    return executeImmediately;
  }

  public DeleteContext setExecuteImmediately(boolean executeImmediately)
  {
    this.executeImmediately = executeImmediately;

    return this;
  }
}
