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
package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.constants.MdAttributeDecInfo;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.AttributeDecMdDTO;

public abstract class MdAttributeDecDAO extends MdAttributeNumberDAO implements MdAttributeDecDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 7497741664574818415L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeDecDAO()
  {
    super();
  }

  /**
   * @see 
   *      com.runwaysdk.dataaccess.metadata.MdAttributeNumber_E#MdAttributeNumber
   *      (Map<String, Attribute>, String)
   */
  public MdAttributeDecDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   *Returns the total maximum length of the decimal number (including number of
   * decimal places the right of the decimal).
   * 
   * @return total maximum length of the decimal number (including number of
   *         decimal places the right of the decimal).
   */
  public String getLength()
  {
    return this.getAttributeIF(MdAttributeDecInfo.LENGTH).getValue();
  }

  /**
   *Returns the number of decimal places to the right of the decimal.
   * 
   * @return number of decimal places to the right of the decimal.
   */
  public String getDecimal()
  {
    return this.getAttributeIF(MdAttributeDecInfo.DECIMAL).getValue();
  }

  /**
   * Returns the type of AttributeMdDTO this MdAttributeDec requires at the DTO
   * Layer.
   * 
   * @return class name of the AttributeMdDTO to represent this MdAttributeDec
   */
  @Override
  public String attributeMdDTOType()
  {
    return AttributeDecMdDTO.class.getName();
  }

  @Override
  protected void validate()
  {
    super.validate();

    if (this.getLength() != null)
    {
      int length = Integer.parseInt(this.getLength());

      if (length < 1)
      {
        String msg = "Precision on MdAttributeDec must be greater than 0";

        throw new AttributeDefinitionLengthException(msg, this, length);
      }
      else if (this.getDecimal() != null)
      {
        int decimal = Integer.parseInt(this.getDecimal());

        if (decimal < 1 || decimal > length)
        {
          String msg = "Precision on MdAttributeDec must be between [1] and [" + length + "]";

          throw new AttributeDefinitionDecimalException(msg, this, length, decimal);
        }
      }
    }
  }

}
