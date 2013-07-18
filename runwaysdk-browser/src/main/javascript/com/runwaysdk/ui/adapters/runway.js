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
      else {
        return new HTMLElement(el, attributes, styles);
      }
    },
    newDocumentFragment : function(el){
      return new com.runwaysdk.ui.RW.DocumentFragment(el);
    },
    newDialog: function(title){
      throw new com.runwaysdk.Exception('Not implemented');
      //return new com.runwaysdk.ui.Dialog(title);
    },
    newButton : function(label, handler, el){
      return new com.runwaysdk.ui.RW.Button(label, handler, el);
    },
    newDataTable: function(){
      throw new com.runwaysdk.Exception('Not implemented');
      //return new com.runwaysdk.ui.DataTable();
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
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newColumn : function(config){
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newRecord : function(obj){
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newDrag : function(elProvider) {
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newDrop : function(elProvider) {
      throw new com.runwaysdk.Exception('Not implemented');
    }
  }
});
RUNWAY_UI.Manager.addFactory("Runway", Factory);

/*
 * Runway implementations
 */

var Node = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'Node', {
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
      newChild.setParent(this);
      return this.getRawNode().appendChild(newChild.getRawNode());
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
      return this.getRawNode().removeChild(oldChild.getRawNode());
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

var Element = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'Element', {
  Implements: RUNWAY_UI.ElementIF,
  Extends : Node,
  Instance: {
    initialize : function(el, attributes, styles)
    {
      // FIXME : change the element parameter to accept an ID and not a type string (like 'div')
      var rawEl;
    
      if(Mojo.Util.isString(el))
      {
        rawEl = DOMFacade.createElement(el);
      }
      else if(Mojo.Util.isElement(el))
      {
        rawEl = el;
      }
      else
      {
        throw new com.runwaysdk.Exception('The first argument must be a node name or reference to an Element.');
      }
  
      DOMFacade.updateElement(rawEl, attributes, styles);
  
      this.$initialize(rawEl);
    },
    // DOM Methods
    getAttribute : function(name)
    {
      DOMFacade.getAttribute(this.getRawEl(), name);
    },
    setAttribute : function(name, value)
    {
      DOMFacade.setAttribute(this.getRawEl(), name, value);
    },
    setAttributes : function(attrs)
    {
      for (var key in attrs) {
        this.setAttribute(key, attrs[key]);
      }
    },
    removeAttribute : function(name)
    {
      return DOMFacade.removeAttribute(this.getRawEl(), name);
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
      return DOMFacade.hasAttribute(this.getRawEl(), name);
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
      return DOMFacade.getTextContent(this.getRawEl());
      return el.textContent != null ? el.textContent : el.innerText;
    },
    setTextContent : function(text)
    {
      DOMFacade.setTextContent(this.getRawEl(), text);
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
    }
  }
});

var HtmlElement = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'HTMLElement', {
  Extends : Element,
  Instance: {
    initialize: function(el, attributes, styles){
      el = RUNWAY_UI.Util.stringToRawElement(el);
      this.$initialize(el, attributes, styles);
    },
    render : function(newParent)
    {
      var parent = RUNWAY_UI.Util.toRawElement(newParent || this.getParent() || DOMFacade.getRawBody());
      this.$render(parent);
    },
    getElementsByClassName:function(className)
    {
      return this.getRawEl().getElementsByClassName(className);
    },
    setInnerHTML:function(html)
    {
      DOMFacade.setInnerHTML(this, html);
    },
    appendInnerHTML:function(html)
    {
      DOMFacade.appendInnerHTML(this, html);
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
      DOMFacade.addClassName(this, c);
    },
    addClassNames : function(obj)
    {
      DOMFacade.addClassNames(this, obj);
    },
    hasClassName : function(c)
    {
      return DOMFacade.hasClassName(this, c);
    },
    removeClassName : function(c)
    {
      DOMFacade.removeClassName(this, c);
    },
    getClassName : function()
    {
      return DOMFacade.getClassName(this);
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
      DOMFacade.setStyle(this.getRawEl(), style, value);
    },
    getStyle : function(style)
    {
      return DOMFacade.getStyle(this.getRawEl(), style);
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
      return DOMFacade.getSize(this);
    },
    getPos : function()
    {
      return DOMFacade.getPos(this);
    }
  }
});

var Attr = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'Attr', {
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

})();
