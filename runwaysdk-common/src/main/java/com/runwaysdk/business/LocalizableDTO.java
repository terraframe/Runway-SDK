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
package com.runwaysdk.business;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.system.metadata.MdExceptionDTO;
import com.runwaysdk.system.metadata.MdLocalizableMessageDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class LocalizableDTO extends TransientDTO
{

  /**
   *
   */
  private static final long serialVersionUID = -3575650888777336069L;

  private String            localizedMessage = "";

  private String            developerMessage = "";

  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected LocalizableDTO(ClientRequestIF clientRequest, String type)
  {
    super(clientRequest, type);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or
   * back-end. If the clientRequest is null, then it is instantiated on the
   * front-end, otherwise on the back-end.
   */
  protected LocalizableDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, type, attributeMap);
  }

  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected LocalizableDTO(ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or
   * back-end. If the clientRequest is null, then it is instantiated on the
   * front-end, otherwise on the back-end.
   */
  protected LocalizableDTO(ClientRequestIF clientRequest, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, attributeMap);
  }

  /**
   * @return The localized end user Message.
   */
  public String getLocalizedMessage()
  {
    return localizedMessage;
  }

  public String getMessage()
  {
    String l = this.getLocalizedMessage();
    if (l.length() == 0)
    {
      ClientRequestIF request = this.getRequest();

      if (request != null)
      {
        Locale locale = request.getLocales()[0];
        String id = this.getMd().getId();

        MdExceptionDTO mdExceptionDTO = MdExceptionDTO.get(getRequest(), id);
        MdLocalizableMessageDTO messages = mdExceptionDTO.getMessage();
        l = messages.getValue(locale);

        this.setLocalizedMessage(l);
      }
    }
    return l;
  }

  /**
   * @param localizedMessage
   *          The localized end user Message.
   */
  public void setLocalizedMessage(String localizedMessage)
  {
    this.localizedMessage = localizedMessage;
  }

  /**
   * @return The unlocalized Developer Message
   */
  public String getDeveloperMessage()
  {
    return developerMessage;
  }

  /**
   * @param developerMessage
   *          The unlocalized Developer Message
   */
  public void setDeveloperMessage(String developerMessage)
  {
    this.developerMessage = developerMessage;
  }
}
