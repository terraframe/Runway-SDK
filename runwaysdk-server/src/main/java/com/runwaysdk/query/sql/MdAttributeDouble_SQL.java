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
package com.runwaysdk.query.sql;

import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.query.ValueQuery;

public class MdAttributeDouble_SQL extends MdAttributeDec_SQL implements MdAttributeDoubleDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = -1068718839741887498L;

  /**
   * Used to spoof metadata for writing SQL directly to the {@link ValueQuery} API.
   *
   * @param attributeName
   * @param displayLabel
   */
  public MdAttributeDouble_SQL(String attributeName, String displayLabel)
  {
    super(attributeName, displayLabel);
  }

  @Override
  public String getType()
  {
    return MdAttributeDoubleInfo.CLASS;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeDoubleDAO getBusinessDAO()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

}
