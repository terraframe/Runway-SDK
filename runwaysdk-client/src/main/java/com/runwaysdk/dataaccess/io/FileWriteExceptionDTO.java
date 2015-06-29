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
package com.runwaysdk.dataaccess.io;

import java.io.File;

import com.runwaysdk.CommonExceptionMessageLocalizer;
import com.runwaysdk.SystemExceptionDTO;
import com.runwaysdk.constants.CommonProperties;

public class FileWriteExceptionDTO extends SystemExceptionDTO
{
  /**
   * The file that was being written
   */
  private File file = null;

  /**
   *
   */
  private static final long serialVersionUID = 1277592448821903387L;

  /**
   * Constructs a new {@link FileWriteExceptionDTO} with the specified localized message from the server.
   *
   * @param type of the runway exception.
   * @param localizedMessage end user error message.
   * @param developerMessage developer error message.
   */
  public FileWriteExceptionDTO(String type, String localizedMessage, String developerMessage)
  {
    super(type, localizedMessage, developerMessage);
  }

  /**
   * Constructs a new {@link FileWriteExceptionDTO} to be thrown from the presentation layer.  Message
   * is not localized.
   *
   * @param developerMessage developer error message.
   */
  public FileWriteExceptionDTO(String developerMessage)
  {
    super("", developerMessage, developerMessage);
    this.setBaseType();
  }

  /**
   * Constructs a new {@link FileWriteExceptionDTO} to be thrown from the presentation layer.  Message
   * is not localized.
   *
   * @param developerMessage developer error message.
   * @param file
   */
  public FileWriteExceptionDTO(File file, Throwable throwable)
  {
    super("", "", "", throwable);
    this.setBaseType();
    this.file = file;
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
      return CommonExceptionMessageLocalizer.fileWriteException(CommonProperties.getDefaultLocale(), this.file);
    }
    else
    {
      return super.getLocalizedMessage();
    }
  }
}
