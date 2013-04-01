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
/*
 * Created on Aug 13, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.runwaysdk;

/**
 * @author nathan
 *
 * TODO To change the template for this generated comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public aspect PolicyEnforcement
{
  declare error
  : call (public com.runwaysdk.dataaccess.BusinessDAO+.new(..))
    && !(   withincode(* com.runwaysdk.dataaccess.database.BusinessDAOFactory.factoryMethod(..))
         || withincode(* com.runwaysdk.dataaccess.database.BusinessDAOFactory.loadFactory(..))
         || withincode(* com.runwaysdk.dataaccess.BusinessDAO+.create(..))
         || within (com.runwaysdk.util.ServerInitializerIF+))
  : "BusinessDAO objects can only be instantiated within the BusinessDAOFactory class.";

  declare error
  : call (public com.runwaysdk.dataaccess.RelationshipDAO+.new(String, String, String, String))
    && !(within(com.runwaysdk.dataaccess.database.RelationshipDAOFactory))
  : "Relationship object can only be instantiated within the com.runwaysdk.dataaccess.database.relationship.RelationshipDAOFactory class.";

  declare error
  : call (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.getDDLConnection(..))
    && !(within(com.runwaysdk.dataaccess.database.general.AbstractDatabase+))
    && !(withincode(* com.runwaysdk.dataaccess.database.Database.getDDLConnection(..)))
  : "AbstractDatabase+.getConnection() can only be called within Database.getDDLConnection().";

  declare error
  : call (* com.runwaysdk.dataaccess.database.general.AbstractDatabase+.getConnection(..))
    && !(within(com.runwaysdk.dataaccess.database.general.AbstractDatabase+))
    && !(withincode(* com.runwaysdk.dataaccess.database.Database.getConnection(..)))
  : "AbstractDatabase+.getConnection() can only be called within Database.getConnection().";

  declare error
  : call (* com.runwaysdk.dataaccess.database.RelationshipDAOFactory.get(..))
    && !(within(com.runwaysdk.dataaccess.cache.CacheStrategy+) ||
         within(com.runwaysdk.dataaccess.RelationshipDAO+) ||
         within(com.runwaysdk.dataaccess.BusinessDAO+) ||
         within(com.runwaysdk.dataaccess.database.RelationshipDAOFactory) ||
         within(com.runwaysdk.dataaccess.transaction.AbstractTransactionManagement+) ||
         within(com.runwaysdk.dataaccess.*Test))
  : "RelationshipFactory.get() can only be called within the RelationshipCache class, a RelationshipCollection class, or a JUnit class whose name ends in Test.";

   declare error
    : call (* com.runwaysdk.dataaccess.database.BusinessDAOFactory.get(..))
      && !(within(com.runwaysdk.dataaccess.cache.CacheStrategy+) ||
          within(com.runwaysdk.dataaccess.database.BusinessDAOFactory) ||
          within(com.runwaysdk.dataaccess.transaction.AbstractTransactionManagement+) ||
          within(com.runwaysdk.dataaccess.*Test))
    : "BusinessDAOFactory.get() can only be called within, BusinessDAOCollection, BusinessDAOFactory, or a JUnit class whose name ends in Test.";

  declare error
  : call (* com.runwaysdk.dataaccess.cache.ObjectCache+.get(..))
    && !within(com.runwaysdk.dataaccess.cache.ObjectCache)
  : "BusinessDAOCollection+.get() can only be called within the ObjectCacheFacade class.";


  declare error
  : call (* com.runwaysdk.dataaccess.cache.ObjectCache.addCacheStrategy(..))
    && !(within(com.runwaysdk.dataaccess.cache.ObjectCache) ||
         within(com.runwaysdk.dataaccess.transaction.TransactionCache) ||
         within(com.runwaysdk.dataaccess.metadata.*))
  : "RelationshipCache.addCacheStrategy() can only be called within the RelationshipCache class, TransactionCache class and the database package.";

  declare error
  : call (* com.runwaysdk.dataaccess.cache.ObjectCache.removeCacheStrategy(..))
    && !(within(com.runwaysdk.dataaccess.cache.ObjectCache) ||
         within(com.runwaysdk.dataaccess.transaction.TransactionCache) ||
         within(com.runwaysdk.dataaccess.metadata.*))
  : "RelationshipCache.removeCacheStrategy() can only be called within the RelationshipCache class, TransactionCache class and the database package.";

  declare error
  : call (* com.runwaysdk.dataaccess.cache.ObjectCache.updateCacheStrategy(..))
    && !(within(com.runwaysdk.dataaccess.cache.ObjectCache) ||
         within(com.runwaysdk.dataaccess.metadata.*))
  : "RelationshipCache.updateCacheStrategy() can only be called within the RelationshipCache class and the database package.";



  declare error
  : call (* com.runwaysdk.dataaccess.cache.ObjectCache.addCacheStrategy(..))
    && !(within(com.runwaysdk.dataaccess.cache.ObjectCache) ||
         within(com.runwaysdk.dataaccess.cache.ObjectCache) ||
         within(com.runwaysdk.dataaccess.transaction.TransactionCache) ||
         within(com.runwaysdk.dataaccess.metadata.*))
  : "ObjectCacheFacade.addCacheStrategy() can only be called within the ObjectCacheFacade class, TransactionCache class and the database package.";

  declare error
  : call (* com.runwaysdk.dataaccess.cache.ObjectCache.removeCacheStrategy(..))
    && !(within(com.runwaysdk.dataaccess.cache.ObjectCache) ||
         within(com.runwaysdk.dataaccess.cache.ObjectCache) ||
         within(com.runwaysdk.dataaccess.transaction.TransactionCache) ||
         within(com.runwaysdk.dataaccess.metadata.*))
  : "ObjectCacheFacade.removeCacheStrategy() can only be called within the ObjectCacheFacade class, TransactionCache class and the database package.";

  declare error
  : call (* com.runwaysdk.dataaccess.cache.ObjectCache.updateParentCacheStrategy(..))
    && !(within(com.runwaysdk.dataaccess.cache.ObjectCache) ||
         within(com.runwaysdk.dataaccess.cache.ObjectCache) ||
         within(com.runwaysdk.dataaccess.metadata.*))
  : "ObjectCacheFacade.updateParentCacheStrategy() can only be called within the ObjectCacheFacade class and the database package.";

  declare error
  : call (* com.runwaysdk.dataaccess.cache.ObjectCache.updateCacheStrategy(..))
    && !(within(com.runwaysdk.dataaccess.cache.ObjectCache) ||
         within(com.runwaysdk.dataaccess.cache.ObjectCache) ||
         within(com.runwaysdk.dataaccess.metadata.*))
  : "ObjectCacheFacade.updateCacheStrategy() can only be called within the ObjectCacheFacade class and the database package.";

  declare error
  : call (* com.runwaysdk.dataaccess.BusinessDAO+.save(..))
    && !(within(com.runwaysdk.dataaccess.metadata.*) ||
         within(com.runwaysdk.dataaccess.attributes.entity.AttributeStruct) ||
         within(com.runwaysdk.dataaccess.cache.ObjectCache) ||
         within(com.runwaysdk.dataaccess.*) ||
         within(grunzke.distributed.*))
  : "BusinessDAO.save() can only be called within the database package.";

//  declare error
//    : call (* java.sql.Statement.execute*(..))
//      && !within(AbstractDatabase+)
//      && !within(DDLCommand)
//    : "Use the methods in AbstractDatabase to execute a relational database query.\nThis provides a single point for logging all SQL statements against the database.";

//  declare warning
//    : call (* com.runwaysdk.dataaccess.EntityDAO.setValue(..))
//      && (   (within(com.runwaysdk.dataaccess..*.*))
//          && !within(junit.framework.Test+)
//          && !within(com.runwaysdk.dataaccess.transaction.AbstractTransactionManagement+)
//          && !withincode(* com.runwaysdk.dataaccess.metadata.*.setRandomValue(com.runwaysdk.dataaccess.EntityDAO))
//          && !within(com.runwaysdk.dataaccess.io..*.*)
//         )
//    : "Call EntityDAO.setValue(..) within the persistence layer only when necessary.  Use EntityDAO.getAttribute(..).setValue(..) instead.";

  declare warning
    : call (* com.runwaysdk.dataaccess.EntityDAO.getValue(..))
      && (   (within(com.runwaysdk.dataaccess..*.*) )
          && !within(com.runwaysdk.dataaccess.io..*)
         )
    : "Call EntityDAO.getValue(..) within the persistence layer only when necessary.  Use EntityDAO.getAttribute(..).getValue(..) instead.";

  declare warning
    : call (* com.runwaysdk.dataaccess.RelationshipDAO.getValue(..))
      && (   (within(com.runwaysdk.dataaccess.*) || within(com.runwaysdk.dataaccess.*.*) )
          && !within(junit.framework.Test+))
    : "Call Relationship.getValue(..) within the persistence layer only when necessary.  Use Relationship.getAttribute(..).getValue(..) instead.";

}
