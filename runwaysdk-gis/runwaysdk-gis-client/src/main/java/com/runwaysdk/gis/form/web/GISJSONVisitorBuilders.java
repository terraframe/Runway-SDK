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
package com.runwaysdk.gis.form.web;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.form.web.JSONFormVisitor;
import com.runwaysdk.form.web.WebFormComponent;
import com.runwaysdk.form.web.JSONFormVisitor.WebFormComponentToJSON;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.gis.constants.MdWebPointInfo;
import com.runwaysdk.gis.form.web.field.WebPoint;
import com.vividsolutions.jts.geom.Point;

public class GISJSONVisitorBuilders implements JSONFormVisitor.PluginIF
{

  @Override
  public WebFormComponentToJSON getBuilder(WebFormComponent component, JSONFormVisitor visitor)
  {
    // TODO hash-map of creation objects if the if/else chain grows too large
    if(component instanceof WebPoint)
    {
      return new WebPointToJSON((WebPoint) component, visitor);
    }
    
    return null;
  }

  @Override
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }
  
  private class WebPointToJSON extends JSONFormVisitor.WebAttributeToJSON
  {

    @Override
    protected void initField(JSONObject obj) throws JSONException
    {
      super.initField(obj);
      
      // the value will be set as WKT but also set the raw x and y values
      WebPoint point = (WebPoint) this.getField();
      JSONFormVisitor visitor = this.getVisitor();
      
      Object x = null;
      Object y = null;
      
      Point value = point.convertToPoint();

      if(value != null)
      {
        x = value.getX();
        y = value.getY();
      }
      
      visitor.put(obj, MdWebPointInfo.X, x);
      visitor.put(obj, MdWebPointInfo.Y, y);
    }
    
    public WebPointToJSON(WebPoint webAttribute,
        JSONFormVisitor visitor)
    {
      // Note: the visitor is enclosing class instance
      visitor.super(webAttribute, visitor);
    }
    
  }

}
