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
package ${package};

public class HelloWorldController extends HelloWorldControllerBase implements com.runwaysdk.generation.loader.
{
  public static final String pakge = "${package}";
  public static final String JSP_DIR = "/WEB-INF/" + pakge.replace(".", "/") + "/HelloWorld/";
  public static final String LAYOUT = "WEB-INF/templates/layout.jsp";
  
  private static final long serialVersionUID = 893027364;
  
  public HelloWorldController(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    super(req, resp, isAsynchronous, JSP_DIR, LAYOUT);
  }
  
  public void cancel(${package}.HelloWorldDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    dto.unlock();
    this.view(dto.getOid());
  }
  public void failCancel(${package}.HelloWorldDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.edit(dto.getOid());
  }
  public void create(${package}.HelloWorldDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
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
  public void failCreate(${package}.HelloWorldDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void delete(${package}.HelloWorldDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
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
  public void failDelete(${package}.HelloWorldDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void edit(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    ${package}.HelloWorldDTO dto = ${package}.HelloWorldDTO.lock(super.getClientRequest(), oid);
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
    ${package}.HelloWorldDTO dto = new ${package}.HelloWorldDTO(clientRequest);
    req.setAttribute("item", dto);
    render("createComponent.jsp");
  }
  public void failNewInstance() throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.viewAll();
  }
  public void update(${package}.HelloWorldDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
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
  public void failUpdate(${package}.HelloWorldDTO dto) throws java.io.IOException, jakarta.servlet.ServletException
  {
    req.setAttribute("item", dto);
    render("editComponent.jsp");
  }
  public void view(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    req.setAttribute("item", ${package}.HelloWorldDTO.get(clientRequest, oid));
    render("viewComponent.jsp");
  }
  public void failView(java.lang.String oid) throws java.io.IOException, jakarta.servlet.ServletException
  {
    this.viewAll();
  }
  public void viewAll() throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    ${package}.HelloWorldQueryDTO query = ${package}.HelloWorldDTO.getAllInstances(clientRequest, null, true, 20, 1);
    req.setAttribute("query", query);
    
    req.setAttribute("helloWorld", HelloWorldDTO.generateRandom(clientRequest));
    
    render("viewAllComponent.jsp");
  }
  public void failViewAll() throws java.io.IOException, jakarta.servlet.ServletException
  {
    resp.sendError(500);
  }
  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, jakarta.servlet.ServletException
  {
    com.runwaysdk.constants.ClientRequestIF clientRequest = super.getClientRequest();
    ${package}.HelloWorldQueryDTO query = ${package}.HelloWorldDTO.getAllInstances(clientRequest, sortAttribute, isAscending, pageSize, pageNumber);
    req.setAttribute("query", query);
    render("viewAllComponent.jsp");
  }
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, jakarta.servlet.ServletException
  {
    resp.sendError(500);
  }
}
