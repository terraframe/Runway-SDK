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
package com.runwaysdk.dataaccess.cache;

import java.util.Hashtable;
import java.util.List;

import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;


public class MdEnumerationStrategy extends MetaDataObjectStrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = 4533381294299517376L;
  
  /**
   * Maps enumeration names to the MdEnumeration objects that define them.
   * <br/><b>invariant</b> this.enumerationNameMap.size() == this.entityDAOMap.size()
   * <br/><b>invariant</b> this.enumerationNameMap references the same objects as this.entityDAOMap
   *                       (i.e. they are in-sync)
   */
  private Hashtable<String, MdEnumerationDAO> enumerationNameMap;

  /**
   * Initializes the {@link MdEnumerationStrategy} with the MdRelationship objects that reside in this
   * collection.
   *
   * <br/><b>Precondition:</b>  classType != null
   * <br/><b>Precondition:</b>  !classType.trim().equals("")
   * <br/><b>Precondition:</b>  classType.equalsIgnoreCase(Constants.MD_ENUMERATION)
   *
   * @param classType name of the classes in this collection (Constants.MD_ENUMERATION)
   */
  public MdEnumerationStrategy(String classType)
  {
    super(classType);
    this.enumerationNameMap = new Hashtable<String, MdEnumerationDAO>();
  }

  /**
   * Returns a DataAccessIF instance of the metadata for the given enumeration
   * name. Note that this method <i>will</i> return <code><b>null</b></code> if
   * the type is invalid (cannot be found).  It is the responsibility of the
   * caller to account for a <code><b>null</b></code> scenario.
   *
   * <br/><b>Precondition:</b>  enumerationName != null
   * <br/><b>Precondition:</b>  !enumerationName.trim().equals("")
   * <br/><b>Precondition:</b>  enumerationName is a valid enumeration defined in the database
   * <br/><b>Postcondition:</b> Returns a DataAccessIF where
   *                            (mdEnumeration.definesEnumeration().equals(enumerationName))
   *
   * @param  enumerationName Name of the enumeration.
   * @return EntityDAO instance of the metadata for the given enumerationName.
   */
  public synchronized MdEnumerationDAOIF getMdEnumerationDAO(String enumerationName)
  {
    if (reload == true)
    {
      this.reload();
    }

    MdEnumerationDAO mdEnumeration =  enumerationNameMap.get(enumerationName);

    if (mdEnumeration==null)
    {
      String error = "The MdEnumeration that defines [" + enumerationName + "] was not found.";

      throw new DataNotFoundException(error, MdClassDAO.getMdClassDAO(MdEnumerationInfo.CLASS));
    }

    return mdEnumeration;
  }


  /**
   *
   */
  public synchronized void reload()
  {
    this.removeCacheStrategy();
    
    this.entityDAOIdSet.clear();
    this.entityDAOIdByKeyMap.clear();
    this.enumerationNameMap.clear();

    super.reload();

    reload = false;
  }

  /**
   * Returns a list of sub types(including this type).
   * @return a list of sub types(including this type).
   */
  protected List<String> getSubTypesFromDatabase()
  {
    return Database.getConcreteSubClasses(MdEnumerationInfo.ID_VALUE);
  }

  /**
   * Places the given EntityDAO into the cache.
   *
   * <br/><b>Precondition:</b>  entityDAO != null
   *
   * <br/><b>Postcondition:</b> cache contains the given entityDAO
   *
   * @param  entityDAO to add to this collection
   */
  public void updateCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.updateCache(entityDAO);

      MdEnumerationDAO mdEnumeration = (MdEnumerationDAO)entityDAO;
      this.enumerationNameMap.put(mdEnumeration.definesEnumeration(), mdEnumeration);
    }
  }


  /**
   * Removes the given EntityDAO from the cache.
   *
   * <br/><b>Precondition:</b>  entityDAO != null
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given EntityDAO
   *
   * @param  elementDAO to remove from this collection
   */
  public void removeCache(EntityDAO mdEnumeration)
  {
    synchronized(mdEnumeration.getId())
    {
      super.removeCache(mdEnumeration);

      this.enumerationNameMap.remove(((MdEnumerationDAOIF)mdEnumeration).definesEnumeration());
    }
  }
}
