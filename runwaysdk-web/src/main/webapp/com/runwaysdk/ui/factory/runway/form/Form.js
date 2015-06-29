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
 * RunwaySDK Javascript UI Adapter for native Runway elements.
 * 
 * @author Terraframe
 */
//define(["../widget/Widget"], function(Widget){
(function(){

  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  
var RUNWAY_UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");
var STRUCT = Mojo.Meta.alias(Mojo.ROOT_PACKAGE+'structure.*', {});

var Visitable = Mojo.Meta.newInterface(Mojo.RW_PACKAGE+'Visitable', {
  Instance : {
    accept : function(visitor){}
  }
});

var Form = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'Form', {
  Extends : Widget,
  Implements : Visitable,
  Instance : {
    initialize : function(config)
    {
      config = config || {};
      config.name = config.name || "";
      config.action = config.action || "";
      config.method = config.method || "POST";
      this._config = config;
      
      this.$initialize("form");
      
      this.setName(this._config.name);
      this.setAction(this._config.action);
      this.setMethod(this._config.method);
      
      this._formList = new FormList();
      this.appendChild(this._formList);
    },
    /**
     * Convenience method:
     * Creates a new input of type, sets optional name & defaultValue, calls add entry.
     */
    addInput : function(type, name, defalutValue) {
      var input = this.newInput(type, name);
      input.setValue(defaultValue);
      this.addEntry(name, input);
    },
    /**
     * Convenience method, invokes accept with the default visitor.
     * 
     * @returns map
     */
    getValues : function() {
      return this.accept(new FormVisitor());
    },
    newInput : function(type, name, config) {
      if (type === "text")
      {
        return new TextInput(name, config);
      }
      else if (type === "textarea")
      {
        return new TextArea(name, config);
      }
      else if (type === "hidden")
      {
        return new HiddenInput(name, config);
      }
      else if (type === "select")
      {
        return new Select(name, config);
      }
      else
      {
        throw new com.runwaysdk.Exception("Input type ["+type+"] not implemented");
      }
    },
    getAction : function() {
      return this.getRawEl().action;
    },
    setAction : function(action) {
      this.getRawEl().action = action;
    },
    getMethod : function() {
      return this.getRawEl().method;
    },
    setMethod : function(method) {
      this.getRawEl().method = method;
    },
    getName : function() {
      return this.getRawEl().name;
    },
    setName : function(method) {
      this.getRawEl().name = name;
    }, 
    newVisitor : function(type, config) {
      if (type == "FormVisitor") {
        return new FormVisitor(config);
      }
      else if (type == "ConsoleFormVisitor") {
        return new ConsoleFormVisitor(config);
      }
      else
      {
        throw new com.runwaysdk.Exception("Input type ["+type+"] not implemented");
      }
    },
    // Takes a string for term and a FormInput as definition
    addEntry : function(label, input)
    {
      var formEntry = new FormEntry(label, input);
      this._formList.addEntry(formEntry);
      return formEntry;
    },
    getEntries : function()
    {
      return this._formList.getEntries();
    },
    _generateFormId : function() {
      return this.getId()+'_RW_Form';
    },
    getChildren : function()
    {
      var children = this.$getChildren();
      var formListChildren = this._formList.getChildren();
      children = children.concat(formListChildren);
      return children;
    },
    accept : function(visitor)
    {
      visitor.visitForm(this);
      var inputs = this.getChildren();
      for (var i = 0; i < inputs.length; i++)
      {
        if (!(inputs[i] instanceof FormList)) {
          inputs[i].accept(visitor);
        }
      }
      
      return visitor.finishAndReturn();
    },
    getWidget : function (widgetId)
    {
      var widgets = this.getEntries().values();
      
      for (var i = 0; i < widgets.length; ++i) {
        if(widgets[i].getId() === widgetId)
        {
          return widgets[i];
        }
      }  
      
      return null;
    },
    removeErrorMessages : function ()
    {    
      var widgets = this.getEntries().values();
        
      for (var i = 0; i < widgets.length; ++i)
      {
        this.removeInlineError(widgets[i]);
      }      
    },
    removeInlineError : function (widget)
    {
      var div = document.getElementById(widget.getId() + "-error");
        
      if(div != null)
      {
        div.innerHTML = '';              
      }          
    },    
    addInlineError : function (widget, msg) {
      var div = document.getElementById(widget.getId() + "-error");
      
      if(div == null)
      {
        div = this.getFactory().newElement('div', {class:"error-message", id:widget.getId() + "-error"});
        //div.style.display = 'inline-block';
        
        var parent = widget.getParent();
        
        if(parent.getLastChild() == widget.getRawEl())
        {
          parent.appendChild(div);
        }
        else
        {
          parent.insertBefore(div, widget.getRawEl().nextSibling);
        }        
        
        div.setInnerHTML(msg);
      }
      else
      {
        div.innerHTML = msg;      
      }
    },
    handleException : function(ex, throwIt)
    {
      var msg = null;
      
      try {          
        if (ex instanceof com.runwaysdk.ProblemExceptionDTO && msg == null) {
          var problems = ex.getProblems();
          var globalMessages = [];
            
          for (var i = 0; i < problems.length; i++) {
            var problem = problems[i];
            var attributeName = problem.getAttributeName();
            
            var widget = this.getWidget(attributeName);
            
            if(widget != null)
            {
              this.addInlineError(widget, problems[i].getLocalizedMessage());
            }
            else
            {
              globalMessages.push(problems[i].getLocalizedMessage());            
            }
          }
          
          if(globalMessages.length > 0) {
            msg = com.runwaysdk.Localize.get("problems", "Problems processing request:\n");    
            
            for (var i = 0; i < globalMessages.length; i++) {
              msg += globalMessages[i] + "\n";
            }
          }
        }
        else {
          var msg = ex.getLocalizedMessage();          
        }
          
        if(msg != null)
        {
          var dialog = this.getFactory().newDialog(com.runwaysdk.Localize.get("rError", "Error"), {modal: true});
          dialog.appendContent(msg);
          dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){dialog.close();});
          dialog.render();
        }
          
        if (throwIt) {
          throw ex;
        }
      }
      catch(e2) {
        throw ex;
      }
    }    
  }
});

