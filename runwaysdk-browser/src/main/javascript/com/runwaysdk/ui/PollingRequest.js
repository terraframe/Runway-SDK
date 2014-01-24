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

(function(){
  var pack = "com.runwaysdk.ui.";
  
  var MINIMUM_POLLING_INTERVAL = 100; // 1/10th of a second
  
  var pollingRequest = Mojo.Meta.newClass(pack+'PollingRequest', {
    
    Extends : com.runwaysdk.ui.Component,
    
    Instance : {
      initialize : function(config)
      {
        this._objCallback = config.callback;
        this._fnPerformRequest = config.performRequest;
        this._pollingInterval = config.pollingInterval || 800;
        this._retryPollingInterval = config.retryPollingInterval || 6000;
        this._numRetries = config.numRetries || 5;
        this._numSequentialFails = 0;
        this._timeoutMsg = config.timeoutMessage || "Polling request has timed out. The widget will no longer update with live server data until it is remade.";
        
        this._timeoutDialog = config.timeoutDialog || com.runwaysdk.ui.Manager.getFactory().newDialog("Polling Failed", {destroyOnExit: false});
        
        var fac = com.runwaysdk.ui.Manager.getFactory();
        this._pollingTimeoutDiv = fac.newElement("div");
        this._pollingTimeoutErrorDiv = fac.newElement("div");
        this._timeoutDialog.appendContent(this._pollingTimeoutDiv);
        this._timeoutDialog.appendContent(fac.newElement("br"));
        this._timeoutDialog.appendContent(fac.newElement("div", {innerHTML: "Error message:"}));
        this._timeoutDialog.appendContent(this._pollingTimeoutErrorDiv);
        
        this._timeoutDialog.render();
        this._timeoutDialog.hide();
        
        this._isPollingEnabled = false;
        
        this._timeSinceLastPoll = 999999;
        
        this._boundDoAfterTimeout = Mojo.Util.bind(this, this._doAfterTimeout);
      },
      
      setPollingInterval : function(num) {
        this._pollingInterval = num;
      },
      
      setRetryPollingInterval : function(num) {
        this._retryPollingInterval = num;
      },
      
      enable : function() {
        if (!this._isPollingEnabled) {
          this._isPollingEnabled = true;
          
          this.poll();
        }
      },
      
      disable : function() {
        this._isPollingEnabled = false;
      },
      
      poll : function() {
        if (this._isPolling) {
          return;
        }
        this._isPolling = true;
        
        setTimeout(this._boundDoAfterTimeout, MINIMUM_POLLING_INTERVAL);
      },
      
      _doAfterTimeout : function() {
        var that = this;
        
        if (that.isDestroyed()) {
          return;
        }
        else if (!that._isPollingEnabled) {
          that._isPolling = false;
          return;
        }
        
        var waitTime = 1000;
        if (that._numSequentialFails > 0) {
          waitTime = that._retryPollingInterval; 
        }
        else {
          waitTime = that._pollingInterval;
        }
        
        that._timeSinceLastPoll = that._timeSinceLastPoll + MINIMUM_POLLING_INTERVAL;
        
        if (that._timeSinceLastPoll >= waitTime) {
          that._timeSinceLastPoll = 0;
          
          var myCallback = {
            onSuccess: function() {
              var args = [].splice.call(arguments, 2, arguments.length);
              that._objCallback.onSuccess.apply(that, args.concat([].splice.call(arguments, 0, arguments.length)));
              
              if (that.isDestroyed()) {
                return;
              }
              
              that._numSequentialFails = 0;
              that._timeoutDialog.hide();
              
              if (that._isPollingEnabled) {
                setTimeout(that._boundDoAfterTimeout, MINIMUM_POLLING_INTERVAL);
              }
              else {
                that._isPolling = false;
                return;
              }
            },
            onFailure: function(ex) {
              that._numSequentialFails++;
              
              if (!that.isDestroyed() && that._isPollingEnabled) {
                if (that._numSequentialFails >= that._numRetries) {
                  var ex = new com.runwaysdk.Exception(that._timeoutMsg);
                  that._objCallback.onFailure(ex);
                  that._isPolling = false;
                  that.destroy();
                  return;
                }
                else {
                  that.onPollRequestFail(ex);
                  setTimeout(that._boundDoAfterTimeout, MINIMUM_POLLING_INTERVAL);
                }
              }
              else {
                that._isPolling = false;
                return;
              }
            }
          }
          
          try {
            that._fnPerformRequest(myCallback);
          }
          catch(e) {
            that._isPolling = false;
            throw e;
          }
        }
        else {
          setTimeout(that._boundDoAfterTimeout, MINIMUM_POLLING_INTERVAL);
        }
      },
      
      onPollRequestFail : function(ex) {
        // Display a dialog telling the user that a polling request failed, but we're still going to keep retrying.
        // TODO localizable
        
        if (!this._timeoutDialog.isDestroyed()) {
          var html = "A polling request has failed, but we will retry in " + (this._retryPollingInterval / 1000) + " seconds.";
          html = html + " We will retry " + (this._numRetries - this._numSequentialFails) + " more times before giving up.";
          this._pollingTimeoutDiv.setInnerHTML(html);
          
          this._pollingTimeoutErrorDiv.setInnerHTML(ex.getMessage());
          
          this._timeoutDialog.show();
        }
      },
      
      destroy : function() {
        if (!this.isDestroyed()) {
          if (!this._timeoutDialog.isDestroyed()) {
            this._timeoutDialog.destroy();
          }
          this.$destroy();
        }
      }
    }
  });
  
  return pollingRequest;
})();
