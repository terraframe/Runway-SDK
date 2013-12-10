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
 * RunwaySDK Javascript UI library.
 * 
 * @author Terraframe
 */
(function(){

var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
var UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");

var Dialog = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'Dialog', {
  
  Extends : RW.Overlay,
  
  /**
   * FIXME
   * Implement using buttonIF
   */
  //Implements : UI.DialogIF,
  
  Instance : {
    
    initialize : function(title, config)
    {
      config = config || {};
      
      config.visible = config.visible || false;
      this.setVisible(config.visible);
      
      this.$initialize(null, config.modal);
      
      this.setWidth(config.width || "400px");
//      this.setHeight(config.height || "300px");
      
      // Title Bar
      this._divTitle = this.getFactory().newElement("div", null, config.titleStyle);
      this.getFactory().makeDraggable(this._divTitle, {dragHandle: this});
      this._divTitle.setInnerHTML(title || "Dialog"); // Set title label text the easy way
      this._divTitle.addClassName("title");
      this.appendChild(this._divTitle);
      
      // Close Button (X)
      var dialog = this;
      this._bClose = this.getFactory().newButton("X", function() {dialog.hide()});
      this._bClose.addClassName("closeButton");
      this._divTitle.appendChild(this._bClose);
      
      // Content
      this._divContent = this.getFactory().newElement("div");
      this._divContent.addClassName("content");
      this.appendChild(this._divContent);
      
      // Div for buttons
      this._divButtons = this.getFactory().newElement("div");
      this._divButtons.addClassName("buttons");
      this.appendChild(this._divButtons);
      
      this._buttons = new com.runwaysdk.structure.HashSet();
    },
    setWidth : function(w) {
      UI.DOMFacade.setWidth(this.getEl(), w);
    },
    setHeight : function(h) {
      UI.DOMFacade.setHeight(this.getEl(), h);
    },
    getTitleDiv : function() {
      return this._divTitle;
    },
    getContentDiv : function()
    {
      return this._divContent;
    },
    setTitleStyle : function(k,v)
    {
      this._divTitle.setStyle(k,v);
    },
    setTitle : function(html)
    {
      this._divTitle.setInnerHTML(html);
    },
    setInnerHTML : function(str){
      return this.getContentDiv().setInnerHTML(str);
    },
    appendInnerHTML : function(str){
      return this.getContentDiv().appendInnerHTML(str);
    },
    setContent : function(HtmlElement_Or_Text) {
      if (Mojo.Util.isString(HtmlElement_Or_Text)) {
        this._divContent.setInnerHTML(HtmlElement_Or_Text);
      }
      else {
        this._divContent.appendChild(HtmlElement_Or_Text);
      }
    },
    appendContent : function(HtmlElement_Or_Text)
    {
      if (Mojo.Util.isString(HtmlElement_Or_Text)) {
        this._divContent.appendInnerHTML(HtmlElement_Or_Text);
      }
      else {
        this._divContent.appendChild(HtmlElement_Or_Text);
      }
    },
    addButton : function(label, handler, context)
    {
      var buttonIF;
      if (com.runwaysdk.ui.ButtonIF.getMetaClass().isInstance(label)) {
        buttonIF = label;
      }
      else {
        buttonIF = this.getFactory().newButton(label, handler, context);
      }
      
      this._buttons.add(buttonIF);
      this._divButtons.appendChild(buttonIF.getEl());
      
      UI.DOMFacade.addClassName(buttonIF, "button");
      
      return buttonIF;
    },
    setVisible : function(bool) {
      if (bool) {
        if (!this.isRendered()) 
          this.render();
        
        this.setStyle("visibility", "visible");
      }
      else if (this.isRendered()) {
        this.setStyle("visibility", "hidden");
      }
    },
    destroy : function()
    {
//      delete this._divTitle;
//      delete this._divClose;
//      delete this._bClose;
//      delete this._divContent;
//      this._buttons.removeAll();
      this.$destroy();
    },
    getImpl : function() {
      return this;
    }
  }
  
});

})();