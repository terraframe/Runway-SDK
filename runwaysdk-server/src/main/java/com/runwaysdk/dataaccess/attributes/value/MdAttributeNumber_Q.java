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
package com.runwaysdk.dataaccess.attributes.value;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.transport.metadata.AttributeNumberMdDTO;

public abstract class MdAttributeNumber_Q extends MdAttributePrimitive_Q implements MdAttributeNumberDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -2250145320133504331L;

  /**
   * Used in value objects with attributes that contain values that are the
   * result of functions, where the function result data type does not match the
   * datatype of the column.
   * 
   * @param mdAttributeIF
   *          metadata that defines the column.
   */
  public MdAttributeNumber_Q(MdAttributeConcreteDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeNumberDAOIF#isNegativeRejected()
   */
  public String isNegativeRejected()
  {
    if (this.mdAttributeConcreteIF instanceof MdAttributeNumberDAOIF)
    {
      return ( (MdAttributeNumberDAOIF) this.mdAttributeConcreteIF ).isNegativeRejected();
    }
    else
    {
      return "";
    }
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeNumberDAOIF#isPositiveRejected()
   */
  public String isPositiveRejected()
  {
    if (this.mdAttributeConcreteIF instanceof MdAttributeNumberDAOIF)
    {
      return ( (MdAttributeNumberDAOIF) this.mdAttributeConcreteIF ).isPositiveRejected();
    }
    else
    {
      return "";
    }
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeNumberDAOIF#isZeroRejected()
   */
  public String isZeroRejected()
  {
    if (this.mdAttributeConcreteIF instanceof MdAttributeNumberDAOIF)
    {
      return ( (MdAttributeNumberDAOIF) this.mdAttributeConcreteIF ).isZeroRejected();
    }
    else
    {
      return "";
    }
  }

  @Override
  public Double getStartRange()
  {
    if (this.mdAttributeConcreteIF instanceof MdAttributeNumberDAOIF)
    {
      return ( (MdAttributeNumberDAOIF) this.mdAttributeConcreteIF ).getStartRange();
    }

    return null;
  }

  @Override
  public Double getEndRange()
  {
    if (this.mdAttributeConcreteIF instanceof MdAttributeNumberDAOIF)
    {
      return ( (MdAttributeNumberDAOIF) this.mdAttributeConcreteIF ).getEndRange();
    }

    return null;
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

}
