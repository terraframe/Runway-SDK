package com.runwaysdk.request;

import java.io.IOException;

import javax.servlet.ServletException;

public class MockRequestDispatcher implements RequestDispatcherIF
{
  private String location;

  public void setLocation(String location)
  {
    this.location = location;
  }

  public String getLocation()
  {
    return location;
  }

  @Override
  public void forward(ServletRequestIF req, ServletResponseIF resp) throws ServletException, IOException
  {
  }

}
