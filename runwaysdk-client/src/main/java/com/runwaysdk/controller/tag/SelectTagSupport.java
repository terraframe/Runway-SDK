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
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspTag;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;

/**
 * @author Justin Smethie
 */
@TagAnnotation(name = "select", description = "A select list of items", bodyContent = "scriptless")
public class SelectTagSupport extends ListElementTagSupport
{
  /**
   * Flag indicating if an empty option should be generated.
   */
  private Boolean includeBlank;

  /**
   * Display Label for ALL option, leave null for no ALL option.
   */
  private String  allLabel;

  private String  selectedValues;

  public SelectTagSupport()
  {
    super();

    includeBlank = false;
  }

  public String getSelectedValues()
  {
    return selectedValues;
  }

  @AttributeAnnotation(description = "Flag indicating if selecting multiple options is allowed.")
  public Boolean getMultiple()
  {
    return Boolean.parseBoolean(this.getAttribute("multiple"));
  }

  public void setMultiple(Boolean multiple)
  {
    this.addAttribute("multiple", multiple.toString());
  }

  @AttributeAnnotation(description = "Flag denoting if the option tag is disabled. Value must be disabled")
  public String getDisabled()
  {
    return this.getAttribute("disabled");
  }

  public void setDisabled(String disabled)
  {
    this.addAttribute("disabled", disabled);
  }

  @AttributeAnnotation(description = "Flag denoting if an empty option should be generated.", rtexprvalue = true)
  public Boolean getIncludeBlank()
  {
    return includeBlank;
  }

  public void setIncludeBlank(Boolean includeBlank)
  {
    this.includeBlank = includeBlank;
  }

  @AttributeAnnotation(description = "Display Label for ALL option, leave null for no ALL option.", rtexprvalue = true)
  public String getAllLabel()
  {
    return allLabel;
  }

  @AttributeAnnotation(description = "Sets the size of the select list", rtexprvalue = true)
  public String getSize()
  {
    return this.getAttribute("size");
  }

  public void setSize(String size)
  {
    this.addAttribute("size", size);
  }

  public void setAllLabel(String allLabel)
  {
    this.allLabel = allLabel;
  }

  private String join(List<String> s, String delimiter)
  {
    StringBuilder builder = new StringBuilder();
    Iterator<String> iter = s.iterator();

    while (iter.hasNext())
    {
      builder.append(iter.next());
      if (iter.hasNext())
      {
        builder.append(delimiter);
      }
    }
    return builder.toString();
  }

  public void doTag() throws JspException, IOException
  {
    JspWriter out = this.getJspContext().getOut();
    JspTag parent = findAncestorWithClass(this, ComponentMarkerIF.class);
    String name = this.getParam();
    this.selectedValues = "";

    if (parent != null)
    {
      ComponentMarkerIF component = (ComponentMarkerIF) parent;
      name = component.getParam() + "." + this.getParam();
      MutableDTO item = component.getItem();

      try
      {
        // Object object = new DTOFacade(this.getParam(), item).getValue();
        
        try
        {
          // try to get attrib as an rnum
          this.selectedValues = "|" + join(item.getEnumNames(this.getParam()), "|") + "|";
        }
        catch (Exception e)
        {
          // try again, perhaps the attrib not an enum
          this.selectedValues = "|" + item.getValue(this.getParam()) + "|";

        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }

    }

    // Write opening select tag
    this.addAttribute("name", name);
 
    this.openTag("select", out);

    if (includeBlank)
    {
      out.println("<option value=\"\"></option>");
    }

    if (allLabel != null)
    {
      out.println("<option value=\"ALL\">" + allLabel + "</option>");
    }

    // Loop through the children tag for each item in the select list
    super.doTag();

    // Write closing select tag
    out.println("</select>");
  }
}
