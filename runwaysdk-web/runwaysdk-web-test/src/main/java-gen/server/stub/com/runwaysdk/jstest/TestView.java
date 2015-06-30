/**
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
package com.runwaysdk.jstest;
public class TestView extends TestViewBase implements com.runwaysdk.generation.loader.Reloadable
{
public TestView()
{
super();
}
public String toString()
{
  return "TestView: "+getId();
}
public static TestView get(String id)
{
return (TestView) com.runwaysdk.business.View.get(id);
}
@com.runwaysdk.business.rbac.Authenticate
public com.runwaysdk.jstest.TestView returnView(com.runwaysdk.jstest.TestView view)
{
  view.setViewCharacter("Returned!");
  return view;
}
@com.runwaysdk.business.rbac.Authenticate
public static java.lang.Integer doubleAnInt(java.lang.Integer num)
{
return 2 * num;
}
}
