package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = 1662871713)
public class JSONSummationAdapter implements com.runwaysdk.generation.loader.Reloadable
{
  private static JSONSummationProxyIF proxy = JSONSummationProxy.getProxy();
  public static java.lang.String arrayInOut(java.lang.String sessionId, java.lang.String array)
  {
    return proxy.arrayInOut(sessionId, array);
  }
  
  public static java.lang.String compareStates(java.lang.String sessionId, java.lang.String state1, java.lang.String state2)
  {
    return proxy.compareStates(sessionId, state1, state2);
  }
  
  public static java.lang.String concatUtilChar(java.lang.String sessionId, java.lang.String util)
  {
    return proxy.concatUtilChar(sessionId, util);
  }
  
  public static java.lang.String concatViewChar(java.lang.String sessionId, java.lang.String view)
  {
    return proxy.concatViewChar(sessionId, view);
  }
  
  public static java.lang.String dateInOut(java.lang.String sessionId, java.lang.String date)
  {
    return proxy.dateInOut(sessionId, date);
  }
  
  public static java.lang.String doNothing(java.lang.String sessionId)
  {
    return proxy.doNothing(sessionId);
  }
  
  public static java.lang.String facadeForceException(java.lang.String sessionId)
  {
    return proxy.facadeForceException(sessionId);
  }
  
  public static java.lang.String getNullInteger(java.lang.String sessionId, java.lang.String nullObj)
  {
    return proxy.getNullInteger(sessionId, nullObj);
  }
  
  public static java.lang.String getState(java.lang.String sessionId, java.lang.String state)
  {
    return proxy.getState(sessionId, state);
  }
  
  public static java.lang.String sumIntegerValues(java.lang.String sessionId, java.lang.String testClassArr)
  {
    return proxy.sumIntegerValues(sessionId, testClassArr);
  }
  
}
