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

import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.query.ValueQuery;

public class MdAttributeCharacter_SQL extends MdAttributePrimitive_SQL implements MdAttributeCharacterDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = -1523110204501547499L;

  /**
   * Used to spoof metadata for writing SQL directly to the {@link ValueQuery} API.
   *
   * @param attributeName
   * @param displayLabel
   */
  public MdAttributeCharacter_SQL(String attributeName, String displayLabel)
  {
    super(attributeName, displayLabel);
  }

  @Override
  public String getType()
  {
    return MdAttributeCharacterInfo.CLASS;
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

  /**
   * @throws UnsupportedOperationException
   */
  public String getSize()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  /**
   * @throws UnsupportedOperationException
   */
  public MdAttributeReferenceDAOIF convertToReference()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }
  
  /**
   * @throws UnsupportedOperationException
   */
  public MdAttributeReferenceDAOIF convertToReference(MdBusinessDAOIF mdReferenecedBusinessDAOIF)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }
  
  /**
   *
   * @return
   */
  public MdAttributeCharacterDAO getBusinessDAO()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

}
