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

//define(["../../../ClassFramework", "../../../Util", "../../factory/generic/datatable/datasource/ServerDataSource"], function(ClassFramework, Util, ServerDataSource) {
(function(){  

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var ServerDataSource = com.runwaysdk.ui.factory.generic.datatable.datasource.ServerDataSource;
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  var STRUCT = ClassFramework.alias(Mojo.STRUCTURE_PACKAGE + "*");
  
  /**
   * @class com.runwaysdk.ui.datatable.InstanceQueryDataSource A data source for data tables that reads instance data from a query dto.
   * 
   * @constructs
   * @param obj
   *   @param com.runwaysdk.business.BusinessQueryDTO queryDTO The query dto to display data from.
   */
  var queryDataSource = ClassFramework.newClass('com.runwaysdk.ui.datatable.datasource.InstanceQueryDataSource', {
    
    Extends : ServerDataSource,
    
    Instance : {
      initialize : function(config) {
        this.$initialize(config);
        
        Util.requireParameter("className [QueryDataSource]", config.className);
        Util.requireParameter("columns [QueryDataSource]", config.columns);
        
        this._config = config;
        
        var colArr = [];
          
        for (var i = 0; i < config.columns.length; ++i) {
          if (config.columns[i].header == null) {
            throw new com.runwaysdk.Exception("[QueryDataSource] Configuration error, all column objects must provide a header.");
          }
          
          colArr.push(config.columns[i].header);
        }
        
        this.setColumns(colArr);
        
        this._type = config.className;
        this._metadataQueryDTO = null;
        this._resultsQueryDTO = null;
        this._requestEvent = null;
        this._attributeNames = [];
      },
      
      getResultsQueryDTO : function() {
        return this._resultsQueryDTO;
      },
      
      // OVERRIDE
      reset : function() {
        this.$reset();
        
        if(this._resultsQueryDTO !== null)
        {
          this._resultsQueryDTO.clearOrderByList();
        }
      },
      
      // OVERRIDE
      performRequest: function(callback) {
        
        var thisDS = this;
        
        this._taskQueue = new STRUCT.TaskQueue();
        
        // 1. Fetch the class if it's not loaded
        // FIXME needs to be able to load subclasses
        if(!ClassFramework.classExists(this._type)) {
          throw new com.runwaysdk.Exception('Operation to lazy load DataSource type ['+this._type+'] is not supported.');
          /*
          this._taskQueue.addTask(new STRUCT.TaskIF({
            start : function(){
              thisDS._loadClass();      
            }
          }));
          */        
        }
        
        // 2. Fetch the QueryDTO with metadata for the DataSchema
        if(this._metadataQueryDTO == null) {
          this._taskQueue.addTask(new STRUCT.TaskIF({
            start : function(){
              thisDS._getQueryDTO();
            }
          }));
        }
        
        // 3. Perform the query and get a result set
        this._taskQueue.addTask(new STRUCT.TaskIF({
          start : function(){
            thisDS._performQuery();
          }
        }));
        
        // 4. Finalize the request and invoke the callback
        this._taskQueue.addTask(new STRUCT.TaskIF({
          start : function(){
            var json = thisDS._finalizeRequest();
            
            callback(json);
          }
        }));
        
        this._taskQueue.start();
      },

      _getQueryDTO : function(callback) {
        var thisDS = this;
        
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function(queryDTO) {
            thisDS._metadataQueryDTO = queryDTO;
            
            if (thisDS._taskQueue.isProcessing() && !thisDS._taskQueue.isStopped() && thisDS._taskQueue.hasNext()) {
              thisDS._taskQueue.next(); // invokes _performQuery
            }
            else if (callback != null) {
              callback();
            }
          },
          onFailure : function(e){
            thisDS.handleException(e);
          }
        });
        
        com.runwaysdk.Facade.getQuery(clientRequest, this._type);
      },
      
      _performQuery : function() {
      
        var thisDS = this;
        
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function(queryDTO){
            thisDS._resultsQueryDTO = queryDTO;
            thisDS._taskQueue.next();
          },
          onFailure : function(e){
            thisDS.handleException(e);
          }
        });
        
        // setup the query parameters
        this._metadataQueryDTO.setCountEnabled(true);
        this._metadataQueryDTO.setPageSize(this._pageSize);
        this._metadataQueryDTO.setPageNumber(this._pageNumber);
        
        
        if(Mojo.Util.isString(this._sortAttribute)){
          this._metadataQueryDTO.clearOrderByList();
          var order = this._ascending ? 'asc' : 'desc';
          this._metadataQueryDTO.addOrderBy(this._sortAttribute, order);
        }
        
        com.runwaysdk.Facade.queryEntities(clientRequest, this._metadataQueryDTO);
      },
      
      _finalizeRequest : function() {
        
        var thisDS = this;
        
        // convert each DTO into an object literal
        var json = [];
        var resultSet = this._resultsQueryDTO.getResultSet();   
        for(var i=0; i<resultSet.length; i++)
        {
          var result = resultSet[i];
          
          var obj = [];
          
          for(var j = 0; j < thisDS._config.columns.length; j++) {
            var name = thisDS._config.columns[j].queryAttr;
            var customFormatter = thisDS._config.columns[j].customFormatter;
            
            var value = "";
            if (name != null) {
              
              if (name === "displayLabel") {
                value = result.getDisplayLabel().getLocalizedValue();
              }
              else {
                value = result.getAttributeDTO(name).getValue();
              }
            }
            else if (customFormatter != null) {
              value = customFormatter(result);
            }
            
            value = value != null ? value : '';
            obj.push(value);
          }
          
          json.push(obj);
        }
        
        this.setTotalResults(this._resultsQueryDTO.getCount());
        
        return json;
      },
      
      _loadClass : function() {
        
        var thisDS = this;
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function(){
            thisDS._taskQueue.next();
          },
          onFailure : function(e){
            thisDS.handleException(e);
          }
        });
        
        com.runwaysdk.Facade.importTypes(clientRequest, [this._type], {autoEval : true});
      }
    }
  });
  
  return queryDataSource;
})();