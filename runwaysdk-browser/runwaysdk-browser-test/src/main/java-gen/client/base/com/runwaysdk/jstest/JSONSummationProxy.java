package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = 1574503449)
public abstract class JSONSummationProxy implements JSONSummationProxyIF, com.runwaysdk.generation.loader.Reloadable
{
  public static final String CLASS = "com.runwaysdk.jstest.JSONSummationProxy";
  
  public static JSONSummationProxy getProxy()
  {
    return getProxy(com.runwaysdk.request.ConnectionLabelFacade.getConnection());
  }
  
  public static JSONSummationProxy getProxy(java.lang.String label)
  {
    return getProxy(com.runwaysdk.request.ConnectionLabelFacade.getConnection(label));
  }
  
  public static JSONSummationProxy getProxy(com.runwaysdk.request.ConnectionLabel connection)
  {
    if(com.runwaysdk.request.ConnectionLabel.Type.JAVA.equals(connection.getType()))
    {
      try
      {
        Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JSONJavaSummationProxy");
        Object object = clazz.getConstructor(String.class, String.class).newInstance(connection.getLabel(), connection.getAddress());
        return (JSONSummationProxy) object;
      }
      catch (Exception e)
      {
        throw new com.runwaysdk.request.ClientRequestException(e);
      }
    }
    else if(com.runwaysdk.request.ConnectionLabel.Type.RMI.equals(connection.getType()))
    {
      return new JSONRMISummationProxy(connection.getLabel(), connection.getAddress());
    }
    else
    {
      return new JSONWebServiceSummationClientRequest(connection.getLabel(), connection.getAddress());
    }
  }
  
}
