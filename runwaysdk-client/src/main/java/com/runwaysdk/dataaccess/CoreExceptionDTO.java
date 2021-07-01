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

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.localization.CommonExceptionMessageLocalizer;

public class CoreExceptionDTO extends DataAccessExceptionDTO
{
  /**
   *
   */
  private static final long serialVersionUID = -4249762858576425563L;

  /**
   * Constructs a new CoreExceptionDTO with the specified localized message from the server.
   *
   * @param type of the runway exception.
   * @param localizedMessage end user error message.
   * @param developerMessage developer error message.
   */
  public CoreExceptionDTO(String type, String localizedMessage, String developerMessage)
  {
    super(type, localizedMessage, developerMessage);
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   *
   */
  public String getLocalizedMessage()
  {
    if (super.getLocalizedMessage() == null || super.getLocalizedMessage().trim().length() == 0)
    {
      return CommonExceptionMessageLocalizer.runwayException(CommonProperties.getDefaultLocale());
    }
    else
    {
      return super.getLocalizedMessage();
    }
  }
}
