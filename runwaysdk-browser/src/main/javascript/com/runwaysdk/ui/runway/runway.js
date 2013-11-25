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
(function(){

var RUNWAY_UI = Mojo.Meta.alias("com.runwaysdk.ui.*");
Mojo.RW_PACKAGE = Mojo.UI_PACKAGE+'RW.';

var Factory = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'Factory', {
  
  IsSingleton : true,
  
  Implements : RUNWAY_UI.AbstractComponentFactoryIF,
  
  Instance: {
  
    newElement: function(el, attributes, styles) {
      if (RUNWAY_UI.Util.isElement(el)) {
        return el;
      }
      
      return new HtmlElement(el);
    },
    newDocumentFragment : function(el){
      return new com.runwaysdk.ui.RW.DocumentFragment(el);
    },
    newDialog: function(title){
//      throw new com.runwaysdk.Exception('Not implemented');
      return new com.runwaysdk.ui.RW.Dialog(title);
    },
    newButton : function(label, handler, el){
      return new com.runwaysdk.ui.RW.Button(label, handler, el);
    },
    newList : function (title, config, items) {
      return new com.runwaysdk.ui.RW.List(title, config, items);
    },
    newListItem : function(data){
      return new com.runwaysdk.ui.RW.ListItem(data);
    },
    newForm : function(config){
      return new com.runwaysdk.ui.RW.Form(name, config);
    },
    newFormControl : function(type, config){
      
      if (type === "text")
      {
        return new com.runwaysdk.ui.RW.TextInput(config);
      }
      else if (type === "textarea")
      {
        return new com.runwaysdk.ui.RW.TextArea(config);
      }
      else if (type === "hidden")
      {
        return new com.runwaysdk.ui.RW.HiddenInput(config);
      }
      else if (type === "select")
      {
        return new com.runwaysdk.ui.RW.Select(config);
      }
      else
      {
        throw new com.runwaysdk.Exception("Input type ["+type+"] not implemented");
      }
    },
    newDataTable : function (type) {
      return new com.runwaysdk.ui.RW.DataTable(type);
    },
    newColumn : function(config){
      return new com.runwaysdk.ui.RW.Column(config);
    },
    newRecord : function(obj){
      return new com.runwaysdk.ui.RW.Record(obj);
    },
    makeDraggable : function(elProvider, config) {
      com.runwaysdk.ui.RW.DragDrop.makeDraggable(elProvider, config.dragHandle);
    },
    makeDroppable : function(elProvider, config) {
      throw new com.runwaysdk.Exception('Not implemented');
    }
  }
});
RUNWAY_UI.Manager.addFactory("Runway", Factory);

/*
 * Runway implementations
 */

var Node = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'Node', {
  Extends : RUNWAY_UI.Component,
  Instance : {
    initialize : function(rawdom)
    {
      this._node = rawdom;
      this.$initialize();
    },
    getChild : function(child){
      throw new com.runwaysdk.Exception('not implemented');
    },
    hasChild : function(child){
      throw new com.runwaysdk.Exception('not implemented');
    },
    getChildren : function(){
      throw new com.runwaysdk.Exception('not implemented');
    },
    // DOM Methods
    appendChild : function(newChild)
    {
      newChild = RUNWAY_UI.Util.toElement(newChild, true);
      this.$appendChild(newChild);
//      return this.getRawNode().appendChild(newChild.getRawNode());
    },
    cloneNode : function(deep)
    {
      return this.getRawNode().cloneNode(deep);
    },
    hasAttributes : function()
    {
      return null;
    },
    hasChildNodes : function()
    {
      return this.getRawNode().hasChildNodes();
    },
    insertBefore : function(newChild, refChild)
    {
      newChild = RUNWAY_UI.Util.toRawElement(newChild);
      refChild = RUNWAY_UI.Util.toRawElement(refChild);
      return this.getRawNode().insertBefore(newChild, refChild);
    },
    isSupported : function(feature, version)
    {
      this.getRawNode().isSupported(feature, version);
    },
    normalize : function()
    {
      this.getRawNode().normalize();
    },
    removeChild : function(oldChild)
    {
      oldChild = RUNWAY_UI.Util.toElement(oldChild, true);
      this.$removeChild(oldChild);
//      return this.getRawNode().removeChild(oldChild.getRawNode());
    },
    replaceChild : function(newChild, oldChild)
    {
      newChild = RUNWAY_UI.Util.toRawElement(newChild);
      oldChild = RUNWAY_UI.Util.toRawElement(oldChild);
      return this.getRawNode().replaceChild(newChild, oldChild);
    },

    // Accessors for DOM attributes (not the attr kind)
    getNodeName : function()
    {
      return this.getRawNode().nodeName;
    },
    getNodeValue : function()
    {
      return this.getRawNode().nodeValue;
    },
    getNodeType : function()
    {
      return this.getRawNode().nodeType;
    },
    getParentNode : function()
    {
      return this.getRawNode().parentNode;
    },
    getChildNodes : function()
    {
      return this.getRawNode().childNodes;
    },
    getLastChild : function()
    {
      return this.getRawNode().lastChild;
    },
    getPreviousSibling : function()
    {
      return this.getRawNode().previousSibling;
    },
    getNextSibling : function()
    {
      return this.getRawNode().nextSibling;
    },    
    getOwnerDocument : function()
    {
      return this.getRawNode().ownerDocument;
    },
    
    // Runway Methods
    getRawNode : function()
    {
      return this._node;
    },
    getImpl : function()
    {
      return this;
    }
  }
});

