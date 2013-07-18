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
 * This test framework is used to aid in the testing of JavaScript code. This framework includes an auto-generated
 * html file: MasterTestLauncher.html that can be easily used to run your tests, or you can run SeleniumJUnitTestAutomator.java
 * in eclipse as a JUnit test to send your tests to a Selenium server.
 * 
 * @author Richard Rowlands
 */

(function(){

com.runwaysdk.test = com.runwaysdk.test || {};
com.runwaysdk.test.TestFramework = {};
var TestFramework = com.runwaysdk.test.TestFramework;
var Y;
TestFramework._suites = {};
TestFramework._testCases = {};
TestFramework._includesPending = 0;
TestFramework._testLoggerId = 'testLogger';
TestFramework.PACKAGE = Mojo.ROOT_PACKAGE + "test.";

/**
 * API Functions:
 * Feel free to use any of these functions when writing your JavaScript unit tests.
 */
TestFramework.defineSuiteTearDown = function (suite, obj)
{
	if (!TestFramework._suites[suite])
		TestFramework._handleException("TestFramework.defineSuiteTearDown: Unable to define suite tear down for the suite '" + suite + "'. The suite does not exist!");
	else if (TestFramework._suites[suite]._suiteTearDown != null)
		TestFramework._handleException("TestFramework.defineSuiteTearDown: Unable to define suite tear down for the suite '" + suite + "'. The suite already has a set up defined for it!");
	else if (typeof obj != "function")
		TestFramework._handleException("TestFramework.defineSuiteTearDown: Unable to define suite tear down for the suite '" + suite + "'. The second argument is not a function!");
	else
		TestFramework._suites[suite].tearDown = obj;
}

TestFramework.defineSuiteSetUp = function (suite, obj)
{
	if (!TestFramework._suites[suite])
		TestFramework._handleException("TestFramework.defineSuiteSetUp: Unable to define suite set up for the suite '" + suite + "'. The suite does not exist!");
	else if (TestFramework._suites[suite]._suiteSetUp != null)
		TestFramework._handleException("TestFramework.defineSuiteSetUp: Unable to define suite set up for the suite '" + suite + "'. The suite already has a set up defined for it!");
	else if (typeof obj != "function")
		TestFramework._handleException("TestFramework.defineSuiteSetUp: Unable to define suite set up for the suite '" + suite + "'. The second argument is not a function!");
	else
		TestFramework._suites[suite].setUp = obj;
}

TestFramework.newTestCase = function (suite, test)
{
	if (test == null)
		TestFramework._handleException("TestFramework.newTest: Unable to create test case without required parameters. (2nd parameter is null)!");
	else if (suite == null || suite == "")
		TestFramework._handleException("TestFramework.newTest: Unable to create a new test case without specifying a suite (1st parameter is null)!");
	else if (test.name == "" || test.name == null)
		TestFramework._handleException("TestFramework.newTest: Unable to create test case, the 'name' field on the provided test object is null or invalid!");
	else if (TestFramework._tooLateToAddTests == true)
		TestFramework._handleException("TestFramework.newTest: Unable to create test case with the name '" + test.name + "'. Test creation time has expired (tests must be created before the page is rendered)");
	else if (TestFramework._suites[suite] == undefined || TestFramework._testCases[suite] == undefined)
		TestFramework._handleException("TestFramework.newTest: Unable to create test case with the name '" + test.name + "'. The suite '" + suite + "' does not exist.");
	else if (TestFramework._testCases[suite][test] != undefined)
		TestFramework._handleException("TestFramework.newTest: Unable to create test case with the name '" + test.name + "'. There already exists a test by that name in the suite '" + suite + "'.");
	else
	{
    TestFramework._testCases[suite][test.name] = test;
	}
}

TestFramework.newSuite = function (name)
{
	if (name == null || name == "")
		TestFramework._handleException("TestFramework.newSuite: Unable to create a new suite without specifying a suite name (1st parameter is null or invalid)!");
	else if (TestFramework._suites[name] != null)
		TestFramework._handleException("TestFramework.newSuite: Unable to create a new suite with the name '" + name + "'. A suite by that name already exists!");
	else if (TestFramework._tooLateToAddTests == true)
		TestFramework._handleException("TestFramework.newSuite: Unable to create a new suite with the name '" + name + "'. Suite creation time has expired (suites must be created before the page is rendered)");
	else
	{
		TestFramework._testCases[name] = {};
		TestFramework._suites[name] = {};
	}
}

TestFramework.alertKeys = function (obj, str) // this is a debug function, it loops through an object and alerts all the keys on it.
{
	var s = "";
	for (key in obj)
		s = s + key + ", ";
	
	if (str)
		alert("found the keys on " + str + ": " + s);
	else
		alert("found the keys: " + s);
}

TestFramework.assertAlert = function (expr, name) // Another function helpful for debugging
{
	if (expr === 0 || expr === null || expr === undefined || expr === false || isNaN(expr))
	{
		name = name || "";
		name = name + ": ";
		alert("Assertion failed. " + name + " : " + expr);
	}
}

TestFramework.run = function (name) // can be used to run a suite, a test case, or a test by namespacing it like: run('ExampleSuite.testCase.test');
{
	document.getElementById(TestFramework._testLoggerId).scrollIntoView();
	
	Y.Test.Runner.clear();
	
	if (name == "ALL")
	{
		var reportSuite = new Y.Test.Suite("All");
		
		for (key in this._suites)
		{
			if (this._suites[suite] instanceof Y.Test.Suite)
			{
				reportSuite.add(this._suites[key]);
			}
		}
		
		Y.Test.Runner.add(reportSuite);
	}
	else
	{
		var data = name.split("\.");
		
		if (data[2]) // run a test
		{
			// YUI Test doesn't natively support individual test running, so make another test case and put the test in it
			
			var testCase = this._testCases[ data[0] ][ data[1] ];
			var test = testCase[ data[2] ];
			
			var shouldError = "";
			if (testCase._should && testCase._should.error && testCase._should.error[data[2]])
			{
				shouldError = {};
				shouldError[ data[2] ] = testCase._should.error[ data[2] ];
			}
			
			var myObj = {};
			
			var testCaseConfig = { name: data[2], _should: { error: shouldError } };
			testCaseConfig[ data[2] ] = Mojo.Util.bind( testCase, test );
			testCaseConfig.setUp = Mojo.Util.bind( testCase, testCase.setUp );
			var td = Mojo.Util.bind( testCase, testCase.tearDown );
			testCaseConfig.tearDown = function () { 
				td(1); 
			}
			
			testCase = new Y.Test.Case( testCaseConfig );
      
			var reportSuite = new Y.Test.Suite(data[2]);
			reportSuite.setUp = Mojo.Util.bind( reportSuite, this._suites[ data[0] ].setUp );
			reportSuite.tearDown = Mojo.Util.bind( reportSuite, this._suites[ data[0] ].tearDown );
			reportSuite.add(testCase);
			
			Y.Test.Runner.add( reportSuite );
		}
		else if (data[1]) // run a test case
		{
			var reportSuite = new Y.Test.Suite(data[0]);
			reportSuite.setUp = this._suites[ data[0] ].setUp;
			reportSuite.tearDown = this._suites[ data[0] ].tearDown;
			reportSuite.add( this._testCases[ data[0] ][ data[1] ] );
			
			Y.Test.Runner.add( reportSuite );
		}
		else // run a test suite
			Y.Test.Runner.add(this._suites[name]);
	}
	
	Y.Test.Runner.run();
};

TestFramework.getY = function() {
  return Y;
}

/**
 * Testing Framework Core:
 * Don't use any of the functions unless modifying the core.
 */
TestFramework._handleException = function (msg)
{
	throw Mojo.Meta.newInstance(TestFramework.PACKAGE + ".Exception", msg);
}

TestFramework._generateButtons = function ()
{
	var testsDiv = document.getElementById("buttonsDiv");
	
	// Register onClick event handler
	var handler = function (e)
	{
		if (e.target.testString != undefined && e.target.testString != "")
		  TestFramework.run(e.target.testString);
	}
	testsDiv.addEventListener("click", handler, false);
	
	
	var frag = document.createDocumentFragment();
	
	var runAllButton = document.createElement("input");
	runAllButton.type = "button";
	runAllButton.id = "all";
	runAllButton.value = "Run All Test Suites";
	runAllButton.testString = "ALL";
	
	frag.appendChild(runAllButton);
	frag.appendChild( document.createElement("br") );
	frag.appendChild( document.createElement("br") );
	
	for (var suite in this._suites)
	{
		if (this._suites[suite] instanceof Y.Test.Suite)
		{
			// Create Run Suite buttons
			var button = document.createElement("input");
			button.type = "button";
			button.testString = suite;
			button.id = suite;
			button.value = suite;
			frag.appendChild(button);
			
			var testCaseUL = document.createElement("ul");
			
			for (var testCase in this._testCases[suite])
			{
				if (this._testCases[suite][testCase] instanceof Y.Test.Case)
				{
					li = document.createElement("li");
					var span = document.createElement("span");
					span.testString = suite + "." + testCase;
					span.innerHTML = testCase;
					li.appendChild(span);
					testCaseUL.appendChild(li);
					
					li = document.createElement("li");
					
					var testUL = document.createElement("ul");
					
					for (var test in this._testCases[suite][testCase])
					{
						if (test.search("test") == 0)
						{
							var li2 = document.createElement("li");
							span = document.createElement("span");
							span.testString = suite + "." + testCase + "." + test;
							span.innerHTML = test;
							li2.appendChild(span);
							testUL.appendChild(li2);
						}
					}
					
					li.appendChild(testUL);
					testCaseUL.appendChild(li);
				}
			}
			frag.appendChild(testCaseUL);
		}
	}
	
	testsDiv.appendChild(frag);
};

TestFramework._addTestCasesToSuites = function ()
{
	for (suite in this._suites)
	{
		if (this._suites[suite] instanceof Y.Test.Suite)
		{
			for (testCase in this._testCases[suite])
			{
				if (this._testCases[suite][testCase] instanceof Y.Test.Case)
				{
					this._suites[suite].add(this._testCases[suite][testCase]);
				}
			}
		}
	}
};

TestFramework._loadTestsIntoYUI = function ()
{
	for (suite in this._suites)
	{
    this._suites[suite].name = suite;
		this._suites[suite] = new Y.Test.Suite(this._suites[suite]);
		
		for (testCase in this._testCases[suite])
		{
			this._testCases[suite][testCase] = new Y.Test.Case(this._testCases[suite][testCase]);
			TestFramework._hackInCaseSetUpAndTearDown(this._testCases[suite][testCase]);
		}
	}
	
	this._addTestCasesToSuites();
}

TestFramework._hackInCaseSetUpAndTearDown = function (testCase)
{
	var test = testCase;
	
  ////Hack In Our Case Set Up ////
	if (test.caseSetUp != undefined)
	{
		if (test.setUp == undefined)
		{
			test.setUp = function () { if (!test.caseSetUp.__hasBeenRun) { test.caseSetUp.__hasBeenRun = true; test.caseSetUp(); } };
		}
		else
		{
			test.__userSetUp = test.setUp;
			
			test.setUp = function () { if (!test.caseSetUp.__hasBeenRun) { test.caseSetUp.__hasBeenRun = true; test.caseSetUp(); } test.__userSetUp(); };
		}
	}
  ///////////////
  
  
  //// Hack In Our Case Tear Down ////
  var testsInCase = 0;
  for (var k in test)
  {
    if (k.search("test") == 0)
      testsInCase++;
  }
  test.__numOfTestsRun = 0;
  var caseTearDown = function(overrideTestsInCase)
  {
  	overrideTestsInCase = overrideTestsInCase || testsInCase;
    test.__numOfTestsRun++;
    if (test.__numOfTestsRun == overrideTestsInCase)
    {
      test.caseTearDown();
      test.__numOfTestsRun = 0;
      
      if (test.caseSetUp != undefined)
      {
        test.caseSetUp.__hasBeenRun = false;
      }
    }
  }
  
	if (test.caseTearDown != undefined)
	{
		if (test.tearDown == undefined)
		{
			test.tearDown = caseTearDown;
		}
		else
		{
			test.__userTearDown = test.tearDown;
			
			test.tearDown = function (overrideTestsInCase) { test.__userTearDown(); caseTearDown(overrideTestsInCase); };
		}
	}
	else if (test.caseSetUp != undefined)
	{
		test.tearDown = function (overrideTestsInCase)
		{
			overrideTestsInCase = overrideTestsInCase || testsInCase;
			test.__numOfTestsRun++;
      if (test.__numOfTestsRun == overrideTestsInCase)
      {
        test.__numOfTestsRun = 0;
        test.caseSetUp.__hasBeenRun = false;
      }
		}
	}
  ///////////////////
}

//YUI({ logInclude: { TestRunner: true } }).use('node', 'console', 'test', 'console-filters', function (YY)
YUI({ logInclude: { TestRunner: true } }).use("*", function(YY)
{
	Y = YY;
	
	Y.namespace("suites");
	
	Y.on("domready", function () {
  	var yconsole = new Y.Console({
          newestOnTop: false,
          style: 'inline'
    });
   
    yconsole.plug(Y.Plugin.ConsoleFilters, {
      defaultVisibility: true,
      source: {
          global : true,
          TestRunner : true
      }
    });

    yconsole.render('#'+TestFramework._testLoggerId);
		TestFramework._loadTestsIntoYUI();
		TestFramework._tooLateToAddTests = true;
		TestFramework._generateButtons();

	});
	
    

});

Mojo.Meta.newClass(TestFramework.PACKAGE + 'Exception', {
	
	Instance : {

		initialize : function (obj)
		{
			this.$initialize(obj);
		}
	},
	
	Extends: Mojo.ROOT_PACKAGE + "Exception"
	
});

})();