var Label = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'Label', {
  Extends : Widget,
  Implements : RUNWAY_UI.ElementProviderIF,
  Instance : {
    initialize : function(text, elFor)
    {
      this.$initialize('label', {htmlFor:elFor});
      this._text = text;
      this.setInnerHTML(text);
    },
    getText : function() {
      return this._text;
    },
    toString : function()
    {
      return this.$toString() + " [" + this.getText() + "]";
    }
  }
});

var FormInput = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'FormInput', {
  Extends : Widget,
  Implements : [RUNWAY_UI.ElementProviderIF, Visitable],
  IsAbstract : true,
  Instance : {
    initialize : function(el, attrs, name, config)
    {
      this.$initialize(el, attrs);
      this._name = name || this.getId();
      this._value = config.value || "";
      this._disabled = config.disabled || false;
      this._readonly = config.readonly || false;
      this.setName(this._name);
    },
    getName : function()
    {
      return this.getRawEl().name;
    },
    setName : function(name)
    {
      this.getRawEl().name = name;
    }
  }
});

var TextInput = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'TextInput', {
  Extends : FormInput,
  Instance : {
    initialize : function(name, config)
    {
      config = config || {};
      config.type = 'text';
      config.attributes = config.attributes || {type:'text'}
      
      this.$initialize('input', config, name, config);
    },
    accept : function(visitor)
    {
      visitor.visitTextInput(this);
    },
    getValue : function()
    {
      return this.getRawEl().value;
    },
    setValue : function(val) {
      this.getRawEl().value = val; 
    }
  }
});

var TextArea = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'TextArea', {
  Extends : FormInput,
  Instance : {
    initialize : function(name, config)
    {
      config = config || {};
      this._rows = config.rows || 5;
      this._cols = config.cols || 40;
      config.rows = this._rows;
      config.cols = this._cols;
      this.$initialize('textarea', config, name, config);
    },
    accept : function(visitor)
    {
      visitor.visitTextArea(this);
    },
    getValue : function()
    {
      return this.getRawEl().value;
    },
    setValue : function(val) {
      this.getRawEl().value = val; 
    }
  }
});

