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
package com.runwaysdk.business;

import java.util.List;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.StructQueryDTOInfo;


public class StructQueryDTO extends EntityQueryDTO
{

  public static final String CLASS = StructQueryDTOInfo.CLASS;

  /**
   *
   */
  private static final long serialVersionUID = -5363729679935536233L;

  /**
   * Constructor to set the query type.
   *
   * @param type
   */
  protected StructQueryDTO(String type)
  {
    super(type);
  }

  @SuppressWarnings("unchecked")
  public List<? extends StructDTO> getResultSet()
  {
    return (List<? extends StructDTO>) super.getResultSet();
  }

  /**
   * Method to create an order by clause with an attribute and sort order defined by a struct
   * through a struct attribute. The order should
   * either be "asc" or "desc". Use the constants QueryConditions.ASC and
   * QueryConditions.DESC for the order param.
   *
   * @param attributeStruct
   * @param attribute
   * @param order
   */
  public void addStructOrderBy(String attributeStruct, String attribute, String order)
  {
    String error = "Queried type [" + this.getType()
        + "] is a struct and cannot have struct attributes. [" + attributeStruct
        + "] is a struct attribute and should not exist on the queried type.";
    CommonExceptionProcessor.processException(
        ExceptionConstants.ForbiddenMethodException.getExceptionClass(), error);
  }
}
