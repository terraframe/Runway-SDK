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
package com.runwaysdk.business;

import com.runwaysdk.util.DTOConversionUtilInfo;

public class MethodResponseDTO
{
  /**
   * The object being returned. It must be a primitive object, a ComponentDTO, a
   * ComponentQueryDTO, or a IOStream.
   */
  private Object     returnObject;

  /**
   * The type of the object being returned.
   */
  private String     returnType;

  /**
   * An updated representation of the object upon which the method was invoked
   */
  private MutableDTO calledObject;

  public MethodResponseDTO(Object[] response)
  {
    this(response[DTOConversionUtilInfo.RETURN_OBJECT], (String) response[DTOConversionUtilInfo.RETURN_DTO_TYPE], (MutableDTO) response[DTOConversionUtilInfo.CALLED_OBJECT]);
  }

  public MethodResponseDTO(Object returnObject, String returnType, MutableDTO calledObject)
  {
    this.returnObject = returnObject;
    this.returnType = returnType;
    this.calledObject = calledObject;
  }

  public MutableDTO getCalledObject()
  {
    return calledObject;
  }

  public Object getReturnObject()
  {
    return returnObject;
  }

  public String getReturnType()
  {
    return returnType;
  }
}
