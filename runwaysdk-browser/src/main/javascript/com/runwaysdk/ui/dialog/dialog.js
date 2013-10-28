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

var RW = Mojo.Meta.alias("com.runwaysdk.ui.RW.*");
var UI = Mojo.Meta.alias("com.runwaysdk.ui.*");

var Dialog = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'Dialog', {
  
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
      
      this.$initialize(null, config.isModal);
      var container = this.getEl();
      
      // Title Bar
      this._divTitle = this.getFactory().newElement("div", null, config.titleStyle);
      UI.DragDrop.makeDraggable(this._divTitle, this);
      this._divTitle.setInnerHTML(title || "Dialog"); // Set title label text the easy way
      this._divTitle.addClassName("title");
      container.appendChild(this._divTitle);
      
      // Close Button (X)
      var dialog = this;
      this._bClose = new UI.Button("X", function() {dialog.hide()});
      this._bClose.addClassName("closeButton");
      this._divTitle.appendChild(this._bClose);
      
      // Content
      this._divContent = this.getFactory().newElement("div");
      this._divContent.addClassName("content");
      container.appendChild(this._divContent);
      
      // Div for buttons
      this._divButtons = this.getFactory().newElement("div");
      this._divButtons.addClassName("buttons");
      container.appendChild(this._divButtons);
      
      this._buttons = new com.runwaysdk.structure.HashSet();
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
    setStyle : function(k,v)
    {
      return this.getEl().setStyle(k,v);
    },
    setInnerHTML : function(str){
      return this.getEl().setInnerHTML(str);
    },
    appendInnerHTML : function(str){
      return this.getEl().appendInnerHTML(str);
    },
    /*
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
    */
    addButton : function(label, handler, context)
    {
      var button = new UI.Button(label, handler, context);
      
      this._buttons.add(button);
      this._divButtons.appendChild(button.getEl());
      
      UI.DOMFacade.addClassName(button, "button");
      
      return button;
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
      // FIXME
      delete this._Dimmer;
      delete this._divTitle;
      delete this._divClose;
      delete this._bClose;
      delete this._divContent;
      this._buttons.removeAll();
      this.getEl().getRawEl().parentNode.removeChild(this.getEl().getRawEl());
      this.$destroy();
    },
    getImpl : function() {
      return this;
    }
  }
  
});

})();