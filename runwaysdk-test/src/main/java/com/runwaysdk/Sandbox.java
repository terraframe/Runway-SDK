/**
 * \ * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk;


import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.JobOperationInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.Versioning;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdAttributeBoolean;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeDateTime;
import com.runwaysdk.system.metadata.MdAttributeEnumeration;
import com.runwaysdk.system.metadata.MdAttributeIndices;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeLocalCharacter;
import com.runwaysdk.system.metadata.MdAttributeLocalText;
import com.runwaysdk.system.metadata.MdAttributeLong;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdRelationship;

public class Sandbox implements Job
{
  @Request
  public static void main(String[] args) throws Exception
  {
    // display new properties
//    System.getProperties().list(System.out);

//     createSchedulerMetadata();
    // scheduler();

    // createType();
    // changeType();
    // updateStrategyType();
    // createMdAttributeTerm();
    // createMdAttributeMultiReference();
     
     importWithDiff();
  }

  
  public static void importWithDiff() {
    Database.enableLoggingDMLAndDDLstatements(true);
    
    Versioning.main(new String[]{"/users/terraframe/documents/workspace/Runway-SDK/runwaysdk-test/src/main/domain"});
  }
  
  private static int count = 0;

  /*
   * (non-Javadoc)
   * 
   * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
   */
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException
  {
    System.out.println("JOBS: " + count++);
  }
