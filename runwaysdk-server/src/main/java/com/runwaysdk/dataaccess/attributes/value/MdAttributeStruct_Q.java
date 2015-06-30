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
package com.runwaysdk.dataaccess.attributes.value;

import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.transport.metadata.AttributeStructMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeStruct_Q extends MdAttributeConcrete_Q implements MdAttributeStructDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -1440868510852850644L;

  /**
   * Used in value objects with attributes that contain values that are the result of functions, where the function result
   * data type does not match the datatype of the column.
   * @param mdAttributeIF metadata that defines the column.
   */
  public MdAttributeStruct_Q(MdAttributeStructDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }

  public MdStructDAOIF getMdStructDAOIF()
  {
    return ((MdAttributeStructDAO)this.mdAttributeConcreteIF).getMdStructDAOIF();
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getType()
   */
  public String getType()
  {
    return MdAttributeStructInfo.CLASS;
  }

  /**
   * Returns the type of AttributeMdDTO this MdAttributeStruct requires at the DTO Layer.
   *
   * @return class name of the AttributeMdDTO to represent this MdAttributeStruct
   */
  @Override
  public String attributeMdDTOType()
  {
    return AttributeStructMdDTO.class.getName();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeStructDAO getBusinessDAO()
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