var Select = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'Select', {
  Extends : FormInput,
  Instance : {
    initialize : function(name, config)
    {
      config = config || {};
      var options = config.options || [];
      this._multiple = config.multiple || false;
      this.$initialize('select', config, name, config);
      if (this._multiple)
      {
        this.getRawEl().multiple = "multiple";
      }
      this._options = [];
    },
    addOption : function(name, valueOf, isSelected)
    {
      isSelected = isSelected || false;
      var opt = this.getFactory().newElement('option', {value: valueOf});
      if (isSelected)
      {
        opt.getRawEl().selected = "selected";
      }
      opt.setInnerHTML(name);
      this._options.push(name);
      this.appendChild(opt);
    },
    getOptions : function()
    {
      return this._options;
    },
    accept : function(visitor)
    {
      visitor.visitSelect(this);
    },
    isMultiple : function()
    {
      return this._multiple;
    },
    getValues : function()
    {
      var selectedOptions = [];
      var rawEl = this.getRawEl();
      for (var i = 0; i < rawEl.options.length; i++)
      {
        if (rawEl.options[i].selected)
        {
          selectedOptions.push(rawEl.options[i]);
          if (!this.isMultiple())
          {
            return rawEl.options[i];
          }
        }
      }
      return selectedOptions;
    }
  }
});

var HiddenInput = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'HiddenInput', {
  Extends : FormInput,
  Instance : {
    initialize : function(name, config)
    {
      config = config || {};
      config.type = 'hidden';
      this.$initialize('input', config, name, config);
    },
    accept : function(visitor)
    {
      visitor.visitHidden(this);
    },
    getValue : function()
    {
      return this.getRawEl().value;
    },
    setValue : function(val) {
      this.getRawEl().value = val; 
    }
  }
});

var FormEntry = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'FormEntry', {
  Instance : {
    initialize : function(term, definition)
    {
      this._parent = null;
      this._rendered = false;
      this._isDestroyed = false;
      
      this._dt = this.getFactory().newElement('dt');
      this._dt.setId(this._generateTermId());
      this._dd = this.getFactory().newElement('dd');
      this._dd.setId(this._generateDefId());
      
      // FIXME ? is label.for using id or name of form input?
      //var defId;
      var defName;
      if (definition instanceof FormInput)
      {
        //defId = definition.getEl().getId();
        defName = definition.getName();
      }
      else
      {
        throw new com.runwaysdk.Exception("addEntry accepts only string for term.");
      }
      
      if (Mojo.Util.isString(term))
      {
        //term = new Label(term, defId);
        term = new Label(term, defName);
      }
      else
      {
        throw new com.runwaysdk.Exception("addEntry accepts only FormInput for definition.");
      }
      
      this._term = term;
      this._definition = definition;
      
      this._dt.appendChild(this._term);
      this._dd.appendChild(this._definition);
    },
    getTermEl : function()
    {
      return this._dt;
    },
    getDefEl : function()
    {
      return this._dd;
    },
    getDelimiterEl : function()
    {
      return this._delimiter;
    },
    getTerm : function()
    {
      return this._term;
    },
    getDef : function()
    {
      return this._definition;
    },
    _generateTermId : function() {
      return this.getId()+'_RW_Term';
    },
    _generateDefId : function() {
      return this.getId()+'_RW_Definition';
    },
    getManager: function()
    {
      return RUNWAY_UI.Manager.getInstance();
    },
    getFactory : function()
    {
      return this.getManager().getFactory();
    },
    setParent : function(parent)
    {
      if (Mojo.Util.isUndefined(parent)) {
        throw new com.runwaysdk.Exception("The argument to Component.setParent cannot be undefined.");
      }
      
      // Parent is allowed to be null
      if (parent !== null && !RUNWAY_UI.Util.isComponentIF(parent)) {
        throw new com.runwaysdk.Exception("The argument to Component.setParent must implement ComponentIF.");
      }
      
      this._parent = parent;
    },
    getParent : function()
    {
      return this._parent;
    },
    getId : function()
    {
      return this._id;
    },
    getHashCode : function()
    {
      return this.getId();
    },
    equals : function(obj)
    {
      var eq = this.$equals(obj);
      if(eq)
      {
        return true;
      }
      else if(obj instanceof Mojo.$.com.runwaysdk.Base
        && this.getId() === obj.getId())
      {
        return true;
      }
      
      return this === obj;
    },
    setId : function(id)
    {
      this._id = id;
    },
    _generateId : function()
    {
      return this.getMetaClass().getName()+'_'+Mojo.Util.generateId(16);
    },
    render : function()
    {
      this._rendered = true;
    },
    isRendered : function()
    {
      return this._rendered;
    },
    _setRendered : function(rendered)
    {
      this._rendered = rendered;
    },
  }
});

