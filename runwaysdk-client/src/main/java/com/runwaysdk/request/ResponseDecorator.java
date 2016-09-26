package com.runwaysdk.request;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ResponseDecorator implements ServletResponseIF
{
  private HttpServletResponse response;

  public ResponseDecorator(HttpServletResponse response)
  {
    this.response = response;
  }

  public HttpServletResponse getResponse()
  {
    return response;
  }

  @Override
  public OutputStream getOutputStream() throws IOException
  {
    return this.response.getOutputStream();
  }

  public PrintWriter getWriter() throws IOException
  {
    return this.response.getWriter();
  }

  @Override
  public int getStatus()
  {
    return this.response.getStatus();
  }

  @Override
  public void setStatus(int sc)
  {
    this.response.setStatus(sc);
  }

  @Override
  public void setContentType(String type)
  {
    this.response.setContentType(type);
  }

  @Override
  public String getContentType()
  {
    return this.response.getContentType();
  }

  @Override
  public void sendRedirect(String location) throws IOException
  {
    this.response.sendRedirect(location);
  }
}
