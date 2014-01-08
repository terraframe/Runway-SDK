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

define(["../../../../../ClassFramework", "../../../../../Util"], function(ClassFramework, Util) {
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  var STRUCT = ClassFramework.alias(Mojo.STRUCTURE_PACKAGE + "*");
  
  /**
   * @class com.runwaysdk.ui.datatable.QueryDataSource A data source for data tables that reads data from a query dto.
   * 
   * @constructs
   * @param obj
   *   @param com.runwaysdk.business.BusinessQueryDTO queryDTO The query dto to display data from.
   */
  var queryDataSource = ClassFramework.newClass('com.runwaysdk.ui.datatable.QueryDataSource', {
    Instance : {
      initialize : function(config) {
        Util.requireParameter("className [QueryDataSource]", config.className);
        
        this._type = config.className;
        this._taskQueue = new STRUCT.TaskQueue();
        this._metadataQueryDTO = null;
        this._resultsQueryDTO = null;
        this._requestEvent = null;
        this._attributeNames = [];
        
        // query attributes
        this._pageNumber = 1;
        this._pageSize = 20;
        this._sortAttribute = null;
        this._ascending = true;
        
        _getQueryDTO();
      },
      
      getColumns : function(hisCallback) {
        
      },
      
      getData : function(hisCallback) {
        this._defRequestFn();
      },
      
      resetQuery : function(){
        this._pageNumber = 1;
        this._pageSize = 20;
        this._sortAttribute = null;
        this._ascending = true;
        if(this._resultsQueryDTO !== null)
        {
          this._resultsQueryDTO.clearOrderByList();
        }
      },
      
      setPageNumber : function(pageNumber){
        this._pageNumber = pageNumber;
      },
      
      setPageSize : function(pageSize){
        this._pageSize = pageSize;
      },
      
      getSortAttribute : function(){
        return this._sortAttribute;
      },
      
      setSortAttribute : function(sortAttribute){
        // TODO accept multiple attributes for priority sorting
        this._sortAttribute = sortAttribute;
      },
      
      toggleAscending : function(){
        this.setAscending(!this.isAscending());
      },
      
      setAscending : function(ascending){
        this._ascending = ascending;
      },
      
      isAscending : function(){
        return this._ascending;
      },
      
      metadataLoaded : function(){
        return this._metadataLoaded;
      },

      _getQueryDTO : function(hisCallback) {
        var thisDS = this;
        
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function(queryDTO) {
            
            // The initial value of the results QueryDTO is the
            // metadata object because we have to start with something
            // and the metadata QueryDTO works as a template.
            thisDS._metadataQueryDTO = queryDTO;
            thisDS._resultsQueryDTO = queryDTO;
            
            // notify any listeners that we have some metadata available
            var attrs = [];
            thisDS._attributeNames = [];
            var names = queryDTO.getAttributeNames();
            var RefDTO = com.runwaysdk.transport.attributes.AttributeReferenceDTO;
            var StructDTO = com.runwaysdk.transport.attributes.AttributeStructDTO;
            for(var i=0; i<names.length; i++)
            {
              var name = names[i];
              var attrDTO = queryDTO.getAttributeDTO(name);
              
              // TODO Custom filter needed. Should only allow primitives based on IF
              if(!(attrDTO instanceof RefDTO) && !(attrDTO instanceof StructDTO)
                && !attrDTO.getAttributeMdDTO().isSystem() && name !== 'keyName'
                && attrDTO.isReadable()){
                  thisDS._attributeNames.push(name);
                  attrs.push(attrDTO);
              }
            }
            
            // TODO publish this event more formally
            var metadata = {
              attributes : attrs
            };
            thisDS._metadataLoaded = true;
            
            thisDS._taskQueue.next(); // invokes _getResultSet
          },
          onFailure : function(e){
            thisDS._requestEvent.error = e;
            hisCallback.onFailure(e);
          }
        });
        
        Facade.getQuery(clientRequest, this._type);
      },
      
      _getResultSet : function(hisCallback) {
      
        var thisDS = this;
        
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function(queryDTO){
            thisDS._resultsQueryDTO = queryDTO;
            thisDS._finalizeRequest();
          },
          onFailure : function(e){
            thisDS._requestEvent.error = e;
            hisCallback.onFailure(e);
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
        
//        this._containingWidget.dispatchEvent(new PreLoadEvent(this._resultsQueryDTO));      
        
        Facade.queryEntities(clientRequest, this._resultsQueryDTO);
      },
      
      _finalizeRequest : function(){
        
        // convert each DTO into an object literal
        var json = [];
        var resultSet = this._resultsQueryDTO.getResultSet();   
        for(var i=0; i<resultSet.length; i++)
        {
          var result = resultSet[i];
           // always include the id and type for dereferencing
          var obj = {
            id:result.getId(),
            type:result.getType()
          };
          for(var j=0; j<this._attributeNames.length; j++){
            var name = this._attributeNames[j];
            var value = result.getAttributeDTO(name).getValue();
            value = value !== null ? value : '';
            obj[name] = value;
          }
          
          json.push(obj);
        }

//        this.fire("data", Y.mix({data:json}, this._requestEvent));
        
        // FIXME pass in as extra params instead of mixed in
        var pagination = {
          pageNumber : this._pageNumber,
          pageSize : this._pageSize,
          count : this._resultsQueryDTO.getCount()
        }
        this.fire('runwaysdk:pagination', pagination);
      },
      
      _loadClass : function(){
        
        var thisDS = this;
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function(){
            thisDS._taskQueue.next();
          },
          onFailure : function(e){
            thisDS._requestEvent.error = e;
            thisDS.fire("data", thisDS._requestEvent);        
          }
        });
        
        Facade.importTypes(clientRequest, [this._type], {autoEval : true});
      },

      _defRequestFn: function(evt) {
        
        this._requestEvent = evt;
        var thisDS = this;
        
        // 1. Fetch the class if it's not loaded
        // FIXME needs to be able to load subclasses
        if(!ClassFramework.classExists(this._type)){
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
        if(!this.metadataLoaded()){
          this._taskQueue.addTask(new STRUCT.TaskIF({
            start : function(){
              thisDS._getQueryDTO();      
            }
          }));
        }
        
        // 3. Perform the query and get a result set
        this._taskQueue.addTask(new STRUCT.TaskIF({
          start : function(){
            thisDS._getResultSet();
          }
        }));
        
        this._taskQueue.start();
        
        return this._requestEvent.tId; // transaction id
      }
    }
  });
  
  return queryDataSource;
});