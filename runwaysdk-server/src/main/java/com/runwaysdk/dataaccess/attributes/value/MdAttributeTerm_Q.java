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
/**
 * 
 */
package com.runwaysdk.dataaccess.attributes.value;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.transport.metadata.AttributeTermMdDTO;

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
public class MdAttributeTerm_Q extends MdAttributeReference_Q implements MdAttributeTermDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 4351396996853205171L;

  /**
   * Used in value objects with attributes that contain values that are the
   * result of functions, where the function result data type does not match the
   * datatype of the column.
   * 
   * @param mdAttributeIF
   *          metadata that defines the column.
   */
  public MdAttributeTerm_Q(MdAttributeTermDAOIF mdAttributeIF)
  {
    super(mdAttributeIF);
  }

  public MdTermDAOIF getReferenceMdBusinessDAO()
  {
    return (MdTermDAOIF) super.getReferenceMdBusinessDAO();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getType()
   */
  public String getType()
  {
    return MdAttributeTermInfo.CLASS;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeTermDAO getBusinessDAO()
  {
    return (MdAttributeTermDAO) super.getBusinessDAO();
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributeTermMdDTO.class.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.MdAttributeTermDAOIF#addAttributeRoot(com.runwaysdk
   * .dataaccess.BusinessDAO, java.lang.Boolean)
   */
  @Override
  public void addAttributeRoot(BusinessDAO term, Boolean selectable)
  {
    // DO NOTHING
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeTermDAOIF#getAllAttributeRoots()
   */
  @Override
  public List<RelationshipDAOIF> getAllAttributeRoots()
  {
    return new LinkedList<RelationshipDAOIF>();
  }
}
