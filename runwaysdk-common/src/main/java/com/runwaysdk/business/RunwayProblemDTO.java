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

import java.io.Serializable;

import com.runwaysdk.constants.RunwayProblemDTOInfo;


public abstract class RunwayProblemDTO implements ProblemDTOIF, Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 8479100918715933486L;

  public static final String CLASS = RunwayProblemDTOInfo.CLASS;
  
  private String type;
  
  private String localizedMessage;

  private String developerMessage = "";

  protected RunwayProblemDTO(String type, String localizedMessage)
  {
    this.type = type;
    this.localizedMessage = localizedMessage;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  /**
   * @return The localized end user Message.
   */
  public String getMessage()
  {
    return this.localizedMessage;
  }

  /**
   * Returns the developer message.
   * @return developer message.
   */
  public String getDeveloperMessage()
  {
    return this.developerMessage;
  }
  
  public void setDeveloperMessage(String developerMessage)
  {
    this.developerMessage = developerMessage;
  }
}
