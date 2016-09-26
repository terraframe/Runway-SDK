package com.runwaysdk.request;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatchDecorator implements RequestDispatcherIF
{
  private RequestDispatcher dispatcher;

  public DispatchDecorator(RequestDispatcher dispatcher)
  {
    this.dispatcher = dispatcher;
  }

  @Override
  public void forward(ServletRequestIF req, ServletResponseIF resp) throws ServletException, IOException
  {
    HttpServletRequest request = ( (RequestDecorator) req ).getRequest();
    HttpServletResponse response = ( (ResponseDecorator) resp ).getResponse();

    this.dispatcher.forward(request, response);
  }

}
