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
package com.runwaysdk.manager.model;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Set;

import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.query.EntityQuery;

public interface IComponentObject extends PropertyChangeListener
{
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

  public void setValue(String key, String value);

  public String getValue(String attributeName);

  public void setStructValue(String structAttributeName, String attributeName, String value);

  public String getStructValue(String structAttributeName, String attributeName);

  public List<EnumerationItemDAOIF> getItems(String attributeName);

  public void setItems(String attributeName, Set<String> ids);

  public void setStructItem(String structName, String attributeName, Set<String> ids);

  public List<EnumerationItemDAOIF> getStructItems(String structName, String attributeName);

  public MdEntityDAOIF getMdClassDAO();

  public List<MdAttributeDAOIF> definesMdAttributes();

  public MdAttributeDAOIF getMdAttributeDAO(String attribute);

  public String getType();

  public EntityQuery getQuery();

  public IComponentObject getStruct(String attributeName);

  public void copyAttributes(IComponentObject entity);

  public String getOid();

  public String getKey();
}
