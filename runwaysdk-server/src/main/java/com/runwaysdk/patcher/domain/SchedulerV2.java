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
package com.runwaysdk.patcher.domain;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeBoolean;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeDate;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeLong;
import com.runwaysdk.system.metadata.MdAttributeVirtual;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdView;
import com.runwaysdk.system.scheduler.AbstractJob;
import com.runwaysdk.system.scheduler.ExecutableJob;
import com.runwaysdk.system.scheduler.ExecutableJobQuery;
import com.runwaysdk.system.scheduler.JobHistory;
import com.runwaysdk.system.scheduler.JobHistoryView;

public class SchedulerV2
{
  public static void main(String[] args)
  {
    doIt();
  }
  
  public static void doIt()
  {
    /**
     * Delete unnecessary attributes
     */
    MdAttribute.get(MdAttributeInteger.CLASS, AbstractJob.CLASS + "." + "maxRetries").delete();
    MdAttribute.get(MdAttributeLong.CLASS, AbstractJob.CLASS + "." + "timeout").delete();
    MdAttribute.get(MdAttributeInteger.CLASS, AbstractJob.CLASS + "." + "workTotal").delete();
    MdAttribute.get(MdAttributeDate.CLASS, ExecutableJob.CLASS + "." + "entryDate").delete();
    MdAttribute.get(MdAttributeBoolean.CLASS, ExecutableJob.CLASS + "." + "recordHistory").delete();
    MdAttribute.get(MdAttributeInteger.CLASS, JobHistory.CLASS + "." + "retries").delete();
    
    /**
     * Migrate JobId to an MdAttributeLocal
     */
    MdAttributeLocalCharacterDAO displayLabel = MdAttributeLocalCharacterDAO.newInstance();
    displayLabel.setValue(MdAttributeLocalCharacterInfo.NAME, MdTermInfo.DISPLAY_LABEL);
    displayLabel.setValue(MdAttributeLocalCharacterInfo.DEFINING_MD_CLASS, MdClass.get(MdBusiness.CLASS, AbstractJob.CLASS).getId());
    displayLabel.setStructValue(MdAttributeLocalCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Display Label");
    displayLabel.setValue(MdAttributeLocalCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    displayLabel.apply();
    
    ExecutableJobQuery ejq = new ExecutableJobQuery(new QueryFactory());
    OIterator<? extends ExecutableJob> it = ejq.getIterator();
    try
    {
      for (ExecutableJob job : it)
      {
        String defaultLocale = job.getValue("jobId");
        job.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, defaultLocale);
      }
    }
    finally
    {
      it.close();
    }
    MdAttribute.get(MdAttributeLong.CLASS, ExecutableJob.CLASS + "." + "jobId").delete();
    
    /**
     * Create some new attributes on JobHistory
     */
    MdAttributeInteger workTotal = new MdAttributeInteger();
    workTotal.setAttributeName("workTotal");
    workTotal.getDisplayLabel().setDefaultValue("Work Total");
    workTotal.getDescription().setDefaultValue("Work Total");
    workTotal.setRequired(false);
    workTotal.setRejectNegative(true);
    workTotal.setDefiningMdClass(MdClass.getByKey(JobHistory.CLASS));
    workTotal.apply();
    
    /**
     * JobHistoryView
     */
    MdAttributeVirtual displayLabelVirtual = new MdAttributeVirtual();
    displayLabelVirtual.setAttributeName(MdAttributeLocalCharacterInfo.DISPLAY_LABEL);
    displayLabelVirtual.setDefiningMdView(MdView.getMdView(JobHistoryView.CLASS));
    displayLabelVirtual.setMdAttributeConcrete(MdAttributeConcrete.getByKey(AbstractJob.CLASS + "." + MdAttributeLocalCharacterInfo.DISPLAY_LABEL));
    displayLabelVirtual.apply();
    
    MdAttributeVirtual workTotalVirtual = new MdAttributeVirtual();
    workTotalVirtual.setAttributeName("workTotal");
    workTotalVirtual.setDefiningMdView(MdView.getMdView(JobHistoryView.CLASS));
    workTotalVirtual.setMdAttributeConcrete(MdAttributeConcrete.getByKey(JobHistory.CLASS + "." + "workTotal"));
    workTotalVirtual.apply();
  }
}
