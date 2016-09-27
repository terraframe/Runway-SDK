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

import org.json.JSONArray;

import com.runwaysdk.business.ontology.TermAndRelDTO;
import com.runwaysdk.controller.ErrorUtility;
import com.runwaysdk.system.ontology.TermUtilDTO;
import com.runwaysdk.transport.conversion.json.BusinessDTOToJSON;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;

public class SynonymController extends SynonymControllerBase
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/system/gis/geo/Synonym/";
  public static final String LAYOUT = "WEB-INF/templates/layout.jsp";
  
  public SynonymController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }
  
  public void cancel(com.runwaysdk.system.gis.geo.SynonymDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    dto.unlock();
    this.view(dto.getId());
  }
  public void failCancel(com.runwaysdk.system.gis.geo.SynonymDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    this.edit(dto.getId());
  }
  public void create(com.runwaysdk.system.gis.geo.SynonymDTO dto, java.lang.String geoId) throws java.io.IOException, javax.servlet.ServletException
  {
    try {
      TermAndRelDTO tnr = SynonymDTO.create(this.getClientRequest(), dto, geoId);
      
      this.resp.getWriter().print(new JSONReturnObject(tnr.toJSON()).toString());
    }
    catch (Throwable t) {
      ErrorUtility.handleFormError(t, req, resp);
    }
  }
  public void failCreate(com.runwaysdk.system.gis.geo.SynonymDTO dto, java.lang.String geoId) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void delete(com.runwaysdk.system.gis.geo.SynonymDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.delete();
    }
    catch(Throwable t)
    {
      ErrorUtility.prepareAjaxThrowable(t, resp);
    }
  }
  public void failDelete(com.runwaysdk.system.gis.geo.SynonymDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void getDirectDescendants(java.lang.String parentId) throws java.io.IOException, javax.servlet.ServletException
  {
    try {
      JSONArray array = new JSONArray();
      
      TermAndRelDTO[] tnrs = TermUtilDTO.getDirectDescendants(getClientRequest(), parentId, new String[]{SynonymRelationshipDTO.CLASS});
      for (TermAndRelDTO tnr : tnrs) {
        array.put(tnr.toJSON());
      }
      
      resp.getWriter().print(new JSONReturnObject(array).toString());
    }
    catch(Throwable t) {
      ErrorUtility.prepareAjaxThrowable(t, resp);
    }
  }
  public void failGetDirectDescendants(java.lang.String parentId) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void update(com.runwaysdk.system.gis.geo.SynonymDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();
      
      String ret = BusinessDTOToJSON.getConverter(dto).populate().toString();
      this.resp.getWriter().print(new JSONReturnObject(ret).toString());
    }
    catch(Throwable t)
    {
      boolean needsRedirect = ErrorUtility.handleFormError(t, req, resp);

      if (needsRedirect)
      {
        this.viewUpdate(dto.getId());
      }
    }
  }
  public void failUpdate(com.runwaysdk.system.gis.geo.SynonymDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("item", com.runwaysdk.system.gis.geo.SynonymDTO.get(clientRequest, id));
    render("viewComponent.jsp");
  }
  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }
  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.system.gis.geo.SynonymQueryDTO query = com.runwaysdk.system.gis.geo.SynonymDTO.getAllInstances(clientRequest, null, true, 20, 1);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void viewCreate() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.system.gis.geo.SynonymDTO dto = new com.runwaysdk.system.gis.geo.SynonymDTO(clientRequest);
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void failViewCreate() throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.system.gis.geo.SynonymQueryDTO query = com.runwaysdk.system.gis.geo.SynonymDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void viewUpdate(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.system.gis.geo.SynonymDTO dto = com.runwaysdk.system.gis.geo.SynonymDTO.lock(super.getClientRequest(), id);
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void failViewUpdate(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }
}
