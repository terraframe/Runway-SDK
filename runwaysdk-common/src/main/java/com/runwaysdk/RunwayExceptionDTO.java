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
package com.runwaysdk;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.RunwayExceptionDTOInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;

public class RunwayExceptionDTO extends RuntimeException implements RunwayExceptionIF
{

  /**
   *
   */
  private static final long serialVersionUID = -3458376575854887082L;

  public static final String CLASS = RunwayExceptionDTOInfo.CLASS;

  private String type;

  private String developerMessage;

  /**
   * Constructs a new <code>RunwayExceptionDTO</code> with the specified localized message from the server.
   *
   * @param type of the runway exception.
   * @param localizedMessage end user error message.
   * @param developerMessage developer error message.
   */
  public RunwayExceptionDTO(String type, String localizedMessage, String developerMessage)
  {
    super(localizedMessage);
    this.init(type, developerMessage);
  }

  /**
   * Constructs a new <code>RunwayExceptionDTO</code> with the specified localized message from the server but includes
   * a throwable and is used when thrown from the client.
   *
   * @param type of the runway exception.
   * @param localizedMessage end user error message.
   * @param developerMessage developer error message.
   * @param throwable
   */
  public RunwayExceptionDTO(String type, String localizedMessage, String developerMessage, Throwable throwable)
  {
    super(localizedMessage, throwable);
    this.init(type, developerMessage);
  }

  private void init(String type, String developerMessage)
  {
    this.type = type;
    this.developerMessage = developerMessage;
  }

  /**
   * Returns the qualified name of the DTO class minus the DTO suffix.
   * @param dtoClassName
   * @return qualified name of the DTO class minus the DTO suffix.
   */
  protected static String getType(String dtoClassName)
  {
    return dtoClassName.substring(0, dtoClassName.length()-3);
  }

  public String getType()
  {
    return this.type;
  }

  public String getDeveloperMessage()
  {
    return this.developerMessage;
  }

  protected void setBaseType()
  {
    String thisType = this.getClass().getName();
    int index = thisType.indexOf(TypeGeneratorInfo.DTO_SUFFIX);
    this.type =  thisType.substring(0, index);
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
