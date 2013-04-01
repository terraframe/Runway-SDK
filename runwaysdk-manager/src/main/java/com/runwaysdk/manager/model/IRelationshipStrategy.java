/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
 * This file is part of Runway SDK(tm).
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.manager.model;

import java.util.List;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.manager.model.object.BusinessObject;

public interface IRelationshipStrategy
{
  public List<MdRelationshipDAOIF> getMdRelationships(MdBusinessDAOIF mdBusiness);

  public List<RelationshipDAOIF> getRelationships(BusinessObject business, String type);

  public String getTabPostfix();

  public List<RelationshipDAOIF> getInverse(BusinessObject business, String relationshipType);

  public BusinessObject getEndPoint(RelationshipDAOIF relationship);

  public MdBusinessDAOIF getInverseType(MdRelationshipDAOIF mdRelationship);

  public RelationshipDAO create(BusinessDAO input, BusinessDAOIF component, String relationshipType);
}
