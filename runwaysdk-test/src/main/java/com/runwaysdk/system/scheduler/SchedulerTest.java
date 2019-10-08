/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

public class SchedulerTest
{

  private static final boolean     HALT          = false;

  protected static ClientSession   systemSession = null;

  protected static ClientRequestIF clientRequest = null;

  // TEST BOILERPLATE

  /*
   * Tracks execution by Jobs.
   */
  private static class TestRecord
  {
    private static Map<String, TestRecord> records = new ConcurrentHashMap<String, TestRecord>();

    /**
     * The oid of the Job that this is recorded against.
     */
    private final String                   oid;

    /**
     * The number of executions.
     */
    private int                            count;

    /**
     * Denotes if the job was executed.
     */
    private boolean                        executed;

    private TestRecord(String oid)
    {
      this.oid = oid;
      this.count = 0;
      this.executed = false;
    }

    /**
     * @return the oid
     */
    public String getOid()
    {
      return oid;
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
        throw new ProgrammingErrorException("Job [" + oid + "] has already executed.");
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
      String oid = job.getDisplayLabel().getValue();
      synchronized (records)
      {
        if (records.containsKey(oid))
        {
          throw new ProgrammingErrorException("Job [" + oid + "] already recorded.");
        }
        else
        {
          TestRecord tr = new TestRecord(oid);
          records.put(oid, tr);
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
      String oid = job.getDisplayLabel().getValue();

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

      TestRecord testRecord = TestRecord.records.get(oid);
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

  @BeforeClass
  @Request
  public static void classSetUp()
  {
    systemSession = ClientSession.createUserSession("default", ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    clientRequest = systemSession.getRequest();

    SchedulerManager.start();
  }

  @AfterClass
  @Request
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
          Assert.fail("The record [" + tr.getOid() + "] took longer than [" + maxWaits + "] retries to complete.");
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
  @Request
  @Test
  public void testCRONSchedule() throws InterruptedException
  {
    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
    job.getDisplayLabel().setValue("testCRONSchedule");
    job.setCronExpression("0/5 * * * * ?");

    try
    {
      job.apply();

      TestRecord tr = TestRecord.newRecord(job);

      wait(tr, 20);

      if (tr.isExecuted() && tr.getCount() == 1)
      {
        Assert.assertEquals(0, SchedulerManager.getRunningJobs().size());
      }
      else
      {
        Assert.fail("The job was not completed.");
      }
    }
    finally
    {
      ExecutableJob.get(job.getOid()).delete();
      clearHistory();
    }
  }

  /**
   * Tests the execution of a job once via invoking the "start" MDMethod.
   * 
   * @throws InterruptedException
   */
  @Request
  @Test
  public void testManuallyStartJob() throws InterruptedException
  {
    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
    job.getDisplayLabel().setValue("testManuallyStartJob");

    try
    {
      job.apply();

      TestRecord tr = TestRecord.newRecord(job);

      JobHistory history = job.start();
      Date startTime = history.getStartTime();

      Assert.assertNotNull(startTime);
      Assert.assertTrue(history.getStatus().contains(AllJobStatus.RUNNING));
      Assert.assertEquals(1, SchedulerManager.getRunningJobs().size());

      wait(tr, 10);

      if (tr.isExecuted() && tr.getCount() == 1)
      {
        JobHistory updated = JobHistory.getByKey(history.getKey());
        Assert.assertTrue(updated.getStatus().get(0).equals(AllJobStatus.SUCCESS));
        Assert.assertEquals(0, SchedulerManager.getRunningJobs().size());
        Assert.assertNotNull(updated.getEndTime());
        Assert.assertTrue(updated.getEndTime().after(startTime));
      }
      else
      {
        Assert.fail("The job was not completed.");
      }
    }
    finally
    {
      ExecutableJob.get(job.getOid()).delete();
      clearHistory();
    }
  }

  /**
   * Tests to make sure that a job will stop running when the CRON string is
   * modified. Also tests to make sure that jobs can be modified while running.
   * 
   * @throws InterruptedException
   */
  @Request
  @Test
  public void testModifyCRONSchedule() throws InterruptedException
  {
    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
    job.getDisplayLabel().setValue("testModifyCRONSchedule");
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
          Assert.fail("Job was never scheduled");
          return;
        }
      }

      // Modify the CRON string to never run while the job is currently
      // executing.
      job = ExecutableJob.get(job.getOid());
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

        Assert.assertEquals(0, SchedulerManager.getRunningJobs().size());
      }
    }
    finally
    {
      ExecutableJob.get(job.getOid()).delete();
      clearHistory();
    }
  }

  /**
   * Tests the clearHistory MdMethod defined on JobHistory.
   */
  @Request
  @Test
  public void testClearHistory()
  {
    ExecutableJob job1 = QualifiedTypeJob.newInstance(TestJob.class);
    job1.getDisplayLabel().setValue("testClearHistory1");
    job1.apply();
    TestRecord tr1 = TestRecord.newRecord(job1);

    ExecutableJob job2 = QualifiedTypeJob.newInstance(TestJob.class);
    job2.getDisplayLabel().setValue("testClearHistory2");
    job2.apply();
    // TestRecord tr2 = TestRecord.newRecord(job2);
    TestRecord.newRecord(job2);

    try
    {
      // First create a history item by running job1.
      job1.start();
      wait(tr1, 10);

      Assert.assertEquals(1, new JobHistoryQuery(new QueryFactory()).getCount());

      job2.start();

      Thread.sleep(10);

      Assert.assertEquals(2, new JobHistoryQuery(new QueryFactory()).getCount());
      Assert.assertEquals(1, SchedulerManager.getRunningJobs().size());

      // Invoke the md method. This should only remove 1 of the histories,
      // because the other one is currently running.
      JobHistory.clearHistory();

      Assert.assertEquals(1, new JobHistoryQuery(new QueryFactory()).getCount());

      Thread.sleep(2000);

      JobHistory.clearHistory();

      Assert.assertEquals(0, new JobHistoryQuery(new QueryFactory()).getCount());
    }
    catch (InterruptedException e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      ExecutableJob.get(job1.getOid()).delete();
      ExecutableJob.get(job2.getOid()).delete();
      clearHistory();
    }
  }

  @Request
  @Test
  public void testJobResume()
  {
    
  }
}
