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

define(["../../../ClassFramework", "../../../Util", "../../factory/generic/datatable/datasource/ServerDataSource"], function(ClassFramework, Util, ServerDataSource) {
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  var STRUCT = ClassFramework.alias(Mojo.STRUCTURE_PACKAGE + "*");
  
  /**
   * @class com.runwaysdk.ui.datatable.InstanceQueryDataSource A data source for data tables that reads data from a query dto.
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
        // OPTIONAL Parameter: queryAttrs The attribute names to query. Is read intelligently from the attributes on className if not provided.
        // OPTIONAL Parameter: columns The columns to display. Set to queryAttrs if not provided.
        
        this._config = config;
        
        if (this._config.columns != null && this._config.queryAttrs == null) {
          this._config.queryAttrs = this._config.columns;
        }
        else if (this._config.queryAttrs != null && this._config.columns == null) {
          this.setColumns(this._config.queryAttrs);
        }
        else if (this._config.queryAttrs != null && this._config.columns != null && this._config.queryAttrs.length != this._config.columns.length) {
          throw new com.runwaysdk.Exception("Parameters 'queryAttrs' and 'columns' must be arrays of the same length.");
        }
        
        this._type = config.className;
        this._taskQueue = new STRUCT.TaskQueue();
        this._metadataQueryDTO = null;
        this._resultsQueryDTO = null;
        this._requestEvent = null;
        this._attributeNames = [];
        this._metadataLoaded = false;
      },
      
      // OVERRIDE
      getColumns : function(callback) {
        if (callback == null) {
          return this.$getColumns();
        }
        
        var cols = this.$getColumns();
        
        // Fetch the dto and read the columns from it if we don't know them yet.
        if (cols == null) {
          var that = this;
          var myCallback = function() {
            callback(that.$getColumns());
          }
          this._getQueryDTO(myCallback);
        }
        else {
          callback(cols);
        }
      },
      
      // OVERRIDE
      reset : function() {
        this.$reset();
        
        if(this._resultsQueryDTO !== null)
        {
          this._resultsQueryDTO.clearOrderByList();
        }
      },
      
      metadataLoaded : function(){
        return this._metadataLoaded;
      },
      
      // OVERRIDE
      performRequest: function(callback) {
        
        var thisDS = this;
        
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
        if(!this.metadataLoaded()) {
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
            
            // The initial value of the results QueryDTO is the
            // metadata object because we have to start with something
            // and the metadata QueryDTO works as a template.
            thisDS._metadataQueryDTO = queryDTO;
            thisDS._resultsQueryDTO = queryDTO;
            
            // notify any listeners that we have some metadata available
//            var attrs = [];
            var names = queryDTO.getAttributeNames();
            var RefDTO = com.runwaysdk.transport.attributes.AttributeReferenceDTO;
            var StructDTO = com.runwaysdk.transport.attributes.AttributeStructDTO;
            
            if (thisDS._config.queryAttrs == null) {
              // They didn't tell us what attrs to use, just use everything that makes sense.
              thisDS._config.queryAttrs = [];
              for(var i=0; i<names.length; i++)
              {
                var name = names[i];
                var attrDTO = queryDTO.getAttributeDTO(name);
                
                // TODO Custom filter needed. Should only allow primitives based on IF
                if(!(attrDTO instanceof RefDTO) && !(attrDTO instanceof StructDTO)
                  && !attrDTO.getAttributeMdDTO().isSystem() && name !== 'keyName'
                  && attrDTO.isReadable()) {
                    thisDS._config.queryAttrs.push(name);
//                    attrs.push(attrDTO);
                }
              }
            }
            else {
              // They told us what attributes they want. Verify that the type actually has them.
              for (var i = 0; i < thisDS._config.queryAttrs.length; ++i) {
                var name = thisDS._config.queryAttrs[i];
                
                if (queryDTO.getAttributeDTO(name) == null) {
                  throw new com.runwaysdk.Exception("The type [" + thisDS._config.className + "] does not have an attribute with the name [" + name + "].");
                }
              }
            }
            
            if (thisDS.getColumns() == null) {
              thisDS.setColumns(thisDS._config.queryAttrs);
            }
            
            thisDS._metadataLoaded = true;
            
            if (thisDS._taskQueue.isProcessing() && !thisDS._taskQueue.isStopped() && thisDS._taskQueue.hasNext()) {
              thisDS._taskQueue.next(); // invokes _getResultSet
            }
            else if (callback != null) {
              callback();
            }
          },
          onFailure : function(e){
            thisDS._handleFailure(e);
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
            thisDS._handleFailure(e);
          }
        });
        
        // setup the query parameters
        this._resultsQueryDTO.setCountEnabled(true);
        this._resultsQueryDTO.setPageSize(this._pageSize);
        this._resultsQueryDTO.setPageNumber(this._pageNumber);
        
        
        if(Mojo.Util.isString(this._sortAttribute)){
          this._resultsQueryDTO.clearOrderByList();
          var order = this._ascending ? 'asc' : 'desc';
          this._resultsQueryDTO.addOrderBy(this._sortAttribute, order);
        }
        
        com.runwaysdk.Facade.queryEntities(clientRequest, this._resultsQueryDTO);
      },
      
      _finalizeRequest : function() {
        
        // convert each DTO into an object literal
        var json = [];
        var resultSet = this._resultsQueryDTO.getResultSet();   
        for(var i=0; i<resultSet.length; i++)
        {
          var result = resultSet[i];
          
           // always include the id and type for dereferencing
//          var obj = {
//            id:result.getId(),
//            type:result.getType()
//          };
          var obj = [];
          
          for(var j=0; j< this._config.queryAttrs.length; j++) {
            var name = this._config.queryAttrs[j];
            
            var value;
            if (name === "displayLabel") {
              value = result.getDisplayLabel().getLocalizedValue();
            }
            else {
              value = result.getAttributeDTO(name).getValue();
            }
            
            value = value !== null ? value : '';
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
            thisDS._handleFailure(e);
          }
        });
        
        com.runwaysdk.Facade.importTypes(clientRequest, [this._type], {autoEval : true});
      },
      
      _handleFailure: function(e) {
        throw e;
      }
    }
  });
  
  return queryDataSource;
});