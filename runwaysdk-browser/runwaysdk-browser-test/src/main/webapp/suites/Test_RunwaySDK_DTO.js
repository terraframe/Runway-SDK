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
/**
 * Runwaysdk Javascript DTO Test Suite
 * 
 * @author Terraframe
 */

(function(){

var TestFramework = com.runwaysdk.test.TestFramework;
var Y = YUI().use("*");
var SUITE_NAME = "RunwaySDK_DTO";
var TIMEOUT = 5000; // standard timeout of five seconds, which is plenty of time for even complex requests

// global reference (for relationship testing)
// These objects are set within the javascript, not
// in the JSP.
var g_parent = null;
var g_child = null;
var g_rel = null;
var g_child2 = null;
var g_rel2 = null;
var g_taskQueue = null;
var struct = null;
var CallbackHandler = null;
var CORE = null;
var JSTEST = null;
var DTO = null;

TestFramework.newSuite(SUITE_NAME);

TestFramework.defineSuiteSetUp(SUITE_NAME, function(){
  // TODO ajax call to setup domain model
  // alias the com.runwaysdk.jstest namespace to the window
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
  }
  
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
 // TODO ajax call to teardown domain model  
});

TestFramework.newTestCase(SUITE_NAME, {

  name : 'InstanceTests',
  
  /**
   * Tests creating a new instance of a class, applying it, and fetching
   * it from the server.
   */
  testPersist : function()
  {
	  Y.log("Creating a new instance of TestClass and applying.", "debug");
	
    var yuiTest = this;
  
    var handler = new CallbackHandler(yuiTest, {
      onSuccess : function(applied){
          
          var id = applied.getId();
          
          Y.log("Getting the applied instance of TestClass.", "debug");
          
          // now fetch the object
          com.runwaysdk.jstest.TestClass.get(new CallbackHandler(yuiTest, {
          
            onSuccess : function(fetched){
            
          	  Y.log("Checking the ids to look for a match.", "debug");
							
              // make sure it's the same object as before
              yuiTest.resume(function(){
                Y.Assert.isTrue(applied instanceof com.runwaysdk.jstest.TestClass);
                Y.Assert.areEqual(id, fetched.getId());
              });
            }
          }), id);
      }
    });
    
    // apply a new instance of TestClass
    var testClass = new com.runwaysdk.jstest.TestClass();
    testClass.apply(handler);
    
    yuiTest.wait(TIMEOUT);
  },
  
  /**
   * Tests creating a new instance of a class, applying it, deleting it,
   * and then fetching it from the server.
   */
  testDelete : function()
  {
		Y.log("Creating a new instance of TestClass and applying.", "debug");
		
    var yuiTest = this;
  	
  	// Creating a new instance of TestClass and applying
  	var testClass = new com.runwaysdk.jstest.TestClass();
  	
  	var cb = function(testClass) // lock callback
  	{
  		var cb2 = function(testClass2) // get instance callback
  		{
  			var cb3 = function() // delete callback
  			{
  				var id = testClass2.getId();
  				
					Y.log("Forcing error by getting a deleted instance.", "debug");
					
  				com.runwaysdk.jstest.TestClass.get(new CallbackHandler(yuiTest, {onSuccess: CallbackHandler.fail , onFailure : CallbackHandler.pass}), id);
  			};
  				
				Y.log("Deleting the instance.", "debug");
  			var handler = new CallbackHandler(yuiTest, {
  				onSuccess : cb3
  			});		
  			testClass.remove(handler);
  		};
  			
			Y.log("Locking the instance.", "debug");
  		var handler = new CallbackHandler(yuiTest, {
  			onSuccess : cb2
  		});	
  		testClass.lock(handler);
  	};
  		
  	var handler = new CallbackHandler(yuiTest, {
  		onSuccess : cb
  	});
  	testClass.apply(handler);
  		
  	yuiTest.wait(TIMEOUT);
  },
	
  /**
   * Tests instanceof and aliases
   */
	testAlias : function ()
  {
    var testClass = new com.runwaysdk.jstest.TestClass();
    if(testClass instanceof com.runwaysdk.jstest.TestClass &&
      testClass instanceof DTO.BusinessDTO &&
      testClass instanceof DTO.ElementDTO &&
      testClass instanceof DTO.EntityDTO)
    {
			// Success
    }
    else
    {
			Y.Assert.fail("Testing instanceof on the new TestClass (before creating an alias) failed");
    }
      
		// Aliasing com.runwaysdk.jstest.TestClass as TestClass
    var TestClass = com.runwaysdk.jstest.TestClass;
    
    var testClass2 = new TestClass();
    if(testClass2 instanceof com.runwaysdk.jstest.TestClass &&
      testClass2 instanceof TestClass &&  
      testClass2 instanceof DTO.BusinessDTO &&
      testClass2 instanceof DTO.ElementDTO &&
      testClass2 instanceof DTO.EntityDTO)
    {
		  // Success
    }
    else
    {
		  Y.Assert.fail("Testing instanceof on an alias failed.");
    }
    
    // make sure the original namespace is preserved
    if(testClass2 instanceof com.runwaysdk.jstest.TestClass)
    {
			// Success
    }
    else
    {
      Y.Assert.fail("Final alias test failed.");
    }
    
  },
  
  /**
   * Check the default values for a new instance
   */
  testDefaultValues: function()
  {
    var testClass = new com.runwaysdk.jstest.TestClass();
    
    Y.Assert.areEqual("Yo diggity", testClass.getTestCharacter(), "testClass.getTestCharacter() did not return the expected value.");
    Y.Assert.areEqual("", testClass.getTestText(), "testClass.getTestText() did not return the expected value.");
    Y.Assert.areEqual(com.runwaysdk.jstest.States.CO, testClass.getSingleState()[0], "testClass.getSingleState()[0] did not return the expected value.");
    Y.Assert.areEqual(true, testClass.isNewInstance(), "testClass.isNewInstance() did not return the expected value.");
  }
  
});

TestFramework.newTestCase(SUITE_NAME, {

  name : 'PermissionTests',
	
	/**
	 * Tests to make sure that a user without permissions can't get our TestClass
	 */
	testGetWithNoPermissions : function ()
	{
		var yuiTest = this;
    
	    // get a type without permission
	    var cb = function(sessionId)
	    {
			var finishTheTest = function (handler)
			{
				var cb2 = function ()
        {
					alert("resume");
          yuiTest.resume(handler);
        }
				Y.log("Logging back into System", "debug");
        com.runwaysdk.Facade.changeLogin(new CallbackHandler(yuiTest, {onSuccess:cb2}), g_allPermUser, g_allPermPass);
			}
			
      // we DON'T want to land in here since the call should fail
      var successCB = function()
      {
        finishTheTest(function(){ Y.Assert.fail("A permissionless user was able to get TestClass."); });
      };
      
      // this is the callback we want to be called
      var failCB = function(exception)
      {
				finishTheTest();
      };
      
      // we do a getInstance here instead of a new instance since the new instance
      // call will have the permissions of the user with all permissions.
			Y.log("Attempting to get TestClass.", "debug");
      com.runwaysdk.jstest.TestClass.get(new CallbackHandler(yuiTest, {onSuccess: successCB, onFailure : failCB}), g_inaccessibleId);
    };
    
		Y.log("Logging into permissionless user.", "debug");
    com.runwaysdk.Facade.changeLogin(new CallbackHandler(yuiTest, {onSuccess : cb}), g_noPermUser, g_noPermPass);
      
    yuiTest.wait(TIMEOUT);
	},
	
	/**
   * testAddRemovePermissions is a very complicated test that is divided into 4 different functions for
   * readability. This particular function gives our permissionless user a bunch of permissions, and then
   * logs in as that user.
   */
  testAddRemovePermissions : function (sessionId)
  {
      var yuiTest = this;
    
      // define an array of Read/Write operations
      var operations = [com.runwaysdk.system.AllOperations.READ.name(),
        com.runwaysdk.system.AllOperations.WRITE.name()];
        
      var cb = function()
      {
        var cb2 = function()
        {
          var cb3 = function()
          {
            Y.log("Logging into user NoPerm", "debug");
            
            var handler = new CallbackHandler(yuiTest, {
              onSuccess: function(){ yuiTest.checkNewPermissions(); }
            });
            com.runwaysdk.Facade.changeLogin(handler, g_noPermUser, g_noPermPass);
          };
          
          // give write permission to the integer attribute
          var handler = new CallbackHandler(yuiTest, {
            onSuccess : cb3
          });
          com.runwaysdk.Facade.grantAttributePermission(handler, g_userIdNoPermissions, g_testIntegerMdId, operations[1]);
        };
        
        // give read/write permission to the boolean attribute
        Y.log("Adding Read/Write permissions on attributes for TestClass to user NoPerm", "debug");
        
        var handler = new CallbackHandler(yuiTest, {
          onSuccess : cb2
        });
        com.runwaysdk.Facade.grantAttributePermission(handler, g_userIdNoPermissions, g_testBooleanMdId, operations);
      };
      
      Y.log("Adding Read/Write permissions for TestClass to user NoPerm", "debug");
      com.runwaysdk.Facade.grantTypePermission(new CallbackHandler(yuiTest, {onSuccess : cb}), g_userIdNoPermissions, g_testClassMdId, operations);
			
			this.wait(TIMEOUT);
  },
	
	/**
   * checkNewPermissions assumes that we are logged in as our previously permissionless user and
   * checks to make sure that he can use all of his new permissions.
   */
  checkNewPermissions : function (sessionId)
  {
      var yuiTest = this;
    
      var cb = function(testClass)
      {
        Y.log("Making sure that the user has Read/Write permissions to the TestClass.", "debug");
        if(testClass.isReadable && testClass.isWritable())    
        {
          // Success
        }
        else
        {
          yuiTest.resume(function(){
            Y.Assert.fail("The user did not have Read/Write permissions to TestClass when he was previously given them.");
          });
        }
        
        Y.log("Making sure the user has Read/Write permissions to the attributes on TestClass.", "debug");
        if(testClass.isTestBooleanReadable() && testClass.isTestBooleanWritable() 
          && testClass.isTestIntegerWritable() && !testClass.isTestIntegerReadable())
        {
          // Success
        }
        else
        {
          yuiTest.resume(function(){
            Y.Assert.fail("The user did not have Read/Write permissions to the attributes on TestClass when he was previously given them.");
          });
        }
        
        yuiTest.cleanPermissions();
      };

      Y.log("Now in checkNewPermissions. Attempting to get TestClass.", "debug");
      com.runwaysdk.jstest.TestClass.get(new CallbackHandler(yuiTest, {onSuccess : cb}), g_inaccessibleId);
  },
  
	/**
   * Logs in as root user and removes all previously given permissions.
   */
  cleanPermissions : function (sessionId)
  {
      var yuiTest = this;
    
      // before we exit, we must remove all permissions from
      // user that we use to test permissions
      var changeLoginCB = function(sessionId)
      {
        // the permissions to revoke
        var operations = [com.runwaysdk.system.AllOperations.READ.name(),
          com.runwaysdk.system.AllOperations.WRITE.name()];
          
        var revokePermissionsCB = function()  
        {
          var revokePermissionsCB2 = function()
          {
            var handler = new CallbackHandler(yuiTest, {
              onSuccess: function(){ yuiTest.checkRevokedPermissions(); }
            });
            com.runwaysdk.Facade.revokeAttributePermission(handler, g_userIdNoPermissions, g_testIntegerMdId, operations[1]);
          };
       
			    Y.log("Revoking Attribute Permissions.", "debug");
          com.runwaysdk.Facade.revokeAttributePermission(new CallbackHandler(yuiTest, {onSuccess : revokePermissionsCB2}), g_userIdNoPermissions, g_testBooleanMdId, operations);
        };
        
				Y.log("Revoking Type Permissions.", "debug");
        com.runwaysdk.Facade.revokeTypePermission(new CallbackHandler(yuiTest, {onSuccess : revokePermissionsCB}), g_userIdNoPermissions, g_testClassMdId, operations);
      };

			Y.log("Now in cleanPermissions. Logging into System.", "debug");
      com.runwaysdk.Facade.changeLogin(new CallbackHandler(yuiTest, {onSuccess : changeLoginCB}), g_allPermUser, g_allPermPass);
  },
	
  /**
   * Logs in as our permissionless user and makes sure his permissions were successfully removed.
   */
  checkRevokedPermissions : function ()
  {
		var yuiTest = this;
		
  		var changeLoginCB = function(session)	
  		{
  			var finish = function(didPass)
  			{
  				var cb = function()
  				{
  					yuiTest.resume(function () {
  						Y.assert(didPass, "A user was given permissions that he should not have been given.");
  					});
  				}
	
					Y.log("Logging into System.", "debug");
  				com.runwaysdk.Facade.changeLogin(new CallbackHandler(yuiTest, {onSuccess : cb}), g_allPermUser, g_allPermPass);
  			}
  			
  			var successCB = function()
  			{
  				finish(false);
  			};
  			
  			// we want to land here because an exception should have been thrown
  			var failCB = function()
  			{
  				finish(true);
  			};

				Y.log("Attempting to get TestClass.", "debug");
  			com.runwaysdk.jstest.TestClass.get(new CallbackHandler(yuiTest, {onSuccess :successCB, onFailure :failCB}), g_inaccessibleId);	
  		};
  		
			Y.log("Now in checkRevokedPermissions. Changing login to NoPerm", "debug");
  		com.runwaysdk.Facade.changeLogin(new CallbackHandler(yuiTest, {onSuccess : changeLoginCB}), g_noPermUser, g_noPermPass);
  },

});

TestFramework.newTestCase(SUITE_NAME, {

  name : 'AttributeTests',
  
  cb : null,
  
  setUp : function ()
  {
    // remove all previously defined tasks and set all values to defaults
    g_taskQueue = new struct.TaskQueue();
  },
  
  tearDown : function()
  {
    g_taskQueue = null;
  },
  
  testPrimatives : function ()
  {
    var yuiTest = this;
    
    // add a 0 to the month and day if necessary
    
    /** Creating a new instance of TestClass **/
  
    var testClass = new com.runwaysdk.jstest.TestClass();
    
    var values = {
      'testInteger' : 123,
      'testLong' : 123456789,
      'testFloat' : -12.34,
      'testDecimal' : 23.87,
      'testDouble' : 87.31,
      'testCharacter' : 'Here\'s Johnny',
      'testText' : 'Here\'s Johnny! No, wait ... it\'s only Ted',
      'testBoolean' : true
    };
    
    /** Setting attributes on TestClass instance **/
    
    testClass.setTestInteger(values.testInteger);
    testClass.setTestLong(values.testLong);
    testClass.setTestFloat(values.testFloat);
    testClass.setTestDecimal(values.testDecimal);
    testClass.setTestDouble(values.testDouble);
    testClass.setTestCharacter(values.testCharacter); 
    testClass.setTestText(values.testText); 
    testClass.setTestBoolean(values.testBoolean); 
    
    /** Applying TestClass instance to the database **/
    
    var checkValues = function checkValues(testClass)
    {
      g_testClass = testClass;
      
      /** Getting attributes from TestClass instance **/
      
      var returned = {
        'testInteger' : testClass.getTestInteger(),
        'testLong' : testClass.getTestLong(),
        'testFloat' : testClass.getTestFloat(),
        'testDecimal' : testClass.getTestDecimal(),
        'testDouble' : testClass.getTestDouble(),
        'testCharacter' : testClass.getTestCharacter(),
        'testText' : testClass.getTestText()
      };
      
      var resumeFunc = function ()
      {
        for(i in returned)
        {
          Y.Assert.areEqual(values[i], returned[i], "The returned value did not match the applied value.");
        }
      }
      
      yuiTest.resume(resumeFunc);
    };

    testClass.apply(new CallbackHandler(yuiTest, {
      onSuccess : checkValues
    }));
    
    this.wait(TIMEOUT);
  },
  
  testReference : function ()
  {
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq /* TaskQueue */){
        
        Y.log("Getting the ref object.", "debug");
        
        var testClass = new com.runwaysdk.jstest.TestClass();
        
        // don't persist this TestClass instance
        testClass.getTestReferenceObject(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, refObj){
        
        Y.log("Making sure the ref object is null and creating a new instance of the referenced object.", "debug");
        
        if (refObj != null) {
          yuiTest.resume(function(){
            Y.Assert.fail("The default reference object was expected to be null but it wasn't.");
          });
        }
        
        var refClass = new com.runwaysdk.jstest.RefClass();
        refClass.setRefChar("some value"); // we'll check this later
        refClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, refClass){
        
        Y.log("Setting the reference object on a TestClass instance and applying.", "debug");
        
        var testClass = new com.runwaysdk.jstest.TestClass();
        testClass.setTestReferenceObject(refClass);
        testClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testClass){
        
        Y.log("Getting the reference object from a TestClass instance.", "debug");
        
        testClass.getTestReferenceObject(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, refClass){
        
        Y.log("Checking the value of the reference.", "debug");
        
        if (refClass.getRefChar() != 'some value') 
          referencePassed = false;
        
        yuiTest.resume(function(){
          Y.Assert.areEqual('some value', refClass.getRefChar(), "The returned value on the reference object does not match the applied value.");
        });
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },
  
  testEnumeration :function ()
  {
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        com.runwaysdk.jstest.States.CO.item(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, co){
      
        var enumStruct = co.getStatePhone();
        if (!(enumStruct instanceof com.runwaysdk.system.PhoneNumber)) {
          yuiTest.resume(function(){
            Y.Assert.fail("The returned enumeration from getStatePhone did not pass the instanceof (com.runwaysdk.system.PhoneNumber) test.");
          });
        }
        
        Y.log("Testing default values and applying the TestClass instance.", "debug");
        
        var testClass = new com.runwaysdk.jstest.TestClass();
        
        var singleVals = testClass.getSingleState(); // should have CO item
        if (!(singleVals instanceof Array && singleVals.length == 1 && singleVals[0] == com.runwaysdk.jstest.States.CO)) {
          yuiTest.resume(function(){
            Y.assert(singleVals instanceof Array, "getSingleState did not return an array.");
            Y.Assert.areEqual(1, singleVals.length, "The size of the returned array from getSingleState did not match the expected value.");
            Y.assert(singleVals[0] == com.runwaysdk.jstest.States.CO, "The returned state did not match the expected value (com.runwaysdk.jstest.States.CO).");
          });
        }
        
        var multipleVals = testClass.getMultipleState(); // should be empty
        if (!(multipleVals instanceof Array && multipleVals.length == 0)) {
          yuiTest.resume(function(){
            Y.assert(multipleVals instanceof Array, "getMultipleState did not return an array.");
            Y.Assert.areEqual(0, singleVals.length, "The size of the returned array from getMultipleState did not match the expected value.");
          });
        }
        
        testClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testClass){
        
        Y.log("Locking the TestClass instance.", "debug");
        testClass.lock(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testClass){
      
        Y.log("Populating TestClass enumerations with data and applying.", "debug");
        
        testClass.addSingleState(com.runwaysdk.jstest.States.CA);
        testClass.addMultipleState(com.runwaysdk.jstest.States.CT);
        testClass.addMultipleState(com.runwaysdk.jstest.States.CO);
        
        testClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testClass){
      
        Y.log("Testing values of TestClass enumeration", "debug");
        
        var singleVals = testClass.getSingleState();
        var multipleVals = testClass.getMultipleState();
        if (singleVals[0] == com.runwaysdk.jstest.States.CA &&
        (multipleVals[0] == com.runwaysdk.jstest.States.CT || multipleVals[0] == com.runwaysdk.jstest.States.CO) &&
        (multipleVals[1] == com.runwaysdk.jstest.States.CT || multipleVals[1] == com.runwaysdk.jstest.States.CO) &&
        multipleVals[0] != multipleVals[1]) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual(singleVals[0], com.runwaysdk.jstest.States.CA, "TestClass.getSingleState()[0] is not the expected value.");
            Y.assert(multipleVals[0] == com.runwaysdk.jstest.States.CT || multipleVals[0] == com.runwaysdk.jstest.States.CO, "TestClass.getMultipleState()[0] = '" + multipleVals[0] + "' is not the expected value.");
            Y.Assert.areNotEqual(multipleVals[0], multipleVals[1], "These two values cannot be the same.");
          });
        }
        
        Y.log("Testing the remove operation on an enumeration", "debug");
        
        testClass.removeMultipleState(com.runwaysdk.jstest.States.CT);
        var multipleState = testClass.getMultipleState();
        if (multipleState instanceof Array && multipleState.length == 1 && multipleState[0] == com.runwaysdk.jstest.States.CO) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.assert(multipleState instanceof Array, "multipleState is not an instanceof Array");
            Y.Assert.areEqual(1, multipleState.length, "multipleState.length did not match the expected value.");
            Y.Assert.areEqual(com.runwaysdk.jstest.States.CO, multipleState[0], "multipleState[0] did not match the expected value");
          });
        }
        
        
        Y.log("Testing the clear operation on an enumeration.", "debug");
        
        // add another item to the multiple select to make sure clear can remove more than one item
        testClass.addMultipleState(com.runwaysdk.jstest.States.CA);
        testClass.clearMultipleState();
        var multipleState = testClass.getMultipleState();
        if (multipleState instanceof Array && multipleState.length == 0) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.assert(multipleState instanceof Array, "multipleState is not an instance of array");
            Y.Assert.areEqual(0, multipleState.length, "multipleState.length did not match the expected value.");
          });
        }
        
        yuiTest.resume(); // Pass this test!
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },
  
  testStruct : function ()
  {
    var yuiTest = this;
    
    var testClass = new com.runwaysdk.jstest.TestClass();

    var cell = testClass.getCellPhone();
    // remove apply, new instance, ... etc from BusinessDTO if containing a Struct
    cell.setAreaCode(303)
    cell.setPrefix(979);
    cell.setSuffix(5445);
    cell.setExtension(976);
    
    var cb = function(testClass){
      var cell = testClass.getCellPhone();
      
      var resumeFunc = function () {
        Y.Assert.areEqual(303, cell.getAreaCode(), "The returned area code did not match the expected value.");
        Y.Assert.areEqual(979, cell.getPrefix(), "The returned prefix did not match the expected value.");
        Y.Assert.areEqual(5445, cell.getSuffix(), "The returned suffix did not match the expected value.");
        Y.Assert.areEqual(976, cell.getExtension(), "The returned extension did not match the expected value.");
      }
      
      yuiTest.resume(resumeFunc);
    }
    
    testClass.apply(new CallbackHandler(yuiTest, {onSuccess: cb}));
    
    yuiTest.wait(TIMEOUT);
  },
  
  testMethods: function(){
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        Y.log("Invoking an instance method", "debug");
        
        var testClass = new com.runwaysdk.jstest.TestClass();
        
        testClass.createInstances(new CallbackHandler(yuiTest), 5);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, retObjs, testClass){
        
        Y.log("Running some instanceof tests and applying.", "debug");
        
        if (retObjs instanceof Array && retObjs.length == 5) {
          for (var i = 0; i < retObjs.length; i++) {
            if (!(retObjs[i] instanceof com.runwaysdk.jstest.TestClass)) {
              yuiTest.resume(function(){
                Y.Assert.fail("retObjs[" + i + "] instanceof TestClass failed.");
              });
            }
          }
        }
        else {
          yuiTest.resume(function(){
            Y.assert(retObjs instanceof Array, "retObjs is not an instanceof Array.");
            Y.Assert.areEqual(5, retObjs.length, "retObjs.length did not equal its expected value.");
          });
        }
        
        testClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testClass2){
        
        Y.log("Invoking the static version of an instance method", "debug");
            
        com.runwaysdk.jstest.TestClass.createInstances(new CallbackHandler(yuiTest), testClass2.getId(), 4);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, retObjs, callingObj){
      
        Y.log("Running some tests and invoking a static method", "debug");
      
        if (retObjs instanceof Array && retObjs.length == 4) {
          for (var i = 0; i < retObjs.length; i++) {
            if (!(retObjs[i] instanceof com.runwaysdk.jstest.TestClass)) {
              yuiTest.resume(function(){
                Y.Assert.fail("retObjs[" + i + "] instanceof TestClass failed.");
              });
            }
          }
        }
        else {
          yuiTest.resume(function(){
            Y.assert(retObjs instanceof Array, "retObjs is not an instanceof Array.");
            Y.Assert.areEqual(4, retObjs.length, "retObjs.length did not equal its expected value.");
          });
        }
        
        com.runwaysdk.jstest.TestClass.doubleAnInteger(new CallbackHandler(yuiTest), 10);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, retVal){
      
        if (retVal == 20) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual(20, retVal, "TestClass.doubleAnInteger did not return the expected value.");
          });
        }
        
        Y.log("Invoking an instance method with both the parameter and return as nulls", "debug");
        var testClass = new com.runwaysdk.jstest.TestClass();
        
        testClass.createInstances(new CallbackHandler(yuiTest), null);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, retObjects){
        
        yuiTest.resume(function(){
          Y.Assert.areEqual(null, retObjects, "TestClass.createInstances did not return the expected value.");
        });
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },
  
  testMetadata : function ()
  {
    var yuiTest = this;
    
    Y.log("Testing the metadata for the TestClass type", "debug");
    var instance = new com.runwaysdk.jstest.TestClass();
    
    var displayLabel = instance.getMd().getDisplayLabel();
    var description = instance.getMd().getDescription();
    var id = instance.getMd().getId();
    
    Y.Assert.areEqual("A Class", displayLabel, "The display label did not match the expected value.");
    Y.Assert.areEqual("ajax test Class", description, "The description did not match the expected value.");
    Y.Assert.areEqual(g_testClassMdId, id, "The id did not match the expected value.");
    
    Y.log("Testing the metadata for an integer", "debug");
    var str = "Integer";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("An " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(true, instance["getTest" + str + "Md"]().rejectZero(), "instance.getTest" + str + "Md().rejectZero() did not match the expected value.");
    Y.Assert.areEqual(true, instance["getTest" + str + "Md"]().rejectNegative(), "instance.getTest" + str + "Md().rejectNegative() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectPositive(), "instance.getTest" + str + "Md().rejectPositive() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a long", "debug");
    str = "Long";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectZero(), "instance.getTest" + str + "Md().rejectZero() did not match the expected value.");
    Y.Assert.areEqual(true, instance["getTest" + str + "Md"]().rejectNegative(), "instance.getTest" + str + "Md().rejectNegative() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectPositive(), "instance.getTest" + str + "Md().rejectPositive() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a float", "debug");
    str = "Float";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(true, instance["getTest" + str + "Md"]().rejectZero(), "instance.getTest" + str + "Md().rejectZero() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectNegative(), "instance.getTest" + str + "Md().rejectNegative() did not match the expected value.");
    Y.Assert.areEqual(true, instance["getTest" + str + "Md"]().rejectPositive(), "instance.getTest" + str + "Md().rejectPositive() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual(10, instance["getTest" + str + "Md"]().getTotalLength(), "instance.getTest" + str + "Md().getTotalLength() did not match the expected value.");
    Y.Assert.areEqual(2, instance["getTest" + str + "Md"]().getDecimalLength(), "instance.getTest" + str + "Md().getDecimalLength() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a decimal", "debug");
    str = "Decimal";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectZero(), "instance.getTest" + str + "Md().rejectZero() did not match the expected value.");
    Y.Assert.areEqual(true, instance["getTest" + str + "Md"]().rejectNegative(), "instance.getTest" + str + "Md().rejectNegative() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectPositive(), "instance.getTest" + str + "Md().rejectPositive() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual(13, instance["getTest" + str + "Md"]().getTotalLength(), "instance.getTest" + str + "Md().getTotalLength() did not match the expected value.");
    Y.Assert.areEqual(3, instance["getTest" + str + "Md"]().getDecimalLength(), "instance.getTest" + str + "Md().getDecimalLength() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
      
    Y.log("Testing the metadata for a double", "debug");
    str = "Double";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectZero(), "instance.getTest" + str + "Md().rejectZero() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectNegative(), "instance.getTest" + str + "Md().rejectNegative() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectPositive(), "instance.getTest" + str + "Md().rejectPositive() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual(16, instance["getTest" + str + "Md"]().getTotalLength(), "instance.getTest" + str + "Md().getTotalLength() did not match the expected value.");
    Y.Assert.areEqual(4, instance["getTest" + str + "Md"]().getDecimalLength(), "instance.getTest" + str + "Md().getDecimalLength() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a date time", "debug");
    str = "DateTime";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a date", "debug");
    str = "Date";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a time", "debug");
    str = "Time";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a boolean", "debug");
    str = "Boolean";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a character", "debug");
    str = "Character";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(16, instance["getTest" + str + "Md"]().getSize(), "instance.getTest" + str + "Md().getSize() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a text", "debug");
    str = "Text";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a reference object", "debug");
    str = "ReferenceObject";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a struct", "debug");
    if(instance.getHomePhoneMd().getDisplayLabel() == 'Test Struct' &&
      instance.getHomePhoneMd().getDescription() == 'A Struct' &&
      instance.getHomePhoneMd().getDefiningMdStruct() == new com.runwaysdk.system.PhoneNumber().getType() &&
      instance.getHomePhoneMd().isImmutable() == false &&
      instance.getHomePhoneMd().isRequired() == false &&
      instance.getHomePhoneMd().getName() == 'homePhone')
      {
        // Success
      }
    else
    {
      Y.Assert.fail("The metadata for a struct is incorrect.");
    }
    
    Y.log("Testing the metadata for an enumeration", "debug");

    var coName = com.runwaysdk.jstest.States.CO.name();
    var ctName = com.runwaysdk.jstest.States.CT.name();
    var caName = com.runwaysdk.jstest.States.CA.name();
    
    var ssValues = instance.getSingleStateMd().getEnumNames();
    
    if(instance.getSingleStateMd().getDisplayLabel() == 'Test Enumeration' &&
      instance.getSingleStateMd().getDescription() == 'An Enumeration' &&
      instance.getSingleStateMd().selectMultiple() == false &&
      instance.getSingleStateMd().isImmutable() == false &&
      instance.getSingleStateMd().isRequired() == true &&
      /* TODO check display label
      ssValues[coName] === coName &&
      ssValues[ctName] === ctName &&
      ssValues[caName] === caName &&
      */
      instance.getSingleStateMd().getName() == 'singleState')
      {
        // Success
      }
    else
    {
      Y.Assert.fail("The metadata for an enumeration is incorrect.");
    }
    
    Y.log("Testing the metadata for a hash", "debug");
    str = "Hash";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual("MD5", instance["getTest" + str + "Md"]().getEncryptionMethod(), "instance.getTest" + str + "Md().getEncryptionMethod() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a symmetric", "debug");
    if(instance.getTestSymmetricMd().getDisplayLabel() == 'Test Symmetric' &&
      instance.getTestSymmetricMd().getDescription() == 'A Symmetric' &&
      instance.getTestSymmetricMd().getEncryptionMethod() == 'DES/CBC/PKCS5Padding' &&
      instance.getTestSymmetricMd().isImmutable() == false &&
      instance.getTestSymmetricMd().isRequired() == false &&
      instance.getTestSymmetricMd().getName() == 'testSymmetric')
      {
        // Success
      }
    else
    {
      Y.Assert.fail("The metadata for a hash is incorrect.");
    }
    str = "Symmetric";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual("DES/CBC/PKCS5Padding", instance["getTest" + str + "Md"]().getEncryptionMethod(), "instance.getTest" + str + "Md().getEncryptionMethod() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
  },
  
  testFacade: function(){
    yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      
        Y.log("Invoking a facade method (param: int, return: TestClass[])", "debug");
        
        var objArr = new Array();
        for (var i = 1; i < 11; i++) {
          var obj = new com.runwaysdk.jstest.TestClass();
          obj.setTestInteger(i);
          objArr.push(obj);
        }
        
        com.runwaysdk.jstest.Summation.sumIntegerValues(new CallbackHandler(yuiTest), objArr);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, retVal){
      
        if (retVal == 55) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual(55, retVal, "The return value (retVal) did not equal the expected value.");
          });
        }
        
        Y.log("Invoking a facade method (param: a null TestClass, return: a null Integer)", "debug");
        
        com.runwaysdk.jstest.Summation.getNullInteger(new CallbackHandler(yuiTest), null);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, nullInt){
      
        // we're expecting a null
        if (nullInt == null) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual(null, nullInt, "The return value (nullInt) did not equal the expected value.");
          });
        }
        
        Y.log("Invoking a facade method (param: TestView, return: TestView)", "debug");
        var testView = new com.runwaysdk.jstest.TestView();
        var viewChar = "Concat1";
        testView.setViewCharacter(viewChar);
        
        var curry = function (testView2) { CallbackHandler.next(testView2,viewChar) };
        
        com.runwaysdk.jstest.Summation.concatViewChar(new CallbackHandler(yuiTest, {onSuccess: curry}), testView);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testView, viewChar){
      
        if (testView.getViewCharacter() == (viewChar + viewChar)) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual((viewChar + viewChar), testView.getViewCharacter(), "testView.getViewCharacter() did not equal the expected value.");
          });
        }
        
        Y.log("Invoking a facade method (param: TestUtil, return: TestUtil)", "debug");
        var testUtil = new com.runwaysdk.jstest.TestUtil();
        var utilChar = "Concat2";
        testUtil.setUtilCharacter(utilChar);
        
        var curry = function (testUtil2) { CallbackHandler.next(testUtil2,utilChar) };
        
        com.runwaysdk.jstest.Summation.concatUtilChar(new CallbackHandler(yuiTest, {onSuccess: curry}), testUtil);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testUtil2, utilChar){
      
        if (testUtil2.getUtilCharacter() == (utilChar + utilChar)) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual((utilChar + utilChar), testUtil2.getUtilCharacter(), "testUtil2.getUtilCharacter() did not equal the expected value.");
          });
        }
        
        Y.log("Invoking a facade method (param: void, return: void)", "debug");
        
        com.runwaysdk.jstest.Summation.doNothing(new CallbackHandler(yuiTest));
      }
    }));
    
    // doNothingCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, voidRet){
      
        if (voidRet == null) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual(null, voidRet, "voidRet did not equal the expected value.");
          });
        }
        
        var array = [];
        for (var i = 0; i < 5; i++) {
          array[i] = [];
          for (var j = 0; j < 5; j++) {
            array[i][j] = j;
          }
        }
        
        Y.log("Invoking a facade method (param: int[][], return int[][])", "debug");
        
        com.runwaysdk.jstest.Summation.arrayInOut(new CallbackHandler(yuiTest), array);
      }
    }));
    
    // arrayInOutCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, array){
        
        if ( !(array instanceof Array) ) {
          yuiTest.resume(function(){
            Y.assert.fail("Summation.arrayInOut did not return an array. It returned " + typeof array + ".");
          });
        }
        
        var passed = true;
        for (var i = 0; i < 5; i++) {
          for (var j = 0; j < 5; j++) {
            if (array[i][j] != j) {
              yuiTest.resume(function(){
                Y.assert.fail("Summation.arrayInOut did not return the correct array.");
              });
            }
          }
        }
        
        Y.log("Invoking a facade method (param: date, return date)", "debug");
        var date = new Date("Fri Jan 25 19:36:44 GMT 2008");
        var dateISO = Mojo.Util.toISO8601(date, false);
        
        var curried = function(a){ CallbackHandler.next(dateISO, a); };
        
        com.runwaysdk.jstest.Summation.dateInOut(new CallbackHandler(yuiTest, {onSuccess: curried}), dateISO);
      }
    }));
    
    // dateInOutCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, dateIn, dateOut){
      
        // TODO test as instance of Date
        yuiTest.resume(function(){
          Y.Assert.areEqual(dateIn.toString(), dateOut.toString(), "The input and output dates did not match.")
        });
      }
    }));
   
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },
  
  testRelationships : function ()
  {
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        new com.runwaysdk.jstest.TestClass().apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, parent_){
      
        g_parent = parent_;
        
        new com.runwaysdk.jstest.RefClass().apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, child){
      
        g_child = child;
        Y.log("Adding a child to a parent (via object)", "debug");
        
        g_parent.addRefClass(new CallbackHandler(yuiTest), g_child);
      }
    }));
    
    // addChildCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rel){
      
        if (rel instanceof com.runwaysdk.jstest.Befriends && rel.getParentId() == g_parent.getId() && rel.getChildId() == g_child.getId()) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.assert(rel instanceof com.runwaysdk.jstest.Befriends, "rel is not an instanceof com.runwaysdk.jstest.Befriends.");
            Y.Assert.areEqual(g_parent.getId(), rel.getParentId(), "The returned parent id (on rel) is incorrect.");
            Y.Assert.areEqual(g_child.getId(), rel.getChildId(), "The returned child id (on rel) is incorrect.");
          });
        }
        
        Y.log("Checking the RelationshipMd on the relationship dto.", "debug");
        var refClass = new com.runwaysdk.jstest.RefClass();
        var testClass = new com.runwaysdk.jstest.TestClass();
        
        if (rel.getMd().getParentMdBusiness() == testClass.getType() &&
        rel.getMd().getChildMdBusiness() == refClass.getType()) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual(testClass.getType(), rel.getMd().getParentMdBusiness(), "rel.getMd().getParentMdBusiness() did not equal the expected value of testClass.getType().");
            Y.Assert.areEqual(refClass.getType(), rel.getMd().getChildMdBusiness(), "rel.getMd().getChildMdBusiness() did not equal the expected value of refClass.getType().");
          });
        }
        
        Y.log("Adding a child to a parent (via id)", "debug");
        
        g_parent.addRefClass(new CallbackHandler(yuiTest), g_child.getId());
      }
    }));
    
    // addChildCB2
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rel){
      
        if (rel instanceof com.runwaysdk.jstest.Befriends && rel.getParentId() == g_parent.getId() && rel.getChildId() == g_child.getId()) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.assert(rel instanceof com.runwaysdk.jstest.Befriends, "The returned relationship (rel) is not an instanceof com.runwaysdk.jstest.Befriends.");
            Y.Assert.areEqual(g_parent.getId(), rel.getParentId(), "The returned relationship (rel) has the wrong parent id.");
            Y.Assert.areEqual(g_child.getId(), rel.getChildId(), "The returned relationship (rel) has the wrong child id.");
          });
        }
        
        Y.log("Adding a parent to a child (via object)", "debug");
        
        g_child.addTestClass(new CallbackHandler(yuiTest), g_parent);
      }
    }));
    
    // addParentCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rel){
      
        if (rel instanceof com.runwaysdk.jstest.Befriends && rel.getParentId() == g_parent.getId() && rel.getChildId() == g_child.getId()) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.assert(rel instanceof com.runwaysdk.jstest.Befriends, "The returned relationship (rel) is not an instanceof com.runwaysdk.jstest.Befriends.");
            Y.Assert.areEqual(g_parent.getId(), rel.getParentId(), "The returned relationship (rel) has the wrong parent id.");
            Y.Assert.areEqual(g_child.getId(), rel.getChildId(), "The returned relationship (rel) has the wrong child id.");
          });
        }
        
        Y.log("Adding a parent to a child (via id)", "debug");
        
        g_child.addTestClass(new CallbackHandler(yuiTest), g_parent.getId());
      }
    }));
    
    // addParentCB2
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rel){
      
        if (rel instanceof com.runwaysdk.jstest.Befriends && rel.getParentId() == g_parent.getId() && rel.getChildId() == g_child.getId()) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.assert(rel instanceof com.runwaysdk.jstest.Befriends, "The returned relationship (rel) is not an instanceof com.runwaysdk.jstest.Befriends.");
            Y.Assert.areEqual(g_parent.getId(), rel.getParentId(), "The returned relationship (rel) has the wrong parent id.");
            Y.Assert.areEqual(g_child.getId(), rel.getChildId(), "The returned relationship (rel) has the wrong child id.");
          });
        }
        
        Y.log("Applying the relationship", "debug");
        
        rel.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    // applyRelCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rel){
      
        g_rel = rel;
        
        Y.log("Getting the relationship instance", "debug");
        
        com.runwaysdk.jstest.Befriends.get(new CallbackHandler(yuiTest), rel.getId());
      }
    }));
    
    // getRelCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rel){
      
        if (g_rel.getId() == rel.getId()) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual(g_rel.getId(), rel.getId(), "The returned relationship (rel) has an unexpected id.");
          });
        }
        
        Y.log("Getting the child from the relationship", "debug");
        
        rel.getChild(new CallbackHandler(yuiTest));
      }
    }));
    
    // getChildCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, child){
      
        if (g_child.getId() == child.getId()) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual(g_rel.getId(), rel.getId(), "The returned child has an unexpected id.");
          });
        }
        
        Y.log("Getting the parent from the relationship", "debug");
        
        g_rel.getParent(new CallbackHandler(yuiTest));
      }
    }));
    
    // getParentCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, parent_){
      
        if (g_parent.getId() == parent_.getId()) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual(g_parent.getId(), parent_.getId(), "The returned parent has an unexpected id.");
          });
        }
        
        // add another child so that getAllChildren() will return more than one result
        
        var newChild = new com.runwaysdk.jstest.RefClass();
        
        newChild.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    // getParentCB ... cb
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, child2){
      
        g_child2 = child2;
        Y.log("Adding another child to the parent", "debug");
        
        g_parent.addRefClass(new CallbackHandler(yuiTest), child2);
      }
    }));
    
    // addChild2CB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rel){
        
        rel.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    // addChild2CB ... cb
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rel2){
      
        // get all children (should be 2) 
        g_rel2 = rel2;
        Y.log("Getting all children objects from a parent", "debug");
        
        g_parent.getAllRefClass(new CallbackHandler(yuiTest));
      }
    }));
    
    // getChildrenCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, children){
      
        if (children instanceof Array && children.length == 2) {
          if ((children[0].getId() == g_child.getId() || children[0].getId() == g_child2.getId()) &&
          (children[1].getId() == g_child.getId() || children[1].getId() == g_child2.getId()) &&
          children[0].getId() != children[1].getId()) {
            // Success
          }
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("getAllRefClass did not return the correct children");
          })
        }
        
        Y.log("Getting all parents objects from a child", "debug");
        
        g_child.getAllTestClass(new CallbackHandler(yuiTest));
      }
    }));
    
    // getParentsCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, parents){
      
        if (parents instanceof Array && parents.length == 1 && parents[0].getId() == g_parent.getId()) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("getAllTestClass did not return the correct parents");
          })
        }
        
        Y.log("Getting all child relationship objects from the parent", "debug");
        
        g_parent.getAllRefClassRelationships(new CallbackHandler(yuiTest));
      }
    }));
    
    // getAllChildRelCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rels){
      
        if (rels instanceof Array && rels.length == 2 &&
        (rels[0].getId() == g_rel.getId() || rels[0].getId() == g_rel2.getId()) &&
        (rels[1].getId() == g_rel.getId() || rels[1].getId() == g_rel2.getId()) &&
        rels[0].getId() != rels[1].getId()) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("getAllRefClassRelationships did not return the correct relationships.");
          })
        }
        
        Y.log("Getting all parent relationship objects from the child", "debug");
        
        g_child.getAllTestClassRelationships(new CallbackHandler(yuiTest));
      }
    }));
    
    // getAllParentsRelCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rels){
      
        if (rels instanceof Array && rels.length == 1 && rels[0].getId() == g_rel.getId()) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("getAllTestClassRelationships did not return the correct relationships.");
          });
        }
        
        com.runwaysdk.jstest.TestClass.get(new CallbackHandler(yuiTest), g_parentIdWithChildren);
      }
    }));
    
    // getAllParentsRelCB ... cb
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testClass){
      
        g_parentWithChildren = testClass;
        
        Y.log("Deleting a single child from a parent", "debug");
        
        testClass.removeRefClass(new CallbackHandler(yuiTest), g_deletedChildRelId);
      }
    }));
    
    // removeChildCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      
        g_parentWithChildren.getAllRefClassRelationships(new CallbackHandler(yuiTest));
      }
    }));
    
    // removeChildCB ... cb
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rels){
      
        // we started with 3 children. There should be only two now
        if (rels.length == 2 && rels[0].getId() != g_deletedChildRelId && rels[1].getId() != g_deletedChildRelId) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("getAllRefClassRelationships did not return the correct relationships.");
          });
        }
        
        Y.log("Deleting all children from a parent", "debug");
        
        g_parentWithChildren.removeAllRefClass(new CallbackHandler(yuiTest));
      }
    }));
    
    // removeAllChildCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        g_parentWithChildren.getAllRefClassRelationships(new CallbackHandler(yuiTest));
      }
    }));
    
    // removeAllChildCB ... cb
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rels){
      
        // we had 2 children. There should be 0 now
        if (rels instanceof Array && rels.length == 0) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("getAllRefClassRelationships did not return the correct relationships.");
          });
        }
        
        // get the child instance
        com.runwaysdk.jstest.RefClass.get(new CallbackHandler(yuiTest), g_childIdWithParents);
      }
    }));
    
    // removeAllChildCB ... cb2
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, refClass){
      
        g_childWithParents = refClass;
        
        Y.log("Deleting a single parent from a child", "debug");
        
        refClass.removeTestClass(new CallbackHandler(yuiTest), g_deletedParentRelId);
      }
    }));
    
    // removeParentCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        g_childWithParents.getAllTestClassRelationships(new CallbackHandler(yuiTest));
      }
    }));
    
    // removeParentCB ... cb
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rels){
      
        // we started with 3 parents. There should be only two now
        if (rels.length == 2 && rels[0].getId() != g_deletedParentRelId && rels[1].getId() != g_deletedParentRelId) {
        // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("getAllTestClassRelationships did not return the correct relationships.");
          });
        }
        
        Y.log("Deleting all parents from a child", "debug");
        
        g_childWithParents.removeAllTestClass(new CallbackHandler(yuiTest));
      }
    }));
    
    // removeAllParentsCB
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      
        g_childWithParents.getAllTestClassRelationships(new CallbackHandler(yuiTest));
      }
    }));
    
    // removeAllParentsCB ... cb
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, rels){
        
        // we had 2 parents. There should be 0 now.
        
        yuiTest.resume(function(){
          Y.assert(rels != null, "getAllTestClassRelationships returned a null value.");
          Y.assert(rels instanceof Array, "getAllTestClassRelationships did not return an array.");
          Y.Assert.areEqual(0, rels.length, "getAllTestClassRelationships did not return an array with the expected length.");
        });
      }
    }));
   
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },
  
  /**
   * Tests enumerations, especially the passing and receiving of items
   * from a generated MdEnumeration.
   */
  testMdEnumeration : function ()
  {
    var yuiTest = this;
    
    // testMdEnumeration
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      
        if (com.runwaysdk.jstest.States.CO instanceof com.runwaysdk.jstest.States) {
          // Success
        }
        else {
          // Note... we should not call yuiTest.resume here because the first task is actually
          // executed before yuiTest.wait is called and thus YUI will throw an exception.
          
          Y.Assert.fail("com.runwaysdk.jstest.States.CO is not an instanceof com.runwaysdk.jstest.States");
        }
        
        // make sure an EnumDTO picked out by an MdEnumeration can be serialized/deserialized
        Y.log("Invoking a facade method that requires an Enumeration Item parameter", "debug");
        
        com.runwaysdk.jstest.Summation.getState(new CallbackHandler(yuiTest), com.runwaysdk.jstest.States.CO);
      }
    }));
    
    // testMdEnumeration ... cb
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, state){
      
        if (state instanceof com.runwaysdk.jstest.States &&
        state == com.runwaysdk.jstest.States.CO) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("Summation.getState returned the wrong value: " + state);
          });
        }
        
        Y.log("Invoking a facade method with a null value for an Enumeration Item", "debug");
        
        com.runwaysdk.jstest.Summation.getState(new CallbackHandler(yuiTest), null);
      }
    }));
    
    // mdEnumerationWithNulls
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, state){
        
        yuiTest.resume(function(){
          Y.Assert.areEqual(null, state, "Summation.getState did not return the expected value.");
        });
      }
    }));
    /*
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        
      }
    }));
    */
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },
  
  /**
   * Tests moment attributes with the date object.
   */
  testMoment : function ()
  {
    var yuiTest = this;
    var date = new Date(2005, 6, 15, 8, 59 , 14);
    
    Y.log("Creating a new instance of TestClass.", "debug");
    
    var testClass = new com.runwaysdk.jstest.TestClass(); 
    
    Y.log("Setting attributes on TestClass instance.", "debug");
    
    testClass.setTestDateTime(date);
    testClass.setTestDate(date);
    testClass.setTestTime(date);  
    
    Y.log("Applying TestClass instance to the database.", "debug");
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      
        testClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    // checkValues
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testClass){
        
        g_testClass = testClass;
        
        /**
         * utility function to check correctness of a date.
         */
        var checkDate = function(date_){
          if (date_.getFullYear() == date.getFullYear() &&
          date_.getMonth() == date.getMonth() &&
          date_.getDate() == date.getDate()) {
            return true;
          }
          else {
            return false;
          }
        };
        
        /**
         * utility function to check correctness of time.
         */
        var checkTime = function(time_, msg){
          var refDate = date;
          var expected, actual;
          // TODO find better daylight savings fix
          
          expected = refDate.getHours();
          actual = time_.getHours();
          Y.Assert.areEqual(expected, actual, msg + " The hours did not match.");
          
          expected = refDate.getMinutes();
          actual = time_.getMinutes();
          Y.Assert.areEqual(expected, actual, msg + " The minutes did not match.");
          
          expected = refDate.getSeconds();
          actual = time_.getSeconds();
          Y.Assert.areEqual(expected, actual, msg + " The seconds did not match.");
        };
        
        var retDateTime = testClass.getTestDateTime();
        var retDate = testClass.getTestDate();
        var retTime = testClass.getTestTime();
        
        Y.log("Checking applied values.", "debug");
        
        yuiTest.resume(function(){
          checkDate(retDateTime, "The applied DateTime has an incorrect date.");
          checkTime(retDateTime, "The applied DateTime has an incorrect time.");
          checkDate(retDate, "The applied Date is incorrect.");
          checkTime(retTime, "The applied Time is incorrect.");
        });
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  }
  
});

