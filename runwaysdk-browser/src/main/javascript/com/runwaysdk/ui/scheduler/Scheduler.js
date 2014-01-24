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
  var shortPollingInterval = 800; // Used when a job has status = running 
  var longPollingInterval = 6000; // Used when no jobs are currently running

  var JOB_QUERY_TYPE = "com.runwaysdk.system.scheduler.ExecutableJob";
  var HISTORY_QUERY_TYPE = "com.runwaysdk.system.scheduler.JobHistory";
  
  var scheduler = ClassFramework.newClass('com.runwaysdk.ui.scheduler.Scheduler', {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        this._tabPanel = this.getFactory().newTabPanel();
        
        this.$initialize(this._tabPanel);
        
        this._jobTable = new JobTable();
        this._tabPanel.addPanel("Jobs", this._jobTable);
        
        this._historyTable = new JobHistoryTable();
        this._tabPanel.addPanel("History", this._historyTable);
        
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
  
  var JobTable = ClassFramework.newClass('com.runwaysdk.ui.scheduler.JobTable', {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg, oLanguage) {
        
        this.$initialize("table");
        this._oLanguage = oLanguage;
        
        if(this._oLanguage === null || this._oLanguage === undefined)
        {
          this._oLanguage = {
            oSchedule: {
              sStart: "Start",
              sStop: "Stop",
              sPause: "Pause",
              sResume: "Resume",
              sCompleted: "Completed",
              sStopped: "Stopped",
              sCanceled: "Canceled",
              sRunning: "Running",
              sPaused: "Paused",
              sEditJob: "Edit job",
              sDescription: "Description",
              sScheduledRun: "Scheduled run",
              sSubmit: "Submit",
              sCancel: "Cancel",
              sNever: "Never",
              sName: "Name",
              sDescription: "Description",
              sProgress: "Progress",
              sStatus: "Status"
            }
          };
        }        
      },
      
      _onClickStartJob : function(contextMenu, contextMenuItem, mouseEvent) {
        var row = contextMenu.getTarget();
        var table = row.getParentTable();
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        var that = this;
        
        jobDTO.start(new Mojo.ClientRequest({
          onSuccess : function() {
//            that._pollingRequest.setPollingInterval(shortPollingInterval);
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
//            that._pollingRequest.setPollingInterval(shortPollingInterval);
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        }));
      },
      
      _openContextMenu : function(mouseEvent) {
        var fac = this.getFactory();
        var row = mouseEvent.getTarget().getParent();
        var statusRowNum = 3;
        
        var cm = fac.newContextMenu(row);
        var start = cm.addItem(this._oLanguage["oSchedule"]["sStart"], "add", Mojo.Util.bind(this, this._onClickStartJob));
        var stop = cm.addItem(this._oLanguage["oSchedule"]["sStop"], "delete", Mojo.Util.bind(this, this._onClickStopJob));
        var pause = cm.addItem(this._oLanguage["oSchedule"]["sPause"], "edit", Mojo.Util.bind(this, this._onClickPauseJob));
        var resume = cm.addItem(this._oLanguage["oSchedule"]["sResume"], "refresh", Mojo.Util.bind(this, this._onClickResumeJob));
        
        var status = row.getChildren()[statusRowNum].getInnerHTML();
        if (status === this._oLanguage["oSchedule"]["sCompleted"] || status === this._oLanguage["oSchedule"]["sStopped"] || status === this._oLanguage["oSchedule"]["sCanceled"]) {
          stop.setEnabled(false);
          pause.setEnabled(false);
          resume.setEnabled(false);
        }
        else if (status === this._oLanguage["oSchedule"]["sRunning"]) {
          start.setEnabled(false);
          resume.setEnabled(false);
        }
        else if (status === this._oLanguage["oSchedule"]["sPaused"]) {
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
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        
        var dialog = fac.newDialog(this._oLanguage["oSchedule"]["sEditJob"], {width: "500px"});
        
        row.addClassName("row_selected");
        dialog.addDestroyEventListener(function() {
          row.removeClassName("row_selected");
        });
        
        var form = this.getFactory().newForm();
        
        var descriptionInput = this.getFactory().newFormControl('textarea', 'description');
        descriptionInput.setValue(jobDTO.getDescription().getLocalizedValue());
        form.addEntry(this._oLanguage["oSchedule"]["sDescription"], descriptionInput);
        
        var cronInput = new schedulerPackage.CronInput("cron");
        cronInput.setValue(jobDTO.getCronExpression());
        form.addEntry(this._oLanguage["oSchedule"]["sScheduledRun"], cronInput);
        
        dialog.appendContent(form);
        
        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        
        var that = this;
        
        tq.addTask(new Structure.TaskIF({
          start : Mojo.Util.bind(this, function(){
            dialog.addButton(this._oLanguage["oSchedule"]["sSubmit"], function() { tq.next(); });
            
            var cancelCallback = function() {
              dialog.close();
              tq.stop();
            };
            dialog.addButton(this._oLanguage["oSchedule"]["sCancel"], cancelCallback);
            
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
            
//            jobDTO.lock(lockCallback);
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
        if (jobDTO.getRunning()) { return this._oLanguage["oSchedule"]["sRunning"]; }
        if (jobDTO.getCompleted()) { return this._oLanguage["oSchedule"]["sCompleted"]; }
        if (jobDTO.getPaused()) { return this._oLanguage["oSchedule"]["sPaused"]; }
        if (jobDTO.getCanceled()) { return this._oLanguage["oSchedule"]["sCanceled"]; }
          
        return this._oLanguage["oSchedule"]["sStopped"];
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
          return this._oLanguage["oSchedule"]["sNever"];
        }
        else {
          return prettyCron.toString(cronStr);
        }
      },
      
//      _afterPerformRequestEventListener : function(event) {
//        // Decide how long to wait between polling based on whether or not a job is running.
//        var response = event.getResponse();
//        
//        var STATUS_COLUMN = 3;
//        
//        var isRunning = false;
//        for (var i = 0; i < response.length; ++i) {
//          if (response[i][STATUS_COLUMN] === this._oLanguage["oSchedule"]["sRunning"]) {
//            isRunning = true;
//            break;
//          }
//        }
//        
//        if (isRunning) {
//          this._pollingRequest.setPollingInterval(shortPollingInterval);
//        }
//        else {
//          this._pollingRequest.setPollingInterval(longPollingInterval);
//        }
//      },
      
      render : function(parent) {
        
        var ds = new InstanceQueryDataSource({
          className: JOB_QUERY_TYPE,
          columns: [
            { queryAttr: "jobId" },
            { queryAttr: "description",  customFormatter: function(jobDTO){ return jobDTO.getDescription().getLocalizedValue(); } },
            { header: this._oLanguage["oSchedule"]["sProgress"], customFormatter: Mojo.Util.bind(this, this.formatProgress) },
            { header: this._oLanguage["oSchedule"]["sStatus"], customFormatter: Mojo.Util.bind(this, this.formatStatus) }
//            { header: "Scheduled Run", customFormatter: this.formatScheduledRun }
          ]
        });
        
//        ds.addAfterPerformRequestEventListener(Mojo.Util.bind(this, this._afterPerformRequestEventListener));
        
        this._table = this.getFactory().newDataTable({
          el : this,
          dataSource : ds,
          oLanguage : this._oLanguage
        });
        
        this._table.addNewRowEventListener(Mojo.Util.bind(this, this._onNewRowEvent));
        
        this._table.render(parent);
        
        var that = this;
        this._pollingRequest = new com.runwaysdk.ui.PollingRequest({
          callback: {
            onSuccess: function(data) {
              for (var i = 0; i < data.length; ++i) {
                if (that._table.getNumberOfRows() <= i) {
                  // full refresh, update row will throw an error on the datatables.net widget because the row doesn't exist. Add row won't work because we're using server-side data.
                  that._table.refresh();
                }
                else {
                 that._table.updateRow(data[i], i);
                }
              }
            },
            onFailure: function(ex) {
              that.handleException(ex);
            }
          },
          performRequest : function(callback) {
            that._table.getDataSource().performRequest(callback);
          },
          pollingInterval : shortPollingInterval,
          retryPollingInterval : longPollingInterval
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
  
  var JobHistoryTable = ClassFramework.newClass('com.runwaysdk.ui.scheduler.JobHistoryTable', {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        this.$initialize("table");
      },
      
      getPollingRequest : function() {
        return this._pollingRequest;
      },
      
      render : function(parent) {
        var that = this;
        
        var ds = new InstanceQueryDataSource({
          className: HISTORY_QUERY_TYPE,
          columns: [
            { queryAttr: "createDate" },
            { queryAttr: "historyInformation",  customFormatter: function(historyDTO){  } },
            { queryAttr: "historyComment",  customFormatter: function(historyDTO){  } },
            { queryAttr: "jobSnapshot",  customFormatter: function(historyDTO){  } }
          ]
        });
        
        this._table = this.getFactory().newDataTable({
          el : this,
          dataSource : ds
        });
        
        this._table.render(parent);
        
        this._pollingRequest = new com.runwaysdk.ui.PollingRequest({
          callback: {
            onSuccess: function(data) {
              for (var i = 0; i < data.length; ++i) {
                if (that._table.getNumberOfRows() <= i) {
                  // full refresh, update row will throw an error on the datatables.net widget because the row doesn't exist. Add row won't work because we're using server-side data.
                  that._table.refresh();
                }
                else {
                 that._table.updateRow(data[i], i);
                }
              }
            },
            onFailure: function(ex) {
              that.handleException(ex);
            }
          },
          performRequest : function(callback) {
            that._table.getDataSource().performRequest(callback);
          },
          pollingInterval : longPollingInterval,
          retryPollingInterval : longPollingInterval
        });
        
//        this._pollingRequest.enable();
      }
    }
  });
  
  return scheduler;
  
})();
