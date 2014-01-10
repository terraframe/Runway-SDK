package com.runwaysdk.system.scheduler;


public class CustomJob extends CustomJobBase
{
  private static final long serialVersionUID = -1448439780;

  private ExecutableJob executableJob;
  
  public CustomJob()
  {
    super();
    
    this.executableJob = null;
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.system.scheduler.Job#execute()
   */
  @Override
  public void execute()
  {
    String className = this.getClassName();
    
    try
    {
      // TODO pass in *this* CustomJob or use a Proxy to treat the object as a JobIF
      Class<? extends ExecutableJob> clazz = Class.forName(className).asSubclass(ExecutableJob.class);
      this.executableJob = clazz.newInstance();
      
      this.executableJob.execute();
    }
    catch (Throwable e)
    {
      throw new SchedulerConfigurationException("The CustomJob with class ["+className+"] could not be initialized.", e);
    }
  }
  
  /* (non-Javadoc)
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
  public static CustomJob newInstance(Class<? extends ExecutableJob> ejClass)
  {
    CustomJob cJob = new CustomJob();

    cJob.setClassName(ejClass.getName());
    
    return cJob;
  }
}
