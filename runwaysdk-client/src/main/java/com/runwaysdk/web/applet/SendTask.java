/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.web.applet;

import java.io.IOException;
import java.io.InputStream;

import com.healthmarketscience.rmiio.DirectRemoteInputStream;
import com.runwaysdk.web.applet.common.RemoteAppletAdapter;

/**
 * Background task used to upload a file to the server
 *
 * @author Justin Smethie
 */
public class SendTask extends SwingWorker
{
  /**
   * Remote RMI stream for streaming the file
   */
  private DirectRemoteInputStream remoteStream;

  /**
   * Local monitored stream
   */
  private InputStream             stream;

  /**
   * Remote server object
   */
  private RemoteAppletAdapter     remoteAdapter;

  /**
   * Session oid of the uploader
   */
  private String                  sessionId;

  /**
   * Name of the file
   */
  private String                  fileName;

  /**
   * File extension
   */
  private String                  extension;

  public SendTask(RemoteAppletAdapter remoteAdapter, String sessionId, InputStream stream, String fullName)
  {
    this.stream = stream;
    this.remoteAdapter = remoteAdapter;
    this.fileName = fullName;
    this.extension = "";
    this.sessionId = sessionId;
    this.remoteStream = new DirectRemoteInputStream(stream);

    int extensionInd = fullName.lastIndexOf(".");

    if (extensionInd != -1 && extensionInd != 0)
    {
      this.fileName = fullName.substring(0, extensionInd);

      if (extensionInd != fullName.length())
      {
        this.extension = fullName.substring(extensionInd + 1);
      }
    }
  }

  @Override
  public Object construct()
  {
    try
    {
      return remoteAdapter.newSecureFile(sessionId, fileName, extension, remoteStream);
    }
    catch (Exception e)
    {
      setThrowable(e);
    }

    return null;
  }

  @Override
  public void interrupt()
  {
    try
    {
      remoteStream.close();
      stream.close();
    }
    catch (IOException e)
    {
      // Do nothing ???
    }
    super.interrupt();
  }
}