/*
  private static void scheduler()
  {
    try
    {
      // Grab the Scheduler instance from the Factory
      SchedulerManager.start();

      // specify the job' s details..
      JobDetail job = JobBuilder.newJob(Sandbox.class).withIdentity("testJob").build();

      // specify the running period of the job
      Trigger trigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever()).build();

      // SchedulerManager.schedule(job, trigger);

      Thread.currentThread().sleep(10000);

    }
    // catch (SchedulerException e)
    // {
    // e.printStackTrace();
    // }
    catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally
    {
      SchedulerManager.shutdown();
    }

  }
*/
  private static void createJobOperation(String name, String display)
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(JobOperationInfo.CLASS);
    businessDAO.setValue(EnumerationMasterInfo.NAME, name);
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, display);
    businessDAO.apply();
  }

  @SuppressAjWarnings({"adviceDidNotMatch"})
  @Transaction
  private static void createSchedulerMetadata()
  {
    try
    {
      Database.enableLoggingDMLAndDDLstatements(true);

      MdBusiness enumMasterMdBusinessIF = MdBusiness.getMdBusiness(EnumerationMasterInfo.CLASS);

      // Job Operation
      MdBusinessDAO jobOperation = MdBusinessDAO.newInstance();
      jobOperation.setValue(MdBusinessInfo.NAME, "JobOperation");
      jobOperation.setValue(MdBusinessInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      jobOperation.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "Job Operation");
      jobOperation.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Job Operation");
      jobOperation.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getId());
      jobOperation.setValue(MdBusinessInfo.EXTENDABLE, "false");
      String jobOperationMdId = jobOperation.apply();

      createJobOperation("PAUSE", "Pause");
      createJobOperation("RESUME", "Resume");
      createJobOperation("START", "Start");
      createJobOperation("STOP", "Stop");
      createJobOperation("CANCEL", "Cancel");

      MdEnumerationDAO allJobOperation = MdEnumerationDAO.newInstance();
      allJobOperation.setValue(MdEnumerationInfo.NAME, "AllJobOperation");
      allJobOperation.setValue(MdEnumerationInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      allJobOperation.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Job Operations");
      allJobOperation.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Job Operations");
      allJobOperation.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      allJobOperation.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
      allJobOperation.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, jobOperationMdId);
      allJobOperation.apply();

      // AbstractJob
      MdBusinessDAO abstractJob = MdBusinessDAO.newInstance();
      abstractJob.setValue(MdBusinessInfo.NAME, "AbstractJob");
      abstractJob.setValue(MdBusinessInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      abstractJob.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "AbstractJob");
      abstractJob.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "AbstractJob");
      abstractJob.setValue(MdBusinessInfo.ABSTRACT, "true");
      abstractJob.setValue(MdBusinessInfo.EXTENDABLE, "true");
      abstractJob.apply();

      MdBusiness abstractJobMd = MdBusiness.get(abstractJob.getId());

      // lastRun::dt
      MdAttributeDateTime lastRun = new MdAttributeDateTime();
      lastRun.setAttributeName("lastRun");
      lastRun.getDisplayLabel().setDefaultValue("Last Run");
      lastRun.getDescription().setDefaultValue("Last Run");
      lastRun.setRequired(false);
      lastRun.setDefiningMdClass(abstractJobMd);
      lastRun.apply();

      // repeat::b
      MdAttributeBoolean repeat = new MdAttributeBoolean();
      repeat.setAttributeName("repeated");
      repeat.getDisplayLabel().setDefaultValue("Repeated");
      repeat.getDescription().setDefaultValue("Repeated");
      repeat.setRequired(true);
      repeat.setDefaultValue(false);
      repeat.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      repeat.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      repeat.setDefiningMdClass(abstractJobMd);
      repeat.apply();

      // pauseable::b
      MdAttributeBoolean pauseable = new MdAttributeBoolean();
      pauseable.setAttributeName("pauseable");
      pauseable.getDisplayLabel().setDefaultValue("Pauseable");
      pauseable.getDescription().setDefaultValue("Pauseable");
      pauseable.setRequired(true);
      pauseable.setDefaultValue(false);
      pauseable.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      pauseable.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      pauseable.setDefiningMdClass(abstractJobMd);
      pauseable.apply();

      // paused::b
      MdAttributeBoolean paused = new MdAttributeBoolean();
      paused.setAttributeName("paused");
      paused.getDisplayLabel().setDefaultValue("Paused");
      paused.getDescription().setDefaultValue("Paused");
      paused.setRequired(true);
      paused.setDefaultValue(false);
      paused.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      paused.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      paused.setDefiningMdClass(abstractJobMd);
      paused.apply();

      // workTotal::i
      MdAttributeInteger workTotal = new MdAttributeInteger();
      workTotal.setAttributeName("workTotal");
      workTotal.getDisplayLabel().setDefaultValue("Work Total");
      workTotal.getDescription().setDefaultValue("Work Total");
      workTotal.setRequired(false);
      workTotal.setRejectNegative(true);
      workTotal.setDefiningMdClass(abstractJobMd);
      workTotal.apply();

      // workProgress::i
      MdAttributeInteger workProgress = new MdAttributeInteger();
      workProgress.setAttributeName("workProgress");
      workProgress.getDisplayLabel().setDefaultValue("Work Progress");
      workProgress.getDescription().setDefaultValue("Work Progress");
      workProgress.setRequired(false);
      workProgress.setRejectNegative(true);
      workProgress.setDefiningMdClass(abstractJobMd);
      workProgress.apply();

      // completed::b
      MdAttributeBoolean completed = new MdAttributeBoolean();
      completed.setAttributeName("completed");
      completed.getDisplayLabel().setDefaultValue("Completed");
      completed.getDescription().setDefaultValue("Completed");
      completed.setRequired(true);
      completed.setDefaultValue(false);
      completed.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      completed.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      completed.setDefiningMdClass(abstractJobMd);
      completed.apply();

      // removeOnComplete::b
      MdAttributeBoolean removeOnComplete = new MdAttributeBoolean();
      removeOnComplete.setAttributeName("removeOnComplete");
      removeOnComplete.getDisplayLabel().setDefaultValue("Remove On Complete");
      removeOnComplete.getDescription().setDefaultValue("Remove On Complete");
      removeOnComplete.setRequired(true);
      removeOnComplete.setDefaultValue(false);
      removeOnComplete.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      removeOnComplete.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      removeOnComplete.setDefiningMdClass(abstractJobMd);
      removeOnComplete.apply();

      // startOnCreate::b
      MdAttributeBoolean startOnCreate = new MdAttributeBoolean();
      startOnCreate.setAttributeName("startOnCreate");
      startOnCreate.getDisplayLabel().setDefaultValue("Start On Create");
      startOnCreate.getDescription().setDefaultValue("Start On Create");
      startOnCreate.setRequired(true);
      startOnCreate.setDefaultValue(false);
      startOnCreate.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      startOnCreate.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      startOnCreate.setDefiningMdClass(abstractJobMd);
      startOnCreate.apply();

      // cancelable::b
      MdAttributeBoolean cancelable = new MdAttributeBoolean();
      cancelable.setAttributeName("cancelable");
      cancelable.getDisplayLabel().setDefaultValue("Cancelable");
      cancelable.getDescription().setDefaultValue("Cancelable");
      cancelable.setRequired(true);
      cancelable.setDefaultValue(false);
      cancelable.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      cancelable.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      cancelable.setDefiningMdClass(abstractJobMd);
      cancelable.apply();

      // canceled::b
      MdAttributeBoolean canceled = new MdAttributeBoolean();
      canceled.setAttributeName("canceled");
      canceled.getDisplayLabel().setDefaultValue("Canceled");
      canceled.getDescription().setDefaultValue("Canceled");
      canceled.setRequired(true);
      canceled.setDefaultValue(false);
      canceled.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      canceled.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      canceled.setDefiningMdClass(abstractJobMd);
      canceled.apply();

      // running::b
      MdAttributeBoolean running = new MdAttributeBoolean();
      running.setAttributeName("running");
      running.getDisplayLabel().setDefaultValue("Running");
      running.getDescription().setDefaultValue("Running");
      running.setRequired(true);
      running.setDefaultValue(false);
      running.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      running.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      running.setDefiningMdClass(abstractJobMd);
      running.apply();

      // maxRetries::i
      MdAttributeInteger maxRetries = new MdAttributeInteger();
      maxRetries.setAttributeName("maxRetries");
      maxRetries.getDisplayLabel().setDefaultValue("Max Retries");
      maxRetries.getDescription().setDefaultValue("Max Retries");
      maxRetries.setRequired(false);
      maxRetries.setRejectNegative(true);
      maxRetries.setDefiningMdClass(abstractJobMd);
      maxRetries.apply();

      // retries::i
      MdAttributeInteger retries = new MdAttributeInteger();
      retries.setAttributeName("retries");
      retries.getDisplayLabel().setDefaultValue("Retries");
      retries.getDescription().setDefaultValue("Retries");
      retries.setRequired(false);
      retries.setRejectNegative(true);
      retries.setDefiningMdClass(abstractJobMd);
      retries.apply();

      // timeout::l
      MdAttributeLong timeout = new MdAttributeLong();
      timeout.setAttributeName("timeout");
      timeout.getDisplayLabel().setDefaultValue("Timeout");
      timeout.getDescription().setDefaultValue("Timeout");
      timeout.setRequired(false);
      timeout.setRejectNegative(true);
      timeout.setDefiningMdClass(abstractJobMd);
      timeout.apply();

      // cron::s
      MdAttributeCharacter cron = new MdAttributeCharacter();
      cron.setAttributeName("cronExpression");
      cron.getDisplayLabel().setDefaultValue("Cron Expression");
      cron.getDescription().setDefaultValue("Cron Expression");
      cron.setRequired(false);
      cron.setDatabaseSize(60);
      cron.setDefiningMdClass(abstractJobMd);
      cron.apply();

      // startTime::dt
      MdAttributeDateTime startTime = new MdAttributeDateTime();
      startTime.setAttributeName("startTime");
      startTime.getDisplayLabel().setDefaultValue("Start Time");
      startTime.getDescription().setDefaultValue("Start Time");
      startTime.setRequired(false);
      startTime.setDefiningMdClass(abstractJobMd);
      startTime.apply();

      // endTime::dt
      MdAttributeDateTime endTime = new MdAttributeDateTime();
      endTime.setAttributeName("endTime");
      endTime.getDisplayLabel().setDefaultValue("End Time");
      endTime.getDescription().setDefaultValue("End Time");
      endTime.setRequired(false);
      endTime.setDefiningMdClass(abstractJobMd);
      endTime.apply();

      // jobOperation::JobOperation
      MdAttributeEnumeration currentJobOperation = new MdAttributeEnumeration();
      currentJobOperation.setValue(MdAttributeEnumerationInfo.NAME, "jobOperation");
      currentJobOperation.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Job Operation");
      currentJobOperation.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The current Job Operation called on the Job.");
      currentJobOperation.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      currentJobOperation.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      currentJobOperation.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, abstractJobMd.getId());
      currentJobOperation.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, allJobOperation.getId());
      currentJobOperation.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
      currentJobOperation.apply();

      // Job
      MdBusinessDAO executableJob = MdBusinessDAO.newInstance();
      executableJob.setValue(MdBusinessInfo.NAME, "ExecutableJob");
      executableJob.setValue(MdBusinessInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      executableJob.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "Executable Job");
      executableJob.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Executable Job");
      executableJob.setValue(MdBusinessInfo.ABSTRACT, "true");
      executableJob.setValue(MdBusinessInfo.EXTENDABLE, "true");
      /*
       * NOTE: Cache everything to avoid frequent DB fetches as events are fired
       * and need to reference the job.
       */
      executableJob.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
      executableJob.setValue(MdBusiness.SUPERMDBUSINESS, abstractJob.getId());
      String executableJobMdId = executableJob.apply();

      MdBusiness jobMd = MdBusiness.get(executableJobMdId);

      /*
       * Define MdMethods for Job
       */
      MdMethodDAO startMethod = MdMethodDAO.newInstance();
      startMethod.setValue(MdMethodInfo.REF_MD_TYPE, executableJobMdId);
      startMethod.setValue(MdMethodInfo.NAME, "start");
      startMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
      startMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Job");
      startMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Job");
      startMethod.apply();

      MdMethodDAO stopMethod = MdMethodDAO.newInstance();
      stopMethod.setValue(MdMethodInfo.REF_MD_TYPE, executableJobMdId);
      stopMethod.setValue(MdMethodInfo.NAME, "stop");
      stopMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
      stopMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Stop Job");
      stopMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Stop Job");
      stopMethod.apply();

      MdMethodDAO cancelMethod = MdMethodDAO.newInstance();
      cancelMethod.setValue(MdMethodInfo.REF_MD_TYPE, executableJobMdId);
      cancelMethod.setValue(MdMethodInfo.NAME, "cancel");
      cancelMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
      cancelMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Cancel Job");
      cancelMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Cancel Job");
      cancelMethod.apply();

      MdMethodDAO resumeMethod = MdMethodDAO.newInstance();
      resumeMethod.setValue(MdMethodInfo.REF_MD_TYPE, executableJobMdId);
      resumeMethod.setValue(MdMethodInfo.NAME, "resume");
      resumeMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
      resumeMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Resume Job");
      resumeMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Resume Job");
      resumeMethod.apply();

      MdMethodDAO pauseMethod = MdMethodDAO.newInstance();
      pauseMethod.setValue(MdMethodInfo.REF_MD_TYPE, executableJobMdId);
      pauseMethod.setValue(MdMethodInfo.NAME, "pause");
      pauseMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
      pauseMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Pause Job");
      pauseMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Pause Job");
      pauseMethod.apply();

      // description::lc
      MdAttributeLocalCharacter description = new MdAttributeLocalCharacter();
      description.setAttributeName("description");
      description.getDisplayLabel().setDefaultValue("Description");
      description.getDescription().setDefaultValue("Description");
      description.setRequired(true);
      description.setDefiningMdClass(jobMd);
      description.apply();
      
      // jobId::c
      MdAttributeCharacter jobId = new MdAttributeCharacter();
      jobId.setAttributeName("jobId");
      jobId.getDisplayLabel().setDefaultValue("Job Id");
      jobId.getDescription().setDefaultValue("Job Id");
      jobId.setRequired(true);
      jobId.setDatabaseSize(64);
      jobId.setValue(MdAttributeCharacter.INDEXTYPE, MdAttributeIndices.UNIQUE_INDEX.getId());
      jobId.setDefiningMdClass(jobMd);
      jobId.apply();

      // Custom Job
      MdBusinessDAO qualifiedTypeJob = MdBusinessDAO.newInstance();
      qualifiedTypeJob.setValue(MdBusinessInfo.NAME, "QualifiedTypeJob");
      qualifiedTypeJob.setValue(MdBusinessInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      qualifiedTypeJob.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "Qualified Type Job");
      qualifiedTypeJob.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Qualified Type Job");
      qualifiedTypeJob.setValue(MdBusinessInfo.ABSTRACT, "false");
      qualifiedTypeJob.setValue(MdBusinessInfo.EXTENDABLE, "true");
      qualifiedTypeJob.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, executableJobMdId);
      qualifiedTypeJob.apply();

      MdBusiness qualifiedTypeJobMd = MdBusiness.get(qualifiedTypeJob.getId());

      // className::s
      MdAttributeCharacter className = new MdAttributeCharacter();
      className.setAttributeName("className");
      className.getDisplayLabel().setDefaultValue("Class Name");
      className.getDescription().setDefaultValue("Class Name");
      className.setRequired(true);
      className.setDatabaseSize(100);
      className.setDefiningMdClass(qualifiedTypeJobMd);
      className.apply();

      // storeHistory::b
      MdAttributeBoolean recordHistory = new MdAttributeBoolean();
      recordHistory.setAttributeName("recordHistory");
      recordHistory.getDisplayLabel().setDefaultValue("Record History");
      recordHistory.getDescription().setDefaultValue("Record History");
      recordHistory.setRequired(true);
      recordHistory.setDefaultValue(true);
      recordHistory.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      recordHistory.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      recordHistory.setDefiningMdClass(jobMd);
      recordHistory.apply();

      // AbstractJob
      MdBusinessDAO snapshot = MdBusinessDAO.newInstance();
      snapshot.setValue(MdBusinessInfo.NAME, "JobSnapshot");
      snapshot.setValue(MdBusinessInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      snapshot.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "JobSnapshot");
      snapshot.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "JobSnapshot");
      snapshot.setValue(MdBusinessInfo.ABSTRACT, "false");
      snapshot.setValue(MdBusinessInfo.EXTENDABLE, "true");
      snapshot.setValue(MdBusiness.SUPERMDBUSINESS, abstractJob.getId());
      snapshot.apply();

      MdBusiness snapshotMd = MdBusiness.get(snapshot.getId());

      // JobHistory
      MdBusinessDAO jobHistory = MdBusinessDAO.newInstance();
      jobHistory.setValue(MdBusinessInfo.NAME, "JobHistory");
      jobHistory.setValue(MdBusinessInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      jobHistory.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "JobHistory");
      jobHistory.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "JobHistory");
      jobHistory.setValue(MdBusinessInfo.ABSTRACT, "false");
      jobHistory.setValue(MdBusinessInfo.EXTENDABLE, "true");
      jobHistory.apply();

      MdBusiness jobHistoryMd = MdBusiness.get(jobHistory.getId());

      // entryDate::datetime
      MdAttributeDateTime entryDate = new MdAttributeDateTime();
      entryDate.setAttributeName("entryDate");
      entryDate.getDisplayLabel().setDefaultValue("Entry Date");
      entryDate.getDescription().setDefaultValue("Entry Date");
      entryDate.setRequired(false);
      entryDate.setDefiningMdClass(jobMd);
      entryDate.apply();

      // historyComment::local text
      MdAttributeLocalText comment = new MdAttributeLocalText();
      comment.setAttributeName("historyComment");
      comment.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Comment");
      comment.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "User Friendly History Comment");
      comment.setRequired(false);
      comment.setDefiningMdClass(jobHistoryMd);
      comment.apply();

      // historyInformation::text
      MdAttributeLocalText historyInformation = new MdAttributeLocalText();
      historyInformation.setAttributeName("historyInformation");
      historyInformation.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "History Information");
      historyInformation.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "History Information with Data and Results");
      historyInformation.setRequired(false);
      historyInformation.setDefiningMdClass(jobHistoryMd);
      historyInformation.apply();

      MdAttributeReference jobSnapshot = new MdAttributeReference();
      jobSnapshot.setAttributeName("jobSnapshot");
      jobSnapshot.getDisplayLabel().setDefaultValue("Job Snapshot");
      jobSnapshot.getDescription().setDefaultValue("Job Snapshot");
      jobSnapshot.setRequired(false);
      jobSnapshot.setValue(MdAttributeReference.MDBUSINESS, snapshot.getId());
      jobSnapshot.setDefiningMdClass(jobHistoryMd);
      jobSnapshot.apply();

      // Rel between Job <-> JobHistory
      MdRelationship jobHistoryRel = new MdRelationship();
      jobHistoryRel.setTypeName("JobHistoryRecord");
      jobHistoryRel.setPackageName(Constants.SCHEDULER_PACKAGE);
      jobHistoryRel.getDisplayLabel().setDefaultValue("Job History Records");
      jobHistoryRel.getDescription().setDefaultValue("Job History Records");
      jobHistoryRel.setParentMdBusiness(jobMd);
      jobHistoryRel.setParentCardinality("1");
      jobHistoryRel.setParentMethod("Job");
      jobHistoryRel.getParentDisplayLabel().setDefaultValue(jobMd.getDisplayLabel().getDefaultValue());
      jobHistoryRel.setChildMdBusiness(jobHistoryMd);
      jobHistoryRel.setChildCardinality("*");
      jobHistoryRel.setChildMethod("JobHistory");
      jobHistoryRel.getChildDisplayLabel().setDefaultValue(jobHistoryMd.getDisplayLabel().getDefaultValue());
      jobHistoryRel.setComposition(true);
      jobHistoryRel.apply();
    }
    finally
    {
      Database.enableLoggingDMLAndDDLstatements(false);
    }
  }

  @SuppressWarnings("unused")
  @Request
  private static void changeType()
  {
    changeTypeInTransaction();
  }

  @Transaction
  private static void changeTypeInTransaction()
  {
    MdBusinessDAO idMapping = MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.mobile.LocalIdMapping").getBusinessDAO();
    idMapping.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    idMapping.apply();

    MdBusinessDAO sessionIdTo = MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.mobile.SessionIdToMobileIdMapping").getBusinessDAO();
    sessionIdTo.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    sessionIdTo.apply();

    MdBusinessDAO linkedStack = MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.mobile.LinkedStackPersistance").getBusinessDAO();
    linkedStack.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    linkedStack.apply();
  }

  @SuppressWarnings("unused")
  @Request
  private static void createType()
  {
    createTypeInTransaction();
  }

  @Transaction
  private static void createTypeInTransaction()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    MdBusinessDAO strategyStateMaster = MdBusinessDAO.newInstance();
    strategyStateMaster.setValue(MdBusinessInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    strategyStateMaster.setValue(MdBusinessInfo.NAME, "StrategyStateMaster");
    strategyStateMaster.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Strategy State Master");
    strategyStateMaster.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "Strategy State Master");
    strategyStateMaster.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    strategyStateMaster.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    strategyStateMaster.setGenerateMdController(false);
    strategyStateMaster.apply();

    BusinessDAO uninitialized = BusinessDAO.newInstance(strategyStateMaster.definesType());
    uninitialized.setValue(EnumerationMasterInfo.NAME, "UNINITIALIZED");
    uninitialized.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, "defaultLocale", "Uninitialized");
    uninitialized.apply();

    BusinessDAO initialized = BusinessDAO.newInstance(strategyStateMaster.definesType());
    initialized.setValue(EnumerationMasterInfo.NAME, "INITIALIZED");
    initialized.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, "defaultLocale", "Initialized");
    initialized.apply();

    BusinessDAO initializing = BusinessDAO.newInstance(strategyStateMaster.definesType());
    initializing.setValue(EnumerationMasterInfo.NAME, "INITIALIZING");
    initializing.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, "defaultLocale", "Initializing");
    initializing.apply();

    MdEnumerationDAO strategyState = MdEnumerationDAO.newInstance();
    strategyState.setValue(MdEnumerationInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    strategyState.setValue(MdEnumerationInfo.NAME, "StrategyState");
    strategyState.setStructValue(MdEnumerationInfo.DESCRIPTION, "defaultLocale", "Strategy State");
    strategyState.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, "defaultLocale", "Strategy State");
    strategyState.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, strategyStateMaster.getId());
    strategyState.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    strategyState.apply();

    MdBusinessDAO ontologyStrategy = MdBusinessDAO.newInstance();
    ontologyStrategy.setValue(MdBusinessInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    ontologyStrategy.setValue(MdBusinessInfo.NAME, "OntologyStrategy");
    ontologyStrategy.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Ontology Strategy");
    ontologyStrategy.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "Ontology Strategy");
    ontologyStrategy.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    ontologyStrategy.setGenerateMdController(false);
    ontologyStrategy.apply();

    MdAttributeEnumerationDAO state = MdAttributeEnumerationDAO.newInstance();
    state.setValue(MdAttributeEnumerationInfo.NAME, "strategyState");
    state.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy State");
    state.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy State");
    state.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    state.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, ontologyStrategy.getId());
    state.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, strategyState.getId());
    state.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    state.apply();

    MdBusinessDAO databaseAllPathsStrategy = MdBusinessDAO.newInstance();
    databaseAllPathsStrategy.setValue(MdBusinessInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    databaseAllPathsStrategy.setValue(MdBusinessInfo.NAME, "DatabaseAllPathsStrategy");
    databaseAllPathsStrategy.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Database all paths strategy");
    databaseAllPathsStrategy.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "Database all paths strategy");
    databaseAllPathsStrategy.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    databaseAllPathsStrategy.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, ontologyStrategy.getId());
    databaseAllPathsStrategy.setGenerateMdController(false);
    databaseAllPathsStrategy.apply();

    MdBusinessDAO postgresAllPathsStrategy = MdBusinessDAO.newInstance();
    postgresAllPathsStrategy.setValue(MdBusinessInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    postgresAllPathsStrategy.setValue(MdBusinessInfo.NAME, "PostgresAllPathsStrategy");
    postgresAllPathsStrategy.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Postgres all paths strategy");
    postgresAllPathsStrategy.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "Postgres all paths strategy");
    postgresAllPathsStrategy.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    postgresAllPathsStrategy.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, databaseAllPathsStrategy.getId());
    postgresAllPathsStrategy.setGenerateMdController(false);
    postgresAllPathsStrategy.apply();

    MdBusinessDAOIF mdTerm = MdBusinessDAO.getMdBusinessDAO(MdTermInfo.CLASS);

    MdAttributeReferenceDAO strategy = MdAttributeReferenceDAO.newInstance();
    strategy.setValue(MdAttributeReferenceInfo.NAME, "strategy");
    strategy.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy");
    strategy.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy");
    strategy.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    strategy.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdTerm.getId());
    strategy.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, ontologyStrategy.getId());
    strategy.apply();
  }

