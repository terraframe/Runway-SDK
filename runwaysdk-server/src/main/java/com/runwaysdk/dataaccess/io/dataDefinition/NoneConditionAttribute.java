/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import com.runwaysdk.constants.MdFieldInfo;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.system.metadata.FieldConditionDAO;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class NoneConditionAttribute implements ConditionAttributeIF
{
  private MdFieldDAO mdField;

  /**
   * @param mdField
   */
  public NoneConditionAttribute(MdFieldDAO mdField)
  {
    this.mdField = mdField;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.ConditionAttributeIF#process()
   */
  @Override
  public FieldConditionDAO process()
  {
    if (this.mdField != null)
    {
      String conditionId = this.mdField.getValue(MdFieldInfo.FIELD_CONDITION);

      if (conditionId != null && conditionId.length() > 0)
      {
        // In this case we must first delete the existing condition
        FieldConditionDAO.get(conditionId).getBusinessDAO().delete();
      }
    }

    return null;
  }

}
