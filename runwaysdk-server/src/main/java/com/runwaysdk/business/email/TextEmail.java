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
package com.runwaysdk.business.email;

import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * Class that represents a text-only email.
 */
public final class TextEmail extends Email
{
  /**
   * Constructor to set the basic email properties for a TextEmail.
   * 
   * @param to
   * @param from
   * @param subject
   * @param body
   */
  public TextEmail(String to, String from, String subject, String body)
  {
    super(to, from, subject, body);
  }

  @Override
  protected synchronized void setBodyInMessage(Message message, String body) throws MessagingException
  {
    message.setText(body);
  }
}
