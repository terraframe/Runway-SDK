/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.dataaccess.cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdDomainInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.EntityMasterTestSetup;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDomainDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.session.Request;

/**
 * CacheTest contains a set of tests for the caching mechanisms in the core.
 * Tests include verification of cache contents, validity of caching in
 * inherited structures, and validity of changes made to a populated system.
 *
 * @author Eric Grunzke
 */
public class CacheTest
{
  /**
   * Container for package and class name of the new parent class
   */
  private static final TypeInfo parentInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Parent1");

  /**
   * Container for package and class name of the new child class
   */
  private static final TypeInfo childInfo  = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Child1");

  /**
   * Id of the cache algorithm originally defined for the parent MdBusiness.
   */
  private static String         originalParentMdCacheId;

  /**
   * Id of the cache algorithm originally defined for the child MdBusiness.
   */
  private static String         originalChildMdCacheId;

  /**
   *
   */
  private static MdDomainDAO    mdDomain;

  /**
   * A list of references of instances of the Parent class
   */
  private List<String>          parentOids;

  /**
   * A list of IDs of instances of the Child class
   */
  private List<String>          childOids;

  /**
   * Defines all metadata for the Parent and Child classes <b>except</b> the
   * cache algorithm attribute. Individual tests will specify the algorithm and
   * apply() the classes.
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    MdBusinessDAO parentMD = MdBusinessDAO.newInstance();
    parentMD.setValue(MdBusinessInfo.NAME, parentInfo.getTypeName());
    parentMD.setValue(MdBusinessInfo.PACKAGE, parentInfo.getPackageName());
    parentMD.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    parentMD.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Parent");
    parentMD.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Parent Class");
    parentMD.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    parentMD.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    parentMD.setValue(MdBusinessInfo.CACHE_SIZE, "50");
    parentMD.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    parentMD.apply();

    AttributeEnumerationIF attributeEnumerationIF = (AttributeEnumerationIF) parentMD.getAttributeIF(MdElementInfo.CACHE_ALGORITHM);
    originalParentMdCacheId = attributeEnumerationIF.dereference()[0].getOid();

    MdBusinessDAO childMD = MdBusinessDAO.newInstance();
    childMD.setValue(MdBusinessInfo.NAME, childInfo.getTypeName());
    childMD.setValue(MdBusinessInfo.PACKAGE, childInfo.getPackageName());
    childMD.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    childMD.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Child");
    childMD.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Child Class");
    childMD.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    childMD.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    childMD.setValue(MdBusinessInfo.CACHE_SIZE, "50");
    childMD.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, parentMD.getOid());
    childMD.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    childMD.apply();

    attributeEnumerationIF = (AttributeEnumerationIF) childMD.getAttributeIF(MdElementInfo.CACHE_ALGORITHM);
    originalChildMdCacheId = attributeEnumerationIF.dereference()[0].getOid();

    mdDomain = MdDomainDAO.newInstance();
    mdDomain.setValue(MdDomainInfo.DOMAIN_NAME, "testDomain");
    mdDomain.setStructValue(MdDomainInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Domain");
    mdDomain.apply();
  }

  /**
   * Removes all instances of the Parent and Child classes, and clears their
   * metadata.
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    MdBusinessDAOIF parentMD = MdBusinessDAO.getMdBusinessDAO(parentInfo.getType());
    if (MdTypeDAO.isDefined(parentMD.definesType()))
    {
      parentMD.getBusinessDAO().delete();
    }

    mdDomain.delete();
  }

  /**
   * Sets the caching algoirthms for both new classes, sets the inheritance, and
   * commits them to the core (and, consequently, the database).
   *
   * @param parentCache
   *          The caching algorithm for the parent
   * @param childCache
   *          The caching algorithm for the child
   */
  private void setCaching(EntityCacheMaster parentCache, EntityCacheMaster childCache)
  {
    MdBusinessDAO parentMD = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(parentInfo.getType()) ).getBusinessDAO();
    MdBusinessDAO childMD = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(childInfo.getType()) ).getBusinessDAO();

    parentMD.setValue(MdElementInfo.CACHE_ALGORITHM, parentCache.getOid());
    parentMD.apply();
    childMD.setValue(MdElementInfo.CACHE_ALGORITHM, childCache.getOid());
    childMD.apply();
  }

  /**
   * Resets the caching algorithm of the parent and the child classes to their
   * original values. It also deletes all instances of these classes.
   *
   */
  private void resetClasses()
  {
    MdBusinessDAO parentMD = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(parentInfo.getType()) ).getBusinessDAO();
    MdBusinessDAO childMD = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(childInfo.getType()) ).getBusinessDAO();

    parentMD.deleteInstances(false);

    parentMD.setValue(MdElementInfo.CACHE_ALGORITHM, originalParentMdCacheId);
    parentMD.apply();
    childMD.setValue(MdElementInfo.CACHE_ALGORITHM, originalChildMdCacheId);
    childMD.apply();
  }

  /**
   * Populates the system with the specified number of parent and child
   * instances.
   *
   * @param parentCount
   *          Desired number of Parent instances
   * @param childCount
   *          Desired number of Child instances
   */
  private void populate(int parentCount, int childCount)
  {
    parentOids = EntityGenerator.generate(parentInfo.getType(), parentCount);
    childOids = EntityGenerator.generate(childInfo.getType(), childCount);
  }

  /**
   * Checks that the collection contains an entry for each key in the list.
   *
   * @param collection
   *          A BusinessDAOCollection
   * @param ids
   *          A List of ids that should be in the collection
   * @return true if every oid is in the collection, false otherwise
   */
  private boolean containsAll(CacheStrategy cacheStrategy, List<String> ids)
  {
    for (String oid : ids)
    {
      if (!cacheStrategy.entityDAOIdSet.contains(oid))
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks the global collection cache to ensure that all data objects with the
   * given ids Performs a BusinessDAO.get(oid) on each oid to ensure the object
   * gets into the global cache. If it does not, then the global cache is not
   * being maintained properly.
   *
   * @param ids
   *          A List of ids that should be in the collection
   * @return true if every oid is in the collection, false otherwise
   */
  private boolean globalCacheContainsAll(List<String> ids)
  {
    for (String oid : ids)
    {
      // Should put the object in the cache.
      BusinessDAO.get(oid);
      if (!ObjectCache.globalCacheContainsId(oid))
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Explicitly checks invalid caching combinations between parent and child
   * classes. All eight invalid scenarios are tested.
   */
  @Request
  @Test
  public void testInvalidCacheTypes()
  {
    // Test Everything extends Everything
    try
    {
      setCaching(EntityCacheMaster.CACHE_EVERYTHING, EntityCacheMaster.CACHE_EVERYTHING);
      Assert.fail("Cache " + EntityCacheMaster.CACHE_EVERYTHING + " allowed to extend " + EntityCacheMaster.CACHE_EVERYTHING);
    }
    catch (CacheCodeException e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      resetClasses();
      Assert.fail(e.getMessage());
    }

    // Test MRU extends Everything
    try
    {
      // MRU requires a valid CACHE_SIZE. Add this to the child MdBusiness
      BusinessDAO childMdBusiness = MdBusinessDAO.getMdBusinessDAO(childInfo.getType()).getBusinessDAO();
      childMdBusiness.setValue(MdBusinessInfo.CACHE_SIZE, "10");

      setCaching(EntityCacheMaster.CACHE_EVERYTHING, EntityCacheMaster.CACHE_MOST_RECENTLY_USED);
      Assert.fail("Cache " + EntityCacheMaster.CACHE_MOST_RECENTLY_USED + " allowed to extend " + EntityCacheMaster.CACHE_EVERYTHING);
    }
    catch (CacheCodeException e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      resetClasses();
      Assert.fail(e.getMessage());
    }

    // Test Hardcoded extends Everything
    try
    {
      setCaching(EntityCacheMaster.CACHE_EVERYTHING, EntityCacheMaster.CACHE_HARDCODED);
      Assert.fail("Cache " + EntityCacheMaster.CACHE_HARDCODED + " allowed to extend " + EntityCacheMaster.CACHE_EVERYTHING);
    }
    catch (CacheCodeException e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      resetClasses();
      Assert.fail(e.getMessage());
    }

    // Test MRU extends MRU
    try
    {
      setCaching(EntityCacheMaster.CACHE_MOST_RECENTLY_USED, EntityCacheMaster.CACHE_MOST_RECENTLY_USED);
      Assert.fail("Cache " + EntityCacheMaster.CACHE_MOST_RECENTLY_USED + " allowed to extend " + EntityCacheMaster.CACHE_MOST_RECENTLY_USED);
    }
    catch (CacheCodeException e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      resetClasses();
      Assert.fail(e.getMessage());
    }

    // Test Hardcoded extends MRU
    try
    {
      setCaching(EntityCacheMaster.CACHE_MOST_RECENTLY_USED, EntityCacheMaster.CACHE_HARDCODED);
      Assert.fail("Cache " + EntityCacheMaster.CACHE_HARDCODED + " allowed to extend " + EntityCacheMaster.CACHE_MOST_RECENTLY_USED);
    }
    catch (CacheCodeException e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      resetClasses();
      Assert.fail(e.getMessage());
    }

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(MdElementInfo.CLASS);

    // Make a new class that extends a HARDCODED class (MdBusiness)
    MdBusinessDAO hardMD = MdBusinessDAO.newInstance();
    hardMD.setValue(MdBusinessInfo.NAME, "Hard");
    hardMD.setValue(MdBusinessInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
    hardMD.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    hardMD.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Hardcoded Extention");
    hardMD.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Parent Class");
    hardMD.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    hardMD.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    hardMD.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdBusinessIF.getOid());
    hardMD.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    // Test Everything extends Hardcoded
    try
    {
      hardMD.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getOid());
      hardMD.apply();
      hardMD.delete();
      Assert.fail("Cache " + EntityCacheMaster.CACHE_EVERYTHING + " allowed to extend " + EntityCacheMaster.CACHE_HARDCODED);
    }
    catch (CacheCodeException e)
    {
      // This is expected
    }

    // Test MRU extends Hardcoded
    try
    {
      hardMD.setValue(MdBusinessInfo.CACHE_SIZE, "5");
      hardMD.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getOid());
      hardMD.apply();
      hardMD.delete();
      Assert.fail("Cache " + EntityCacheMaster.CACHE_MOST_RECENTLY_USED + " allowed to extend " + EntityCacheMaster.CACHE_HARDCODED);
    }
    catch (CacheCodeException e)
    {
      // This is expected
    }
  }

  /**
   * Tests the scenario where Parent caches all and Child caches none. Checks
   * that a {@link CacheAllBusinessDAOstrategy} is created for Parent, and
   * checks that Child defers to that collection.
   */
  @Request
  @Test
  public void testParentCachesAll()
  {
    setCaching(EntityCacheMaster.CACHE_EVERYTHING, EntityCacheMaster.CACHE_NOTHING);

    try
    {
      populate(10, 10);

      CacheStrategy parentCache = ObjectCache.getTypeCollection(parentInfo.getType());
      if (! ( parentCache instanceof CacheAllStrategy ))
      {
        Assert.fail("Parent cache collection is NOT [" + CacheAllStrategy.class.getName() + "] as expected");
      }

      CacheStrategy childCache = ObjectCache.getTypeCollection(childInfo.getType());
      if (!childCache.entityType.equals(parentInfo.getType()))
      {
        Assert.fail("Child cache collection is NOT deferreing to Parent as expected");
      }

      if (!containsAll(parentCache, parentOids))
      {
        Assert.fail("Parent cache collection is missing an instance of Parent.");
      }

      if (!globalCacheContainsAll(parentOids))
      {
        Assert.fail("Global cache is corrupt: Global cache is missing an instance of Parent.");
      }

      if (!containsAll(parentCache, childOids))
      {
        Assert.fail("Parent cache collection is missing an instance of an inherited Child");
      }

      if (!globalCacheContainsAll(childOids))
      {
        Assert.fail("Global cache is corrupt: Global cache is missing an instance of an inherited Child");
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      resetClasses();
    }

  }

  /**
   * Tests the scenario where Parent caches none and Child caches all. Checks
   * that a {@link CacheNoneStrategy} is created for Parent, and a
   * {@link CacheALLBusinessDAOstrategy } is created for Child. Also tests that
   * all Child instances are included in the collection.
   */
  @Request
  @Test
  public void testParentCachesNone()
  {
    setCaching(EntityCacheMaster.CACHE_NOTHING, EntityCacheMaster.CACHE_EVERYTHING);
    try
    {
      populate(10, 10);

      CacheStrategy parentCache = ObjectCache.getTypeCollection(parentInfo.getType());
      if (! ( parentCache instanceof CacheNoneStrategy ))
      {
        Assert.fail("Parent cache collection is NOT [" + CacheNoneStrategy.class.getName() + "] as expected");
      }

      CacheStrategy childCache = ObjectCache.getTypeCollection(childInfo.getType());
      if (! ( childCache instanceof CacheAllStrategy ))
      {
        Assert.fail("Child cache collection is NOT [" + CacheAllBusinessDAOstrategy.class.getName() + "] as expected");
      }

      if (parentCache.entityDAOIdSet.size() != 0)
      {
        Assert.fail("Parent is set to cache none, but the cache collection is NOT empty");
      }

      if (!containsAll(childCache, childOids))
      {
        Assert.fail("Child cache collection is missing an instance of Child");
      }

      if (!globalCacheContainsAll(childOids))
      {
        Assert.fail("Global cache is corrupt: Child cache collection is missing an instance of Child");
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      resetClasses();
    }
  }

  /**
   * Tests the scenario where Parent caches none and Child caches all, but the
   * parent then switches to all. Checks that the child collection is changed to
   * none and all instances of child and parent are picked up in the new parent
   * collection.
   */
  @Request
  @Test
  public void testParentSwitchToAll()
  {

    setCaching(EntityCacheMaster.CACHE_NOTHING, EntityCacheMaster.CACHE_EVERYTHING);

    try
    {
      populate(10, 10);

      MdBusinessDAO parentMD = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(parentInfo.getType()) ).getBusinessDAO();
      parentMD.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getOid());
      parentMD.apply();

      CacheStrategy parentCache = ObjectCache.getTypeCollection(parentInfo.getType());
      if (! ( parentCache instanceof CacheAllStrategy ))
      {
        Assert.fail("Parent cache collection is NOT [" + CacheAllStrategy.class.getName() + "] as expected");
      }

      CacheStrategy childCache = ObjectCache.getTypeCollection(childInfo.getType());
      if (!childCache.entityType.equals(parentInfo.getType()))
      {
        Assert.fail("Child cache collection is NOT deferreing to Parent as expected");
      }

      if (!containsAll(parentCache, parentOids))
      {
        Assert.fail("Parent cache collection is missing an instance of Parent");
      }

      if (!globalCacheContainsAll(parentOids))
      {
        Assert.fail("Global cache is corrupt: Global cache is missing an instance of Parent");
      }

      if (!containsAll(parentCache, childOids))
      {
        Assert.fail("Parent cache collection is missing an instance of an inherited Child");
      }

      if (!globalCacheContainsAll(childOids))
      {
        Assert.fail("Global cache is corrupt:  Global cache is missing an instance of an inherited Child");
      }

    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      resetClasses();
    }

  }

  /**
   * Tests the Most Recently Used algorithm in an inherited structure. Checks
   * that the collection contains the most recent instances of the parent and
   * child.
   */
  @Request
  @Test
  public void testParentMRU()
  {
    setCaching(EntityCacheMaster.CACHE_MOST_RECENTLY_USED, EntityCacheMaster.CACHE_NOTHING);

    try
    {
      // Find the size of the MRU cache. We need to populate more than that for
      // an interesting test scenario
      int cacheSize = MdBusinessDAO.getMdBusinessDAO(parentInfo.getType()).getCacheSize();
      populate(cacheSize * 2, 25);

      CacheStrategy parentCache = ObjectCache.getTypeCollection(parentInfo.getType());
      if (! ( parentCache instanceof CacheMRUStrategy ))
      {
        Assert.fail("Parent cache collection is NOT [" + CacheMRUStrategy.class.getName() + "] as expected");
      }
      CacheStrategy childCache = ObjectCache.getTypeCollection(childInfo.getType());
      if (!childCache.entityType.equals(parentInfo.getType()))
      {
        Assert.fail("Child cache collection is NOT deferreing to Parent as expected");
      }
      // Since we know the ordr of population, we also know that the end of the
      // list is the most recently used - get that sublist
      List<String> cachedParentOids = parentOids.subList(parentOids.size() - cacheSize + 25, parentOids.size());
      if (!containsAll(parentCache, cachedParentOids))
      {
        Assert.fail("Parent cache collection is missing an instance of Parent");
      }
      if (!containsAll(parentCache, childOids))
      {
        Assert.fail("Parent cache collection is missing an instance of an inherited Child");
      }

      if (!globalCacheContainsAll(childOids))
      {
        Assert.fail("Global cache is corrupt: Global cache collection is missing an instance of an inherited Child");
      }

      // The front portion of the list should have been bumped out of the cache
      List<String> uncachedParentOids = parentOids.subList(0, parentOids.size() - cacheSize + 25);
      for (String oid : uncachedParentOids)
      {
        if (parentCache.entityDAOIdSet.contains(oid))
        {
          Assert.fail("MRU collection contains items that were not used recently.");
        }
      }

      // The list should also have been bumped out of the global cache.
      for (String oid : uncachedParentOids)
      {
        if (ObjectCache.globalCacheContainsId(oid))
        {
          Assert.fail("Global cache is corrupt:  Global cache contains items that were not used recently.");
        }
      }

    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      resetClasses();
    }
  }

  /**
   * When a parent caches MRU and a Child caches all, the parent's collection
   * conatins no isntances of the child. This tests that scenario.
   */
  @Request
  @Test
  public void testParentMRU_ChildAll()
  {
    setCaching(EntityCacheMaster.CACHE_MOST_RECENTLY_USED, EntityCacheMaster.CACHE_EVERYTHING);

    try
    {
      // Find the size of the MRU cache. We need to populate more than that for
      // an interesting test scenario
      int cacheSize = MdBusinessDAO.getMdBusinessDAO(parentInfo.getType()).getCacheSize();
      populate(cacheSize + 25, 25);

      CacheStrategy parentCache = ObjectCache.getTypeCollection(parentInfo.getType());
      if (! ( parentCache instanceof CacheMRUStrategy ))
      {
        Assert.fail("Parent cache collection is NOT [" + CacheMRUStrategy.class.getName() + "] as expected");
      }
      CacheStrategy childCache = ObjectCache.getTypeCollection(childInfo.getType());
      if (! ( childCache instanceof CacheAllStrategy ))
      {
        Assert.fail("Child cache collection is NOT [" + CacheAllStrategy.class.getName() + "] as expected");
      }
      // Since we know the order of population, we also know that the end of the
      // list is the most recently used - get that sublist
      List<String> cachedParentOids = parentOids.subList(25, parentOids.size());
      if (!containsAll(parentCache, cachedParentOids))
      {
        Assert.fail("Parent cache collection is missing an instance of Parent");
      }

      if (!globalCacheContainsAll(cachedParentOids))
      {
        Assert.fail("Global cache is corrupt: Global cache is missing an instance of Parent");
      }

      // The child caches all, so we check its collection
      if (!containsAll(childCache, childOids))
      {
        Assert.fail("Child cache collection is missing an instance of Child");
      }

      if (!globalCacheContainsAll(childOids))
      {
        Assert.fail("Global cache is corrupt: Child cache collection is missing an instance of Child");
      }

      // The front portion of the list should have been bumped out of the cache
      List<String> uncachedParentOids = parentOids.subList(0, 25);
      for (String oid : uncachedParentOids)
      {
        if (parentCache.entityDAOIdSet.contains(oid))
        {
          Assert.fail("MRU collection contains items that were not used recently.");
        }
      }

      for (String oid : uncachedParentOids)
      {
        if (ObjectCache.globalCacheContainsId(oid))
        {
          Assert.fail("Global cache is corrupt:  Global cache contains items that were not used recently.");
        }
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      resetClasses();
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testParentMRU_ChildSwitchToAll()
  {
    setCaching(EntityCacheMaster.CACHE_MOST_RECENTLY_USED, EntityCacheMaster.CACHE_NOTHING);

    try
    {
      // Find the size of the MRU cache. We need to populate more than that for
      // an interesting test scenario
      int cacheSize = MdBusinessDAO.getMdBusinessDAO(parentInfo.getType()).getCacheSize();
      populate(cacheSize * 2, 25);

      MdBusinessDAO childMD = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(childInfo.getType()) ).getBusinessDAO();

      // Set the child to start caching everything
      childMD.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getOid());
      childMD.apply();

      CacheStrategy parentCache = ObjectCache.getTypeCollection(parentInfo.getType());
      if (! ( parentCache instanceof CacheMRUStrategy ))
      {
        Assert.fail("Parent cache collection is NOT [" + CacheMRUStrategy.class.getName() + "] as expected");
      }

      CacheStrategy childCache = ObjectCache.getTypeCollection(childInfo.getType());
      if (! ( childCache instanceof CacheAllStrategy ))
      {
        Assert.fail("Child cache collection is NOT [" + CacheAllStrategy.class.getName() + "] as expected");
      }

      // The child caches all, so we check its collection
      if (!containsAll(childCache, childOids))
      {
        Assert.fail("Child cache collection is missing an instance of Child");
      }

      if (!globalCacheContainsAll(childOids))
      {
        Assert.fail("Global cache is corrupt: Global cache is missing an instance of Child");
      }

      // Since the child is caching all, the parent collection should have no
      // instances of the child.
      // The child should still be referenced in the global cache.
      for (String oid : childOids)
      {
        if (parentCache.entityDAOIdSet.contains(oid))
        {
          Assert.fail("Parent collection still has instances of Child");
        }
        if (!ObjectCache.globalCacheContainsId(oid))
        {
          Assert.fail("Global cache is corrupt: Global cache does not contain an instance of Child");
        }
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      resetClasses();
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testDroppedAttribute()
  {
    setCaching(EntityCacheMaster.CACHE_MOST_RECENTLY_USED, EntityCacheMaster.CACHE_EVERYTHING);

    MdBusinessDAOIF parentMdIF = MdBusinessDAO.getMdBusinessDAO(parentInfo.getType());

    try
    {
      // Add an attribute that we'll drop
      MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "dropMe");
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "8");
      mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "String to be Dropped");
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "Sup Yo");
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      mdAttributeCharacter.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, parentMdIF.getOid());
      mdAttributeCharacter.apply();

      populate(10, 10);

      // Now we check to make sure that all items in both caches have the new
      // attribute
      for (String oid : parentOids)
      {
        EntityDAOIF parent = ObjectCache.getEntityDAO(oid);
        parent.getAttributeIF("dropMe");
      }

      for (String oid : childOids)
      {
        EntityDAOIF child = ObjectCache.getEntityDAO(oid);
        child.getAttributeIF("dropMe");
      }

      // No Exceptions means all cached instances have the new attribute. Drop
      // it.
      mdAttributeCharacter.delete();

      // Now we check to make sure that all items in both caches have dropped
      // the attribute
      for (String oid : parentOids)
      {
        // Check the global cached
        // Make sure it gets into the cache.
        ObjectCache.getEntityDAO(oid);
        EntityDAOIF parent = ObjectCache.getEntityDAOIFfromCache(oid);
        try
        {
          parent.getAttributeIF("dropMe");
          Assert.fail("Global cache is corrupt.  A cached instance of the parent type still contained a dropped attribute.");
        }
        catch (AttributeDoesNotExistException e)
        {
          // We want to catch the error - that attribute doesn't exist
        }
      }

      for (String oid : childOids)
      {
        // Check the global cached
        // Make sure it gets into the cache.
        ObjectCache.getEntityDAO(oid);
        EntityDAOIF child = ObjectCache.getEntityDAOIFfromCache(oid);
        try
        {
          child.getAttributeIF("dropMe");
          Assert.fail("Global cache is corrupt.  A cached instance of the child type still contained a dropped attribute.");
        }
        catch (AttributeDoesNotExistException e)
        {
          // We want to catch the error - that attribute doesn't exist
        }
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      resetClasses();
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testCacheMRU_SizeAccurate()
  {
    MdBusinessDAO accurDO = null;
    String size = "10";
    try
    {
      TypeInfo accurClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Accuracy1");

      accurDO = MdBusinessDAO.newInstance();
      accurDO.setValue(MdBusinessInfo.NAME, accurClass.getTypeName());
      accurDO.setValue(MdBusinessInfo.PACKAGE, accurClass.getPackageName());
      accurDO.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      accurDO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, accurClass.getTypeName() + " test type");
      accurDO.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit TestCache Type");
      accurDO.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      accurDO.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      accurDO.setValue(MdBusinessInfo.CACHE_SIZE, size);
      accurDO.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getOid());
      accurDO.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      accurDO.apply();

      // create a bunch of teacher instances
      List<String> objectIdList = new LinkedList<String>();

      BusinessDAO accur;
      for (int i = 0; i < Integer.parseInt(size); i++)
      {
        accur = BusinessDAO.newInstance(accurClass.getType());
        String oid = accur.apply();
        BusinessDAO.get(oid);
        objectIdList.add(oid);
      }

      // get number of objects
      CacheStrategy coll = ObjectCache.getTypeCollection(accurClass.getType());
      int count = coll.getCachedIds().size();
      if (count != Integer.parseInt(size))
      {
        Assert.fail("The MRU cache size did not equal the size originally set");
      }

      for (String oid : objectIdList)
      {
        if (!ObjectCache.globalCacheContainsId(oid))
        {
          Assert.fail("The global object cache is corrupt. It is missing a reference to an object");
        }
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (accurDO != null && !accurDO.isNew())
        accurDO.delete();
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testDeleteEnumeration()
  {
    TypeInfo status = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Status1");
    TypeInfo statusEnum = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "StatusEnum");
    MdBusinessDAOIF enumerationAttributeMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    // Define an EnumerationMaster class
    MdBusinessDAO statusMdBusiness = MdBusinessDAO.newInstance();
    statusMdBusiness.setValue(MdBusinessInfo.NAME, status.getTypeName());
    statusMdBusiness.setValue(MdBusinessInfo.PACKAGE, status.getPackageName());
    statusMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Auction Status");
    statusMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    statusMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumerationAttributeMdBusinessIF.getOid());
    statusMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    status.setOid(statusMdBusiness.apply());

    // Define an MdEnumeration
    MdEnumerationDAO statusMdEnumeration = MdEnumerationDAO.newInstance();
    statusMdEnumeration.setValue(MdEnumerationInfo.NAME, statusEnum.getTypeName());
    statusMdEnumeration.setValue(MdEnumerationInfo.PACKAGE, statusEnum.getPackageName());
    statusMdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Auction Statuses");
    statusMdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    statusMdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, statusMdBusiness.getOid());
    statusMdEnumeration.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    statusEnum.setOid(statusMdEnumeration.apply());

    // Verify that the MdEnumeration made it into the cache and the database
    ResultSet resultSet = Database.selectFromWhere(EntityDAOIF.ID_COLUMN, MdEnumerationDAOIF.TABLE, EntityDAOIF.ID_COLUMN + "='" + statusEnum.getOid() + "'");

    int loopCount = 0;

    try
    {
      while (resultSet.next())
      {
        loopCount++;
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    if (loopCount != 1)
    {
      Assert.fail("MdEnumeration is not in the database after .apply()");
    }
    if (!MdTypeDAO.isDefined(statusEnum.getType()))
    {
      Assert.fail("MdEnumeration is not in the system after .apply()");
    }

    if (!ObjectCache.globalCacheContainsId(statusMdEnumeration.getOid()))
    {
      Assert.fail("The global cache is corrupt.  It does not contain a reference to a newly created MdEnumeration.");
    }

    // Now delete it
    BusinessDAO.get(statusMdEnumeration.getOid()).getBusinessDAO().delete();
    BusinessDAO.get(statusMdBusiness.getOid()).getBusinessDAO().delete();

    resultSet = Database.selectFromWhere(EntityDAOIF.ID_COLUMN, MdEnumerationDAOIF.TABLE, EntityDAOIF.ID_COLUMN + "='" + statusEnum.getOid() + "'");

    loopCount = 0;

    try
    {
      while (resultSet.next())
      {
        loopCount++;
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    // Verify that the MdEnumeration has been removed from both the cache and
    // the database
    if (loopCount != 0)
    {
      Assert.fail("MdEnumeration is still in the database after being deleted");
    }
    if (MdTypeDAO.isDefined(statusEnum.getType()))
    {
      Assert.fail("MdEnumeration is still in the cache after being deleted");
    }

    // ObjectCache.flushCache();

    if (ObjectCache.globalCacheContainsId(statusMdEnumeration.getOid()))
    {
      Assert.fail("The global cache is corrupt.  It contains a reference to a deleted MdEnumeration.");
    }

  }

  /**
   *
   */
  @Request
  @Test
  public void testCacheMRU_SizeChange()
  {
    MdBusinessDAO accurDO = null;
    String size = "10";
    try
    {
      TypeInfo accurClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Accuracy");

      accurDO = MdBusinessDAO.newInstance();
      accurDO.setValue(MdBusinessInfo.NAME, accurClass.getTypeName());
      accurDO.setValue(MdBusinessInfo.PACKAGE, accurClass.getPackageName());
      accurDO.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      accurDO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, accurClass.getTypeName() + " test type");
      accurDO.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit TestCache Type");
      accurDO.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      accurDO.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      accurDO.setValue(MdBusinessInfo.CACHE_SIZE, size);
      accurDO.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getOid());
      accurDO.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      accurDO.apply();

      // create a bunch of teacher instances
      List<String> oldTeacherList = new LinkedList<String>();

      BusinessDAO accur;
      for (int i = 0; i < Integer.parseInt(size); i++)
      {
        accur = BusinessDAO.newInstance(accurClass.getType());
        String oid = accur.apply();
        BusinessDAO.get(oid);
        oldTeacherList.add(oid);
      }

      String newSize = "7";
      accurDO.setValue(MdBusinessInfo.CACHE_SIZE, newSize);
      accurDO.apply();

      List<String> newTeacherList = new LinkedList<String>();

      for (int i = 0; i < Integer.parseInt(newSize); i++)
      {
        accur = BusinessDAO.newInstance(accurClass.getType());
        String oid = accur.apply();
        BusinessDAO.get(oid);
        newTeacherList.add(oid);
      }

      // get number of objects
      CacheStrategy coll = ObjectCache.getTypeCollection(accurClass.getType());
      int count = coll.getCachedIds().size();
      if (count != Integer.parseInt(newSize))
      {
        Assert.fail("The MRU cache size did not equal the newly set size");
      }

      for (String oid : oldTeacherList)
      {
        if (ObjectCache.globalCacheContainsId(oid))
        {
          Assert.fail("The global object cache is corrupt. Global cache contains a reference to an object that should have been removed from the cache.");
        }
      }

      for (String oid : newTeacherList)
      {
        if (!ObjectCache.globalCacheContainsId(oid))
        {
          Assert.fail("The global object cache is corrupt. It is missing a reference to an object");
        }
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (accurDO != null && !accurDO.isNew())
        accurDO.delete();
    }
  }
}
