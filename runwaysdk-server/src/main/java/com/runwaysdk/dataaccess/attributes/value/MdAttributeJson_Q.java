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

import com.runwaysdk.constants.MdAttributeJsonInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeJsonDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeJsonDAO;
import com.runwaysdk.transport.metadata.AttributeJsonMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeJson_Q extends MdAttributeConcrete_Q implements MdAttributeJsonDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -5816297244090548904L;

  /**
   * Used in value objects with attributes that contain values that are the result of functions, where the function result
   * data type does not match the datatype of the column.
   * @param mdAttributeIF metadata that defines the column.
   */
  public MdAttributeJson_Q(MdAttributeConcreteDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }
  
  /**
   *
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getType()
   */
  public String getType()
  {
    return MdAttributeJsonInfo.CLASS;
  }
  
  
  /**
   * Returns the java class object for the attribute type.
   * 
   * @return the java class object for the attribute type.
   */
  public Class<?> javaClass()
  {
    return String.class;
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeJsonMdDTO.class.getName();
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  @Override
  public MdAttributeJsonDAO getBusinessDAO()
  {
    this.unsupportedBusinessDAO();
    return null;
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() {
    throw new UnsupportedOperationException();
  }
}
