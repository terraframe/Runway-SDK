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

import com.runwaysdk.form.web.metadata.ConditionMd;

public abstract class CompositeFieldCondition extends Condition
{
  protected Condition firstCondition;
  protected Condition secondCondition;
  
  public CompositeFieldCondition(ConditionMd fMd)
  {
    super(fMd);
    this.firstCondition = null;
    this.secondCondition = null;
  }

  /**
   * @return the firstCondition
   */
  public Condition getFirstCondition()
  {
    return firstCondition;
  }

  /**
   * @param firstCondition the firstCondition to set
   */
  public void setFirstCondition(Condition firstCondition)
  {
    this.firstCondition = firstCondition;
  }

  /**
   * @return the secondCondition
   */
  public Condition getSecondCondition()
  {
    return secondCondition;
  }

  /**
   * @param secondCondition the secondCondition to set
   */
  public void setSecondCondition(Condition secondCondition)
  {
    this.secondCondition = secondCondition;
  }


}
