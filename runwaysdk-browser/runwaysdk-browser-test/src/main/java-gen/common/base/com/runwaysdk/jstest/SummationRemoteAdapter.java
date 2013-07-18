package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = 851809795)
public interface SummationRemoteAdapter extends java.rmi.Remote, com.runwaysdk.generation.loader.Reloadable
{
  public static final String SERVICE_NAME = "java:com.runwaysdk.jstest.SummationRemoteAdapter";
  public java.lang.Integer[][] arrayInOut(String sessionId, java.lang.Integer[][] array) throws java.rmi.RemoteException;
  
  public java.lang.Boolean compareStates(String sessionId, com.runwaysdk.business.EnumDTO state1, com.runwaysdk.business.EnumDTO state2) throws java.rmi.RemoteException;
  
  public com.runwaysdk.business.ComponentDTO concatUtilChar(String sessionId, com.runwaysdk.business.ComponentDTO util) throws java.rmi.RemoteException;
  
  public com.runwaysdk.business.ComponentDTO concatViewChar(String sessionId, com.runwaysdk.business.ComponentDTO view) throws java.rmi.RemoteException;
  
  public java.util.Date dateInOut(String sessionId, java.util.Date date) throws java.rmi.RemoteException;
  
  public void doNothing(String sessionId) throws java.rmi.RemoteException;
  
  public void facadeForceException(String sessionId) throws java.rmi.RemoteException;
  
  public java.lang.Integer getNullInteger(String sessionId, com.runwaysdk.business.ComponentDTO nullObj) throws java.rmi.RemoteException;
  
  public com.runwaysdk.business.EnumDTO getState(String sessionId, com.runwaysdk.business.EnumDTO state) throws java.rmi.RemoteException;
  
  public java.lang.Integer sumIntegerValues(String sessionId, com.runwaysdk.business.ComponentDTO[] testClassArr) throws java.rmi.RemoteException;
  
}
