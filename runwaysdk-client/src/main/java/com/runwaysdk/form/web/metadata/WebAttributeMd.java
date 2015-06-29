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
package com.runwaysdk.form.web.metadata;

public abstract class WebAttributeMd extends WebFieldMd
{
  private String  definingMdAttribute;

  private String  definingClass;

  private String  definingAttribute;

  private Boolean showOnSearch;

  private Boolean showOnViewAll;

  protected WebAttributeMd()
  {
    super();
    this.definingMdAttribute = null;
    this.definingClass = null;
    this.definingAttribute = null;
  }

  protected void setDefiningAttribute(String definingAttribute)
  {
    this.definingAttribute = definingAttribute;
  }

  protected void setDefiningClass(String definingClass)
  {
    this.definingClass = definingClass;
  }

  public String getDefiningAttribute()
  {
    return definingAttribute;
  }

  public String getDefiningClass()
  {
    return definingClass;
  }

  public String getDefiningMdAttribute()
  {
    return this.definingMdAttribute;
  }

  protected void setDefiningMdAttribute(String id)
  {
    this.definingMdAttribute = id;
  }

  protected void setShowOnSearch(Boolean showOnSearch)
  {
    this.showOnSearch = showOnSearch;
  }

  public Boolean getShowOnSearch()
  {
    return showOnSearch;
  }

  protected void setShowOnViewAll(Boolean showOnViewAll)
  {
    this.showOnViewAll = showOnViewAll;
  }

  public Boolean getShowOnViewAll()
  {
    return showOnViewAll;
  }
}
