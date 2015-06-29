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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.runwaysdk.constants.JSON;
import com.runwaysdk.controller.ServletDispatcher;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;
import com.runwaysdk.util.IDGenerator;

@TagAnnotation(name = "form", bodyContent = "scriptless", description = "Runway form tag")
public class FormTagSupport extends StandardTagSupport
{

  /**
   * List of Commands used by this form
   */
  private List<CommandTagSupport> commands = new LinkedList<CommandTagSupport>();

  public FormTagSupport()
  {
    super();

    this.commands = new LinkedList<CommandTagSupport>();

    this.addAttribute("action", "");
    this.setId(IDGenerator.nextID());
    this.setMethod("GET");
  }

  public void setMethod(String method)
  {
    this.addAttribute("method", method);
  }

  @AttributeAnnotation(description = "The HTTP method for sending data to the action URL. Default is get.")
  public String getMethod()
  {
    return this.getAttribute("method");
  }

  public void setName(String name)
  {
    this.addAttribute("name", name);
  }

  @AttributeAnnotation(required = true, description = "Defines a unique name for the form")
  public String getName()
  {
    return this.getAttribute("name");
  }

  public void setEnctype(String enctype)
  {
    this.addAttribute("enctype", enctype);
  }

  @AttributeAnnotation(required = false, description = "Encoding type")
  public String getEnctype()
  {
    return this.getAttribute("enctype");
  }

  public void setOnreset(String onreset)
  {
    this.addAttribute("onreset", onreset);
  }

  @AttributeAnnotation(description = "Script to be run when the form is reset")
  public String getOnreset()
  {
    return this.getAttribute("onreset");
  }

  public void setOnsubmit(String onsubmit)
  {
    this.addAttribute("onsubmit", onsubmit);
  }

  @AttributeAnnotation(description = "Script to be run when the form is submitted")
  public String getOnsubmit()
  {
    return this.getAttribute("onsubmit");
  }

  public void setTarget(String target)
  {
    this.addAttribute("target", target);
  }

  @AttributeAnnotation(description = "Where to open the target URL")
  public String getTarget()
  {
    return this.getAttribute("target");
  }

  void addCommand(CommandTagSupport tag)
  {
    this.commands.add(tag);
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    JspWriter out = this.getJspContext().getOut();

    try
    {
      openFormTag(out);

      if (this.getJspBody() != null)
      {
        this.getJspBody().invoke(null);
      }

      closeFormTag(out);
    }
    catch (Exception e)
    {
      // FIXME
      e.printStackTrace();
    }
  }

  private void openFormTag(JspWriter out) throws IOException
  {
    this.openTag("form", out);
  }

  public boolean isAsynchronous()
  {
    Object asynchronous = this.getJspContext().findAttribute(ServletDispatcher.IS_ASYNCHRONOUS);

    if (asynchronous != null)
    {
      return (Boolean) asynchronous;
    }

    return false;
  }

  private void closeFormTag(JspWriter out) throws IOException
  {
    this.closeTag("form", out);

    out.println();
    out.println("<script type=\"text/javascript\">");
    out.println("(function(){ ");

    for (CommandTagSupport command : commands)
    {
      out.println("// " + command.getAction());
      out.println("document.getElementById(\"" + command.getId() + "\").onclick = function(){");
      if (isAsynchronous())
      {
        String notifyCall = JSON.createControllerNotifyListenerCall(command.getAction());

        out.println("  var params = " + JSON.RUNWAY_COLLECT_FORM_VALUES.getLabel() + "('" + this.getId() + "');");
        out.println("  " + notifyCall + "(params, '" + command.getAction() + "', '" + command.getId() + "');");
      }
      else
      {
        out.println("  var formEl = document.getElementById(\"" + this.getId() + "\");");
        out.println("  formEl.action = \"" + command.getAction() + "\";");

        out.println("  var evt = document.createEvent(\"HTMLEvents\");");
        out.println("  evt.initEvent(\"submit\", false, true);");
        out.println("  formEl.dispatchEvent(evt);");
        out.println();
      }
      out.println("};");
    }

    out.println("})();");
    out.println("</script>");
  }
}
