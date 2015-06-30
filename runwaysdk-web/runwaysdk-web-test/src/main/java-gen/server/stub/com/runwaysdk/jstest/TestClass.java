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

import com.runwaysdk.dataaccess.transaction.SkipIfProblem;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class TestClass extends TestClassBase implements com.runwaysdk.generation.loader.Reloadable
{
public TestClass()
{
super();
}
protected String buildKey()
{
  return this.getId();
}
public String toString()
{
  return "TestClass: "+getId();
}
public static TestClass get(String id)
{
return (TestClass) com.runwaysdk.business.Business.get(id);
}
@com.runwaysdk.business.rbac.Authenticate
public com.runwaysdk.jstest.TestClass[] createInstances(java.lang.Integer num)
{
if(num == null)
{
return null;
}
TestClass[] ret = new TestClass[num];
for (int i = 0; i < num; i++)
{
ret[i] = new TestClass();
}
return ret;
}
@com.runwaysdk.business.rbac.Authenticate
public static java.lang.Integer doubleAnInteger(java.lang.Integer num)
{
return 2 * num;
}
@com.runwaysdk.business.rbac.Authenticate
public void instanceForceException()
{
  com.runwaysdk.jstest.TestException ex = new com.runwaysdk.jstest.TestException("Test Ex");
  ex.setExInt(12345);
  ex.setExChar("Test exChar");
  throw ex;
}
public void instanceForceException1()
{
  throw new com.runwaysdk.session.InvalidLoginException("Test InvalidLoginException");
}
@com.runwaysdk.business.rbac.Authenticate
public void instanceForceException2()
{
  throw new java.lang.NullPointerException("Test NullPointerException");
}
@com.runwaysdk.business.rbac.Authenticate
public void instanceForceException3()
{
  throw new java.lang.ClassCastException("Test ClassCastException");
}
  @com.runwaysdk.dataaccess.transaction.Transaction

public void instanceForceProblem()
{
  com.runwaysdk.jstest.TestProblem p = new com.runwaysdk.jstest.TestProblem("Test Problem");
  p.setProblemInt(12345);
  p.setProblemChar("Test problemChar");
//  p.apply();
  p.throwIt();
}
  @com.runwaysdk.dataaccess.transaction.Transaction

public java.lang.Integer instanceForceWarning()
{
  com.runwaysdk.jstest.TestWarning w = new com.runwaysdk.jstest.TestWarning();
  w.setWarningInt(12345);
  w.setWarningChar("Test warningChar");
//  w.apply();
  w.throwIt();
  return 10;
}
  @com.runwaysdk.dataaccess.transaction.Transaction

public void instanceForceWarningVoid()
{
  com.runwaysdk.jstest.TestWarning w = new com.runwaysdk.jstest.TestWarning();
  w.setWarningInt(12345);
  w.setWarningChar("Test warningChar Void");
//  w.apply();
  w.throwIt();
}
  @com.runwaysdk.dataaccess.transaction.Transaction

public java.lang.Integer instanceForceInformation()
{
  com.runwaysdk.jstest.TestInformation i = new com.runwaysdk.jstest.TestInformation();
  i.setInfoInt(12345);
  i.setInfoChar("Test infoChar");
//  i.apply();
  i.throwIt();
  return 10;
}
  @com.runwaysdk.dataaccess.transaction.Transaction

public void instanceForceMultipleProblems()
{
  com.runwaysdk.jstest.TestProblem p = new com.runwaysdk.jstest.TestProblem("Test Problem 1");
  p.setProblemInt(12345);
  p.setProblemChar("Test problemChar 1");
//  p.apply();
  p.throwIt();

  com.runwaysdk.jstest.TestProblem p2 = new com.runwaysdk.jstest.TestProblem("Test Problem 2");
  p2.setProblemInt(54321);
  p2.setProblemChar("Test problemChar 2");
//  p2.apply();
  p2.throwIt();
}
  
  @com.runwaysdk.dataaccess.transaction.Transaction

public java.lang.Integer instanceForceMultipleWarnings()
{
  com.runwaysdk.jstest.TestWarning w = new com.runwaysdk.jstest.TestWarning();
  w.setWarningInt(12345);
  w.setWarningChar("Test warningChar 1");
//  w.apply();
  w.throwIt();

  com.runwaysdk.jstest.TestWarning w2 = new com.runwaysdk.jstest.TestWarning();
  w2.setWarningInt(54321);
  w2.setWarningChar("Test warningChar 2");
//  w2.apply();
  w2.throwIt();

  return 10;
}
  @com.runwaysdk.dataaccess.transaction.Transaction
public java.lang.Integer instanceForceMultipleInformations()
{
  com.runwaysdk.jstest.TestInformation i = new com.runwaysdk.jstest.TestInformation();
  i.setInfoInt(12345);
  i.setInfoChar("Test warningChar 1");
//  i.apply();
  i.throwIt();

  com.runwaysdk.jstest.TestInformation i2 = new com.runwaysdk.jstest.TestInformation();
  i2.setInfoInt(54321);
  i2.setInfoChar("Test warningChar 2");
//  i2.apply();
  i2.throwIt();

  return 10;
}
}
