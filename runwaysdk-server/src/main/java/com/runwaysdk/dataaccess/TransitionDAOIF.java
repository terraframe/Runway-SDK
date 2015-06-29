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
package com.runwaysdk.dataaccess;

import java.util.Map;

import com.runwaysdk.business.state.StateMasterDAOIF;

public interface TransitionDAOIF extends GraphDAOIF
{
  public final String DISPLAY_LABEL = "displayLabel";

  /**
   * Returns the name of the transition.
   * @return name of the transition.
   */
  public String getName();

  /**
   * Returns the fully qualified name of the transition, including the type and the name of the transition.
   * @return fully qualified name of the transition, including the type and the name of the transition.
   */
  public String getQualifiedName();

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getDisplayLabes();

  /**
   * Returns the signature of the metadata.
   *
   * @return signature of the metadata.
   */
  public String getSignature();

  /**
   * Returns a deep cloned-copy of this Relationshipo
   */
  public TransitionDAO getRelationshipDAO();

  public StateMasterDAOIF getParent();

  public StateMasterDAOIF getChild();

}
