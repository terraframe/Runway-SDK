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

import com.runwaysdk.constants.MdWebCommentInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdWebCommentDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;

public class MdWebCommentDAO extends MdWebFieldDAO implements MdWebCommentDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 7903924236889901621L;

  public MdWebCommentDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebCommentDAO()
  {
    super();
  }
  
  public static MdWebCommentDAO newInstance()
  {
    return (MdWebCommentDAO) BusinessDAO.newInstance(MdWebCommentInfo.CLASS);
  }
  
  @Override
  public MdWebCommentDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebCommentDAO(attributeMap, classType);
  }
  
  @Override
  public final String getCommentText(Locale locale)
  {
    return ((AttributeLocal)this.getAttributeIF(MdWebCommentInfo.COMMENT_TEXT)).getValue(locale);
  }
}
