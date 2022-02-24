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
package com.runwaysdk.dataaccess.io.excel;

import java.util.HashMap;
import java.util.List;

import com.runwaysdk.business.Entity;
import com.runwaysdk.business.Mutable;

public interface ImportApplyListener extends ImportListener
{
  /**
   * @param instance
   *          Mutable which was just applied when the row was parsed
   */
  public void afterApply(Mutable instance);

  /**
   * @param instance
   *          Mutable which is about to be applied when the row was parsed
   */
  public void beforeApply(Mutable instance);

  /**
   * Validation event for the instance and any extra entities which were
   * applied.
   * 
   * @param instance
   * @param extraValues
   */
  public void validate(Mutable instance, HashMap<String, List<Entity>> entities);

  /**
   * @param extraEntities
   *          Map of extra entities associated with each listener
   */
  public void addAdditionalEntities(HashMap<String, List<Entity>> extraEntities);
}
