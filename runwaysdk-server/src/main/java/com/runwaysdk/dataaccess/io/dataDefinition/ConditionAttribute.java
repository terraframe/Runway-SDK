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
/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.BasicConditionInfo;
import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.DateConditionInfo;
import com.runwaysdk.constants.DoubleConditionInfo;
import com.runwaysdk.constants.LongConditionInfo;
import com.runwaysdk.constants.MdFieldInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.system.AllOperation;
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
public class ConditionAttribute implements ConditionAttributeIF
{
  private String       tagName;

  private MdWebFormDAO mdForm;

  private MdFieldDAO   mdField;

  private String       operationName;

  private String       value;

  private String       fieldName;

  /**
   * @param tagName
   * @param attributes
   * @param mdField
   */
  public ConditionAttribute(String tagName, Attributes attributes, MdWebFormDAO mdForm, MdFieldDAO mdField)
  {
    this.tagName = tagName;
    this.mdForm = mdForm;
    this.mdField = mdField;
    this.operationName = attributes.getValue(XMLTags.OPERATION_TAG);
    this.value = attributes.getValue(XMLTags.VALUE_ATTRIBUTE);
    this.fieldName = attributes.getValue(XMLTags.FIELD_ATTRIBUTE);
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
    // <xs:element name="and" type="andCondition" />
    if (this.tagName.equals(XMLTags.AND_TAG))
    {
      throw new ProgrammingErrorException("And tags are not supported");
    }

    String operationId = this.getOperation();
    MdFieldDAOIF definingField = this.getDefiningField();

    FieldConditionDAO condition = this.getCondition();
    condition.setValue(BasicConditionInfo.DEFINING_MD_FIELD, definingField.getId());
    condition.setValue(BasicConditionInfo.OPERATION, operationId);
    condition.setValue(BasicConditionInfo.VALUE, value);
    condition.apply();

    // IMPORTANT: It is possible that the mdField cached in memory has become
    // stale because this field already had an existing condition which was
    // deleted.
    if (this.mdField != null)
    {
      BusinessDAO mdField = MdFieldDAO.get(this.mdField.getId()).getBusinessDAO();
      mdField.setValue(MdFieldInfo.FIELD_CONDITION, condition.getId());
      mdField.apply();
    }

    return condition;
  }

  /**
   * @return
   */
  private FieldConditionDAO getCondition()
  {
    String classType = this.getClassType();

    if (this.mdField != null)
    {
      String conditionId = this.mdField.getValue(MdFieldInfo.FIELD_CONDITION);

      if (conditionId != null && conditionId.length() > 0)
      {
        // In this case we must first delete the existing condition
        FieldConditionDAO.get(conditionId).getBusinessDAO().delete();
      }
    }

    return (FieldConditionDAO) BusinessDAO.newInstance(classType);
  }

  /**
   * @return
   */
  private String getClassType()
  {
    if (this.tagName.equals(XMLTags.DATE_TAG))
    {
      return DateConditionInfo.CLASS;
    }
    else if (this.tagName.equals(XMLTags.CHARACTER_TAG))
    {
      return CharacterConditionInfo.CLASS;
    }
    else if (this.tagName.equals(XMLTags.LONG_TAG))
    {
      return LongConditionInfo.CLASS;
    }
    else if (this.tagName.equals(XMLTags.DOUBLE_TAG))
    {
      return DoubleConditionInfo.CLASS;
    }

    throw new ProgrammingErrorException("Unable to handle the condition tag type [" + this.tagName + "]");
  }

  /**
   * @param operationName
   * @return
   */
  private String getOperation()
  {
    // Change to an everything caching algorithm
    if (operationName.equals(XMLTags.EQ_OPERATION))
    {
      return AllOperation.EQ.getId();
    }
    else if (operationName.equals(XMLTags.GT_OPERATION))
    {
      return AllOperation.GT.getId();
    }
    else if (operationName.equals(XMLTags.GTE_OPERATION))
    {
      return AllOperation.GTE.getId();
    }
    else if (operationName.equals(XMLTags.LT_OPERATION))
    {
      return AllOperation.LT.getId();
    }
    else if (operationName.equals(XMLTags.LTE_OPERATION))
    {
      return AllOperation.LTE.getId();
    }

    return AllOperation.NEQ.getId();
  }

  /**
   * @param fieldName
   * @return
   */
  private MdFieldDAOIF getDefiningField()
  {
    String key = MdFieldDAO.buildKey(this.mdForm.getKey(), fieldName);

    return (MdFieldDAOIF) MdFieldDAO.get(MdFieldInfo.CLASS, key);
  }
}
