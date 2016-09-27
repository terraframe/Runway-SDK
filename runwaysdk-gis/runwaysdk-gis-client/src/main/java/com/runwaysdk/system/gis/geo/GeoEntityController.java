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

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.controller.ErrorUtility;
import com.runwaysdk.system.ontology.TermUtilDTO;
import com.runwaysdk.transport.conversion.json.BusinessDTOToJSON;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;

public class GeoEntityController extends GeoEntityControllerBase
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/system/gis/geo/GeoEntity/";

  public static final String LAYOUT  = "WEB-INF/templates/layout.jsp";

  public GeoEntityController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }

  public void create(com.runwaysdk.system.gis.geo.GeoEntityDTO dto, String parentId, String relationshipType) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      GeoEntityViewDTO view = GeoEntityDTO.create(super.getClientRequest(), dto, parentId, relationshipType);

      this.resp.getWriter().print(new JSONReturnObject(BusinessDTOToJSON.getConverter(view).populate()).toString());
    }
    catch (Throwable t)
    {
      boolean needsRedirect = ErrorUtility.handleFormError(t, req, resp);

      if (needsRedirect)
      {
        this.failCreate(dto, parentId, relationshipType);
      }
    }
  }

  @Override
  public void update(com.runwaysdk.system.gis.geo.GeoEntityDTO dto, String parentId, String relationshipType) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.apply();

      // TODO : create an MdMethod on GeoEntity that returns this boolean.
      Boolean canCreateChildren = getUniversals(null, dto.getId(), LocatedInDTO.CLASS).size() != 0;

      GeoEntityViewDTO view = new GeoEntityViewDTO(getClientRequest());
      view.setCanCreateChildren(canCreateChildren);
      view.setGeoEntityId(dto.getId());
      view.setUniversalDisplayLabel(dto.getUniversal().getDisplayLabel().getValue());
      view.setGeoEntityDisplayLabel(dto.getDisplayLabel().getValue());

      this.resp.getWriter().print(new JSONReturnObject(BusinessDTOToJSON.getConverter(view).populate()).toString());
    }
    catch (Throwable t)
    {
      boolean needsRedirect = ErrorUtility.handleFormError(t, req, resp);

      if (needsRedirect)
      {
        this.viewUpdate(dto.getId(), parentId);
      }
    }
  }

  @Override
  public void getDirectDescendants(String parentId, String[] relationshipTypes, Integer pageNum, Integer pageSize) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      JSONArray array = new JSONArray();

      // List<TermAndRelDTO> tnrs =
      // getClientRequest().getTermAllChildren(parentId, pageNum, pageSize);
      // for (TermAndRelDTO tnr : tnrs) {
      // GeoEntityDTO geo = (GeoEntityDTO) tnr.getTerm();
      // Boolean canCreate =
      // getClientRequest().getTermAllChildren(geo.getUniversal().getId(), 0,
      // 2).size() != 0;
      //
      // GeoEntityViewDTO view = new GeoEntityViewDTO(getClientRequest());
      // view.setCanCreateChildren(canCreate);
      // view.setGeoEntityId(geo.getId());
      // view.setUniversalDisplayLabel(geo.getUniversal().getDisplayLabel().getValue());
      // view.setGeoEntityDisplayLabel(geo.getDisplayLabel().getValue());
      // view.setRelationshipId(tnr.getRelationshipId());
      // view.setRelationshipType(tnr.getRelationshipType());
      //
      // array.put(BusinessDTOToJSON.getConverter(view).populate());
      // }

      GeoEntityViewDTO[] views = GeoEntityDTO.getDirectDescendants(getClientRequest(), parentId, relationshipTypes, pageNum, pageSize);

      JSONObject page = new JSONObject();

      if (pageNum != null && pageSize != null && pageNum > 0 && pageSize > 0)
      {
        int startIndex = Math.max(0, (( pageNum - 1 ) * pageSize));
        int endIndex = Math.min( ( pageNum * pageSize ), views.length);
        int maxPages = ( (int) views.length / pageSize ) + 1;

        for (int i = startIndex; i < endIndex; i++)
        {
          GeoEntityViewDTO view = views[i];

          array.put(BusinessDTOToJSON.getConverter(view).populate());
        }

        page.put("pageNumber", pageNum);
        page.put("maxPages", maxPages);
      }
      else
      {
        for (GeoEntityViewDTO view : views)
        {
          array.put(BusinessDTOToJSON.getConverter(view).populate());
        }

        page.put("pageNumber", pageNum);
        page.put("maxPages", 0);
      }

      page.put("values", array);

      resp.getWriter().print(new JSONReturnObject(page).toString());
    }
    catch (Throwable t)
    {
      ErrorUtility.prepareAjaxThrowable(t, resp);
    }
  }

  public void delete(com.runwaysdk.system.gis.geo.GeoEntityDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    try
    {
      dto.delete();
    }
    catch (Throwable t)
    {
      ErrorUtility.prepareAjaxThrowable(t, resp);
    }
  }

  @Override
  public void viewCreate(String parentId, String relationshipType) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.system.gis.geo.GeoEntityDTO dto = new com.runwaysdk.system.gis.geo.GeoEntityDTO(super.getClientRequest());

    req.setAttribute("_universal", this.getUniversals(dto, parentId, relationshipType));
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  private List<TermDTO> getUniversals(GeoEntityDTO dto, String parentId, String relationshipType)
  {
    relationshipType = AllowedInDTO.CLASS;

    // If this is a root geo entity, then the only possible universal is its
    // current universal
    if (dto != null && ( parentId == null || parentId.equals(GeoEntityDTO.getRoot(this.getClientRequest()).getId()) ))
    {
      return Arrays.asList(new TermDTO[] { dto.getUniversal() });
    }

    GeoEntityDTO parentGeo = GeoEntityDTO.get(this.getClientRequest(), parentId);

    return Arrays.asList(TermUtilDTO.getAllDescendants(this.getClientRequest(), parentGeo.getUniversalId(), new String[] { relationshipType }));
  }

  @Override
  public void failCreate(com.runwaysdk.system.gis.geo.GeoEntityDTO dto, String parentId, String relationshipType) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("_universal", this.getUniversals(dto, parentId, relationshipType));
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }

  public void failDelete(com.runwaysdk.system.gis.geo.GeoEntityDTO dto, String parentId, String relationshipType) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("_universal", this.getUniversals(dto, parentId, relationshipType));
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    // this.view(id);
  }

  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  @Override
  public void viewUpdate(java.lang.String id, String parentId) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.system.gis.geo.GeoEntityDTO dto = com.runwaysdk.system.gis.geo.GeoEntityDTO.lock(super.getClientRequest(), id);
    req.setAttribute("_universal", this.getUniversals(dto, parentId, null));
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  @Override
  public void failUpdate(com.runwaysdk.system.gis.geo.GeoEntityDTO dto, String parentId, String relationshipType) throws java.io.IOException, javax.servlet.ServletException
  {
    req.setAttribute("_universal", this.getUniversals(dto, null, null));
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }

  public void view(java.lang.String id, String parentId, String relationshipType) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    GeoEntityDTO dto = com.runwaysdk.system.gis.geo.GeoEntityDTO.get(clientRequest, id);
    req.setAttribute("_universal", this.getUniversals(dto, parentId, relationshipType));
    req.setAttribute("item", dto);
    render("viewComponent.jsp");
  }

  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    this.viewAll();
  }

  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.system.gis.geo.GeoEntityQueryDTO query = com.runwaysdk.system.gis.geo.GeoEntityDTO.getAllInstances(clientRequest, null, true, 20, 1);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.system.gis.geo.GeoEntityQueryDTO query = com.runwaysdk.system.gis.geo.GeoEntityDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }

  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    resp.sendError(500);
  }

  public void cancel(com.runwaysdk.system.gis.geo.GeoEntityDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    dto.unlock();
    // this.view(dto.getId());
  }
}
