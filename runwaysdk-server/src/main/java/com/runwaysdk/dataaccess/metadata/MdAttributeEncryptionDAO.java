/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.constants.MdAttributeEncryptionInfo;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO;

public abstract class MdAttributeEncryptionDAO extends MdAttributeConcreteDAO implements MdAttributeEncryptionDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 2355715259082465802L;

  public MdAttributeEncryptionDAO()
  {
    super();
  }

  /**
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeEncryptionDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * Returns the default value for the attribute that this metadata defines. If
   * no default value has been defined, an empty string is returned.
   *
   * @return the default value for the attribute that this metadata defines.
   */
  public String getDefaultValue()
  {
    return getDefaultValue(this.getAttributeIF(MdAttributeEncryptionInfo.DEFAULT_VALUE).getValue());
  }

  /**
   * Returns a string representing the query attribute class for attributes of this type.
   *
   * @return string representing the query attribute class for attributes of this type.
   */
  public String queryAttributeClass()
  {
    String errMsg = "Methood "+this.getClass().getName()+".queryAttributeClass() should never be called.";
    throw new ProgrammingErrorException(errMsg);
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
