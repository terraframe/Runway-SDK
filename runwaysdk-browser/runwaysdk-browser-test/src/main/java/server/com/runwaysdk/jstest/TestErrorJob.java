package com.runwaysdk.jstest;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.system.scheduler.ExecutableJobIF;
import com.runwaysdk.system.scheduler.ExecutionContext;

/*
 * Basic job that errors when executed.
 */
public class TestErrorJob implements ExecutableJobIF
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
