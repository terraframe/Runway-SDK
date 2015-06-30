/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.query.ValueQuery;

public class MdAttributeInteger_SQL extends MdAttributeInt_SQL implements MdAttributeIntegerDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = 1632326446600676229L;

  /**
   * Used to spoof metadata for writing SQL directly to the {@link ValueQuery} API.
   *
   * @param attributeName
   * @param displayLabel
   */
  public MdAttributeInteger_SQL(String attributeName, String displayLabel)
  {
    super(attributeName, displayLabel);
  }

  @Override
  public String getType()
  {
    return MdAttributeIntegerInfo.CLASS;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeIntegerDAO getBusinessDAO()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

}
