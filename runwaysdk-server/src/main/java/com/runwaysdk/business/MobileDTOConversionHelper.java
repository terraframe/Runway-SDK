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
package com.runwaysdk.business;

import java.util.List;
import java.util.Map;

import com.runwaysdk.transport.attributes.AttributeDTO;

/**
 * @author Richard Rowlands
 *
 * The purpose of this class is to allow access to protected DTO methods in the MobileAdapter without changing access permissions.
 */

public class MobileDTOConversionHelper
{
  public static Map<String, AttributeDTO> getComponentDTOAttributeMap(ComponentDTO componentDTO) {
    return componentDTO.attributeMap;
  }
  public static Map<String, AttributeDTO> getDefinedAttributes(ComponentQueryDTO qry) {
    return qry.getDefinedAttributes();
  }
  public static void setComponentQueryResultSet(ComponentQueryDTO qry, List<? extends ComponentDTOIF> resultSet) {
    qry.setResultSet(resultSet);
  }
  public static void addAttribute(ComponentQueryDTO qry, AttributeDTO attributeDTO)
  {
    qry.addAttribute(attributeDTO);
  }
  public static void setRelationshipIds(RelationshipDTO dto, String parentOid, String childOid) {
    dto.setParentOid(parentOid);
    dto.setChildOid(childOid);
  }
}
