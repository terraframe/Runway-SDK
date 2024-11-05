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
package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = -1756526317)
public class SubClassControllerBase implements com.runwaysdk.generation.loader.
{
  public static final String CLASS = "com.runwaysdk.jstest.SubClassController";
  protected jakarta.servlet.http.HttpServletRequest req;
  protected jakarta.servlet.http.HttpServletResponse resp;
  protected java.lang.Boolean isAsynchronous;
  protected java.lang.String dir;
  protected java.lang.String layout;
  
  private static final long serialVersionUID = -1756526317;
  
  public SubClassControllerBase(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    this(req, resp, isAsynchronous, "","");
  }
  
  public SubClassControllerBase(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous, java.lang.String dir, java.lang.String layout)
  {
    this.req = req;
    this.resp = resp;
    this.isAsynchronous = isAsynchronous;
    this.dir = dir;
    this.layout = layout;
  }
  
  protected void render(String jsp) throws java.io.IOException, jakarta.servlet.ServletException
  {
    if(!resp.isCommitted())
    {
      if(this.isAsynchronous())
      {
        req.getRequestDispatcher(dir+jsp).forward(req, resp);
      }
      else
      {
        req.setAttribute(com.runwaysdk.controller.JSPFetcher.INNER_JSP, dir+jsp);
        req.getRequestDispatcher(layout).forward(req, resp);
      }
    }
  }
  
  public jakarta.servlet.http.HttpServletRequest getRequest()
  {
    return this.req;
  }
  
  public jakarta.servlet.http.HttpServletResponse getResponse()
  {
    return this.resp;
  }
  
  public java.lang.Boolean isAsynchronous()
  {
    return this.isAsynchronous;
  }
  
  public com.runwaysdk.constants.ClientRequestIF getClientRequest()
  {
    return (com.runwaysdk.constants.ClientRequestIF) req.getAttribute(com.runwaysdk.constants.ClientConstants.CLIENTREQUEST);
  }
  
  public com.runwaysdk.ClientSession getClientSession()
  {
    return (com.runwaysdk.ClientSession) req.getSession().getAttribute(com.runwaysdk.constants.ClientConstants.CLIENTSESSION);
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.SubClassDTO:dto", post=true)
  public void cancel(com.runwaysdk.jstest.SubClassDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.cancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.SubClassDTO:dto", post=true)
  public void failCancel(com.runwaysdk.jstest.SubClassDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.failCancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.SubClassDTO:dto", post=true)
  public void create(com.runwaysdk.jstest.SubClassDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.create");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.SubClassDTO:dto", post=true)
  public void failCreate(com.runwaysdk.jstest.SubClassDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.failCreate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.SubClassDTO:dto", post=true)
  public void delete(com.runwaysdk.jstest.SubClassDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.delete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.SubClassDTO:dto", post=true)
  public void failDelete(com.runwaysdk.jstest.SubClassDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.failDelete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:oid", post=false)
  public void edit(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.edit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:oid", post=false)
  public void failEdit(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.failEdit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void newInstance() throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.newInstance");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failNewInstance() throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.failNewInstance");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.SubClassDTO:dto", post=true)
  public void update(com.runwaysdk.jstest.SubClassDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.update");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.SubClassDTO:dto", post=true)
  public void failUpdate(com.runwaysdk.jstest.SubClassDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.failUpdate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:oid", post=false)
  public void view(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.view");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:oid", post=false)
  public void failView(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.failView");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void viewAll() throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.viewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failViewAll() throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.failViewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.Boolean:isAscending, java.lang.Integer:pageSize, java.lang.Integer:pageNumber", post=false)
  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.viewPage");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.String:isAscending, java.lang.String:pageSize, java.lang.String:pageNumber", post=false)
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.SubClassController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.SubClassController.failViewPage");
  }
  
}
