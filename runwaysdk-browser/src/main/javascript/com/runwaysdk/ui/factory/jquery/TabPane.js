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
  
  var TabPane = Mojo.Meta.newClass(Mojo.JQUERY_PACKAGE+'TabPane', {
    
    Extends : RW.Widget,
    
    Instance: {
      initialize : function(config) {
        config = config || {};
        
        this.$initialize(config.el || "div");
        
        this._ul = this.getFactory().newElement("ul");
        this.appendChild(this._ul);
      },
      
      addPane : function(title, content) {
        var contentDiv = this.getFactory().newElement("div");
        this.appendChild(contentDiv);
        
        if (Mojo.Util.isString(content)) {
          contentDiv.setInnerHTML(content);
        }
        else {
          contentDiv.appendChild(content);
        }
        
        var li = this.getFactory().newElement("li");
        li.appendChild(this.getFactory().newElement("a", {href: "#" + contentDiv.getId(), innerHTML: title}))
        
        this._ul.appendChild(li);
      },
      
      render : function(parent) {
        this.$render(parent);
        $(this.getRawEl()).tabs();
      }
    }
  });
  
  return TabPane;
  
})();
