/**
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
 */
/**
*
*/
package com.runwaysdk.system.scheduler;

import java.util.Date;
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
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

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
    
    
    // Run this in pgadmin if you get "contraint on object violated", sometimes the database gets screwy.
//    truncate abstract_job;
//    truncate executable_job;
//    truncate job_history;
//    truncate job_history_record;
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
  public static class TestJob implements ExecutableJobIF
  {
    /**
     * Execution method that modifies its associated TestRecord.
     */
    @Override
    public void execute(ExecutionContext executionContext)
    {
      ExecutableJob job = executionContext.getJob();
      String id = job.getJobId();
      
      JobHistory history = executionContext.getJobHistory();
      history.lock();
      try
      {
        Thread.sleep(1000);
      }
      catch (InterruptedException e)
      {
        throw new RuntimeException(e);
      }
      history.unlock();
 
      TestRecord testRecord = TestRecord.records.get(id);
      testRecord.recordOnce();
    }
  }

  /*
   * Basic job that errors when executed.
   */
  public static class TestErrorJob implements ExecutableJobIF
  {

    /**
     * 
     */
    @Override
    public void execute(ExecutionContext executionContext)
    {
      throw new ProgrammingErrorException("Failed on purpose.");
    }
  }
  
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

  private void clearHistory()
  {
    JobHistoryRecordQuery query = new JobHistoryRecordQuery(new QueryFactory());
    OIterator<? extends JobHistoryRecord> jhrs = query.getIterator();
    
    while (jhrs.hasNext())
    {
      JobHistoryRecord jhr = jhrs.next();
      jhr.getChild().delete();
    }
  }

  /**
   * Tests the execution of a job via CRON scheduling.
   * 
   * @throws InterruptedException
   */
  public void testCRONSchedule() throws InterruptedException
  {
    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
    job.setJobId("testCRONSchedule");
    job.setCronExpression("0/5 * * * * ?");

    try
    {
      job.apply();

      TestRecord tr = TestRecord.newRecord(job);

      wait(tr, 20);

      if (tr.isExecuted() && tr.getCount() == 1)
      {
        assertEquals(0, SchedulerManager.getRunningJobs().size());
      }
      else
      {
        fail("The job was not completed.");
      }
    }
    finally
    {
      ExecutableJob.get(job.getId()).delete();
      clearHistory();
    }
  }

  /**
   * Tests the execution of a job once via invoking the "start" MDMethod.
   * 
   * @throws InterruptedException
   */
  public void testManuallyStartJob() throws InterruptedException
  {
    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
    job.setJobId("testManuallyStartJob");

    try
    {
      job.apply();

      TestRecord tr = TestRecord.newRecord(job);

      JobHistory history = job.start();
      Date startTime = history.getStartTime();
      
      assertNotNull(startTime);
      assertTrue(history.getStatus().contains(AllJobStatus.RUNNING));
      assertEquals(1, SchedulerManager.getRunningJobs().size());

      wait(tr, 10);

      if (tr.isExecuted() && tr.getCount() == 1)
      {
        JobHistory updated = JobHistory.getByKey(history.getKey());
        assertTrue(updated.getStatus().get(0).equals(AllJobStatus.SUCCESS));
        assertEquals(0, SchedulerManager.getRunningJobs().size());
        assertNotNull(updated.getEndTime());
        assertTrue(updated.getEndTime().after(startTime));
      }
      else
      {
        fail("The job was not completed.");
      }
    }
    finally
    {
      ExecutableJob.get(job.getId()).delete();
      clearHistory();
    }
  }
  
  /**
   * Tests to make sure that a job will stop running when the CRON string is modified. Also tests to make sure
   * that jobs can be modified while running.
   * 
   * @throws InterruptedException
   */
  public void testModifyCRONSchedule() throws InterruptedException
  {
    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
    job.setJobId("testModifyCRONSchedule");
    job.setCronExpression("0/5 * * * * ?");

    try
    {
      job.apply();

      TestRecord tr = TestRecord.newRecord(job);
      
      // Wait till the job is running
      int waitTime = 0;
      while (SchedulerManager.getRunningJobs().size() == 0)
      {
        Thread.sleep(10);
        
        waitTime += 10;
        if (waitTime > 6000)
        {
          fail("Job was never scheduled");
          return;
        }
      }
      
      // Modify the CRON string to never run while the job is currently executing.
      job = ExecutableJob.get(job.getId());
      job.setCronExpression("");
      job.apply();
      
      // Wait till the job is no longer running
      wait(tr, 30);
      
      // Make sure the job never starts up again.
      waitTime = 0;
      while (waitTime < 10000)
      {
        Thread.sleep(100);
        waitTime += 100;
        
        assertEquals(0, SchedulerManager.getRunningJobs().size());
      }
    }
    finally
    {
      ExecutableJob.get(job.getId()).delete();
      clearHistory();
    }
  }
  
  /**
   * Tests the clearHistory MdMethod defined on JobHistory.
   */
  public void testClearHistory()
  {
    ExecutableJob job1 = QualifiedTypeJob.newInstance(TestJob.class);
    job1.setJobId("testClearHistory1");
    job1.apply();
    TestRecord tr1 = TestRecord.newRecord(job1);
    
    ExecutableJob job2 = QualifiedTypeJob.newInstance(TestJob.class);
    job2.setJobId("testClearHistory2");
    job2.apply();
//    TestRecord tr2 = TestRecord.newRecord(job2);
    TestRecord.newRecord(job2);
    
    try
    {
      // First create a history item by running job1.
      job1.start();
      wait(tr1, 10);

      assertEquals(1, new JobHistoryQuery(new QueryFactory()).getCount());
      
      job2.start();
      
      Thread.sleep(10);
      
      assertEquals(2, new JobHistoryQuery(new QueryFactory()).getCount());
      assertEquals(1, SchedulerManager.getRunningJobs().size());
      
      // Invoke the md method. This should only remove 1 of the histories, because the other one is currently running.
      JobHistory.clearHistory();
      
      assertEquals(1, new JobHistoryQuery(new QueryFactory()).getCount());
      
      Thread.sleep(2000);
      
      JobHistory.clearHistory();
      
      assertEquals(0, new JobHistoryQuery(new QueryFactory()).getCount());
    }
    catch (InterruptedException e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      ExecutableJob.get(job1.getId()).delete();
      ExecutableJob.get(job2.getId()).delete();
      clearHistory();
    }
  }
  
//  public void ignoreJobHistoryView()
//  {
//
//    fail("fix me i'm broken and it hurts!");

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
//  }

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
