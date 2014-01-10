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

define(["../../../../../ClassFramework", "../../../../../Util", "./DataSourceFactory", "./BaseServerDataSource"], function(ClassFramework, Util, DataSourceFactory, BaseServerDataSource) {
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  
  var serverDataSource = ClassFramework.newClass('com.runwaysdk.ui.factory.generic.datatable.datasource.ServerDataSource', {
    
    IsAbstract : true,
    
    Extends : BaseServerDataSource,
    
    Instance : {
      
      initialize : function(cfg)
      {
        this.$initialize(cfg);
        
        cfg.type = "Server";
        
        this._impl = DataSourceFactory.newDataSource(cfg);
        this._impl.addPerformRequestEventListener(Util.bind(this, this._performRequestListener));
        this._impl.addSetPageNumberEventListener(Util.bind(this, this._setPageNumListener));
        this._impl.addSetPageSizeEventListener(Util.bind(this, this._setPageSizeListener));
      },
      
      _performRequestListener : function(callback) {
        this.performRequest(callback);
      },
      
      _setPageNumListener : function(num) {
        this.setPageNumber(num);
      },
      
      _setPageSizeListener : function(size) {
        this.setPageSize(size);
      },
      
      formatResponse : function(response) {
        return this._impl.formatRepsonse();
      },
      
      getConfig : function() {
        return this._impl.getConfig();
      },
      
      getColumns : function(callback) {
        return this._impl.getColumns();
      },
      
      setColumns : function(cols) {
        this.$setColumns(cols);
        this._impl.setColumns(cols);
      },
      
      getData : function(callback) {
        return this._impl.getData();
      },
      
      setTotalResults : function(total) {
        this.$setTotalResults(total);
        this._impl.setTotalResults(total);
      }
      
    }
  });
  
  return serverDataSource;
  
});
