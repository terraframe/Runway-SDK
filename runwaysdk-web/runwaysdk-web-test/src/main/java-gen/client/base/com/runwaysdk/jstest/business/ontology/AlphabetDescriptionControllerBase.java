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
package com.runwaysdk.jstest.business.ontology;

@com.runwaysdk.business.ClassSignature(hash = -147226281)
public class AlphabetDescriptionControllerBase implements com.runwaysdk.generation.loader.
{
  public static final String CLASS = "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController";
  protected jakarta.servlet.http.HttpServletRequest req;
  protected jakarta.servlet.http.HttpServletResponse resp;
  protected java.lang.Boolean isAsynchronous;
  protected java.lang.String dir;
  protected java.lang.String layout;
  
  public AlphabetDescriptionControllerBase(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    this(req, resp, isAsynchronous, "","");
  }
  
  public AlphabetDescriptionControllerBase(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous, java.lang.String dir, java.lang.String layout)
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
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO:dto", post=true)
  public void cancel(com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.cancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO:dto", post=true)
  public void failCancel(com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.failCancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO:dto", post=true)
  public void create(com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.create");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO:dto", post=true)
  public void failCreate(com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.failCreate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO:dto", post=true)
  public void delete(com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.delete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO:dto", post=true)
  public void failDelete(com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.failDelete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:oid", post=false)
  public void edit(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.edit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:oid", post=false)
  public void failEdit(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.failEdit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void newInstance() throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.newInstance");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failNewInstance() throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.failNewInstance");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO:dto", post=true)
  public void update(com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.update");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO:dto", post=true)
  public void failUpdate(com.runwaysdk.jstest.business.ontology.AlphabetDescriptionDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.failUpdate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:oid", post=false)
  public void view(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.view");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:oid", post=false)
  public void failView(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.failView");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void viewAll() throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.viewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failViewAll() throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.failViewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.Boolean:isAscending, java.lang.Integer:pageSize, java.lang.Integer:pageNumber", post=false)
  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.viewPage");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.String:isAscending, java.lang.String:pageSize, java.lang.String:pageNumber", post=false)
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, jakarta.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.failViewPage");
  }
  
}
