/**
 * 
 */
package com.runwaysdk.dataaccess.attributes.value;

import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.transport.metadata.AttributeMultiReferenceMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

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
public class MdAttributeMultiReference_Q extends MdAttributeConcrete_Q implements MdAttributeMultiReferenceDAOIF
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
  public MdAttributeMultiReference_Q(MdAttributeMultiReferenceDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getType()
   */
  public String getType()
  {
    return MdAttributeMultiReferenceInfo.CLASS;
  }

  public MdBusinessDAOIF getReferenceMdBusinessDAO()
  {
    return ( (MdAttributeMultiReferenceDAOIF) this.mdAttributeConcreteIF ).getReferenceMdBusinessDAO();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF#getTableName()
   */
  @Override
  public String getTableName()
  {
    return ( (MdAttributeMultiReferenceDAOIF) this.mdAttributeConcreteIF ).getTableName();
  }

  /**
   * Returns the type of AttributeMdDTO this MdAttributeMultiReference requires
   * at the DTO Layer.
   * 
   * @return class name of the AttributeMdDTO to represent this
   *         MdAttributeMultiReference
   */
  @Override
  public String attributeMdDTOType()
  {
    return AttributeMultiReferenceMdDTO.class.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeMultiReferenceDAO getBusinessDAO()
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