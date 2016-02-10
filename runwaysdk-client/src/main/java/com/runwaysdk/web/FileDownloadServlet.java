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
package com.runwaysdk.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.constants.AdminConstants;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.util.FileIO;

public abstract class FileDownloadServlet extends HttpServlet
{
  /**
   * 
   */
  private static final long serialVersionUID = 4005252300034582789L;

  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
  {
    ClientRequestIF clientRequest = (ClientRequestIF)req.getAttribute(ClientConstants.CLIENTREQUEST);
    
    String type = (String) req.getParameter(ClientConstants.VAULT_ATTRIBUTE_FILE_TYPE);
    String attributeName = (String) req.getParameter(ClientConstants.VAULT_ATTRIBUTE_FILE_NAME); 

    String vaultId = req.getParameter(AdminConstants.FILE_DOWNLOAD_ID);

    MutableDTO fileDTO;
    InputStream input;
    
    if (type != null && !type.trim().equals("") &&
        attributeName != null && !attributeName.trim().equals(""))
    {
      fileDTO = clientRequest.getVaultFileDTO(type, attributeName, vaultId);
      input = clientRequest.getSecureFile(attributeName, type, vaultId);
    }
    else
    {
      fileDTO = clientRequest.get(vaultId); 
      input = getFileBytes(clientRequest, (BusinessDTO) fileDTO);
    }

    String fileName = fileDTO.getValue(VaultFileInfo.FILE_NAME);
    String ext = fileDTO.getValue(VaultFileInfo.EXTENSION);
    
    // write the bytes to the outgoing stream
    String fullName = fileName +"."+ext;
    res.addHeader("Content-Disposition", "attachment;filename=\""+fullName+"\"");
    ServletOutputStream stream  = res.getOutputStream();
    FileIO.write(stream, input);    
  }
//getVaultFileDTO(String sessionId, String type, String attributeName, String fileId)
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    doPost(req, resp);
  }
  
  /**
   * Gets the file bytes for the download.
   * 
   * @param clientRequest
   * @param fileDTO
   * @return
   */
  /**
   * Gets the file bytes by calling FileManager.getSecureFile().
   */
  protected abstract InputStream getFileBytes(ClientRequestIF clientRequest, BusinessDTO fileDTO);
}
