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

//define(["../../../../../ClassFramework", "../../../../../Util", "./DataSourceFactory", "./DataSourceIF", "./Events"], function(ClassFramework, Util, DataSourceFactory, DataSourceIF, Events) {
(function(){  

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var DataSourceFactory = com.runwaysdk.ui.factory.generic.datatable.datasource.DataSourceFactory;
  var DataSourceIF = com.runwaysdk.ui.factory.generic.datatable.datasource.DataSourceIF;
  var Events = Mojo.Meta.alias("com.runwaysdk.ui.factory.generic.datatable.datasource.events.*");
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  
  var baseServerDataSource = ClassFramework.newClass('com.runwaysdk.ui.factory.generic.datatable.datasource.BaseServerDataSource', {
    
    IsAbstract : true,
    
    Implements : DataSourceIF,
    
    Instance : {
      
      initialize : function(cfg)
      {
        this._columns = cfg.columns;
        
        this._pageNumber = 1;
        this._pageSize = 20;
        this._totalResults = 0;
        this._sortAttribute = null;
        this._ascending = true;
        
        this._performRequestListeners = [];
        this._setPageNumListeners = [];
        this._setPageSizeListeners = [];
      },
      
      getData : function(callback) {
        var that = this;
        var myCallback = function(response) {
          response = that.formatResponse(response);
          callback(response);
        }
        this.performRequest(myCallback);
      },
      
      formatResponse : function(response) {
        this.dispatchEvent(new Events.FormatResponseEvent(response));
        return response;
      },
      
      performRequest : function(callback) {
        this.dispatchEvent(new Events.PerformRequestEvent(callback));
      },
      
      addFormatResponseEventListener : function(fnListener) {
        this.addEventListener(Events.FormatResponseEvent, {handleEvent: fnListener});
      },
      
      addPerformRequestEventListener : function(fnListener) {
        this.addEventListener(Events.PerformRequestEvent, {handleEvent: fnListener});
      },
      
      addSetPageNumberEventListener : function(fnListener) {
        this.addEventListener(Events.SetPageNumberEvent, {handleEvent: fnListener});
      },
      
      addSetPageSizeEventListener : function(fnListener) {
        this.addEventListener(Events.SetPageSizeEvent, {handleEvent: fnListener});
      },
      
      reset : function() {
        this._pageNumber = 1;
        this._pageSize = 20;
        this._sortAttribute = null;
        this._ascending = true;
      },
      
      setTotalResults : function(iTotal) {
        this._totalResults = iTotal;
      },
      
      getTotalResults : function() {
        return this._totalResults;
      },
  
      setPageNumber : function(pageNumber) {
        this._pageNumber = pageNumber;
        
        this.dispatchEvent(new Events.SetPageNumberEvent(pageNumber));
      },
      
      setPageSize : function(pageSize) {
        this._pageSize = pageSize;
        
        this.dispatchEvent(new Events.SetPageSizeEvent(pageSize));
      },
      
      getSortAttribute : function() {
        return this._sortAttribute;
      },
      
      setSortAttribute : function(sortAttribute) {
        // TODO accept multiple attributes for priority sorting
        this._sortAttribute = sortAttribute;
      },
      
      toggleAscending : function() {
        this.setAscending(!this.isAscending());
      },
      
      setAscending : function(ascending){
        this._ascending = ascending;
      },
      
      isAscending : function(){
        return this._ascending;
      },
      
      getColumns : function(callback) {
        if (callback != null) {
          callback(this._columns);
        }
        else {
          return this._columns;
        }
      },
      
      setColumns : function(cols) {
        this._columns = cols;
      },
      
      // TODO : we need a common base class to put this on, one below widget, for things like this that have no display.
      handleException : function(ex, throwIt) {
        var dialog = this.getFactory().newDialog("Error", {modal: true});
        dialog.appendContent(ex.getLocalizedMessage() || ex.getMessage() || ex.getDeveloperMessage());
        dialog.addButton("Ok", function(){dialog.close();});
        dialog.render();
        
        if (throwIt) {
          throw ex;
        }
      }
      
    }
  });
  
  return baseServerDataSource;
  
})();
