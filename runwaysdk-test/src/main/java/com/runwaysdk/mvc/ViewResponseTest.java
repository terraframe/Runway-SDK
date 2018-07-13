/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.mvc;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.controller.JSPFetcher;
import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.request.MockServletRequest;
import com.runwaysdk.request.MockServletResponse;
import com.runwaysdk.session.Request;

public class ViewResponseTest
{
  @Request
  @Test
  public void testAttribute() throws Exception
  {
    String name = "test";
    Integer value = new Integer(45);

    ViewTemplateResponse response = new ViewTemplateResponse("template.jsp", "view.jsp");
    response.set(name, value);

    Assert.assertEquals(value, response.getAttribute(name));
  }

  @Request
  @Test
  public void testHandle() throws Exception
  {
    MockServletRequest req = new MockServletRequest();
    MockServletResponse resp = new MockServletResponse();
    RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

    String template = "template.jsp";
    String view = "view.jsp";

    ViewTemplateResponse response = new ViewTemplateResponse(template, view);
    response.handle(manager);

    Assert.assertEquals(view, req.getAttribute(JSPFetcher.INNER_JSP));
    Assert.assertEquals(template, req.getDispatcher().getLocation());
  }

  @Request
  @Test
  public void testHandleDirectory() throws Exception
  {
    MockServletRequest req = new MockServletRequest();
    MockServletResponse resp = new MockServletResponse();
    RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

    String template = "template.jsp";
    String directory = "test";
    String view = "view.jsp";

    ViewTemplateResponse response = new ViewTemplateResponse(template, directory, view);
    response.handle(manager);

    Assert.assertEquals(directory + File.separator + view, req.getAttribute(JSPFetcher.INNER_JSP));
    Assert.assertEquals(template, req.getDispatcher().getLocation());
  }

  @Request
  @Test
  public void testHandleAttributes() throws Exception
  {
    MockServletRequest req = new MockServletRequest();
    MockServletResponse resp = new MockServletResponse();
    RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

    String name = "test";
    Integer value = new Integer(45);

    ViewTemplateResponse response = new ViewTemplateResponse("template.jsp", "view.jsp");
    response.set(name, value);
    response.handle(manager);

    Assert.assertEquals(value, (Integer) req.getAttribute(name));
  }
}
