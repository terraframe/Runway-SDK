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

  var queryType = "com.runwaysdk.system.scheduler.ExecutableJob";
  
  var scheduler = ClassFramework.newClass('com.runwaysdk.ui.scheduler.Scheduler', {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        
        this.$initialize("table");
        
      },
      
      _onClickStartJob : function(contextMenu, contextMenuItem, mouseEvent) {
        var row = contextMenu.getTarget();
        var table = row.getParentTable();
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        var that = this;
        
        jobDTO.start(new Mojo.ClientRequest({
          onSuccess : function() {
            that._pollingRequest.enable();
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
            that._pollingRequest.enable();
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
        var start = cm.addItem("start", "add", Mojo.Util.bind(this, this._onClickStartJob));
        var stop = cm.addItem("stop", "delete", Mojo.Util.bind(this, this._onClickStopJob));
        var pause = cm.addItem("pause", "edit", Mojo.Util.bind(this, this._onClickPauseJob));
        var resume = cm.addItem("resume", "refresh", Mojo.Util.bind(this, this._onClickResumeJob));
        
        var status = row.getChildren()[statusRowNum].getInnerHTML();
        if (status === "Completed" || status === "Stopped" || status === "Canceled") {
          stop.setEnabled(false);
          pause.setEnabled(false);
          resume.setEnabled(false);
        }
        else if (status === "Running") {
          start.setEnabled(false);
          resume.setEnabled(false);
        }
        else if (status === "Paused") {
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
        
        var dialog = fac.newDialog("Edit Job", {width: "500px"});
        
        row.addClassName("row_selected");
        dialog.addDestroyEventListener(function() {
          row.removeClassName("row_selected");
        });
        
        var form = this.getFactory().newForm();
        
        var descriptionInput = this.getFactory().newFormControl('textarea', 'description');
        descriptionInput.setValue(jobDTO.getDescription().getLocalizedValue());
        form.addEntry("Description", descriptionInput);
        
        var cronInput = new schedulerPackage.CronInput("cron");
        cronInput.setValue(jobDTO.getCronExpression());
        form.addEntry("Scheduled Run", cronInput);
        
        dialog.appendContent(form);
        
        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        
        var that = this;
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            dialog.addButton("Submit", function() { tq.next(); });
            
            var cancelCallback = function() {
              dialog.close();
              tq.stop();
            };
            dialog.addButton("Cancel", cancelCallback);
            
            dialog.render();
          }
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            dialog.close();
            
            var lockCallback = new Mojo.ClientRequest({
              onSuccess : function(retJobDTO) {
                jobDTO = retJobDTO;
                tq.next();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);;
              }
            });
            
//            jobDTO.lock(lockCallback);
            com.runwaysdk.Facade.lock(lockCallback, jobDTO.getId());
          }
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
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
          }
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
        if (jobDTO.getRunning()) { return "Running"; }
        if (jobDTO.getCompleted()) { return "Completed"; }
        if (jobDTO.getPaused()) { return "Paused"; }
        if (jobDTO.getCanceled()) { return "Canceled"; }
          
        return "Stopped";
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
          return "Never";
        }
        else {
          return prettyCron.toString(cronStr);
        }
      },
      
      _afterPerformRequestEventListener : function(event) {
        // Decide whether to enable or disable polling based on whether or not a job is running.
        var response = event.getResponse();
        
        var STATUS_COLUMN = 3;
        
        var isRunning = false;
        for (var i = 0; i < response.length; ++i) {
          if (response[i][STATUS_COLUMN] === "Running") {
            isRunning = true;
            break;
          }
        }
        
        if (isRunning) {
          this._pollingRequest.enable();
        }
        else {
          this._pollingRequest.disable();
        }
      },
      
      render : function(parent) {
        
        var ds = new InstanceQueryDataSource({
          className: queryType,
          columns: [
            { queryAttr: "jobId" },
            { queryAttr: "description",  customFormatter: function(jobDTO){ return jobDTO.getDescription().getLocalizedValue(); } },
            { header: "Progress", customFormatter: Mojo.Util.bind(this, this.formatProgress) },
            { header: "Status", customFormatter: this.formatStatus }
//            { header: "Scheduled Run", customFormatter: this.formatScheduledRun }
          ]
        });
        
        ds.addAfterPerformRequestEventListener(Mojo.Util.bind(this, this._afterPerformRequestEventListener));
        
        this._table = this.getFactory().newDataTable({
          el : this,
          dataSource : ds
        });
        
        this._table.addNewRowEventListener(Mojo.Util.bind(this, this._onNewRowEvent));
        
        this._table.render(parent);
        
        var that = this;
        this._pollingRequest = new com.runwaysdk.ui.PollingRequest({
          callback: {
            onSuccess: function(data) {
              for (var i = 0; i < data.length; ++i) {
                that._table.updateRow(data[i], i);
              }
            },
            onFailure: function(ex) {
              that.handleException(ex);
            }
          },
          performRequest : function(callback) {
            that._table.getDataSource().performRequest(callback);
          },
          retryPollingInterval : 5000
        });
        
        this._pollingRequest.enable();
      },
      
      destroy : function() {
        this.$destroy();
        this._pollingRequest.destroy();
      }
    }
    
  });
  
  return scheduler;
  
})()
