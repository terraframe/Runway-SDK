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

import com.runwaysdk.form.field.FieldMdIF;

public abstract class WebFieldMd implements FieldMdIF
{
  protected String definingMdForm;
  private String displayLabel;
  private String fieldName;
  private Integer fieldOrder;
  private String id;
  private Boolean required;
  private String description;
  
  protected WebFieldMd()
  {
    super();
    this.definingMdForm = null;
    this.displayLabel = null;
    this.description = null;
    this.fieldName = null;
    this.fieldOrder = null;
    this.id = null;
    this.required = null;
  }
  
  protected void setRequired(Boolean required)
  {
    this.required = required;
  }
  
  protected void setId(String id)
  {
    this.id = id;
  }

  protected void setDefiningMdForm(String definingMdForm)
  {
    this.definingMdForm = definingMdForm;
  }
  
  protected void setDisplayLabel(String displayLabel)
  {
    this.displayLabel = displayLabel;
  }
  
  protected void setFieldName(String fieldName)
  {
    this.fieldName = fieldName;
  }
  
  protected void setFieldOrder(Integer fieldOrder)
  {
    this.fieldOrder = fieldOrder;
  }
  
  @Override
  public String getDefiningMdForm()
  {
    return definingMdForm;
  }
  
  @Override
  public String getDisplayLabel()
  {
    return this.displayLabel;
  }
  
  @Override
  public String getFieldName()
  {
    return fieldName;
  }
  
  @Override
  public Integer getFieldOrder()
  {
    return fieldOrder;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public Boolean isRequired()
  {
    return this.required;
  }
  
  public String getDescription()
  {
    return description;
  }

  protected void setDescription(String description)
  {
    this.description = description;
  }

}
