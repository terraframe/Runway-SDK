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
package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterEmbeddedInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterEmbeddedDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;

public class MdAttributeLocalCharacterEmbeddedDAO extends MdAttributeLocalEmbeddedDAO implements MdAttributeLocalCharacterEmbeddedDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -5159428428690263486L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeLocalCharacterEmbeddedDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdAttributeLocalCharacterEmbeddedDAO} from the given
   * hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   * 
   * 
   * @param attributeMap
   * @param classType
   */
  public MdAttributeLocalCharacterEmbeddedDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Map, String)
   */
  public MdAttributeLocalCharacterEmbeddedDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeLocalCharacterEmbeddedDAO(attributeMap, classType);
  }

  @Override
  public MdAttributeLocalCharacterEmbeddedDAO getBusinessDAO()
  {
    return (MdAttributeLocalCharacterEmbeddedDAO) super.getBusinessDAO();
  }

  public static MdAttributeLocalCharacterEmbeddedDAO newInstance()
  {
    return (MdAttributeLocalCharacterEmbeddedDAO) BusinessDAO.newInstance(MdAttributeLocalCharacterEmbeddedInfo.CLASS);
  }
}
