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

define(["../../../../../ClassFramework", "../../../../../Util", "../../../../RunwaySDK_UI", "../../../runway/datatable/datasource/QueryDataSource"], function(ClassFramework, Util, Runway_UI, QueryDataSource) {
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  var STRUCT = ClassFramework.alias(Mojo.STRUCTURE_PACKAGE + "*");
  
  // Extend datatables.net to provide pagination info
  // http://datatables.net/plug-ins/api#fnPagingInfo
//  $.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
//  {
//      return {
//          "iStart":         oSettings._iDisplayStart,
//          "iEnd":           oSettings.fnDisplayEnd(),
//          "iLength":        oSettings._iDisplayLength,
//          "iTotal":         oSettings.fnRecordsTotal(),
//          "iFilteredTotal": oSettings.fnRecordsDisplay(),
//          "iPage":          oSettings._iDisplayLength === -1 ?
//              0 : Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
//          "iTotalPages":    oSettings._iDisplayLength === -1 ?
//              0 : Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
//      };
//  };
  
  /**
   * @class com.runwaysdk.ui.datatable.QueryDataSource A data source for data tables that reads data from a query dto.
   * 
   * @constructs
   * @param obj
   *   @param com.runwaysdk.business.BusinessQueryDTO queryDTO The query dto to display data from.
   */
  var queryDataSource = ClassFramework.newClass(Mojo.JQUERY_PACKAGE+'datatable.datasource.QueryDataSource', {
    
    Extends : QueryDataSource,
    
    Instance : {
      initialize : function(config) {
        this.$initialize(config);
      },
      
      getConfig : function() {
        return {
          "bProcessing": true,
          "bServerSide": true,
          "sAjaxSource": "",
          "fnServerData": Util.bind(this, this.performRequest),
          aoColumns: this.getColumns()
        };
      },
      
      getColumns : function() {
        return ["ID", "Type", "Display Label"];
      },
      
      // OVERRIDE
      performRequest : function(sSource, aoData, fnCallback, oSettings) {
        this.$performRequest();
        
        var displayStart;
        var displayLen;
        for (var i = 0; i < aoData.length; ++i) {
          if (aoData[i].name === "iDisplayLength") {
            displayLen = aoData[i].value;
          }
          else if (aoData[i].name === "iDisplayStart") {
            displayStart = aoData[i].value;
          }
          
          if (displayStart != null && displayLen != null) { break; }
        }
        
        this._pageNumber = displayStart / displayLen + 1;
        this._pageSize = displayLen;
        
        this._sSource = sSource;
        this._aoData = aoData;
        this._fnCallback = fnCallback;
      },
      
      // OVERRIDE
      _finalizeRequest : function() {
        // convert each DTO into an object literal
        var json = [];
        var resultSet = this._resultsQueryDTO.getResultSet();   
        for(var i=0; i<resultSet.length; i++)
        {
          var result = resultSet[i];
           // always include the id and type for dereferencing
          var row = [result.getId(), result.getType()];
          for(var j=0; j<this._attributeNames.length; j++) {
            var name = this._attributeNames[j];
            var value = result.getAttributeDTO(name).getValue();
            value = value != null ? value : '';
            row.push(value);
          }
          
          json.push(row);
        }
        
        var count = this._resultsQueryDTO.getCount();
        
        this._fnCallback({
          sEcho : this._aoData.sEcho,
          iTotalRecords: count,
          iTotalDisplayRecords: count,
          aaData: json,
          aoColumns: this.getColumns()
        });
      }
    }
  });
  
  return queryDataSource;
});