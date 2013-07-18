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