//  @Request
//  private static void updateStrategyType()
//  {
//    updateStrategyTypeInTransaction();
//  }

  @Transaction
  private static void updateStrategyTypeInTransaction()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    MdBusinessDAO databaseAllPathsStrategy = MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy").getBusinessDAO();
    databaseAllPathsStrategy.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    databaseAllPathsStrategy.setGenerateMdController(false);
    databaseAllPathsStrategy.apply();

    MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.metadata.ontology.PostgresAllPathsStrategy").getBusinessDAO().delete();
  }

//  @Request
//  private static void createMdAttributeTerm()
//  {
//    createMdAttributeTermInTransaction();
//  }

  @SuppressAjWarnings({"adviceDidNotMatch"})
  @Transaction
  private static void createMdAttributeTermInTransaction()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    MdBusinessDAO mdAttributeReference = MdBusinessDAO.getMdBusinessDAO(MdAttributeReferenceInfo.CLASS).getBusinessDAO();
    mdAttributeReference.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.apply();

    MdBusinessDAO mdAttributeTerm = MdBusinessDAO.newInstance();
    mdAttributeTerm.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdAttributeTerm.setValue(MdBusinessInfo.NAME, "MdAttributeTerm");
    mdAttributeTerm.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Metadata definition for term attributes");
    mdAttributeTerm.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "MdAttributeTerm");
    mdAttributeTerm.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeTerm.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttributeReference.getId());
    mdAttributeTerm.setGenerateMdController(false);
    mdAttributeTerm.apply();
  }

  /**
   * 
   */
  @SuppressAjWarnings({"adviceDidNotMatch"})
  @Request
  private static void createMdAttributeMultiReference()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    Sandbox.createMdAttributeMultiReferenceInTransaction();
  }

  @Transaction
  private static void createMdAttributeMultiReferenceInTransaction()
  {
    MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(MdBusinessInfo.CLASS);
    MdBusinessDAOIF mdAttributeConcrete = MdBusinessDAO.getMdBusinessDAO(MdAttributeConcreteInfo.CLASS);

    MdBusinessDAO mdAttributeMultiReference = MdBusinessDAO.newInstance();
    mdAttributeMultiReference.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdAttributeMultiReference.setValue(MdBusinessInfo.NAME, "MdAttributeMultiReference");
    mdAttributeMultiReference.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Metadata definition for multi reference attributes");
    mdAttributeMultiReference.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "MdAttributeMultiReference");
    mdAttributeMultiReference.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeMultiReference.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttributeConcrete.getId());
    mdAttributeMultiReference.setGenerateMdController(false);
    mdAttributeMultiReference.apply();

    MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdAttributeMultiReference.getId());
    mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "mdBusiness");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference MdBusiness");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference MdBusiness");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdBusiness.getId());
    mdAttributeReference.apply();

    MdAttributeCharacterDAO tableName = MdAttributeCharacterDAO.newInstance();
    tableName.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdAttributeMultiReference.getId());
    tableName.setValue(MdAttributeCharacterInfo.NAME, MdAttributeMultiReferenceInfo.TABLE_NAME);
    tableName.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Table name");
    tableName.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Table name");
    tableName.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.SIZE, "255");
    tableName.apply();

    MdAttributeCharacterDAO defaultValue = MdAttributeCharacterDAO.newInstance();
    defaultValue.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdAttributeMultiReference.getId());
    defaultValue.setValue(MdAttributeCharacterInfo.NAME, MdAttributeMultiReferenceInfo.DEFAULT_VALUE);
    defaultValue.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Default value");
    defaultValue.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Default value");
    defaultValue.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    defaultValue.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    defaultValue.setValue(MdAttributeCharacterInfo.SIZE, "64");
    defaultValue.apply();

    MdBusinessDAO mdAttributeMultiTerm = MdBusinessDAO.newInstance();
    mdAttributeMultiTerm.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdAttributeMultiTerm.setValue(MdBusinessInfo.NAME, "MdAttributeMultiTerm");
    mdAttributeMultiTerm.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Metadata definition for multi term attributes");
    mdAttributeMultiTerm.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale", "MdAttributeMultiTerm");
    mdAttributeMultiTerm.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeMultiTerm.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttributeMultiReference.getId());
    mdAttributeMultiTerm.setGenerateMdController(false);
    mdAttributeMultiTerm.apply();
  }

}
