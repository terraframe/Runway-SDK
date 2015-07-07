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
/**
 * Created on Aug 18, 2005
 *
 */
package com.runwaysdk.dataaccess.metadata;

/**
 * @author nathan
 */
public aspect MdAttribute_delete
{
  protected pointcut updateParentCacheStrategy(MdEntityDAO collectionMdEntity, MdEntityDAO deleteMdEntity) 
  : (call (* com.runwaysdk.dataaccess.cache.ObjectCache.updateParentCacheStrategy(..)) && args(collectionMdEntity))
  && cflow(execution (* com.runwaysdk.dataaccess.metadata.MdEntityDAO.delete(..)) && target(deleteMdEntity));

  //Do not refresh the cache of a class if the class is being deleted anyway  
  void around(MdEntityDAO collectionMdEntity, MdEntityDAO deleteMdEntity) : updateParentCacheStrategy(collectionMdEntity, deleteMdEntity) 
  {  
    // Do not refresh the cache of a class you are deleting.  That cache collection will be removed anyway.
    if (!collectionMdEntity.definesType().equals(deleteMdEntity.definesType()))
    {
//      System.out.println("refresh cache true");
      proceed(collectionMdEntity, deleteMdEntity);
    }
//    else
//    {
//      System.out.println("refresh cache false");
//    }
  }

  // This advice was commented out because it no longer works with the command pattern.  Although it does not
  // make sense to individually drop columns from a table that will be deleted anyway, it is necessary to do 
  // so in order to build a command pattern undo stack that can roll back the operation.
  /*
  protected pointcut dropAttribute(MdAttribute mdAttribute, MdEntity deleteMdEntity, String tableName, String attributeName, String dbColumnType) 
  : (call (* com.runwaysdk.dataaccess.metadata.MdAttribute.dropAttribute(..)) 
            && args(tableName, attributeName, dbColumnType) && target(mdAttribute))
  && cflow(execution (* com.runwaysdk.dataaccess.metadata.MdEntity.delete(..)) && target(deleteMdEntity));
  

  void around(MdAttribute mdAttribute, MdEntity deleteMdEntity, String tableName, String attributeName, String dbColumnType) : 
    dropAttribute(mdAttribute, deleteMdEntity, tableName, attributeName, dbColumnType) 
  {  
    // this is done to improve performance.  Do not drop the column in the table when you drop an attribute if
    // you are going to delete the class anyway in the same transaction.  However, if the attribute being dropped refreshed
    // does not match the defining class of the attribute, then go ahead and drop the attribute.  This can occur
    // when a struct attribute class is being deleted, causing all struct attributes that use that class to be
    // dropped.  
    if (!mdAttribute.definedByEntity().definesType().equals(deleteMdEntity.definesType()))
    {
//      System.out.println("drop attribute true");
      proceed(mdAttribute, deleteMdEntity, tableName, attributeName, dbColumnType);
    }
//    else
//    {
//      System.out.println("drop attribute false");
//    }
  }
 */
  
}
