/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.system.scheduler;

@com.runwaysdk.business.ClassSignature(hash = 1231901536)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to JobHistoryView.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  abstract  class JobHistoryViewQueryBase extends com.runwaysdk.query.GeneratedViewQuery

{

  public JobHistoryViewQueryBase(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
  }

  public JobHistoryViewQueryBase(com.runwaysdk.query.QueryFactory componentQueryFactory, com.runwaysdk.query.ViewQueryBuilder viewQueryBuilder)
  {
    super(componentQueryFactory, viewQueryBuilder);
  }
  public String getClassType()
  {
    return com.runwaysdk.system.scheduler.JobHistoryView.CLASS;
  }
  public com.runwaysdk.query.SelectableMoment getCreateDate()
  {
    return getCreateDate(null);

  }
 
  public com.runwaysdk.query.SelectableMoment getCreateDate(String alias)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.CREATEDATE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableMoment getCreateDate(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.CREATEDATE, alias, displayLabel);

  }
 
  public com.runwaysdk.query.SelectableChar getCronExpression()
  {
    return getCronExpression(null);

  }
 
  public com.runwaysdk.query.SelectableChar getCronExpression(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.CRONEXPRESSION, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getCronExpression(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.CRONEXPRESSION, alias, displayLabel);

  }
 
  public com.runwaysdk.system.scheduler.ExecutableJobDescriptionQuery.ExecutableJobDescriptionQueryStructIF getDescription()
  {
    return getDescription(null);

  }
 
  public com.runwaysdk.system.scheduler.ExecutableJobDescriptionQuery.ExecutableJobDescriptionQueryStructIF getDescription(String alias)
  {
    return (com.runwaysdk.system.scheduler.ExecutableJobDescriptionQuery.ExecutableJobDescriptionQueryStructIF)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.DESCRIPTION, alias, null);

  }
 
  public com.runwaysdk.system.scheduler.ExecutableJobDescriptionQuery.ExecutableJobDescriptionQueryStructIF getDescription(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.scheduler.ExecutableJobDescriptionQuery.ExecutableJobDescriptionQueryStructIF)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.DESCRIPTION, alias, displayLabel);

  }
 
  public com.runwaysdk.system.scheduler.AbstractJobDisplayLabelQuery.AbstractJobDisplayLabelQueryStructIF getDisplayLabel()
  {
    return getDisplayLabel(null);

  }
 
  public com.runwaysdk.system.scheduler.AbstractJobDisplayLabelQuery.AbstractJobDisplayLabelQueryStructIF getDisplayLabel(String alias)
  {
    return (com.runwaysdk.system.scheduler.AbstractJobDisplayLabelQuery.AbstractJobDisplayLabelQueryStructIF)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.DISPLAYLABEL, alias, null);

  }
 
  public com.runwaysdk.system.scheduler.AbstractJobDisplayLabelQuery.AbstractJobDisplayLabelQueryStructIF getDisplayLabel(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.scheduler.AbstractJobDisplayLabelQuery.AbstractJobDisplayLabelQueryStructIF)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.DISPLAYLABEL, alias, displayLabel);

  }
 
  public com.runwaysdk.query.SelectableMoment getEndTime()
  {
    return getEndTime(null);

  }
 
  public com.runwaysdk.query.SelectableMoment getEndTime(String alias)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.ENDTIME, alias, null);

  }
 
  public com.runwaysdk.query.SelectableMoment getEndTime(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.ENDTIME, alias, displayLabel);

  }
 
  public com.runwaysdk.system.scheduler.JobHistoryHistoryCommentQuery.JobHistoryHistoryCommentQueryStructIF getHistoryComment()
  {
    return getHistoryComment(null);

  }
 
  public com.runwaysdk.system.scheduler.JobHistoryHistoryCommentQuery.JobHistoryHistoryCommentQueryStructIF getHistoryComment(String alias)
  {
    return (com.runwaysdk.system.scheduler.JobHistoryHistoryCommentQuery.JobHistoryHistoryCommentQueryStructIF)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.HISTORYCOMMENT, alias, null);

  }
 
  public com.runwaysdk.system.scheduler.JobHistoryHistoryCommentQuery.JobHistoryHistoryCommentQueryStructIF getHistoryComment(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.scheduler.JobHistoryHistoryCommentQuery.JobHistoryHistoryCommentQueryStructIF)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.HISTORYCOMMENT, alias, displayLabel);

  }
 
  public com.runwaysdk.system.scheduler.JobHistoryHistoryInformationQuery.JobHistoryHistoryInformationQueryStructIF getHistoryInformation()
  {
    return getHistoryInformation(null);

  }
 
  public com.runwaysdk.system.scheduler.JobHistoryHistoryInformationQuery.JobHistoryHistoryInformationQueryStructIF getHistoryInformation(String alias)
  {
    return (com.runwaysdk.system.scheduler.JobHistoryHistoryInformationQuery.JobHistoryHistoryInformationQueryStructIF)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.HISTORYINFORMATION, alias, null);

  }
 
  public com.runwaysdk.system.scheduler.JobHistoryHistoryInformationQuery.JobHistoryHistoryInformationQueryStructIF getHistoryInformation(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.scheduler.JobHistoryHistoryInformationQuery.JobHistoryHistoryInformationQueryStructIF)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.HISTORYINFORMATION, alias, displayLabel);

  }
 
  public com.runwaysdk.system.scheduler.JobOperationQuery.AllJobOperationQueryIF getJobOperation()
  {
    return getJobOperation(null);

  }
 
  public com.runwaysdk.system.scheduler.JobOperationQuery.AllJobOperationQueryIF getJobOperation(String alias)
  {
    return (com.runwaysdk.system.scheduler.JobOperationQuery.AllJobOperationQueryIF)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.JOBOPERATION, alias, null);

  }
 
  public com.runwaysdk.system.scheduler.JobOperationQuery.AllJobOperationQueryIF getJobOperation(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.scheduler.JobOperationQuery.AllJobOperationQueryIF)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.JOBOPERATION, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableMoment getLastRun()
  {
    return getLastRun(null);

  }
 
  public com.runwaysdk.query.SelectableMoment getLastRun(String alias)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.LASTRUN, alias, null);

  }
 
  public com.runwaysdk.query.SelectableMoment getLastRun(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.LASTRUN, alias, displayLabel);

  }
 
  public com.runwaysdk.query.SelectableUUID getOid()
  {
    return getOid(null);

  }
 
  public com.runwaysdk.query.SelectableUUID getOid(String alias)
  {
    return (com.runwaysdk.query.SelectableUUID)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.OID, alias, null);

  }
 
  public com.runwaysdk.query.SelectableUUID getOid(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableUUID)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.OID, alias, displayLabel);

  }
 
  public com.runwaysdk.query.SelectableMoment getStartTime()
  {
    return getStartTime(null);

  }
 
  public com.runwaysdk.query.SelectableMoment getStartTime(String alias)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.STARTTIME, alias, null);

  }
 
  public com.runwaysdk.query.SelectableMoment getStartTime(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.STARTTIME, alias, displayLabel);

  }
 
  public com.runwaysdk.system.scheduler.JobStatusQuery.AllJobStatusQueryIF getStatus()
  {
    return getStatus(null);

  }
 
  public com.runwaysdk.system.scheduler.JobStatusQuery.AllJobStatusQueryIF getStatus(String alias)
  {
    return (com.runwaysdk.system.scheduler.JobStatusQuery.AllJobStatusQueryIF)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.STATUS, alias, null);

  }
 
  public com.runwaysdk.system.scheduler.JobStatusQuery.AllJobStatusQueryIF getStatus(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.scheduler.JobStatusQuery.AllJobStatusQueryIF)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.STATUS, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getStatusLabel()
  {
    return getStatusLabel(null);

  }
 
  public com.runwaysdk.query.SelectableChar getStatusLabel(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.STATUSLABEL, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getStatusLabel(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.STATUSLABEL, alias, displayLabel);

  }
 
  public com.runwaysdk.query.SelectableLong getWorkProgress()
  {
    return getWorkProgress(null);

  }
 
  public com.runwaysdk.query.SelectableLong getWorkProgress(String alias)
  {
    return (com.runwaysdk.query.SelectableLong)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.WORKPROGRESS, alias, null);

  }
 
  public com.runwaysdk.query.SelectableLong getWorkProgress(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableLong)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.WORKPROGRESS, alias, displayLabel);

  }
 
  public com.runwaysdk.query.SelectableLong getWorkTotal()
  {
    return getWorkTotal(null);

  }
 
  public com.runwaysdk.query.SelectableLong getWorkTotal(String alias)
  {
    return (com.runwaysdk.query.SelectableLong)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.WORKTOTAL, alias, null);

  }
 
  public com.runwaysdk.query.SelectableLong getWorkTotal(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableLong)this.getSelectable(com.runwaysdk.system.scheduler.JobHistoryView.WORKTOTAL, alias, displayLabel);

  }
 
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  @java.lang.SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends JobHistoryView> getIterator()
  {
    com.runwaysdk.query.ValueIterator valueIterator;
    if (_pageSize != null && _pageNumber != null)
    {
      valueIterator = (com.runwaysdk.query.ValueIterator<com.runwaysdk.dataaccess.ValueObject>)this.getComponentQuery().getIterator(_pageSize, _pageNumber);
    }
    else
    {
      valueIterator = (com.runwaysdk.query.ValueIterator<com.runwaysdk.dataaccess.ValueObject>)this.getComponentQuery().getIterator();
    }
    return new com.runwaysdk.query.ViewIterator<JobHistoryView>(this.getMdClassIF(), valueIterator);
  }

}
