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
  
  var runwayFormName = "com.runwaysdk.ui.RunwayControllerFormDialog";
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(runwayFormName, {
    "create" : "Create",
    "update" : "Update",
    "submit" : "Submit",
    "cancel" : "Cancel"
  });
  
  /**
   * Wraps the RunwayControllerForm into a dialog.
   */
  var runwayForm = ClassFramework.newClass(runwayFormName, {
    
    Extends : com.runwaysdk.ui.RunwayControllerForm,
    
    Instance : {
      
      initialize : function(config) {
        this._config = config || {};
        this._config.width = config.width || 550;
        this._config.height = config.height || 300;
        this._config.modal = config.modal || true;
        this._config.resizable = config.resizable || false;
        this._config.buttons = config.buttons || {};
        
        var defaultConfig = {
          width: 550,
          height: 300,
          modal: true,
          resizable: false,
          buttons: [
                    { text: this.localize("submit"), click:  Util.bind(this, this._onClickSubmit)},
                    { text: this.localize("cancel"), click:  Util.bind(this, this._onClickCancel)}
                    ]
        };
        Mojo.Util.deepMerge(defaultConfig, this._config);
        
        this._dialog = this.getFactory().newDialog("", this._config);
        
        config.el = config.el || this._dialog;
        
        this.$initialize(config);
      },
      
      // @Override
      _appendButtons : function() {
        // We're adding the buttons to the dialog (not the form), so we don't use this function because it adds them everytime we get a view from the server.
      },
      
      // @Override
      _onViewSuccess : function(html) {
        if (!this._dialog.isRendered()) {
          this._dialog.render();
        }
        
        this._dialog.setTitle(this.getTitle());
        
        this.$_onViewSuccess(html);
      },
      
      // @Override
      _onActionSuccess : function(type) {
        this.$_onActionSuccess(type);
        this._dialog.destroy();
      },
      
      // @Override
      _onClickCancel : function() {
        this.$_onClickCancel();
        this._dialog.destroy();
      },
      
      render : function(parent) {
//        this._dialog.addButton(this.localize("submit"), Util.bind(this, this._onClickSubmit));
//        this._dialog.addButton(this.localize("cancel"), Util.bind(this, this._onClickCancel));
        
        this.$render(parent);
      }

    }
  });
  
  return runwayForm;
  
})();