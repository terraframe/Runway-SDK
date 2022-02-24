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
package com.runwaysdk.controller;

/**
 * Programming exception thrown when multiple endpoints with the same action
 * name are registered multiple times
 * 
 * @author terraframe
 */
public class DuplicateEndpointException extends RuntimeException
{

  /**
   * 
   */
  private static final long serialVersionUID = 3536724705273765083L;

  private String            endpoint;

  /**
   * 
   */
  public DuplicateEndpointException(String endpoint)
  {
    super();

    this.endpoint = endpoint;
  }

  /**
   * @param message
   * @param cause
   */
  public DuplicateEndpointException(String endpoint, String message)
  {
    super(message);

    this.endpoint = endpoint;
  }

  public String getEndpoint()
  {
    return endpoint;
  }
}
