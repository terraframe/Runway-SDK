package com.runwaysdk.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class MockServletResponse implements ServletResponseIF
{
  private OutputStream ostream;

  private int          status;

  private String       location;

  private String       contentType;

  public MockServletResponse()
  {
    this.ostream = new ByteArrayOutputStream();
    this.status = 200;
  }

  @Override
  public PrintWriter getWriter() throws IOException
  {

    return null;
  }

  @Override
  public OutputStream getOutputStream() throws IOException
  {
    return ostream;
  }

  @Override
  public void setStatus(int sc)
  {
    this.status = sc;
  }

  @Override
  public int getStatus()
  {
    return this.status;
  }

  @Override
  public void sendRedirect(String location) throws IOException
  {
    this.location = location;
  }

  public String getRedirect()
  {
    return location;
  }

  @Override
  public void setContentType(String contentType)
  {
    this.contentType = contentType;
  }

  public String getContentType()
  {
    return contentType;
  }
}