var FormList = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'FormList', {
  Extends : Widget,
  Instance : {
    initialize : function()
    {
      this.$initialize("dl");
      this._entries = new STRUCT.LinkedHashMap();
    },
    getChildren : function()
    {
      return this._entries.values();
    },
    getChild : function(id)
    {
      return this.getEl().getChild(id);
    },
    hasChild : function(child)
    {
      return this.getEl().hasChild(child);
    },
    addEntry : function(formEntry, delimiter)
    {
//      delimiter = delimiter || this.getFactory().newElement('br');
      formEntry.setParent(this);
      this._entries.put(formEntry.getTerm().getText(), formEntry.getDef());
      this.appendChild(formEntry.getTermEl());
      this.appendChild(formEntry.getDefEl());
//      this.appendChild(delimiter);
    },
    getEntries : function()
    {
      return this._entries;
    }
  }
});

var Visitor = Mojo.Meta.newInterface(Mojo.RW_PACKAGE+'Visitor', {
  Instance : {
    visitForm : function(form){},
    visitTextInput : function(textInput){},
    visitTextArea : function(textArea){},
    visitHidden : function(hidden){},
    visitSelect : function(select){},
    finishAndReturn : function(){}
  }
});

var FormVisitor = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'FormVisitor', {
  Implements : Visitor,
  Instance : {
    initialize : function()
    {
      this._values = new STRUCT.HashMap();
    },
    visitForm : function(form)
    {
      
    },
    visitDefaultInput : function(input) {
      var key = input.getName();
      var value = input.getValue();
      this._values.put(key, value);
    },
    visitTextInput : function(textInput)
    {
      var key = textInput.getName();
      var value = textInput.getValue();
      this._values.put(key, value);
    },
    visitTextArea : function(textArea)
    {
      var key = textArea.getName();
      var value = textArea.getValue();
      this._values.put(key, value);
    },
    visitHidden : function(hidden)
    {
      var key = hidden.getName();
      var value = hidden.getValue();
      this._values.put(key, value);
    },
    visitSelect : function(select)
    {
      var key = select.getName();
      var values = select.getValues();
      this._values.put(key, values);
    },
    finishAndReturn : function() {
      return this._values;
    }
  }
});

var ConsoleFormVisitor = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'ConsoleFormVisitor', {
  Implements: Visitor,
  Instance : {
    initialize : function()
    {
      //no instance anything =\
    },
    visitForm : function(form)
    {
      console.log("Now visiting form");
    },
    visitTextInput : function(textInput)
    {
      var key = textInput.getName();
      var value = textInput.getValue();
      console.log("Now visiting ["+key+"], value: ["+value+"].");
    },
    visitTextArea : function(textArea)
    {
      var key = textArea.getName();
      var value = textArea.getValue();
      console.log("Now visiting ["+key+"], value: ["+value+"].");
    },
    visitHidden : function(hidden)
    {
      var key = hidden.getName();
      var value = hidden.getValue();
      console.log("Now visiting ["+key+"], value: ["+value+"].");
    },
    visitSelect : function(select)
    {
      var key = select.getName();
      var values = select.getValues();
      var valueString = "";
      for (var i = 0; i < values.length; i++)
      {
        if (i == 0) 
        {
          valueString = values[i].value;
        }
        else
        {
          valueString = valueString + ", " + values[i].value;
        }
      }
      console.log("Now visiting ["+key+"] value: ["+valueString+"].");
    },
    finishAndReturn : function() {
      
    }
  }
});

return Mojo.Meta.alias(Mojo.RW_PACKAGE+"*");

})();
