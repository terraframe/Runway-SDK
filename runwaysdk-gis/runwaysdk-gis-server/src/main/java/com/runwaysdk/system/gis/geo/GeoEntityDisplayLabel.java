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

import com.runwaysdk.business.MutableWithStructs;
import com.runwaysdk.business.ontology.OntologyStrategyIF;

public class GeoEntityDisplayLabel extends GeoEntityDisplayLabelBase
{
  private static final long serialVersionUID = 135222526;

  public GeoEntityDisplayLabel()
  {
    super();
  }

  public GeoEntityDisplayLabel(com.runwaysdk.business.MutableWithStructs entity, String structName)
  {
    super(entity, structName);
  }

  @Override
  public void apply()
  {
    if (this.isAppliedToDB())
    {
      MutableWithStructs parent = this.getParent();

      if (parent != null && parent instanceof GeoEntity)
      {
        OntologyStrategyIF strategy = GeoEntity.createStrategy();
        strategy.updateLabel((GeoEntity) parent, this.getValue());
      }
    }

    super.apply();
  }

}
