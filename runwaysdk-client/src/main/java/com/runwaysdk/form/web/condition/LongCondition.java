/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.form.web.condition;

import com.runwaysdk.form.web.WebFormVisitor;
import com.runwaysdk.form.web.metadata.LongConditionMd;

public class LongCondition extends BasicCondition
{
  private Long value;

  protected LongCondition(LongConditionMd fMd)
  {
    super(fMd);
  }
  
  @Override
  public LongConditionMd getConditionMd()
  {
    return (LongConditionMd) super.getConditionMd();
  }

  public void setValue(Long value)
  {
    this.value = value;
  }

  @Override
  public String getValue()
  {
    return value.toString();
  }

  @Override
  public Long getObjectValue()
  {
    return value;
  }

  @Override
  public void accept(WebFormVisitor visitor)
  {
    visitor.visit(this);
  }

}
