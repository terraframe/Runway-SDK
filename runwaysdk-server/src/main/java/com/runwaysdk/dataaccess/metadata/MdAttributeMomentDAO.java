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

import java.util.Date;
import java.util.Map;

import com.runwaysdk.dataaccess.MdAttributeMomentDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public abstract class MdAttributeMomentDAO extends MdAttributePrimitiveDAO implements MdAttributeMomentDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -8905709236935670112L;


  public MdAttributeMomentDAO()
  {
    super();
  }


  public MdAttributeMomentDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * Returns the date format used based on the properties files.
   *
   * @return
   */
  public abstract String getFormat();

  /**
   * Called for java class generation.  Returns the java type of this attribute
   * (Date), which is used in the generated classes for type safety.
   *
   * @return The java type of this attribute (Date)
   */
  public String javaType(boolean isDTO)
  {
    return Date.class.getName();
  }


  /**
   * Returns a string representing the query attribute class for attributes of this type.
   *
   * @return string representing the query attribute class for attributes of this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableMoment.class.getName();
  }

}
