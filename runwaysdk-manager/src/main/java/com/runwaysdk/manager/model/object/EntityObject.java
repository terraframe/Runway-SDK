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
package com.runwaysdk.manager.model.object;

import java.util.HashMap;

import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.dataaccess.resolver.IEntityContainer;
import com.runwaysdk.manager.model.attributes.SimpleAttribute;
import com.runwaysdk.manager.model.conversion.ConversionStrategyAdapter;
import com.runwaysdk.manager.model.conversion.ImportConversionStrategy;

public abstract class EntityObject extends ComponentObject implements IEntityObject, IEntityContainer
{
  private String  id;

  private boolean isNew;

  private boolean appliedToDB;

  private boolean isImport;

  public EntityObject(EntityDAOIF entityDAO)
  {
    super(entityDAO.getMdClassDAO());

    this.populate(entityDAO);
  }

  public EntityObject(MdEntityDAOIF mdEntity, HashMap<String, SimpleAttribute> attributes)
  {
    super(mdEntity, attributes);
  }

  protected void populate(EntityDAOIF entityDAO)
  {
    this.isNew = entityDAO.isNew();
    this.appliedToDB = entityDAO.isAppliedToDB();
    this.id = entityDAO.getId();
    this.isImport = false;

    new ConversionStrategyAdapter().populate(entityDAO, this);
  }

  public void setImport(boolean isImport)
  {
    this.isImport = isImport;
  }

  @Override
  public boolean isNew()
  {
    return this.isNew;
  }

  public boolean isAppliedToDB()
  {
    return appliedToDB;
  }

  @Override
  public String getId()
  {
    return this.id;
  }

  @Override
  public String apply()
  {
    EntityDAO entity = this.save();

    this.populate(entity);

    return entity.getId();
  }

  private EntityDAO save()
  {
    if (this.isImport)
    {
      return PersistanceFacade.importSave(this);
    }
    else
    {
      return PersistanceFacade.apply(this);
    }
  }

  @Override
  public void delete()
  {
    if (this.isImport)
    {
      PersistanceFacade.importDelete(this);
    }
    else
    {
      PersistanceFacade.delete(this);
    }
  }

  public abstract EntityDAO instance();

  @Override
  public EntityDAO getEntityDAO()
  {
    if (this.isImport)
    {
      return new ImportConversionStrategy().populate(this);
    }

    return new ConversionStrategyAdapter().populate(this);
  }

  public static IEntityObject get(String id)
  {
    EntityDAOIF entity = PersistanceFacade.get(id);

    return EntityObject.get(entity);
  }

  public static IEntityObject get(EntityDAOIF entity)
  {
    if (entity instanceof BusinessDAOIF)
    {
      return new BusinessObject((BusinessDAOIF) entity);
    }
    else if (entity instanceof RelationshipDAOIF)
    {
      return new RelationshipObject((RelationshipDAOIF) entity);
    }
    return new StructObject((StructDAOIF) entity);
  }

}