TestFramework.newTestCase(SUITE_NAME, {

  name : 'SubclassTests',
  
  cb : null,
  
  setUp : function ()
  {
    // remove all previously defined tasks and set all values to defaults
    g_taskQueue = new struct.TaskQueue();
  },
  
  tearDown : function()
  {
    g_taskQueue = null;
  },
  
  testInstanceof : function ()
  {
    var subclass = new Mojo.$.com.runwaysdk.jstest.SubClass();
    
    // Making sure that subclass inherits from superclass
    
    Y.assert(subclass instanceof Mojo.$.com.runwaysdk.jstest.SubClass, "The first instanceof test failed.");
    Y.assert(subclass instanceof Mojo.$.com.runwaysdk.jstest.TestClass, "The second instanceof test failed.");
  },
  
  testPrimatives : function ()
  {
    var yuiTest = this;
    
    // add a 0 to the month and day if necessary
    
    /** Creating a new instance of TestClass **/
  
    var testClass = new com.runwaysdk.jstest.SubClass();
    
    var values = {
      'testInteger' : 123,
      'testLong' : 123456789,
      'testFloat' : -12.34,
      'testDecimal' : 23.87,
      'testDouble' : 87.31,
      'testCharacter' : 'Here\'s Johnny',
      'testText' : 'Here\'s Johnny! No, wait ... it\'s only Ted',
      'testBoolean' : true,
      'subCharacter' : 'sub char value'
    };
    
    /** Setting attributes on TestClass instance **/
    
    testClass.setTestInteger(values.testInteger);
    testClass.setTestLong(values.testLong);
    testClass.setTestFloat(values.testFloat);
    testClass.setTestDecimal(values.testDecimal);
    testClass.setTestDouble(values.testDouble);
    testClass.setTestCharacter(values.testCharacter); 
    testClass.setTestText(values.testText); 
    testClass.setTestBoolean(values.testBoolean); 
    testClass.setSubCharacter(values.subCharacter);
    
    /** Applying TestClass instance to the database **/
    
    var checkValues = function checkValues(testClass)
    {
      g_testClass = testClass;
      
      /** Getting attributes from TestClass instance **/
      
      var returned = {
        'testInteger' : testClass.getTestInteger(),
        'testLong' : testClass.getTestLong(),
        'testFloat' : testClass.getTestFloat(),
        'testDecimal' : testClass.getTestDecimal(),
        'testDouble' : testClass.getTestDouble(),
        'testCharacter' : testClass.getTestCharacter(),
        'testText' : testClass.getTestText(),
        'subCharacter' : testClass.getSubCharacter()
      };
      
      yuiTest.resume(function(){
        for(i in returned)
        {
          Y.Assert.areEqual(values[i], returned[i], "The returned value did not match the applied value.");
        }
      });
    };

    testClass.apply(new CallbackHandler(yuiTest, {
      onSuccess : checkValues
    }));
    
    this.wait(TIMEOUT);
  },
  
  testMethods: function(){
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        Y.log("Invoking an instance method", "debug");
        
        var testClass = new com.runwaysdk.jstest.SubClass();
        
        testClass.createInstances(new CallbackHandler(yuiTest), 5);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, retObjs, testClass){
        
        Y.log("Running some instanceof tests and applying.", "debug");
        
        if (retObjs instanceof Array && retObjs.length == 5) {
          for (var i = 0; i < retObjs.length; i++) {
            if (!(retObjs[i] instanceof com.runwaysdk.jstest.TestClass)) {
              yuiTest.resume(function(){
                Y.Assert.fail("retObjs[" + i + "] instanceof TestClass failed.");
              });
            }
          }
        }
        else {
          yuiTest.resume(function(){
            Y.assert(retObjs instanceof Array, "retObjs is not an instanceof Array.");
            Y.Assert.areEqual(5, retObjs.length, "retObjs.length did not equal its expected value.");
          });
        }
        
        testClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testClass2){
        
        Y.log("Invoking the static version of an instance method", "debug");
            
        com.runwaysdk.jstest.SubClass.createInstances(new CallbackHandler(yuiTest), testClass2.getId(), 4);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, retObjs, callingObj){
      
        Y.log("Running some tests and invoking a static method", "debug");
      
        if (retObjs instanceof Array && retObjs.length == 4) {
          for (var i = 0; i < retObjs.length; i++) {
            if (!(retObjs[i] instanceof com.runwaysdk.jstest.TestClass)) {
              yuiTest.resume(function(){
                Y.Assert.fail("retObjs[" + i + "] instanceof TestClass failed.");
              });
            }
          }
        }
        else {
          yuiTest.resume(function(){
            Y.assert(retObjs instanceof Array, "retObjs is not an instanceof Array.");
            Y.Assert.areEqual(4, retObjs.length, "retObjs.length did not equal its expected value.");
          });
        }
        
        com.runwaysdk.jstest.SubClass.doubleAnInteger(new CallbackHandler(yuiTest), 10);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, retVal){
      
        if (retVal == 20) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual(20, retVal, "SubClass.doubleAnInteger did not return the expected value.");
          });
        }
        
        Y.log("Invoking an instance method with both the parameter and return as nulls", "debug");
        var testClass = new com.runwaysdk.jstest.SubClass();
        
        testClass.createInstances(new CallbackHandler(yuiTest), null);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, retObjects){
        
        yuiTest.resume(function(){
          Y.Assert.areEqual(null, retObjects, "SubClass.createInstances did not return the expected value.");
        });
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },
  
  testDefaultValues: function()
  {
    var subClass = new com.runwaysdk.jstest.SubClass();
    
    Y.Assert.areEqual("Yo diggity", subClass.getTestCharacter(), "subClass.getTestCharacter() did not return the expected value.");
    Y.Assert.areEqual("", subClass.getTestText(), "subClass.getTestText() did not return the expected value.");
    Y.Assert.areEqual(com.runwaysdk.jstest.States.CO, subClass.getSingleState()[0], "subClass.getSingleState()[0] did not return the expected value.");
    Y.Assert.areEqual(true, subClass.isNewInstance(), "subClass.isNewInstance() did not return the expected value.");
    Y.Assert.areEqual("Sub char", subClass.getSubCharacter(), "subClass.getSubCharacter() did not return the expected value.");
  },
  
  testPersist : function()
  {
    Y.log("Creating a new instance of SubClass and applying.", "debug");
  
    var yuiTest = this;
  
    var handler = new CallbackHandler(yuiTest, {
      onSuccess : function(applied){
          
          var id = applied.getId();
          
          Y.log("Getting the applied instance of SubClass.", "debug");
          
          // now fetch the object
          com.runwaysdk.jstest.TestClass.get(new CallbackHandler(yuiTest, {
          
            onSuccess : function(fetched){
            
              Y.log("Checking the ids to look for a match.", "debug");
              
              // make sure it's the same object as before
              yuiTest.resume(function(){
                Y.Assert.isTrue(applied instanceof com.runwaysdk.jstest.TestClass);
                Y.Assert.areEqual(id, fetched.getId());
              });
            }
          }), id);
      }
    });
    
    // apply a new instance of TestClass
    var subClass = new com.runwaysdk.jstest.SubClass();
    subClass.apply(handler);
    
    yuiTest.wait(TIMEOUT);
  },
  
  testDelete : function()
  {
    Y.log("Creating a new instance of TestClass and applying.", "debug");
    
    var yuiTest = this;
    
    // Creating a new instance of SubClass and applying
    var testClass = new com.runwaysdk.jstest.SubClass();
    
    var cb = function(testClass) // lock callback
    {
      var cb2 = function(testClass2) // get instance callback
      {
        var cb3 = function() // delete callback
        {
          var id = testClass2.getId();
          
          var cb4_S = function()
          {
            yuiTest.resume(function(){
              Y.Assert.fail("Attempting to get a deleted instance did not fail when it should have.");
            });
          };
          
          var cb4_F = function(e)
          {
            yuiTest.resume();
          };
          
          Y.log("Focing error by getting a deleted instance.", "debug");
          
          com.runwaysdk.jstest.SubClass.get(new CallbackHandler(yuiTest, {onSuccess: cb4_S , onFailure : cb4_F}), id);
        };
          
        Y.log("Deleting the instance.", "debug");
        var handler = new CallbackHandler(yuiTest, {
          onSuccess : cb3
        });   
        testClass.remove(handler);
      };
        
      Y.log("Locking the instance.", "debug");
      var handler = new CallbackHandler(yuiTest, {
        onSuccess : cb2
      }); 
      testClass.lock(handler);
    };
      
    var handler = new CallbackHandler(yuiTest, {
      onSuccess : cb
    });
    testClass.apply(handler);
      
    yuiTest.wait(TIMEOUT);
  },
  
  testStruct : function ()
  {
    var yuiTest = this;
    
    var testClass = new com.runwaysdk.jstest.SubClass();

    var cell = testClass.getCellPhone();
    // remove apply, new instance, ... etc from BusinessDTO if containing a Struct
    cell.setAreaCode(303)
    cell.setPrefix(979);
    cell.setSuffix(5445);
    cell.setExtension(976);
    
    var cb = function(testClass){
      var cell = testClass.getCellPhone();
      
      var resumeFunc = function () {
        Y.Assert.areEqual(303, cell.getAreaCode(), "The returned area code did not match the expected value.");
        Y.Assert.areEqual(979, cell.getPrefix(), "The returned prefix did not match the expected value.");
        Y.Assert.areEqual(5445, cell.getSuffix(), "The returned suffix did not match the expected value.");
        Y.Assert.areEqual(976, cell.getExtension(), "The returned extension did not match the expected value.");
      }
      
      yuiTest.resume(resumeFunc);
    }
    
    testClass.apply(new CallbackHandler(yuiTest, {onSuccess: cb}));
    
    yuiTest.wait(TIMEOUT);
  },

  testEnumeration :function ()
  {
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        com.runwaysdk.jstest.States.CO.item(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, co){
      
        var enumStruct = co.getStatePhone();
        if (!(enumStruct instanceof com.runwaysdk.system.PhoneNumber)) {
          yuiTest.resume(function(){
            Y.Assert.fail("The returned enumeration from getStatePhone did not pass the instanceof (com.runwaysdk.system.PhoneNumber) test.");
          });
        }
        
        Y.log("Testing default values and applying the SubClass instance.", "debug");
        
        var testClass = new com.runwaysdk.jstest.SubClass();
        
        var singleVals = testClass.getSingleState(); // should have CO item
        if (!(singleVals instanceof Array && singleVals.length == 1 && singleVals[0] == com.runwaysdk.jstest.States.CO)) {
          yuiTest.resume(function(){
            Y.assert(singleVals instanceof Array, "getSingleState did not return an array.");
            Y.Assert.areEqual(1, singleVals.length, "The size of the returned array from getSingleState did not match the expected value.");
            Y.assert(singleVals[0] == com.runwaysdk.jstest.States.CO, "The returned state did not match the expected value (com.runwaysdk.jstest.States.CO).");
          });
        }
        
        var multipleVals = testClass.getMultipleState(); // should be empty
        if (!(multipleVals instanceof Array && multipleVals.length == 0)) {
          yuiTest.resume(function(){
            Y.assert(multipleVals instanceof Array, "getMultipleState did not return an array.");
            Y.Assert.areEqual(0, singleVals.length, "The size of the returned array from getMultipleState did not match the expected value.");
          });
        }
        
        testClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testClass){
        
        Y.log("Locking the SubClass instance.", "debug");
        testClass.lock(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testClass){
      
        Y.log("Populating SubClass enumerations with data and applying.", "debug");
        
        testClass.addSingleState(com.runwaysdk.jstest.States.CA);
        testClass.addMultipleState(com.runwaysdk.jstest.States.CT);
        testClass.addMultipleState(com.runwaysdk.jstest.States.CO);
        
        testClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testClass){
      
        Y.log("Testing values of SubClass enumeration", "debug");
        
        var singleVals = testClass.getSingleState();
        var multipleVals = testClass.getMultipleState();
        if (singleVals[0] == com.runwaysdk.jstest.States.CA &&
        (multipleVals[0] == com.runwaysdk.jstest.States.CT || multipleVals[0] == com.runwaysdk.jstest.States.CO) &&
        (multipleVals[1] == com.runwaysdk.jstest.States.CT || multipleVals[1] == com.runwaysdk.jstest.States.CO) &&
        multipleVals[0] != multipleVals[1]) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual(singleVals[0], com.runwaysdk.jstest.States.CA, "SubClass.getSingleState()[0] is not the expected value.");
            Y.assert(multipleVals[0] == com.runwaysdk.jstest.States.CT || multipleVals[0] == com.runwaysdk.jstest.States.CO, "SubClass.getMultipleState()[0] = '" + multipleVals[0] + "' is not the expected value.");
            Y.Assert.areNotEqual(multipleVals[0], multipleVals[1], "These two values cannot be the same.");
          });
        }
        
        Y.log("Testing the remove operation on an enumeration", "debug");
        
        testClass.removeMultipleState(com.runwaysdk.jstest.States.CT);
        var multipleState = testClass.getMultipleState();
        if (multipleState instanceof Array && multipleState.length == 1 && multipleState[0] == com.runwaysdk.jstest.States.CO) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.assert(multipleState instanceof Array, "multipleState is not an instanceof Array");
            Y.Assert.areEqual(1, multipleState.length, "multipleState.length did not match the expected value.");
            Y.Assert.areEqual(com.runwaysdk.jstest.States.CO, multipleState[0], "multipleState[0] did not match the expected value");
          });
        }
        
        
        Y.log("Testing the clear operation on an enumeration.", "debug");
        
        // add another item to the multiple select to make sure clear can remove more than one item
        testClass.addMultipleState(com.runwaysdk.jstest.States.CA);
        testClass.clearMultipleState();
        var multipleState = testClass.getMultipleState();
        if (multipleState instanceof Array && multipleState.length == 0) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.assert(multipleState instanceof Array, "multipleState is not an instance of array");
            Y.Assert.areEqual(0, multipleState.length, "multipleState.length did not match the expected value.");
          });
        }
        
        yuiTest.resume(); // Pass this test!
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },
  
  testMetadata : function ()
  {
    var yuiTest = this;
    
    Y.log("Testing the metadata for the SubClass type", "debug");
    var instance = new com.runwaysdk.jstest.SubClass();
    
    var displayLabel = instance.getMd().getDisplayLabel();
    var description = instance.getMd().getDescription();
    var id = instance.getMd().getId();
    
    Y.Assert.areEqual("A subclass", displayLabel, "The display label did not match the expected value.");
    Y.Assert.areEqual("ajax test SubClass", description, "The description did not match the expected value.");
    Y.Assert.areEqual(g_subClassMdId, id, "The id did not match the expected value.");
    
    Y.log("Testing the metadata for an integer", "debug");
    var str = "Integer";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("An " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(true, instance["getTest" + str + "Md"]().rejectZero(), "instance.getTest" + str + "Md().rejectZero() did not match the expected value.");
    Y.Assert.areEqual(true, instance["getTest" + str + "Md"]().rejectNegative(), "instance.getTest" + str + "Md().rejectNegative() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectPositive(), "instance.getTest" + str + "Md().rejectPositive() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a long", "debug");
    str = "Long";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectZero(), "instance.getTest" + str + "Md().rejectZero() did not match the expected value.");
    Y.Assert.areEqual(true, instance["getTest" + str + "Md"]().rejectNegative(), "instance.getTest" + str + "Md().rejectNegative() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectPositive(), "instance.getTest" + str + "Md().rejectPositive() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a float", "debug");
    str = "Float";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(true, instance["getTest" + str + "Md"]().rejectZero(), "instance.getTest" + str + "Md().rejectZero() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectNegative(), "instance.getTest" + str + "Md().rejectNegative() did not match the expected value.");
    Y.Assert.areEqual(true, instance["getTest" + str + "Md"]().rejectPositive(), "instance.getTest" + str + "Md().rejectPositive() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual(10, instance["getTest" + str + "Md"]().getTotalLength(), "instance.getTest" + str + "Md().getTotalLength() did not match the expected value.");
    Y.Assert.areEqual(2, instance["getTest" + str + "Md"]().getDecimalLength(), "instance.getTest" + str + "Md().getDecimalLength() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a decimal", "debug");
    str = "Decimal";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectZero(), "instance.getTest" + str + "Md().rejectZero() did not match the expected value.");
    Y.Assert.areEqual(true, instance["getTest" + str + "Md"]().rejectNegative(), "instance.getTest" + str + "Md().rejectNegative() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectPositive(), "instance.getTest" + str + "Md().rejectPositive() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual(13, instance["getTest" + str + "Md"]().getTotalLength(), "instance.getTest" + str + "Md().getTotalLength() did not match the expected value.");
    Y.Assert.areEqual(3, instance["getTest" + str + "Md"]().getDecimalLength(), "instance.getTest" + str + "Md().getDecimalLength() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
      
    Y.log("Testing the metadata for a double", "debug");
    str = "Double";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectZero(), "instance.getTest" + str + "Md().rejectZero() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectNegative(), "instance.getTest" + str + "Md().rejectNegative() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().rejectPositive(), "instance.getTest" + str + "Md().rejectPositive() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual(16, instance["getTest" + str + "Md"]().getTotalLength(), "instance.getTest" + str + "Md().getTotalLength() did not match the expected value.");
    Y.Assert.areEqual(4, instance["getTest" + str + "Md"]().getDecimalLength(), "instance.getTest" + str + "Md().getDecimalLength() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a date time", "debug");
    str = "DateTime";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a date", "debug");
    str = "Date";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a time", "debug");
    str = "Time";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a boolean", "debug");
    str = "Boolean";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a character", "debug");
    str = "Character";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(16, instance["getTest" + str + "Md"]().getSize(), "instance.getTest" + str + "Md().getSize() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a text", "debug");
    str = "Text";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a reference object", "debug");
    str = "ReferenceObject";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a struct", "debug");
    if(instance.getHomePhoneMd().getDisplayLabel() == 'Test Struct' &&
      instance.getHomePhoneMd().getDescription() == 'A Struct' &&
      instance.getHomePhoneMd().getDefiningMdStruct() == new com.runwaysdk.system.PhoneNumber().getType() &&
      instance.getHomePhoneMd().isImmutable() == false &&
      instance.getHomePhoneMd().isRequired() == false &&
      instance.getHomePhoneMd().getName() == 'homePhone')
      {
        // Success
      }
    else
    {
      Y.Assert.fail("The metadata for a struct is incorrect.");
    }
    
    Y.log("Testing the metadata for an enumeration", "debug");

    var coName = com.runwaysdk.jstest.States.CO.name();
    var ctName = com.runwaysdk.jstest.States.CT.name();
    var caName = com.runwaysdk.jstest.States.CA.name();
    
    var ssValues = instance.getSingleStateMd().getEnumNames();
    
    if(instance.getSingleStateMd().getDisplayLabel() == 'Test Enumeration' &&
      instance.getSingleStateMd().getDescription() == 'An Enumeration' &&
      instance.getSingleStateMd().selectMultiple() == false &&
      instance.getSingleStateMd().isImmutable() == false &&
      instance.getSingleStateMd().isRequired() == true &&
      /* TODO check display label
      ssValues[coName] === coName &&
      ssValues[ctName] === ctName &&
      ssValues[caName] === caName &&
      */
      instance.getSingleStateMd().getName() == 'singleState')
      {
        // Success
      }
    else
    {
      Y.Assert.fail("The metadata for an enumeration is incorrect.");
    }
    
    Y.log("Testing the metadata for a hash", "debug");
    str = "Hash";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual("MD5", instance["getTest" + str + "Md"]().getEncryptionMethod(), "instance.getTest" + str + "Md().getEncryptionMethod() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
    
    Y.log("Testing the metadata for a symmetric", "debug");
    if(instance.getTestSymmetricMd().getDisplayLabel() == 'Test Symmetric' &&
      instance.getTestSymmetricMd().getDescription() == 'A Symmetric' &&
      instance.getTestSymmetricMd().getEncryptionMethod() == 'DES/CBC/PKCS5Padding' &&
      instance.getTestSymmetricMd().isImmutable() == false &&
      instance.getTestSymmetricMd().isRequired() == false &&
      instance.getTestSymmetricMd().getName() == 'testSymmetric')
      {
        // Success
      }
    else
    {
      Y.Assert.fail("The metadata for a hash is incorrect.");
    }
    str = "Symmetric";
    Y.Assert.areEqual("Test " + str, instance["getTest" + str + "Md"]().getDisplayLabel(), "instance.getTest" + str + "Md().getDisplayLabel() did not match the expected value.");
    Y.Assert.areEqual("A " + str, instance["getTest" + str + "Md"]().getDescription(), "instance.getTest" + str + "Md().getDescription() did not match the expected value.");
    Y.Assert.areEqual("DES/CBC/PKCS5Padding", instance["getTest" + str + "Md"]().getEncryptionMethod(), "instance.getTest" + str + "Md().getEncryptionMethod() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isImmutable(), "instance.getTest" + str + "Md().isImmutable() did not match the expected value.");
    Y.Assert.areEqual(false, instance["getTest" + str + "Md"]().isRequired(), "instance.getTest" + str + "Md().isRequired() did not match the expected value.");
    Y.Assert.areEqual("test" + str, instance["getTest" + str + "Md"]().getName(), "instance.getTest" + str + "Md().getName() did not match the expected value.");
  },
  
  testReference : function ()
  {
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq /* TaskQueue */){
        
        Y.log("Getting the ref object.", "debug");
        
        var testClass = new com.runwaysdk.jstest.SubClass();
        
        // don't persist this SubClass instance
        testClass.getTestReferenceObject(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, refObj){
        
        Y.log("Making sure the ref object is null and creating a new instance of the referenced object.", "debug");
        
        if (refObj != null) {
          yuiTest.resume(function(){
            Y.Assert.fail("The default reference object was expected to be null but it wasn't.");
          });
        }
        
        var refClass = new com.runwaysdk.jstest.RefClass();
        refClass.setRefChar("some value"); // we'll check this later
        refClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, refClass){
        
        Y.log("Setting the reference object on a SubClass instance and applying.", "debug");
        
        var testClass = new com.runwaysdk.jstest.SubClass();
        testClass.setTestReferenceObject(refClass);
        testClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testClass){
        
        Y.log("Getting the reference object from a SubClass instance.", "debug");
        
        testClass.getTestReferenceObject(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, refClass){
        
        Y.log("Checking the value of the reference.", "debug");
        
        if (refClass.getRefChar() != 'some value') 
          referencePassed = false;
        
        yuiTest.resume(function(){
          Y.Assert.areEqual('some value', refClass.getRefChar(), "The returned value on the reference object does not match the applied value.");
        });
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },
  
  testType : function ()
  {
    var yuiTest = this;
    
    Y.log("Getting an instance of SubClass with its superclass (TestClass) API", "debug");
    
    // testSubclassType
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
       Mojo.$.com.runwaysdk.jstest.TestClass.get(new CallbackHandler(yuiTest), g_subclassId)
      }
    }));
    
    // testSubclassType ... cb
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, subclass){
      
        if (subclass instanceof Mojo.$.com.runwaysdk.jstest.SubClass &&
        subclass.getId() == g_subclassId) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("TestClass.get did not return the expected subclass.");
          });
        }
        
        Y.log("Invoking a facade method (param: int, return: SubClass[])", "debug");
        
        com.runwaysdk.jstest.Summation.getNullInteger(new CallbackHandler(yuiTest), null);
      }
    }));
    
    // testSubclassFacade
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      
        var objArr = new Array();
        for (var i = 1; i < 11; i++) {
          var obj = new com.runwaysdk.jstest.SubClass();
          obj.setTestInteger(i);
          objArr.push(obj);
        }
        
        com.runwaysdk.jstest.Summation.sumIntegerValues(new CallbackHandler(yuiTest), objArr);
      }
    }));
    
    // testSubclassFacade ... cb
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, retVal){
        
        yuiTest.resume(function(){
          Y.Assert.areEqual(55, retVal, "sumIntegerValues did not return the expected value.");
        });
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },
  
});

