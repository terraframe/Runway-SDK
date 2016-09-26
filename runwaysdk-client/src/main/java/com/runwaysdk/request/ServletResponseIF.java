package com.runwaysdk.request;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public interface ServletResponseIF
{
  public PrintWriter getWriter() throws IOException;

  public OutputStream getOutputStream() throws IOException;

  public void setStatus(int sc);

  public void sendRedirect(String location) throws IOException;

  public void setContentType(String string);

  public int getStatus();

  public String getContentType();
}
