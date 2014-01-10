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

define(["./TestFramework"], function(TestFramework){
  
com.runwaysdk.test = com.runwaysdk.test || {};
com.runwaysdk.test.DOMTest = {};
com.runwaysdk.test.WidgetTest = {};
var DOMTest = com.runwaysdk.test.DOMTest;
var WidgetTest = com.runwaysdk.test.WidgetTest;
var Y;

DOMTest.fire = function(name, listener, element)
{
  // FIXME remove
  if(!com.runwaysdk.ui.ComponentIF.getMetaClass().isInstance(element)){
    element = com.runwaysdk.ui.Manager.getInstance().getFactory().newElement(element);
  }
  
  listener = new com.runwaysdk.event.EventListener({handleEvent:function(){}});
  
  element.addEventListener(name, listener);
  var event = com.runwaysdk.event.DocumentEvent.getInstance().createEvent(name);
  event.initMouseEvent(name, "true", "true");
  element = com.runwaysdk.ui.Util.toRawElement(element);
  element.dispatchEvent(event.getEvent());
}
/*
WidgetTest.fire = function(name, f, widget)
{
  
}
*/

});
