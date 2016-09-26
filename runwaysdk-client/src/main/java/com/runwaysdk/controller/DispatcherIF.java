package com.runwaysdk.controller;

import java.io.IOException;

public interface DispatcherIF
{

  public void invokeControllerAction(String controllerName, String actionName, RequestManager manager) throws IOException;

}
