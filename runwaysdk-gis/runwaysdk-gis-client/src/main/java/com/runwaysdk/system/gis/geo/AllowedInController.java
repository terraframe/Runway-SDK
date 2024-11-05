/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

public class AllowedInController extends AllowedInControllerBase
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/system/gis/geo/AllowedIn/";
  public static final String LAYOUT = "WEB-INF/templates/layout.jsp";
  
  public AllowedInController(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }
  
  public void cancel(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    dto.unlock();
    this.view(dto.getOid());
  }
  public void failCancel(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.edit(dto.getOid());
  }
  public void childQuery(java.lang.String childOid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.system.gis.geo.AllowedInQueryDTO query = com.runwaysdk.system.gis.geo.AllowedInDTO.childQuery(clientRequest, childOid);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failChildQuery(java.lang.String childOid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void create(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
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
  public void failCreate(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void delete(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
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
  public void failDelete(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void edit(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.system.gis.geo.AllowedInDTO dto = com.runwaysdk.system.gis.geo.AllowedInDTO.lock(super.getClientRequest(), oid);
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
    com.runwaysdk.system.gis.geo.AllowedInDTO dto = new com.runwaysdk.system.gis.geo.AllowedInDTO(clientRequest, parentOid, childOid);
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void failNewInstance(java.lang.String parentOid, java.lang.String childOid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.viewAll();
  }
  public void newRelationship() throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("parentList", com.runwaysdk.system.gis.geo.UniversalDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    req.setAttribute("childList", com.runwaysdk.system.gis.geo.UniversalDTO.getAllInstances(super.getClientSession().getRequest(), "keyName", true, 0, 0).getResultSet());
    render("newRelationshipComponent.jsp");
  }
  public void failNewRelationship() throws java.io.IOException, jakarta.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void parentQuery(java.lang.String parentOid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.system.gis.geo.AllowedInQueryDTO query = com.runwaysdk.system.gis.geo.AllowedInDTO.parentQuery(clientRequest, parentOid);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failParentQuery(java.lang.String parentOid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void update(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
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
  public void failUpdate(com.runwaysdk.system.gis.geo.AllowedInDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void view(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("item", com.runwaysdk.system.gis.geo.AllowedInDTO.get(clientRequest, oid));
    render("viewComponent.jsp");
  }
  public void failView(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.viewAll();
  }
  public void viewAll() throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.system.gis.geo.AllowedInQueryDTO query = com.runwaysdk.system.gis.geo.AllowedInDTO.getAllInstances(clientRequest, null, true, 20, 1);
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
    com.runwaysdk.system.gis.geo.AllowedInQueryDTO query = com.runwaysdk.system.gis.geo.AllowedInDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, jakarta.servlet.ServletException
  {
    resp.sendError(500);
  }
}
