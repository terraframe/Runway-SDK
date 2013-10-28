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

(function(){

var TestFramework = com.runwaysdk.test.TestFramework;
var SUITE_NAME = "RunwaySDK_JQuery";
var RUNWAY_UI;

TestFramework.newSuite(SUITE_NAME);

TestFramework.defineSuiteSetUp(SUITE_NAME, function ()
{
  RUNWAY_JQUERY = Mojo.Meta.alias("com.runwaysdk.jquery.*");
  RUNWAY_UI = Mojo.Meta.alias("com.runwaysdk.ui.*");
});

TestFramework.defineSuiteTearDown(SUITE_NAME, function ()
{
  
});

TestFramework.newTestCase(SUITE_NAME, {
   
  name: "WidgetTests",
  
  caseSetUp : function() {
    
  },
  
  caseTearDown : function() {
    
  },
  
  testTree : function() {
    var termA = new com.runwaysdk.business.Term({label : "termA"});
    var termB = new com.runwaysdk.business.Term({label : "termB"});
    var termC = new com.runwaysdk.business.Term({label : "termC"});
    
    RUNWAY_UI.Manager.setFactory("Runway");
    var factory = RUNWAY_UI.Manager.getFactory();
    
    var dialog = factory.newDialog("K00L Dialog");
    dialog.appendChild("JQ Tree");
    dialog.getContentDiv().setId("dialogTree");
    
    var tree = new com.runwaysdk.ui.jquery.Tree();
    tree.addChild(termA);
  }
  
});

});