package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = 728497518)
public interface SummationClientRequestIF extends com.runwaysdk.constants.ClientRequestMarker, com.runwaysdk.generation.loader.Reloadable
{
  public static final String CLASS = "com.runwaysdk.jstest.Summation";
  public java.lang.Integer[][] arrayInOut(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, java.lang.Integer[][] array);
  
  public java.lang.Boolean compareStates(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.StatesDTO state1, com.runwaysdk.jstest.StatesDTO state2);
  
  public com.runwaysdk.jstest.TestUtilDTO concatUtilChar(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestUtilDTO util);
  
  public com.runwaysdk.jstest.TestViewDTO concatViewChar(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestViewDTO view);
  
  public java.util.Date dateInOut(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, java.util.Date date);
  
  public void doNothing(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF);
  
  public void facadeForceException(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF);
  
  public java.lang.Integer getNullInteger(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestClassDTO nullObj);
  
  public com.runwaysdk.jstest.StatesDTO getState(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.StatesDTO state);
  
  public java.lang.Integer sumIntegerValues(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestClassDTO[] testClassArr);
  
}
