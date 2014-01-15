package com.runwaysdk.system.scheduler;

public class QualifiedTypeJob extends QualifiedTypeJobBase
{
  private static final long serialVersionUID = 1991343666;
  
  private ExecutableJobIF executableJobIF;
  
  public QualifiedTypeJob()
  {
    super();
  }
  
  @Override
  public void execute(ExecutionContext executionContext)
  {
    String className = this.getClassName();
    
    try
    {
      // TODO pass in *this* CustomJob or use a Proxy to treat the object as a
      // JobIF
      Class<? extends ExecutableJobIF> clazz;
      try
      {
        clazz = Class.forName(className).asSubclass(ExecutableJobIF.class);
        this.executableJobIF = clazz.newInstance();
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

      this.executableJobIF.execute(executionContext);
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      throw new SchedulerConfigurationException("The CustomJob with class ["+className+"] could not be initialized.", e);
    }
  }
  
  public static QualifiedTypeJob newInstance(Class<? extends ExecutableJobIF> ej)
  {
    QualifiedTypeJob job = new QualifiedTypeJob();
    job.setClassName(ej.getName());
    return job;
  }
}
