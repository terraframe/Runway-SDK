package com.runwaysdk.request;

import java.io.IOException;

import javax.servlet.ServletException;

public interface RequestDispatcherIF
{

  public void forward(ServletRequestIF req, ServletResponseIF resp) throws ServletException, IOException;

}
