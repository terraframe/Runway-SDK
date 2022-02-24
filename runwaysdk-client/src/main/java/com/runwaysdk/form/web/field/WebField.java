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
package com.runwaysdk.form.web.field;

import com.runwaysdk.form.field.FieldIF;
import com.runwaysdk.form.web.WebFormVisitor;
import com.runwaysdk.form.web.condition.Condition;
import com.runwaysdk.form.web.metadata.WebFieldMd;

public abstract class WebField implements FieldIF
{
  private WebFieldMd fieldMd;
  private Object value;
  private Boolean readable;
  private Boolean writable;
  private Boolean modified;
  private String type;
  private Condition condition;
  
  public WebField(WebFieldMd fieldMd)
  {
    super();
    this.fieldMd = fieldMd;
    this.writable = false;
    this.readable = false;
    this.value = null;
    this.modified = false;
    this.type = null;
    this.condition = null;
  }
  
  public String getFieldName()
  {
    return getFieldMd().getFieldName();
  }
  
  @Override
  public Boolean isModified()
  {
    return this.modified; 
  }
  
  @Override
  public WebFieldMd getFieldMd()
  {
    return this.fieldMd;
  }

  /**
   * Returns the toString() of the object value.
   */
  @Override
  public String getValue()
  {
    return this.value != null ? this.value.toString() : null;
  }
  
  @Override
  public Object getObjectValue()
  {
    return this.value;
  }
  
  @Override
  public void setValue(Object value)
  {
    this.value = value;
    this.setModified(true);
  }
  
  @Override
  public void setValue(String value)
  {
    // upcast to avoid infinite recursion
    this.setValue((Object) value);
  }
  
  @Override
  public Boolean isReadable()
  {
    return this.readable;
  }
  
  @Override
  public String getType()
  {
    return this.type;
  }
  
  protected void setType(String type)
  {
    this.type = type;
  }
  
  public Condition getCondition()
  {
    return this.condition;
  }

  protected void setCondition(Condition condition)
  {
    this.condition = condition;
  }
  
  protected boolean hasCondition()
  {
    return this.condition != null;
  }

  protected void setModified(Boolean modified)
  {
    this.modified = modified;
  }
  
  protected void setReadable(Boolean readable)
  {
    this.readable = readable;
  }
  
  @Override
  public Boolean isWritable()
  {
    return this.writable;
  }
  
  protected void setWritable(Boolean writable)
  {
    this.writable = writable;
  }

  public void accept(WebFormVisitor visitor)
  {
    if (hasCondition())
    {
      this.getCondition().accept(visitor);
    }
  }
}
