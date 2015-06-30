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
package com.runwaysdk.controller.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.runwaysdk.business.NotificationDTOIF;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name="message", bodyContent="empty", description="Marker to denote where to display each message")
public class MessageTagSupport extends SimpleTagSupport
{
  @Override
  public void doTag() throws JspException, IOException
  {
    JspTag parent = SimpleTagSupport.findAncestorWithClass(this, MessagesTagSupport.class);
    JspWriter out = this.getJspContext().getOut();

    if (parent != null)
    {
      MessagesTagSupport messages = (MessagesTagSupport) parent;
      NotificationDTOIF current = messages.getCurrent();

      out.println(current.getMessage());
    }
  }
}
