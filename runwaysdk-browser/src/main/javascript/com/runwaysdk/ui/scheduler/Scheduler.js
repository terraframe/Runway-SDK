/*
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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

//define(["../../ClassFramework", "../../Util", "../RunwaySDK_UI", "../factory/runway/widget/Widget", "../datatable/datasource/InstanceQueryDataSource", "./CronPicker", "prettycron"], function(ClassFramework, Util, UI, Widget, InstanceQueryDataSource, CronPicker) {
(function(){

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  var InstanceQueryDataSource = com.runwaysdk.ui.datatable.datasource.InstanceQueryDataSource;
  var schedulerPackage = Mojo.Meta.alias("com.runwaysdk.ui.scheduler.*");
  
  // In miliseconds
  var JOBS_POLLING_INTERVAL = 1000;
  var HISTORY_POLLING_INTERVAL = 6000;

  var JOB_QUERY_TYPE = "com.runwaysdk.system.scheduler.ExecutableJob";
  var HISTORY_QUERY_TYPE = "com.runwaysdk.system.scheduler.JobHistory";
  
  var schedulerName = 'com.runwaysdk.ui.scheduler.Scheduler';
  var jobTableName = 'com.runwaysdk.ui.scheduler.JobTable';
  var jobHistoryTableName = "com.runwaysdk.ui.scheduler.JobHistoryTable";
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(schedulerName, {
    "jobs" : "Jobs",
    "history" : "History",
    
    "editJobTitle" : "Edit Job",
    "scheduledRun" : "Scheduled Run",
    "submit" : "Submit",
    "cancel" : "Cancel",
    "never" : "Never",
    "progress" : "Progress",
    
    "duration" : "Duration",
    "problems" : "Problems",
    "seconds" : "seconds",
     
     // The metadata for MdMethods is not included in the Javascript query results, which is why we have to hardcode these values here (for now at least).
    "start" : "Start",
    "stop" : "Stop",
    "pause" : "Pause",
    "resume" : "Resume",
    
    // When we refactor the Job metadata from flags into a status enum these will be removed.
    "stopped" : "Stopped",
    "status" : "Status",
  });
  
  var scheduler = ClassFramework.newClass(schedulerName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(config) {
        this._tabPanel = this.getFactory().newTabPanel();
        
        this.$initialize(this._tabPanel);
        
        this._config = config || {};
        this._config.language = this._config.language || {};
        Util.merge(com.runwaysdk.Localize.getLanguage(schedulerName), this._config.language);
        
        this._jobTable = new JobTable(this._config);
        this._tabPanel.addPanel(this._config.language["jobs"], this._jobTable);
        
        this._historyTable = new JobHistoryTable(this._config);
        this._tabPanel.addPanel(this._config.language["history"], this._historyTable);
        
        this._tabPanel.addSwitchPanelEventListener(Mojo.Util.bind(this, this.onSwitchPanel));
      },
      
      onSwitchPanel : function(switchPanelEvent) {
        var panel = switchPanelEvent.getPanel();
        
        if (panel.getPanelNumber() === 0) { // Jobs
          this._jobTable.getPollingRequest().enable();
          this._historyTable.getPollingRequest().disable();
        }
        else if (panel.getPanelNumber() === 1) { // History
          this._jobTable.getPollingRequest().disable();
          this._historyTable.getPollingRequest().enable();
        }
      },
      
      render : function(parent) {
        this._tabPanel.render(parent);
      }
    }
  });
  
  var JobTable = ClassFramework.newClass(jobTableName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(config) {
        
        this.$initialize("table");
        
        this._config = config;
        
      },
      
      _onClickStartJob : function(contextMenu, contextMenuItem, mouseEvent) {
        var row = contextMenu.getTarget();
        var table = row.getParentTable();
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        var that = this;
        
        jobDTO.start(new Mojo.ClientRequest({
          onSuccess : function() {
            
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        }));
      },
      
      _onClickStopJob : function(contextMenu, contextMenuItem, mouseEvent) {
        var row = contextMenu.getTarget();
        var table = row.getParentTable();
        var resultsQueryDTO = table.getDataSource().getResultsQueryDTO();
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        var that = this;
        
        jobDTO.stop(new Mojo.ClientRequest({
          onSuccess : function() {
            
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        }));
      },
      
      _onClickPauseJob : function(contextMenu, contextMenuItem, mouseEvent) {
        var row = contextMenu.getTarget();
        var table = row.getParentTable();
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        var that = this;
        
        jobDTO.pause(new Mojo.ClientRequest({
          onSuccess : function() {
            
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        }));
      },
      
      _onClickResumeJob : function(contextMenu, contextMenuItem, mouseEvent) {
        var row = contextMenu.getTarget();
        var table = row.getParentTable();
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        var that = this;
        
        jobDTO.resume(new Mojo.ClientRequest({
          onSuccess : function() {
            
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        }));
      },
      
      _openContextMenu : function(mouseEvent) {
        var fac = this.getFactory();
        var row = mouseEvent.getTarget().getParent();
        var jobMetadata = row.getParentTable().getDataSource().getMetadataQueryDTO();
        var statusRowNum = 3;
        
        // Create Runway's Context Menu
        var cm = fac.newContextMenu(row);
        var start = cm.addItem(this._config.language["start"], "add", Mojo.Util.bind(this, this._onClickStartJob));
        var stop = cm.addItem(this._config.language["stop"], "delete", Mojo.Util.bind(this, this._onClickStopJob));
        var pause = cm.addItem(this._config.language["pause"], "edit", Mojo.Util.bind(this, this._onClickPauseJob));
        var resume = cm.addItem(this._config.language["resume"], "refresh", Mojo.Util.bind(this, this._onClickResumeJob));
        
        var completed = jobMetadata.getAttributeDTO("completed").getAttributeMdDTO().getDisplayLabel();
        var stopped = this._config.language["stopped"];
        var canceled = jobMetadata.getAttributeDTO("canceled").getAttributeMdDTO().getDisplayLabel();
        var running = jobMetadata.getAttributeDTO("running").getAttributeMdDTO().getDisplayLabel();
        var paused = jobMetadata.getAttributeDTO("paused").getAttributeMdDTO().getDisplayLabel();
        
        var status = row.getChildren()[statusRowNum].getInnerHTML();
        if (status === completed || status === stopped || status === canceled) {
          stop.setEnabled(false);
          pause.setEnabled(false);
          resume.setEnabled(false);
        }
        else if (status === running) {
          start.setEnabled(false);
          resume.setEnabled(false);
        }
        else if (status === paused) {
          start.setEnabled(false);
          pause.setEnabled(false);
        }
        
        cm.render();
        
        row.addClassName("row_selected");
        cm.addDestroyEventListener(function() {
          row.removeClassName("row_selected");
        });
        
        return false; // Prevents default (displaying the browser context menu)
      },
      
      _openEditMenu : function(mouseEvent) {
        var fac = this.getFactory();
        var row = mouseEvent.getTarget().getParent();
        var table = row.getParentTable();
        var jobMetadata = table.getDataSource().getMetadataQueryDTO();
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        
        var dialog = fac.newDialog(this._config.language["editJobTitle"], {width: "500px"});
        
        row.addClassName("row_selected");
        dialog.addDestroyEventListener(function() {
          row.removeClassName("row_selected");
        });
        
        var form = this.getFactory().newForm();
        
        var descriptionInput = this.getFactory().newFormControl('textarea', 'description');
        descriptionInput.setValue(jobDTO.getDescription().getLocalizedValue());
        form.addEntry(jobMetadata.getAttributeDTO("description").getAttributeMdDTO().getDisplayLabel(), descriptionInput);
        
        var cronInput = new com.runwaysdk.ui.CronInput("cron");
        cronInput.setValue(jobDTO.getCronExpression());
        form.addEntry(this._config.language["scheduledRun"], cronInput);
        
        dialog.appendContent(form);
        
        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        
        var that = this;
        
        tq.addTask(new Structure.TaskIF({
          start : Mojo.Util.bind(this, function(){
            dialog.addButton(this._config.language["submit"], function() { tq.next(); });
            
            var cancelCallback = function() {
              dialog.close();
              tq.stop();
            };
            dialog.addButton(this._config.language["cancel"], cancelCallback);
            
            dialog.render();
          })
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : Mojo.Util.bind(this, function(){
            dialog.close();
            
            var lockCallback = new Mojo.ClientRequest({
              onSuccess : function(retJobDTO) {
                jobDTO = retJobDTO;
                tq.next();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            });
            
            com.runwaysdk.Facade.lock(lockCallback, jobDTO.getId());
          })
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : Mojo.Util.bind(this, function(){
            var values = form.accept(fac.newFormControl('FormVisitor'));
            
            jobDTO.getDescription().localizedValue = values.get("description");
            jobDTO.setCronExpression(values.get("cron"));
            
            var applyCallback = new Mojo.ClientRequest({
              onSuccess : function() {
                // Intentionally empty
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            });
            
            jobDTO.apply(applyCallback);
          })
        }));
        
        tq.start();
        
        return false;
      },
      
      _onNewRowEvent : function(newRowEvent) {
        var fac = this.getFactory();
        var row = newRowEvent.getRow();
        
        var progressRowNum = 2;
        
        if (row.getRowNumber() == progressRowNum) {
          // TODO create a progress bar widget
        }
        
        row.addEventListener("dblclick", Mojo.Util.bind(this, this._openEditMenu));
        row.addEventListener("contextmenu", Mojo.Util.bind(this, this._openContextMenu));
      },
      
      formatStatus : function(jobDTO) {
        
        var jobMetadata = this._table.getDataSource().getMetadataQueryDTO();
        
        var completed = jobMetadata.getAttributeDTO("completed").getAttributeMdDTO().getDisplayLabel();
        var stopped = this._config.language["stopped"];
        var canceled = jobMetadata.getAttributeDTO("canceled").getAttributeMdDTO().getDisplayLabel();
        var running = jobMetadata.getAttributeDTO("running").getAttributeMdDTO().getDisplayLabel();
        var paused = jobMetadata.getAttributeDTO("paused").getAttributeMdDTO().getDisplayLabel();
        
        if (jobDTO.getRunning()) { return running; }
        if (jobDTO.getCompleted()) { return completed }
        if (jobDTO.getPaused()) { return paused; }
        if (jobDTO.getCanceled()) { return canceled; }
          
        return stopped;
      },
      
      formatProgress : function(jobDTO) {
        if (jobDTO.getWorkTotal() == null || jobDTO.getWorkProgress() == null || jobDTO.getWorkTotal() == 0) {
          return null;
        }
        
        return ((jobDTO.getWorkProgress() / jobDTO.getWorkTotal()) * 100) + "%";
      },
      
      formatScheduledRun : function(jobDTO) {
        var cronStr = jobDTO.getCronExpression();
        
        if (cronStr == null || cronStr === "") {
          return this._config.language["never"];
        }
        else {
          return prettyCron.toString(cronStr);
        }
      },
      
      render : function(parent) {
        
        var ds = new InstanceQueryDataSource({
          className: JOB_QUERY_TYPE,
          columns: [
            { queryAttr: "jobId" },
            { queryAttr: "description",  customFormatter: function(jobDTO){ return jobDTO.getDescription().getLocalizedValue(); } },
            { header: this._config.language["progress"], customFormatter: Mojo.Util.bind(this, this.formatProgress) },
            { header: this._config.language["status"], customFormatter: Mojo.Util.bind(this, this.formatStatus) },
            { header: this._config.language["scheduledRun"], customFormatter: function(job) {
              return com.runwaysdk.ui.CronUtil.cronToHumanReadable(job.getCronExpression());
            } }
          ]
        });
        
        // Create the DataTable impl
        this._config.el = this;
        this._config.dataSource = ds;
        this._table = this.getFactory().newDataTable(this._config);
        
        this._table.addNewRowEventListener(Mojo.Util.bind(this, this._onNewRowEvent));
        
        this._table.render(parent);
        
        var that = this;
        this._pollingRequest = new com.runwaysdk.ui.PollingRequest({
          callback: {
            onSuccess: function(data) {
              
            },
            onFailure: function(ex) {
              that.handleException(ex);
            }
          },
          performRequest : function(callback) {
            that._table.refresh(callback);
          },
          pollingInterval : JOBS_POLLING_INTERVAL
        });
        
        this._pollingRequest.enable();
      },
      
      getPollingRequest : function() {
        return this._pollingRequest;
      },
      
      destroy : function() {
        this.$destroy();
        this._pollingRequest.destroy();
      }
    }
    
  });
  
  var JobHistoryTable = ClassFramework.newClass(jobHistoryTableName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(config) {
        
        this.$initialize("table");
        
        this._config = config;
        
      },
      
      getPollingRequest : function() {
        return this._pollingRequest;
      },
      
      render : function(parent) {
        var that = this;
        
        var ds = new com.runwaysdk.ui.datatable.datasource.MdMethodDataSource({
          method : function(clientRequest) {
            com.runwaysdk.system.scheduler.JobHistoryView.getJobHistories(clientRequest, this.getSortAttr(), this.isAscending(), this.getPageSize(), this.getPageNumber());
          },
          columns : [
                     {queryAttr: "lastRun"},
                     {queryAttr: "jobId"},
                     {header: that._config.language["duration"], customFormatter: function(view) { return ((view.getEndTime() - view.getStartTime()) / 1000) + " " + that._config.language["seconds"] + "."; }},
                     {queryAttr: "description"},
                     {header: that._config.language["problems"], customFormatter : function(view) {
                       // This may be a workaround to a bug in runway, the value isn't getting set to the localized value.
                       return view.getAttributeDTO('historyInformation').getValue();
//                       return view.getHistoryInformation().getLocalizedValue();
                     }}
                    ]
        });
        
        // Create the DataTable impl
        this._config["iDisplayLength"] = 5;
        this._config.el = this;
        this._config.dataSource = ds;
        this._table = this.getFactory().newDataTable(this._config);
        this._table.render(parent);
        
        this._pollingRequest = new com.runwaysdk.ui.PollingRequest({
          callback: {
            onSuccess: function(data) {
              
            },
            onFailure: function(ex) {
              that.handleException(ex);
            }
          },
          performRequest : function(callback) {
            that._table.refresh(callback);
          },
          pollingInterval : HISTORY_POLLING_INTERVAL
        });
      }
    }
  });
  
  return scheduler;
  
})();
