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
package com.runwaysdk.dataaccess.cache;

import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class EnumerationMasterStrategy extends CacheAllBusinessDAOstrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = 4362883672981136766L;
  /**
   * Key: type+enumName of the enumeration Item.  Value: EnumerationItem
   */
  private Map<String, EnumerationItemDAO> enumItemMap;

  /**
   *
   * @param classType
   */
  public EnumerationMasterStrategy(MdBusinessDAOIF mdBusinessIF)
  {
    super(mdBusinessIF);

    this.enumItemMap = new HashMap<String, EnumerationItemDAO>();
  }

  public synchronized EnumerationItemDAO getEnumeration(String masterType, String enumName)
  {
    if (reload == true)
    {
      this.reload();
    }

    String enumKeyName = this.buildEnumMapKey(masterType, enumName);

    EnumerationItemDAO enumerationItem = this.enumItemMap.get(enumKeyName);

    if (enumerationItem == null)
    {

      QueryFactory queryFactory = new QueryFactory();
      BusinessDAOQuery q1 = queryFactory.businessDAOQuery(masterType);
      q1.WHERE(q1.aCharacter(EnumerationMasterInfo.NAME).EQ(enumName));

      OIterator<BusinessDAOIF> iterator = q1.getIterator();

      for (BusinessDAOIF businessDAOIF : iterator)
      {
        enumerationItem = (EnumerationItemDAO)businessDAOIF;
      }

      if (enumerationItem == null)
      {
        String msg = "An enumeration of name [" + enumName + "] of type the master type [" + masterType + "] does not exist";
        throw new ProgrammingErrorException(msg);
      }
      else
      {
        this.enumItemMap.put(enumKeyName, enumerationItem);
      }
    }

    return enumerationItem;
  }

  /**
   *
   *
   */
  public synchronized void reload()
  {
    super.reload();
    this.enumItemMap.clear();
  }

  /**
   * Places the given EntityDAO into the cache.
   *
   * <br/><b>Precondition:</b>  mdClass != null
   * <br/><b>Precondition:</b>  mdClass.getTypeName().equals(MdClassInfo.CLASS)
   *
   * <br/><b>Postcondition:</b> cache contains the given EntityDAO
   *
   * @param  entityDAO EntityDAO to add to this collection
   */
  public void updateCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.updateCache(entityDAO);

      String enumKeyName = this.buildEnumMapKey(entityDAO.getType(), entityDAO.getAttribute(EnumerationMasterInfo.NAME).getValue());
      this.enumItemMap.put(enumKeyName, (EnumerationItemDAO)entityDAO);
    }
  }

  /**
   * Removes the given EntityDAO from the cache.
   *
   * <br/><b>Precondition:</b>  mdClass != null
   * <br/><b>Precondition:</b>  mdClass.getTypeName().equals(MdClassInfo.CLASS)
   *
   * <br/><b>Postcondition:</b> cache no longer contains the given EntityDAO
   *
   * @param  EntityDAO entityDAO to remove from this collection
   */
  public void removeCache(EntityDAO entityDAO)
  {
    synchronized(entityDAO.getId())
    {
      super.removeCache(entityDAO);

      String enumKeyName = this.buildEnumMapKey(entityDAO.getType(), entityDAO.getAttribute(EnumerationMasterInfo.NAME).getValue());
      this.enumItemMap.remove(enumKeyName);
    }
  }

  private String buildEnumMapKey(String masterType, String enumName)
  {
    return masterType+"."+enumName;
  }
}
