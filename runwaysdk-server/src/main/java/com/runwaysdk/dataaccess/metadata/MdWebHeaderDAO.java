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
package com.runwaysdk.dataaccess.metadata;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.MdWebHeaderInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdWebHeaderDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;

public class MdWebHeaderDAO extends MdWebFieldDAO implements MdWebHeaderDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -8917472436942197699L;

  public MdWebHeaderDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebHeaderDAO()
  {
    super();
  }
  
  public static MdWebHeaderDAO newInstance()
  {
    return (MdWebHeaderDAO) BusinessDAO.newInstance(MdWebHeaderInfo.CLASS);
  }
  
  @Override
  public MdWebHeaderDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebHeaderDAO(attributeMap, classType);
  }
  
  @Override
  public final String getHeaderText(Locale locale)
  {
    return ((AttributeLocal)this.getAttributeIF(MdWebHeaderInfo.HEADER_TEXT)).getValue(locale);
  }
}
