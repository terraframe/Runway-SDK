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

define(["../../../../../ClassFramework", "../../../../../Util", "./DataSourceFactory", "./DataSourceIF"], function(ClassFramework, Util, DataSourceFactory, DataSourceIF) {
  
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
          callback(that.formatResponse(response));
        }
        this.performRequest(myCallback);
      },
      
      formatResponse : { IsAbstract : true },
      
      performRequest : function(callback) {
        for (var i = 0; i < this._performRequestListeners.length; ++i) {
          this._performRequestListeners[i](callback);
        }
      },
      
      addPerformRequestEventListener : function(listener) {
        this._performRequestListeners.push(listener);
      },
      
      addSetPageNumberEventListener : function(listener) {
        this._setPageNumListeners.push(listener);
      },
      
      addSetPageSizeEventListener : function(listener) {
        this._setPageSizeListeners.push(listener);
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
        
        for (var i = 0; i < this._setPageNumListeners.length; ++i) {
          this._setPageNumListeners[i](pageNumber);
        }
      },
      
      setPageSize : function(pageSize) {
        this._pageSize = pageSize;
        
        for (var i = 0; i < this._setPageSizeListeners.length; ++i) {
          this._setPageSizeListeners[i](pageSize);
        }
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
      }
      
    }
  });
  
  return baseServerDataSource;
  
});
