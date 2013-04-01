package com.runwaysdk;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.session.SessionTestSuite;

public class SessionTestSuite_MavenTest extends TestSuite
{
  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(SessionTestSuite_MavenTest.suite());
  }

  public static Test suite()
  {
    return SessionTestSuite.suite();
  }

}
