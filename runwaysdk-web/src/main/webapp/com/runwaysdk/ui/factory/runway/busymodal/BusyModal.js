/*
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
//define(["../button/Button", "../overlay/Overlay"], function(){
(function(){

var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
var UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");

var BusyModal = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'busymodal.BusyModal', {
  
  Extends : RW.Overlay,
  
  Instance : {
    
    initialize : function(message, config)
    {
      config = config || {};
      config.modal = config.modal || true;
      config.message = message;
      this._config = config;
      
      this.$initialize("div", config);
    },

    createHtml : function() {
      this._progressDiv = this.getFactory().newElement("div");
      this._progressDiv.addClassName("rw-busymodal-progressdiv");
      this.appendChild(this._progressDiv);
      
      this._message = this.getFactory().newElement("span");
      this._message.addClassName("rw-busymodal-message");
      this._message.setInnerHTML(this._config.message);
      this._progressDiv.appendChild(this._message);
      
      this._image = this.getFactory().newElement("div");
      this._image.addClassName("rw-busymodal-img");
      this._progressDiv.appendChild(this._image);
    },
    
    render : function(parent) {
      this.createHtml();
      
      this.$render(parent);
    }
  }
  
});

return BusyModal;

})();
