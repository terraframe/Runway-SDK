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
package com.runwaysdk.gis.mapping.gwc;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.gis.constants.GeoserverProperties;
import com.runwaysdk.system.gis.ConnectionException;
import com.runwaysdk.system.gis.InvalidFormatException;
import com.runwaysdk.system.gis.mapping.GeoserverFacade;

public abstract class AbstractRequest implements Reloadable
{
  private static final String REST_SERVICE = "/gwc/rest/seed/";

  public static final int     SUCCESS      = 200;

  private String              url;

  private int                 zoomStart;

  private int                 zoomStop;

  private String              layer;

  private String              workspace;

  private int                 code;

  public AbstractRequest(String layer)
  {
    this.workspace = GeoserverProperties.getWorkspace();
    this.url = GeoserverProperties.getLocalPath() + REST_SERVICE + this.workspace + ":" + layer
        + ".json";
    this.zoomStart = GeoserverProperties.getZoomStart();
    this.zoomStop = GeoserverProperties.getZoomStop();
    this.layer = layer;
    this.code = -1;
  }

  /**
   * Returns the type of request.
   * 
   * @return
   */
  protected abstract String getType();

  public int getCode()
  {
    return this.code;
  }

  public String getURL()
  {
    return this.url;
  }

  public boolean doRequest()
  {
    HttpClient client = new HttpClient();

    URL u;
    try
    {
      u = new URL(this.url);
    }
    catch (MalformedURLException e)
    {
      throw new ConnectionException("Could not connect to [" + this.url + "]", e);
    }

    PostMethod post = new PostMethod(url);

    JSONObject seedRequest = new JSONObject();

    JSONObject content = new JSONObject();
    String type = this.getType();
    try
    {
      seedRequest.put("seedRequest", content);
      content.put("name", this.workspace);

      JSONObject bounds = new JSONObject();
      content.put("bounds", bounds);

      double[] bbox = GeoserverFacade.getBBOX(this.layer);
      JSONArray double_ = new JSONArray();
      double_.put(Double.toString(bbox[GeoserverFacade.MINX_INDEX]));
      double_.put(Double.toString(bbox[GeoserverFacade.MINY_INDEX]));
      double_.put(Double.toString(bbox[GeoserverFacade.MAXX_INDEX]));
      double_.put(Double.toString(bbox[GeoserverFacade.MAXY_INDEX]));

      JSONObject coords = new JSONObject();
      coords.put("double", double_);

      bounds.put("coords", coords);

      JSONObject number = new JSONObject();
      number.put("number", GeoserverFacade.SRS_CODE);
      content.put("srs", number);

      content.put("zoomStart", this.zoomStart);
      content.put("zoomStop", this.zoomStop);

      content.put("format", "image/png");

      content.put("type", type);

      content.put("threadCount", "1");

      String body = seedRequest.toString();

      RequestEntity entity = new StringRequestEntity(body, "application/json", "UTF-8");
      post.setRequestEntity(entity);
    }
    catch (JSONException e)
    {
      throw new InvalidFormatException("Error creating JSON for layer ["+this.layer+"] of request type ["+type+"].", e);
    }
    catch (UnsupportedEncodingException e)
    {
      throw new ConnectionException("Invalid encoding when connecting to ["+this.url+"].", e);
    }


    Credentials defaultcreds = new UsernamePasswordCredentials(GeoserverProperties.getAdminUser(),
        GeoserverProperties.getAdminPassword());
    client.getState().setCredentials(new AuthScope(u.getHost(), u.getPort()), defaultcreds);
    client.getParams().setAuthenticationPreemptive(true);

    try
    {
      this.code = client.executeMethod(post);
    }
    catch (Throwable t)
    {
      throw new ConnectionException("Error executing POST method to ["+this.url+"].", t);
    }

    return this.code == SUCCESS;
  }
}
