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
package com.runwaysdk.vault;

import com.runwaysdk.RunwayExceptionDTO;

public class VaultExceptionDTO extends RunwayExceptionDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = 1638616408302231557L;

  /**
   * Constructs a new VaultExceptionDTO with the specified localized message from the server. 
   * 
   * @param type of the runway exception.
   * @param localizedMessage end user error message.
   * @param developerMessage developer error message.
   */
  public VaultExceptionDTO(String type, String localizedMessage, String developerMessage)
  {
    super(type, localizedMessage, developerMessage);
  }

  /**
   * Constructs a new FileWriteExceptionDTO to be thrown from the presentation layer.  Message
   * is not localized.
   * 
   * @param developerMessage developer error message.
   */
  public VaultExceptionDTO(String developerMessage)
  {
    super("", developerMessage, developerMessage);
    this.setBaseType();
  }
}