var Element = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'Element', {
  Implements: [RUNWAY_UI.ElementIF, RUNWAY_UI.ElementProviderIF],
  Extends : Node,
  Instance: {
    initialize : function(el, attributes, styles)
    {
      // FIXME : change the element parameter to accept an ID and not a type string (like 'div')
      var rawEl;
    
      if(Mojo.Util.isString(el))
      {
        rawEl = RUNWAY_UI.DOMFacade.createElement(el);
      }
      else if(Mojo.Util.isElement(el))
      {
        rawEl = el;
      }
      else
      {
        throw new com.runwaysdk.Exception('The first argument must be a node name or reference to an Element.');
      }
  
      RUNWAY_UI.DOMFacade.updateElement(rawEl, attributes, styles);
  
      this.$initialize(rawEl);
    },
    // DOM Methods
    getAttribute : function(name)
    {
      RUNWAY_UI.DOMFacade.getAttribute(this.getRawEl(), name);
    },
    setAttribute : function(name, value)
    {
      RUNWAY_UI.DOMFacade.setAttribute(this.getRawEl(), name, value);
    },
    setAttributes : function(attrs)
    {
      for (var key in attrs) {
        this.setAttribute(key, attrs[key]);
      }
    },
    removeAttribute : function(name)
    {
      return RUNWAY_UI.DOMFacade.removeAttribute(this.getRawEl(), name);
    },
    getAttributeNode : function(name)
    {
      var attr = this.getRawEl().getAttributeNode(name);
      if (Mojo.Util.isValid(attr))
      {
        return this.getFactory().wrapAttr(attr, Util.toRawElement(this));
      }
      else
      {
        return null; 
      }
    },
    setAttributeNode : function(newAttr)
    {
      var oldAttr = null;
      if (RUNWAY_UI.Util.isAttr(newAttr))
      {
        var name = newAttr.getName();
        var value = newAttr.getValue();
        if (this.hasAttribute(name)) 
        {
          oldAttr = this.getAttribute(name);
        }
        this.setAttribute(name, value);
      }
      else if (RUNWAY_UI.Util.isRawAttr(newAttr))
      {
        this.getRawEl().setAttributeNode(newAttr);
      }
      return oldAttr;
    },
    removeAttributeNode : function(oldAttr)
    {
      var oldNode = null;
      if (RUNWAY_UI.Util.isAttr(oldAttr))
      {
        if (this.hasAttribute(oldAttr.getName()))
        {
          oldNode = this.getAttribute(oldAttr.getName());
          this.removeAttribute(oldAttr.getName());
        }
      }
      return oldNode;
    },
    hasAttribute : function(name)
    {
      return RUNWAY_UI.DOMFacade.hasAttribute(this.getRawEl(), name);
    },
    getElementsByClassName:function(className)
    {
      return this.getRawEl().getElementsByClassName(className);
    },
    // Accessors for DOM attributes (not the attr kind)
    getTagName : function()
    {
      return this.getRawEl().tagName;
    },
    getRawEl : function()
    {
      return this.getRawNode();
    },
    getValue : function()
    {
      return this.getRawEl().value;
    },
    setValue : function(value)
    {
      this.getRawEl().value = value;
    },
    getNamespaceURI : function()
    {
      return this.getRawEl().getNamespaceURI();
    },
    getPrefix : function()
    {
      return this.getRawEl().prefix;
    },
    getLocalName : function()
    {
      return this.getRawEl().localName;
    },
    getAttributes : function()
    {
      return this.getRawEl().getAttributes();
    },
    // Runway Methods
    render : function(newParent)
    {
      var parent = RUNWAY_UI.Util.toRawElement(newParent || this.getParent());
      
      var el = this.getRawEl();
      
      parent.appendChild(el);
      
      this.$render();
    },
    getTextContent : function()
    {
      return RUNWAY_UI.DOMFacade.getTextContent(this.getRawEl());
      return el.textContent != null ? el.textContent : el.innerText;
    },
    setTextContent : function(text)
    {
      RUNWAY_UI.DOMFacade.setTextContent(this.getRawEl(), text);
      var el = this.getRawEl();
      if(el.textContent != null)
      {
        el.textContent = text;
      }
      else
      {
        el.innerText = text;
      }
    },
    setId : function(id)
    {
      this.setAttribute('id', id);
      this.$setId(id);
    },
    _generateId : function()
    {
      var id = this.getAttribute('id');
      if(Mojo.Util.isString(id) && id.length > 0)
      {
        return id;
      }
      else
      {
        var newId = 'el_'+Mojo.Util.generateId(16);
        this.setAttribute('id', newId);
        return newId;
      }
    },
    normalize : function()
    {
      this.getRawEl().normalize();
    },
    toString : function()
    {
      return 'Element: ['+this.getNodeName()+'] ['+this.getAttribute('id')+'].';
    },
    destroy : function()
    {
      this.$destroy();
      // TODO remove all event listeners
      //this.setEl(null);
    },
    getEl : function()
    {
      return this;
    },
    getContentEl : function()
    {
      return this;
    }
  }
});

