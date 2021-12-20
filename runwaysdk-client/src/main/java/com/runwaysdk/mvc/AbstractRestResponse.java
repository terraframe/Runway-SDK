/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.mvc;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;

import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.request.ServletRequestIF;
import com.runwaysdk.request.ServletResponseIF;

public abstract class AbstractRestResponse implements ResponseIF
{
  private int status;

  public AbstractRestResponse(int status)
  {
    this.status = status;
  }

  protected abstract Object serialize();

  @Override
  public void handle(RequestManager manager) throws ServletException, IOException
  {
    ServletRequestIF req = manager.getReq();
    ServletResponseIF resp = manager.getResp();

    String encoding = ( req.getCharacterEncoding() != null ? req.getCharacterEncoding() : "UTF-8" );

    Object object = this.serialize();

    resp.setStatus(this.status);
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
