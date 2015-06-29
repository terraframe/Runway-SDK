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
package com.runwaysdk.controller.tag;

import com.runwaysdk.controller.tag.develop.AttributeAnnotation;

public abstract class InputElementTagSupport extends FormElementTagSupport implements InputMarker
{

  public InputElementTagSupport()
  {
    super();
  }

  public void setValue(String value)
  {
    this.addAttribute("value", value);
  }

  @AttributeAnnotation(rtexprvalue = true, description = "The value to be displayed in the input field")
  public String getValue()
  {
    return this.getAttribute("value");
  }

  public void setAccept(String accept)
  {
    this.addAttribute("accept", accept);
  }

  @AttributeAnnotation(description = "List of MIME types for file upload")
  public String getAccept()
  {
    return this.getAttribute("accept");
  }

  public void setAlt(String alt)
  {
    this.addAttribute("alt", alt);
  }

  @AttributeAnnotation(description = "Short alternative description")
  public String getAlt()
  {
    return this.getAttribute("alt");
  }

  public void setIsmap(String ismap)
  {
    this.addAttribute("ismap", ismap);
  }

  @AttributeAnnotation(description = "Use server-side image map")
  public String getIsmap()
  {
    return this.getAttribute("ismap");
  }

  public void setUsemap(String usemap)
  {
    this.addAttribute("usemap", usemap);
  }

  @AttributeAnnotation(description = "Use client-side image map")
  public String getUsemap()
  {
    return this.getAttribute("usemap");
  }

  public void setMaxlength(String maxlength)
  {
    this.addAttribute("maxlength", maxlength);
  }

  @AttributeAnnotation(description = "Max chars for text fields")
  public String getMaxlength()
  {
    return this.getAttribute("maxlength");
  }

  public void setSize(String size)
  {
    this.addAttribute("size", size);
  }

  @AttributeAnnotation(description = "Size of the input field")
  public String getSize()
  {
    return this.getAttribute("size");
  }

  public void setSrc(String src)
  {
    this.addAttribute("src", src);
  }

  @AttributeAnnotation(description = "For fields with images")
  public String getSrc()
  {
    return this.getAttribute("src");
  }

  public void setReadonly(String readonly)
  {
    this.addAttribute("readonly", readonly);
  }

  @AttributeAnnotation(description = "Flag indicating if the input is read only")
  public String getReadonly()
  {
    return this.getAttribute("readonly");
  }

  @AttributeAnnotation(description = "Alignment")
  public String getAlign()
  {
    return this.getAttribute("align");
  }

  public void setAlign(String align)
  {
    this.addAttribute("align", align);
  }

  @AttributeAnnotation(description = "Disabled input controls")
  public String getDisabled()
  {
    return this.getAttribute("disabled");
  }

  public void setDisabled(String disabled)
  {
    this.addAttribute("disabled", disabled);
  }

  public void setOnselect(String onselect)
  {
    this.addAttribute("onselect", onselect);
  }

  @AttributeAnnotation(description = "Some text was selected")
  public String getOnselect()
  {
    return this.getAttribute("onselect");
  }
}
