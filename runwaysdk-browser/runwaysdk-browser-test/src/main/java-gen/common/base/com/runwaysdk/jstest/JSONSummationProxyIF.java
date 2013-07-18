package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = -1785262308)
public interface JSONSummationProxyIF extends com.runwaysdk.constants.ClientRequestMarker, com.runwaysdk.generation.loader.Reloadable
{
  public java.lang.String arrayInOut(java.lang.String sessionId, java.lang.String array);
  
  public java.lang.String compareStates(java.lang.String sessionId, java.lang.String state1, java.lang.String state2);
  
  public java.lang.String concatUtilChar(java.lang.String sessionId, java.lang.String util);
  
  public java.lang.String concatViewChar(java.lang.String sessionId, java.lang.String view);
  
  public java.lang.String dateInOut(java.lang.String sessionId, java.lang.String date);
  
  public java.lang.String doNothing(java.lang.String sessionId);
  
  public java.lang.String facadeForceException(java.lang.String sessionId);
  
  public java.lang.String getNullInteger(java.lang.String sessionId, java.lang.String nullObj);
  
  public java.lang.String getState(java.lang.String sessionId, java.lang.String state);
  
  public java.lang.String sumIntegerValues(java.lang.String sessionId, java.lang.String testClassArr);
  
}
