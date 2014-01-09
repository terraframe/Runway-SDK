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

define(["../../../../ClassFramework", "../../../../Util", "../../runway/datatable/datasource/DataSourceFactory", "jquery-datatables", "../../runway/widget/Widget", "../Factory", "./datasource/DataSourceFactory"], function(ClassFramework, Util, DataSourceFactory) {
  
  var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
  var UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");
  
  var DataTable = ClassFramework.newClass(Mojo.JQUERY_PACKAGE+'datatable.DataTable', {
    
    Extends : RW.Widget,
    
    Instance : {
      
      initialize : function(config)
      {
        config = config || {};
        config.el = config.el || "table";
        
        this.$initialize(config.el);
        
        config.dataSource.dataTable = this;
        this._dataSource = DataSourceFactory.newDataSource(config.dataSource);
        
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
      
      deleteRow : function(rowNum) {
        if (this.isRendered()) {
          this.getImpl().fnDeleteRow(rowNum);
        }
        else {
          
        }
      },
      
      getImpl : function() {
        return this._impl;
      },
      
      __addChildElements : function() {
        // Header
        var thead = this.getFactory().newElement("thead");
      
        var headers = this._dataSource.getColumns();
        for (var i = 0; i < headers.length; ++i) {
          var th = this.getFactory().newElement("th");
          th.setInnerHTML(headers[i]);
          thead.appendChild(th)
        }
        
        this.appendChild(thead);
        
//          <tbody>
//          <tr>
//            <td colspan="5" class="dataTables_empty">Loading data from server</td>
//          </tr>
//        </tbody>
        var tbody = this.getFactory().newElement("tbody");
        this.appendChild(tbody);
        var tr = this.getFactory().newElement("tr");
        tbody.appendChild(tr);
        var td = this.getFactory().newElement("td");
        td.addClassName("dataTables_empty");
        td.setAttribute("colspan", "5");
        td.setInnerHTML("Loading data from server.");
        tr.appendChild(td);
      },
      
      render : function(parent) {
        this.$render(parent);
        
        var cfg = this._dataSource.getConfig();
        Util.merge(this._config, cfg);
        
        this.__addChildElements();
        
        this._impl = $(this.getRawEl()).dataTable(cfg);
      }
      
    }
  
  });
  
});