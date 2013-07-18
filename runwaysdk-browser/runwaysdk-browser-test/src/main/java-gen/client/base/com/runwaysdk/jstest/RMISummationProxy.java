package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = -1775162593)
public class RMISummationProxy extends SummationProxy implements com.runwaysdk.generation.loader.Reloadable
{
  
  private java.lang.String label;
  private java.lang.String address;
  private SummationRemoteAdapter adapter;
  
  private static final long serialVersionUID = -1775162593;
  
  public RMISummationProxy(java.lang.String label, java.lang.String address)
  {
    this.label = label;
    this.address = address;
    try
    {
      this.adapter = (SummationRemoteAdapter) java.rmi.Naming.lookup(this.address + SummationRemoteAdapter.SERVICE_NAME);
    }
    catch (java.net.MalformedURLException e)
    {
      throw new com.runwaysdk.request.RMIClientException(e);
    }
    catch (java.rmi.RemoteException e)
    {
      throw new com.runwaysdk.request.RMIClientException(e);
    }
    catch (java.rmi.NotBoundException e)
    {
      throw new com.runwaysdk.request.RMIClientException(e);
    }
  }
  
  public void unbindRMIProxy()
  {
    try
    {
      java.rmi.Naming.unbind(address + SummationRemoteAdapter.SERVICE_NAME);
    }
    catch (java.net.MalformedURLException e)
    {
      throw new com.runwaysdk.request.RMIClientException(e);
    }
    catch (java.rmi.RemoteException e)
    {
      throw new com.runwaysdk.request.RMIClientException(e);
    }
    catch (java.rmi.NotBoundException e)
    {
      throw new com.runwaysdk.request.RMIClientException(e);
    }
  }
  
  public java.lang.Integer[][] arrayInOut(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, java.lang.Integer[][] array)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    java.lang.Integer[][] _array = array;
    
    java.lang.Integer[][] ___output = null;
    try
    {
      ___output = (java.lang.Integer[][])adapter.arrayInOut(___clientRequestIF.getSessionId(), _array);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (java.lang.Integer[][])me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return ___output;
  }
  
  public java.lang.Boolean compareStates(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.StatesDTO state1, com.runwaysdk.jstest.StatesDTO state2)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.EnumDTO _state1 = (com.runwaysdk.business.EnumDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(state1);
    com.runwaysdk.business.EnumDTO _state2 = (com.runwaysdk.business.EnumDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(state2);
    
    java.lang.Boolean ___output = null;
    try
    {
      ___output = (java.lang.Boolean)adapter.compareStates(___clientRequestIF.getSessionId(), _state1, _state2);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (java.lang.Boolean)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return ___output;
  }
  
  public com.runwaysdk.jstest.TestUtilDTO concatUtilChar(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestUtilDTO util)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.ComponentDTO _util = (com.runwaysdk.business.ComponentDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(util);
    
    com.runwaysdk.business.ComponentDTO ___output = null;
    try
    {
      ___output = (com.runwaysdk.business.ComponentDTO)adapter.concatUtilChar(___clientRequestIF.getSessionId(), _util);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (com.runwaysdk.business.ComponentDTO)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return (com.runwaysdk.jstest.TestUtilDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertToTypeSafe(___clientRequestIF, "com.runwaysdk.jstest.TestUtilDTO", ___output);
  }
  
  public com.runwaysdk.jstest.TestViewDTO concatViewChar(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestViewDTO view)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.ComponentDTO _view = (com.runwaysdk.business.ComponentDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(view);
    
    com.runwaysdk.business.ComponentDTO ___output = null;
    try
    {
      ___output = (com.runwaysdk.business.ComponentDTO)adapter.concatViewChar(___clientRequestIF.getSessionId(), _view);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (com.runwaysdk.business.ComponentDTO)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return (com.runwaysdk.jstest.TestViewDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertToTypeSafe(___clientRequestIF, "com.runwaysdk.jstest.TestViewDTO", ___output);
  }
  
  public java.util.Date dateInOut(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, java.util.Date date)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    java.util.Date _date = date;
    
    java.util.Date ___output = null;
    try
    {
      ___output = (java.util.Date)adapter.dateInOut(___clientRequestIF.getSessionId(), _date);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (java.util.Date)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return ___output;
  }
  
  public void doNothing(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    try
    {
      adapter.doNothing(___clientRequestIF.getSessionId());
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    
  }
  
  public void facadeForceException(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    try
    {
      adapter.facadeForceException(___clientRequestIF.getSessionId());
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    
  }
  
  public java.lang.Integer getNullInteger(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestClassDTO nullObj)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.ComponentDTO _nullObj = (com.runwaysdk.business.ComponentDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(nullObj);
    
    java.lang.Integer ___output = null;
    try
    {
      ___output = (java.lang.Integer)adapter.getNullInteger(___clientRequestIF.getSessionId(), _nullObj);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (java.lang.Integer)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return ___output;
  }
  
  public com.runwaysdk.jstest.StatesDTO getState(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.StatesDTO state)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.EnumDTO _state = (com.runwaysdk.business.EnumDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(state);
    
    com.runwaysdk.business.EnumDTO ___output = null;
    try
    {
      ___output = (com.runwaysdk.business.EnumDTO)adapter.getState(___clientRequestIF.getSessionId(), _state);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (com.runwaysdk.business.EnumDTO)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return (com.runwaysdk.jstest.StatesDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertToTypeSafe(___clientRequestIF, "com.runwaysdk.jstest.StatesDTO", ___output);
  }
  
  public java.lang.Integer sumIntegerValues(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestClassDTO[] testClassArr)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.ComponentDTO[] _testClassArr = (com.runwaysdk.business.ComponentDTO[]) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(testClassArr);
    
    java.lang.Integer ___output = null;
    try
    {
      ___output = (java.lang.Integer)adapter.sumIntegerValues(___clientRequestIF.getSessionId(), _testClassArr);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (java.lang.Integer)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return ___output;
  }
  
}
