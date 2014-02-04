/**
*
*/
package com.runwaysdk.system.scheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

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

  private static final boolean HALT = false;
  
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

    public TestJob()
    {
    }

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

  /*
   * Basic job that errors when executed.
   */
  private static class TestErrorJob implements ExecutableJobIF
  {

    public TestErrorJob()
    {
    }

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
          fail("The record [" + tr.getId() + "] took longer than [" + maxWaits
              + "] retries to complete.");
        }

        // Let's wait a while and try again.
        Thread.sleep(HALT ? Long.MAX_VALUE : 10);
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

  /**
   * Tests the execution of a job once.
   */
  public void testJobCompleted()
  {
    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);

    try
    {
      job.apply();

      TestRecord tr = TestRecord.newRecord(job);
      job.start();

      wait(tr);

      if (tr.isExecuted() && tr.getCount() == 1)
      {
        ExecutableJob updated = ExecutableJob.get(job.getId());
//        assertTrue(updated.getCompleted());
      }
      else
      {
        fail("The job was not completed.");
      }
    }

    finally
    {
      if (!job.isNew())
      {
        job.delete();
      }
    }
  }
  
//  /**
//   * Tests that a Jobs start, end, and duration times are set correctly.
//   */
//  public void testExecutionTiming()
//  {
//    fail("not implemented");
//    
//    // start, stop, duration, lastRun
//  }
//  
//  /**
//   * Tests the start listener.
//   */
//  public void testStartAndStopListener()
//  {
//    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
//    final MutableBoolean onStartFired = new MutableBoolean(false);
//    final MutableBoolean onStopFired = new MutableBoolean(false);
//
//    job.addJobListener(new JobListener()
//    {
//
//      public String getName()
//      {
//        return "testStartAndStopListener";
//      }
//
//      @Override
//      public void onStart(ExecutionContext context)
//      {
//        onStartFired.setValue(true);
//      }
//
//      public void onCancel(ExecutionContext context)
//      {
//      }
//
//      public void onStop(ExecutionContext context)
//      {
//        onStopFired.setValue(true);
//      }
//    });
//
//    try
//    {
//      job.apply();
//
//      TestRecord tr = TestRecord.newRecord(job);
//      job.start();
//
//      wait(tr);
//
//      if (!onStartFired.booleanValue())
//      {
//        fail("The start listener did not fire.");
//      }
//
//      if (!onStopFired.booleanValue())
//      {
//        fail("The stop listener did not fire.");
//      }
//    }
//
//    finally
//    {
//      if (!job.isNew())
//      {
//        job.delete();
//      }
//    }
//  }
//
//  /**
//   * Ensures that multiple listeners can fire for one job.
//   */
//  public void testMultipleListeners()
//  {
//    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
//
//    // #1
//    final MutableBoolean onStartFired1 = new MutableBoolean(false);
//    final MutableBoolean onStopFired1 = new MutableBoolean(false);
//    job.addJobListener(new JobListener()
//    {
//
//      public String getName()
//      {
//        return "testMultipleListeners1";
//      }
//
//      @Override
//      public void onStart(ExecutionContext context)
//      {
//        onStartFired1.setValue(true);
//      }
//
//      public void onCancel(ExecutionContext context)
//      {
//      }
//
//      public void onStop(ExecutionContext context)
//      {
//        onStopFired1.setValue(true);
//      }
//    });
//
//    // #2
//    final MutableBoolean onStartFired2 = new MutableBoolean(false);
//    final MutableBoolean onStopFired2 = new MutableBoolean(false);
//    job.addJobListener(new JobListener()
//    {
//
//      public String getName()
//      {
//        return "testMultipleListeners2";
//      }
//
//      @Override
//      public void onStart(ExecutionContext context)
//      {
//        onStartFired2.setValue(true);
//      }
//
//      public void onCancel(ExecutionContext context)
//      {
//      }
//
//      public void onStop(ExecutionContext context)
//      {
//        onStopFired2.setValue(true);
//      }
//    });
//
//    try
//    {
//      job.apply();
//
//      TestRecord tr = TestRecord.newRecord(job);
//      job.start();
//
//      wait(tr);
//
//      if (!onStartFired1.booleanValue())
//      {
//        fail("The start listener #1 did not fire.");
//      }
//
//      if (!onStopFired1.booleanValue())
//      {
//        fail("The stop listener #1 did not fire.");
//      }
//
//      if (!onStartFired1.booleanValue())
//      {
//        fail("The start listener #2 did not fire.");
//      }
//      
//      if (!onStopFired1.booleanValue())
//      {
//        fail("The stop listener #2 did not fire.");
//      }
//    }
//
//    finally
//    {
//      if (!job.isNew())
//      {
//        job.delete();
//      }
//    }
//  }
//
//  /**
//   * Tests that a ExecutableJob with removeOnComplete set to true is deleted
//   * from the database when completed.
//   */
//  public void testRemoveOnComplete()
//  {
//    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
//    
//    
//    try
//    {
//      job.setRemoveOnComplete(true);
//      job.apply();
//
//      TestRecord tr = TestRecord.newRecord(job);
//      job.start();
//
//      wait(tr);
//
//      if (tr.isExecuted() && tr.getCount() == 1)
//      {
//        // Make sure the ExecutableJob DB record was removed
//        try
//        {
//          ExecutableJob.get(job.getId());
//          
//          fail("The ExecutableJob was not deleted with removeOnComplete set to true.");
//        }
//        catch(DataNotFoundException e)
//        {
//          // expected
//        }
//      }
//      else
//      {
//        fail("The job was not completed.");
//      }
//      
//    }
//
//    finally
//    {
//      try
//      {
//        ExecutableJob.get(job.getId()).delete();
//      }
//      catch(DataNotFoundException e)
//      {
//        // expected if test was successful
//        // as there's nothing to delete.
//      }
//    }
//  }
//  
//  public void testCancelListener()
//  {
//    ExecutableJobIF eJob = new ExecutableJobIF()
//    {
//
//      @Override
//      public void execute(ExecutionContext executionContext)
//      {
//        // TODO Auto-generated method stub
//
//      }
//    };
//
//    ExecutableJob job = QualifiedTypeJob.newInstance(eJob.getClass());
//
//    try
//    {
//      job.apply();
//
//      TestRecord tr = TestRecord.newRecord(job);
//      job.start();
//
//      wait(tr);
//
//      if (!tr.isExecuted() || tr.getCount() == 0)
//      {
//        fail("The job was not completed.");
//      }
//
//    }
//
//    finally
//    {
//      if (!job.isNew())
//      {
//        job.delete();
//      }
//    }
//  }
//
//  /**
//   * Tests the start, stop, and duration time of a job
//   */
//  public void testJobTiming()
//  {
//    ExecutableJob job = QualifiedTypeJob.newInstance(TestJob.class);
//
//    try
//    {
//      job.apply();
//
//      TestRecord tr = TestRecord.newRecord(job);
//      job.start();
//
//      wait(tr);
//
//      if (!tr.isExecuted() || tr.getCount() == 0)
//      {
//        fail("The job was not completed.");
//      }
//
//    }
//
//    finally
//    {
//      if (!job.isNew())
//      {
//        job.delete();
//      }
//    }
//  }
//
//  /**
//   * Tests that many jobs can hit the system and will be handled without error.
//   */
//  public void testFloodJobs()
//  {
//    fail("not implemented");
//  }
  
  // /**
  // * Tests a job that errors.
  // */
  // public void testJobError()
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
