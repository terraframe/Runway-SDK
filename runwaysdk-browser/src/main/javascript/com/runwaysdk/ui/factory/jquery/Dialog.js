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
define(["./Factory"], function(){

  var RUNWAY_UI = Mojo.Meta.alias("com.runwaysdk.ui.*");
  
  var Dialog = Mojo.Meta.newClass(Mojo.JQUERY_PACKAGE+'Dialog', {
    
    Extends : RUNWAY_UI.WidgetBase,
    
    Implements : [RUNWAY_UI.DialogIF, RUNWAY_UI.ButtonProviderIF],
    
    Instance: {
      initialize : function(title, config) {
        config = config || {};
        
        this.$initialize(config.id);
        
        config.title = title;
        config.buttons = config.buttons || [];
        
        this._el = this.getFactory().newElement("div");
        com.runwaysdk.ui.DOMFacade.getBody().appendChild(this._el);
        this._impl = $(this._el.getRawEl()).dialog(config);
      },
      getImpl : function() {
        return this._impl;
      },
      getEl : function() {
        return this._el;
      },
      getContentEl : function(){
        return this.getEl();
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
      appendContent : function(content){
        if (Mojo.Util.isString(content)) {
          this.getContentEl().appendInnerHTML(content);
        }
        else {
          this.getContentEl().appendChild(content);
        }
      },
      setInnerHTML : function(str){
        this.getContentEl().setInnerHTML(str);
      },
      appendInnerHTML : function(str){
        this.getContentEl().appendInnerHTML(str);
      },
      addButton: function(label, handler, context) {
        var buttons = this.getImpl().dialog("option", "buttons"); // getter
        var butCfg = {};
        butCfg.text = label;
        butCfg.click = handler;
        buttons.push(butCfg);
//        $.extend(buttons,  butCfg);
        this.getImpl().dialog("option", "buttons", buttons); // setter
      },
      removeButton : function() {
        
      },
      getButton : function() {
        
      },
      getButtons : function()
      {
        return this._buttons.values();
      },
      setClose : function(bool)
      {
        if (bool) {
          this.hide();
        }
        else {
          this.show();
        }
      },
      close : function() {
        this.setClose(true);
      },
      show : function() {
        $(this.getImpl()).dialog('open');
      },
      hide : function() {
        $(this.getImpl()).dialog("close");
      },
      render : function(parent) {
//        this.$render(parent);
      }
    }
  });

});
