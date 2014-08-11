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
//define(["jquery-ui", "./Factory", "../runway/widget/Widget",], function(){
(function(){

  var RUNWAY_UI = Mojo.Meta.alias("com.runwaysdk.ui.*");
  var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
  
  var Dialog = Mojo.Meta.newClass(Mojo.JQUERY_PACKAGE+'Dialog', {
    
    Extends : RW.Widget,
    
    Implements : [RUNWAY_UI.DialogIF, RUNWAY_UI.ButtonProviderIF],
    
    Instance: {
      initialize : function(title, config) {
        
        config = config || {};
        // JQuery makes no distinction between close and hide, so we'll need to modify their close function if they've provided one.
        if (config.close != null) {
          var theirFunc = config.close;
          config.close = Mojo.Util.bind(this, function() {
            if (!this._isHide) {
              theirFunc.apply(this, arguments);
            }
          });
        }
        
        var defaultConfig = {
          title: title,
          buttons: [],
          destroyOnExit: true,
          closeOnEscape: false,
          autoOpen: false, // We display on render
          el: "div",
          
          // Destroy the dialog when we hit the close button
          close: Mojo.Util.bind(this, function(){
            if (this._config.destroyOnExit && !this._isHide) {
              this.destroy();
            }
          }),
          
          open: Mojo.Util.bind(this, this._disableJqueryUiDefaults)
        };
        this._config = Mojo.Util.deepMerge(defaultConfig, config);
        
        // The underlying element (this.getRawEl()) must be the outermost div, which is why we're going to create our impl here, and then super up with the parent.
        this._contentEl = this.getFactory().newElement(this._config.el);
        this._impl = $(this._contentEl.getRawEl()).dialog(this._config);
        
        this.$initialize(this._impl.parent()[0]);
        
        this._buttons = new com.runwaysdk.structure.HashSet();
      },
      _disableJqueryUiDefaults : function(event, ui) {
        // JQuery likes to create a useless tooltip on the close button for some reason... lets disable it.
        $(this.getRawEl()).siblings(".ui-dialog-titlebar").children(".ui-dialog-titlebar-close").removeAttr("title");
        $(".ui-tooltip").remove();      
        
        // Hide the close button in upper right of dialog
        $(this.getRawEl()).siblings(".ui-dialog-titlebar").children(".ui-dialog-titlebar-close").remove();
      },
      getImpl : function() {
        return this._impl;
      },
      getHeader : function() {
        throw new com.runwaysdk.Exception("Not implemented");
      },
      getFooter : function() {
        throw new com.runwaysdk.Exception("Not implemented");
      },
      setTitle : function (title) {
        this.getImpl().dialog("option", "title", title);
      },
      appendContent : function(content) {
        if (Mojo.Util.isString(content)) {
          this._contentEl.appendInnerHTML(content);
        }
        else {
          this._contentEl.appendChild(content);
        }
      },
      getContentEl : function() {
        return this._contentEl;
      },
      setContent : function(content) {
        if (Mojo.Util.isString(content)) {
          this._contentEl.setInnerHTML(content);
        }
        else {
          this._contentEl.removeAllChildren();
          this._contentEl.appendChild(content);
        }
      },
      addButton: function(label, handler, context, config) {
    
        if(config == null) {
          config = {};
        }
        
        Mojo.Util.merge({text: label, click: handler}, config);
        
        if (config.primary === true) {
          config["class"] = config["class"] == null ? "btn btn-primary" : config["class"] + " btn btn-primary";
        }
        else {
          config["class"] = config["class"] == null ? "btn" : config["class"] + " btn";
        }
    
        this._config.buttons.push(config);
        
        var buttons = this._impl.dialog("option", "buttons"); // getter
        buttons[label] = handler;
        this._impl.dialog("option", "buttons", buttons); // setter
      },
      removeButton : function() {
        
      },
      getButton : function() {
        
      },
      getButtons : function()
      {
        return this._impl.dialog("option", "buttons");
      },
      close : function() {
        this.destroy();
      },
      show : function() {
        $(this.getImpl()).dialog('open');
          
        if (!this.isRendered()) {
          this._config.startHidden = false;
        }
      },
      hide : function() {
        // JQuery has no distinction between close and hide.
        this._isHide = true;
        $(this.getImpl()).dialog("close");
        this._isHide = false;
        
        if (!this.isRendered()) {
          this._config.startHidden = true;
        }
      },
      render : function(parent) {
        this.$render(parent);
        
        if (this._config.startHidden && !this._config.autoOpen) {
          this.hide();
        }
        else {
          this.show();
        }
      },
      destroy : function() {
        this._impl.dialog("destroy");
        this.$destroy();
      },
      
      /*
       * BlockableIF
       */
      showStandby : function(overlay) {
        var rootParent = $(this.getRawEl());
        
        var jqTitle = rootParent.children("div.ui-dialog-titlebar.ui-widget-header");
        if (jqTitle[0] == null) { throw new com.runwaysdk.Exception("Unable to find JQuery Dialog title element."); }
        var titleHeight = jqTitle.outerHeight();
        
        overlay.setStyle("top", titleHeight + "px");
        overlay.setStyle("left", 0 + "px");
        overlay.setStyle("height", (rootParent.outerHeight() - titleHeight) + "px");
        overlay.setStyle("width", rootParent.outerWidth() + "px");
        
        overlay.render(rootParent[0]);
        
        return true; // Return true to override rendering
      }
    }
  });
  
  return Dialog;

})();
