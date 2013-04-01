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

import java.util.Map;

import com.runwaysdk.constants.MdWebSingleTermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdWebSingleTermDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdWebSingleTermDAO extends MdWebAttributeDAO implements MdWebSingleTermDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 5890075633511456455L;

  public MdWebSingleTermDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebSingleTermDAO()
  {
    super();
  }
  
  @Override
  public MdWebSingleTermDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebSingleTermDAO(attributeMap, classType);
  }
  
  public static MdWebSingleTermDAO newInstance()
  {
    return (MdWebSingleTermDAO) BusinessDAO.newInstance(MdWebSingleTermInfo.CLASS);
  }
  
}