TestFramework.newTestCase(SUITE_NAME, {

  name : 'QueryTests',
  
  cb : null,
  
  setUp : function ()
  {
    // remove all previously defined tasks and set all values to defaults
    g_taskQueue = new struct.TaskQueue();
  },
  
  tearDown : function()
  {
    g_taskQueue = null;
  },
  
  testBusinessQueryDTO : function ()
  {
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        Y.log("Getting a QueryDTO to query the type [com.runwaysdk.jstest.TestClass]", "debug");
        
        com.runwaysdk.Facade.getQuery(new CallbackHandler(yuiTest), 'com.runwaysdk.jstest.TestClass');
      }
    }));
    
    // cb
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, query){
      
        if (query instanceof DTO.BusinessQueryDTO && query.getType() == 'com.runwaysdk.jstest.TestClass') {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("Facade.getQuery did not return a query of the expected type.");
          });
        }
        
        Y.log("Testing a sample of the defined attributes.", "debug")
        
        Y.log("Testing the defined attribute [testCharacter]", "debug");
        
        // test some attributes on the QueryDTO
        var attributeDTO = query.getAttributeDTO('testCharacter');
        if (attributeDTO.getName() == 'testCharacter' &&
        attributeDTO.getValue() == 'Yo diggity' &&
        attributeDTO.isReadable() == true &&
        attributeDTO.getAttributeMdDTO().getSize() == 16 &&
        attributeDTO.getAttributeMdDTO().isImmutable() == false &&
        attributeDTO.getAttributeMdDTO().isRequired() == false) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The attribute testCharacter is incorrect.");
          });
        }
        
        
        Y.log("Testing the defined attribute [testDouble]", "debug");
        var attributeDTO2 = query.getAttributeDTO('testDouble');
        if (attributeDTO2.getName() == 'testDouble' &&
        attributeDTO2.getValue() == null &&
        attributeDTO2.isReadable() == true &&
        attributeDTO2.getAttributeMdDTO().getDisplayLabel() == 'Test Double' &&
        attributeDTO2.getAttributeMdDTO().getDescription() == 'A Double' &&
        attributeDTO2.getAttributeMdDTO().rejectZero() == false &&
        attributeDTO2.getAttributeMdDTO().rejectNegative() == false &&
        attributeDTO2.getAttributeMdDTO().rejectPositive() == false &&
        attributeDTO2.getAttributeMdDTO().isImmutable() == false &&
        attributeDTO2.getAttributeMdDTO().isRequired() == false &&
        attributeDTO2.getAttributeMdDTO().getTotalLength() == 16 &&
        attributeDTO2.getAttributeMdDTO().getDecimalLength() == 4) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The attribute testDouble is incorrect.");
          });
        }
        
        
        Y.log("Testing the defined attribute [singleState]", "debug");
        var attributeDTO3 = query.getAttributeDTO('singleState');
        
        var coName = com.runwaysdk.jstest.States.CO.name();
        var ctName = com.runwaysdk.jstest.States.CT.name();
        var caName = com.runwaysdk.jstest.States.CA.name();
        
        var ssValues = attributeDTO3.getAttributeMdDTO().getEnumNames();
        
        if (attributeDTO3.getName() == 'singleState' &&
        attributeDTO3.isReadable() == true &&
        attributeDTO3.getAttributeMdDTO().getDisplayLabel() == 'Test Enumeration' &&
        attributeDTO3.getAttributeMdDTO().getDescription() == 'An Enumeration' &&
        attributeDTO3.getAttributeMdDTO().selectMultiple() == false &&
        attributeDTO3.getAttributeMdDTO().isImmutable() == false &&
        attributeDTO3.getAttributeMdDTO().isRequired() == true  /* TODO test display label
   && ssValues[coName] == coName &&
   ssValues[ctName] == ctName &&
   ssValues[caName] == caName*/
        ) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The attribute singleState is incorrect.");
          });
        }
        
        Y.log("Testing the MdRelationship information on the BusinessQueryDTO", "debug");
        
        var asParentList = query.getTypeInMdRelationshipAsParentList();
        if (asParentList.length == 1 &&
        asParentList[0].getParentDisplayLabel() == "testclass parent" &&
        asParentList[0].getRelationshipType() == "com.runwaysdk.jstest.Befriends") {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The MdRelationship information is incorrect.");
          });
        }
        
        Y.log("Testing the isAbstract value", "debug")
        if (!query.isAbstract()) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The isAbstract value is incorrect.");
          });
        }
        
        Y.log("Adding conditions, order bys, and enabling count on the BusinessQueryDTO", "debug");
        query.setCountEnabled(true);
        query.setPageSize(3);
        query.setPageNumber(2);
        query.addCondition('testCharacter', 'EQ', 'queryMe!!!');
        query.addCondition('testLong', 'EQ', 1234567890);
        query.addOrderBy('testInteger', 'desc');
        
        com.runwaysdk.Facade.queryBusinesses(new CallbackHandler(yuiTest), query);
      }
    }));
    
    // cb2
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, query2){
      
        Y.log('Testing properties on the returned BusinessQueryDTO', "debug");
        if (query2.isCountEnabled() == true &&
        query2.getCount() >= 10 &&
        query2.getPageNumber() == 2 &&
        query2.getPageSize() == 3) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The properties on the returned BuisnessQueryDTO are incorrect.");
          });
        }
        
        Y.log('Testing result set', "debug");
        var resultSet = query2.getResultSet();
        if (resultSet.length == 3) {
          var resultsPassed = true;
          var currentInt = null;
          for (var i = 0; i < resultSet.length; i++) {
            var result = resultSet[i];
            if (result instanceof com.runwaysdk.jstest.TestClass) {
              if (result.getTestCharacter() != 'queryMe!!!' ||
              result.getTestLong() != 1234567890) {
                resultsPassed = false;
                break;
              }
              
              // check for proper ordering
              if (i == 0) 
                currentInt = result.getTestInteger();
              else {
                if (currentInt < result.getTestInteger()) {
                  resultsPassed = false;
                  break;
                }
                else {
                  currentInt = result.getTestInteger();
                }
              }
            }
            else {
              resultsPassed = true;
              break;
            }
          }
          
          if (resultsPassed) {
            // Success
          }
          else {
            yuiTest.resume(function(){
              Y.Assert.fail("The result set is incorrect.");
            });
          }
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The result set does not have the expected length.");
          });
        }
        
        yuiTest.resume(); // Pass this test!
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },

  testRelationshipQueryDTO : function ()
  {
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        Y.log("Getting a QueryDTO to query the type [com.runwaysdk.jstest.Befriends]", "debug");
        
        com.runwaysdk.Facade.getQuery(new CallbackHandler(yuiTest), 'com.runwaysdk.jstest.Befriends');
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, query){
      
        if (query instanceof DTO.RelationshipQueryDTO && query.getType() == 'com.runwaysdk.jstest.Befriends') {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("Facade.getQuery did not return a query of the expected type.");
          });
        }
        
        Y.log("Testing a sample of the defined attributes.", "debug")
        
        Y.log("Testing the defined attribute [relChar]", "debug");
        
        // test some attributes on the QueryDTO
        var attributeDTO = query.getAttributeDTO('relChar');
        if (attributeDTO.getName() == 'relChar' &&
        attributeDTO.getValue() == "I'm a rel char" &&
        attributeDTO.isReadable() == true &&
        attributeDTO.getAttributeMdDTO().getSize() == 16 &&
        attributeDTO.getAttributeMdDTO().isImmutable() == false &&
        attributeDTO.getAttributeMdDTO().isRequired() == false) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The attribute relChar is incorrect.");
          });
        }
        
        Y.log('Testing the properties on the RelationshipQueryDTO', "debug");
        if (query.getParentMdBusiness() == 'com.runwaysdk.jstest.TestClass' &&
        query.getChildMdBusiness() == 'com.runwaysdk.jstest.RefClass') {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The properties on the RelationshipQueryDTO are incorrect.");
          });
        }
        
        Y.log("Adding conditions, order bys, and enabling count on the RelationshipQueryDTO", "debug");
        query.setCountEnabled(true);
        query.setPageSize(3);
        query.setPageNumber(2);
        query.addCondition('relChar', 'EQ', 'queryMe!!!');
        
        com.runwaysdk.Facade.queryRelationships(new CallbackHandler(yuiTest), query);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, query2){
      
        Y.log('Testing properties on the returned RelationshipQueryDTO', "debug");
        if (query2.isCountEnabled() == true &&
        query2.getCount() >= 10 &&
        query2.getPageNumber() == 2 &&
        query2.getPageSize() == 3) {
        // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The properties on the returned RelationshipQueryDTO are incorrect.");
          });
        }
        
        Y.log('Testing result set', "debug");
        var resultSet = query2.getResultSet();
        if (resultSet.length == 3) {
          var resultsPassed = true;
          var currentInt = null;
          for (var i = 0; i < resultSet.length; i++) {
            var result = resultSet[i];
            if (result instanceof com.runwaysdk.jstest.Befriends) {
              if (result.getRelChar() != 'queryMe!!!') {
                resultsPassed = false;
                break;
              }
            }
            else {
              resultsPassed = true;
              break;
            }
          }
          
          if (resultsPassed) {
          // Success
          }
          else {
            yuiTest.resume(function(){
              Y.Assert.fail("The result set is incorrect.");
            });
          }
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The result set length is incorrect.");
          });
        }
        
        yuiTest.resume(); // Pass this test!
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },

  testValueQueryDTO : function ()
  {
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      
        Y.log('Querying the server with a groovy string', "debug");
        
        var groovyQuery = "def testClass = new com.runwaysdk.jstest.TestClassQuery(f);";
        groovyQuery += "q.SELECT testClass.getTestCharacter(\"testCharacter\"), testClass.getTestLong(\"testLong\");";
        groovyQuery += "q.WHERE testClass.getTestCharacter(\"testCharacter\").EQ(\"queryMe!!!\");";
        groovyQuery += "q.WHERE testClass.getTestLong(\"testLong\").EQ(1234567890);";
        
        var query = new DTO.ValueQueryDTO(groovyQuery);
        query.setCountEnabled(true);
        query.setPageNumber(1);
        query.setPageSize(3);
        
        com.runwaysdk.Facade.groovyValueQuery(new CallbackHandler(yuiTest), query);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, query){
      
        Y.log('Checking the basic properties on the ValueQueryDTO', "debug");
        if (query.isCountEnabled() == true &&
        query.getCount() >= 10 &&
        query.getPageNumber() == 1 &&
        query.getPageSize() == 3) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The properties on the ValueQueryDTO are incorrect.");
          });
        }
        
        Y.log('Checking the defined attributes on the ValueQueryDTO', "debug");
        var definedAttributes = query.getAttributeNames();
        if (definedAttributes.length == 2) {
          var foundChar = false;
          var foundLong = false;
          definedAttributes.each(function(attribute){
          
            var attributeDTO = query.getAttributeDTO(attribute);
            
            if (attributeDTO.getName() == 'testCharacter') {
              foundChar = true;
            }
            else 
              if (attributeDTO.getName() == 'testLong') {
                foundLong = true;
              }
          });
          
          if (foundChar && foundLong) {
            // Success
          }
          else {
            yuiTest.resume(function(){
              Y.Assert.fail("The attributes on the ValueQueryDTO are incorrect.");
            });
          }
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The length of the defined attributes on the ValueQueryDTO is incorrect.");
          });
        }
        
        Y.log('Checking the result set', "debug");
        var resultSet = query.getResultSet();
        if (resultSet.length == 3) {
          var resultsPassed = true;
          for (var i = 0; i < resultSet.length; i++) {
            var result = resultSet[i];
            if (result instanceof ValueObjectDTO) {
              if (result.getAttributeDTO('testCharacter').getValue() != 'queryMe!!!' ||
              result.getAttributeDTO('testLong').getValue() != 1234567890) {
                resultsPassed = false;
                break;
              }
            }
            else {
              resultsPassed = true;
              break;
            }
          }
          
          if (resultsPassed) {
            // Success
          }
          else {
            yuiTest.resume(function(){
              Y.Assert.fail("The result set is incorrect.");
            });
          }
          
          yuiTest.resume(); // Pass this test!
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The length of the result set is incorrect.");
          });
        }
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },

  testStructQueryDTO : function ()
  {
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        Y.log("Getting a QueryDTO to query the type [com.runwaysdk.jstest.TestStruct]", "debug");
        
        com.runwaysdk.Facade.getQuery(new CallbackHandler(yuiTest), 'com.runwaysdk.jstest.TestStruct');
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, query){
      
        if (query instanceof DTO.StructQueryDTO && query.getType() == 'com.runwaysdk.jstest.TestStruct') {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("Facade.getQuery did not return a query of the expected type.");
          });
        }
        
        Y.log("Testing a sample of the defined attributes.", "debug")
        
        Y.log("Testing the defined attribute [structChar]", "debug");
        
        // test some attributes on the QueryDTO
        var attributeDTO = query.getAttributeDTO('structChar');
        if (attributeDTO.getName() == 'structChar' &&
        attributeDTO.getValue() == "A struct char" &&
        attributeDTO.isReadable() == true &&
        attributeDTO.getAttributeMdDTO().getSize() == 16 &&
        attributeDTO.getAttributeMdDTO().isImmutable() == false &&
        attributeDTO.getAttributeMdDTO().isRequired() == false) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The attribute structChar is incorrect.");
          });
        }
        
        Y.log("Adding conditions, order bys, and enabling count on the StructQueryDTO", "debug");
        query.setCountEnabled(true);
        query.setPageSize(3);
        query.setPageNumber(2);
        query.addCondition('structChar', 'EQ', 'queryMe!!!');
        
        com.runwaysdk.Facade.queryStructs(new CallbackHandler(yuiTest), query);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, query2){
      
        Y.log('Testing properties on the returned StructQueryDTO', "debug");
        if (query2.isCountEnabled() == true &&
        query2.getCount() >= 10 &&
        query2.getPageNumber() == 2 &&
        query2.getPageSize() == 3) {
        // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The properties on the returned StructQueryDTO are incorrect.");
          });
        }
        
        
        Y.log('Testing result set', "debug");
        var resultSet = query2.getResultSet();
        if (resultSet.length == 3) {
          var resultsPassed = true;
          var currentInt = null;
          for (var i = 0; i < resultSet.length; i++) {
            var result = resultSet[i];
            if (result instanceof com.runwaysdk.jstest.TestStruct) {
              if (result.getStructChar() != 'queryMe!!!') {
                resultsPassed = false;
                break;
              }
            }
            else {
              resultsPassed = true;
              break;
            }
          }
          
          if (resultsPassed) {
          // Success
          }
          else {
            yuiTest.resume(function(){
              Y.Assert.fail("The result set is incorrect.");
            });
          }
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The length of the result set is incorrect.");
          });
        }
        
        yuiTest.resume(); // Pass this test!
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },

});

