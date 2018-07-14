package com.runwaysdk.dataaccess;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.session.Request;

@RunWith(Suite.class)
@Suite.SuiteClasses({ EntityAttributeTest.class, EnumerationTest.class })
public class CacheEverythingTestSuite
{
  private static EntityMasterTestSetup entityMasterTestSetup = new EntityMasterTestSetup(EntityCacheMaster.CACHE_EVERYTHING.getCacheCode());

  @Request
  @BeforeClass
  public static void classSetup()
  {
    entityMasterTestSetup.setUp();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    entityMasterTestSetup.tearDown();
  }

}
