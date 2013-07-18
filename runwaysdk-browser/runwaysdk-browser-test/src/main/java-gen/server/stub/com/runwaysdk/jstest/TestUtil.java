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
