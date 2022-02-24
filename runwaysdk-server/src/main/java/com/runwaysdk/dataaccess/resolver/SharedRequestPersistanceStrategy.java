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
package com.runwaysdk.dataaccess.resolver;

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
import com.runwaysdk.dataaccess.transaction.ThreadTransactionState;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OIterator;

public class SharedRequestPersistanceStrategy extends PersistanceStrategyAdapter implements IPersistanceStrategy
{
  private ThreadTransactionState state;

  public SharedRequestPersistanceStrategy(ThreadTransactionState state)
  {
    this.state = state;
  }

  @Override
  public EntityDAO apply(final IEntityContainer container)
  {
    SharedRequestExecutor<EntityDAO> executor = new SharedRequestExecutor<EntityDAO>(state)
    {
      @Override
      protected EntityDAO run()
      {
        EntityDAO entity = container.getEntityDAO();
        entity.setImportResolution(true);

        try
        {
          entity.apply();
        }
        finally
        {
          entity.setImportResolution(false);
        }

        return entity;
      }
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public void delete(final IEntityContainer container)
  {
    SharedRequestExecutor<Void> executor = new SharedRequestExecutor<Void>(state)
    {
      @Override
      protected Void run()
      {
        EntityDAO entity = container.getEntityDAO();
        entity.setImportResolution(true);

        try
        {
          entity.delete();
        }
        finally
        {
          entity.setImportResolution(false);
        }

        return null;
      }
    };

    executor.executeWithinTransaction();
  }

  @Override
  public EntityDAO importSave(final IEntityContainer container)
  {
    SharedRequestExecutor<EntityDAO> executor = new SharedRequestExecutor<EntityDAO>(state)
    {
      @Override
      protected EntityDAO run()
      {
        EntityDAO entity = container.getEntityDAO();
        entity.setImportResolution(true);

        try
        {
          entity.importSave();
        }
        finally
        {
          entity.setImportResolution(false);
        }

        return entity;
      }
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public void importDelete(final IEntityContainer container)
  {
    SharedRequestExecutor<Void> executor = new SharedRequestExecutor<Void>(state)
    {
      @Override
      protected Void run()
      {
        EntityDAO entity = container.getEntityDAO();
        entity.setImportResolution(true);

        try
        {
          entity.importDelete();
        }
        finally
        {
          entity.setImportResolution(false);
        }

        return null;
      }
    };

    executor.executeWithinTransaction();
  }

  @Override
  public EntityDAOIF get(final String oid)
  {
    SharedRequestExecutor<EntityDAOIF> executor = new SharedRequestExecutor<EntityDAOIF>(state)
    {
      @Override
      protected EntityDAOIF run()
      {
        return EntityDAO.get(oid);
      }
    };

    return executor.executeWithinRequest();
  }

  @Override
  public MdEntityDAOIF getMdEntityDAO(final String type)
  {
    SharedRequestExecutor<MdEntityDAOIF> executor = new SharedRequestExecutor<MdEntityDAOIF>(state)
    {
      @Override
      protected MdEntityDAOIF run()
      {
        return MdEntityDAO.getMdEntityDAO(type);
      }
    };

    return executor.executeWithinRequest();
  }

  @SuppressWarnings("unchecked")
  public List<EntityDAOIF> getResults(final EntityQuery query)
  {
    SharedRequestExecutor<List<EntityDAOIF>> executor = new SharedRequestExecutor<List<EntityDAOIF>>(state)
    {
      @Override
      protected List<EntityDAOIF> run()
      {
        OIterator<? extends EntityDAOIF> iterator = (OIterator<? extends EntityDAOIF>) query.getIterator();

        return getResults(iterator);
      }
    };

    return executor.executeWithinTransaction();
  }

  @SuppressWarnings("unchecked")
  public List<EntityDAOIF> getResults(final EntityQuery query, final int pageSize, final int pageNumber)
  {
    SharedRequestExecutor<List<EntityDAOIF>> executor = new SharedRequestExecutor<List<EntityDAOIF>>(state)
    {
      @Override
      protected List<EntityDAOIF> run()
      {
        OIterator<? extends EntityDAOIF> iterator = (OIterator<? extends EntityDAOIF>) query.getIterator(pageSize, pageNumber);

        return getResults(iterator);
      }
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public EntityDAO newInstance(final String type)
  {
    SharedRequestExecutor<EntityDAO> executor = new SharedRequestExecutor<EntityDAO>(state)
    {
      @Override
      protected EntityDAO run()
      {
        MdEntityDAOIF mdEntity = MdEntityDAO.getMdEntityDAO(type);

        if (mdEntity instanceof MdStructDAOIF)
        {
          return StructDAO.newInstance(type);
        }

        return BusinessDAO.newInstance(type);
      }
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public RelationshipDAO newInstance(final String parentOid, final String childOid, final String type)
  {
    SharedRequestExecutor<RelationshipDAO> executor = new SharedRequestExecutor<RelationshipDAO>(state)
    {
      @Override
      protected RelationshipDAO run()
      {
        return RelationshipDAO.newInstance(parentOid, childOid, type);
      }
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public List<MdRelationshipDAOIF> getAllChildMdRelationships(final MdBusinessDAOIF mdBusiness)
  {
    SharedRequestExecutor<List<MdRelationshipDAOIF>> executor = new SharedRequestExecutor<List<MdRelationshipDAOIF>>(state)
    {
      @Override
      protected List<MdRelationshipDAOIF> run()
      {
        return mdBusiness.getAllChildMdRelationships();
      }
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public List<MdRelationshipDAOIF> getAllParentMdRelationships(final MdBusinessDAOIF mdBusiness)
  {
    SharedRequestExecutor<List<MdRelationshipDAOIF>> executor = new SharedRequestExecutor<List<MdRelationshipDAOIF>>(state)
    {
      @Override
      protected List<MdRelationshipDAOIF> run()
      {
        return mdBusiness.getAllParentMdRelationships();
      }
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public MdBusinessDAOIF getChildMdBusiness(final MdRelationshipDAOIF mdRelationship)
  {
    SharedRequestExecutor<MdBusinessDAOIF> executor = new SharedRequestExecutor<MdBusinessDAOIF>(state)
    {
      @Override
      protected MdBusinessDAOIF run()
      {
        return mdRelationship.getChildMdBusiness();
      }
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public List<RelationshipDAOIF> getChildren(final String oid, final String relationshipType)
  {
    SharedRequestExecutor<List<RelationshipDAOIF>> executor = new SharedRequestExecutor<List<RelationshipDAOIF>>(state)
    {
      @Override
      protected List<RelationshipDAOIF> run()
      {
        return BusinessDAO.get(oid).getChildren(relationshipType);
      }
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public MdBusinessDAOIF getParentMdBusiness(final MdRelationshipDAOIF mdRelationship)
  {
    SharedRequestExecutor<MdBusinessDAOIF> executor = new SharedRequestExecutor<MdBusinessDAOIF>(state)
    {
      @Override
      protected MdBusinessDAOIF run()
      {
        return mdRelationship.getParentMdBusiness();
      }
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public List<RelationshipDAOIF> getParents(final String oid, final String relationshipType)
  {
    SharedRequestExecutor<List<RelationshipDAOIF>> executor = new SharedRequestExecutor<List<RelationshipDAOIF>>(state)
    {
      @Override
      protected List<RelationshipDAOIF> run()
      {
        return BusinessDAO.get(oid).getParents(relationshipType);
      }
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public MdAttributeDAOIF getMdAttributeDAO(final MdEntityDAOIF mdEntity, final String attributeName)
  {
    SharedRequestExecutor<MdAttributeDAOIF> executor = new SharedRequestExecutor<MdAttributeDAOIF>(state)
    {
      @Override
      protected MdAttributeDAOIF run()
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
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public List<MdAttributeDAOIF> definesMdAttributes(final MdEntityDAOIF mdEntity)
  {
    SharedRequestExecutor<List<MdAttributeDAOIF>> executor = new SharedRequestExecutor<List<MdAttributeDAOIF>>(state)
    {
      @Override
      protected List<MdAttributeDAOIF> run()
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
    };

    return executor.executeWithinTransaction();
  }

  @Override
  public String getLocalizedMessage(final RuntimeException exception)
  {
    SharedRequestExecutor<String> executor = new SharedRequestExecutor<String>(state)
    {
      @Override
      protected String run()
      {
        return exception.getLocalizedMessage();
      }
    };

    return executor.executeWithinTransaction();
  }
}
