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
package com.runwaysdk.transport.conversion.business;

import java.util.List;

import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.session.Session;

public class MdBusinessToQueryDTO extends MdElementToQueryDTO
{
  /**
   *
   * @param sessionId
   * @param mdBusinessIF
   * @param queryDTO
   */
  public MdBusinessToQueryDTO(String sessionId, MdBusinessDAOIF mdBusinessIF, BusinessQueryDTO queryDTO)
  {
    super(sessionId, mdBusinessIF, queryDTO);
  }

  @Override
  public BusinessQueryDTO populate()
  {
    BusinessQueryDTO queryDTO =  (BusinessQueryDTO) super.populate();

    loadRelationshipDefinitions();

    return queryDTO;
  }

  /**
   * Loads the MdRelationshipIF information as defined by
   * the MdBusinessIF.
   *
   * @param mdBusinessIF
   */
  private void loadRelationshipDefinitions()
  {
    MdBusinessDAOIF mdBusinessIF = (MdBusinessDAOIF) getMdClassIF();
    BusinessQueryDTO queryDTO = (BusinessQueryDTO) getClassQueryDTO();

    List<MdRelationshipDAOIF> parentRelationships = mdBusinessIF.getAllParentMdRelationships();
    for(MdRelationshipDAOIF mdRelationshipIF : parentRelationships)
    {
      queryDTO.addTypeInMdRelationshipAsParent(mdRelationshipIF.definesType(), mdRelationshipIF.getParentDisplayLabel(Session.getCurrentLocale()));
    }

    List<MdRelationshipDAOIF> childRelationships = mdBusinessIF.getAllChildMdRelationships();
    for(MdRelationshipDAOIF mdRelationshipIF : childRelationships)
    {
      queryDTO.addTypeInMdRelationshipAsChild(mdRelationshipIF.definesType(), mdRelationshipIF.getChildDisplayLabel(Session.getCurrentLocale()));
    }
  }
}
