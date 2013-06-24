package com.runwaysdk;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class UeberTestSuite_MavenTest extends TestSuite
{
  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public static Test suite()
  {
    return UeberTestSuite.suite();
  }

}
