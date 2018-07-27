package com.runwaysdk.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.dataaccess.EntityAttributeIndicatorTest;
import com.runwaysdk.dataaccess.EntityMasterTestSetup;
import com.runwaysdk.session.Request;

@RunWith(Suite.class)
@Suite.SuiteClasses({ EntityAttributeIndicatorTest.class, })
public class EntityQueryTestSuite
{
  private static EntityMasterTestSetup setup = new EntityMasterTestSetup(EntityCacheMaster.CACHE_NOTHING.getCacheCode());

  @Request
  @BeforeClass
  public static void suiteClassSetup() throws Exception
  {
    setup.setUp();
  }

  @Request
  @AfterClass
  public static void suiteTearDown() throws Exception
  {
    setup.tearDown();
  }

}
