/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.form.web.condition;

import com.runwaysdk.form.web.WebFormComponent;
import com.runwaysdk.form.web.WebFormVisitor;
import com.runwaysdk.form.web.metadata.ConditionMd;

public abstract class Condition implements WebFormComponent
{
  protected String conditionId;
  protected ConditionMd conditionMd;
  protected String operation;
  protected String definingMdFieldId;
  
  public Condition(ConditionMd fMd)
  {
    super();
    this.setConditionMd(fMd);
  }
  
  /**
   * @return the conditionId
   */
  public String getOid()
  {
    return conditionId;
  }

  /**
   * @param conditionId the conditionId to set
   */
  public void setOid(String conditionId)
  {
    this.conditionId = conditionId;
  }
  
  public String getOperation()
  {
    return this.operation;
  }

  public void setOperation(String operation)
  {
    this.operation = operation;
  }
  
  public void setDefiningMdField(String fieldId)
  {
    this.definingMdFieldId = fieldId;
  }
  
  public String getDefiningMdField()
  {
    return this.definingMdFieldId;
  }

  private void setConditionMd(ConditionMd conditionMd)
  {
    this.conditionMd = conditionMd;
  }

  public ConditionMd getConditionMd()
  {
    return conditionMd;
  }
  
  public abstract void accept(WebFormVisitor visitor);
}
