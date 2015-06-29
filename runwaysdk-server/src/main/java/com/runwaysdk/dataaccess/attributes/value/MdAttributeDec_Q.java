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
package com.runwaysdk.dataaccess.attributes.value;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.transport.metadata.AttributeDecMdDTO;

public abstract class MdAttributeDec_Q extends MdAttributeNumber_Q implements MdAttributeDecDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 8700723727181975556L;

  /**
   * Used in value objects with attributes that contain values that are the result of functions, where the function result
   * data type does not match the datatype of the column.
   * @param mdAttributeIF metadata that defines the column.
   */
  public MdAttributeDec_Q(MdAttributeConcreteDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }

  /**
   * Returns the type of AttributeMdDTO this MdAttributeDec requires at the DTO Layer.
   *
   * @return class name of the AttributeMdDTO to represent this MdAttributeDec
   */
  @Override
  public String attributeMdDTOType()
  {
    return AttributeDecMdDTO.class.getName();
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.MdAttributeDecDAOIF#getDecimal()
   */
  public String getDecimal()
  {
    if (this.mdAttributeConcreteIF instanceof MdAttributeDecDAOIF)
    {
      return ((MdAttributeDecDAOIF)this.mdAttributeConcreteIF).getDecimal();
    }
    else
    {
      return "10";
    }
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.MdAttributeDecDAOIF#getLength()
   */
  public String getLength()
  {
    if (this.mdAttributeConcreteIF instanceof MdAttributeDecDAOIF)
    {
      return ((MdAttributeDecDAOIF)this.mdAttributeConcreteIF).getLength();
    }
    else
    {
      return "4";
    }
  }

}
