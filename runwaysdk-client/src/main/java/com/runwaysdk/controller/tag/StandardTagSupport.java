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

public abstract class StandardTagSupport extends SimpleMapTagSupport
{
  @AttributeAnnotation(description = "A unique id for the element")
  public String getId()
  {
    return this.getAttribute("id");
  }

  public void setId(String id)
  {
    this.addAttribute("id", id);
  }

  @AttributeAnnotation(description = "Sets the language code")
  public String getLang()
  {
    return this.getAttribute("lang");
  }

  public void setLang(String lang)
  {
    this.addAttribute("lang", lang);
  }

  @AttributeAnnotation(description = "Sets the text direction")
  public String getDir()
  {
    return this.getAttribute("dir");
  }

  public void setDir(String dir)
  {
    this.addAttribute("dir", dir);
  }

  @AttributeAnnotation(description = "The class of the element")
  public String getClasses()
  {
    return this.getAttribute("class");
  }

  public void setClasses(String classes)
  {
    this.addAttribute("class", classes);
  }

  @AttributeAnnotation(description = "An inline style definition")
  public String getStyle()
  {
    return this.getAttribute("style");
  }

  public void setStyle(String style)
  {
    this.addAttribute("style", style);
  }

  @AttributeAnnotation(description = "A text to display in a tool tip")
  public String getTitle()
  {
    return this.getAttribute("title");
  }

  public void setTitle(String title)
  {
    this.addAttribute("title", title);
  }
  
  @AttributeAnnotation(description="What to do on a mouse click")
  public String getOnclick()
  {
    return this.getAttribute("onclick");
  }

  public void setOnclick(String onclick)
  {
    this.addAttribute("onclick", onclick);
  }

  @AttributeAnnotation(description="What to do on a mouse doubleclick")
  public String getOndblclick()
  {
    return this.getAttribute("ondblclick");
  }

  public void setOndblclick(String ondblclick)
  {
    this.addAttribute("ondblclick", ondblclick);
  }

  @AttributeAnnotation(description="What to do when mouse button is pressed")
  public String getOnmousedown()
  {
    return this.getAttribute("onmousedown");
  }

  public void setOnmousedown(String onmousedown)
  {
    this.addAttribute("onmousedown", onmousedown);
  }

  @AttributeAnnotation(description="What to do when mouse button is released")
  public String getOnmouseup()
  {
    return this.getAttribute("onmouseup");
  }

  public void setOnmouseup(String onmouseup)
  {
    this.addAttribute("onmouseup", onmouseup);
  }

  @AttributeAnnotation(description="What to do when mouse pointer moves over an element")
  public String getOnmouseover()
  {
    return this.getAttribute("onmouseover");
  }

  public void setOnmouseover(String onmouseover)
  {
    this.addAttribute("onmouseover", onmouseover);
  }

  @AttributeAnnotation(description="What to do when mouse pointer moves")
  public String getOnmousemove()
  {
    return this.getAttribute("onmousemove");
  }

  public void setOnmousemove(String onmousemove)
  {
    this.addAttribute("onmousemove", onmousemove);
  }

  @AttributeAnnotation(description="What to do when mouse pointer moves out of an element")
  public String getOnmouseout()
  {
    return this.getAttribute("onmouseout");
  }

  public void setOnmouseout(String onmouseout)
  {
    this.addAttribute("onmouseout", onmouseout);
  }

  @AttributeAnnotation(description="What to do when key is pressed and released")
  public String getOnkeypress()
  {
    return this.getAttribute("onkeypress");
  }

  public void setOnkeypress(String onkeypress)
  {
    this.addAttribute("onkeypress", onkeypress);
  }

  @AttributeAnnotation(description="What to do when key is pressed")
  public String getOnkeydown()
  {
    return this.getAttribute("onkeydown");
  }

  public void setOnkeydown(String onkeydown)
  {
    this.addAttribute("onkeydown", onkeydown);
  }

  @AttributeAnnotation(description="What to do when key is released")
  public String getOnkeyup()
  {
    return this.getAttribute("onkeyup");
  }

  public void setOnkeyup(String onkeyup)
  {
    this.addAttribute("onkeyup", onkeyup);
  }

}
