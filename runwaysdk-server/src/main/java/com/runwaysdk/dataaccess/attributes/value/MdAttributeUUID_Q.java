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

import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeUUIDDAO;
import com.runwaysdk.transport.metadata.AttributeUUIDMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeUUID_Q extends MdAttributePrimitive_Q implements MdAttributeUUIDDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 952303035986154985L;

  /**
   * Used in value objects with attributes that contain values that are the
   * result of functions, where the function result data type does not match the
   * datatype of the column.
   * 
   * @param mdAttributeIF
   *          metadata that defines the column.
   */
  public MdAttributeUUID_Q(MdAttributeConcreteDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeUUIDMdDTO.class.getName();
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getType()
   */
  public String getType()
  {
    return MdAttributeUUIDInfo.CLASS;
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
   * Returns the concrete attribute representing this attribute. This is a
   * concrete attribute so itself is returned.
   *
   * @return concrete attribute representing this attribute.
   */
  public MdAttributeUUIDDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeUUIDDAOIF) super.getMdAttributeConcrete();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeUUIDDAO getBusinessDAO()
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
  
  /**
   * Precondition: assumes this character attribute is an OID. The collection of
   * <code>AttributeDAO</code> objects do not have their containing reference updated to
   * the returned <code>MdAttributeReferenceDAO</code> 
   */
  public MdAttributeReferenceDAOIF convertToReference()
  {
    return ((MdAttributeUUIDDAO)mdAttributeConcreteIF).convertToReference();
  }
  
  /**
   * This is used by the query API to allow for parent ids and child ids of relationships to
   * be used in queries.
   * 
   * Precondition: assumes this character attribute is an OID. The collection of
   * <code>AttributeDAO</code> objects do not have their containing reference updated to
   * the returned <code>MdAttributeReferenceDAO</code> 
   * 
   * @param the code>MdBusinessDAOIF</code> of the referenced type in the relationship.
   */
  public MdAttributeReferenceDAOIF convertToReference(MdBusinessDAOIF mdReferenecedBusinessDAOIF)
  {
    return ((MdAttributeUUIDDAO)mdAttributeConcreteIF).convertToReference(mdReferenecedBusinessDAOIF);
  }
}
