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
package com.runwaysdk.dataaccess.graph;

import java.util.Date;

import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.ComponentDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;

public interface GraphObjectDAOIF extends ComponentDAOIF
{
  /**
   * Column name of the attribute that specifies the OID.
   */
  public static final String ID_ATTRIBUTE = "oid";

  public Object getRID();

  /**
   * Returns the embedded {@link ComponentDAO} for the attribute of the given
   * name.
   * 
   * @param attriubteName
   * @return
   */
  public ComponentDAO getEmbeddedComponentDAO(String attributeName);

  public Object getObjectValue(String name, Date date);

  public ValueOverTimeCollection getValuesOverTime(String name);

  public void setValue(String name, Object value, Date startDate, Date endDate);

  public AttributeIF[] getAttributeArrayIF();
}
