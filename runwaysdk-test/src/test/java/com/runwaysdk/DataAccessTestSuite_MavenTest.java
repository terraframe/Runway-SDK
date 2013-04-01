package com.runwaysdk;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.dataaccess.DataAccessTestSuite;

public class DataAccessTestSuite_MavenTest extends TestSuite
{
  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(DataAccessTestSuite_MavenTest.suite());
  }

  public static Test suite()
  {
    return DataAccessTestSuite.suite();
  }

}
