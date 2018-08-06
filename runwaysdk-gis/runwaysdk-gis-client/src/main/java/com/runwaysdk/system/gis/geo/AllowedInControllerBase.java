/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.gis.geo;

@com.runwaysdk.business.ClassSignature(hash = -554962983)
public class AllowedInControllerBase
{
  public static final String CLASS = "com.runwaysdk.system.gis.geo.AllowedInController";
  protected javax.servlet.http.HttpServletRequest req;
  protected javax.servlet.http.HttpServletResponse resp;
  protected java.lang.Boolean isAsynchronous;
  protected java.lang.String dir;
  protected java.lang.String layout;
  
  public AllowedInControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    this(req, resp, isAsynchronous, "","");
  }
  
  public AllowedInControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous, java.lang.String dir, java.lang.String layout)
  {
    this.req = req;
    this.resp = resp;
    this.isAsynchronous = isAsynchronous;
    this.dir = dir;
    this.layout = layout;
  }
  
  protected void render(String jsp) throws java.io.IOException, javax.servlet.ServletException
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
  
  public javax.servlet.http.HttpServletRequest getRequest()
  {
    return this.req;
  }
  
  public javax.servlet.http.HttpServletResponse getResponse()
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
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.gis.geo.AllowedInDTO:dto", post=true)
  public void cancel(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.cancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.gis.geo.AllowedInDTO:dto", post=true)
  public void failCancel(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.failCancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:childId", post=false)
  public void childQuery(java.lang.String childId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.childQuery");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:childId", post=false)
  public void failChildQuery(java.lang.String childId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.failChildQuery");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.gis.geo.AllowedInDTO:dto", post=true)
  public void create(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.create");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.gis.geo.AllowedInDTO:dto", post=true)
  public void failCreate(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.failCreate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.gis.geo.AllowedInDTO:dto", post=true)
  public void delete(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.delete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.gis.geo.AllowedInDTO:dto", post=true)
  public void failDelete(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.failDelete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:oid", post=false)
  public void edit(java.lang.String oid) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.edit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:oid", post=false)
  public void failEdit(java.lang.String oid) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.failEdit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:parentId, java.lang.String:childId", post=true)
  public void newInstance(java.lang.String parentId, java.lang.String childId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.newInstance");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:parentId, java.lang.String:childId", post=true)
  public void failNewInstance(java.lang.String parentId, java.lang.String childId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.failNewInstance");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void newRelationship() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.newRelationship");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failNewRelationship() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.failNewRelationship");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:parentId", post=false)
  public void parentQuery(java.lang.String parentId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.parentQuery");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:parentId", post=false)
  public void failParentQuery(java.lang.String parentId) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.failParentQuery");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.gis.geo.AllowedInDTO:dto", post=true)
  public void update(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.update");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.gis.geo.AllowedInDTO:dto", post=true)
  public void failUpdate(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.failUpdate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:oid", post=false)
  public void view(java.lang.String oid) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.view");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:oid", post=false)
  public void failView(java.lang.String oid) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.failView");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.viewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.failViewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.Boolean:isAscending, java.lang.Integer:pageSize, java.lang.Integer:pageNumber", post=false)
  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.viewPage");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.String:isAscending, java.lang.String:pageSize, java.lang.String:pageNumber", post=false)
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.AllowedInController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.gis.geo.AllowedInController.failViewPage");
  }
  
}
