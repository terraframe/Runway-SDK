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
package com.runwaysdk.session;

import java.util.Locale;

import com.runwaysdk.localization.CommonExceptionMessageLocalizer;

public class AttributeReadPermissionExceptionDTO extends PermissionExceptionDTO
{
  /**
   *
   */
  private static final long serialVersionUID = -7336609730777709475L;

  /**
   * Constructs a new {@link AttributePermissionExceptionDTO} with the specified
   * localized message from the server.
   * 
   * @param type
   *          of the runway exception.
   * @param localizedMessage
   *          end user error message.
   * @param developerMessage
   *          developer error message.
   */
  public AttributeReadPermissionExceptionDTO(String type, String localizedMessage, String developerMessage)
  {
    super(type, localizedMessage, developerMessage);
  }

  private String attributeDisplayLabel;

  private String classDisplayLabel;

  private Locale locale;

  /**
   * Assumes the locale is not null.
   * 
   * @param developerMessage
   * @param locale
   * @param attributeDisplayLabel
   * @param classDisplayLabel
   */
  public AttributeReadPermissionExceptionDTO(String developerMessage, Locale locale, String attributeDisplayLabel, String classDisplayLabel)
  {
    super("", developerMessage, developerMessage);
    this.setBaseType();
    this.locale = locale;
    this.attributeDisplayLabel = attributeDisplayLabel;
    this.classDisplayLabel = classDisplayLabel;
  }

  /**
   * 
   * @return
   */
  protected Locale getLocale()
  {
    return this.locale;
  }

  @Override
  public String getLocalizedMessage()
  {
    if (attributeDisplayLabel != null && classDisplayLabel != null)
    {
      return CommonExceptionMessageLocalizer.attributeReadPermissionException(this.getLocale(), attributeDisplayLabel, classDisplayLabel);
    }
    else
    {
      return super.getLocalizedMessage();
    }
  }
}
