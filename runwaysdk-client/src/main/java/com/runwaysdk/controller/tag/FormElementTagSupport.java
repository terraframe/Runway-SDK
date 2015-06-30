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

import com.runwaysdk.controller.tag.develop.AttributeAnnotation;

public abstract class FormElementTagSupport extends StandardTagSupport
{
  public FormElementTagSupport()
  {
    super();
  }

  public void setAccesskey(String accesskey)
  {
    this.addAttribute("accesskey", accesskey);
  }

  @AttributeAnnotation(description = "Accessibility key character")
  public String getAccesskey()
  {
    return this.getAttribute("accesskey");
  }

  public void setTabindex(String tabindex)
  {
    this.addAttribute("tabindex", tabindex);
  }

  @AttributeAnnotation(description = "Position in tabbing order")
  public String getTabindex()
  {
    return this.getAttribute("tabindex");
  }

  public void setOnblur(String onblur)
  {
    this.addAttribute("onblur", onblur);
  }

  @AttributeAnnotation(description = "The element lost the focus")
  public String getOnblur()
  {
    return this.getAttribute("onblur");
  }

  public void setOnchange(String onchange)
  {
    this.addAttribute("onchange", onchange);
  }

  @AttributeAnnotation(description = "The element value was changed")
  public String getOnchange()
  {
    return this.getAttribute("onchange");
  }

  public void setOnfocus(String onfocus)
  {
    this.addAttribute("onfocus", onfocus);
  }

  @AttributeAnnotation(description = "The element got the focus")
  public String getOnfocus()
  {
    return this.getAttribute("onfocus");
  }
}
