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
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestBodyResponse;
import com.runwaysdk.mvc.RestResponse;
import com.runwaysdk.system.ontology.TermUtilDTO;
import com.runwaysdk.transport.conversion.json.BusinessDTOToJSON;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;

@Controller(url = "geo-entity")
public class GeoEntityController
{
  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF create(ClientRequestIF request, GeoEntityDTO dto, String parentId, String relationshipType) throws JSONException
  {
    GeoEntityViewDTO view = GeoEntityDTO.create(request, dto, parentId, relationshipType);

    return new RestBodyResponse(new JSONReturnObject(BusinessDTOToJSON.getConverter(view).populate()));
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF update(ClientRequestIF request, GeoEntityDTO dto, String parentId, String relationshipType) throws JSONException
  {
    dto.apply();

    // TODO : create an MdMethod on GeoEntity that returns this boolean.
    Boolean canCreateChildren = getUniversals(request, null, dto.getOid(), LocatedInDTO.CLASS).size() != 0;

    GeoEntityViewDTO view = new GeoEntityViewDTO(request);
    view.setCanCreateChildren(canCreateChildren);
    view.setGeoEntityId(dto.getOid());
    view.setUniversalDisplayLabel(dto.getUniversal().getDisplayLabel().getValue());
    view.setGeoEntityDisplayLabel(dto.getDisplayLabel().getValue());

    return new RestBodyResponse(new JSONReturnObject(BusinessDTOToJSON.getConverter(view).populate()));
  }

  @Endpoint(url = "direct-descendants", method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF getDirectDescendants(ClientRequestIF request, String parentId, String[] relationshipTypes, Integer pageNum, Integer pageSize) throws JSONException
  {
    JSONArray array = new JSONArray();

    GeoEntityViewDTO[] views = GeoEntityDTO.getDirectDescendants(request, parentId, relationshipTypes, pageNum, pageSize);

    JSONObject page = new JSONObject();

    if (pageNum != null && pageSize != null && pageNum > 0 && pageSize > 0)
    {
      int startIndex = Math.max(0, ( ( pageNum - 1 ) * pageSize ));
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

    return new RestBodyResponse(new JSONReturnObject(page));
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF delete(ClientRequestIF request, GeoEntityDTO dto)
  {
    dto.delete();

    return new RestResponse();
  }

  private List<TermDTO> getUniversals(ClientRequestIF request, GeoEntityDTO dto, String parentId, String relationshipType)
  {
    relationshipType = AllowedInDTO.CLASS;

    // If this is a root geo entity, then the only possible universal is its
    // current universal
    if (dto != null && ( parentId == null || parentId.equals(GeoEntityDTO.getRoot(request).getOid()) ))
    {
      return Arrays.asList(new TermDTO[] { dto.getUniversal() });
    }

    GeoEntityDTO parentGeo = GeoEntityDTO.get(request, parentId);

    return Arrays.asList(TermUtilDTO.getAllDescendants(request, parentGeo.getUniversalOid(), new String[] { relationshipType }));
  }

  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF cancel(ClientRequestIF request, GeoEntityDTO dto)
  {
    dto.unlock();

    return new RestResponse();
  }
}
