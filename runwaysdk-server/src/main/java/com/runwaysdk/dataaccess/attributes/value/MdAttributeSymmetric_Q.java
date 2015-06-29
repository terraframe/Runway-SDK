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

import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.dataaccess.MdAttributeSymmetricDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeSymmetricDAO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeSymmetric_Q extends MdAttributeEncryption_Q implements MdAttributeSymmetricDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -6753251722840272532L;

  /**
   * Used in value objects with attributes that contain values that are the result of functions, where the function result
   * data type does not match the datatype of the column.
   * @param mdAttributeIF metadata that defines the column.
   */
  public MdAttributeSymmetric_Q(MdAttributeSymmetricDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getType()
   */
  public String getType()
  {
    return MdAttributeSymmetricInfo.CLASS;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeSymmetricDAO getBusinessDAO()
  {
    this.unsupportedBusinessDAO();
    return null;
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    throw new UnsupportedOperationException();
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeSymmetricDAOIF.class.getName();
  }
}
