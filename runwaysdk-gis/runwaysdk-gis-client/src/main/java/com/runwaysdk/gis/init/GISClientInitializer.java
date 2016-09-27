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
package com.runwaysdk.gis.init;

import com.runwaysdk.controller.ParameterFactory;
import com.runwaysdk.form.web.JSONFormVisitor;
import com.runwaysdk.form.web.field.FieldBuilders;
import com.runwaysdk.format.StandardFormat;
import com.runwaysdk.gis.controller.GISParameterFactory;
import com.runwaysdk.gis.form.web.GISJSONVisitorBuilders;
import com.runwaysdk.gis.form.web.field.GISFieldBuilders;
import com.runwaysdk.gis.format.GISFormatFactory;
import com.runwaysdk.gis.transport.attributes.GISAttributeDTOFactory;
import com.runwaysdk.gis.transport.metadata.GISAttributeMdDTOFactory;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.runwaysdk.transport.metadata.AttributeMdDTOFactory;
import com.runwaysdk.util.ClientInitializerIF;

public class GISClientInitializer implements ClientInitializerIF
{
  private static boolean initialized = false;
  
  public synchronized static void init()
  {
    if(initialized)
    {
      return;
    }
    
    // Register FieldBuilders factory
    FieldBuilders.registerPlugin(new GISFieldBuilders());
    
    // Register Field-to-JSON builders
    JSONFormVisitor.registerPlugin(new GISJSONVisitorBuilders());
    
    // Register entity attribute factory
    AttributeDTOFactory.registerPlugin(new GISAttributeDTOFactory());
    
    // Register entity attribute metadata factory
    AttributeMdDTOFactory.registerPlugin(new GISAttributeMdDTOFactory());
    
    // Register servlet scrapper
    StandardFormat.registerPlugin(new GISFormatFactory());
    
    ParameterFactory.registerPlugin(new GISParameterFactory());
    
    initialized = true;
  }
}
