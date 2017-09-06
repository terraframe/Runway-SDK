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
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.constants.MdAttributeConcreteInfo;

public class MdAttributeIndicator_T extends MdAttributeConcrete_T
{

  /**
   * 
   */
  private static final long serialVersionUID = 8410913086961615052L;

  /**
   * @param mdAttribute
   */
  public MdAttributeIndicator_T(MdAttributeIndicatorDAO mdAttribute)
  {
    super(mdAttribute);
  } 
  
  protected void preSaveValidate()
  {
    super.preSaveValidate();
    
    if (this.getMdAttribute().isNew() && !this.appliedToDB)
    {
      // Supply a default value, as it does not make sense to have a column name for an attribute defined on an external table.
      this.getMdAttribute().getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValue("n_a");
    }
  }
  
}
