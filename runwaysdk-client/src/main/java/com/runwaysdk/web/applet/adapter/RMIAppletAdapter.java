/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.web.applet.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.healthmarketscience.rmiio.RemoteOutputStream;
import com.healthmarketscience.rmiio.SimpleRemoteInputStream;
import com.healthmarketscience.rmiio.SimpleRemoteOutputStream;
import com.runwaysdk.ClientSession;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.web.applet.common.RemoteAppletAdapter;

public class RMIAppletAdapter extends UnicastRemoteObject implements RemoteAppletAdapter
{
  /**
   *
   */
  private static final long serialVersionUID = -6440850588490076361L;

  protected RMIAppletAdapter() throws RemoteException
  {
    super();
  }

  /**
   * Creates a new globally viewable file on the server
   * 
   * @param sessionId
   *          The id of the session
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
  public String newFile(String sessionId, String path, String filename, String extension, RemoteInputStream stream)
  {
    try
    {
      ClientSession clientSession = ClientSession.getExistingSession(sessionId, new Locale[] { CommonProperties.getDefaultLocale() });
      ClientRequestIF clientRequest = clientSession.getRequest();
      BusinessDTO businessDTO = clientRequest.newFile(path, filename, extension, RemoteInputStreamClient.wrap(stream));

      JSONObject json = new JSONObject();
      json.put(EntityInfo.ID, businessDTO.getId());
      json.put(VaultFileInfo.FILE_NAME, businessDTO.getValue(VaultFileInfo.FILE_NAME));
      json.put(VaultFileInfo.EXTENSION, businessDTO.getValue(VaultFileInfo.EXTENSION));
      json.put(VaultFileInfo.FILE_SIZE, Integer.parseInt(businessDTO.getValue(VaultFileInfo.FILE_SIZE)));

      return json.toString();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * @param sessionId
   *          Session Id
   * @param attributeName
   *          Name of the file attribute to retrieve
   * @param type
   *          Fully qualified type name of the MdType defining the AttributeFile
   * @param fileId
   *          The id of the file to retrieve
   * 
   * @return The content of a secure file as a {@link InputStream} of data
   */
  public RemoteInputStream getSecureFile(String sessionId, String attributeName, String type, String fileId)
  {
    ClientSession clientSession = ClientSession.getExistingSession(sessionId, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = clientSession.getRequest();
    InputStream stream = clientRequest.getSecureFile(attributeName, type, fileId);

    return new SimpleRemoteInputStream(stream);
  }

  /**
   * @param sessionId
   *          The id of the session
   * @param fileId
   *          The id of the vault file to retrieve
   * 
   * @return The content of a secure file as a {@link InputStream} of data
   */
  public RemoteInputStream getSecureFile(String sessionId, String fileId)
  {
    ClientSession clientSession = ClientSession.getExistingSession(sessionId, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = clientSession.getRequest();
    InputStream stream = clientRequest.getSecureFile(fileId);

    return new SimpleRemoteInputStream(stream);
  }

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
   * @return The {@link BusinessDTO} representing the new vault file which was
   *         written
   */
  public String newSecureFile(String sessionId, String filename, String extension, RemoteInputStream stream)
  {
    try
    {
      ClientSession clientSession = ClientSession.getExistingSession(sessionId, new Locale[] { CommonProperties.getDefaultLocale() });
      ClientRequestIF clientRequest = clientSession.getRequest();
      BusinessDTO businessDTO = clientRequest.newSecureFile(filename, extension, RemoteInputStreamClient.wrap(stream));

      JSONObject json = new JSONObject();
      json.put(EntityInfo.ID, businessDTO.getId());
      json.put(VaultFileInfo.FILE_NAME, businessDTO.getValue(VaultFileInfo.FILE_NAME));
      json.put(VaultFileInfo.EXTENSION, businessDTO.getValue(VaultFileInfo.EXTENSION));
      json.put(VaultFileInfo.FILE_SIZE, Integer.parseInt(businessDTO.getValue(VaultFileInfo.FILE_SIZE)));

      return json.toString();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
    catch (NumberFormatException e)
    {
      throw new RuntimeException(e.getMessage());
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  public RemoteInputStream getFile(String sessionId, String fileId) throws RemoteException
  {
    ClientSession clientSession = ClientSession.getExistingSession(sessionId, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = clientSession.getRequest();
    InputStream stream = clientRequest.getFile(fileId);

    return new SimpleRemoteInputStream(stream);
  }

  public void testString(String string) throws RemoteException
  {
  }

  public RemoteOutputStream getStream() throws RemoteException
  {
    return new SimpleRemoteOutputStream(System.out);
  }
}
