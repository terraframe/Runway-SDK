package com.runwaysdk.system.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import com.runwaysdk.util.IDGenerator;

public abstract class Job extends JobBase implements org.quartz.Job, JobIF, ExecutableJob
{
  private static final long serialVersionUID = 1082140153;
  
  public Job()
  {
    super();
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
   * Executes the Job within the context of Quartz.
   */
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException
  {
    String key = context.getJobDetail().getKey().getName();
    Job job = Job.getByKey(key);
    
    job.execute();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.system.scheduler.ExecutableJob#execute()
   */
  @Override
  public void execute()
  {
    // do nothing by default
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.system.scheduler.JobIF#getLocalizedDisplayLabel()
   */
  @Override
  public String getLocalizedDisplayLabel()
  {
    return this.getDisplayLabel().getValue();
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
    JobDisplayLabel label = this.getDisplayLabel();
    String value = label.getDefaultValue();
    if(value == null || value.trim().length() == 0)
    {
      label.setDefaultValue(this.getJobId());
    }
    
    
    // TODO Auto-generated method stub
    super.apply();
  }
  
}
