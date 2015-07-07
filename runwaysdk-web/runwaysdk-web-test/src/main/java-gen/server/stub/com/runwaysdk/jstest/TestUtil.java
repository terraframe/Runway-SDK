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
public class TestUtil extends TestUtilBase implements com.runwaysdk.generation.loader.Reloadable
{
public TestUtil()
{
super();
}
public String toString()
{
  return "TestUtil: "+getId();
}
public static TestUtil get(String id)
{
return (TestUtil) com.runwaysdk.business.Util.get(id);
}
@com.runwaysdk.business.rbac.Authenticate
public com.runwaysdk.jstest.TestUtil returnUtil(com.runwaysdk.jstest.TestUtil util)
{
  util.setUtilCharacter("Returned!");
  return util;
}
@com.runwaysdk.business.rbac.Authenticate
public static java.lang.Integer doubleAnInt(java.lang.Integer num)
{
return 2 * num;
}
}
