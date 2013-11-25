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
 * RunwaySDK Javascript UI Adapter for YUI2.
 * 
 * @author Terraframe
 */
(function(){

  var RUNWAY_UI = Mojo.Meta.alias("com.runwaysdk.ui.*");
  Mojo.JQUERY_PACKAGE = Mojo.UI_PACKAGE+'jquery.';
  
  var Factory = Mojo.Meta.newClass(Mojo.JQUERY_PACKAGE+'Factory', {
    
    IsSingleton : true,
    
    Implements : RUNWAY_UI.AbstractComponentFactoryIF,
    
    Instance: {
      newElement: function(el, attributes, styles) {
        if (RUNWAY_UI.Util.isElement(el)) {
          return el;
        }
        else {
          return new com.runwaysdk.ui.jquery.HTMLElement(el, attributes, styles);
        }
      },
      newDocumentFragment : function(el){
        throw new com.runwaysdk.Exception('Not implemented');
      },
      newDialog : function (title, config) {
//        throw new com.runwaysdk.Exception("Not implemented");
//        return com.runwaysdk.ui.YUI2.Factory.getInstance().newDialog(title, config);
        return new com.runwaysdk.ui.jquery.Dialog(title, config);
      },
      newButton : function(label, handler, context) {
        //return new Button(label, handler, el);
        return com.runwaysdk.ui.RW.Factory.getInstance().newButton(label, handler, context);
      },
      newDataTable: function(){
        throw new com.runwaysdk.Exception('Not implemented');
        //return new com.runwaysdk.ui.DataTable();
      },
      newList : function (title, config, items) {
        throw new com.runwaysdk.Exception('Not implemented');
      },
      newListItem : function(data){
        throw new com.runwaysdk.Exception('Not implemented');
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
      makeDraggable : function(elProvider, config) {
        com.runwaysdk.ui.RW.Factory.getInstance().makeDraggable(elProvider, config);
      },
      makeDroppable : function(elProvider, config) {
        throw new com.runwaysdk.Exception('Not implemented');
      },
      newForm : function(name, config){
        throw new com.runwaysdk.Exception('Not implemented');
      },
      newFormControl : function(type, config){
        throw new com.runwaysdk.Exception('Not implemented');
      },
    }
  });
  RUNWAY_UI.Manager.addFactory("JQuery", Factory);
  
})();