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
 * This test suite tests RunwaySDK_UI.js
 * 
 * @author Terraframe
 */

(function(){

var TestFramework = com.runwaysdk.test.TestFramework;
var DOMTest = com.runwaysdk.test.DOMTest;
var EVENT_PACKAGE = 'com.runwaysdk.event.';
var Y = YUI().use("*");
var SUITE_NAME = "RunwaySDK_UI";
var FACTORY;
var RUNWAY_UI;
var MockDTO = null;
var TIMEOUT = 5000; // standard timeout of five seconds, which is plenty of time for even complex requests
var RELATIONSHIP_TYPE = "com.runwaysdk.jstest.business.ontology.Sequential";

com.runwaysdk.ui.DOMFacade.execOnPageLoad(function() {
  //Create select dropdown
  if (document.getElementById("guiFrameworkSelectSelect") == null) {
    var select = document.createElement("select");
    select.id = "guiFrameworkSelectSelect";
    document.getElementById("guiFrameworkSelect").appendChild(select);
    var factories = com.runwaysdk.ui.Manager.getAvailableFactories();
    for (var i = 0; i < factories.length; ++i) {
      var option = document.createElement("option");
      option.value = factories[i];
      option.innerHTML = factories[i];
      select.appendChild(option);
    }
    select.selectedIndex = 2; // Sets the default to jquery
  }
});

TestFramework.newSuite(SUITE_NAME);

TestFramework.defineSuiteSetUp(SUITE_NAME, function ()
{
  RUNWAY_UI = Mojo.Meta.alias("com.runwaysdk.ui.*");
  
  var select = document.getElementById("guiFrameworkSelectSelect");
  RUNWAY_UI.Manager.setFactory(select.options[select.selectedIndex].text);
  
  FACTORY = RUNWAY_UI.Manager.getFactory();
  
  MockDTO = Mojo.Meta.newClass(TestFramework.PACKAGE+'MockDTO', {
    Extends : Mojo.ROOT_PACKAGE+'Base',
    Instance : {
      initialize : function(id)
      {
        this._id = id || this._generateId();
        this.$initialize();
      },
      _generateId : function()
      {
        return 'md_'+Mojo.Util.generateId(16);
      },
      getId : function()
      {
        return this._id;
      },
      toString : function()
      {
        return this.getId();
      }
    }
  });
  
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
	//FACTORY.DragDrop.disable();
});

TestFramework.newTestCase(SUITE_NAME, {
  
  name: "Widgets",
  
  caseSetUp : function() {
    
  },
  
  caseTearDown : function() {
    
  },
  
  testDialog : function()
  {
    var okHandler = function() { alert("You clicked Ok!"); };
    var cancelHandler = function() { 
     dialog.destroy();
    };
    
    var dialog = FACTORY.newDialog("K00L Dialog");
    dialog.appendContent("Dialogs are super kool. Newlines are easy to do. All you have to do is specify a width for the dialog and then it auto-wraps your text when your text is longer than the specified width. If you don't specify a width it uses a default width specified in the initialize method of dialog.");
    
    dialog.addButton("Ok", okHandler);
    dialog.addButton("Cancel", cancelHandler);
    
    dialog.render();
    dialog.addButton("AfterThought", cancelHandler);
  },
  
  testModalDialog : function() {
    var okHandler = function() { alert("You clicked Ok!"); };
    var cancelHandler = function() { 
     modal.destroy();
    };
    
    var modal = FACTORY.newDialog("Modal", {modal:true});
    modal.appendContent("Deal with me first. You have no other choice.");
    modal.addButton("Ok", okHandler, true);
    modal.addButton("Cancel", cancelHandler);
    modal.render();
  },
  
  testLongInheritanceChain : function() {
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyBase',{
      Instance : {
        method : function() {
          console.log("MyBase");
        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass1',{
      Extends : com.runwaysdk.ui.MyBase,
      Instance : {
        method : function() {
          console.log("MyClass1");
          this.$method();
        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass2',{
      Extends : com.runwaysdk.ui.MyClass1,
      Instance : {
//        method : function() {
//          console.log("MyClass2");
//          this.$method();
//        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass3',{
      Extends : com.runwaysdk.ui.MyClass2,
      IsAbstract : true,
      Instance : {
//        method : function() {
//          console.log("MyClass3");
//          this.$method();
//        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass4',{
      Extends : com.runwaysdk.ui.MyClass3,
      IsAbstract : true,
      Instance : {
        method : function() {
          console.log("MyClass4");
          this.$method();
        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass5',{
      Extends : com.runwaysdk.ui.MyClass4,
      Instance : {
        method : function() {
          console.log("MyClass5");
          this.$method();
        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass6',{
      Extends : com.runwaysdk.ui.MyClass5,
      Instance : {
        method : function() {
          console.log("MyClass6");
          this.$method();
        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass7',{
      Extends : com.runwaysdk.ui.MyClass6,
      Instance : {
        method : function() {
          console.log("MyClass7");
          this.$method();
        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass8',{
      Extends : com.runwaysdk.ui.MyClass7,
      Instance : {
        method : function() {
          console.log("MyClass8");
          this.$method();
        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass9',{
      Extends : com.runwaysdk.ui.MyClass8,
      Instance : {
        method : function() {
          console.log("MyClass9");
          this.$method();
        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass10',{
      Extends : com.runwaysdk.ui.MyClass9,
      Instance : {
        method : function() {
          console.log("MyClass10");
          this.$method();
        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass11',{
      Extends : com.runwaysdk.ui.MyClass10,
      Instance : {
        method : function() {
          console.log("MyClass11");
          this.$method();
        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass12',{
      Extends : com.runwaysdk.ui.MyClass11,
      Instance : {
        method : function() {
          console.log("MyClass12");
          this.$method();
        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass13',{
      Extends : com.runwaysdk.ui.MyClass12,
      Instance : {
        method : function() {
          console.log("MyClass13");
          this.$method();
        }
      }
    });
    Mojo.Meta.newClass(Mojo.UI_PACKAGE+'MyClass14',{
      Extends : com.runwaysdk.ui.MyClass13,
      Instance : {
//        method : function() {
//          console.log("MyClass14");
//          this.$method();
//        }
      }
    });
    
    var myClass14 = new com.runwaysdk.ui.MyClass14();
    myClass14.method();
  },
  
  testTree : function() {
    var dialog = FACTORY.newDialog("K00L Dialog", {width: 600, height: 300});
    
    var treeDiv = FACTORY.newElement("div");
    treeDiv.setId("dialogTree");
    dialog.appendContent(treeDiv);
    
    dialog.render();
    
    var tree = new com.runwaysdk.ui.ontology.TermTree({nodeId : "#dialogTree", dragDrop : true});
    tree.setRootTerm(g_idTermRoot, RELATIONSHIP_TYPE);
    
    var editDialog = null;
    
    var selectCallback = function(term) {
      editDialog = FACTORY.newDialog("Edit Term");
      editDialog.setInnerHTML(term.getId());
      
      var editHandler = function() { alert("You clicked edit!"); };
      
      var deleteCallback = {
          onSuccess: function() {
            editDialog.getImpl().destroy();
          },
          
          onFailure: function(obj) {
            alert("An error occurred: " + obj);
          }
      };
      var deleteHandler = function() { tree.removeTerm(term, deleteCallback); };
      
      editDialog.addButton("edit", editHandler);
      editDialog.addButton("delete", deleteHandler);
      editDialog.render();
      editDialog.getEl().setStyle("zIndex", 1000);
    };
    tree.registerOnTermSelect(selectCallback);
    
    var deselectCallback = function(term) {
      if (editDialog != null) {
        editDialog.getImpl().destroy();
      }
    };
    tree.registerOnTermDeselect(deselectCallback);
    
    var yuiTest = this;
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      tree.addChild(g_idTerm1NoChildren, g_idTermRoot, RELATIONSHIP_TYPE, new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      tree.addChild(g_idTerm2NoChildren, g_idTerm1NoChildren, RELATIONSHIP_TYPE, new CallbackHandler(yuiTest));
      }
    }));
    
    g_taskQueue.addTask(new struct.TaskIF({
      start: function(tq){
      tree.addChild(g_idTerm3NoChildren, g_idTerm2NoChildren, RELATIONSHIP_TYPE, new CallbackHandler(yuiTest));
      
      yuiTest.resume();
      }
    }));
    
    g_taskQueue.start();
    
    yuiTest.wait(TIMEOUT);
  }
});

TestFramework.newTestCase(SUITE_NAME, {
   
  name: "ComponentFactoryTests",
  
  caseSetUp : function() {
    this._domEl = RUNWAY_UI.DOMFacade.createElement("div");
    this._domEl.id = "myTestyDiv";
    RUNWAY_UI.DOMFacade.getRawBody().appendChild(this._domEl);
    
    // For DataTable test
    this.arrayData = [
      ["Id", "Title", "Author", "Publication Date"],
      ["0", "House of the Scorpion", "Nancy Farmer", "Whatever"],
      ["1", "My Book", "Duh", "Stuff"],
      ["3", "Foooooooooooooooooooooooooooooooooooooo", "Baaaaaaaaaaaaaaaaaaaaaaaaaaar", "Today"],
      ["3", "Foooooooooooo", "Baaaaaaaaaaar", "Today"],
      ["3", "Foo", "Bar", "Today"],
      ["3", "Foo", "Bar", "Today"],
      ["3", "Foo", "Bar", "Today"],
      ["3", "Foo", "Bar", "Today"],
      ["3", "Foo", "Bar", "Today"],
      ["3", "Foo", "Bar", "Today"],
      ["3", "Foo", "Bar", "Today"],
      ["3", "Foo", "Bar", "Today"]
    ];
  },
  
  caseTearDown : function() {
    RUNWAY_UI.DOMFacade.getRawBody().removeChild(this._domEl);
  },
  
  testNewElement : function() {
    // The El parameter can be:
    // A string : if the string has a # at the beginning then it finds the element via the proceeding ID and then wraps that
    var el = FACTORY.newElement("#myTestyDiv");
    Y.Assert.isTrue(RUNWAY_UI.Util.isElement(el), "SubTest 1 failed: newElement via #id did not return a valid element.");
    
    //            if the string does not have a # at the beginning then it creates a new element with the string
    el = FACTORY.newElement("div");
    Y.Assert.isTrue(RUNWAY_UI.Util.isElement(el), "SubTest 2 failed: newElement via string 'div' did not return a valid element.");
    
    // An HTMLElementIF, in which case it does nothing and returns el
    var el2 = FACTORY.newElement(el);
    Y.Assert.areEqual(el, el2, "SubTest 3 failed: newElement via an HTMLElementIF did not return the same HTMLElementIF");
    
    // An instance of an element of the underlying implementation, in which case it wraps it and returns a HTMLElementIF wrapper
    el = FACTORY.newElement( el2.getImpl() );
    Y.Assert.isTrue(RUNWAY_UI.Util.isElement(el), "SubTest 4 failed: newElement via an underlying implementation element did not return an HTMLElementIF");
    
    // A raw DOM element, in which case it wraps it and returns a HTMLElementIF wrapper.
    el = FACTORY.newElement(this._domEl);
    Y.Assert.isTrue(RUNWAY_UI.Util.isElement(el), "SubTest 5 failed: newElement via a raw DOM element did not return an HTMLElementIF");
  },
  
  testLayoutManager : function()
  {
    var dialog = FACTORY.newDialog("Layout Manager Test", {width:"auto"});
    dialog.render();
    
    var mainVL = FACTORY.newLayout("vertical");
    var midHL = FACTORY.newLayout("horizontal");
    dialog.appendChild(mainVL);
    
    var headerDiv = FACTORY.newElement("div");
    headerDiv.setInnerHTML("Header div.");
    var navDiv = FACTORY.newElement("div");
    navDiv.setInnerHTML("Navigation div.");
    var contentDiv = FACTORY.newElement("div");
    contentDiv.setInnerHTML("Content div.");
    var detailDiv = FACTORY.newElement("div");
    detailDiv.setInnerHTML("Extra div.");
    var footerDiv = FACTORY.newElement("div");
    footerDiv.setInnerHTML("Footer div.");
    
    var list = FACTORY.newList("Test List", {id: 'testList'});
    
    var addHandler = function(){
      list.addItem(new MockDTO());
    };
    var removeHandler = function() { list.removeItem(); };
    var clearHandler = function() { list.clearItems(); };
    var exitHandler = function() { dialog.destroy(); };
    
    var addButton = FACTORY.newButton("Add", addHandler);
    var removeButton = FACTORY.newButton("Remove", removeHandler);
    var clearButton = FACTORY.newButton("Clear", clearHandler);
    var exitButton = FACTORY.newButton("Exit", exitHandler);
    
    dialog.addButton(addButton);
    dialog.addButton(removeButton);
    dialog.addButton(clearButton);
    dialog.addButton(exitButton);
    
    mainVL.appendChild(headerDiv);
    mainVL.appendChild(midHL);
      midHL.appendChild(navDiv);
      midHL.appendChild(contentDiv);
      midHL.appendChild(detailDiv);
    mainVL.appendChild(footerDiv);
  },
  
  testNewForm : function()
  {
    //var dialog = FACTORY.newDialog("Form Test", {width:"auto"});
    var dialog = FACTORY.newDialog("Form Test");
    
    var submitHandler = function() { 
      var v = FACTORY.newFormControl('FormVisitor');
      form.accept(v);
      
      var cv = FACTORY.newFormControl('ConsoleFormVisitor');
      form.accept(cv);
    
      dialog.destroy();
    };
    
    var cancelHandler = function() { dialog.destroy(); };
    
    var submitButton = FACTORY.newButton("Submit", submitHandler);
    var cancelButton = FACTORY.newButton("Cancel", cancelHandler);
    
    dialog.addButton(submitButton);
    dialog.addButton(cancelButton);
    dialog.render();
    
    var form = FACTORY.newForm("Test Form");
    
    var fNameTextInput = FACTORY.newFormControl('text', 'firstName');
    form.addEntry("First name", fNameTextInput);
    
    var lNameTextInput = FACTORY.newFormControl('text', 'lastName');
    form.addEntry("Last name", lNameTextInput);
    
    var reasonSelectList = FACTORY.newFormControl('select', 'reason', {multiple:true});
    reasonSelectList.addOption("Too basic", 'basic');
    reasonSelectList.addOption("Doesn't do anything", 'worthless');
    reasonSelectList.addOption("Not enough Web 2.0", 'twopointoh', true);
    reasonSelectList.addOption("Needs more cowbell", 'cowbell');
    form.addEntry("Reason(s) you think this form sucks", reasonSelectList);
    
    var commentsTextArea = FACTORY.newFormControl('textarea', 'comments');
    form.addEntry("Additional Hateful Comments", commentsTextArea);
    
    var feedbackId = FACTORY.newFormControl('hidden', 'feedbackId', {value: "3812908309"});
    form.appendChild(feedbackId);
    
    
    dialog.appendChild(form);
    
  },
  
  testDialogDestroy : function()
  {
    var testDTO = new MockDTO("supguyz");
    var mEvt = Mojo.Meta.findClass(Mojo.EVENT_PACKAGE+'MouseEvent');
    
    var l = new com.runwaysdk.event.EventListener({
    handleEvent : function(e) {
      }
    });
    
    var list = FACTORY.newList("Test List", {id: 'testList'});
    list.addItem(testDTO);
    var listItemId = list.getItem(0).getEl().getRawEl().id;
    
    var addHandler = function(){
      list.addItem(new MockDTO());
    };
    
    var listItem = list.getItem(0);
    Y.Assert.areEqual(false, listItem.hasEventListener(mEvt, l), "ListItem should not yet have an EventListener.");
    listItem.addEventListener(mEvt, l);
    Y.Assert.areEqual(true, listItem.hasEventListener(mEvt, l), "ListItem should have an EventListener.");
    
    var removeHandler = function() { list.removeItem(); };
    var clearHandler = function() { list.clearItems(); };
    var exitHandler = function() { dialog.destroy(); };
    
    var addButton = FACTORY.newButton({label:"Add", handler:addHandler, isDefault:true});
    var removeButton = FACTORY.newButton({label:"Remove", handler:removeHandler, isDefault:true});
    var clearButton = FACTORY.newButton({label:"Clear", handler:clearHandler, isDefault:true});
    var exitButton = FACTORY.newButton({label:"Exit", handler:exitHandler});
    
    var dialog = FACTORY.newDialog("List Test");
    dialog.addButton(addButton);
    dialog.addButton(removeButton);
    dialog.addButton(clearButton);
    dialog.addButton(exitButton);
    dialog.appendChild(list);
    dialog.render();
    
    var listItemExists = false;
    if (document.getElementById(listItemId))
    {
      listItemExists = true;
    }
    Y.Assert.areEqual(true, listItemExists, "ListItem is supposed to be present.");
    
    dialog.destroy();
    
    var listItemExists = false;
    if (document.getElementById(listItemId))
    {
      listItemExists = true;
    }
    Y.Assert.areEqual(false, listItemExists, "ListItem is supposed to be gone.");
    Y.Assert.areEqual(false, listItem.hasEventListener(mEvt, l), "ListItem's EventListener should be gone.");
  },
  
  testNewList : function()
  {
    var testDTO = new MockDTO("supguyz");
    
    var list = FACTORY.newList("Test List", {id: 'testList'});
    list.addItem(testDTO);
    
    var addHandler = function(){
      list.addItem(new MockDTO());
    };
    var removeHandler = function() { list.removeItem(); };
    var clearHandler = function() { list.clearItems(); };
    var exitHandler = function() { dialog.destroy(); };
    var dialog = FACTORY.newDialog("List Test");
    
    dialog.addButton("Add", addHandler);
    dialog.addButton("Remove", removeHandler);
    dialog.addButton("Clear", clearHandler);
    dialog.addButton("Exit", exitHandler);
    dialog.render();
    
    dialog.appendChild(list);
    dialog.render();
  },
  
  testNewListAuto : function()
  {
  
    var list = FACTORY.newList("Test List", {id: 'testList'});
    
    var addHandler = function(){
      list.addItem(new MockDTO());
    };
    var removeHandler = function() { list.removeItem(); };
    var clearHandler = function() { list.clearItems(); };
    var exitHandler = function() { dialog.destroy(); };
    var dialog = FACTORY.newDialog("List Test");
    
    var addButton = FACTORY.newButton("Add", addHandler);
    var removeButton = FACTORY.newButton("Remove", removeHandler);
    var clearButton = FACTORY.newButton("Clear", clearHandler);
    var exitButton = FACTORY.newButton("Exit", exitHandler);
    
    dialog.addButton(addButton);
    dialog.addButton(removeButton);
    dialog.addButton(clearButton);
    dialog.addButton(exitButton);
    dialog.appendChild(list);
    dialog.render();
  
    
    var buttons = dialog.getButtons();
    var buttonIds = [];
    for (var i = 0; i < buttons.length; i++)
    {
      //var text = buttons[i].text;
      var id = buttons[i].htmlButton.id;
      buttonIds.push(id);
    }
      
    // Verify List is empty
    Y.Assert.areEqual(0, list.size(), "List is supposed to be empty.");
    
    // Press add
    DOMTest.fire('click', {}, buttons[0].htmlButton);
    Y.Assert.areEqual(1, list.size(), "List is supposed to contain one element.");
    
    // Press add
    DOMTest.fire('click', {}, buttons[0].htmlButton);
    Y.Assert.areEqual(2, list.size(), "List is supposed to contain two elements.");
    
    // Press remove
    DOMTest.fire('click', {}, buttons[1].htmlButton);
    Y.Assert.areEqual(1, list.size(), "List is supposed to contain one element.");
      
    // Add 3 more elements
    for (var i = 0; i < 3; i++)
    {
      DOMTest.fire('click', {}, buttons[0].htmlButton);
    }
    Y.Assert.areEqual(4, list.size(), "List is supposed to contain four elements.");
    
    // Clear list
    DOMTest.fire('click', {}, buttons[2].htmlButton);
    Y.Assert.areEqual(0, list.size(), "List is supposed to be empty.");
     console.log(3.5); 
    
    // Check to see if we can find the dialog
    var dialogId = dialog.getEl().getId();
    var dialogExists = false;
     console.log(3.6); 
    if (document.getElementById(dialogId))
    {
      dialogExists = true;
    }
    Y.Assert.areEqual(true, dialogExists, "Dialog is supposed to be here.");
      
    // Check to see if we can't
    DOMTest.fire('click', {}, buttons[3].htmlButton);
    if (document.getElementById(dialogId))
    {
      dialogExists = true;
    }
    Y.Assert.areEqual(false, dialogExists, "Dialog is supposed to be gone.");
   
  },
  /*
  testNewTabView : function()
  {
    var tab1Content = "<h1>This is a test tab.<h1>";
    var tab2Content = "<h2>This is another test tab.<h2>";
    var tab3Content = "<h3>This is also a test tab.<h3>";
    var arr = new Array(FACTORY.newTab('A', tab1Content), FACTORY.newTab('B', tab2Content), FACTORY.newTab('C', tab3Content));
    
    var tabView = FACTORY.newTabView({}, arr);
    
    var exitHandler = function() { dialog.destroy(); };
    var dialog = FACTORY.newDialog("TabView Test");
    //dialog.addButton("Add", addHandler, true);
    //dialog.addButton("Remove", removeHandler, true);
    //dialog.addButton("Clear", clearHandler, true);
    dialog.addButton("Exit", exitHandler);
    dialog.render();
    dialog.appendChild(tabView);
  },
  */
  testNewDataTable : function()
  {
    var dialog = FACTORY.newDialog("My Data Table");
    
    //var dataSource = FACTORY.ArrayDataSource(array);
    var dataTable = FACTORY.newDataTable();
    dataTable.acceptArray(this.arrayData);
    
    var arrayData = this.arrayData;
    
    dialog.appendChild(dataTable);
    dialog.addButton("Add Row", function(){
      dataTable.addRow(arrayData[Math.floor(Math.random()*5)]);
    });
    dialog.addButton("Add 20 Rows", function(){ dataTable.addRow(arrayData[3],20); });
    dialog.addButton("Delete Row", function(){ dataTable.deleteRow(1); });
    dialog.addButton("Delete 20 Rows", function(){ dataTable.deleteRow(1,20); });
    dialog.render();
  }
  
});

TestFramework.newTestCase(SUITE_NAME, {
   
  name: "ComponentAPITests",
  
  setUp : function()
  {
  },
  
  tearDown : function()
  {
  },
  
  testListAPI : function()
  {
    var arr = [];
    arr[0] = new MockDTO("test-item-1");
    arr[1] = new MockDTO("test-item-B");
    arr[2] = new MockDTO("test-item-the-third");
    
    // Instantiating the list with an array of ListItem-wrapped DTOs
    var list = FACTORY.newList("Test List", {id: 'testList'}, arr);
    Y.Assert.areEqual(3, list.size(), "List should have 3 elements.");
    
    // Test that the items are returned in order
    var itemArr = list.getAllItems();
    Y.Assert.areEqual(3, itemArr.length, "3 ListItems should have been retrieved.");
    for(var i=0; i<arr.length; i++){
      Y.Assert.isTrue(itemArr[i].getData().equals(arr[i]));
    }
    
    // Removing the last item added
    list.removeItem();
    Y.Assert.areEqual(2, list.size(), "List should have 2 elements.");
    
    // Adding an item
    var someData = new MockDTO("test-item-the-next-generation");
    var item = list.addItem(someData);  // ListItem
    var itemId = item.getId();  // ListItem ID
    Y.Assert.areEqual(3, list.size(), "List should have 3 elements.");
    Y.Assert.areEqual(true, list.hasItem(item), "List should now contain new third element (by object)");
    Y.Assert.areEqual(true, list.hasItem(itemId), "List should now contain new third element (by ID)");
    
    // Trying to create an empty ListItem should fail
    var failed = false;
    try {
      var badItem = list.addItem();
    } catch (e) {
      failed = true;
    }
    Y.Assert.areEqual(true, failed, "Creating a ListItem without a DTO should fail.");
    
    // Trying to add a non-ListItem should fail
    failed = false;
    try {
      list.addItem([]);
    } catch (e) {
      failed = true;
    }
    Y.Assert.areEqual(true, failed, "Adding a non-Base-extending class should fail.");
    
    // Trying to remove item at position 50 should fail
    failed = false;
    try {
      list.removeItem(49);
    } catch (e) {
      failed = true;
    }
    Y.Assert.areEqual(true, failed, "Removing an item out of bounds should fail.");
    
    // Getting an item
    var thirdItemId = itemId;
    var thirdItemIndex = 2;
    Y.Assert.areEqual(item, list.getItem(thirdItemId), "List could not retrieve item by element ID.");
    Y.Assert.areEqual(item, list.getItem(thirdItemIndex), "List could not retrieve item by list item index.");
    
    // Replacing an item
    var replacementItem = FACTORY.newListItem(new MockDTO("replaced"));
    var itemToBeReplaced = list.getItem(0);
    list.replaceItem(replacementItem, itemToBeReplaced);
    Y.Assert.areEqual(false, list.hasItem(itemToBeReplaced), "List should no longer contain original first element.");
    Y.Assert.areEqual(true, list.hasItem(replacementItem), "List should now contain new replacement element");
    
    // Getting an item from DOM LI
    var rawLI = replacementItem.getEl().getRawEl();
    Y.Assert.areEqual(replacementItem, list.getItemByLI(rawLI), "GetItemByLI fails");
    
    // Clearing all items
    list.clearItems();
    Y.Assert.areEqual(0, list.size(), "List should be empty now.");
  }
  
});

/**
 * This suite tests to make sure that all of the methods you can call on an instance of HTMLElement
 * work as expected.
 */
TestFramework.newTestCase(SUITE_NAME, {
   
  name: "HTMLElementTests",
  
  setUp : function() {
    var body = RUNWAY_UI.DOMFacade.getBody();
    
    this._el = FACTORY.newElement("div");
    this._el.render();
    
    this._el2 = FACTORY.newElement("div");
    this._el2.render();
    
    this._el3 = FACTORY.newElement("div");
    this._el2.appendChild(this._el3);
    
    this._rawEl = this._el.getRawEl();
  },
  
  tearDown : function() {
    RUNWAY_UI.DOMFacade.getBody().removeChild(this._el);
    RUNWAY_UI.DOMFacade.getBody().removeChild(this._el2);
  },
  
  testEquals : function() {
    // Testing equality between wrapped and raw IDs
    Y.Assert.areEqual(this._rawEl.id, this._el.getId());
    Y.Assert.areEqual(true, this._el.equals(this._rawEl));
    
    var wEl = FACTORY.newElement(this._rawEl);
    Y.Assert.areEqual(true, this._el.equals(wEl));
  },
  
  testGetSetId : function() {
    // Testing setId across wrapped and raw dom elements
    this._el.setId('testId');
    Y.Assert.areEqual('testId', this._el.getId());
    Y.Assert.areEqual(this._rawEl.id, this._el.getId());
  },
  
  testGetSetStyle : function() {
    this._el.setStyle('height','20px');
    Y.Assert.areEqual('20px', this._el.getStyle('height'));
  },
  
  testClassNames : function() {
    this._el.addClassName("foo");
    this._el.addClassName("foobar");
    this._el.addClassName("foos");
    this._el.addClassName("qfoo");
    this._el.addClassName("Foo");
    this._el.removeClassName("foo");
    
    Y.Assert.areEqual(false, this._el.hasClassName("foo"));
  },
  
  testHTMLMethods : function() {
    // Testing set InnerHTML
    this._el.setInnerHTML("<b>test</b>");
    Y.Assert.areEqual("<b>test</b>", this._el.getInnerHTML(), "SubTest 1 failed: The inner HTML was not correctly retreived.");
    this._el.render();
    Y.Assert.areEqual("test", this._el.getRawEl().firstChild.innerHTML, "SubTest 2 failed: The inner HTML was not rendered correctly.");
    
    // Testing append InnerHTML
    this._el.appendInnerHTML("<b>testys</b>");
    Y.Assert.areEqual("<b>test</b><b>testys</b>", this._el.getInnerHTML());
    Y.Assert.areEqual("testys", this._el.getRawEl().children[1].innerHTML, "SubTest 3 failed: The appendInnerHTML did not append correctly.");
  },
  
  testAttributeMethods : function() {
    // Testing hasAttribute on a non-existent attribute
    Y.Assert.areEqual(false, this._el.hasAttribute("foo"), "SubTest 1 failed: hasAttribute on a non-existent attribute did not return the expected value.");
    
    // Getting a non-existent attribute
    Y.Assert.areEqual(null, this._el.getAttribute("foo"), "SubTest 2 failed: getting a non-existent attribute did not return the expected value.");
    
    // Setting an attribute and then getting it
    this._el.setAttribute('foo','bar');
    Y.Assert.areEqual('bar', this._el.getAttribute('foo'), "SubTest 3 failed: getting a previously set attribute did not return the expected value.");
    
    // Testing hasAttribute on an existing attribute
    Y.Assert.areEqual(true, this._el.hasAttribute('foo'), "SubTest 4 failed: hasAttribute on a previously set attribute did not return the expected value.");
    
    // Removing an attribute and then getting it
    this._el.removeAttribute('foo');
    Y.Assert.areEqual(null, this._el.getAttribute('foo'), "SubTest 5 failed: removing an attribute and then getting it did not return the expected value.");
    
    // Testing hasAttribute on a removed attribute
    Y.Assert.areEqual(false, this._el.hasAttribute('foo'), "SubTest 6 failed: hasAttribute on a previously removed attribute did not return the expected value.");
    
    // Setting an attribute to null and then getting it, which should return the toStringed value of null.
    // This behavior differs between browsers so we want to standardize it. Chrome returns "null" whereas firefox returns ""
    // It makes the most sense to me to toString the value anyway so that's what I went with.
    this._el.setAttribute('foo', null);
    Y.Assert.areEqual('null', this._el.getAttribute('foo'), "SubTest 7 failed: setting an attribute to null and then getting it did not return the expected value.");
  },
  
  testGetElementsByClassName : function() {
    this._el3.addClassName("foo");
    var els = this._el2.getElementsByClassName('foo', 'div');
    var elarr = Array.prototype.slice.call(els, 0, els.length);
    Y.Assert.areEqual(elarr.length, 1);
  },
  
  testEventFire : function()
  {
    var testHandler = function() { alert("Test fired successfully."); };
    DOMTest.fire('click', testHandler, this._el3);
  }
  
});

TestFramework.newTestCase(SUITE_NAME, {
	 
	name: "MiscellaneousTests",
	
	_should: {
		error: {
      //test : com.runwaysdk.Exception,
		}
	},
  
  testListDragDrop : function() {
    var dialog = FACTORY.newDialog("List Drag Drop Test", {width: "38em", height: "320px"});
    
    // First List
    var list1 = FACTORY.newList("List 1", {id: "list1", draggable: {constrain2node: dialog.getContentEl().getRawEl()}, droppable: true});
    for (var i = 0; i < 20; i++) {
      var listItem = FACTORY.newListItem(new MockDTO("item " + i));
      listItem.getEl().setStyles({backgroundColor: "#8DD5E7", border: "1px solid #004C6D;"});
      list1.addItem(listItem);
    }
    list1.getEl().setStyle("float", "left");
    var drag = FACTORY.makeDraggable(list1);
    var drop = FACTORY.makeDroppable(list1);
    dialog.appendChild(list1);
    
    // Second List
    var list2 = FACTORY.newList("List 2", {id: "list2", draggable: {constrain2node: dialog.getContentEl().getRawEl()}, droppable: true});
    for (var i = 0; i < 20; i++) {
      var listItem = FACTORY.newListItem(new MockDTO("item " + i));
      listItem.getEl().setStyles({backgroundColor: "#EDFF9F", border:"1px solid #CDCDCD;"});
      list2.addItem(listItem);
    }
    list2.getEl().setStyle("float", "left");
    var drag2 = FACTORY.makeDraggable(list2);
    var drop2 = FACTORY.makeDroppable(list2);
    dialog.appendChild(list2);
    
    dialog.render();
  }
  
  // This is a runway DataTable specific test
  /*
  testCustomTableStyles : function() {
    var changeSkin = function(){
      dataTable.getHeaderRow().getCell(3).setStyle("background-color", "black");
      dataTable.getHeaderRow().getCell(3).setStyle("font-size", "30px");
      dataTable.getRow(3).getCell(4).setStyle("background-color", "black");
      dataTable.getColumn(2).setStyle("background-color", "green");
      dataTable.getRow(5).setStyle("background-color", "purple");
    };
    var removeSkin = function() {
      dataTable.getHeaderRow().getCell(3).removeAttribute("style");
      dataTable.getRow(3).getCell(4).removeAttribute("style");
      dataTable.getColumn(2).setStyle("background-color", "");
      dataTable.getRow(5).setStyle("background-color", "");
    };
    
    var dataTable = FACTORY.newDataTable();
    dataTable.acceptArray(this.arrayData);
    
    var dialog = FACTORY.newDialog("Skinny Dialog");
    dialog.appendChild(dataTable);
    dialog.addDialogButton("Normal", removeSkin);
    dialog.addDialogButton("Modded", changeSkin);
    dialog.render();
  },
  */
  
  /*
  testDTOTable : function()
  {
    var yuiTest = this;
    
    Mojo.ClientSession.setNativeParsingEnabled(false);
    
    var cb = function (testInstance) {
      var dialog = FACTORY.newDialog("My Data Table");
    
      var dataSource = FACTORY.DTODataSource();
      dataSource.defineDTO(com.runwaysdk.jstest.TestClass, testInstance.getId());
      dataSource.mapKeys({
        //Class : "TestClass",
        testInteger : "getTestInteger",
        testLong : "getTestLong",
        testFloat : "getTestFloat",
        testDecimal : "getTestDecimal",
        testCharacter : "getTestCharacter",
        testText : "getTestText",
        testBoolean : "getTestBoolean"
      })
      
      var cb2 = function() {
        var dataTable = FACTORY.newDataTable(dataSource);
      
        dialog.appendChild(dataTable);
        dialog.render();
        
        yuiTest.resume();
      }
      
      dataSource.fetchData(cb2);
    }
    
    var testInstance = new com.runwaysdk.jstest.TestClass();
    
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
    
    testInstance.setTestInteger(values.testInteger);
    testInstance.setTestLong(values.testLong);
    testInstance.setTestFloat(values.testFloat);
    testInstance.setTestDecimal(values.testDecimal);
    testInstance.setTestDouble(values.testDouble);
    testInstance.setTestCharacter(values.testCharacter); 
    testInstance.setTestText(values.testText); 
    testInstance.setTestBoolean(values.testBoolean);
    
    testInstance.apply(new Mojo.ClientRequest({
      onSuccess : cb
    }));
    
    yuiTest.wait(5000);
  }*/
});

})();