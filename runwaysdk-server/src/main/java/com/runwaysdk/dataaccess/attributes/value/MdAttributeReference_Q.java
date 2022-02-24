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

import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.transport.metadata.AttributeReferenceMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeReference_Q extends MdAttributeConcrete_Q implements MdAttributeReferenceDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 9056981636003531984L;

  /**
   * Used in value objects with attributes that contain values that are the result of functions, where the function result
   * data type does not match the datatype of the column.
   * @param mdAttributeIF metadata that defines the column.
   */
  public MdAttributeReference_Q(MdAttributeReferenceDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }

  public MdBusinessDAOIF getReferenceMdBusinessDAO()
  {
    return ((MdAttributeReferenceDAOIF)this.mdAttributeConcreteIF).getReferenceMdBusinessDAO();
  }

  /**
   * Special case for generating an oid getter for reference attributes.
   *
   * @return reference oid getter.
   */
  public String generatedServerGetterRefId()
  {
    return ((MdAttributeReferenceDAOIF)this.mdAttributeConcreteIF).generatedServerGetterRefId();
  }

  /**
   * Special case for generating an oid getter for reference attributes.
   *
   * @return reference oid getter.
   */
  public String generatedClientGetterRefId()
  {
    return ((MdAttributeReferenceDAOIF)this.mdAttributeConcreteIF).generatedClientGetterRefId();
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getType()
   */
  public String getType()
  {
    return MdAttributeReferenceInfo.CLASS;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeReferenceDAO getBusinessDAO()
  {
    this.unsupportedBusinessDAO();
    return null;
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeReferenceMdDTO.class.getName();
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() {
    throw new UnsupportedOperationException();
  }
}
