/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;

import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.request.ServletResponseIF;

public class InputStreamResponse implements ResponseIF
{
  private InputStream istream;

  private String      contentType;

  private String      filename;

  /**
   * @param istream
   * @param contentType
   */
  public InputStreamResponse(InputStream istream, String contentType)
  {
    this(istream, contentType, null);
  }
  
  public InputStreamResponse(InputStream istream, String contentType, String filename)
  {
    this.istream = istream;
    this.contentType = contentType;
    this.filename = filename;
  }

  @Override
  public void handle(RequestManager manager) throws ServletException, IOException
  {
    ServletResponseIF resp = manager.getResp();
    resp.setStatus(200);
    resp.setContentType(this.contentType);
    
    if (this.filename != null)
    {
      resp.setHeader("Content-disposition", "attachment; filename=" + this.filename);
    }

    try
    {
      OutputStream ostream = resp.getOutputStream();

      try
      {
        IOUtils.copy(this.istream, ostream);
      }
      finally
      {
        ostream.close();
      }
    }
    finally
    {
      this.istream.close();
    }
  }

}
