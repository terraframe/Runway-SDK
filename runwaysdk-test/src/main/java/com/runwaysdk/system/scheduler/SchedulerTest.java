/**
*
*/
package com.runwaysdk.system.scheduler;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;

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

  private static final boolean     HALT          = false;

  protected static ClientSession   systemSession = null;

  protected static ClientRequestIF clientRequest = null;

  // TEST BOILERPLATE
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
        systemSession = ClientSession.createUserSession("default", ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
        clientRequest = systemSession.getRequest();
        classSetUp();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  // TEST BOILERPLATE

  /*
   * Tracks execution by Jobs.
   */
  private static class TestRecord
  {
    private static Map<String, TestRecord> records = new ConcurrentHashMap<String, TestRecord>();

    /**
     * The id of the Job that this is recorded against.
     */
    private final String                   id;

    /**
     * The number of executions.
     */
    private int                            count;

    /**
     * Denotes if the job was executed.
     */
    private boolean                        executed;

    private TestRecord(String id)
    {
      this.id = id;
      this.count = 0;
      this.executed = false;
    }

    /**
     * @return the id
     */
    public String getId()
    {
      return id;
    }

    /**
     * @return the count
     */
    public synchronized int getCount()
    {
      return count;
    }

    /**
     * @return the executed
     */
    public synchronized boolean isExecuted()
    {
      return executed;
    }

    private synchronized void recordOnce()
    {
      if (this.count > 0)
      {
        throw new ProgrammingErrorException("Job [" + id + "] has already executed.");
      }
      else
      {
        this.record();
      }
    }

    private synchronized void record()
    {
      this.executed = true;
      this.count++;
    }

    private static TestRecord newRecord(ExecutableJob job)
    {
      String id = job.getJobId();
      synchronized (records)
      {
        if (records.containsKey(id))
        {
          throw new ProgrammingErrorException("Job [" + id + "] already recorded.");
        }
        else
        {
          TestRecord tr = new TestRecord(id);
          records.put(id, tr);
          return tr;
        }

      }
    }
  }

  /*
   * Basic job that records its execution count.
   */
  private static class TestJob implements ExecutableJobIF
  {
    /**
     * Execution method that modifies its associated TestRecord.
     */
    @Override
    public void execute(ExecutionContext executionContext)
    {
      ExecutableJob job = executionContext.getJob();
      String id = job.getJobId();

      TestRecord testRecord = TestRecord.records.get(id);
      testRecord.recordOnce();
    }
  }

//  /*
//   * Basic job that errors when executed.
//   */
//  private static class TestErrorJob implements ExecutableJobIF
//  {
//
//    /**
//     * 
//     */
//    @Override
//    public void execute(ExecutionContext executionContext)
//    {
//      throw new ProgrammingErrorException("Failed on purpose.");
//    }
//  }

  public static void classSetUp()
  {
    SchedulerManager.start();
  }

  public static void classTearDown()
  {
    systemSession.logout();
    SchedulerManager.shutdown();
  }

  /**
   * Custom wait method that stalls until the TestRecord is modified and only
   * within a specific number of retries.
   * 
   * @param tr
   * @param maxWaits
   */
  private void wait(TestRecord tr, int maxWaits)
  {
    try
    {
      int runs = 0;
      while (!tr.isExecuted())
      {
        if (runs > maxWaits)
        {
          fail("The record [" + tr.getId() + "] took longer than [" + maxWaits + "] retries to complete.");
        }

        // Let's wait a while and try again.
        Thread.sleep(HALT ? Long.MAX_VALUE : 1000);
        runs++;
      }
    }
    catch (InterruptedException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Waits (roughly) one second for the ExecutableJob to complete.
   * 
   * @param tr
   */
  private void wait(TestRecord tr)
  {
    wait(tr, 10);
  }

  // /**
  // * Tests the execution of a job once.
  // */
  // public void ignoreJobCompleted()
  // {
  // ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
  //
  // try
  // {
  // job.apply();
  //
  // TestRecord tr = TestRecord.newRecord(job);
  // job.start();
  //
  // wait(tr);
  //
  // if (tr.isExecuted() && tr.getCount() == 1)
  // {
  // ExecutableJob updated = ExecutableJob.get(job.getId());
  // // assertTrue(updated.getCompleted());
  // }
  // else
  // {
  // fail("The job was not completed.");
  // }
  // }
  //
  // finally
  // {
  // if (!job.isNew())
  // {
  // job.delete();
  // }
  // }
  // }
  //

  /**
   * Tests the execution of a job once.
   * 
   * @throws InterruptedException
   */
  public void testScheduledCompleted() throws InterruptedException
  {
    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
    job.setCronExpression("*/5 * * * * ?");

    try
    {
      job.apply();

      TestRecord tr = TestRecord.newRecord(job);

      wait(tr, 5000);

      if (tr.isExecuted() && tr.getCount() == 1)
      {
//        ExecutableJob updated = ExecutableJob.get(job.getId());
        // assertTrue(updated.getCompleted());
      }
      else
      {
        fail("The job was not completed.");
      }
    }
    finally
    {
      TestFixtureFactory.delete(job);
    }
  }

  /**
   * Tests the execution of a job once.
   * 
   * @throws InterruptedException
   */
  public void testAdHocRunOfScheduledJob() throws InterruptedException
  {
    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
    job.setCronExpression("*/5 * * * * ?");

    try
    {
      job.apply();

      TestRecord tr = TestRecord.newRecord(job);

      job.start();

      wait(tr, 5000);

      if (tr.isExecuted() && tr.getCount() == 1)
      {
//        ExecutableJob updated = ExecutableJob.get(job.getId());
        // assertTrue(updated.getCompleted());
      }
      else
      {
        fail("The job was not completed.");
      }
    }
    finally
    {
      TestFixtureFactory.delete(job);
    }
  }

  public void ignoreJobHistoryView()
  {

    fail("fix me i'm broken and it hurts!");

    // ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
    // job.setJobId("job 2");
    // job.setCompleted(true);
    // job.setRunning(false);
    // job.apply();
    //
    // JobSnapshot snap = new JobSnapshot();
    // snap.setCancelable(job.getCancelable());
    // snap.setCanceled(job.getCanceled());
    // snap.setCompleted(job.getCompleted());
    // snap.setCronExpression(job.getCronExpression());
    // snap.setEndTime(job.getEndTime());
    // snap.setLastRun(job.getLastRun());
    // snap.setMaxRetries(job.getMaxRetries());
    // snap.setPauseable(job.getPauseable());
    // snap.setPaused(job.getPaused());
    // snap.setRemoveOnComplete(job.getRemoveOnComplete());
    // snap.setRepeated(job.getRepeated());
    // snap.setRetries(job.getRetries());
    // snap.setRunning(job.getRunning());
    // snap.setStartOnCreate(job.getStartOnCreate());
    // snap.setStartTime(job.getStartTime());
    // snap.setTimeout(job.getTimeout());
    // snap.setWorkProgress(job.getWorkProgress());
    // snap.setWorkTotal(job.getWorkTotal());
    // snap.apply();
    //
    // JobHistory history = new JobHistory();
    // history.setJobSnapshot(snap);
    // history.apply();
    //
    // JobHistoryRecord rec = new JobHistoryRecord(job, history);
    // rec.apply();
    //
    // JobHistoryViewQueryDTO view =
    // JobHistoryViewDTO.getJobHistories(clientRequest, "jobId", false, 10, 1);
    // List<? extends JobHistoryViewDTO> results = view.getResultSet();
    //
    // assertTrue(results.size() > 0);
  }

  // /**
  // * Tests that a Jobs start, end, and duration times are set correctly.
  // */
  // public void ignoreExecutionTiming()
  // {
  // fail("not implemented");
  //
  // // start, stop, duration, lastRun
  // }
  //
  // /**
  // * Tests the start listener.
  // */
  // public void ignoreStartAndStopListener()
  // {
  // ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
  // final MutableBoolean onStartFired = new MutableBoolean(false);
  // final MutableBoolean onStopFired = new MutableBoolean(false);
  //
  // job.addJobListener(new JobListener()
  // {
  //
  // public String getName()
  // {
  // return "testStartAndStopListener";
  // }
  //
  // @Override
  // public void onStart(ExecutionContext context)
  // {
  // onStartFired.setValue(true);
  // }
  //
  // public void onCancel(ExecutionContext context)
  // {
  // }
  //
  // public void onStop(ExecutionContext context)
  // {
  // onStopFired.setValue(true);
  // }
  // });
  //
  // try
  // {
  // job.apply();
  //
  // TestRecord tr = TestRecord.newRecord(job);
  // job.start();
  //
  // wait(tr);
  //
  // if (!onStartFired.booleanValue())
  // {
  // fail("The start listener did not fire.");
  // }
  //
  // if (!onStopFired.booleanValue())
  // {
  // fail("The stop listener did not fire.");
  // }
  // }
  //
  // finally
  // {
  // if (!job.isNew())
  // {
  // job.delete();
  // }
  // }
  // }
  //
  // /**
  // * Ensures that multiple listeners can fire for one job.
  // */
  // public void ignoreMultipleListeners()
  // {
  // ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
  //
  // // #1
  // final MutableBoolean onStartFired1 = new MutableBoolean(false);
  // final MutableBoolean onStopFired1 = new MutableBoolean(false);
  // job.addJobListener(new JobListener()
  // {
  //
  // public String getName()
  // {
  // return "testMultipleListeners1";
  // }
  //
  // @Override
  // public void onStart(ExecutionContext context)
  // {
  // onStartFired1.setValue(true);
  // }
  //
  // public void onCancel(ExecutionContext context)
  // {
  // }
  //
  // public void onStop(ExecutionContext context)
  // {
  // onStopFired1.setValue(true);
  // }
  // });
  //
  // // #2
  // final MutableBoolean onStartFired2 = new MutableBoolean(false);
  // final MutableBoolean onStopFired2 = new MutableBoolean(false);
  // job.addJobListener(new JobListener()
  // {
  //
  // public String getName()
  // {
  // return "testMultipleListeners2";
  // }
  //
  // @Override
  // public void onStart(ExecutionContext context)
  // {
  // onStartFired2.setValue(true);
  // }
  //
  // public void onCancel(ExecutionContext context)
  // {
  // }
  //
  // public void onStop(ExecutionContext context)
  // {
  // onStopFired2.setValue(true);
  // }
  // });
  //
  // try
  // {
  // job.apply();
  //
  // TestRecord tr = TestRecord.newRecord(job);
  // job.start();
  //
  // wait(tr);
  //
  // if (!onStartFired1.booleanValue())
  // {
  // fail("The start listener #1 did not fire.");
  // }
  //
  // if (!onStopFired1.booleanValue())
  // {
  // fail("The stop listener #1 did not fire.");
  // }
  //
  // if (!onStartFired1.booleanValue())
  // {
  // fail("The start listener #2 did not fire.");
  // }
  //
  // if (!onStopFired1.booleanValue())
  // {
  // fail("The stop listener #2 did not fire.");
  // }
  // }
  //
  // finally
  // {
  // if (!job.isNew())
  // {
  // job.delete();
  // }
  // }
  // }
  //
  // /**
  // * Tests that a ExecutableJob with removeOnComplete set to true is deleted
  // * from the database when completed.
  // */
  // public void ignoreRemoveOnComplete()
  // {
  // ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
  //
  //
  // try
  // {
  // job.setRemoveOnComplete(true);
  // job.apply();
  //
  // TestRecord tr = TestRecord.newRecord(job);
  // job.start();
  //
  // wait(tr);
  //
  // if (tr.isExecuted() && tr.getCount() == 1)
  // {
  // // Make sure the ExecutableJob DB record was removed
  // try
  // {
  // ExecutableJob.get(job.getId());
  //
  // fail("The ExecutableJob was not deleted with removeOnComplete set to true.");
  // }
  // catch(DataNotFoundException e)
  // {
  // // expected
  // }
  // }
  // else
  // {
  // fail("The job was not completed.");
  // }
  //
  // }
  //
  // finally
  // {
  // try
  // {
  // ExecutableJob.get(job.getId()).delete();
  // }
  // catch(DataNotFoundException e)
  // {
  // // expected if test was successful
  // // as there's nothing to delete.
  // }
  // }
  // }
  //
  // public void ignoreCancelListener()
  // {
  // ExecutableJobIF eJob = new ExecutableJobIF()
  // {
  //
  // @Override
  // public void execute(ExecutionContext executionContext)
  // {
  // // TODO Auto-generated method stub
  //
  // }
  // };
  //
  // ExecutableJob job = QualifiedTypeJob.newInstance(eJob.getClass());
  //
  // try
  // {
  // job.apply();
  //
  // TestRecord tr = TestRecord.newRecord(job);
  // job.start();
  //
  // wait(tr);
  //
  // if (!tr.isExecuted() || tr.getCount() == 0)
  // {
  // fail("The job was not completed.");
  // }
  //
  // }
  //
  // finally
  // {
  // if (!job.isNew())
  // {
  // job.delete();
  // }
  // }
  // }
  //
  // /**
  // * Tests the start, stop, and duration time of a job
  // */
  // public void ignoreJobTiming()
  // {
  // ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
  //
  // try
  // {
  // job.apply();
  //
  // TestRecord tr = TestRecord.newRecord(job);
  // job.start();
  //
  // wait(tr);
  //
  // if (!tr.isExecuted() || tr.getCount() == 0)
  // {
  // fail("The job was not completed.");
  // }
  //
  // }
  //
  // finally
  // {
  // if (!job.isNew())
  // {
  // job.delete();
  // }
  // }
  // }
  //
  // /**
  // * Tests that many jobs can hit the system and will be handled without
  // error.
  // */
  // public void ignoreFloodJobs()
  // {
  // fail("not implemented");
  // }
  // /**
  // * Tests a job that errors.
  // */
  // public void ignoreJobError()
  // {
  // Job job = CustomJob.newInstance(TestErrorJob.class);
  //
  // job.addJobListener(new TestJobListener());
  // try
  // {
  // job.apply();
  //
  // TestRecord tr = TestRecord.newRecord(job);
  //
  // try
  // {
  // job.start();
  //
  // wait(tr);
  // }
  // catch(ProgrammingErrorException e)
  // {
  // // this is expected
  // }
  //
  // }
  //
  // finally
  // {
  // if (!job.isNew())
  // {
  // job.delete();
  // }
  // }
  // }
}
