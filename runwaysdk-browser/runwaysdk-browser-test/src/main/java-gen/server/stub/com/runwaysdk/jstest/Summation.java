package com.runwaysdk.jstest;
public class Summation extends SummationBase implements com.runwaysdk.generation.loader.Reloadable
{
protected static void facadeForceException(String sessionId)
{  com.runwaysdk.jstest.TestException ex = new com.runwaysdk.jstest.TestException("Test Ex");
  ex.setExChar("Test exChar");
  throw ex;
}public static java.util.Date dateInOut(java.util.Date date)
{
 return date;
}
public static java.lang.Integer[][] arrayInOut(java.lang.Integer[][] arrayIn)
{
  java.lang.Integer[][] array = new java.lang.Integer[5][5];
  for(int i=0; i<5; i++)
  {
    for(int j=0; j<5; j++)
    {
      array[i][j] = arrayIn[i][j];
    }
  }
  return array;}
public static void doNothing()
{
}
public static java.lang.Integer sumIntegerValues(com.runwaysdk.jstest.TestClass[] testClassArr)
{
int sum = 0;
for(TestClass testClass : testClassArr)
{
sum += testClass.getTestInteger();
}
return sum;
}
public static java.lang.Integer getNullInteger(com.runwaysdk.jstest.TestClass nullObj)
{
if(nullObj == null)
return null;
else
throw new RuntimeException();
}
public static com.runwaysdk.jstest.States getState(com.runwaysdk.jstest.States state)
{
return state;
}
public static com.runwaysdk.jstest.TestView concatViewChar(com.runwaysdk.jstest.TestView view)
{
  view.setViewCharacter(view.getViewCharacter() + view.getViewCharacter());
  return view;
}
public static com.runwaysdk.jstest.TestUtil concatUtilChar(com.runwaysdk.jstest.TestUtil util)
{
  util.setUtilCharacter(util.getUtilCharacter() + util.getUtilCharacter());
  return util;
}
}
