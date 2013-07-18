package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = -1391018199)
public class JSONSummationGenericAdapter implements com.runwaysdk.generation.loader.Reloadable
{
  public static java.lang.String arrayInOut(java.lang.String sessionId, java.util.Map map)
  {
    java.lang.String array = ((java.lang.String[]) map.get("array"))[0];
    
    return JSONSummationAdapter.arrayInOut(sessionId, array);
  }
  
  public static java.lang.String compareStates(java.lang.String sessionId, java.util.Map map)
  {
    java.lang.String state1 = ((java.lang.String[]) map.get("state1"))[0];
    java.lang.String state2 = ((java.lang.String[]) map.get("state2"))[0];
    
    return JSONSummationAdapter.compareStates(sessionId, state1, state2);
  }
  
  public static java.lang.String concatUtilChar(java.lang.String sessionId, java.util.Map map)
  {
    java.lang.String util = ((java.lang.String[]) map.get("util"))[0];
    
    return JSONSummationAdapter.concatUtilChar(sessionId, util);
  }
  
  public static java.lang.String concatViewChar(java.lang.String sessionId, java.util.Map map)
  {
    java.lang.String view = ((java.lang.String[]) map.get("view"))[0];
    
    return JSONSummationAdapter.concatViewChar(sessionId, view);
  }
  
  public static java.lang.String dateInOut(java.lang.String sessionId, java.util.Map map)
  {
    java.lang.String date = ((java.lang.String[]) map.get("date"))[0];
    
    return JSONSummationAdapter.dateInOut(sessionId, date);
  }
  
  public static java.lang.String doNothing(java.lang.String sessionId, java.util.Map map)
  {
    return JSONSummationAdapter.doNothing(sessionId);
  }
  
  public static java.lang.String facadeForceException(java.lang.String sessionId, java.util.Map map)
  {
    return JSONSummationAdapter.facadeForceException(sessionId);
  }
  
  public static java.lang.String getNullInteger(java.lang.String sessionId, java.util.Map map)
  {
    java.lang.String nullObj = ((java.lang.String[]) map.get("nullObj"))[0];
    
    return JSONSummationAdapter.getNullInteger(sessionId, nullObj);
  }
  
  public static java.lang.String getState(java.lang.String sessionId, java.util.Map map)
  {
    java.lang.String state = ((java.lang.String[]) map.get("state"))[0];
    
    return JSONSummationAdapter.getState(sessionId, state);
  }
  
  public static java.lang.String sumIntegerValues(java.lang.String sessionId, java.util.Map map)
  {
    java.lang.String testClassArr = ((java.lang.String[]) map.get("testClassArr"))[0];
    
    return JSONSummationAdapter.sumIntegerValues(sessionId, testClassArr);
  }
  
}
