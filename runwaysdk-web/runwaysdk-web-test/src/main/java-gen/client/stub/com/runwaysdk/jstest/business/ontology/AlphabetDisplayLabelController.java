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

public class AlphabetDisplayLabelController extends AlphabetDisplayLabelControllerBase implements com.runwaysdk.generation.loader.
{
  public static final String JSP_DIR = "/WEB-INF/com/runwaysdk/jstest/business/ontology/AlphabetDisplayLabel/";
  public static final String LAYOUT = "WEB-INF/templates/layout.jsp";
  
  public AlphabetDisplayLabelController(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }
  
  public void cancel(com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.view(dto.getOid());
  }
  public void failCancel(com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.edit(dto.getOid());
  }
  public void create(com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
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
  public void failCreate(com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void delete(com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
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
  public void failDelete(com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void edit(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO dto = com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO.get(super.getClientRequest(), oid);
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void failEdit(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.view(oid);
  }
  public void newInstance() throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO dto = new com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO(clientRequest);
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void failNewInstance() throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.viewAll();
  }
  public void update(com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
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
  public void failUpdate(com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void view(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("item", com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO.get(clientRequest, oid));
    render("viewComponent.jsp");
  }
  public void failView(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.viewAll();
  }
  public void viewAll() throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelQueryDTO query = com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO.getAllInstances(clientRequest, null, true, 20, 1);
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
    com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelQueryDTO query = com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, jakarta.servlet.ServletException
  {
    resp.sendError(500);
  }
}
