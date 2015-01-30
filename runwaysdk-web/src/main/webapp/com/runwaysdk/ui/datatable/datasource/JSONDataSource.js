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
   * @class com.runwaysdk.ui.datatable.datasource.JSONDataSource This data source reads a JSON format from the server and pipes it into the data table. Like most data sources,
   *   a columns configuration object is required. The JSON returned from the server is assumed to be wrapped inside of a JSONReturnValue.
   *   
   *   The data portion of this JSON returned from the server is simply a 2 dimensional array, as an example:
   *     [
   *       [ "sun", "red" ]
   *       [ "jeans, "blue ]
   *       [ "paper", "white" ]
   *       [ "dirt", "brown" ]
   *     ]
   *     
   *     In this example, column 1 is the name of the object and column 2 is its color.
   *     
   *     Note: Be careful when using this because it may be tough to integrate into Runway's permissions model.
   * 
   * @constructs
   * @param obj
   *   @param com.runwaysdk.business.BusinessQueryDTO queryDTO The query dto to display data from.
   */
  var methodDataSource = ClassFramework.newClass('com.runwaysdk.ui.datatable.datasource.JSONDataSource', {
    
    Extends : ServerDataSource,
    
    Instance : {
      initialize : function(config) {
        
        this.$initialize(config); // Supering is important
        
        this._config = config;
      },
      // OVERRIDE
      initialSetup : function(callback) {
        
        this.$initialSetup(); // Supering is important
        
        this._doQuery(callback);
      },
      // OVERRIDE
      performRequest: function(callback) {
        this._doQuery(callback);
      },
      
      getSortAttr : function() {
        return this._config.columns[this.getSortColumn()].queryAttr;
      },
      
      _doQuery : function(callback) {
        var that = this;
        
        this._config.method.call(this, new Mojo.ClientRequest({
          onSuccess : function(view) {
            callback.onSuccess(that._onQuerySuccess(view));
          },
          onFailure : function(ex) {
            callback.onFailure(ex);
          }
        }));
      },
      
      _onQuerySuccess : function(jsonReturnValue) {
        var obj = Mojo.Util.toObject(jsonReturnValue);
        
        var resultSet = obj.returnValue;
        
        var retVal = [];
        var colArr = [];
        
        // calculate column headers
        if (this._config.columns != null)
        {
          // from the configuration
          for (var i=0; i < this._config.columns.length; ++i) {
            var header = this._config.columns[i].header;
            var queryAttr = this._config.columns[i].queryAttr;
            
            if (header != null) {
              colArr.push(this._config.columns[i].header);
            }
            else {
              var ex = new com.runwaysdk.Exception("Configuration error, all column objects must provide either a header or a queryAttr or both.");
              callback.onFailure(ex);
              return;
            }
          }
        }
        else
        {
          throw new com.runwaysdk.Exception("Configuration parameter 'columns' is required.");
        }
        this.setColumns(colArr);

        this.setTotalResults(resultSet.length);
        
        return resultSet;
      }
    }
  });
  
  return methodDataSource;
})();
