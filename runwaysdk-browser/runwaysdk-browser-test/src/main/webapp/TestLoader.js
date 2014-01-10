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

require.config({
  baseUrl: "",
  shim : {
    "jquery-ui": {
      deps: ["jquery"]
    },
    "jquery-datatables" : {
      deps: ["jquery", "jquery-ui"]
    },
    "jquery-tree" : {
      deps: ["jquery", "jquery-ui"]
    }
  },
  paths: {
    jquery: "webjars/jquery/2.0.3/jquery",
    "jquery-ui": "webjars/jquery-ui/1.10.3/ui/jquery-ui",
    "jquery-tree": "jquerytree/tree.jquery",
    "jquery-datatables": "webjars/datatables/1.9.4/media/js/jquery.dataTables"
  }
});
require([
         "./com/runwaysdk/ui/ontology/termtree/TermTree",
         "./com/runwaysdk/ui/datatable/datasource/InstanceQueryDataSource",
         
         "./com/runwaysdk/ui/factory/jquery/datatable/DataTable",
         "./com/runwaysdk/ui/factory/jquery/Dialog",
         
         "./com/runwaysdk/ui/factory/runway/dialog/Dialog",
         "./com/runwaysdk/ui/factory/runway/datatable/DataTable",
         "./com/runwaysdk/ui/factory/runway/form/Form",
         "./com/runwaysdk/ui/factory/runway/list/List",
         
         "./com/runwaysdk/ui/factory/yui3/yui3",
         
         "./tests/Test_RunwaySDK_Core",
         "./tests/Test_RunwaySDK_DTO",
         "./tests/Test_RunwaySDK_JQuery",
         "./tests/Test_RunwaySDK_UI"
         ], function() {
  
});