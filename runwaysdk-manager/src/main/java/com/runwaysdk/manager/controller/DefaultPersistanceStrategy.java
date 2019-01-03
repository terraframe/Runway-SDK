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
package com.runwaysdk.manager.controller;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.resolver.IEntityContainer;
import com.runwaysdk.dataaccess.resolver.IPersistanceStrategy;
import com.runwaysdk.dataaccess.resolver.PersistanceStrategyAdapter;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.session.Request;

public class DefaultPersistanceStrategy extends PersistanceStrategyAdapter implements IPersistanceStrategy
{
  @Request
  public EntityDAO apply(IEntityContainer container)
  {
    EntityDAO entity = container.getEntityDAO();
    entity.apply();

    return entity;
  }

  @Request
  public EntityDAO importSave(IEntityContainer container)
  {
    EntityDAO entity = container.getEntityDAO();

    entity.importSave();

    return entity;
  }

  @Request
  public void delete(IEntityContainer container)
  {
    container.getEntityDAO().delete();
  }

  @Override
  public void importDelete(IEntityContainer container)
  {
    container.getEntityDAO().importDelete();
  }

  @Request
  public EntityDAOIF get(String oid)
  {
    return EntityDAO.get(oid);
  }

  @Request
  public MdEntityDAOIF getMdEntityDAO(String type)
  {
    return MdEntityDAO.getMdEntityDAO(type);
  }

  @SuppressWarnings("unchecked")
  @Request
  public List<EntityDAOIF> getResults(EntityQuery query)
  {
    OIterator<? extends EntityDAOIF> iterator = (OIterator<? extends EntityDAOIF>) query.getIterator();

    return getResults(iterator);
  }

  @SuppressWarnings("unchecked")
  @Request
  public List<EntityDAOIF> getResults(EntityQuery query, int pageSize, int pageNumber)
  {
    OIterator<? extends EntityDAOIF> iterator = (OIterator<? extends EntityDAOIF>) query.getIterator(pageSize, pageNumber);

    return getResults(iterator);
  }

  @Request
  public EntityDAO newInstance(String type)
  {
    MdEntityDAOIF mdEntity = MdEntityDAO.getMdEntityDAO(type);

    if (mdEntity instanceof MdStructDAOIF)
    {
      return StructDAO.newInstance(type);
    }

    return BusinessDAO.newInstance(type);
  }

  @Request
  public RelationshipDAO newInstance(String parentOid, String childOid, String type)
  {
    return RelationshipDAO.newInstance(parentOid, childOid, type);
  }

  @Request
  public List<MdRelationshipDAOIF> getAllChildMdRelationships(MdBusinessDAOIF mdBusiness)
  {
    return mdBusiness.getAllChildMdRelationships();
  }

  @Request
  public List<MdRelationshipDAOIF> getAllParentMdRelationships(MdBusinessDAOIF mdBusiness)
  {
    return mdBusiness.getAllParentMdRelationships();
  }

  @Request
  public MdBusinessDAOIF getChildMdBusiness(MdRelationshipDAOIF mdRelationship)
  {
    return mdRelationship.getChildMdBusiness();
  }

  @Request
  public List<RelationshipDAOIF> getChildren(String oid, String relationshipType)
  {
    return BusinessDAO.get(oid).getChildren(relationshipType);
  }

  @Request
  public MdBusinessDAOIF getParentMdBusiness(MdRelationshipDAOIF mdRelationship)
  {
    return mdRelationship.getParentMdBusiness();
  }

  @Request
  public List<RelationshipDAOIF> getParents(String oid, String relationshipType)
  {
    return BusinessDAO.get(oid).getParents(relationshipType);
  }

  @Request
  public MdAttributeDAOIF getMdAttributeDAO(MdEntityDAOIF mdEntity, String attributeName)
  {
    List<? extends MdEntityDAOIF> superClasses = mdEntity.getSuperClasses();

    for (MdEntityDAOIF entity : superClasses)
    {
      MdAttributeDAOIF attribute = entity.definesAttribute(attributeName);

      if (attribute != null)
      {
        return attribute;
      }
    }

    return null;
  }

  @Request
  public List<MdAttributeDAOIF> definesMdAttributes(MdEntityDAOIF mdEntity)
  {
    List<MdAttributeDAOIF> attributes = new LinkedList<MdAttributeDAOIF>();

    List<? extends MdEntityDAOIF> superClasses = mdEntity.getSuperClasses();

    for (MdEntityDAOIF entity : superClasses)
    {
      List<? extends MdAttributeConcreteDAOIF> list = entity.definesAttributesOrdered();

      attributes.addAll(list);
    }

    return attributes;
  }

  @Override
  public String getLocalizedMessage(RuntimeException exception)
  {
    return exception.getLocalizedMessage(); 
  }
}
