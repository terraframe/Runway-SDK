/*
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
define(["./TestFramework"], function(TestFramework){

var SUITE_NAME = "RunwaySDK_JQuery";
var RUNWAY_UI;



var TIMEOUT = 5000; // standard timeout of five seconds, which is plenty of time for even complex requests

//global reference (for relationship testing)
//These objects are set within the javascript, not
//in the JSP.
var g_taskQueue = null;
var struct = null;
var CallbackHandler = null;
var CORE = null;
var JSTEST = null;
var DTO = null;

TestFramework.newSuite(SUITE_NAME);

TestFramework.defineSuiteSetUp(SUITE_NAME, function ()
{
  RUNWAY_JQUERY = Mojo.Meta.alias("com.runwaysdk.jquery.*");
  RUNWAY_UI = Mojo.Meta.alias("com.runwaysdk.ui.*");
  
  CORE = Mojo.Meta.alias('com.runwaysdk.*');
  DTO = Mojo.Meta.alias('com.runwaysdk.business.*');
  JSTEST = Mojo.Meta.alias('com.runwaysdk.jstest.*');
  
});

TestFramework.defineSuiteTearDown(SUITE_NAME, function ()
{
  
});

TestFramework.newTestCase(SUITE_NAME, {
   
  name: "WidgetTests",
  
  caseSetUp : function() {
	  RUNWAY_UI.Manager.setFactory("YUI3");
    this.factory = RUNWAY_UI.Manager.getFactory();
  },
  
  caseTearDown : function() {
    
  },
  
});

});