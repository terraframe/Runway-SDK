package com.runwaysdk.dataaccess;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.session.Request;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TransientAttributeTest.class })
public class ChildSessionTestSuite
{
  @Request
  @BeforeClass
  public static void classSetup()
  {
    new SessionMasterTestSetup(SessionMasterTestSetup.CHILD_SESSION_CLASS).setUp();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    new SessionMasterTestSetup(SessionMasterTestSetup.CHILD_SESSION_CLASS).tearDown();
  }
}