var HtmlElement = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'HTMLElement', {
  
  Implements : RUNWAY_UI.HTMLElementIF,
  
  Extends : Element,
  
  Instance: {
    
    initialize: function(el, attributes, styles){
      el = RUNWAY_UI.Util.stringToRawElement(el);
      this.$initialize(el, attributes, styles);
    },
    render : function(newParent)
    {
      var parent = RUNWAY_UI.Util.toRawElement(newParent || this.getParent() || RUNWAY_UI.DOMFacade.getRawBody());
      this.$render(parent);
    },
    getElementsByClassName:function(className)
    {
      return this.getRawEl().getElementsByClassName(className);
    },
    setInnerHTML:function(html)
    {
      RUNWAY_UI.DOMFacade.setInnerHTML(this, html);
    },
    appendInnerHTML:function(html)
    {
      RUNWAY_UI.DOMFacade.appendInnerHTML(this, html);
    },
    getInnerHTML:function()
    {
      return DOMFacade.getInnerHTML(this);
    },
    setOuterHTML:function(html)
    {
      this.getRawEl().outerHTML = html;
    },
    getOuterHTML:function()
    {
      return this.getRawEl().outerHTML;
    },
    insertAdjacentHTML:function(position, text)
    {
      this.getRawEl().insertAdjacentHTML(position, text);
    },
    getTitle:function()
    {
      return this.getRawEl().getAttribute('title');
    },
    setTitle:function(title)
    {
      this.getRawEl().setAttribute('title', title);
    },
    getLang:function()
    {
      return this.getRawEl().getAttribute('lang');
    },
    setLang:function(lang)
    {
      this.getRawEl().setAttribute('lang', lang);
    },
    getDir:function()
    {
      return this.getRawEl().getAttribute('dir');
    },
    setDir:function(dir)
    {
      this.getRawEl().setAttribute('dir', dir);
    },
    addClassName : function(c)
    {
      RUNWAY_UI.DOMFacade.addClassName(this, c);
    },
    addClassNames : function(obj)
    {
      RUNWAY_UI.DOMFacade.addClassNames(this, obj);
    },
    hasClassName : function(c)
    {
      return RUNWAY_UI.DOMFacade.hasClassName(this, c);
    },
    removeClassName : function(c)
    {
      RUNWAY_UI.DOMFacade.removeClassName(this, c);
    },
    getClassName : function()
    {
      return RUNWAY_UI.DOMFacade.getClassName(this);
    },
    getDataset:function()
    {
      return this.getRawEl().dataset;// TODO use getAttribute() AND verify this property
    },
    isHidden:function()
    {
      return this.getRawEl().hidden; // TODO use getAttribute()/getStyle() AND verify this property
    },
    setHidden:function(hidden)
    {
      this.getRawEl().hidden = hidden; // TODO use setAttribute()/setStyle() AND verify this property
    },
    click:function()
    {
      this.getRawEl().click();
    },
    scrollIntoView:function(top)
    {
      this.getRawEl().scrollIntoView(top);
    },
    setStyle : function(style, value)
    {
      RUNWAY_UI.DOMFacade.setStyle(this.getRawEl(), style, value);
    },
    getStyle : function(style)
    {
      return RUNWAY_UI.DOMFacade.getStyle(this.getRawEl(), style);
    },
    setStyles : function(styles)
    {
      for (var key in styles) {
        this.setStyle(key, styles[key]);
      }
    },
    offsetLeft : function()
    {
      return this.getRawEl().offsetLeft;
    },
    offsetTop : function()
    {
      return this.getRawEl().offsetTop;
    },
    offsetWidth : function()
    {
      return this.getRawEl().offsetWidth;
    },
    offsetHeight : function()
    {
      return this.getRawEl().offsetHeight;
    },
    getSize : function()
    {
      return RUNWAY_UI.DOMFacade.getSize(this);
    },
    getPos : function()
    {
      return RUNWAY_UI.DOMFacade.getPos(this);
    }
  }
});

