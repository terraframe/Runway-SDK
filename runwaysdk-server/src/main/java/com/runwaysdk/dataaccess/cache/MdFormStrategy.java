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
package com.runwaysdk.dataaccess.cache;

import java.util.HashMap;
import java.util.List;

import com.runwaysdk.constants.MdFormInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdFormDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdFormDAO;

public class MdFormStrategy extends MetaDataObjectStrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = -4071096141689162041L;
  
  /**
   * Maps facade types to the MdForm objects that define them. <br/><b>invariant</b>
   * this.mdFormMap.size() == this.entityDAOMap.size() <br/><b>invariant</b>
   * this.mdFormMap references the same objects as this.entityDAOMap (i.e.
   * they are in-sync)
   */
  private HashMap<String, MdFormDAO> mdFormMap;

  /**
   * Initializes the EntityDAOCollection with the all MdForm objects.
   *
   * <br/><b>Precondition:</b> classType != null <br/><b>Precondition:</b>
   * !classType.trim().equals("")
   *
   * @param classType name of the type of the objects in this collection
   */
  public MdFormStrategy(String classType)
  {
    super(classType);

    this.mdFormMap = new HashMap<String, MdFormDAO>();
  }

  /**
   * Returns a MdFormIF that defines the facade with the given type. Note that
   * this method <i>will</i> return <code><b>null</b></code> if the type is
   * invalid (cannot be found). It is the responsibility of the caller to
   * account for a <code><b>null</b></code> scenario.
   *
   * <br/><b>Precondition:</b> facadeType != null <br/><b>Precondition:</b>
   * !facadeType.trim().equals("") <br/><b>Postcondition:</b> Returns a
   * MdFormIF where (mdForm.getType().equals(classType)
   *
   * @param type Name of the entity.
   * @return MdFormIF that defines the facade with the given type.
   */
  public synchronized MdFormDAOIF getMdForm(String type)
  {
    if (reload == true)
    {
      this.reload();
    }

    MdFormDAO mdForm = this.mdFormMap.get(type);
    if (mdForm == null)
    {
      String error = "The MdForm that defines [" + type + "] was not found.";
      throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(MdFormInfo.CLASS));
    }

    return mdForm;
  }

  /**
   * Reloads the cache of this collection. The cache is cleared. All EntityDAOs
   * of this type stored in this collection are instantiated an put in the
   * cache. This method uses the database instead of the metadata cache.
   *
   * <br/><b>Precondition:</b> true
   *
   * <br/><b>Postcondition:</b> Cache contains all EntityDAOs of this type
   * that are to be stored in this collection
   *
   */
  public synchronized void reload()
  {
    this.removeCacheStrategy();
    
    this.entityDAOIdSet.clear();
    this.entityDAOIdByKeyMap.clear();
    
    this.mdFormMap.clear();

    super.reload();

    reload = false;
  }

  /**
   * Returns a list of sub types(including this type).
   * @return a list of sub types(including this type).
   */
  protected List<String> getSubTypesFromDatabase()
  {
    return Database.getConcreteSubClasses(MdFormInfo.ID_VALUE);
  }

  /**
   * Places the given EntityDAO into the cache.
   *
   * <br/><b>Precondition:</b> entityDAO != null <br/><b>Precondition:</b>
   * entityDAO.getTypeName().equals(MdFormIF.CLASS)
   *
   * <br/><b>Postcondition:</b> cache contains the given EntityDAO
   *
   * @param entityDAO EntityDAO to add to this collection
   */
  public void updateCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.updateCache(entityDAO);

      MdFormDAO mdForm = (MdFormDAO) entityDAO;
      this.mdFormMap.put(mdForm.definesType(), mdForm);
    }
  }

  /**
   * Removes the given EntityDAO from the cache.
   *
   * <br/><b>Precondition:</b> mdEntity != null <br/><b>Precondition:</b>
   * mdEntity.getTypeName().equals(MdFormIF.CLASS)
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given EntityDAO
   *
   * @param entityDAO EntityDAO to remove from this collection
   */
  public void removeCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.removeCache(entityDAO);

      this.mdFormMap.remove( ( (MdFormDAOIF) entityDAO ).definesType());
    }
  }
}
