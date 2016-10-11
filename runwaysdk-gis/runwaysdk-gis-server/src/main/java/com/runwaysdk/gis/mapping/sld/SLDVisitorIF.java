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
package com.runwaysdk.gis.mapping.sld;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.system.gis.mapping.AndRule;
import com.runwaysdk.system.gis.mapping.ExactRule;
import com.runwaysdk.system.gis.mapping.GreaterThanOrEqualRule;
import com.runwaysdk.system.gis.mapping.GreaterThanRule;
import com.runwaysdk.system.gis.mapping.Layer;
import com.runwaysdk.system.gis.mapping.LayerStyle;
import com.runwaysdk.system.gis.mapping.LessThanOrEqualRule;
import com.runwaysdk.system.gis.mapping.LessThanRule;
import com.runwaysdk.system.gis.mapping.OrRule;
import com.runwaysdk.system.gis.mapping.ThematicAttribute;

public interface SLDVisitorIF extends Reloadable
{
  public void visit(LayerStyle style, SLDVisitable visitable);

  public void visit(Layer layer);
  
  public void visit(ThematicAttribute thematicAttribute);
  
  public void visit(AndRule andRule);
  
  public void visit(OrRule orRule);
  
  public void visit(ExactRule exactRule, SLDVisitable visitable);
  
  public void visit(GreaterThanOrEqualRule gtorRule, SLDVisitable visitable);
  
  public void visit(GreaterThanRule gtRule, SLDVisitable visitable);
  
  public void visit(LessThanOrEqualRule ltoeRule, SLDVisitable visitable);
  
  public void visit(LessThanRule ltRule, SLDVisitable visitable);
  
  public String getSLD();
}