TestFramework.newTestCase(SUITE_NAME, {

  name : 'Miscellaneous',
  
  cb : null,
  
  caseSetUp : function ()
  {
    
  },
  
  setUp : function ()
  {
    // remove all previously defined tasks and set all values to defaults
    g_taskQueue = new struct.TaskQueue();
  },
  
  tearDown : function()
  {
    g_taskQueue = null;
  },

  /**
   * Tests instances defined by an MdView
   */
  testView : function ()
  {
    var yuiTest = this;
    var newViewChar = null;
    var refId = null;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      
        Y.log("Creating a new instance of TestView", "debug");
        var testView = new com.runwaysdk.jstest.TestView();
        
        Y.log("Testing the character default value.", "debug");
        var viewChar = testView.getViewCharacter();
        if (viewChar == 'View char') {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual("View char", viewChar, "The default character value did not match the expected value.");
          });
        }
        
        Y.log("Testing the enumeration default value.", "debug");
        var singleVals = testView.getViewSingleState();
        if (singleVals instanceof Array && singleVals.length == 1 && singleVals[0] == com.runwaysdk.jstest.States.CO) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The default enumeration value did not match the expected value.");
          });
        }
        
        Y.log("Setting attribute values on the instance.", "debug");
        
        
        testView.setViewCharacter("Oh Noes!!!");
        newViewChar = testView.getViewCharacter();
        
        testView.addViewSingleState(com.runwaysdk.jstest.States.CA);
        var phone = testView.getViewPhone();
        phone.setPrefix(301);
        phone.setAreaCode(721);
        phone.setSuffix(4455);
        
        Y.log("Applying the instance to the database then checking the return values.", "debug");
        
        testView.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testView2){
      
        var viewChar2 = testView2.getViewCharacter();
        var phone2 = testView2.getViewPhone();
        if (newViewChar == viewChar2 &&
        testView2.getViewSingleState()[0] == com.runwaysdk.jstest.States.CA &&
        phone2.getPrefix() == 301 &&
        phone2.getAreaCode() == 721 &&
        phone2.getSuffix() == 4455) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The applied view did not match the expected value.");
          });
        }
        
        Y.log("Creating a new instance of RefClass (for reference attribute).", "debug");
        var refClass = new com.runwaysdk.jstest.RefClass();
        refClass.setRefChar("some value");
        
        refClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, refClass2){
      
        refId = refClass2.getId();
        
        Y.log("Adding instance of RefClass as Reference attribute.", "debug");
        var testViewWithRef = new com.runwaysdk.jstest.TestView();
        testViewWithRef.setViewReferenceObject(refClass2);
        
        testViewWithRef.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testViewWithRef2){
      
        testViewWithRef2.getViewReferenceObject(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, refClass3){
      
        if (refId == refClass3.getId() &&
        refClass3.getRefChar() == "some value") {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("getViewReferenceObject did not return the expected value.");
          });
        }
        
        yuiTest.resume(); // Pass this test!
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },

  /**
   * Tests instances defined by an MdUtil
   */
  testUtil : function ()
  {
    var yuiTest = this;
    var refId = null;
    var newUtilChar = null;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      
        Y.log("Creating a new instance of TestUtil.", "debug");
        var testUtil = new com.runwaysdk.jstest.TestUtil();
        
        Y.log("Testing the character default value.", "debug");
        var utilChar = testUtil.getUtilCharacter();
        if (utilChar == 'Util char') {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual("Util char", utilChar, "The default character does not match the expected value.");
          });
        }
        
        Y.log("Testing the enumeration default value.", "debug");
        var singleVals = testUtil.getUtilSingleState();
        if (singleVals instanceof Array && singleVals.length == 1 && singleVals[0] == com.runwaysdk.jstest.States.CO) {
          // Success 
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The default enumeration does not match the expected value.");
          });
        }
        
        Y.log("Setting attribute values on the instance.", "debug");
        
        
        testUtil.setUtilCharacter("Oh Noes!!!");
        newUtilChar = testUtil.getUtilCharacter();
        
        testUtil.addUtilSingleState(com.runwaysdk.jstest.States.CA);
        var phone = testUtil.getUtilPhone();
        phone.setPrefix(301);
        phone.setAreaCode(721);
        phone.setSuffix(4455);
        
        Y.log("Applying the instance to the database then checking the return values.", "debug");
        
        testUtil.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testUtil2){
      
        var utilChar2 = testUtil2.getUtilCharacter();
        var phone2 = testUtil2.getUtilPhone();
        if (newUtilChar == utilChar2 &&
        testUtil2.getUtilSingleState()[0] == com.runwaysdk.jstest.States.CA &&
        phone2.getPrefix() == 301 &&
        phone2.getAreaCode() == 721 &&
        phone2.getSuffix() == 4455) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.fail("The applied testUtil does not match the expected value.");
          });
        }
        
        Y.log("Creating a new instance of RefClass (for reference attribute).", "debug");
        var refClass = new com.runwaysdk.jstest.RefClass();
        refClass.setRefChar("some value");
        
        refClass.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, refClass2){
      
        refId = refClass2.getId();
        
        Y.log("Adding instance of RefClass as Reference attribute.", "debug");
        var testUtilWithRef = new com.runwaysdk.jstest.TestUtil();
        testUtilWithRef.setUtilReferenceObject(refClass2);
        
        testUtilWithRef.apply(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, testUtilWithRef2){
      
        testUtilWithRef2.getUtilReferenceObject(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, refClass3){
      
        yuiTest.resume(function(){
          Y.Assert.areEqual(refId, refClass3.getId(), "The returned reference id did not match the expected value.");
          Y.Assert.areEqual("some value", refClass3.getRefChar(), "The returned RefChar did not match the expected value.");
        });
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },

  /**
   * Tests MdMethods on a view.
   */
  testViewInvokeMethod : function ()
  {
    var yuiTest = this;
    var val = 5;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        Y.log("Invoking a static method on an instance of TestView", "debug");
        com.runwaysdk.jstest.TestView.doubleAnInt(new CallbackHandler(yuiTest), val);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, doubledInt){
      
        if (doubledInt == (2 * val)) {
          // Success
        }
        else {
          yuiTest.resume(function(){
            Y.Assert.areEqual(2*val, doubledInt, "doubleAnInt did not return the expected value.");
          });
        }
        
        Y.log("Invoking an instance method on an instance of TestView", "debug");
        var testView = new com.runwaysdk.jstest.TestView();
        var testView2 = new com.runwaysdk.jstest.TestView();
        
        testView.returnView(new CallbackHandler(yuiTest), testView2);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, returnedView){
      
        yuiTest.resume(function(){
          Y.Assert.areEqual("Returned!", returnedView.getViewCharacter(), "The returned View Character does not match the expected value.");
        });
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },

  testUtilInvokeMethod : function ()
  {
    var yuiTest = this;
    var val = 5;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        Y.log("Invoking a static method on an instance of TestUtil", "debug");
        
        com.runwaysdk.jstest.TestUtil.doubleAnInt(new CallbackHandler(yuiTest), val);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, doubledInt){
        
        if (doubledInt == (2 * val)) {
            // Success
          }
          else {
            yuiTest.resume(function(){
              Y.Assert.areEqual(2*val, doubledInt, "doubleAnInt did not return the expected value.");
            });
          }
          
          Y.log("Invoking an instance method on an instance of TestUtil", "debug");
          var testUtil = new com.runwaysdk.jstest.TestUtil();
          var testUtil2 = new com.runwaysdk.jstest.TestUtil();
          
          testUtil.returnUtil(new CallbackHandler(yuiTest), testUtil2);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, returnedUtil){
            
        yuiTest.resume(function(){
          Y.Assert.areEqual("Returned!", returnedUtil.getUtilCharacter(), "The returned Util Character does not match the expected value.");
        });
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },

  testAdminScreenAccess : function ()
  {
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        Y.log("Checking admin screen access as AllPerm", "debug");
        com.runwaysdk.Facade.checkAdminScreenAccess(new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        Y.log("Changing login to NO PERM.", "debug");
        com.runwaysdk.Facade.changeLogin(new CallbackHandler(yuiTest), g_noPermUser, g_noPermPass);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        var successCB = function(){
          var cb = function(){
            yuiTest.resume(function(){
              Y.Assert.fail("A user with no permissions was allowed to access the admin screen.");
            });
          }
          
          com.runwaysdk.Facade.changeLogin(cb, g_allPermUser, g_allPermPass);
        }
        
        Y.log("Checking admin screen access as NO PERM", "debug");
        com.runwaysdk.Facade.checkAdminScreenAccess(new CallbackHandler(yuiTest, {
          onSuccess: successCB,
          onFailure: CallbackHandler.next // We want this to fail
        }));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        Y.log("Changing login to AllPerm", "debug");
        com.runwaysdk.Facade.changeLogin(new CallbackHandler(yuiTest), g_allPermUser, g_allPermPass);
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        yuiTest.resume(); // Pass this test!
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },

  testException : function ()
  {
    var yuiTest = this;
    var testClass = new com.runwaysdk.jstest.TestClass();;
    
    var eNoMatch = function(){
      yuiTest.resume(function(){
        Y.Assert.fail("The thrown exception does not match the expected value.");
      });
    }
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
        
        Y.log('Catching an MdException from the server', "debug");
        
        testClass.instanceForceException(new CallbackHandler(yuiTest, {
          onSuccess: CallbackHandler.failSuccess,
          onFailure: CallbackHandler.next
        }));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, e){
      
        if (e instanceof com.runwaysdk.jstest.TestException &&
        e.getExChar() == 'Test exChar' &&
        e.getLocalizedMessage() == 'This is a message from TestException: 12345' &&
        e.getExInt() === 12345) {
          // Success
        }
        else {
          eNoMatch();
        }
        
        Y.log('Catching an MdException from the server using a custom handler', "debug");
        
        testClass.instanceForceException(new CallbackHandler(yuiTest, {
          onSuccess: CallbackHandler.failSuccess,
          onTestException: CallbackHandler.next
        }));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, e){
        
        if (e instanceof com.runwaysdk.jstest.TestException &&
        e.getExChar() == 'Test exChar' &&
        e.getLocalizedMessage() == 'This is a message from TestException: 12345' &&
        e.getExInt() === 12345) {
          // Success
        }
        else {
          eNoMatch();
        }
        
        // even without the definition, the custom handler should be called but with
        // a generic Exception
        
        Y.log('Catching an MdException from the server without an exception definition', "debug");
        
        // remove the TestException definition
        Mojo.Meta.dropClass('com.runwaysdk.jstest.TestException');
        
        testClass.instanceForceException(new CallbackHandler(yuiTest, {
          onSuccess: CallbackHandler.failSuccess,
          onTestException: CallbackHandler.next
        }));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, e){
      
        if (e instanceof com.runwaysdk.Exception &&
        e.getLocalizedMessage() == 'This is a message from TestException: 12345') {
          // Success
        }
        else {
          eNoMatch();
        }
        
        Y.log('Catching a hard-coded Exception from the server', "debug");
        
        testClass.instanceForceException1(new CallbackHandler(yuiTest, {
          onSuccess: CallbackHandler.failSuccess,
          onFailure: CallbackHandler.next
        }));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, e){
      
        if (e instanceof com.runwaysdk.RunwayExceptionDTO &&
        e.getWrappedException() == 'com.runwaysdk.session.InvalidLoginException' &&
        e.getLocalizedMessage() == 'Login failed - please try again.') {
          // Success
        }
        else {
          eNoMatch();
        }
        
        Y.log('Catching an Exception from the server', "debug");
        
        testClass.instanceForceException2(new CallbackHandler(yuiTest, {
          onSuccess: CallbackHandler.failSuccess,
          onFailure: CallbackHandler.next
        }));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, e){
      
        if (e instanceof com.runwaysdk.RunwayExceptionDTO &&
        e.getWrappedException() == 'com.runwaysdk.dataaccess.ProgrammingErrorException' &&
        e.getLocalizedMessage() == 'An unspecified error has occurred.  Please try your operation again.  If the problem continues, alert your technical support staff.') {
          // Success
        }
        else {
          eNoMatch();
        }
        
        Y.log('Catching an Exception from the server', "debug");
        
        testClass.instanceForceException3(new CallbackHandler(yuiTest, {
          onSuccess: CallbackHandler.failSuccess,
          onFailure: CallbackHandler.next
        }));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, e){
      
        if (e instanceof com.runwaysdk.RunwayExceptionDTO &&
        e.getWrappedException() == 'com.runwaysdk.dataaccess.ProgrammingErrorException' &&
        e.getLocalizedMessage() == 'An unspecified error has occurred.  Please try your operation again.  If the problem continues, alert your technical support staff.') {
          // Success
        }
        else {
          eNoMatch();
        }
        
        Y.log('Removing the com.runwaysdk.system.PhoneNumber Import', "debug");
        
        Mojo.Meta.dropClass('com.runwaysdk.system.PhoneNumber');
        
        try {
          Y.log('Attempting to erroneously access a PhoneNumber as a struct', "debug");
          var c = testClass.getCellPhone();
          
          // should not get this far ... above call should have thrown exception
          yuiTest.resume(function(){
           Y.Assert.fail("A javascript exception was not thrown when it should have been.");
          });
        }
        catch (jsEx) {
          if (jsEx instanceof com.runwaysdk.Exception) {
            // Success
          }
          else {
            eNoMatch();
          }
        }
        finally {
          Y.log('Adding the com.runwaysdk.system.PhoneNumber Import', "debug");
          
          com.runwaysdk.Facade.importTypes(new CallbackHandler(yuiTest), 'com.runwaysdk.system.PhoneNumber', {
            autoEval: true
          });
        }
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },
  
  testProblem : function ()
  {
    var yuiTest = this;
    var testClass = new com.runwaysdk.jstest.TestClass();
    
    var pNoMatch = function(){
      yuiTest.resume(function(){
        Y.Assert.fail("The thrown problem does not match the expected value.");
      });
    }
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      
        Y.log('Testing a type-safe Problem', "debug");
        
        testClass.instanceForceProblem(new CallbackHandler(yuiTest, {
          onSuccess: CallbackHandler.failSuccess,
          onProblemExceptionDTO: CallbackHandler.next
        }));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, request){
      
        var problem = request.getProblems()[0];
        if (problem instanceof com.runwaysdk.jstest.TestProblem &&
        problem.getProblemChar() == 'Test problemChar' &&
        problem.getLocalizedMessage() == 'This is a message from TestProblem: 12345') {
          // Success
        }
        else {
          pNoMatch();
        }
        
        Y.log('Testing multiple type-safe Problems', "debug");
        testClass.instanceForceMultipleProblems(new CallbackHandler(yuiTest, {
          onSuccess: CallbackHandler.failSuccess,
          onProblemExceptionDTO: CallbackHandler.next
        }));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, request){
      
        var problems = request.getProblems();
        if (problems.length == 2 &&
        problems[0] instanceof com.runwaysdk.jstest.TestProblem &&
        problems[1] instanceof com.runwaysdk.jstest.TestProblem &&
        problems[0].getProblemInt() + problems[1].getProblemInt() === 66666) {
          // Success
        }
        else {
          pNoMatch();
        }
        
        Y.log('Testing a generic Problem', "debug");
        
        // force a problem but don't have the type definition. We should receive a generic ProblemDTO
        Mojo.Meta.dropClass('com.runwaysdk.jstest.TestProblem');
        
        testClass.instanceForceProblem(new CallbackHandler(yuiTest, {
          onSuccess: CallbackHandler.failSuccess,
          onProblemExceptionDTO: CallbackHandler.next
        }));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, request){
      
        var problem = request.getProblems()[0];
        if (problem instanceof com.runwaysdk.business.ProblemDTO &&
        problem.getLocalizedMessage() == 'This is a message from TestProblem: 12345' &&
        !('getProblemChar' in problem)) {
          // Success
        }
        else {
          pNoMatch();
        }
        
        yuiTest.resume(); // Pass this test!
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },

  testWarning : function ()
  {
    var yuiTest = this;
    var testClass = new com.runwaysdk.jstest.TestClass();
    var wNoMatch = function(){
      yuiTest.resume(function(){
        Y.Assert.fail("The thrown warning does not match the expected value.");
      });
    }
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      
        Y.log('Testing a type-safe Warning', "debug");
        
        testClass.instanceForceWarning(new CallbackHandler(yuiTest, {onSuccess:function(ret){CallbackHandler.next(ret, this.getWarnings())}}));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, ret, warnings){
      
        if (ret === 10 &&
        warnings.length == 1 &&
        warnings[0] instanceof com.runwaysdk.jstest.TestWarning &&
        warnings[0].getWarningInt() === 12345 &&
        warnings[0].getWarningChar() === 'Test warningChar' &&
        warnings[0].getMessage() === 'This is a message from TestWarning: 12345') {
        // Success
        }
        else {
          wNoMatch();
        }
        
        Y.log('Testing a type-safe Warning w/ void return', "debug");
        
        testClass.instanceForceWarningVoid(new CallbackHandler(yuiTest, {
          onSuccess: function(ret, called){
            CallbackHandler.next(ret, called, this.getWarnings())
          }
        }));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, ret, called, warnings){
      
        if (ret == null &&
        called instanceof com.runwaysdk.jstest.TestClass &&
        warnings.length == 1 &&
        warnings[0] instanceof com.runwaysdk.jstest.TestWarning &&
        warnings[0].getWarningInt() === 12345 &&
        warnings[0].getWarningChar() === 'Test warningChar Void' &&
        warnings[0].getMessage() === 'This is a message from TestWarning: 12345') {
          // Success
        }
        else {
          wNoMatch();
        }
        
        Y.log('Testing multiple type-safe Warnings',"debug");
        
        testClass.instanceForceMultipleWarnings(new CallbackHandler(yuiTest, {onSuccess:function(ret){CallbackHandler.next(ret, this.getWarnings())}}));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, ret, warnings){
      
        if (ret === 10 &&
        warnings.length == 2 &&
        warnings[0] instanceof com.runwaysdk.jstest.TestWarning &&
        warnings[1] instanceof com.runwaysdk.jstest.TestWarning &&
        warnings[0].getWarningInt() + warnings[1].getWarningInt() === 66666) {
          // Success
        }
        else {
          wNoMatch();
        }
        
        Y.log('Testing a generic Warning',"debug");
        
        Mojo.Meta.dropClass('com.runwaysdk.jstest.TestWarning');
        
        testClass.instanceForceWarning(new CallbackHandler(yuiTest, {onSuccess:function(ret){CallbackHandler.next(ret, this.getWarnings())}}));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, ret, warnings){
      
        if (ret === 10 &&
        warnings.length == 1 &&
        warnings[0] instanceof DTO.WarningDTO &&
        !('getWarningChar' in warnings[0]) &&
        warnings[0].getMessage() === 'This is a message from TestWarning: 12345') {
          // Success
        }
        else {
          wNoMatch();
        }
        
        yuiTest.resume() // Pass this test!
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },

  testInformation : function ()
  {
    var yuiTest = this;
    var testClass = new com.runwaysdk.jstest.TestClass();
    
    var iNoMatch = function(){
      yuiTest.resume(function(){
        Y.Assert.fail("The thrown information does not match the expected value.");
      });
    }
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      
        Y.log('Testing a type-safe Information', "debug");
        
        testClass.instanceForceInformation(new CallbackHandler(yuiTest, {onSuccess:function(ret){CallbackHandler.next(ret, this.getInformation())}}));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, ret, information){
      
        if (ret === 10 &&
        information.length == 1 &&
        information[0] instanceof com.runwaysdk.jstest.TestInformation &&
        information[0].getInfoInt() === 12345 &&
        information[0].getInfoChar() === 'Test infoChar' &&
        information[0].getMessage() === 'This is a message from TestInformation: 12345') {
          // Success
        }
        else {
          iNoMatch();
        }
        
        Y.log('Testing multiple type-safe Information',"debug");
        
        testClass.instanceForceMultipleInformations(new CallbackHandler(yuiTest, {onSuccess:function(ret){CallbackHandler.next(ret, this.getInformation())}}));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, ret, information){
      
        if (ret === 10 &&
        information.length == 2 &&
        information[0] instanceof com.runwaysdk.jstest.TestInformation &&
        information[1] instanceof com.runwaysdk.jstest.TestInformation &&
        information[0].getInfoInt() + information[1].getInfoInt() === 66666) {
          // Success
        }
        else {
          iNoMatch();
        }
        
        Y.log('Testing a generic Information',"debug");
        
        Mojo.Meta.dropClass('com.runwaysdk.jstest.TestInformation');
        
        testClass.instanceForceInformation(new CallbackHandler(yuiTest, {onSuccess:function(ret){CallbackHandler.next(ret, this.getInformation())}}));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq, ret, information){
      
        if (ret === 10 &&
        information.length == 1 &&
        information[0] instanceof DTO.InformationDTO &&
        !('getInfoChar' in information[0]) &&
        information[0].getMessage() === 'This is a message from TestInformation: 12345') {
          // Success
        }
        else {
          iNoMatch();
        }
        
        yuiTest.resume(); // Pass this test!
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  },

});

TestFramework.newTestCase(SUITE_NAME, {

	  name : 'OntologyTests',
	  
	  caseSetUp : function ()
	  {
	    
	  },
	  
	  setUp : function ()
	  {
//	    g_taskQueue = new struct.TaskQueue();
	  },
	  
	  tearDown : function()
	  {
//	    g_taskQueue = null;
	  },

	  testTerm : function ()
	  {
		  Mojo.Meta.newClass('com.runwaysdk.test.Sequential', {

			  Extends : Mojo.BUSINESS_PACKAGE+'RelationshipMd',

			  Instance : {

			    initialize : function(obj)
			    {
			      this.$initialize(obj);
			    }
			  
			  }
			});
		  
		  Mojo.Meta.newClass('com.runwaysdk.test.Alphabet', {

			  Extends : Mojo.BUSINESS_PACKAGE+'TermDTO',

			  Instance : {

			    initialize : function(obj)
			    {
			      this.$initialize(obj);
			    }
			  
			  }
			});
		  
		  var termA = new com.runwaysdk.test.Alphabet();
		  var termB = new com.runwaysdk.test.Alphabet();
		  var termC = new com.runwaysdk.test.Alphabet();
		  
		  termA.addChild(termB, "com.runwaysdk.test.Sequential");
		  termB.addChild(termC, "com.runwaysdk.test.Sequential");
		  
		  var parents = termC.getParents();
	  }
	  
});

})();