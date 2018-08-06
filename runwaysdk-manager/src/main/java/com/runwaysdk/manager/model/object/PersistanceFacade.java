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
package com.runwaysdk.manager.model.object;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.resolver.IEntityContainer;
import com.runwaysdk.dataaccess.resolver.IPersistanceStrategy;
import com.runwaysdk.manager.controller.DefaultPersistanceStrategy;
import com.runwaysdk.query.EntityQuery;

public class PersistanceFacade
{
  private IPersistanceStrategy strategy;

  private ReentrantLock        lock;

  private PersistanceFacade()
  {
    this.strategy = new DefaultPersistanceStrategy();
    this.lock = new ReentrantLock();
  }

  private IPersistanceStrategy getStrategy()
  {
    this.lock.lock();

    try
    {
      return strategy;
    }
    finally
    {
      this.lock.unlock();
    }
  }

  public void setStrategy(IPersistanceStrategy strategy)
  {
    this.lock.lock();

    try
    {
      this.strategy = strategy;
    }
    finally
    {
      this.lock.unlock();
    }
  }

  private static PersistanceFacade instance;

  private static synchronized PersistanceFacade getInstance()
  {
    if (instance == null)
    {
      instance = new PersistanceFacade();
    }

    return instance;
  }

  private static IPersistanceStrategy strategy()
  {
    return getInstance().getStrategy();
  }

  public static void setPersistanceStrategy(IPersistanceStrategy strategy)
  {
    getInstance().setStrategy(strategy);
  }

  public static EntityDAO apply(IEntityContainer entity)
  {
    return strategy().apply(entity);
  }

  public static void delete(IEntityContainer entity)
  {
    strategy().delete(entity);
  }

  public static EntityDAO importSave(IEntityContainer entity)
  {
    return strategy().importSave(entity);
  }

  public static void importDelete(IEntityContainer entity)
  {
    strategy().importDelete(entity);
  }

  public static EntityDAOIF get(String oid)
  {
    return strategy().get(oid);
  }

  public static MdEntityDAOIF getMdEntityDAO(String type)
  {
    return strategy().getMdEntityDAO(type);
  }

  public static List<EntityDAOIF> getResults(EntityQuery query)
  {
    return strategy().getResults(query);
  }

  public static List<EntityDAOIF> getResults(EntityQuery query, int pageSize, int pageNumber)
  {
    return strategy().getResults(query, pageSize, pageNumber);
  }

  public static EntityDAO newInstance(String type)
  {
    return strategy().newInstance(type);
  }

  public static RelationshipDAO newInstance(String parentId, String childId, String type)
  {
    return strategy().newInstance(parentId, childId, type);
  }

  public static List<MdRelationshipDAOIF> getAllParentMdRelationships(MdBusinessDAOIF mdBusiness)
  {
    return strategy().getAllParentMdRelationships(mdBusiness);
  }

  public static List<MdRelationshipDAOIF> getAllChildMdRelationships(MdBusinessDAOIF mdBusiness)
  {
    return strategy().getAllChildMdRelationships(mdBusiness);
  }

  public static MdBusinessDAOIF getChildMdBusiness(MdRelationshipDAOIF mdRelationship)
  {
    return strategy().getChildMdBusiness(mdRelationship);
  }

  public static MdBusinessDAOIF getParentMdBusiness(MdRelationshipDAOIF mdRelationship)
  {
    return strategy().getParentMdBusiness(mdRelationship);
  }

  public static List<RelationshipDAOIF> getChildren(String oid, String relationshipType)
  {
    return strategy().getChildren(oid, relationshipType);
  }

  public static List<RelationshipDAOIF> getParents(String oid, String relationshipType)
  {
    return strategy().getParents(oid, relationshipType);
  }

  public static MdAttributeDAOIF getMdAttributeDAO(MdEntityDAOIF mdEntity, String attributeName)
  {
    return strategy().getMdAttributeDAO(mdEntity, attributeName);
  }

  public static List<MdAttributeDAOIF> definesMdAttributes(MdEntityDAOIF mdEntity)
  {
    return strategy().definesMdAttributes(mdEntity);
  }

  public static String getLocalizedMessage(RuntimeException exception)
  {
    return strategy().getLocalizedMessage(exception);
  }
}
