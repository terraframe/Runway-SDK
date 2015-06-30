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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.NotificationDTOIF;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.NullClientRequestException;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

@TagAnnotation(name = "messages", bodyContent = "scriptless", description = "Display all messages concerning a given attribute")
public class MessagesTagSupport extends SimpleTagSupport
{
  private String                   attribute;

  private String                   classes = "alert";

  /**
   * Current {@link NotificationDTOIF} being iterated over.
   */
  private NotificationDTOIF current;

  @AttributeAnnotation(description = "Name of desired attribute, if a name is not supplied then a list of all messages is returned")
  public String getAttribute()
  {
    return attribute;
  }

  public void setAttribute(String attribute)
  {
    this.attribute = attribute;
  }

  @AttributeAnnotation(description = "Class of span surronding the message")
  public String getClasses()
  {
    return classes;
  }

  public void setClasses(String classes)
  {
    this.classes = classes;
  }

  public void setCurrent(NotificationDTOIF current)
  {
    this.current = current;
  }

  NotificationDTOIF getCurrent()
  {
    return current;
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspTag parent = findAncestorWithClass(this, ComponentMarkerIF.class);

    //Get the client request from the JSP context
    ClientRequestIF clientRequestIF = (ClientRequestIF) this.getJspContext().findAttribute(ClientConstants.CLIENTREQUEST);

    if(clientRequestIF == null)
    {
      PageContext pageContext = (PageContext)getJspContext();
      HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

      String msg = "Could not find the ClientRequest.  Either the user is not logged in, or the ClientRequest does not exist in the ServletRequest attribute [RUNWAY_ClientRequest].";
      new NullClientRequestException(msg, request.getLocale());
    }


    if (attribute != null && parent != null)
    {
      ComponentMarkerIF component = (ComponentMarkerIF) parent;
      MutableDTO item = component.getItem();
      
      if(attribute.equals("*"))
      {
        renderMessages((List<? extends NotificationDTOIF>) clientRequestIF.getAttributeNotifications(item.getId()));        
      }
      else
      {
        renderMessages((List<? extends NotificationDTOIF>) clientRequestIF.getAttributeNotifications(item.getId(), attribute));
      }
    }
    else if (attribute != null)
    {
      if(attribute.equals("*"))
      {
        renderMessages((List<? extends NotificationDTOIF>) clientRequestIF.getAttributeNotifications(AttributeNotificationDTO.NO_COMPONENT));
      }
      else
      {
        renderMessages((List<? extends NotificationDTOIF>) clientRequestIF.getAttributeNotifications(AttributeNotificationDTO.NO_COMPONENT, attribute));        
      }
    }
    else
    {
      renderMessages((List<? extends NotificationDTOIF>) clientRequestIF.getMessages());
    }

  }

  private void renderMessages(List<? extends NotificationDTOIF> items) throws JspException, IOException
  {
    JspWriter out = this.getJspContext().getOut();

    if (items != null)
    {
      for (int i = 0; i < items.size(); i++)
      {
        current = items.get(i);

        out.write("<span class = " + this.getClasses() + ">");
        if(this.getJspBody() == null)
        {
            out.println(current.getMessage());
        }
        else
        {
          this.getJspBody().invoke(null);
        }
        out.write("</span>");
      }
    }
  }
}
