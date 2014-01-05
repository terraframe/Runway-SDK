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

define(["../../../ClassFramework", "jquery", "jquery-datatables", "../runway/widget/Widget", "./Factory"], function(ClassFramework) {
  
  var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
  var UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");
  
  var DataTable = ClassFramework.newClass(Mojo.JQUERY_PACKAGE+'DataTable', {
    
    Extends : RW.Widget,
    
    Instance : {
      
      initialize : function(config)
      {
        config = config || {};
        config.el = config.el || "table";
        
        this.$initialize(config.el);
        
        this._config = config;
        this._rows = [];
      },
      
      acceptArray : function(columns, array) {
        this._config["aoColumns"] = [];
        for (var i = 0; i < columns.length; ++i) {
          this._config.aoColumns.push({"sTitle" : columns[i]});
        }
        
        this._config.aaData = array;
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
      
      render : function(parent) {
        this.$render(parent);
        
        this._impl = $(this.getRawEl()).dataTable(this._config);
      }
      
    }
  
  });
  
});