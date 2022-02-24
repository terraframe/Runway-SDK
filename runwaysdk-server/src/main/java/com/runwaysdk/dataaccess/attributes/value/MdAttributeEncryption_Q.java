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

import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;
import com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO;

public abstract class MdAttributeEncryption_Q extends MdAttributeConcrete_Q implements MdAttributeEncryptionDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -8851317559718954245L;

  /**
   * Used in value objects with attributes that contain values that are the result of functions, where the function result
   * data type does not match the datatype of the column.
   * @param mdAttributeIF metadata that defines the column.
   */
  public MdAttributeEncryption_Q(MdAttributeEncryptionDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }

  public String getEncryptionMethod()
  {
    return ((MdAttributeEncryptionDAOIF)this.mdAttributeConcreteIF).getEncryptionMethod();
  }

  /**
   * Returns the type of AttributeMdDTO this MdAttributeEncryption requires at the DTO Layer.
   *
   * @return class name of the AttributeMdDTO to represent this MdAttributeEncryption
   */
  @Override
  public String attributeMdDTOType()
  {
    return AttributeEncryptionMdDTO.class.getName();
  }

}
