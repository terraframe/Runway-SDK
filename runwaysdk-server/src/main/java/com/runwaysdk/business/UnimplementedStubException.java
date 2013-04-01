/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.business;

import com.runwaysdk.ServerExceptionMessageLocalizer;

/**
 * Sometimes generated methods in base classes expect to be overridden. If one
 * is called, this Exception is thrown. This is especially helpful for methods
 * with no return type, because without throwing an exception they can silently
 * do nothing, which can be difficult to debug.
 * 
 * @author Eric
 */
public class UnimplementedStubException extends BusinessException
{
  private static final long serialVersionUID = 6316579579617771742L;
  
  private String type;
  
  private String method;
  
  /**
   * Constructs a new UnimplementedStubException with the specified developer message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param type The type name that contains the method
   * @param method The name of the method
   */
  public UnimplementedStubException(String devMessage, String type, String method)
  {
    super(devMessage);
    this.type = type;
    this.method = method;
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.unimplementedStubException(this.getLocale(), type, method);
  }

}
