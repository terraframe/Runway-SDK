package com.runwaysdk.system.scheduler;

public class CustomJob extends CustomJobBase
{
  private static final long serialVersionUID = -1448439780;

  private ExecutableJobIF     executableJob;

  public CustomJob()
  {
    super();

    this.executableJob = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.system.scheduler.Job#execute()
   */
  @Override
  public void execute(ExecutionContext executionContext)
  {
    String className = this.getClassName();

    // TODO pass in *this* CustomJob or use a Proxy to treat the object as a
    // JobIF
    Class<? extends ExecutableJobIF> clazz;
    try
    {
      clazz = Class.forName(className).asSubclass(ExecutableJobIF.class);
      this.executableJob = clazz.newInstance();
    }
    catch (ClassNotFoundException e)
    {
      throw new SchedulerConfigurationException("Could not find Job class ["+className+"]", e);
    }
    catch (IllegalAccessException e)
    {
      throw new SchedulerConfigurationException("Could not access Job class ["+className+"]", e);
    }
    catch (InstantiationException e)
    {
      throw new SchedulerConfigurationException("Could not instantiate Job class ["+className+"]", e);
    }

    this.executableJob.execute(executionContext);
//    this.executeJob(this.executableJob, executionContext);
  }
  

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.system.scheduler.Job#start()
   */
  @Override
  public synchronized void start()
  {
    super.start();
  }

  /**
   * Creates a new instance of Job based on the ExecutableJob.
   * 
   * @param job
   * @return
   */
  public static CustomJob newInstance(Class<? extends ExecutableJobIF> ejClass)
  {
    CustomJob cJob = new CustomJob();

    cJob.setClassName(ejClass.getName());

    return cJob;
  }
}
