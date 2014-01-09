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

define(["../../../../../ClassFramework", "../../../../../Util", "../../../runway/datatable/datasource/ArrayDataSource"], function(ClassFramework, Util, ArrayDataSource) {
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  
  var arrayDataSource = ClassFramework.newClass(Mojo.JQUERY_PACKAGE+'datatable.datasource.ArrayDataSource', {
    
    Extends : ArrayDataSource,
    
    Instance : {
      
      initialize : function(cfg)
      {
        this.$initialize(cfg);
      },
      
      getConfig : function() {
        var config = {};
        
        config["aoColumns"] = [];
        for (var i = 0; i < this._columns.length; ++i) {
          config.aoColumns.push({"sTitle" : this._columns[i]});
        }
        
        config.aaData = this._data;
        
        return config;
      },
      
      getColumns : function() {
        return this._columns;
      },
      
      getData : function() {
        return this._data;
      }
      
    }
  });
  
  return arrayDataSource;
  
});
