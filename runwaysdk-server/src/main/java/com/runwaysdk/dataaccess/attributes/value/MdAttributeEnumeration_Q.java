/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.dataaccess.attributes.value;

import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeEnumeration_Q extends MdAttributeConcrete_Q implements MdAttributeEnumerationDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = 7909519469644895172L;

  /**
   * Used in value objects with attributes that contain values that are the
   * result of functions, where the function result data type does not match the
   * datatype of the column.
   * 
   * @param mdAttributeIF
   *          metadata that defines the column.
   */
  public MdAttributeEnumeration_Q(MdAttributeEnumerationDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getType()
   */
  public String getType()
  {
    return MdAttributeEnumerationInfo.CLASS;
  }

  public String getCacheColumnName()
  {
    return ( (MdAttributeEnumerationDAOIF) this.mdAttributeConcreteIF ).getCacheColumnName();
  }

  public String getDefinedCacheColumnName()
  {
    return ( (MdAttributeEnumerationDAOIF) this.mdAttributeConcreteIF ).getDefinedCacheColumnName();
  }

  public MdEnumerationDAOIF getMdEnumerationDAO()
  {
    return ( (MdAttributeEnumerationDAOIF) this.mdAttributeConcreteIF ).getMdEnumerationDAO();
  }

  public boolean selectMultiple()
  {
    return ( (MdAttributeEnumerationDAOIF) this.mdAttributeConcreteIF ).selectMultiple();
  }

  public MdBusinessDAOIF getReferenceMdBusinessDAO()
  {
    return ( (MdAttributeEnumerationDAOIF) this.mdAttributeConcreteIF ).getReferenceMdBusinessDAO();
  }

  /**
   * Returns the type of AttributeMdDTO this MdAttributeEnumeration requires at
   * the DTO Layer.
   * 
   * @return class name of the AttributeMdDTO to represent this
   *         MdAttributeEnumeration
   */
  @Override
  public String attributeMdDTOType()
  {
    return AttributeEnumerationMdDTO.class.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeEnumerationDAO getBusinessDAO()
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

}
