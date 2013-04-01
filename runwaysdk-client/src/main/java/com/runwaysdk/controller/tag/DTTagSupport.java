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
package com.runwaysdk.controller.tag;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspTag;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.controller.DTOFacade;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;
import com.runwaysdk.transport.metadata.AttributeMdDTO;

/**
 * @author Darrell Taylor
 */
@TagAnnotation(name = "dt", description = "a DT DL pair with inner tags going in to the DL ", bodyContent = "scriptless")
public class DTTagSupport extends FormElementTagSupport
{

  /**
   * Name of the controller parameter or attribute being inputed
   */
  private String  attribute;

  /**
   * The type of the input field
   */
  private String  type;

  /**
   * The display label for this attribute
   */
  private String  displayLabel;

  /**
   * Flag indicating if a messages tag should generated.
   */
  private Boolean includeMessages;

  public DTTagSupport()
  {
    super();
  }

  public void setAttribute(String attribute)
  {
    this.attribute = attribute;
  }

  @AttributeAnnotation(required = true, description = "The name of the controller parameter or attribute")
  public String getAttribute()
  {
    return attribute;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  @AttributeAnnotation(required = false, description = "The type of the input field")
  public String getType()
  {
    return type;
  }

  @AttributeAnnotation(required = false, description = "The display label for this attribute")
  public String getDisplayLabel()
  {
    return displayLabel;
  }

  public void setDisplayLabel(String displayLabel)
  {
    this.displayLabel = displayLabel;
  }

  @AttributeAnnotation(description = "Flag denoting if an empty option should be generated.", rtexprvalue = true)
  public Boolean getIncludeMessages()
  {
    return includeMessages;
  }

  public void setIncludeMessages(Boolean includeMessages)
  {
    this.includeMessages = includeMessages;
  }

  public void doTag() throws JspException, IOException
  {
    JspWriter out = this.getJspContext().getOut();
    JspTag parent = findAncestorWithClass(this, ComponentMarkerIF.class);

    String name = this.getAttribute();

    // If the input tag is in the context of a component then
    // load update the parameter name and display value
    if (parent != null)
    {
      ComponentMarkerIF component = (ComponentMarkerIF) parent;
      MutableDTO item = component.getItem();

      name = component.getParam() + "." + this.getAttribute();

      DTOFacade facade = new DTOFacade(this.getAttribute(), item);

      try
      {
        if (item.isReadable(this.getAttribute()))
        {
          String displayLabel = this.getDisplayLabel(facade);
          String title = this.getTitle(item, facade);

          out.write("<dt ");
          // we want to use classes as an attrib for the inner input
          // tag if it exists
          String inner_class = this.getClasses();
          if (this.getType() != null)
          {
            this.setClasses(null);
          }

          this.writeAttributes(out);

          out.write(" ><label ");

          if (title != null && title.length() > 0)
          {
            out.write("title=\"" + title + "\"");
          }

          out.write(">");

          if (displayLabel != null)
          {
            out.write(displayLabel);
          }
          out.write("</label></dt>");
          out.write("<dd>");

          // if type is set we write an input tag
          if (this.getType() == "text")
          {

            InputTagSupport it = new InputTagSupport();
            it.setJspContext(this.getJspContext());
            it.setValue(item.getValue(this.getAttribute()));
            it.setParam(name);
            it.setType(this.getType());
            it.setId(this.getAttribute() + "input");
            it.setClasses(inner_class);
            it.doTag();

          }
          // execute any inner tags
          if (this.getJspBody() != null)
          {
            StringWriter body = new StringWriter();
            this.getJspBody().invoke(body);

            if (body.toString().trim().length() > 0)
            {
              out.println(body);
            }
            else
            {
              out.println("-");
            }
          }
          // dt tag always prints out messages
          MessagesTagSupport mt = new MessagesTagSupport();
          mt.setJspContext(this.getJspContext());
          mt.setParent(parent);
          mt.setAttribute(this.getAttribute());
          mt.doTag();

          out.write("</dd>");
        }
      }
      catch (ClassCastException e)
      {
        // something went wrong, do nothing
        e.printStackTrace();
      }
    }

  }

  private String getDisplayLabel(DTOFacade facade)
  {
    try
    {
      AttributeMdDTO attributeMdDTO = facade.getAttributeMdDTO();

      if (this.getDisplayLabel() == null)
      {
        if (attributeMdDTO.isRequired())
        {
          return "* " + attributeMdDTO.getDisplayLabel();
        }

        return attributeMdDTO.getDisplayLabel();
      }
      
      if (attributeMdDTO.isRequired())
      {
        return "* " + this.getDisplayLabel();
      }
      
      return this.getDisplayLabel();
    }
    catch (Exception e)
    {
      return this.getAttribute();
    }

  }

  private String getTitle(MutableDTO item, DTOFacade facade)
  {
    if (this.getTitle() == null)
    {
      try
      {
        StringBuffer buffer = new StringBuffer();

        AttributeMdDTO attributeMdDTO = facade.getAttributeMdDTO();

        if (attributeMdDTO.isRequired())
        {
          buffer.append("* ");
        }

        buffer.append(attributeMdDTO.getDescription());

        String classLabel = item.getMd().getDisplayLabel();
        String attributeLabel = attributeMdDTO.getDisplayLabel();

        buffer.append(" (" + classLabel + " : " + attributeLabel + ")");

        return buffer.toString();
      }
      catch (Exception e)
      {
        // e.printStackTrace();
      }

    }

    return this.getTitle();
  }
}
