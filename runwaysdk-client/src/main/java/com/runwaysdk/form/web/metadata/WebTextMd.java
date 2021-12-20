/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.form.web.metadata;


public class WebTextMd extends WebPrimitiveMd
{
  private Integer width;
  private Integer height;
  
  protected WebTextMd()
  {
    super();
    
    this.width = null;
    this.height = null;
  }
  
  public Integer getWidth()
  {
    return width;
  }
  
  public Integer getHeight()
  {
    return height;
  }
  
  protected void setWidth(Integer width)
  {
    this.width = width;
  }
  
  protected void setHeight(Integer height)
  {
    this.height = height;
  }


}
