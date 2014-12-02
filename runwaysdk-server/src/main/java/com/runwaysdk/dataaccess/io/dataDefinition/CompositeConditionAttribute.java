/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import com.runwaysdk.constants.CompositeFieldConditionInfo;
import com.runwaysdk.constants.MdFieldInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.AndFieldConditionDAO;
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
public class CompositeConditionAttribute implements ConditionAttributeIF, ConditionListIF
{
  private String               tagName;

  private MdFieldDAO           mdField;

  private ImportManager        manager;

  private ConditionAttributeIF firstCondition;

  private ConditionAttributeIF secondCondition;

  /**
   * @param tagName
   * @param attributes
   * @param mdField
   */
  public CompositeConditionAttribute(String tagName, MdFieldDAO mdField, ImportManager manager)
  {
    this.tagName = tagName;
    this.mdField = mdField;
    this.manager = manager;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.dataDefinition.ConditionListIF#addCondition
   * (com.runwaysdk.dataaccess.io.dataDefinition.ConditionAttributeIF)
   */
  @Override
  public void addCondition(ConditionAttributeIF condition)
  {
    if(condition instanceof NoneConditionAttribute)
    {
      throw new ProgrammingErrorException("None conditions cannot be added to a composite condition");
    }
    
    if (this.firstCondition == null)
    {
      this.firstCondition = condition;
    }
    else if (this.secondCondition == null)
    {
      this.secondCondition = condition;
    }
    else
    {
      // COMPOSITE CONDITIONS ONLY SUPPORTS TWO CONDITIONS
      throw new ProgrammingErrorException("Composite conditions only support having two conditions");
    }
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
    if (!this.tagName.equals(XMLTags.AND_TAG))
    {
      throw new ProgrammingErrorException("And tags are not supported");
    }

    if (this.firstCondition == null || this.secondCondition == null)
    {
      throw new ProgrammingErrorException("Composite conditions must have two conditions defined on it");
    }

    FieldConditionDAO firstFieldCondition = this.firstCondition.process();
    FieldConditionDAO secondFieldCondition = this.secondCondition.process();

    AndFieldConditionDAO condition = AndFieldConditionDAO.newInstance();
    condition.setValue(CompositeFieldConditionInfo.FIRST_CONDITION, firstFieldCondition.getId());
    condition.setValue(CompositeFieldConditionInfo.SECOND_CONDITION, secondFieldCondition.getId());
    condition.apply();

    if (manager.isUpdateState())
    {
      String conditionId = this.mdField.getValue(MdFieldInfo.FIELD_CONDITION);

      if (conditionId != null && conditionId.length() > 0)
      {
        // In this case we must first delete the existing condition
        FieldConditionDAO.get(conditionId).getBusinessDAO().delete();
      }
    }

    // IMPORTANT: It is possible that the mdField cached in memory has become
    // stale because this field already had an existing condition which was
    // deleted.
    BusinessDAO mdField = MdFieldDAO.get(this.mdField.getId()).getBusinessDAO();
    mdField.setValue(MdFieldInfo.FIELD_CONDITION, condition.getId());
    mdField.apply();

    return condition;
  }

}
