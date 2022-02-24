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

import com.runwaysdk.constants.MdAttributeGraphReferenceInfo;
import com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeGraphReferenceDAO;
import com.runwaysdk.transport.metadata.AttributeUUIDMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeUUIDMdSession;

public class MdAttributeGraphReference_Q extends MdAttributeConcrete_Q implements MdAttributeGraphReferenceDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -3458978442226203956L;

  /**
   * Used in value objects with attributes that contain values that are the
   * result of functions, where the function result data type does not match the
   * datatype of the column.
   * 
   * @param mdAttributeConcreteIF
   *          metadata that defines the column.
   */
  public MdAttributeGraphReference_Q(MdAttributeGraphReferenceDAOIF mdAttributeConcreteIF)
  {
    super(mdAttributeConcreteIF);
  }

  @Override
  public AttributeMdSession getAttributeMdSession()
  {
    return new AttributeUUIDMdSession();
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeUUIDMdDTO.class.getName();
  }

  @Override
  public String getType()
  {
    return MdAttributeGraphReferenceInfo.CLASS;
  }

  @Override
  public MdClassDAOIF getReferenceMdVertexDAOIF()
  {
    return ( (MdAttributeGraphReferenceDAOIF) this.mdAttributeConcreteIF ).getReferenceMdVertexDAOIF();
  }

  @Override
  public MdAttributeGraphReferenceDAO getBusinessDAO()
  {
    return ( (MdAttributeGraphReferenceDAOIF) this.mdAttributeConcreteIF ).getBusinessDAO();
  }

}
