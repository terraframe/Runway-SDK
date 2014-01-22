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

//define(["../../ClassFramework", "../../Util", "../RunwaySDK_UI", "../factory/runway/widget/Widget", "../datatable/datasource/InstanceQueryDataSource"], function(ClassFramework, Util, UI, Widget, InstanceQueryDataSource) {
(function(){  

  var Util = Mojo.Util;
  var ClassFramework = Mojo.Meta;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  var InstanceQueryDataSource = com.runwaysdk.ui.datatable.datasource.InstanceQueryDataSource;
  
  var queryType = "com.runwaysdk.system.Users";
  
  var usersTable = ClassFramework.newClass('com.runwaysdk.ui.userstable.UsersTable', {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        
        this.$initialize("table");
        
      },
      
      _onEditUser : function(mouseEvent) {
        var fac = this.getFactory();
        var row = mouseEvent.getTarget().getParent();
        var table = row.getParentTable();
        var usersDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        
        var dialog = fac.newDialog("Edit User");
        
        row.addClassName("row_selected");
        dialog.addDestroyEventListener(function() {
          row.removeClassName("row_selected");
        });
        
        var form = this.getFactory().newForm();
        
        var usernameInput = this.getFactory().newFormControl('text', 'username');
        usernameInput.setValue(usersDTO.getUsername());
        form.addEntry("User Name", usernameInput);
        
        var passwordInput = this.getFactory().newFormControl('text', 'password');
        passwordInput.setValue(usersDTO.getPassword());
        form.addEntry("Password", passwordInput);
        
        var localeInput = this.getFactory().newFormControl('select', 'locale');
        localeInput.addOption("locale1", "locale1", true);
        localeInput.addOption("locale2", "locale2", false);
        localeInput.addOption("locale3", "locale3", false);
        form.addEntry("Locale", localeInput);
        
        var inactiveInput = this.getFactory().newFormControl('text', 'inactive');
        inactiveInput.setValue(usersDTO.getInactive().toString());
        form.addEntry("Inactive", inactiveInput);
        
        var sessionLimitInput = this.getFactory().newFormControl('text', 'sessionLimit');
        sessionLimitInput.setValue(usersDTO.getSessionLimit());
        form.addEntry("Session Limit", sessionLimitInput);
        
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
            var values = form.accept(fac.newFormControl('FormVisitor'));
            dialog.close();
            
            usersDTO.setUsername(values.get("username"));
            usersDTO.setPassword(values.get("password"));
//              usersDTO.setLocale()
            usersDTO.setInactive(values.get("inactive"));
            usersDTO.setSessionLimit(values.get("sessionLimit"));
            
            var lockCallback = new Mojo.ClientRequest({
              onSuccess : function() {
                tq.next();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            });
            
            usersDTO.lock(lockCallback);
          }
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            var applyCallback = new Mojo.ClientRequest({
              onSuccess : function() {
                console.log("UserDTO apply Success!");
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            });
            
            usersDTO.apply(applyCallback);
          }
        }));
        
        tq.start();
        
        return false;
      },
      
      _onNewRowEvent : function(newRowEvent) {
        var row = newRowEvent.getRow();
        
        row.addEventListener("dblclick", Mojo.Util.bind(this, this._onEditUser));
      },
      
      _localeFormatter : function(userDTO) {
        return String(userDTO.getLocale());
      },
      
      render : function(parent) {
        
        var ds = new InstanceQueryDataSource({
          className: queryType,
          columns: [
            {header: "User Name", queryAttr: "username"},
            {header: "Session Limit", queryAttr: "sessionLimit"},
            {header: "Inactive", queryAttr: "inactive"},
            {header: "Locales", customFormatter: this._localeFormatter}
          ]
        });
        
        this._table = this.getFactory().newDataTable({
          el : this,
          dataSource : ds
        });
        
        this._table.addNewRowEventListener(Mojo.Util.bind(this, this._onNewRowEvent));
        
        this._table.render(parent);
        
      }
      
    }
    
  });
  
  return usersTable;
  
})();
