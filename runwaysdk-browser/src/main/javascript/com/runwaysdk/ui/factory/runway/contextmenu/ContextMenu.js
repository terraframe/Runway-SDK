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

var ContextMenu = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'ContextMenu', {
  
  Extends : RW.Overlay,
  
//  Implements : [],

  Instance : {
    initialize : function(label, handler) {
      this.$initialize();
      
      this._list = this.getFactory().newList("", {});
      this._list.addClassName("ContextMenuList");
      this.appendChild(this._list);
    },
    addItem : function(label, icon, handler, config) {
      cmItem = new ContextMenuItem(label, icon, handler, config);
      this._list.addItem(cmItem.getListItem());
    }
  }
});

var ContextMenuItem = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'ContextMenuItem', {
  
  Extends : RW.Widget,
  
  Instance : {
    initialize : function(label, icon, handler, config) {
      this._impl = this.getFactory().newListItem()
      this.$initialize();
      
      this._handler = handler;
      
      this._impl.setInnerHTML(label);
      this._impl.addClassName("icon-" + icon);
      
      this._impl.addEventListener("click", Mojo.Util.bind(this, this.onClickListener));
      this._impl.addEventListener("mouseover", Mojo.Util.bind(this, this.onMouseOverListener));
      this._impl.addEventListener("mouseout", Mojo.Util.bind(this, this.onMouseOutListener));
    },
    onMouseOverListener : function(e) {
      this._impl.addClassName("hover");
    },
    onMouseOutListener : function(e) {
      this._impl.removeClassName("hover");
    },
    onClickListener : function(e)
    {
      this._handler(e);
    },
    getListItem : function() {
      return this._impl;
    },
    getEl : function() {
      return this._impl.getEl();
    },
    getContentEl : function() {
      return this.getEl();
    }
  }
});

})();