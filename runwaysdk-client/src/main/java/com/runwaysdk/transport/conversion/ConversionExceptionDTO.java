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
package com.runwaysdk.transport.conversion;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.dataaccess.ProgrammingErrorExceptionDTO;
import com.runwaysdk.localization.CommonExceptionMessageLocalizer;

public class ConversionExceptionDTO extends ProgrammingErrorExceptionDTO
{
  /**
   *
   */
  private static final long serialVersionUID = -5662792845974269715L;

  /**
   * Constructs a new ConversionExceptionDTO with the specified localized message from the server.
   *
   * @param type of the runway exception.
   * @param localizedMessage end user error message.
   * @param developerMessage developer error message.
   */
  public ConversionExceptionDTO(String type, String localizedMessage, String developerMessage)
  {
    super(type, localizedMessage, developerMessage);
  }

  /**
   * Constructs a new {@link ConversionExceptionDTO} with the specified localized message from the server but includes
   * a throwable and is used when thrown from the client.
   *
   * @param type of the runway exception.
   * @param localizedMessage end user error message.
   * @param developerMessage developer error message.
   * @param throwable
   */
  public ConversionExceptionDTO(String type, String localizedMessage, String developerMessage, Throwable throwable)
  {
    super(type, localizedMessage, developerMessage, throwable);
  }

  /**
   * Constructs a new {@link ConversionExceptionDTO} with the specified localized message from the server but includes
   * a throwable and is used when thrown from the client.
   *
   * @param developerMessage developer error message.
   * @param throwable
   */
  public ConversionExceptionDTO(String developerMessage, Throwable throwable)
  {
    super(ExceptionConstants.ConversionException.getExceptionClass(), "", developerMessage, throwable);
  }

  /**
   * Constructs a new {@link ConversionExceptionDTO} with the specified localized message from the server but includes
   * a throwable and is used when thrown from the client.
   *
   * @param developerMessage developer error message.
   * @param throwable
   */
  public ConversionExceptionDTO(String developerMessage)
  {
    super(ExceptionConstants.ConversionException.getExceptionClass(), "", developerMessage);
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
      return CommonExceptionMessageLocalizer.conversionException(CommonProperties.getDefaultLocale());
    }
    else
    {
      return super.getLocalizedMessage();
    }
  }
}
