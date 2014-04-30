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
    "delete" : "Delete",
    "deleteDescribe" : "Are you sure you want to delete '${displayLabel}'?"
  });
  
  /**
   * Wraps the RunwayControllerForm into a dialog.
   */
  var runwayForm = ClassFramework.newClass(runwayFormName, {
    
    Extends : com.runwaysdk.ui.RunwayControllerForm,
    
    Instance : {
      
      initialize : function(config) {
        
        var defaultConfig = {
          width: 550,
          height: 300,
          modal: false,
          resizable: false,
          close: Util.bind(this, this._onClickCancel),
          buttons: [
                    {text: this.localize("submit"), "class": "btn btn-primary", click: Util.bind(this, this._onClickSubmit)},
                    {text: this.localize("cancel"), "class": "btn", click: Util.bind(this, this._onClickCancel)}
                    ]
        };
        if (config.action === "delete") {
          this.requireParameter("dto", config.dto, "object");
          
          var newType = eval("new " + config.type + "()");
          var termMdLabel = newType.getMd().getDisplayLabel();
          var deleteLabel = this.localize("delete") + " " + termMdLabel;
          
          defaultConfig.buttons[0] = {text: deleteLabel, "class": "btn btn-primary", click: Util.bind(this, this._onClickDelete)};
          defaultConfig.width = 485;
          defaultConfig.height = 200;
        }
        this._config = Mojo.Util.deepMerge(defaultConfig, config);
        
        this.$initialize(this._config);
        
        this._dialog = this.getFactory().newDialog("", this._config);
        if (config.action === "delete") {
          this._dialog.setTitle(deleteLabel);
          this._dialog.appendContent(this.localize("deleteDescribe").replace("${displayLabel}", this._config.dto.getDisplayLabel().getLocalizedValue()));
        }
        else {
          this._dialog.appendContent(this);
        }
      },
      
      // @Override
      _appendButtons : function() {
        // We're adding the buttons to the dialog (not the form), so we don't use this function because it adds them everytime we get a view from the server.
      },
      
      // @Override
      _onViewSuccess : function(html) {
        this._dialog.setTitle(this.getTitle());
        
//        this._dialog.show();
        
        this.$_onViewSuccess(html);
        
        // Heh, little strange here, but the this reference = the form. Since this is inside a dialog, we're setting a padding on the form.
        this.setStyle("padding", "17px 0px 17px 0px");
        //           padding goes top right bot left
      },
      
      // @Override
      _onActionSuccess : function(type) {
        this.$_onActionSuccess(type);
        this.destroy();
      },
      
      // @Override
      _onClickCancel : function() {
        this.$_onClickCancel();
        this.destroy();
      },
      
      destroy : function() {
        if (!this.isDestroyed() && !this._dialog.isDestroyed()) {
          this.$destroy();
          this._dialog.destroy();
        }
      },
      
      _onFailure : function(ex) {
        this.$_onFailure(ex);
        this.destroy();
      },
      
      _onClickDelete : function() {
        var cr = new com.runwaysdk.geodashboard.StandbyClientRequest({
          onSuccess : Util.bind(this, this._onActionSuccess),
          onFailure : Util.bind(this, this._onFailure),
        }, this._dialog);
        
        Mojo.Util.invokeControllerAction(this._config.type, "delete", {dto: this._config.dto}, cr);
      },
      
      getHtmlFromController : function() {
        
        if (this._config.action !== "delete") {
          this._viewRequest = new com.runwaysdk.geodashboard.StandbyClientRequest({onSuccess: Util.bind(this, this._onViewSuccess), onFailure: Util.bind(this, this._onFailure)}, this._dialog);
          
          // default = viewCreate, viewUpdate, etc.
          var viewAction = this._config.viewAction == null ? "view" + this._config.action.charAt(0).toUpperCase() + this._config.action.slice(1) : this._config.viewAction;
          
          Util.invokeControllerAction(this._config.type, viewAction, this._config.viewParams, this._viewRequest);
        }
      },
      
      render : function(parent) {
//        this._dialog.addButton(this.localize("submit"), Util.bind(this, this._onClickSubmit));
//        this._dialog.addButton(this.localize("cancel"), Util.bind(this, this._onClickCancel));
        
        if (!this.isRendered() && !this._dialog.isRendered()) {
          this._dialog.render(parent);
          this.$render(this._dialog);
//          this._dialog.hide();
        }
      }

    }
  });
  
  return runwayForm;
  
})();