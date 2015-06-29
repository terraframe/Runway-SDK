/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.transport.conversion.dom;

import org.w3c.dom.Element;

import com.runwaysdk.business.RunwayProblemDTO;

public abstract class DocToRunwayProblemDTO
{
  private Element rootElement;
  
  public DocToRunwayProblemDTO(Element rootElement)
  {
    super();
    this.rootElement = rootElement;
  }

  protected Element getRootElement()
  {
    return this.rootElement;
  }
  
  public abstract RunwayProblemDTO populate();
}
