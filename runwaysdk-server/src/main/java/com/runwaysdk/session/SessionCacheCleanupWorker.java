package com.runwaysdk.session;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class SessionCacheCleanupWorker implements Runnable
{
  /**
   * The default amount of time to sleep between expire session checks
   */
  public static final int DEFAULT_PERIOD = 8000;
  
  private static Integer period = DEFAULT_PERIOD;
  
  private static Thread workerThread;
  
  private static Boolean runCleanupThread = true;
  
  public static void startWorkerThread()
  {
    if (workerThread != null)
    {
      return;
    }
    
    workerThread = new Thread(new SessionCacheCleanupWorker(), "Runway SessionCache Cleanup Worker");
    workerThread.setDaemon(true);
    workerThread.start();
    
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        runCleanupThread = false;
      }
    }, "Runway SessionCache shutdown thread"));
  }
  
  public static void setPeriod(Integer period)
  {
    SessionCacheCleanupWorker.period = period;
  }
  
  public void run()
  {
    while (runCleanupThread)
    {
      try
      {
        Thread.sleep(period);
      }
      catch (Exception e)
      {
        String errMsg = e.getMessage();
        throw new ProgrammingErrorException(errMsg);
      }
      
      doInRequest();
    }
  }
  
  @Request
  private void doInRequest()
  {
    SessionFacade.cleanUp();
  }
}