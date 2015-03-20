/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class PermissionComponent implements ComponentIF
{
  private RoleDAOIF             component;

  private List<MdBusinessDAOIF> allPermissions;

  /**
   * 
   */
  public PermissionComponent(RoleDAOIF component)
  {
    this.component = component;
    this.allPermissions = new LinkedList<MdBusinessDAOIF>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.ComponentIF#isNew()
   */
  @Override
  public boolean isNew()
  {
    return this.component.isNew();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.ComponentIF#getId()
   */
  @Override
  public String getId()
  {
    return this.component.getId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.ComponentIF#getType()
   */
  @Override
  public String getType()
  {
    return this.component.getType();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.ComponentIF#getKey()
   */
  @Override
  public String getKey()
  {
    return this.component.getKey();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.ComponentIF#getValue(java.lang.String)
   */
  @Override
  public String getValue(String name)
  {
    return this.component.getValue(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.ComponentIF#getObjectValue(java.lang.String)
   */
  @Override
  public Object getObjectValue(String name)
  {
    return this.component.getObjectValue(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.ComponentIF#getMdAttributeDAOs()
   */
  @Override
  public List<? extends MdAttributeDAOIF> getMdAttributeDAOs()
  {
    return this.component.getMdAttributeDAOs();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.ComponentIF#getMdAttributeDAO(java.lang.String)
   */
  @Override
  public MdAttributeDAOIF getMdAttributeDAO(String name)
  {
    return this.component.getMdAttributeDAO(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.ComponentIF#printAttributes()
   */
  @Override
  public void printAttributes()
  {
    this.component.printAttributes();
  }

  public void addAllPermissions(MdBusinessDAOIF mdBusiness)
  {
    this.allPermissions.add(mdBusiness);
  }

  /**
   * @return the allPermissions
   */
  public List<MdBusinessDAOIF> getAllPermissions()
  {
    return allPermissions;
  }

  /**
   * @return the component
   */
  public RoleDAOIF getComponent()
  {
    return component;
  }

}
