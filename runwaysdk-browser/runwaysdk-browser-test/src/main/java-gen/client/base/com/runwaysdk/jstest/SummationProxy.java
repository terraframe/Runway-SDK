package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = 307846891)
public abstract class SummationProxy implements SummationClientRequestIF, com.runwaysdk.generation.loader.Reloadable
{
  public static SummationProxy getClientRequest()
  {
    return getClientRequest(com.runwaysdk.request.ConnectionLabelFacade.getConnection());
  }
  
  public static SummationProxy getClientRequest(java.lang.String label)
  {
    return getClientRequest(com.runwaysdk.request.ConnectionLabelFacade.getConnection(label));
  }
  
  public static SummationProxy getClientRequest(com.runwaysdk.request.ConnectionLabel connection)
  {
    if(com.runwaysdk.request.ConnectionLabel.Type.JAVA.equals(connection.getType()))
    {
      try
      {
        Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JavaSummationProxy");
        Object object = clazz.getConstructor(String.class, String.class).newInstance(connection.getLabel(), connection.getAddress());
        return (SummationProxy) object;
      }
      catch (Exception e)
      {
        throw new com.runwaysdk.request.ClientRequestException(e);
      }
    }
    else if(com.runwaysdk.request.ConnectionLabel.Type.RMI.equals(connection.getType()))
    {
      return new RMISummationProxy(connection.getLabel(), connection.getAddress());
    }
    else
    {
      return new WebServiceSummationProxy(connection.getLabel(), connection.getAddress());
    }
  }
  
}
