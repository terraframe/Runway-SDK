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

define(["../../ClassFramework", "../../Util", "../RunwaySDK_UI", "../factory/runway/widget/Widget", "../datatable/datasource/InstanceQueryDataSource"], function(ClassFramework, Util, UI, Widget, InstanceQueryDataSource) {
  
  var jobType = "com.runwaysdk.system.scheduler.Job";
  
  var scheduler = ClassFramework.newClass('com.runwaysdk.ui.scheduler.Scheduler', {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        
        this.$initialize("table");
        
      },
      
      _onClickStartJob : function() {
        
      },
      
      _onClickCancelJob : function() {
        
      },
      
      _onClickPauseJob : function() {
        
      },
      
      _onClickResumeJob : function() {
        
      },
      
      _onNewHeaderRowEvent : function(newRowEvent) {
        var row = newRowEvent.getRow();
        var fac = this.getFactory();
        
//        var th1 = this.getFactory().newElement("th");
//        var th2 = this.getFactory().newElement("th");
//        var th3 = this.getFactory().newElement("th");
//        var th4 = this.getFactory().newElement("th");
//        
//        row.insertBefore(th1, row.getChildren()[0]);
//        row.insertBefore(th2, row.getChildren()[0]);
//        row.insertBefore(th3, row.getChildren()[0]);
//        row.insertBefore(th4, row.getChildren()[0]);
        
        var description = fac.newElement("th");
        description.setInnerHTML("Description");
        row.appendChild(description);
        
        var progress = fac.newElement("th");
        progress.setInnerHTML("Progress");
        row.appendChild(progress);
      },
      
      _onNewRowEvent : function(newRowEvent) {
        var fac = this.getFactory();
        var row = newRowEvent.getRow();
        
        var children = newRowEvent.getRow().getChildren();
//        children[0].appendChild(fac.newButton("start", this._onClickStartJob));
//        children[1].appendChild(fac.newButton("cancel", this._onClickCancelJob));
//        children[2].appendChild(fac.newButton("pause", this._onClickPauseJob));
//        children[3].appendChild(fac.newButton("resume", this._onClickResumeJob));
        
        children[1].setInnerHTML("job description goes here")
        children[2].setInnerHTML("[|||||||||---------]");
        
        var that = this;
//        row.addEventListener("contextmenu", function(e) {
        $(row.getRawEl()).bind("contextmenu", function(e) {
          var cm = fac.newContextMenu({row: row, scheduler: that});
          cm.addItem("start", "add", function(){});
          cm.addItem("cancel", "delete", function(){});
          cm.addItem("pause", "edit", function(){});
          cm.addItem("resume", "refresh", function(){});
          cm.render();
          
          row.addClassName("row_selected");
          cm.addDestroyEventListener(function() {
            row.removeClassName("row_selected");
          });
          
          return false;
        });
//        });
      },
      
      _onFormatResponseEvent : function(formatResponseEvent) {
        var response = formatResponseEvent.getResponse();
        
        for (var i = 0; i < response.length; ++i) {
          response[i].splice(response[i].length, 0, [], []);
        }
      },
      
      render : function(parent) {
        
        var ds = new InstanceQueryDataSource({
          className: jobType,
          columns: ["Name"],
          queryAttrs: ["displayLabel"]
        });
        
        this._table = this.getFactory().newDataTable({
          el : this,
          dataSource : ds,
          "aoColumnDefs": [
             { "bSortable": false, "aTargets": [ 1, 2 ] },
             { "asSorting" : ["asc"], "aTargets" : [0]}
           ]
        });
        
        this._table.addNewRowEventListener(Mojo.Util.bind(this, this._onNewRowEvent));
        this._table.addNewHeaderRowEventListener(Mojo.Util.bind(this, this._onNewHeaderRowEvent));
        this._table.getDataSource().addFormatResponseEventListener(Mojo.Util.bind(this, this._onFormatResponseEvent));
        
        this._table.render(parent);
        
      }
      
    }
    
  });
  
  return scheduler;
  
});
