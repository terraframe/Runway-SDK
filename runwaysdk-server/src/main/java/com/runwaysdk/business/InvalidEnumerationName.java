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
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;

public class InvalidEnumerationName extends BusinessException
{
  /**
   * 
   */
  private static final long serialVersionUID = 3935162259731934127L;

  private String enumName;

  private MdEnumerationDAOIF mdEnumerationIF;
  
  /**
   * Thrown when an enumeration name is not valid with respect to the given enumeration.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param enumName name of the enumeration that was invalid.
   * @param mdEnumerationIF enumeration.
   */
  public InvalidEnumerationName(String devMessage, String enumName, MdEnumerationDAOIF mdEnumerationIF)
  {
    super(devMessage);
    this.enumName = enumName;
    this.mdEnumerationIF = mdEnumerationIF;
  }
  
  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.invalidEnumerationName(this.getLocale(), this.enumName, this.mdEnumerationIF);
  }

}
