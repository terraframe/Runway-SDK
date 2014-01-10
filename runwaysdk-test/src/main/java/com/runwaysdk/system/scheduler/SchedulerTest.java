/**
*
*/
package com.runwaysdk.system.scheduler;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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
public class SchedulerTest extends TestCase
{

  private static class TestJobListener implements JobListener
  {

    @Override
    public String getName()
    {
      return "Test Listener";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.system.scheduler.JobListener#onStart()
     */
    @Override
    public void onStart(Job job)
    {
      System.out.println("START: " + job.getJobId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.runwaysdk.system.scheduler.JobListener#onCancel(com.runwaysdk.system
     * .scheduler.Job)
     */
    @Override
    public void onCancel(Job job)
    {
      System.out.println("CANCEL: " + job.getJobId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.system.scheduler.JobListener#onStop()
     */
    @Override
    public void onStop(Job job)
    {
      System.out.println("STOP: " + job.getJobId());
    }

  }

  private static class TestJob implements ExecutableJob
  {

    private static boolean executed = false;

    /**
     * 
     */
    public TestJob()
    {
    }

    /**
     * 
     */
    @Override
    public void execute()
    {
      executed = true;
    }

    /**
     * @return the executed
     */
    public static boolean isExecuted()
    {
      return executed;
    }
  }

  public static void main(String args[])
  {
    TestSuite suite = new TestSuite();

    suite.addTest(SchedulerTest.suite());

    junit.textui.TestRunner.run(suite);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(SchedulerTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        classSetUp();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  public static void classSetUp()
  {
    SchedulerManager.start();
  }

  public static void classTearDown()
  {
    SchedulerManager.shutdown();
  }

  public void testJobComplete()
  {
    Job job = CustomJob.newInstance(TestJob.class);

    job.addJobListener(new TestJobListener());

    try
    {
      job.apply();

      job.start();

      Thread.sleep(3000);

      if (!TestJob.isExecuted())
      {
        fail("The job was not completed.");
      }

    }
    catch (InterruptedException e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      if (!job.isNew())
      {
        job.delete();
      }
    }
  }

}
