package com.runwaysdk.system.scheduler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.runwaysdk.util.IDGenerator;

public abstract class ExecutableJob extends ExecutableJobBase implements org.quartz.Job, JobIF, ExecutableJobIF
{
  private static final long serialVersionUID = 328266996;
  
  private Map<String, JobListener> listeners;
  
  public ExecutableJob()
  {
    super();
    
    this.listeners = new LinkedHashMap<String, JobListener>();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.business.Entity#buildKey()
   */
  @Override
  protected String buildKey()
  {
    return this.getJobId();
  }
  
  /**
   * 
   * @param jobListener
   */
  public void addJobListener(JobListener jobListener)
  {
    this.listeners.put(jobListener.getName(), jobListener);
  }
  
  /**
   * 
   * @return
   */
  public Map<String, JobListener> getJobListeners()
  {
    return this.listeners;
  }

  /**
   * Executes the Job within the context of Quartz.
   */
  @Override
  public final void execute(JobExecutionContext context) throws JobExecutionException
  {
    // the job's key is equal to the Runway id. Fetch the job
    // from the cache and pass it into the execution context
    String id = context.getJobDetail().getKey().getName();
    ExecutableJob job = ExecutableJob.get(id);
    
    ExecutionContext executionContext = ExecutionContext.factory(ExecutionContext.Context.EXECUTION, job);
    
    executeJob(job, job, executionContext);
  }
  
  /**
   * The final stage in executing the job, which invokes the execute() method directly. This
   * cannot be overridden because it follows a special error handling procedure.
   * 
   * @param ej
   * @param executionContext
   */
  static final void executeJob(ExecutableJob job, ExecutableJobIF ej, ExecutionContext executionContext)
  {
    try
    {
//      job.appLock();
//      job.setStartTime(new Date());
//      job.apply();
      
      job.execute(executionContext);
      
      // Job completed
      // FIXME handle asynchronous jobs?
//      job.appLock();
//      job.setCompleted(true);
//      job.setEndTime(new Date());
//      job.setLastRun(job.getEndTime());
//      job.apply();
    }
    catch(Throwable t)
    {
      throw new RuntimeException(t);
    }
  }
  
  /**
   * Returns the duration of the last execution (end time - start time).
   * 
   * @return
   */
  public Long getDuration()
  {
    if(this.getCompleted())
    {
      return this.getEndTime().getTime() - this.getStartTime().getTime();
    }
    else
    {
      return null;
    }
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.system.scheduler.ExecutableJob#execute()
   */
  @Override
  public void execute(ExecutionContext executionContext)
  {
    // do nothing by default
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.system.scheduler.JobIF#getLocalizedDisplayLabel()
   */
  @Override
  public String getLocalizedDescription()
  {
    return this.getDescription().getValue();
  }

  /**
   * Ensures the state of the Job is valid for the given operation.
   */
  private void validateOperation(AllJobOperation operation)
  {
    if(!this.isAppliedToDB())
    {
      String msg = "Cannot call operation ["+operation+"] on job ["+this+"] because it is not persisted.";
      throw new JobNotPersistedException(msg, this, operation);
    }
  }
  
  public synchronized void start()
  {
    validateOperation(AllJobOperation.START);
    
    for(JobListener jobListener : this.listeners.values())
    {
      SchedulerManager.addJobListener(this, jobListener);
    }
    
    SchedulerManager.schedule(this);
  }

  public void stop()
  {
    validateOperation(AllJobOperation.STOP);
    
  }

  public void pause()
  {
    validateOperation(AllJobOperation.PAUSE);
    
  }
  
  public void resume()
  {
    validateOperation(AllJobOperation.RESUME);
    
  }
  
  public void cancel()
  {
    validateOperation(AllJobOperation.CANCEL);
    
  }

  /**
   * 
   */
  @Override
  public void apply()
  {
    
    // If a job id is not set generate a unique id in its place.
    String jobId = this.getJobId();
    if(jobId == null || jobId.trim().length() == 0)
    {
      String id = IDGenerator.nextID();
      this.setJobId(id);
    }
    
    // Set the display label to the job id if one is not set already
    ExecutableJobDescription desc = this.getDescription();
    String value = desc.getDefaultValue();
    if(value == null || value.trim().length() == 0)
    {
      desc.setDefaultValue(this.getJobId());
    }
    
    
    // TODO Auto-generated method stub
    super.apply();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.system.scheduler.JobBase#toString()
   */
  @Override
  public String toString()
  {
    String clazz = this.getClassDisplayLabel();
    String id = this.getJobId();
    String desc = this.getDescription().getValue();
    
    if(id != null && desc != null && id == desc)
    {
      return "["+clazz+"] - "+desc;
    }
    else if(id != null && desc != null)
    {
      return "["+clazz+"] - "+desc+" ("+id+")";
    }
    else
    {
      return "["+clazz+"] - "+this.getId();
    }
  }
  
}
