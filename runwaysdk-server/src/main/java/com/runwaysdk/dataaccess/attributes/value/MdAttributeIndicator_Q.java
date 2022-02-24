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

import com.runwaysdk.constants.MdAttributeIndicatorInfo;
import com.runwaysdk.dataaccess.IndicatorElementDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.transport.metadata.AttributeIndicatorMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeIndicatorMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeIndicator_Q extends MdAttributeConcrete_Q implements MdAttributeIndicatorDAOIF
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -3458978442226203956L;
  
  private String javaType;
  
  /**
   * Used in value objects with attributes that contain values that are the
   * result of functions, where the function result data type does not match the
   * datatype of the column.
   * 
   * @param mdAttributeConcreteIF
   *          metadata that defines the column.
   */
  public MdAttributeIndicator_Q(MdAttributeIndicatorDAOIF mdAttributeConcreteIF)
  {
    super(mdAttributeConcreteIF);
    
    this.javaType = ((MdAttributeIndicatorDAOIF)this.mdAttributeConcreteIF).javaType(false);
  }

  @Override
  public AttributeMdSession getAttributeMdSession()
  {
    return new AttributeIndicatorMdSession(this.javaType);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeIndicatorMdDTO.class.getName();
  }

  @Override
  public String getType()
  {
    return MdAttributeIndicatorInfo.CLASS;
  }

  
  /**
   * Returns the {@link IndicatorElementDAOIF} object.
   * 
   * @return the {@link IndicatorElementDAOIF} object.
   */
  public IndicatorElementDAOIF getIndicator()
  {
    return ((MdAttributeIndicatorDAOIF)this.mdAttributeConcreteIF).getIndicator();
  }
}
