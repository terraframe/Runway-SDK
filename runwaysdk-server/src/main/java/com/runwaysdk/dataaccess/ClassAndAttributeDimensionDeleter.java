/**
 * 
 */
package com.runwaysdk.dataaccess;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class ClassAndAttributeDimensionDeleter
{
  public static enum Type {
    ATTRIBUTE, CLASS;
  }

  public static class Item
  {
    private String key;

    private Type   type;

    /**
     * @param key
     * @param type
     */
    public Item(String key, Type type)
    {
      this.key = key;
      this.type = type;
    }

    /**
     * @return the key
     */
    public String getKey()
    {
      return key;
    }

    /**
     * @return the type
     */
    public Type getType()
    {
      return type;
    }
  }

  private boolean    logTransactions;

  private boolean    originalLogTransactions;

  private List<Item> items;

  public ClassAndAttributeDimensionDeleter()
  {
    this.logTransactions = false;
    this.originalLogTransactions = ServerProperties.logTransactions();
    this.items = new LinkedList<Item>();
  }

  public void addItem(Item item)
  {
    this.items.add(item);
  }

  @Transaction
  public void delete()
  {
    ServerProperties.setLogTransactions(logTransactions);

    try
    {
      for (Item item : this.items)
      {
        if (item.getType().equals(Type.CLASS))
        {
          this.deleteMdClassDimension(item);
        }
        else
        {
          this.deleteMdAttributeDimension(item);
        }
      }
    }
    finally
    {
      ServerProperties.setLogTransactions(originalLogTransactions);
    }
  }

  private void deleteMdClassDimension(Item item)
  {
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(item.getKey());

    List<MdClassDimensionDAOIF> mdClassDimensions = mdClass.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      mdClassDimension.getBusinessDAO().delete();
    }
  }

  private void deleteMdAttributeDimension(Item item)
  {
    MdAttributeDAOIF mdAttribute = MdAttributeDAO.getByKey(item.getKey());

    List<MdAttributeDimensionDAOIF> mdAttributeDimensions = mdAttribute.getMdAttributeDimensions();

    for (MdAttributeDimensionDAOIF mdAttributeDimension : mdAttributeDimensions)
    {
      mdAttributeDimension.getBusinessDAO().delete();
    }
  }

  public static void main(String[] args)
  {
    try
    {
      ClassAndAttributeDimensionDeleter.start(args);
    }
    finally
    {
      CacheShutdown.shutdown();
    }
  }

  @Request
  private static void start(String[] args)
  {
    try
    {
      ClassAndAttributeDimensionDeleter deleter = new ClassAndAttributeDimensionDeleter();
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.paused", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.completed", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.canceled", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.running", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.JobHistoryView.running", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.JobHistoryView.paused", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.JobHistoryView.completed", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.JobHistoryView.canceled", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.repeated", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.JobHistoryView.repeated", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.removeOnComplete", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.JobHistoryView.removeOnComplete", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.startOnCreate", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.JobHistoryView.startOnCreate", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.cancelable", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.JobHistoryView.cancelable", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.pauseable", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.JobHistoryView.pauseable", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.JobHistory.jobSnapshot", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.JobSnapshot", Type.CLASS));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.JobOperation", Type.CLASS));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.startTime", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.endTime", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.retries", Type.ATTRIBUTE));
      deleter.addItem(new Item("com.runwaysdk.system.scheduler.AbstractJob.workProgress", Type.ATTRIBUTE));
      deleter.delete();
    }
    finally
    {
      ObjectCache.refreshCache();
    }
  }
}
