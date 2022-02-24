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
package com.runwaysdk.gis.form.web.field;

import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.form.web.field.WebField;
import com.runwaysdk.form.web.field.FieldBuilders.PluginIF;
import com.runwaysdk.form.web.field.FieldBuilders.WebFieldBuilder;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.gis.constants.MdWebPointInfo;
import com.runwaysdk.gis.form.web.metadata.WebPointMd;
import com.runwaysdk.gis.form.web.metadata.WebPointMdBuilder;
import com.runwaysdk.gis.transport.attributes.AttributePointDTO;
import com.runwaysdk.system.metadata.MdFormDTO;
import com.runwaysdk.system.metadata.MdWebFieldDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;

public class GISFieldBuilders implements PluginIF
{
  private static Map<String, WebFieldBuilder> builders = new HashMap<String, WebFieldBuilder>();
  
  static {
    builders.put(MdWebPointInfo.CLASS, new WebPointBuilder());
  }
  
  @Override
  public WebFieldBuilder getBuilder(String type)
  {
    return builders.get(type);
  }

  @Override
  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }
  
  private static class WebPointBuilder extends WebFieldBuilder
  {
    public WebPointBuilder()
    {
      super(new WebPointMdBuilder());
    }
    
    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data,
        Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebPointMd fMd = (WebPointMd) this.getMdBuilder().create(mdForm, mdField);
      WebPoint f = new WebPoint(fMd);
      
      AttributePointDTO attr = (AttributePointDTO) mdIdToAttrDTOs.get(fMd
          .getDefiningMdAttribute());
      
      this.init(attr, f, mdField);
      
      return f;
    }
    
  }

}
