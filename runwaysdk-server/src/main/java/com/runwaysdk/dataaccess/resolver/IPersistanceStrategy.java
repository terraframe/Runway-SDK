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
package com.runwaysdk.dataaccess.resolver;

import java.util.List;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.query.EntityQuery;

public interface IPersistanceStrategy
{
  public EntityDAO apply(IEntityContainer container);

  public void delete(IEntityContainer container);

  public EntityDAO importSave(IEntityContainer entity);

  public void importDelete(IEntityContainer entity);

  public EntityDAOIF get(String id);

  public MdEntityDAOIF getMdEntityDAO(String type);

  public List<EntityDAOIF> getResults(EntityQuery query);

  public List<EntityDAOIF> getResults(EntityQuery query, int pageSize, int pageNumber);

  public EntityDAO newInstance(String type);

  public RelationshipDAO newInstance(String parentId, String childId, String type);

  public List<MdRelationshipDAOIF> getAllParentMdRelationships(MdBusinessDAOIF mdBusiness);

  public List<MdRelationshipDAOIF> getAllChildMdRelationships(MdBusinessDAOIF mdBusiness);

  public MdBusinessDAOIF getChildMdBusiness(MdRelationshipDAOIF mdRelationship);

  public MdBusinessDAOIF getParentMdBusiness(MdRelationshipDAOIF mdRelationship);

  public List<RelationshipDAOIF> getChildren(String id, String relationshipType);

  public List<RelationshipDAOIF> getParents(String id, String relationshipType);

  public MdAttributeDAOIF getMdAttributeDAO(MdEntityDAOIF mdEntity, String attributeName);

  public List<MdAttributeDAOIF> definesMdAttributes(MdEntityDAOIF mdEntity);

  public String getLocalizedMessage(RuntimeException exception);
}
