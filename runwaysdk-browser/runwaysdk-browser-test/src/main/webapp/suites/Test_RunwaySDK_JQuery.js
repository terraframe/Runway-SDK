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

var RELATIONSHIP_TYPE = "com.runwaysdk.jstest.business.ontology.Sequential";

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
  
  struct = Mojo.Meta.alias('com.runwaysdk.structure.*');
  g_taskQueue = new struct.TaskQueue();
  
  var localPassCB = function(){
    if (g_taskQueue.hasNext()) 
      CallbackHandler.next.apply(CallbackHandler, arguments);
    else 
      this.yuiTest.resume();
  };
  
  var localNextCB = function(){
    g_taskQueue.next.apply(g_taskQueue, arguments);
  };
  
  var localFailCB = function(e, name, thisRef) {
    var name = name || "";
    var thisRef = thisRef || this;
    var eStr = "";
    
    if (e) 
      eStr = e.message || e.description || e.getLocalizedMessage();
    
    thisRef.yuiTest.resume(function(){
      Y.Assert.fail("The " + name + " callback function was not intended to be called. \n" + eStr);
    });
  };
  
  var localFailNameCB = function(name) {
    return function(e){
      localFailCB(e, name, this);
    };
  };
  
  CallbackHandler = Mojo.Meta.newClass('com.runwaysdk.jstest.CallbackHandler', {
  
    Instance: {
      initialize: function(yuiTest, obj){
        Mojo.Util.copy(new Mojo.ClientRequest(obj), this);
        this.yuiTest = yuiTest;
      },
      
      onSuccess: localPassCB,
      onFailure: localFailNameCB("onFailure")
    },
    
    Static: {
      pass: localPassCB,
      next: localNextCB,
      fail: localFailCB,
      failName: localFailNameCB,
      failSuccess: localFailNameCB("onSuccess"),
    }
  });
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
  
  testTreeAdd : function() {
    var dialog = this.factory.newDialog("K00L Dialog");
    dialog.appendChild("JQ Tree");
    dialog.render();
    dialog.getContentEl().setId("dialogTree");
    
    var tree = new com.runwaysdk.ui.jquery.Tree({nodeId : "#dialogTree", dragDrop : true});
    tree.setRootTerm(g_idTermRoot);
    
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
    	tree.addChild(g_idTermA, g_idTermRoot, RELATIONSHIP_TYPE, new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
	  start: function(tq){
		tree.addChild(g_idTermB, g_idTermA, RELATIONSHIP_TYPE, new CallbackHandler(yuiTest));
	  }
	}));
    
    g_taskQueue.addTask(new struct.TaskIF({
  	  start: function(tq){
  		tree.addChild(g_idTermC, g_idTermB, RELATIONSHIP_TYPE, new CallbackHandler(yuiTest));
  		
  		yuiTest.resume();
  	  }
  	}));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },
  
  testTreeRemove : function() {
  var dialog = this.factory.newDialog("K00L Dialog");
    dialog.appendChild("JQ Tree");
    dialog.render();
    dialog.getContentEl().setId("dialogTree");
    
    var tree = new com.runwaysdk.ui.jquery.Tree({nodeId : "#dialogTree", dragDrop : true});
    tree.setRootTerm(g_idTermRoot);
    
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        tree.addChild(g_idTermA, g_idTermRoot, RELATIONSHIP_TYPE, new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        tree.addChild(g_idTermB, g_idTermA, RELATIONSHIP_TYPE, new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        tree.addChild(g_idTermC, g_idTermB, RELATIONSHIP_TYPE, new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        tree.removeTerm(g_idTermB, new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        tree.addChild(g_idTermC, g_idTermB, RELATIONSHIP_TYPE, new CallbackHandler(yuiTest));
    
        yuiTest.resume();
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  }
  
});

})();