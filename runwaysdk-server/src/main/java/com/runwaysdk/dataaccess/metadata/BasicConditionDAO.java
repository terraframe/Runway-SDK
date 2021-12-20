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
package com.runwaysdk.dataaccess.metadata;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.business.Business;
import com.runwaysdk.constants.BasicConditionInfo;
import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.FieldConditionDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebGeoDAOIF;
import com.runwaysdk.dataaccess.MdWebMultipleTermDAOIF;
import com.runwaysdk.dataaccess.MdWebSingleTermDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.FieldConditionDAO;

public abstract class BasicConditionDAO extends FieldConditionDAO implements FieldConditionDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 3194497182246677500L;

  /**
   * The default constructor, does not set any attributes
   */
  public BasicConditionDAO()
  {
    super();
  }

  public BasicConditionDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdFieldDAOIF getDefiningMdFieldDAO()
  {
    return MdFieldDAO.get(this.getAttribute(CharacterConditionInfo.DEFINING_MD_FIELD).getValue());
  }

  @Override
  public String getFormattedString()
  {
    MdFieldDAOIF mdField = this.getDefiningMdFieldDAO();

    String value = this.getAttribute(BasicConditionInfo.VALUE).getValue();

    AttributeEnumeration attribute = (AttributeEnumeration) this.getAttribute(BasicConditionInfo.OPERATION);
    Set<String> itemIds = attribute.getEnumItemIdList();
    String operationLabel = new String();

    Locale locale = Session.getCurrentLocale();

    for (String itemId : itemIds)
    {
      EnumerationItemDAOIF item = EnumerationItemDAO.get(itemId);

      operationLabel = ( (AttributeLocalIF) item.getAttributeIF(EnumerationMasterInfo.DISPLAY_LABEL) ).getValue(locale);
    }
    
    if(value != null && value.length() > 0 && (mdField instanceof MdWebSingleTermDAOIF || mdField instanceof MdWebGeoDAOIF || mdField instanceof MdWebMultipleTermDAOIF))
    {
      value = Business.get(value).toString();
    }

    return ServerExceptionMessageLocalizer.fieldCondition(Session.getCurrentLocale(), mdField.getDisplayLabel(locale), operationLabel, value);
  }
}
