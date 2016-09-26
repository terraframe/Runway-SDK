package com.runwaysdk.mvc;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;

import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.request.ServletRequestIF;
import com.runwaysdk.request.ServletResponseIF;

public abstract class AbstractRestResponse implements ResponseIF
{
  protected abstract Object serialize();

  @Override
  public void handle(RequestManager manager) throws ServletException, IOException
  {
    ServletRequestIF req = manager.getReq();
    ServletResponseIF resp = manager.getResp();

    String encoding = ( req.getCharacterEncoding() != null ? req.getCharacterEncoding() : "UTF-8" );

    Object object = this.serialize();

    resp.setStatus(200);
    resp.setContentType("application/json");

    OutputStream ostream = resp.getOutputStream();

    try
    {
      if (object != null)
      {
        ostream.write(object.toString().getBytes(encoding));
      }

      ostream.flush();
    }
    finally
    {
      ostream.close();
    }
  }
}
