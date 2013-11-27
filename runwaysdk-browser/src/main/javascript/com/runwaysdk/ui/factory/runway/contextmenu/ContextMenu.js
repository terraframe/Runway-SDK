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
    initialize : function(target, pos) {
      this.$initialize();
      
      this._list = this.getFactory().newList("", {});
      this._list.addClassName("ContextMenuList");
      this.appendChild(this._list);
      
      this.setTarget(target);
      
      if (pos == null) {
        pos = UI.DOMFacade.getMousePos();
      }
      this.getEl().setPos(pos.x, pos.y);
      
      this.getEl().setStyle("z-index", "200");
      
      UI.DOMFacade.getDocument().addEventListener("click", Mojo.Util.bind(this, this.onDocumentClickListener));
    },
    onDocumentClickListener : function(mouseEvent) {
      if (!this._list.hasLI(mouseEvent.getTarget())){
        this.close();
      }
    },
    addItem : function(label, icon, handler, config) {
      cmItem = new ContextMenuItem(this, label, icon, handler, config);
      this._list.addItem(cmItem.getListItem());
    },
    getTarget : function() {
      return this._target;
    },
    setTarget : function(target) {
      this._target = target;
//      this._target = UI.Util.toElement(target);
    },
    manageZIndex : function() {
      return false;
    },
    close : function() {
      // TODO : move this logic to the destroy method.
      this.getEl().getRawEl().parentNode.removeChild(this.getEl().getRawEl());
      this.destroy();
    }
  }
});

var ContextMenuItem = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'ContextMenuItem', {
  
  Extends : RW.Widget,
  
  Instance : {
    initialize : function(contextMenu, label, icon, handler, config) {
      this._impl = this.getFactory().newListItem()
      this.$initialize();
      
      this._handler = handler;
      this._contextMenu = contextMenu;
      
      this._impl.setInnerHTML(label);
      this._impl.addClassName("icon-" + icon);
      
      this._impl.getEl().addEventListener("click", Mojo.Util.bind(this, this.onClickListener));
      this._impl.getEl().addEventListener("mouseover", Mojo.Util.bind(this, this.onMouseOverListener));
      this._impl.getEl().addEventListener("mouseout", Mojo.Util.bind(this, this.onMouseOutListener));
    },
    onMouseOverListener : function(e) {
      this._impl.addClassName("hover");
    },
    onMouseOutListener : function(e) {
      this._impl.removeClassName("hover");
    },
    onClickListener : function(e)
    {
      this._handler(e, this._contextMenu);
      this._contextMenu.close();
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