var Attr = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'Attr', {
  Extends : Node,
  Instance : {
    initialize : function(attr, parent)
    {
      this.$initialize(attr);
    },
    getName : function()
    {
      return this.getRawAttr().name;
    },
    getValue : function()
    {
      return this.getRawAttr().value;
    },
    setValue : function(value)
    {
      this.getRawAttr().value = value;
    },
    getOwnerElement : function()
    {
      return this.getFactory().newElement(this.getRawAttr().ownerElement);
    },
    hasAttributes : function()
    {
      return false;
    },
    getRawAttr : function()
    {
      return this.getRawNode();
    },
    getImpl : function()
    {
      return this;
    }
  }
});

/**
 * Wrapper for a DocumentFragment that delegates to an underlying node for its
 * implementation.
 */
var DocumentFragment = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'DocumentFragment', {
  Extends : RUNWAY_UI.HTMLElementBase,
  Instance : {
    initialize : function(el){
      this.$initialize();
      
      this._impl = el || RUNWAY_UI.DOMFacade.createDocumentFragment();
    },
    _generateId : function()
    {
      return this.getMetaClass().getName()+'_'+Mojo.Util.generateId(16);
    },
    getImpl : function(){
      return this._impl
    },
    getRawEl : function()
    {
      return this.getImpl();
    },
    setId : function(id){
      this._id = id;
    },
    getId : function(){
      return this._id;
    },
    getRawNode : function(){
      return this.getRawEl();
    },
    hasAttributes : function(){
      return false;
    },
    getChildren : function(){
      var childNodes = this.getRawEl().childNodes;
      
      var len = children.length;
      var elementIFs = [len];
      var f = this.getFactory();
      for(var i=0; i<len; i++)
      {
        elementIFs[i] = f.newElement(children[i]);
      }
      return elementIFs;
    }
  }
});

/**
 * Runway HTML5 Drag Drop Implementation
 */
var Draggable = Mojo.Meta.newInterface(Mojo.RW_PACKAGE+'Draggable', {
  Instance : {
    getDragDelegate : function(){}, // FIXME: never called with current YUI3 Impl
    getDraggables : function(){},
    getDragData : function(){},
    modifyDragDisplay : function(styles, node){},
    modifyDragGhostDisplay : function(styles, node){}
  }
});

var Droppable = Mojo.Meta.newInterface(Mojo.RW_PACKAGE+'Droppable', {
  Instance : {
    getDropDelegate : function(){},
    acceptDrop : function(e, data){},
    handleDrop : function(e, data){} // FIXME: never called with current YUI3 Impl
  }
});

var DragTarget = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'DragTarget', {
  Instance : {
    initialize : function(draggable)
    {
      this._draggable = draggable;
      
      // mark all draggable elements with the required draggable=true attribute
      var draggables = this._draggable.getDraggables();
      for(var i=0, len=draggables.length; i<len; i++)
      {
        draggables[i].getEl().setAttribute('draggable', true);
      }
      
      var el = this._draggable.getDragDelegate();
      el.addEventListener('dragstart', this.onDragStart, null, this);
      el.addEventListener('drag', this.onDrag, null, this);
      el.addEventListener('dragend', this.onDragEnd, null, this);
    },
    
    onDragStart : function(e)
    {
      var data = this._draggable.getDragData(e);
      
      var dt = e.getDataTransfer();
      dt.effectAllowed = 'move';
      // FIXME allow custom encoding (e.g., Files)
      e.getDataTransfer().setData('text/html', data);
      
      this._draggable.setDragDisplay(e);
    },
    
    onDrag : function(e)
    {
    },
    
    onDragEnd : function(e)
    {
    }
  }
});

