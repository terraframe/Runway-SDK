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

public class BefriendsController extends BefriendsControllerBase implements com.runwaysdk.generation.loader.
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/jstest/Befriends/";
  public static final String LAYOUT = "WEB-INF/templates/layout.jsp";
  
  private static final long serialVersionUID = 1871158599;
  
  public BefriendsController(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }
  
  public void cancel(com.runwaysdk.jstest.BefriendsDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    dto.unlock();
    this.view(dto.getOid());
  }
  public void failCancel(com.runwaysdk.jstest.BefriendsDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.edit(dto.getOid());
  }
  public void childQuery(java.lang.String childOid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.jstest.BefriendsQueryDTO query = com.runwaysdk.jstest.BefriendsDTO.childQuery(clientRequest, childOid);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failChildQuery(java.lang.String childOid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void create(com.runwaysdk.jstest.BefriendsDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    try
    {
      dto.apply();
      this.view(dto.getOid());
    }
    catch(com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failCreate(dto);
    }
  }
  public void failCreate(com.runwaysdk.jstest.BefriendsDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void delete(com.runwaysdk.jstest.BefriendsDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    try
    {
      dto.delete();
      this.viewAll();
    }
    catch(com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failDelete(dto);
    }
  }
  public void failDelete(com.runwaysdk.jstest.BefriendsDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void edit(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.jstest.BefriendsDTO dto = com.runwaysdk.jstest.BefriendsDTO.lock(super.getClientRequest(), oid);
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void failEdit(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.view(oid);
  }
  public void newInstance(java.lang.String parentOid, java.lang.String childOid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.jstest.BefriendsDTO dto = new com.runwaysdk.jstest.BefriendsDTO(clientRequest, parentOid, childOid);
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void failNewInstance(java.lang.String parentOid, java.lang.String childOid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.viewAll();
  }
  public void newRelationship() throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("parentList", com.runwaysdk.jstest.TestClassDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("childList", com.runwaysdk.jstest.RefClassDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    render("newRelationshipComponent.jsp");
  }
  public void failNewRelationship() throws java.io.IOException, jakarta.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void parentQuery(java.lang.String parentOid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.jstest.BefriendsQueryDTO query = com.runwaysdk.jstest.BefriendsDTO.parentQuery(clientRequest, parentOid);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failParentQuery(java.lang.String parentOid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void update(com.runwaysdk.jstest.BefriendsDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    try
    {
      dto.apply();
      this.view(dto.getOid());
    }
    catch(com.runwaysdk.ProblemExceptionDTO e)
    {
      this.failUpdate(dto);
    }
  }
  public void failUpdate(com.runwaysdk.jstest.BefriendsDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void view(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("item", com.runwaysdk.jstest.BefriendsDTO.get(clientRequest, oid));
    render("viewComponent.jsp");
  }
  public void failView(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.viewAll();
  }
  public void viewAll() throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.jstest.BefriendsQueryDTO query = com.runwaysdk.jstest.BefriendsDTO.getAllInstances(clientRequest, null, true, 20, 1);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failViewAll() throws java.io.IOException, jakarta.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.jstest.BefriendsQueryDTO query = com.runwaysdk.jstest.BefriendsDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, jakarta.servlet.ServletException
  {
    resp.sendError(500);
  }
}
