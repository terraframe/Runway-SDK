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
package com.runwaysdk.web.applet.common;

import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteOutputStream;

public interface RemoteAppletAdapter extends Remote
{
  /**
   * @param sessionId
   *          The oid of the session
   * @param fileId
   *          The oid of the file to retrieve
   * 
   * @return The content of a file as a {@link InputStream} of data
   */
  public RemoteInputStream getFile(String sessionId, String fileId) throws RemoteException;

  /**
   * Creates a new globally viewable file on the server
   * 
   * @param sessionId
   *          The oid of the session
   * @param path
   *          The fully qualified path on the server to put the file
   * @param filename
   *          The name of the file
   * @param extension
   *          The extension of the file
   * @param stream
   *          An {@link InputStream} containing the contents to write
   * @return
   */
  public String newFile(String sessionId, String path, String filename, String extension, RemoteInputStream stream) throws RemoteException;

  /**
   * @param sessionId
   *          Session Id
   * @param attributeName
   *          Name of the file attribute to retrieve
   * @param type
   *          Fully qualified type name of the MdType defining the AttributeFile
   * @param fileId
   *          The oid of the file to retrieve
   * 
   * @return The content of a secure file as a {@link InputStream} of data
   */
  public RemoteInputStream getSecureFile(String sessionId, String attributeName, String type, String fileId) throws RemoteException;

  /**
   * @param sessionId
   *          The oid of the session
   * @param fileId
   *          The oid of the vault file to retrieve
   * 
   * @return The content of a secure file as a {@link InputStream} of data
   */
  public RemoteInputStream getSecureFile(String sessionId, String fileId) throws RemoteException;

  /**
   * Create a new secure file in a file valult on the sever
   * 
   * @param sessionId
   *          sessionId
   * @param filename
   *          Name of the file to create
   * @param extension
   *          Extension of the file to create
   * @param Stream
   *          containing the contents to be written
   * 
   * @return The oid representing the new vault file which was written
   */
  public String newSecureFile(String sessionId, String filename, String extension, RemoteInputStream stream) throws RemoteException;

  public RemoteOutputStream getStream() throws RemoteException;
}
