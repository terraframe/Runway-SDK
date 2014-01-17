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

define(["../../../../ClassFramework", "../../../../Util", "./datasource/DataSourceFactory", "./../../generic/datatable/Events", "../../generic/datatable/datasource/DataSourceIF", "../../generic/datatable/Row", "../../generic/datatable/Column", "jquery-datatables", "../../runway/widget/Widget", "../Factory"], function(ClassFramework, Util, DataSourceFactory, TableEvents, DataSourceIF, Row, Column) {
  
  var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
  var UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");
  
  $.fn.dataTableExt.oApi.fnReloadAjax = function ( oSettings, sNewSource, fnCallback, bStandingRedraw )
  {
      // DataTables 1.10 compatibility - if 1.10 then versionCheck exists.
      // 1.10s API has ajax reloading built in, so we use those abilities
      // directly.
      if ( $.fn.dataTable.versionCheck ) {
          var api = new $.fn.dataTable.Api( oSettings );
   
          if ( sNewSource ) {
              api.ajax.url( sNewSource ).load( fnCallback, !bStandingRedraw );
          }
          else {
              api.ajax.reload( fnCallback, !bStandingRedraw );
          }
          return;
      }
   
      if ( sNewSource !== undefined && sNewSource !== null ) {
          oSettings.sAjaxSource = sNewSource;
      }
   
      // Server-side processing should just call fnDraw
//      if ( oSettings.oFeatures.bServerSide ) {
//          this.fnDraw();
//          return;
//      }
   
      this.oApi._fnProcessingDisplay( oSettings, true );
      var that = this;
      var iStart = oSettings._iDisplayStart;
      var aData = [];
   
      this.oApi._fnServerParams( oSettings, aData );
   
      oSettings.fnServerData.call( oSettings.oInstance, oSettings.sAjaxSource, aData, function(json) {
          /* Clear the old information from the table */
          that.oApi._fnClearTable( oSettings );
   
          /* Got the data - add it to the table */
          var aData =  (oSettings.sAjaxDataProp !== "") ?
              that.oApi._fnGetObjectDataFn( oSettings.sAjaxDataProp )( json ) : json;
   
          for ( var i=0 ; i<aData.length ; i++ )
          {
              that.oApi._fnAddData( oSettings, aData[i] );
          }
           
          oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
   
          that.fnDraw();
   
          if ( bStandingRedraw === true )
          {
              oSettings._iDisplayStart = iStart;
              that.oApi._fnCalculateEnd( oSettings );
              that.fnDraw( false );
          }
   
          that.oApi._fnProcessingDisplay( oSettings, false );
   
          /* Callback user function - for event handlers etc */
          if ( typeof fnCallback == 'function' && fnCallback !== null )
          {
              fnCallback( oSettings );
          }
      }, oSettings );
  };
  
  var DataTable = ClassFramework.newClass(Mojo.JQUERY_PACKAGE+'datatable.DataTable', {
    
    Extends : RW.Widget,
    
    Instance : {
      
      initialize : function(config)
      {
        config = config || {};
        config.el = config.el || "table";
        
        this.$initialize(config.el);
        
        if (DataSourceIF.getMetaClass().isInstance(config.dataSource)) {
          this._dataSource = config.dataSource;
        }
        else {
          config.dataSource.dataTable = this;
          this._dataSource = DataSourceFactory.newDataSource(config.dataSource);
        }
        
        this._config = config;
        this._rows = [];
      },
      
      addRow : function(rowData) {
        if (this.isRendered()) {
          this.getImpl().fnAddData(rowData);
        }
        else {
          
        }
      },
      
      addNewRowEventListener : function(fnListener) {
        this.addEventListener(TableEvents.NewRowEvent, {handleEvent: fnListener});
      },
      
      addNewHeaderRowEventListener : function(fnListener) {
        this.addEventListener(TableEvents.NewHeaderRowEvent, {handleEvent: fnListener});
      },
      
      deleteRow : function(rowNum) {
        if (this.isRendered()) {
          this.getImpl().fnDeleteRow(rowNum);
        }
        else {
          
        }
      },
      
      getDataSource : function() {
        return this._dataSource;
      },
      
      getImpl : function() {
        return this._impl;
      },
      
      __addChildElements : function() {
        // Header
        var thead = this.getFactory().newElement("thead");
      
        var headRow = new Row({isHeader: true, parentTable: this});
        for (var i = 0; i < this._columnHeaders.length; ++i) {
          headRow.addData(this._columnHeaders[i]);
        }
        thead.appendChild(headRow);
        this.dispatchEvent(new TableEvents.NewHeaderRowEvent(headRow));
        
        this.appendChild(thead);
        
//          <tbody>
//          <tr>
//            <td colspan="5" class="dataTables_empty">Loading data from server</td>
//          </tr>
//        </tbody>
        var tbody = this.getFactory().newElement("tbody");
        this.appendChild(tbody);
        var tr = new Row({isHeader: false, parentTable: this});
        tbody.appendChild(tr);
        for (var i = 0; i < headRow.getChildren().length; ++i) {
          var td = tr.addData("Loading data from server.");
          td.addClassName("dataTables_empty");
          td.setAttribute("colspan", "5");
        }
      },
      
      refresh : function() {
        this.getImpl().fnDraw();
      },
      
      render : function(parent) {
        this.$render(parent);
        
        var that = this

        var cfg = that._dataSource.getConfig();
        var columns = this._dataSource.getColumns();
          
        Util.merge(that._config, cfg);
          
        that._columnHeaders = columns;
        that.__addChildElements();
        
        var fnRowCallback = function (tr, aData, iDisplayIndex) {
          var row = new Row({
            el: tr,
            isHeader: true,
            parentTable: that,
            rowNumber: iDisplayIndex
          });
          row.setComponents(row.getChildNodes());
          
          that.dispatchEvent(new TableEvents.NewRowEvent(row));
        };
        cfg.fnRowCallback = fnRowCallback;

        that._impl = $(that.getRawEl()).dataTable(cfg);
      }
      
    }
  
  });
  
});