var DropTarget  = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'DropTarget', {
  Instance : {
    initialize : function(droppable)
    {
      this._droppable = droppable;
      
      var el = this._droppable.getDropDelegate();
      el.addEventListener('dragenter', this.onDragEnter, null, this);
      el.addEventListener('dragleave', this.onDragLeave, null, this);
      el.addEventListener('dragover', this.onDragOver, null, this);
      el.addEventListener('drop', this.onDrop, null, this);
    },
    
    onDragEnter : function(e)
    {
      e.preventDefault();
    },
    
    onDragLeave : function(e)
    {
    },
    
    onDragOver : function(e)
    {
      e.preventDefault();
      e.getDataTransfer().dropEffect = 'move';
    },
    
    onDrop : function(e)
    {
      if(this._droppable.acceptDrop(e))
      {
        e.preventDefault();
        e.stopPropagation();
        
        this._droppable.handleDrop(e);
      }
    }
  }
});

/**
 * Raw DOM Drag Drop Implementation
 */
var DragDrop = Mojo.Meta.newClass(Mojo.RW_PACKAGE+"DragDrop", {

  IsSingleton : true,
  
  Instance : {
    
    initialize: function()
    {
      this.$initialize();
      
      this._dragHandles = new com.runwaysdk.structure.HashMap();
    },
    
    makeDraggable : function(el, dragHandle)
    {
      el = com.runwaysdk.ui.Util.toRawElement(el);
      dragHandle = com.runwaysdk.ui.Util.toRawElement(dragHandle);
      
      if (dragHandle != undefined)
      {
          this._dragHandles.put(el.id, dragHandle);
      }
      
      com.runwaysdk.ui.Util.disableSelection(el);
      com.runwaysdk.ui.DOMFacade.addClassName(el, "draggable");
    },
    
    handleEvent : function(event)
    {
      switch(event.type)
      {
        case "mousedown":
          if (com.runwaysdk.ui.DOMFacade.hasClassName(event.target, "draggable"))
          {
            this._dragObj = this._dragHandles.get(event.target.id) || event.target;
            this._mouseDownPos = com.runwaysdk.ui.Util.getMousePos(event);
            
            var dragObjPos = com.runwaysdk.ui.DOMFacade.getPos(this._dragObj);
            this._mouseClickOffset = { x: this._mouseDownPos.x - dragObjPos.x, y: this._mouseDownPos.y - dragObjPos.y };
            
            com.runwaysdk.ui.RW.OverlayManager.bringToFront(this._dragObj);
          }
          break;
        
        case "mousemove":
          if (this._dragObj != null)
          {
            var mousePos = com.runwaysdk.ui.Util.getMousePos(event);
            
            com.runwaysdk.ui.DOMFacade.setStyle(this._dragObj, "left", mousePos.x - this._mouseClickOffset.x + "px");
            com.runwaysdk.ui.DOMFacade.setStyle(this._dragObj, "top", mousePos.y - this._mouseClickOffset.y + "px")
          }
          break;
        
        case "mouseup":
          this._dragObj = null;
          break;
      }
    }
    
  },

  Static : {
    
    makeDraggable : function(el, dragHandle)
    {
      this.getInstance().makeDraggable(el, dragHandle);
    },
    
    enable : function()
    {
      var hand = Mojo.Util.bind(this.getInstance(), this.getInstance().handleEvent);
      
      // FIXME
      document.addEventListener("mousedown", hand, false);
      document.addEventListener("mousemove", hand, false);
      document.addEventListener("mouseup", hand, false);
    },
    
    disable : function()
    {
      var hand = Mojo.Util.bind(this.getInstance(), this.getInstance().handleEvent);
      
      // FIXME
      document.removeEventListener("mousedown", hand, false);
      document.removeEventListener("mousemove", hand, false);
      document.removeEventListener("mouseup", hand, false);
    }
    
  }

});
DragDrop.enable();

})();
