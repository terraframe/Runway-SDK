/*
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
//define(["../../ClassFramework", "../../Util", "../RunwaySDK_UI", "../factory/runway/widget/Widget", "../datatable/datasource/InstanceQueryDataSource"], function(ClassFramework, Util, UI, Widget, InstanceQueryDataSource) {
(function(){  

  var Util = Mojo.Util;
  var ClassFramework = Mojo.Meta;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  
  var runwayFormName = "com.runwaysdk.ui.RunwayControllerFormDialogDownloader";
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(runwayFormName, {
    timeout: "Request timed out."
  });
  
  /**
   * Wraps the RunwayControllerForm into a dialog.
   */
  var runwayForm = ClassFramework.newClass(runwayFormName, {
    
    Extends : com.runwaysdk.ui.RunwayControllerFormDialog,
    
    Instance : {
      
      initialize : function(config) {
        this.$initialize(config);
        
        this._downloadTimer = null;
        this._attempts = 30;
      },
      
      /**
       * This method is invoked when the request has returned and the client is now downloading the file.
       */
      requestSuccess : function() {
        this.expireCookie( "downloadToken" );
        window.clearInterval(this._downloadTimer);
        this._standByCR._hideStandby();
        this._onActionSuccess();
      },
      
      requestFail : function(ex) {
        this.expireCookie( "downloadToken" );
        window.clearInterval(this._downloadTimer);
        this._standByCR._hideStandby();
//        this.handleException(ex);
        this._onFailure(ex);
      },
      
      getCookie : function( name ) {
        var parts = document.cookie.split(name + "=");
        if (parts.length == 2) return parts.pop().split(";").shift();
      },

      expireCookie : function( cName ) {
        document.cookie = 
          encodeURIComponent( cName ) +
          "=deleted; expires=" +
          new Date( 0 ).toUTCString();
      },
      
      // The only way with browsers to detect when the download has finished is to check for the existence of a cookie on an interval, so thats what we're doing here
      // http://stackoverflow.com/questions/1106377/detect-when-browser-receives-file-download
      _checkCookieExist : function() {
        var token = this.getCookie( "downloadToken" );
        
        if(parseInt(token) === this._downloadToken) {
          this.requestSuccess();
        }
        else if (this._attempts <= 0) {
          this.requestFail(new com.runwaysdk.Exception(this.localize("timeout")));
        }

        this._attempts--;
      },
      
      _onClickSubmit : function() {
        if (this._config.onClickSubmit != null) {
          this._config.onClickSubmit();
        }
        
//        var params = Mojo.Util.collectFormValues(this.getChildren()[0].getRawEl());
        var params = {};
        this._downloadToken = new Date().getTime();
        params.downloadToken = this._downloadToken;
        Util.merge(this._config.actionParams, params);
        
        this._standByCR = new com.runwaysdk.geodashboard.StandbyClientRequest({onSuccess: Util.bind(this, this._onSuccess), onFailure: Util.bind(this, this._onFailure)}, this._dialog);
        this._standByCR._showStandby();
        
        this._downloadTimer = window.setInterval(Mojo.Util.bind(this, this._checkCookieExist), 1000);
        
        var form = this.getFactory().newElement(document.getElementById(this._config.type + ".form.id"));
        this.appendInputsFromConfig(form, params);
        form.getRawEl().action = Mojo.ClientSession.getBaseEndpoint() + this._config.type + "Controller." + this._config.action + ".mojax"; 
        form.getRawEl().submit();
        
//        var frame = this.getFactory().newElement("iframe", {method: "post"}, {display: "none"});
//        frame.render(this);
      },
      
      appendInputsFromConfig : function(form, config) {
        var fac = this.getFactory();
        
        for (var key in config) {
          if(config.hasOwnProperty(key)){
            var input = fac.newElement("input", {type: "hidden", name: key, value: config[key]});
            form.appendChild(input);
          }
       }
      }
    }
  });
  
  return runwayForm;
  
})();