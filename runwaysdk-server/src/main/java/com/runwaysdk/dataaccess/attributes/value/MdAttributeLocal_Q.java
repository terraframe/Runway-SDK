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

import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalDAO;

public abstract class MdAttributeLocal_Q extends MdAttributeStruct_Q implements MdAttributeLocalDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -7232600849321565180L;

  /**
   * Used in value objects with attributes that contain values that are the result of functions, where the function result
   * data type does not match the datatype of the column.
   * @param mdAttributeIF metadata that defines the column.
   */
  public MdAttributeLocal_Q(MdAttributeStructDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }
  /**
   * Returns the MdStruct that defines the class used to store the values of the struct attribute.
   *
   * @return the MdStruct that defines the class used to store the values of the struct attribute.
   */
  public MdLocalStructDAOIF getMdStructDAOIF()
  {
    return (MdLocalStructDAOIF)super.getMdStructDAOIF();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeLocalDAO getBusinessDAO()
  {
    this.unsupportedBusinessDAO();
    return null;
  }
}
