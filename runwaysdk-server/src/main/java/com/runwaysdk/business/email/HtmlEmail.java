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
package com.runwaysdk.business.email;

import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * Class that represents an html-only email.
 */
public final class HtmlEmail extends Email
{
  /**
   * Constructor to set the basic properties of an html email.
   * 
   * @param to
   * @param from
   * @param subject
   * @param body
   */
  public HtmlEmail(String to, String from, String subject, String body)
  {
    super(to, from, subject, body);
  }

  @Override
  synchronized protected void setBodyInMessage(Message message, String body) throws MessagingException
  {
    message.setContent(body, "text/html");
  }

}
