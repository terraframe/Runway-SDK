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
package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeNumberInfo;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFactory;
import com.runwaysdk.transport.metadata.AttributeNumberMdDTO;

public abstract class MdAttributeNumberDAO extends MdAttributePrimitiveDAO implements MdAttributeNumberDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -3211518117970912613L;

  public MdAttributeNumberDAO()
  {
    super();
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttribute#MdAttribute(Map<String,
   *      Attribute>, String)
   */
  public MdAttributeNumberDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * Returns the value setting of whether or not negative numbers are rejected
   * by this attribute.
   * 
   * @return true if negative numbers are rejected, false otherwise.
   */
  public String isNegativeRejected()
  {
    return this.getAttributeIF(MdAttributeNumberInfo.REJECT_NEGATIVE).getValue();
  }

  /**
   * Returns the value setting of whether or not positive numbers are rejected
   * by this attribute.
   * 
   * @return true if positive numbers are rejected, false otherwise.
   */
  public String isPositiveRejected()
  {
    return this.getAttributeIF(MdAttributeNumberInfo.REJECT_POSITIVE).getValue();
  }

  /**
   * Returns the value setting of whether or not zero is rejected by this
   * attribute.
   * 
   * @return true if zero is rejected, false otherwise.
   */
  public String isZeroRejected()
  {
    return this.getAttributeIF(MdAttributeNumberInfo.REJECT_ZERO).getValue();
  }

  /**
   * Returns the type of AttributeMdDTO this MdAttribute requires at the DTO
   * Layer.
   * 
   * @return class name of the AttributeMdDTO to represent this MdAttribute
   */
  @Override
  public String attributeMdDTOType()
  {
    return AttributeNumberMdDTO.class.getName();
  }

  @Override
  public String save(boolean validateRequired)
  {
    this.validateStartRange();
    this.validateEndRange();

    return super.save(validateRequired);
  }

  @Override
  public Double getStartRange()
  {
    String value = this.getAttribute(MdAttributeNumberInfo.START_RANGE).getValue();
    if (value != null && value.length() > 0)
    {
      return Double.parseDouble(value);
    }

    return null;
  }

  @Override
  public Double getEndRange()
  {
    String value = this.getAttribute(MdAttributeNumberInfo.END_RANGE).getValue();
    if (value != null && value.length() > 0)
    {
      return Double.parseDouble(value);
    }

    return null;
  }

  private void validateStartRange()
  {
    this.validateSelfAttribute(MdAttributeNumberInfo.START_RANGE);
  }

  private void validateEndRange()
  {
    this.validateSelfAttribute(MdAttributeNumberInfo.END_RANGE);
  }

  private void validateSelfAttribute(String attributeName)
  {
    if (this.getAttribute(attributeName).isModified())
    {
      String value = this.getAttribute(attributeName).getValue();
      MdClassDAOIF definingMdClassDAOIF = this.definedByClass();

      Attribute spoofAttribute = AttributeFactory.createAttribute(this.getKey(), this.getType(), attributeName, definingMdClassDAOIF.definesType(), value);
      spoofAttribute.setContainingComponent(this);

      MdAttributeNumberDAO definingMdAttribute = (MdAttributeNumberDAO) this.getBusinessDAO();

      if (definingMdAttribute.hasAttribute(MdAttributeConcreteInfo.IMMUTABLE))
      {
        definingMdAttribute.setValue(MdAttributeConcreteInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      }

      spoofAttribute.validate(definingMdAttribute, value);
    }
  }
}
