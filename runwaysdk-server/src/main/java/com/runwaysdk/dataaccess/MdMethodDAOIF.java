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
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Map;

import com.runwaysdk.business.rbac.MethodActorDAOIF;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.ParameterMarker;
import com.runwaysdk.dataaccess.metadata.Type;

public interface MdMethodDAOIF extends ParameterMarker
{
  /**
   * Get the MdTypeIF on which the MdMethod is generated (either an MdEntity or an MdFacade).
   *
   * @return MdTypeIF on which the MdMethod is generated (either an MdEntity or an MdFacade).
   */
  public MdTypeDAOIF getEnclosingMdTypeDAO();

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public MdMethodDAO getBusinessDAO();

  /**
   * Returns the name of the method
   *
   * @return
   */
  public String getName();

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getDisplayLabels();

  /**
   * Returns all the MdParameters that are defined for this MdMethod.
   * The MdParameters are returned in ascending order according to the
   * MdParameterInfo.Order value.
   *
   * @return
   */
  public List<MdParameterDAOIF> getMdParameterDAOs();

  /**
   * Returns the AssignableMethod declared for this MdMethod.
   * If a AssignableMethod does not exist for this MdMethod then it
   * returns null.
   *
   * @return
   */
  public MethodActorDAOIF getMethodActor();

  /**
   * Returns the return type of this MdMethod
   * @return
   */
  public Type getReturnType();

  /**
   * @return The business layer method signature of this method
   */
  public String getMethodSignature();

  /**
   * Returns if the MdMethod is static
   * @return
   */
  public boolean isStatic();

  /**
   * Get the MdType on which the MdMethod is generated.
   *
   * @return MdType on which the MdMethod is generated.
   */
  public MdTypeDAOIF getMdType();
}
