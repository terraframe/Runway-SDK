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

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.transport.metadata.AttributeDateMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeDate_Q extends MdAttributeMoment_Q implements MdAttributeDateDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 4767729926606158605L;

  /**
   * Used in value objects with attributes that contain values that are the result of functions, where the function result
   * data type does not match the datatype of the column.
   * @param mdAttributeIF metadata that defines the column.
   */
  public MdAttributeDate_Q(MdAttributeConcreteDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getType()
   */
  public String getType()
  {
    return MdAttributeDateInfo.CLASS;
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.MdAttributeDateDAOIF#getFormat()
   */
  @Override
  public String getFormat()
  {
    return Constants.DATE_FORMAT;
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeDateMdDTO.class.getName();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeDateDAO getBusinessDAO()
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
