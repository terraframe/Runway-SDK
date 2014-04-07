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
  
  var runwayFormName = "com.runwaysdk.ui.RunwayControllerForm";
  
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
   * This class creates a form with localizable submit/cancel buttons and requests the fields from the server
   * for the given type as well as an automatic submit behavior that invokes a controller action.
   */
  var runwayForm = ClassFramework.newClass(runwayFormName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(config) {
        
        this.requireParameter("type", config.type, "string");
        this.requireParameter("action", config.action, "string");
        this.requireParameter("onSuccess", config.onSuccess, "function");
        this.requireParameter("onFailure", config.onFailure, "function");
        config.onCancel = config.onCancel || function(){};
        config.viewParams = config.viewParams || {};
        config.actionParams = config.actionParams || {};
        this._config = config;
        
        if (config.action === "update") {
          this.requireParameter("id", config.id, "string");
          config.viewParams.id = config.id;
        }
//        else if (config.viewAction != null) {
//          
//        }
        
        this.$initialize(config.el || "div");
        
        var that = this;
//        this._viewRequest = new com.runwaysdk.geodashboard.BlockingClientRequest({onSuccess: Util.bind(this, this._onViewSuccess), onFailure: Util.bind(this, this._onFailure)});
//        this._request = new com.runwaysdk.geodashboard.BlockingClientRequest({onSuccess: Util.bind(this, this._onSuccess), onFailure: Util.bind(this, this._onFailure)});
      },
      
      getTitle : function() {
        var newType = eval("new " + this._config.type + "()");
        var label = newType.getMd().getDisplayLabel();
        
        return this.localize(this._config.action) + " " + label;
      },
      
      _onSuccess : function(retval) {
        // TODO : We should be reading the response type here.
        
        // Instead, check for response text included in a JSONReturnObject
        if (retval.indexOf("information") !== -1 && retval.indexOf("returnValue") !== -1) {
          var jsonReturnObj = Mojo.Util.getObject(retval);
          
          this._onActionSuccess(com.runwaysdk.DTOUtil.convertToType(Mojo.Util.getObject(jsonReturnObj.returnValue)));
        }
        else {
          this._onViewSuccess(retval);
        }
      },
      
      _onViewSuccess : function(html) {
        this.setInnerHTML(Mojo.Util.removeScripts(html));
        this._appendButtons();
        
        if (this._config.onViewSuccess != null) {
          this._config.onViewSuccess(html);
        }
        
        // FIXME: This is kinda dumb but I'm not sure how else to get JCF to do its thing.
        if (jcf != null && jcf.customForms != null) {
          jcf.customForms.replaceAll();
        }
      },
      
      _onActionSuccess : function(type) {
        this._config.onSuccess(type);
      },
      
      _appendButtons : function() {
        var fac = this.getFactory();
        
        var submit = fac.newButton(this.localize("submit"), Util.bind(this, this._onClickSubmit));
        this.appendChild(submit);
        
        var cancel = fac.newButton(this.localize("cancel"), Util.bind(this, this._onClickCancel));
        this.appendChild(cancel);
      },
      
      _onClickSubmit : function() {
        var that = this;
        
        if (this._config.onClickSubmit != null) {
          this._config.onClickSubmit();
        }
        
        var params = Mojo.Util.collectFormValues(this.getChildren()[0].getRawEl());
        Util.merge(this._config.actionParams, params);
        
        var request = new com.runwaysdk.geodashboard.StandbyClientRequest({onSuccess: Util.bind(this, this._onSuccess), onFailure: Util.bind(this, this._onFailure)}, this._dialog);
        
        Util.invokeControllerAction(this._config.type, this._config.action, params, request);
      },
      
      _onClickCancel : function() {
        this._config.onCancel.apply(this, arguments);
      },
      
      _onFailure : function(e) {
        this._config.onFailure(e);
      },
      
      _cancelListener : function() {
        this._config.onCancel.apply(this, arguments);
      },

      _deleteListener : function() {
        
      },
      
      getHtmlFromController : function() {
        
        this._viewRequest = new com.runwaysdk.geodashboard.StandbyClientRequest({onSuccess: Util.bind(this, this._onViewSuccess), onFailure: Util.bind(this, this._onFailure)}, this);
        
        // default = viewCreate, viewUpdate, etc.
        var viewAction = this._config.viewAction == null ? "view" + this._config.action.charAt(0).toUpperCase() + this._config.action.slice(1) : this._config.viewAction;
        
        Util.invokeControllerAction(this._config.type, viewAction, this._config.viewParams, this._viewRequest);
        
      },
      
      render : function(parent) {
        
        this.$render(parent);
        
        this.getHtmlFromController();
        
      }

    }
  });
  
  return runwayForm;
  
})();